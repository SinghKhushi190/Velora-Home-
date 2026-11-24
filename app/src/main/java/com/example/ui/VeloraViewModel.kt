package com.example.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.catalog.VeloraCatalog
import com.example.data.model.DesignIdea
import com.example.data.model.LightingConcept
import com.example.data.model.LightingSeries
import com.example.data.model.RoomCategory
import com.example.data.model.StudioContactConfig
import com.example.data.remote.FirestoreConsultation
import com.example.data.remote.VeloraFirestoreRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class EstimatorHomeSize(val label: String, val multiplier: Double) {
    TWO_BHK("2 BHK", 1.0),
    THREE_BHK("3 BHK", 1.45),
    VILLA_4BHK("4 BHK / Villa", 2.1)
}

enum class EstimatorScope(val label: String, val baseLakhs: Double, val weeks: Int) {
    FULL_HOME("Full Home Interiors", 5.5, 10),
    KITCHEN_WARDROBE("Modular Kitchen & Wardrobes", 3.2, 6),
    LIGHTING_PLAN("Vol. II Architectural Lighting", 1.8, 4),
    LIVING_DINING("Living & Dining Makeover", 2.8, 5)
}

enum class EstimatorTier(val label: String, val factor: Double, val finishSummary: String) {
    ESSENTIAL("Essential", 1.0, "Matte laminates, warm cove LEDs & soft-close hardware"),
    SIGNATURE("Signature Gold", 1.35, "Acrylic/veneer finishes, brushed brass & architectural troughs"),
    BESPOKE("Bespoke Luxury", 1.8, "Backlit onyx, Italian marble, circadian & smart RGBW scenes")
}

data class ConsultationFormState(
    val fullName: String = "",
    val phoneNumber: String = "",
    val city: String = "Lucknow, UP",
    val propertyType: String = "3 BHK",
    val serviceScope: String = "Full Home & Lighting",
    val designReference: String = "",
    val notes: String = "",
    val errorMessage: String? = null,
    val isSubmitting: Boolean = false,
    val submittedSuccessName: String? = null
)

data class VeloraUiState(
    val currentUserEmail: String = "",
    val isOwnerAdmin: Boolean = false,
    val selectedCategory: RoomCategory = RoomCategory.ALL,
    val searchQuery: String = "",
    val selectedLightingSeries: LightingSeries = LightingSeries.ALL,
    val filteredIdeas: List<DesignIdea> = VeloraCatalog.designIdeas,
    val filteredLighting: List<LightingConcept> = VeloraCatalog.lightingConcepts,
    val savedIds: Set<String> = emptySet(),
    val consultations: List<FirestoreConsultation> = emptyList(),
    val cloudErrorMessage: String? = null,
    val estimatorHomeSize: EstimatorHomeSize = EstimatorHomeSize.THREE_BHK,
    val estimatorScope: EstimatorScope = EstimatorScope.FULL_HOME,
    val estimatorTier: EstimatorTier = EstimatorTier.SIGNATURE,
    val isQuoteSheetOpen: Boolean = false,
    val formState: ConsultationFormState = ConsultationFormState()
) {
    val estimatedRangeText: String
        get() {
            val mid = estimatorScope.baseLakhs * estimatorHomeSize.multiplier * estimatorTier.factor
            val low = (mid * 0.88).let { ((it * 10).toInt() / 10.0) }
            val high = (mid * 1.15).let { ((it * 10).toInt() / 10.0) }
            return "₹$low L – ₹$high L"
        }

    val estimatedTimelineText: String
        get() {
            val baseWeeks = estimatorScope.weeks
            val extra = if (estimatorHomeSize == EstimatorHomeSize.VILLA_4BHK) 2 else 0
            return "${baseWeeks + extra}–${baseWeeks + extra + 2} Weeks Turnkey"
        }
}

class VeloraViewModel(
    private val repository: VeloraFirestoreRepository,
    private val currentUserId: String,
    private val currentUserEmail: String,
    isEmailVerified: Boolean
) : ViewModel() {

    val isOwnerAdmin: Boolean =
        isEmailVerified && currentUserEmail.equals(OWNER_EMAIL, ignoreCase = true)

    private val _selectedCategory = MutableStateFlow(RoomCategory.ALL)
    private val _searchQuery = MutableStateFlow("")
    private val _selectedLightingSeries = MutableStateFlow(LightingSeries.ALL)
    private val _estimatorHomeSize = MutableStateFlow(EstimatorHomeSize.THREE_BHK)
    private val _estimatorScope = MutableStateFlow(EstimatorScope.FULL_HOME)
    private val _estimatorTier = MutableStateFlow(EstimatorTier.SIGNATURE)
    private val _isQuoteSheetOpen = MutableStateFlow(false)
    private val _formState = MutableStateFlow(ConsultationFormState())
    private val _cloudError = MutableStateFlow<String?>(null)

    private val savedIdsFlow: StateFlow<Set<String>> = repository.observeSavedIdeas(currentUserId)
        .map { list -> list.map { it.ideaId }.toSet() }
        .catch { error ->
            val firestoreError = generateSequence(error) { it.cause }
                .filterIsInstance<FirebaseFirestoreException>()
                .firstOrNull()
            if (firestoreError == null && error is CancellationException) throw error
            val rootError = firestoreError ?: error
            Log.w(TAG, "Error observing saved ideas", rootError)
            _cloudError.value = rootError.localizedMessage ?: "Failed to load saved ideas."
            emit(emptySet())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), emptySet())

    private val consultationsFlow: StateFlow<List<FirestoreConsultation>> =
        repository.observeConsultations(currentUserId, isOwnerAdmin)
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                val rootError = firestoreError ?: error
                Log.w(TAG, "Error observing consultations", rootError)
                _cloudError.value = rootError.localizedMessage ?: "Failed to load consultations."
                emit(emptyList())
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), emptyList())

    private val filterFlow = combine(
        _selectedCategory,
        _searchQuery,
        _selectedLightingSeries
    ) { category, query, series ->
        Triple(category, query, series)
    }

    private val estimatorFlow = combine(
        _estimatorHomeSize,
        _estimatorScope,
        _estimatorTier
    ) { size, scope, tier ->
        Triple(size, scope, tier)
    }

    private val modalAndErrorFlow = combine(
        _isQuoteSheetOpen,
        _formState,
        _cloudError
    ) { open, form, error ->
        Triple(open, form, error)
    }

    val uiState: StateFlow<VeloraUiState> = combine(
        filterFlow,
        savedIdsFlow,
        consultationsFlow,
        estimatorFlow,
        modalAndErrorFlow
    ) { (category, query, series), savedSet, consultations, (homeSize, scope, tier), (sheetOpen, form, cloudError) ->
        val normalizedQuery = query.trim().lowercase()
        val ideas = VeloraCatalog.designIdeas.filter { idea ->
            val matchesCategory = category == RoomCategory.ALL || idea.category == category
            val matchesQuery = normalizedQuery.isEmpty() ||
                idea.title.lowercase().contains(normalizedQuery) ||
                idea.subtitle.lowercase().contains(normalizedQuery) ||
                idea.styleTag.lowercase().contains(normalizedQuery) ||
                idea.category.label.lowercase().contains(normalizedQuery) ||
                idea.materials.any { it.lowercase().contains(normalizedQuery) }
            matchesCategory && matchesQuery
        }
        val lighting = VeloraCatalog.lightingConcepts.filter { concept ->
            series == LightingSeries.ALL || concept.series == series
        }
        VeloraUiState(
            currentUserEmail = currentUserEmail,
            isOwnerAdmin = isOwnerAdmin,
            selectedCategory = category,
            searchQuery = query,
            selectedLightingSeries = series,
            filteredIdeas = ideas,
            filteredLighting = lighting,
            savedIds = savedSet,
            consultations = consultations,
            cloudErrorMessage = cloudError,
            estimatorHomeSize = homeSize,
            estimatorScope = scope,
            estimatorTier = tier,
            isQuoteSheetOpen = sheetOpen,
            formState = form
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = VeloraUiState(
            currentUserEmail = currentUserEmail,
            isOwnerAdmin = isOwnerAdmin
        )
    )

    fun selectCategory(category: RoomCategory) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectLightingSeries(series: LightingSeries) {
        _selectedLightingSeries.value = series
    }

    fun toggleSaved(itemId: String) {
        val currentlySaved = uiState.value.savedIds.contains(itemId)
        val idea = VeloraCatalog.designIdeas.find { it.id == itemId }
        val lighting = VeloraCatalog.lightingConcepts.find { it.id == itemId }
        val title = idea?.title ?: lighting?.title ?: itemId
        val category = idea?.category?.shortLabel ?: lighting?.series?.label ?: "Design"

        viewModelScope.launch {
            val result = repository.toggleSavedIdea(
                userId = currentUserId,
                ideaId = itemId,
                title = title,
                category = category,
                currentlySaved = currentlySaved
            )
            result.onFailure { error ->
                _cloudError.value = error.localizedMessage ?: "Unable to update shortlist."
            }
        }
    }

    fun setEstimatorHomeSize(size: EstimatorHomeSize) {
        _estimatorHomeSize.value = size
    }

    fun setEstimatorScope(scope: EstimatorScope) {
        _estimatorScope.value = scope
    }

    fun setEstimatorTier(tier: EstimatorTier) {
        _estimatorTier.value = tier
    }

    fun openQuoteSheet(designReference: String = "", serviceScope: String? = null) {
        _formState.update { current ->
            current.copy(
                designReference = designReference,
                serviceScope = serviceScope ?: current.serviceScope,
                errorMessage = null,
                submittedSuccessName = null
            )
        }
        _isQuoteSheetOpen.value = true
    }

    fun closeQuoteSheet() {
        _isQuoteSheetOpen.value = false
    }

    fun updateFormName(value: String) {
        _formState.update { it.copy(fullName = value, errorMessage = null) }
    }

    fun updateFormPhone(value: String) {
        val digitsOnly = value.filter { it.isDigit() || it == '+' || it == ' ' }.take(15)
        _formState.update { it.copy(phoneNumber = digitsOnly, errorMessage = null) }
    }

    fun updateFormCity(value: String) {
        _formState.update { it.copy(city = value, errorMessage = null) }
    }

    fun updateFormPropertyType(value: String) {
        _formState.update { it.copy(propertyType = value) }
    }

    fun updateFormServiceScope(value: String) {
        _formState.update { it.copy(serviceScope = value) }
    }

    fun updateFormNotes(value: String) {
        _formState.update { it.copy(notes = value) }
    }

    fun dismissSuccessBanner() {
        _formState.update { it.copy(submittedSuccessName = null) }
    }

    fun dismissCloudError() {
        _cloudError.value = null
    }

    fun submitConsultation() {
        val current = _formState.value
        val trimmedName = current.fullName.trim()
        val phoneDigits = current.phoneNumber.filter { it.isDigit() }
        val trimmedCity = current.city.trim().ifBlank { StudioContactConfig.STUDIO_CITY }

        if (trimmedName.length < 2 || trimmedName.length > 80) {
            _formState.update { it.copy(errorMessage = "Please enter your name (2–80 characters).") }
            return
        }
        if (phoneDigits.length < 10 || current.phoneNumber.trim().length > 20) {
            _formState.update {
                it.copy(errorMessage = "Please enter your 10-digit mobile number so Velora Home (+91 8084189925) can reach you.")
            }
            return
        }
        if (trimmedCity.length < 2 || trimmedCity.length > 80) {
            _formState.update { it.copy(errorMessage = "Please enter a valid city or locality.") }
            return
        }
        if (current.notes.trim().length > 500) {
            _formState.update { it.copy(errorMessage = "Project notes must be 500 characters or fewer.") }
            return
        }

        viewModelScope.launch {
            _formState.update { it.copy(isSubmitting = true, errorMessage = null) }
            val result = repository.submitConsultation(
                userId = currentUserId,
                userEmail = currentUserEmail,
                fullName = trimmedName,
                phoneNumber = current.phoneNumber.trim(),
                city = trimmedCity,
                propertyType = current.propertyType,
                serviceScope = current.serviceScope,
                designReference = current.designReference,
                notes = current.notes
            )
            result.onSuccess {
                _formState.update {
                    ConsultationFormState(
                        fullName = "",
                        phoneNumber = "",
                        city = "Lucknow, UP",
                        propertyType = current.propertyType,
                        serviceScope = current.serviceScope,
                        designReference = "",
                        notes = "",
                        isSubmitting = false,
                        submittedSuccessName = trimmedName
                    )
                }
            }.onFailure { error ->
                _formState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = error.localizedMessage ?: "Failed to submit consultation request."
                    )
                }
            }
        }
    }

    fun updateLeadStatus(consultationId: String, newStatus: String) {
        viewModelScope.launch {
            val result = repository.updateConsultationStatus(consultationId, newStatus)
            result.onFailure { error ->
                _cloudError.value = error.localizedMessage ?: "Unable to update lead status."
            }
        }
    }

    fun deleteConsultation(consultationId: String) {
        viewModelScope.launch {
            val result = repository.deleteConsultation(consultationId)
            result.onFailure { error ->
                _cloudError.value = error.localizedMessage ?: "Unable to remove consultation."
            }
        }
    }

    private companion object {
        const val TAG = "VeloraVM"
        const val STOP_TIMEOUT_MILLIS = 5000L
        const val OWNER_EMAIL = "kk8147561@gmail.com"
    }
}
