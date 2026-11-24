package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Architecture
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DesignIdea
import com.example.data.model.RoomCategory
import com.example.ui.components.EditorialHeading
import com.example.ui.components.SaveBookmarkButton
import com.example.ui.components.VeloraAsyncImage
import com.example.ui.theme.CormorantGaramondFontFamily
import com.example.ui.theme.VeloraGold
import com.example.ui.theme.VeloraGoldDark
import com.example.ui.theme.VeloraGoldLight
import com.example.ui.theme.VeloraInk

@Composable
fun IdeasScreen(
    selectedCategory: RoomCategory,
    searchQuery: String,
    ideas: List<DesignIdea>,
    savedIds: Set<String>,
    onSelectCategory: (RoomCategory) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onToggleSave: (String) -> Unit,
    onIdeaClick: (String) -> Unit,
    onGetQuoteForIdea: (DesignIdea) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Header + Search + Category Filter Strip
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "HOME DESIGN GALLERY",
                        style = MaterialTheme.typography.labelMedium,
                        color = VeloraGold
                    )
                    EditorialHeading(
                        prefix = "Design",
                        italicAccent = "Ideas",
                        fontSizeSp = 32
                    )
                }
                Text(
                    text = "${ideas.size} looks",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search kitchens, onyx, walnut, wardrobe...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search design ideas",
                        tint = VeloraGoldDark
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VeloraGold,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ideas_search_input")
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(RoomCategory.entries, key = { it.name }) { category ->
                val isSelected = category == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectCategory(category) },
                    label = { Text(category.shortLabel) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VeloraGold,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("category_chip_${category.name.lowercase()}")
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (ideas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.Architecture,
                        contentDescription = null,
                        tint = VeloraGold,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No matching design ideas",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = CormorantGaramondFontFamily
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Try clearing your search or switching to All Spaces.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(onClick = {
                        onSearchQueryChange("")
                        onSelectCategory(RoomCategory.ALL)
                    }) {
                        Text("Reset Filters", color = VeloraGoldDark)
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(ideas, key = { it.id }) { idea ->
                    DesignIdeaCard(
                        idea = idea,
                        isSaved = savedIds.contains(idea.id),
                        onToggleSave = { onToggleSave(idea.id) },
                        onClick = { onIdeaClick(idea.id) },
                        onGetQuote = { onGetQuoteForIdea(idea) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DesignIdeaCard(
    idea: DesignIdea,
    isSaved: Boolean,
    onToggleSave: () -> Unit,
    onClick: () -> Unit,
    onGetQuote: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shadowElevation = 3.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .clickable(onClick = onClick)
            .testTag("design_idea_card_${idea.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                VeloraAsyncImage(
                    imageUrl = idea.imageUrl,
                    contentDescription = idea.title,
                    modifier = Modifier.fillMaxSize()
                )
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = VeloraInk.copy(alpha = 0.8f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(14.dp)
                ) {
                    Text(
                        text = "${idea.category.shortLabel.uppercase()} • ${idea.dimensions}",
                        style = MaterialTheme.typography.labelSmall,
                        color = VeloraGoldLight,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
                SaveBookmarkButton(
                    isSaved = isSaved,
                    onClick = onToggleSave,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                )
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = VeloraGold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Text(
                        text = "Est. ${idea.estimatedBudget}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = idea.styleTag.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = VeloraGoldDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = idea.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = CormorantGaramondFontFamily,
                        fontSize = 23.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = idea.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Lightbulb,
                            contentDescription = null,
                            tint = VeloraGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = idea.lightingPairing,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = VeloraGoldLight,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable(onClick = onGetQuote)
                    ) {
                        Text(
                            text = "Get Quote",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = VeloraGoldDark,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
