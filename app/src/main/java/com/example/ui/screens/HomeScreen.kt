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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bed
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Countertops
import androidx.compose.material.icons.outlined.Desk
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Spa
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.catalog.VeloraCatalog
import com.example.data.model.CraftedHighlight
import com.example.data.model.RoomCategory
import com.example.data.model.StudioContactConfig
import com.example.ui.components.EditorialHeading
import com.example.ui.components.SectionHeaderBlock
import com.example.ui.components.StudioContactBanner
import com.example.ui.components.VeloraAsyncImage
import com.example.ui.components.openStudioDialer
import com.example.ui.components.shareVeloraApp
import com.example.ui.theme.CormorantGaramondFontFamily
import com.example.ui.theme.VeloraGold
import com.example.ui.theme.VeloraGoldDark
import com.example.ui.theme.VeloraGoldLight
import com.example.ui.theme.VeloraInk

@Composable
fun HomeScreen(
    onExploreIdeas: (RoomCategory) -> Unit,
    onExploreLighting: () -> Unit,
    onOpenQuote: (String) -> Unit,
    onIdeaClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val roomCategories = RoomCategory.entries.filter { it != RoomCategory.ALL }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        // 1. Hero Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                VeloraGoldLight.copy(alpha = 0.42f),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        EditorialHeading(
                            prefix = "Velora",
                            italicAccent = "Home",
                            fontSizeSp = 34
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, VeloraGold.copy(alpha = 0.45f)),
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .clickable { shareVeloraApp(context) }
                                    .testTag("hero_share_app_button")
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.Share,
                                        contentDescription = "Share Velora Home with any device",
                                        tint = VeloraGoldDark,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(30.dp),
                                color = VeloraGold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(30.dp))
                                    .clickable { onOpenQuote("") }
                                    .testTag("hero_top_quote_button")
                            ) {
                                Text(
                                    text = "Get a Quote",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                                )
                            }
                        }
                    }

                    // Featured Hero Visual Card
                    Surface(
                        shape = RoundedCornerShape(28.dp),
                        shadowElevation = 8.dp,
                        border = BorderStroke(1.dp, VeloraGold.copy(alpha = 0.25f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            VeloraAsyncImage(
                                imageUrl = VeloraCatalog.HERO_IMAGE_URL,
                                contentDescription = "Velora Home luxury interior showcase",
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                VeloraInk.copy(alpha = 0.82f)
                                            )
                                        )
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(20.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = VeloraGold.copy(alpha = 0.9f)
                                ) {
                                    Text(
                                        text = "CURATED INTERIORS & LIGHTING",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Where light meets intention.",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontFamily = CormorantGaramondFontFamily,
                                        fontSize = 28.sp,
                                        color = Color.White
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "A consciously crafted interior experience blending warmth, nature and modern luxury.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.82f),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { onExploreIdeas(RoomCategory.ALL) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VeloraGold,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(30.dp),
                            contentPadding = PaddingValues(vertical = 13.dp, horizontal = 18.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("hero_explore_ideas_button")
                        ) {
                            Text(
                                text = "Explore Design Ideas",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        OutlinedButton(
                            onClick = onExploreLighting,
                            border = BorderStroke(1.dp, VeloraGold),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = VeloraGoldDark),
                            shape = RoundedCornerShape(30.dp),
                            contentPadding = PaddingValues(vertical = 13.dp, horizontal = 16.dp),
                            modifier = Modifier.testTag("hero_lighting_vol2_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Vol. II Light",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }

        // 2. Consciously Crafted Section (from HTML)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                SectionHeaderBlock(
                    eyebrow = "Philosophy",
                    prefix = "Consciously",
                    italicAccent = "Crafted",
                    subtitle = "Subtle illumination, natural materials, and modern detailing create a home that feels effortless.",
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(VeloraCatalog.craftedHighlights, key = { it.id }) { highlight ->
                        CraftedHighlightCard(
                            highlight = highlight,
                            onClick = { onOpenQuote(highlight.title) }
                        )
                    }
                }
            }
        }

        // 3. Livspace-Inspired Room Design Ideas Grid
        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "DESIGN IDEAS BY SPACE",
                            style = MaterialTheme.typography.labelMedium,
                            color = VeloraGold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        EditorialHeading(
                            prefix = "Curated",
                            italicAccent = "Rooms",
                            fontSizeSp = 28
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onExploreIdeas(RoomCategory.ALL) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "View All",
                                style = MaterialTheme.typography.labelLarge,
                                color = VeloraGoldDark
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = VeloraGoldDark,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                // 2-Column Room Category Cards
                val rows = roomCategories.chunked(2)
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    rows.forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            pair.forEach { category ->
                                val count = VeloraCatalog.designIdeas.count { it.category == category }
                                RoomCategoryTile(
                                    category = category,
                                    count = count,
                                    onClick = { onExploreIdeas(category) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (pair.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        // 4. Featured Room Concepts Strip
        item {
            val featured = VeloraCatalog.designIdeas.filter { it.isFeatured }
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                SectionHeaderBlock(
                    eyebrow = "Editor's Picks",
                    prefix = "Quietly",
                    italicAccent = "Elegant",
                    subtitle = "Signature interiors pairing bespoke millwork with architectural light.",
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(featured, key = { it.id }) { idea ->
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .width(270.dp)
                                .clickable { onIdeaClick(idea.id) }
                                .testTag("featured_idea_${idea.id}")
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(170.dp)
                                ) {
                                    VeloraAsyncImage(
                                        imageUrl = idea.imageUrl,
                                        contentDescription = idea.title,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        color = VeloraInk.copy(alpha = 0.78f),
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = idea.estimatedBudget,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = VeloraGoldLight,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = idea.styleTag.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = VeloraGoldDark
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = idea.title,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontFamily = CormorantGaramondFontFamily
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = idea.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. CTA Banner + Footer with 8084189925
        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                StudioContactBanner(
                    onBookConsultationClick = { onOpenQuote("") }
                )

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            EditorialHeading(
                                prefix = "Velora",
                                italicAccent = "Home",
                                fontSizeSp = 22
                            )
                            Text(
                                text = "${StudioContactConfig.STUDIO_OWNER_ATTRIBUTION} • ${StudioContactConfig.STUDIO_PHONE_DISPLAY}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = StudioContactConfig.STUDIO_CITY,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(1.dp, VeloraGold.copy(alpha = 0.4f)),
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .clickable { shareVeloraApp(context) }
                                    .testTag("footer_share_app_button")
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.Share,
                                        contentDescription = "Share Velora Home to any device",
                                        tint = VeloraGoldDark,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Surface(
                                shape = CircleShape,
                                color = VeloraGoldLight,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .clickable { openStudioDialer(context) }
                                    .testTag("footer_call_8084189925")
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.Call,
                                        contentDescription = "Call ${StudioContactConfig.STUDIO_PHONE_RAW}",
                                        tint = VeloraGoldDark,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CraftedHighlightCard(
    highlight: CraftedHighlight,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shadowElevation = 2.dp,
        modifier = Modifier
            .width(260.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            VeloraAsyncImage(
                imageUrl = highlight.imageUrl,
                contentDescription = highlight.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(155.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = highlight.tag.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = VeloraGoldDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = highlight.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = VeloraGoldDark,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = highlight.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun RoomCategoryTile(
    category: RoomCategory,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon: ImageVector = when (category) {
        RoomCategory.KITCHENS -> Icons.Outlined.Countertops
        RoomCategory.LIVING_ROOM -> Icons.Outlined.Chair
        RoomCategory.BEDROOM -> Icons.Outlined.Bed
        RoomCategory.WARDROBES -> Icons.Outlined.Checkroom
        RoomCategory.BATHROOMS -> Icons.Outlined.Spa
        RoomCategory.DINING_BAR -> Icons.Outlined.Restaurant
        RoomCategory.STUDY_FOYER -> Icons.Outlined.Desk
        RoomCategory.ALL -> Icons.Outlined.AutoAwesome
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag("room_tile_${category.name.lowercase()}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = VeloraGoldLight.copy(alpha = 0.6f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = category.label,
                        tint = VeloraGoldDark,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.shortLabel,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$count ideas",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
