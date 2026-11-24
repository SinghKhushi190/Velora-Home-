package com.example.data.remote

import android.content.Context
import com.example.R
import com.example.data.model.StudioContactConfig
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class VeloraFirestoreRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    fun observeSavedIdeas(userId: String): Flow<List<FirestoreSavedIdea>> = flow {
        val uid = requireUserId()
        check(uid == userId) { "Signed-in user mismatch." }
        val path = "users/$uid/saved_ideas"
        emitAll(
            db.collection("users").document(uid).collection("saved_ideas")
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        FirestoreSavedIdea::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    ).sortedByDescending { it.createdAt ?: Timestamp(0, 0) }
                }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val exceptionToLog = firestoreError ?: (error as? Exception)
                    if (exceptionToLog != null && auth.currentUser?.uid == uid) {
                        handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                    }
                    throw error
                }
        )
    }

    suspend fun toggleSavedIdea(
        userId: String,
        ideaId: String,
        title: String,
        category: String,
        currentlySaved: Boolean
    ): Result<Unit> {
        val docPath = "users/$userId/saved_ideas/$ideaId"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user mismatch; refusing cross-user write." }
            require(ideaId.isNotBlank() && ideaId.length <= 128) { "Invalid ideaId." }
            val docRef = db.collection("users").document(uid).collection("saved_ideas").document(ideaId)
            if (currentlySaved) {
                docRef.delete().await()
            } else {
                val payload = mapOf(
                    "userId" to uid,
                    "ideaId" to ideaId,
                    "title" to title.take(120).ifBlank { ideaId },
                    "category" to category.take(60).ifBlank { "Design" },
                    "createdAt" to FieldValue.serverTimestamp()
                )
                docRef.set(payload).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(
                e,
                if (currentlySaved) OperationType.DELETE else OperationType.CREATE,
                docPath
            )
            Result.failure(e)
        }
    }

    fun observeConsultations(userId: String, isOwnerAdmin: Boolean): Flow<List<FirestoreConsultation>> = flow {
        val uid = requireUserId()
        check(uid == userId) { "Signed-in user mismatch." }
        val path = "consultations"
        val query = if (isOwnerAdmin) {
            db.collection(path)
        } else {
            db.collection(path).whereEqualTo("userId", uid)
        }
        emitAll(
            query.snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        FirestoreConsultation::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    ).sortedByDescending { it.createdAt ?: Timestamp(0, 0) }
                }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val exceptionToLog = firestoreError ?: (error as? Exception)
                    if (exceptionToLog != null && auth.currentUser?.uid == uid) {
                        handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                    }
                    throw error
                }
        )
    }

    suspend fun submitConsultation(
        userId: String,
        userEmail: String,
        fullName: String,
        phoneNumber: String,
        city: String,
        propertyType: String,
        serviceScope: String,
        designReference: String,
        notes: String
    ): Result<Unit> {
        var docPath = "consultations"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user mismatch; refusing cross-user write." }
            val docRef = db.collection("consultations").document()
            docPath = docRef.path
            val payload = mapOf(
                "userId" to uid,
                "userEmail" to userEmail.take(150),
                "fullName" to fullName.trim().take(80),
                "phoneNumber" to phoneNumber.trim().take(20),
                "city" to city.trim().ifBlank { StudioContactConfig.STUDIO_CITY }.take(80),
                "propertyType" to propertyType.take(40),
                "serviceScope" to serviceScope.take(60),
                "designReference" to designReference.trim().take(160),
                "notes" to notes.trim().take(500),
                "studioOwnerPhone" to StudioContactConfig.STUDIO_PHONE_RAW,
                "status" to "pending",
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docPath)
            Result.failure(e)
        }
    }

    suspend fun updateConsultationStatus(consultationId: String, newStatus: String): Result<Unit> {
        val docPath = "consultations/$consultationId"
        return try {
            requireUserId()
            require(consultationId.isNotBlank()) { "Consultation ID must not be blank." }
            require(newStatus in listOf("pending", "contacted", "scheduled", "completed")) {
                "Invalid status."
            }
            db.collection("consultations").document(consultationId)
                .update(
                    mapOf(
                        "status" to newStatus,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                )
                .await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docPath)
            Result.failure(e)
        }
    }

    suspend fun deleteConsultation(consultationId: String): Result<Unit> {
        val docPath = "consultations/$consultationId"
        return try {
            requireUserId()
            require(consultationId.isNotBlank()) { "Consultation ID must not be blank." }
            db.collection("consultations").document(consultationId).delete().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docPath)
            Result.failure(e)
        }
    }
}
