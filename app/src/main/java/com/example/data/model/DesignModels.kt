package com.example.data.model

enum class RoomCategory(val label: String, val shortLabel: String) {
    ALL("All Spaces", "All"),
    KITCHENS("Modular Kitchens", "Kitchens"),
    LIVING_ROOM("Living Rooms", "Living"),
    BEDROOM("Bedrooms", "Bedrooms"),
    WARDROBES("Wardrobes", "Wardrobes"),
    BATHROOMS("Bathrooms & Vanity", "Bathrooms"),
    DINING_BAR("Dining & Bar", "Dining"),
    STUDY_FOYER("Study & Foyers", "Study & Foyer")
}

enum class LightingSeries(val code: String, val label: String, val subtitle: String) {
    ALL("ALL", "All Lighting", "Complete illumination anthology"),
    ARCHITECTURAL("01", "Architectural", "Light as Structure"),
    DECORATIVE("02", "Decorative", "Objects of Desire"),
    FUNCTIONAL("03", "Functional", "Light with Purpose"),
    SMART("04", "Smart & Innovative", "The Future of Light")
}

data class CraftedHighlight(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val tag: String
)

data class DesignIdea(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: RoomCategory,
    val styleTag: String,
    val dimensions: String,
    val estimatedBudget: String,
    val imageUrl: String,
    val description: String,
    val lightingPairing: String,
    val materials: List<String>,
    val isFeatured: Boolean = false
)

data class LightingConcept(
    val id: String,
    val series: LightingSeries,
    val numberCode: String,
    val tag: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val idealFor: String,
    val colorTemp: String
)

object StudioContactConfig {
    const val STUDIO_NAME = "Velora Home"
    const val STUDIO_OWNER_ATTRIBUTION = "Owned & Operated by Velora Home"
    const val STUDIO_CITY = "Lucknow, Uttar Pradesh"
    const val STUDIO_PHONE_RAW = "8084189925"
    const val STUDIO_PHONE_DISPLAY = "+91 8084189925"
    const val STUDIO_HOURS = "Mon – Sun • 10:00 AM – 8:00 PM"
    const val STUDIO_WHATSAPP_URL = "https://wa.me/918084189925"
}
