const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";
const OWNER_UID = "owner_velora";
const OWNER_EMAIL = "kk8147561@gmail.com";

const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

function buildConsultationPayload(uid, email = "alice@example.com") {
  const now = new Date(Date.now() - 2000);
  return {
    userId: uid,
    userEmail: email,
    fullName: "Alice Sharma",
    phoneNumber: "9876543210",
    city: "Lucknow, UP",
    propertyType: "3 BHK",
    serviceScope: "Full Home & Lighting",
    designReference: "Champagne Island Modular Kitchen",
    notes: "Need warm architectural lighting quote.",
    studioOwnerPhone: "8084189925",
    status: "pending",
    createdAt: now,
    updatedAt: now,
  };
}

test("Unauthenticated user: denied reading or writing saved_ideas and consultations", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("saved_ideas").get()
  );
  await assertFails(unauthDb.collection("consultations").get());
  await assertFails(
    unauthDb.collection("consultations").doc("c1").set(buildConsultationPayload(ALICE_UID))
  );
});

test("User-scoped saved_ideas: owner can create/list/delete; other user denied", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const now = new Date(Date.now() - 2000);

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("saved_ideas")
      .doc("kitchen_island_onyx")
      .set({
        userId: ALICE_UID,
        ideaId: "kitchen_island_onyx",
        title: "Champagne Island Modular Kitchen",
        category: "Kitchens",
        createdAt: now,
      })
  );

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("saved_ideas").get()
  );

  // Cross-user isolation
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("saved_ideas").get()
  );
  await assertFails(
    bobDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("saved_ideas")
      .doc("kitchen_island_onyx")
      .delete()
  );

  // Reject @DocumentId `id` field or ghost field
  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("saved_ideas")
      .doc("living_1")
      .set({
        id: "living_1",
        userId: ALICE_UID,
        ideaId: "living_1",
        title: "Living",
        category: "Living",
        createdAt: now,
      })
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("saved_ideas")
      .doc("kitchen_island_onyx")
      .delete()
  );
});

test("Customer consultation: create, scoped query, pending note update; reject tampering & unfiltered list", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();

  // Create valid consultation targeting Velora Home 8084189925
  await assertSucceeds(
    aliceDb
      .collection("consultations")
      .doc("consult_1")
      .set(buildConsultationPayload(ALICE_UID))
  );

  // Scoped query succeeds
  await assertSucceeds(
    aliceDb.collection("consultations").where("userId", "==", ALICE_UID).get()
  );

  // Unfiltered query by customer fails
  await assertFails(aliceDb.collection("consultations").get());

  // Cross-user read & update fail
  await assertFails(bobDb.collection("consultations").doc("consult_1").get());
  await assertFails(
    bobDb.collection("consultations").doc("consult_1").update({
      notes: "Tampered",
      updatedAt: new Date(Date.now() - 500),
    })
  );

  // Customer cannot self-approve status or spoof studioOwnerPhone or inject ghost fields
  await assertFails(
    aliceDb.collection("consultations").doc("consult_1").update({
      status: "completed",
      updatedAt: new Date(Date.now() - 500),
    })
  );
  await assertFails(
    aliceDb.collection("consultations").doc("consult_2").set({
      ...buildConsultationPayload(ALICE_UID),
      studioOwnerPhone: "9999999999",
    })
  );
  await assertFails(
    aliceDb.collection("consultations").doc("consult_3").set({
      ...buildConsultationPayload(ALICE_UID),
      isVerified: true,
    })
  );

  // Customer can update own notes while pending
  await assertSucceeds(
    aliceDb.collection("consultations").doc("consult_1").update({
      notes: "Prefer warm 2700K cove lights",
      updatedAt: new Date(Date.now() - 500),
    })
  );
});

test("Velora Home owner (verified email): can list all consultations & update status; unverified email spoof rejected", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb
      .collection("consultations")
      .doc("consult_1")
      .set(buildConsultationPayload(ALICE_UID))
  );

  // Spoofed unverified owner token must fail
  const spoofedOwnerDb = testEnv
    .authenticatedContext("spoof_uid", {
      email: OWNER_EMAIL,
      email_verified: false,
    })
    .firestore();
  await assertFails(spoofedOwnerDb.collection("consultations").get());
  await assertFails(
    spoofedOwnerDb.collection("consultations").doc("consult_1").update({
      status: "contacted",
      updatedAt: new Date(Date.now() - 500),
    })
  );

  // Verified Velora Home owner succeeds
  const verifiedOwnerDb = testEnv
    .authenticatedContext(OWNER_UID, {
      email: OWNER_EMAIL,
      email_verified: true,
    })
    .firestore();

  await assertSucceeds(verifiedOwnerDb.collection("consultations").get());
  await assertSucceeds(
    verifiedOwnerDb.collection("consultations").doc("consult_1").update({
      status: "contacted",
      updatedAt: new Date(Date.now() - 500),
    })
  );

  // Once status is 'contacted', customer can no longer mutate notes (terminal/locked from customer edits)
  await assertFails(
    aliceDb.collection("consultations").doc("consult_1").update({
      notes: "Late edit",
      updatedAt: new Date(Date.now() - 200),
    })
  );
});
