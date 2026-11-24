package com.example.data.remote

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@Keep
@IgnoreExtraProperties
data class FirestoreSavedIdea(
    @DocumentId val id: String = "",
    val userId: String = "",
    val ideaId: String = "",
    val title: String = "",
    val category: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class FirestoreConsultation(
    @DocumentId val id: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val city: String = "",
    val propertyType: String = "",
    val serviceScope: String = "",
    val designReference: String = "",
    val notes: String = "",
    val studioOwnerPhone: String = "8084189925",
    val status: String = "pending",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
