package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.catalog.VeloraCatalog
import com.example.data.model.DesignIdea
import com.example.data.model.LightingConcept
import com.example.data.model.StudioContactConfig
import com.example.data.remote.FirestoreConsultation
import com.example.ui.EstimatorHomeSize
import com.example.ui.EstimatorScope
import com.example.ui.EstimatorTier
import com.example.ui.VeloraUiState
import com.example.ui.components.ConsultationFormContent
import com.example.ui.components.EditorialHeading
import com.example.ui.components.SectionHeaderBlock
import com.example.ui.components.VeloraAsyncImage
import com.example.ui.components.openStudioDialer
import com.example.ui.components.openWhatsAppStudio
import com.example.ui.components.shareTextWithAnyDevice
import com.example.ui.components.shareVeloraApp
import com.example.ui.theme.CormorantGaramondFontFamily
import com.example.ui.theme.VeloraGold
import com.example.ui.theme.VeloraGoldDark
import com.example.ui.theme.VeloraGoldLight
import com.example.ui.theme.VeloraInk
import com.example.ui.theme.VeloraObsidianCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StudioScreen(
    uiState: VeloraUiState,
    onSignOut: () -> Unit,
    onDismissCloudError: () -> Unit,
    onSelectHomeSize: (EstimatorHomeSize) -> Unit,
    onSelectScope: (EstimatorScope) -> Unit,
    onSelectTier: (EstimatorTier) -> Unit,
    onApplyEstimateToQuote: (String) -> Unit,
    onToggleSave: (String) -> Unit,
    onIdeaClick: (String) -> Unit,
    onLightingClick: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onPropertyTypeChange: (String) -> Unit,
    onScopeChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSubmitConsultation: () -> Unit,
    onDismissSuccess: () -> Unit,
    onUpdateLeadStatus: (String, String) -> Unit,
    onDeleteConsultation: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val savedIdeas: List<DesignIdea> = VeloraCatalog.designIdeas.filter { uiState.savedIds.contains(it.id) }
    val savedLighting: List<LightingConcept> = VeloraCatalog.lightingConcepts.filter { uiState.savedIds.contains(it.id) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header + Account / Owner Status Bar
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "VELORA STUDIO & CONCIERGE",
                    style = MaterialTheme.typography.labelMedium,
                    color = VeloraGold
                )
                EditorialHeading(
                    prefix = "Plan Your",
                    italicAccent = "Sanctuary",
                    fontSizeSp = 32
                )
                Text(
                    text = "Estimate budgets, sync your shortlist across devices, or connect with Velora Home's official owner line at ${StudioContactConfig.STUDIO_PHONE_DISPLAY}.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = if (uiState.isOwnerAdmin) VeloraInk else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, VeloraGold.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.VerifiedUser,
                                contentDescription = null,
                                tint = VeloraGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (uiState.isOwnerAdmin) {
                                        "VELORA HOME OWNER INBOX"
                                    } else {
                                        "SIGNED IN ACCOUNT"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (uiState.isOwnerAdmin) VeloraGoldLight else VeloraGoldDark
                                )
                                Text(
                                    text = uiState.currentUserEmail.ifBlank { "Google Account" },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (uiState.isOwnerAdmin) Color.White else MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        OutlinedButton(
                            onClick = onSignOut,
                            border = BorderStroke(
                                1.dp,
                                if (uiState.isOwnerAdmin) VeloraGold.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline
                            ),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("sign_out_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Sign out",
                                tint = if (uiState.isOwnerAdmin) VeloraGoldLight else VeloraGoldDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Sign Out",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (uiState.isOwnerAdmin) VeloraGoldLight else VeloraGoldDark
                            )
                        }
                    }
                }

                if (uiState.cloudErrorMessage != null) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.errorContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = uiState.cloudErrorMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = onDismissCloudError) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Dismiss error",
                                    tint = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                }

                // Multi-Device Sync & Cross-Device Sharing Card
                val shareablePlanSummary = buildString {
                    appendLine("Velora Home Project Plan & Estimate")
                    appendLine("Configuration: ${uiState.estimatorHomeSize.label}")
                    appendLine("Scope: ${uiState.estimatorScope.label}")
                    appendLine("Finish Tier: ${uiState.estimatorTier.label}")
                    appendLine("Estimated Investment: ${uiState.estimatedRangeText} (${uiState.estimatedTimelineText})")
                    if (savedIdeas.isNotEmpty()) {
                        appendLine()
                        appendLine("Saved Room Ideas:")
                        savedIdeas.forEach { appendLine("• ${it.title} (${it.estimatedBudget})") }
                    }
                    if (savedLighting.isNotEmpty()) {
                        appendLine()
                        appendLine("Saved Vol. II Lighting:")
                        savedLighting.forEach { appendLine("• ${it.title} (${it.colorTemp})") }
                    }
                    appendLine()
                    appendLine("WhatsApp Studio: ${StudioContactConfig.STUDIO_WHATSAPP_URL}")
                    append("Official Studio Line (${StudioContactConfig.STUDIO_OWNER_ATTRIBUTION}): ${StudioContactConfig.STUDIO_PHONE_DISPLAY}")
                }

                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = VeloraGoldLight.copy(alpha = 0.35f),
                    border = BorderStroke(1.dp, VeloraGold.copy(alpha = 0.38f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Devices,
                                contentDescription = null,
                                tint = VeloraGoldDark,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "MULTI-DEVICE SYNC & SHARING",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = VeloraGoldDark
                                )
                                Text(
                                    text = "Sign in with Google on any Android device or share with family & designers",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { shareVeloraApp(context) },
                                border = BorderStroke(1.dp, VeloraGold),
                                shape = RoundedCornerShape(24.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("studio_share_app_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Share,
                                    contentDescription = "Share Velora Home app link",
                                    tint = VeloraGoldDark,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Share App",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = VeloraGoldDark,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Button(
                                onClick = {
                                    shareTextWithAnyDevice(
                                        context = context,
                                        subject = "Velora Home Estimate & Shortlist",
                                        body = shareablePlanSummary
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = VeloraGold,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(24.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                                modifier = Modifier
                                    .weight(1.15f)
                                    .testTag("studio_share_plan_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Share,
                                    contentDescription = "Share estimate and shortlist",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Share Plan",
                                    style = MaterialTheme.typography.labelMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { openWhatsAppStudio(context, shareablePlanSummary) },
                            border = BorderStroke(1.dp, VeloraGold.copy(alpha = 0.7f)),
                            shape = RoundedCornerShape(24.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("studio_whatsapp_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ChatBubbleOutline,
                                contentDescription = "Send plan via WhatsApp to ${StudioContactConfig.STUDIO_PHONE_RAW}",
                                tint = VeloraGoldDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Send Plan via WhatsApp (${StudioContactConfig.STUDIO_PHONE_DISPLAY})",
                                style = MaterialTheme.typography.labelLarge,
                                color = VeloraGoldDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // 1. Interior & Lighting Cost Estimator
        item {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shadowElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = VeloraGoldLight,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.Calculate,
                                    contentDescription = null,
                                    tint = VeloraGoldDark
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "INSTANT COST ESTIMATOR",
                                style = MaterialTheme.typography.labelSmall,
                                color = VeloraGoldDark
                            )
                            Text(
                                text = "Interior & Lighting Budget",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontFamily = CormorantGaramondFontFamily
                                )
                            )
                        }
                    }

                    Text("Home Configuration", style = MaterialTheme.typography.titleSmall)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EstimatorHomeSize.entries.forEach { size ->
                            FilterChip(
                                selected = uiState.estimatorHomeSize == size,
                                onClick = { onSelectHomeSize(size) },
                                label = { Text(size.label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = VeloraGold,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Text("Project Scope", style = MaterialTheme.typography.titleSmall)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EstimatorScope.entries.forEach { scope ->
                            FilterChip(
                                selected = uiState.estimatorScope == scope,
                                onClick = { onSelectScope(scope) },
                                label = { Text(scope.label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = VeloraInk,
                                    selectedLabelColor = VeloraGoldLight
                                )
                            )
                        }
                    }

                    Text("Material & Finish Tier", style = MaterialTheme.typography.titleSmall)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EstimatorTier.entries.forEach { tier ->
                            FilterChip(
                                selected = uiState.estimatorTier == tier,
                                onClick = { onSelectTier(tier) },
                                label = { Text(tier.label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = VeloraGold,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = VeloraInk,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(VeloraInk, VeloraObsidianCard)
                                    )
                                )
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "ESTIMATED TURNKEY INVESTMENT",
                                style = MaterialTheme.typography.labelSmall,
                                color = VeloraGold
                            )
                            Text(
                                text = uiState.estimatedRangeText,
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontFamily = CormorantGaramondFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "${uiState.estimatedTimelineText} • ${uiState.estimatorTier.finishSummary}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.72f)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = {
                                    onApplyEstimateToQuote(
                                        "${uiState.estimatorHomeSize.label} • ${uiState.estimatorScope.label} (${uiState.estimatorTier.label}: ${uiState.estimatedRangeText})"
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = VeloraGold,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(30.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("estimator_get_exact_quote_button")
                            ) {
                                Text("Lock This Estimate & Book Consultation")
                            }
                        }
                    }
                }
            }
        }

        // 2. Saved Shortlist
        item {
            SectionHeaderBlock(
                eyebrow = "Cloud Shortlist",
                prefix = "Saved",
                italicAccent = "Inspirations",
                subtitle = if (savedIdeas.isEmpty() && savedLighting.isEmpty()) {
                    "Tap the bookmark icon on any room design or lighting concept to save it to your account."
                } else {
                    "${savedIdeas.size + savedLighting.size} saved concepts synced to your account."
                }
            )
        }

        if (savedIdeas.isEmpty() && savedLighting.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkBorder,
                            contentDescription = null,
                            tint = VeloraGold,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = "No saved ideas yet. Explore Design Ideas or Vol. II Lighting to bookmark your favourites.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(savedIdeas, key = { "saved_idea_${it.id}" }) { idea ->
                SavedRowCard(
                    title = idea.title,
                    subtitle = "${idea.category.shortLabel} • ${idea.estimatedBudget}",
                    imageUrl = idea.imageUrl,
                    onClick = { onIdeaClick(idea.id) },
                    onRemove = { onToggleSave(idea.id) }
                )
            }
            items(savedLighting, key = { "saved_light_${it.id}" }) { concept ->
                SavedRowCard(
                    title = concept.title,
                    subtitle = "${concept.series.label} • ${concept.colorTemp}",
                    imageUrl = concept.imageUrl,
                    onClick = { onLightingClick(concept.id) },
                    onRemove = { onToggleSave(concept.id) }
                )
            }
        }

        // 3. Direct Consultation & Quote Contact Form (with 8084189925)
        item {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    SectionHeaderBlock(
                        eyebrow = "Book Free Consultation",
                        prefix = "Connect with",
                        italicAccent = "Velora",
                        subtitle = "Speak directly on ${StudioContactConfig.STUDIO_PHONE_DISPLAY} or submit your project brief to Velora Home."
                    )
                    ConsultationFormContent(
                        formState = uiState.formState,
                        onNameChange = onNameChange,
                        onPhoneChange = onPhoneChange,
                        onCityChange = onCityChange,
                        onPropertyTypeChange = onPropertyTypeChange,
                        onScopeChange = onScopeChange,
                        onNotesChange = onNotesChange,
                        onSubmit = onSubmitConsultation,
                        onDismissSuccess = onDismissSuccess
                    )
                }
            }
        }

        // 4. Cloud Consultations / Owner Client Leads Inbox
        item {
            SectionHeaderBlock(
                eyebrow = if (uiState.isOwnerAdmin) "Owner Lead Inbox" else "Your Requests",
                prefix = if (uiState.isOwnerAdmin) "All Client" else "Booked",
                italicAccent = if (uiState.isOwnerAdmin) "Inquiries" else "Consultations",
                subtitle = if (uiState.isOwnerAdmin) {
                    "All inquiries submitted by users across Velora Home • Owner line ${StudioContactConfig.STUDIO_PHONE_DISPLAY}"
                } else {
                    "Live consultation status • Velora Home ${StudioContactConfig.STUDIO_PHONE_DISPLAY}"
                }
            )
        }

        if (uiState.consultations.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (uiState.isOwnerAdmin) {
                            "No customer consultation leads yet. Inquiries submitted by any user will appear here automatically."
                        } else {
                            "No consultation inquiries submitted yet. Use the form above to request a free callback from Velora Home."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(18.dp)
                    )
                }
            }
        } else {
            items(uiState.consultations, key = { "consult_${it.id}" }) { consultation ->
                ConsultationHistoryCard(
                    consultation = consultation,
                    isOwnerAdmin = uiState.isOwnerAdmin,
                    onCallNumber = { number -> openStudioDialer(context, number) },
                    onUpdateStatus = { newStatus -> onUpdateLeadStatus(consultation.id, newStatus) },
                    onDelete = { onDeleteConsultation(consultation.id) }
                )
            }
        }
    }
}

@Composable
private fun SavedRowCard(
    title: String,
    subtitle: String,
    imageUrl: String?,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imageUrl != null) {
                VeloraAsyncImage(
                    imageUrl = imageUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(14.dp))
                )
            } else {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = VeloraInk,
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Lightbulb,
                            contentDescription = null,
                            tint = VeloraGold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = CormorantGaramondFontFamily,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Filled.BookmarkRemove,
                    contentDescription = "Remove from shortlist",
                    tint = VeloraGoldDark
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ConsultationHistoryCard(
    consultation: FirestoreConsultation,
    isOwnerAdmin: Boolean,
    onCallNumber: (String) -> Unit,
    onUpdateStatus: (String) -> Unit,
    onDelete: () -> Unit
) {
    val dateMillis = consultation.createdAt?.toDate()?.time ?: System.currentTimeMillis()
    val dateLabel = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        .format(Date(dateMillis))
    val statuses = listOf("pending", "contacted", "scheduled", "completed")

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${consultation.fullName} • ${consultation.propertyType}",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = VeloraGoldLight
                ) {
                    Text(
                        text = consultation.status.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = VeloraGoldDark,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "${consultation.serviceScope} • ${consultation.city} • $dateLabel",
                style = MaterialTheme.typography.bodySmall,
                color = VeloraGoldDark
            )

            if (isOwnerAdmin && consultation.userEmail.isNotBlank()) {
                Text(
                    text = "Client Email: ${consultation.userEmail}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (consultation.designReference.isNotBlank()) {
                Text(
                    text = "Concept: ${consultation.designReference}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (consultation.notes.isNotBlank()) {
                Text(
                    text = "Notes: ${consultation.notes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isOwnerAdmin) {
                        "Client Phone: ${consultation.phoneNumber}"
                    } else {
                        "Callback: ${consultation.phoneNumber} • Owner: +91 ${consultation.studioOwnerPhone}"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    IconButton(
                        onClick = {
                            val target = if (isOwnerAdmin) consultation.phoneNumber else consultation.studioOwnerPhone
                            onCallNumber(target)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = "Call",
                            tint = VeloraGold
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Filled.DeleteOutline,
                            contentDescription = "Delete inquiry",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (isOwnerAdmin) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    statuses.forEach { statusOption ->
                        FilterChip(
                            selected = consultation.status == statusOption,
                            onClick = { if (consultation.status != statusOption) onUpdateStatus(statusOption) },
                            label = {
                                Text(
                                    statusOption.replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = VeloraGold,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}
