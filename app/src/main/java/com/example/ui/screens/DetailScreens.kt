package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.AspectRatio
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.WbIncandescent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DesignIdea
import com.example.data.model.LightingConcept
import com.example.data.model.StudioContactConfig
import com.example.ui.components.SaveBookmarkButton
import com.example.ui.components.VeloraAsyncImage
import com.example.ui.components.openStudioDialer
import com.example.ui.components.shareTextWithAnyDevice
import com.example.ui.theme.CormorantGaramondFontFamily
import com.example.ui.theme.VeloraGold
import com.example.ui.theme.VeloraGoldDark
import com.example.ui.theme.VeloraGoldLight
import com.example.ui.theme.VeloraInk
import com.example.ui.theme.VeloraObsidianCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DesignIdeaDetailScreen(
    idea: DesignIdea,
    isSaved: Boolean,
    onBack: () -> Unit,
    onToggleSave: () -> Unit,
    onBookQuote: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    BackHandler(onBack = onBack)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 28.dp)
    ) {
        // Hero Image Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(310.dp)
        ) {
            VeloraAsyncImage(
                imageUrl = idea.imageUrl,
                contentDescription = idea.title,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                VeloraInk.copy(alpha = 0.4f),
                                Color.Transparent,
                                VeloraInk.copy(alpha = 0.78f)
                            )
                        )
                    )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = VeloraInk.copy(alpha = 0.65f),
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("detail_back_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        shape = CircleShape,
                        color = VeloraInk.copy(alpha = 0.65f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.28f)),
                        onClick = {
                            val summary = buildString {
                                appendLine("Velora Home Design Concept: ${idea.title}")
                                appendLine("${idea.category.label} • ${idea.styleTag}")
                                appendLine("Dimensions: ${idea.dimensions} • Est. Budget: ${idea.estimatedBudget}")
                                appendLine("Lighting Pairing: ${idea.lightingPairing}")
                                appendLine("Finishes: ${idea.materials.joinToString(", ")}")
                                appendLine()
                                appendLine("WhatsApp Velora Home: ${StudioContactConfig.STUDIO_WHATSAPP_URL}")
                                append("Official Studio Line: ${StudioContactConfig.STUDIO_PHONE_DISPLAY}")
                            }
                            shareTextWithAnyDevice(
                                context = context,
                                subject = "${idea.title} — Velora Home",
                                body = summary
                            )
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("detail_share_idea_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Share ${idea.title} to any device",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    SaveBookmarkButton(
                        isSaved = isSaved,
                        onClick = onToggleSave
                    )
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = VeloraGold
                ) {
                    Text(
                        text = idea.category.label.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = idea.title,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontFamily = CormorantGaramondFontFamily,
                        color = Color.White
                    )
                )
            }
        }

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = idea.subtitle,
                style = MaterialTheme.typography.titleMedium,
                color = VeloraGoldDark
            )

            // Specs Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SpecPillCard(
                    icon = Icons.Outlined.AspectRatio,
                    label = "DIMENSIONS",
                    value = idea.dimensions,
                    modifier = Modifier.weight(1f)
                )
                SpecPillCard(
                    icon = Icons.Outlined.CurrencyRupee,
                    label = "EST. BUDGET",
                    value = idea.estimatedBudget,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = idea.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Lighting Pairing Card
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = VeloraInk,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = VeloraGold.copy(alpha = 0.2f),
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.Lightbulb,
                                contentDescription = null,
                                tint = VeloraGold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "VELORA LIGHTING PAIRING",
                            style = MaterialTheme.typography.labelSmall,
                            color = VeloraGold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = idea.lightingPairing,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }
            }

            // Materials & Finishes
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "RECOMMENDED MATERIALS & FINISHES",
                    style = MaterialTheme.typography.labelMedium,
                    color = VeloraGoldDark
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    idea.materials.forEach { material ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Text(
                                text = material,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Primary Quote & Direct Call 8084189925 Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onBookQuote,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VeloraGold,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(30.dp),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("detail_get_quote_button")
                ) {
                    Text(
                        text = "Get Quote for This Design",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                OutlinedButton(
                    onClick = { openStudioDialer(context) },
                    border = BorderStroke(1.dp, VeloraGold),
                    shape = RoundedCornerShape(30.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                    modifier = Modifier.testTag("detail_call_8084189925")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Call,
                        contentDescription = "Call ${StudioContactConfig.STUDIO_PHONE_RAW}",
                        tint = VeloraGoldDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LightingDetailScreen(
    concept: LightingConcept,
    isSaved: Boolean,
    onBack: () -> Unit,
    onToggleSave: () -> Unit,
    onBookLightingQuote: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    BackHandler(onBack = onBack)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 28.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(290.dp)
        ) {
            if (concept.imageUrl != null) {
                VeloraAsyncImage(
                    imageUrl = concept.imageUrl,
                    contentDescription = concept.title,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    VeloraGold.copy(alpha = 0.25f),
                                    VeloraObsidianCard,
                                    VeloraInk
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AutoAwesome,
                        contentDescription = null,
                        tint = VeloraGold,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                VeloraInk.copy(alpha = 0.35f),
                                Color.Transparent,
                                VeloraInk.copy(alpha = 0.85f)
                            )
                        )
                    )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = VeloraInk.copy(alpha = 0.65f),
                    onClick = onBack,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        shape = CircleShape,
                        color = VeloraInk.copy(alpha = 0.65f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.28f)),
                        onClick = {
                            val summary = buildString {
                                appendLine("Velora Home Vol. II Lighting: ${concept.title}")
                                appendLine("${concept.series.label} • ${concept.tag}")
                                appendLine("Colour Temp: ${concept.colorTemp} • Ideal For: ${concept.idealFor}")
                                appendLine(concept.description)
                                appendLine()
                                appendLine("WhatsApp Velora Home: ${StudioContactConfig.STUDIO_WHATSAPP_URL}")
                                append("Official Studio Line: ${StudioContactConfig.STUDIO_PHONE_DISPLAY}")
                            }
                            shareTextWithAnyDevice(
                                context = context,
                                subject = "${concept.title} — Velora Vol. II Lighting",
                                body = summary
                            )
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("detail_share_lighting_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Share ${concept.title} to any device",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    SaveBookmarkButton(
                        isSaved = isSaved,
                        onClick = onToggleSave
                    )
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = VeloraGold
                ) {
                    Text(
                        text = "${concept.series.label.uppercase()} • ${concept.tag.uppercase()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = concept.title,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontFamily = CormorantGaramondFontFamily,
                        color = Color.White
                    )
                )
            }
        }

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SpecPillCard(
                    icon = Icons.Outlined.WbIncandescent,
                    label = "COLOUR TEMP",
                    value = concept.colorTemp,
                    modifier = Modifier.weight(1f)
                )
                SpecPillCard(
                    icon = Icons.Outlined.Place,
                    label = "IDEAL FOR",
                    value = concept.idealFor,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = concept.description,
                style = MaterialTheme.typography.bodyLarge
            )

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = VeloraGoldLight.copy(alpha = 0.45f),
                border = BorderStroke(1.dp, VeloraGold.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "BESPOKE LIGHTING SPECIFICATION",
                        style = MaterialTheme.typography.labelSmall,
                        color = VeloraGoldDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Our lighting architects prepare false-ceiling electrical layouts, dimmer zoning, and fixture sourcing tailored to your rooms. Direct desk: ${StudioContactConfig.STUDIO_PHONE_DISPLAY}.",
                        style = MaterialTheme.typography.bodySmall,
                        color = VeloraInk
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onBookLightingQuote,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VeloraGold,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(30.dp),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Request Lighting Plan", style = MaterialTheme.typography.labelLarge)
                }
                OutlinedButton(
                    onClick = { openStudioDialer(context) },
                    border = BorderStroke(1.dp, VeloraGold),
                    shape = RoundedCornerShape(30.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Call,
                        contentDescription = "Call ${StudioContactConfig.STUDIO_PHONE_RAW}",
                        tint = VeloraGoldDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SpecPillCard(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = VeloraGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
