package com.example.ui.components

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.data.model.StudioContactConfig
import com.example.ui.ConsultationFormState
import com.example.ui.theme.CormorantGaramondFontFamily
import com.example.ui.theme.VeloraGold
import com.example.ui.theme.VeloraGoldDark
import com.example.ui.theme.VeloraGoldLight
import com.example.ui.theme.VeloraInk
import com.example.ui.theme.VeloraObsidianCard
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun openStudioDialer(context: Context, phone: String = StudioContactConfig.STUDIO_PHONE_RAW) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    context.startActivity(intent)
}

fun shareTextWithAnyDevice(
    context: Context,
    subject: String,
    body: String
) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }
    val chooser = Intent.createChooser(sendIntent, "Share via any device or app")
    context.startActivity(chooser)
}

fun shareVeloraApp(context: Context) {
    val appContext = context.applicationContext
    val fallbackMessage = buildString {
        appendLine("Velora Home — Luxury Interiors & Vol. II Lighting")
        appendLine("Sign in with any Google account on any Android device to browse room ideas, save your shortlist, and request quotes.")
        appendLine("Connect with Velora Home (${StudioContactConfig.STUDIO_OWNER_ATTRIBUTION}): ${StudioContactConfig.STUDIO_PHONE_DISPLAY}")
        append("WhatsApp Studio: ${StudioContactConfig.STUDIO_WHATSAPP_URL}")
    }

    CoroutineScope(Dispatchers.IO).launch {
        val apkUri = runCatching {
            val info = appContext.applicationInfo
            val isSingleApk = info.splitSourceDirs.isNullOrEmpty()
            val sourceFile = File(info.sourceDir)
            if (!isSingleApk || !sourceFile.exists() || !sourceFile.canRead()) {
                return@runCatching null
            }
            val shareDir = File(appContext.cacheDir, "shared_apks").apply { mkdirs() }
            val destFile = File(shareDir, "VeloraHome.apk")
            if (!destFile.exists() || destFile.length() != sourceFile.length()) {
                sourceFile.inputStream().use { input ->
                    destFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
            FileProvider.getUriForFile(
                appContext,
                "${appContext.packageName}.fileprovider",
                destFile
            )
        }.getOrNull()

        withContext(Dispatchers.Main) {
            if (apkUri != null) {
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/vnd.android.package-archive"
                    putExtra(Intent.EXTRA_STREAM, apkUri)
                    putExtra(Intent.EXTRA_SUBJECT, "Velora Home Android App (APK)")
                    putExtra(Intent.EXTRA_TEXT, fallbackMessage)
                    clipData = ClipData.newRawUri("VeloraHome.apk", apkUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                val chooser = Intent.createChooser(sendIntent, "Send Velora Home APK to any device")
                if (context !is android.app.Activity) {
                    chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                try {
                    context.startActivity(chooser)
                } catch (_: Exception) {
                    shareTextWithAnyDevice(
                        context = context,
                        subject = "Velora Home — Interiors & Lighting",
                        body = fallbackMessage
                    )
                }
            } else {
                shareTextWithAnyDevice(
                    context = context,
                    subject = "Velora Home — Interiors & Lighting",
                    body = fallbackMessage
                )
            }
        }
    }
}

fun openWhatsAppStudio(context: Context, message: String) {
    val encoded = Uri.encode(message)
    val waUri = Uri.parse("${StudioContactConfig.STUDIO_WHATSAPP_URL}?text=$encoded")
    val intent = Intent(Intent.ACTION_VIEW, waUri)
    try {
        context.startActivity(intent)
    } catch (_: Exception) {
        shareTextWithAnyDevice(
            context = context,
            subject = "Velora Home Project Inquiry",
            body = message
        )
    }
}

@Composable
fun VeloraAsyncImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surfaceVariant,
                                VeloraGoldLight.copy(alpha = 0.45f)
                            )
                        )
                    )
            )
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(VeloraInk, VeloraObsidianCard)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = VeloraGold.copy(alpha = 0.6f),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    )
}

@Composable
fun EditorialHeading(
    prefix: String,
    italicAccent: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    fontSizeSp: Int = 28
) {
    Text(
        text = buildAnnotatedString {
            append(prefix)
            if (prefix.isNotEmpty()) append(" ")
            withStyle(
                SpanStyle(
                    color = VeloraGold,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Normal
                )
            ) {
                append(italicAccent)
            }
        },
        style = MaterialTheme.typography.headlineLarge.copy(
            fontFamily = CormorantGaramondFontFamily,
            fontSize = fontSizeSp.sp,
            lineHeight = (fontSizeSp + 6).sp,
            color = color
        ),
        modifier = modifier
    )
}

@Composable
fun SectionHeaderBlock(
    eyebrow: String,
    prefix: String,
    italicAccent: String,
    subtitle: String,
    countNumber: String? = null,
    modifier: Modifier = Modifier,
    darkMode: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = eyebrow.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = VeloraGold
            )
            Spacer(modifier = Modifier.height(6.dp))
            EditorialHeading(
                prefix = prefix,
                italicAccent = italicAccent,
                color = if (darkMode) Color.White else MaterialTheme.colorScheme.onBackground,
                fontSizeSp = 30
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = if (darkMode) Color.White.copy(alpha = 0.68f) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (countNumber != null) {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = countNumber,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Light
                ),
                color = if (darkMode) VeloraGold.copy(alpha = 0.28f) else VeloraGoldLight
            )
        }
    }
}

@Composable
fun SaveBookmarkButton(
    isSaved: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = CircleShape,
        color = if (isSaved) VeloraGold else VeloraInk.copy(alpha = 0.62f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.28f)),
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                contentDescription = if (isSaved) "Remove from shortlist" else "Save to shortlist",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun StudioContactBanner(
    onBookConsultationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(VeloraGold, VeloraGoldDark)
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "VELORA HOME • OFFICIAL OWNER LINE",
                    style = MaterialTheme.typography.labelMedium,
                    color = VeloraGoldLight
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ready to Light Your World?",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = CormorantGaramondFontFamily,
                        fontSize = 28.sp,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Our interior & lighting consultants craft a tailored layout, 3D plan, and illumination schedule for your budget.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = VeloraGoldLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${StudioContactConfig.STUDIO_CITY} • ${StudioContactConfig.STUDIO_PHONE_DISPLAY} (${StudioContactConfig.STUDIO_OWNER_ATTRIBUTION})",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onBookConsultationClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = VeloraGoldDark
                        ),
                        shape = RoundedCornerShape(30.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("cta_book_consultation_button")
                    ) {
                        Text(
                            text = "Book Consultation",
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    OutlinedButton(
                        onClick = { openStudioDialer(context) },
                        border = BorderStroke(1.2.dp, Color.White.copy(alpha = 0.7f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        shape = RoundedCornerShape(30.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                        modifier = Modifier.testTag("cta_call_studio_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = "Call ${StudioContactConfig.STUDIO_PHONE_RAW}",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = StudioContactConfig.STUDIO_PHONE_RAW,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable { shareVeloraApp(context) }
                            .testTag("cta_share_app_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Share Velora Home with any device",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConsultationFormContent(
    formState: ConsultationFormState,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onPropertyTypeChange: (String) -> Unit,
    onScopeChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismissSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val propertyOptions = listOf("2 BHK", "3 BHK", "4 BHK / Villa", "Commercial")
    val scopeOptions = listOf(
        "Full Home & Lighting",
        "Modular Kitchen",
        "Architectural Lighting",
        "Wardrobes & Storage"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Studio Direct Contact Line Banner with 8084189925
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "OFFICIAL OWNER LINE • VELORA HOME",
                        style = MaterialTheme.typography.labelSmall,
                        color = VeloraGoldDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${StudioContactConfig.STUDIO_PHONE_DISPLAY} (Velora Home)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${StudioContactConfig.STUDIO_CITY} • ${StudioContactConfig.STUDIO_HOURS}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = CircleShape,
                    color = VeloraGold,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { openStudioDialer(context) }
                        .testTag("form_call_8084189925")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = "Call studio ${StudioContactConfig.STUDIO_PHONE_RAW}",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        if (formState.submittedSuccessName != null) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFEDF7EF),
                border = BorderStroke(1.dp, Color(0xFF2E6F40).copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF2E6F40),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Consultation Booked, ${formState.submittedSuccessName}!",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = Color(0xFF1A4326)
                        )
                        Text(
                            text = "Our design lead will connect with you from ${StudioContactConfig.STUDIO_PHONE_DISPLAY}.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF2A5A37)
                        )
                    }
                    IconButton(onClick = onDismissSuccess) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Dismiss confirmation",
                            tint = Color(0xFF1A4326)
                        )
                    }
                }
            }
        }

        if (formState.designReference.isNotBlank()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = VeloraGoldLight.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, VeloraGold.copy(alpha = 0.4f))
            ) {
                Text(
                    text = "Selected Concept: ${formState.designReference}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = VeloraInk,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }

        OutlinedTextField(
            value = formState.fullName,
            onValueChange = onNameChange,
            label = { Text("Full Name") },
            placeholder = { Text("Enter your full name") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VeloraGold,
                focusedLabelColor = VeloraGoldDark
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("contact_name_input")
        )

        OutlinedTextField(
            value = formState.phoneNumber,
            onValueChange = onPhoneChange,
            label = { Text("Your Mobile Number") },
            placeholder = { Text("Enter your 10-digit mobile number") },
            supportingText = {
                Text("Velora Home owner line (${StudioContactConfig.STUDIO_PHONE_DISPLAY}) will reach out to you")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VeloraGold,
                focusedLabelColor = VeloraGoldDark
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("contact_phone_input")
        )

        OutlinedTextField(
            value = formState.city,
            onValueChange = onCityChange,
            label = { Text("City / Locality") },
            placeholder = { Text(StudioContactConfig.STUDIO_CITY) },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VeloraGold,
                focusedLabelColor = VeloraGoldDark
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("contact_city_input")
        )

        Column {
            Text(
                text = "Property Configuration",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                propertyOptions.forEach { option ->
                    val selected = formState.propertyType == option
                    FilterChip(
                        selected = selected,
                        onClick = { onPropertyTypeChange(option) },
                        label = { Text(option) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = VeloraGold,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        Column {
            Text(
                text = "Design Requirement",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                scopeOptions.forEach { option ->
                    val selected = formState.serviceScope == option
                    FilterChip(
                        selected = selected,
                        onClick = { onScopeChange(option) },
                        label = { Text(option) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = VeloraInk,
                            selectedLabelColor = VeloraGoldLight
                        )
                    )
                }
            }
        }

        OutlinedTextField(
            value = formState.notes,
            onValueChange = onNotesChange,
            label = { Text("Project Notes (Optional)") },
            placeholder = { Text("Share room sizes, possession month, or lighting preferences...") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VeloraGold,
                focusedLabelColor = VeloraGoldDark
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("contact_notes_input")
        )

        if (formState.errorMessage != null) {
            Text(
                text = formState.errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = onSubmit,
            enabled = !formState.isSubmitting,
            colors = ButtonDefaults.buttonColors(
                containerColor = VeloraGold,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(30.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("submit_consultation_button")
        ) {
            Text(
                text = if (formState.isSubmitting) "Booking Consultation..." else "Request Free Quote & Callback",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultationModalSheet(
    formState: ConsultationFormState,
    onDismiss: () -> Unit,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onPropertyTypeChange: (String) -> Unit,
    onScopeChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismissSuccess: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "FREE CONSULTATION",
                        style = MaterialTheme.typography.labelMedium,
                        color = VeloraGold
                    )
                    EditorialHeading(
                        prefix = "Get a",
                        italicAccent = "Quote",
                        fontSizeSp = 28
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close quote form"
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            ConsultationFormContent(
                formState = formState,
                onNameChange = onNameChange,
                onPhoneChange = onPhoneChange,
                onCityChange = onCityChange,
                onPropertyTypeChange = onPropertyTypeChange,
                onScopeChange = onScopeChange,
                onNotesChange = onNotesChange,
                onSubmit = onSubmit,
                onDismissSuccess = onDismissSuccess
            )
        }
    }
}
