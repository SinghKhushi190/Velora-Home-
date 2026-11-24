package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Flare
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.PhoneIphone
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.outlined.Waves
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LightingConcept
import com.example.data.model.LightingSeries
import com.example.ui.components.EditorialHeading
import com.example.ui.components.SaveBookmarkButton
import com.example.ui.components.SectionHeaderBlock
import com.example.ui.components.VeloraAsyncImage
import com.example.ui.theme.CormorantGaramondFontFamily
import com.example.ui.theme.VeloraGold
import com.example.ui.theme.VeloraGoldDark
import com.example.ui.theme.VeloraGoldLight
import com.example.ui.theme.VeloraInk
import com.example.ui.theme.VeloraObsidianCard

@Composable
fun LightingScreen(
    selectedSeries: LightingSeries,
    concepts: List<LightingConcept>,
    savedIds: Set<String>,
    onSelectSeries: (LightingSeries) -> Unit,
    onToggleSave: (String) -> Unit,
    onConceptClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val architectural = concepts.filter { it.series == LightingSeries.ARCHITECTURAL }
    val decorative = concepts.filter { it.series == LightingSeries.DECORATIVE }
    val functional = concepts.filter { it.series == LightingSeries.FUNCTIONAL }
    val smart = concepts.filter { it.series == LightingSeries.SMART }

    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "LIGHTING COLLECTION VOL. II",
                style = MaterialTheme.typography.labelMedium,
                color = VeloraGold
            )
            EditorialHeading(
                prefix = "The Art of",
                italicAccent = "Illumination",
                fontSizeSp = 32
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Specialist architectural, decorative, functional, and smart lighting forms.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(LightingSeries.entries, key = { it.name }) { series ->
                FilterChip(
                    selected = selectedSeries == series,
                    onClick = { onSelectSeries(series) },
                    label = {
                        Text(
                            if (series == LightingSeries.ALL) series.label
                            else "${series.code} ${series.label}"
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VeloraGold,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("lighting_chip_${series.name.lowercase()}")
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(26.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // 01 ARCHITECTURAL
            if (architectural.isNotEmpty()) {
                item {
                    SectionHeaderBlock(
                        eyebrow = "Architectural",
                        prefix = "Light as",
                        italicAccent = "Structure",
                        subtitle = "Embedded, intentional illumination inseparable from the space it inhabits.",
                        countNumber = "01",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                }
                items(architectural, key = { it.id }) { concept ->
                    ArchitecturalConceptCard(
                        concept = concept,
                        isSaved = savedIds.contains(concept.id),
                        onToggleSave = { onToggleSave(concept.id) },
                        onClick = { onConceptClick(concept.id) },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }

            // 02 DECORATIVE
            if (decorative.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        SectionHeaderBlock(
                            eyebrow = "Decorative Fixtures",
                            prefix = "Objects of",
                            italicAccent = "Desire",
                            subtitle = "Statement fixtures that double as sculpture — chosen for form and glow.",
                            countNumber = "02",
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(decorative, key = { it.id }) { concept ->
                                DecorativeConceptCard(
                                    concept = concept,
                                    isSaved = savedIds.contains(concept.id),
                                    onToggleSave = { onToggleSave(concept.id) },
                                    onClick = { onConceptClick(concept.id) }
                                )
                            }
                        }
                    }
                }
            }

            // 03 FUNCTIONAL SPECIALTIES
            if (functional.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        SectionHeaderBlock(
                            eyebrow = "Functional Specialties",
                            prefix = "Light with",
                            italicAccent = "Purpose",
                            subtitle = "Specialist lighting tuned for focused tasks, botanical care, and circadian rhythm.",
                            countNumber = "03"
                        )
                        Surface(
                            shape = RoundedCornerShape(26.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                functional.forEachIndexed { index, concept ->
                                    FunctionalConceptRow(
                                        concept = concept,
                                        icon = functionalIconFor(concept.id),
                                        onClick = { onConceptClick(concept.id) }
                                    )
                                    if (index < functional.lastIndex) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                                .background(MaterialTheme.colorScheme.outline)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 04 SMART & INNOVATIVE (Dark Obsidian Section from HTML)
            if (smart.isNotEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(32.dp),
                        color = VeloraInk,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            VeloraGold.copy(alpha = 0.14f),
                                            VeloraInk
                                        )
                                    )
                                )
                                .padding(22.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            SectionHeaderBlock(
                                eyebrow = "Smart & Innovative",
                                prefix = "The Future of",
                                italicAccent = "Light",
                                subtitle = "Next-generation illumination that adapts and responds at the touch of a button.",
                                countNumber = "04",
                                darkMode = true
                            )
                            smart.forEach { concept ->
                                SmartDarkCard(
                                    concept = concept,
                                    icon = smartIconFor(concept.id),
                                    onClick = { onConceptClick(concept.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ArchitecturalConceptCard(
    concept: LightingConcept,
    isSaved: Boolean,
    onToggleSave: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(215.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .testTag("lighting_card_${concept.id}")
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            VeloraAsyncImage(
                imageUrl = concept.imageUrl.orEmpty(),
                contentDescription = concept.title,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                VeloraInk.copy(alpha = 0.88f)
                            )
                        )
                    )
            )
            SaveBookmarkButton(
                isSaved = isSaved,
                onClick = onToggleSave,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(18.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = VeloraGold.copy(alpha = 0.92f)
                ) {
                    Text(
                        text = concept.tag.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = concept.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = CormorantGaramondFontFamily,
                        fontSize = 23.sp,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = concept.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun DecorativeConceptCard(
    concept: LightingConcept,
    isSaved: Boolean,
    onToggleSave: () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shadowElevation = 2.dp,
        modifier = Modifier
            .width(260.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .testTag("lighting_card_${concept.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                VeloraAsyncImage(
                    imageUrl = concept.imageUrl.orEmpty(),
                    contentDescription = concept.title,
                    modifier = Modifier.fillMaxSize()
                )
                SaveBookmarkButton(
                    isSaved = isSaved,
                    onClick = onToggleSave,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = VeloraGoldLight
                ) {
                    Text(
                        text = concept.tag.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = VeloraGoldDark,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = concept.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = CormorantGaramondFontFamily
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = concept.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun FunctionalConceptRow(
    concept: LightingConcept,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(18.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = VeloraGoldLight,
            modifier = Modifier.size(46.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = concept.title,
                    tint = VeloraGoldDark,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = concept.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = CormorantGaramondFontFamily
                    )
                )
                Text(
                    text = concept.colorTemp,
                    style = MaterialTheme.typography.labelSmall,
                    color = VeloraGoldDark
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = concept.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SmartDarkCard(
    concept: LightingConcept,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = VeloraObsidianCard,
        border = BorderStroke(1.dp, VeloraGold.copy(alpha = 0.25f)),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag("lighting_card_${concept.id}")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = VeloraGold,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = concept.numberCode,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontFamily = CormorantGaramondFontFamily,
                        fontWeight = FontWeight.Light
                    ),
                    color = VeloraGold.copy(alpha = 0.28f)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = concept.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = concept.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.65f)
            )
        }
    }
}

private fun functionalIconFor(id: String): ImageVector = when (id) {
    "func_spotlights" -> Icons.Outlined.TrackChanges
    "func_candelabra" -> Icons.Outlined.Flare
    "func_grow" -> Icons.Outlined.LocalFlorist
    "func_circadian" -> Icons.Outlined.WbSunny
    else -> Icons.Outlined.LightMode
}

private fun smartIconFor(id: String): ImageVector = when (id) {
    "smart_pool" -> Icons.Outlined.Waves
    "smart_voice" -> Icons.Outlined.PhoneIphone
    else -> Icons.Outlined.AutoAwesome
}
