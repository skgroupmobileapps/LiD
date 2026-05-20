package de.skgroup.einburgerungstest.feature.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.core.model.Language
import de.skgroup.einburgerungstest.core.model.ThemeMode
import de.skgroup.einburgerungstest.designsystem.components.AppButton
import de.skgroup.einburgerungstest.designsystem.components.GermanFlagBar
import de.skgroup.einburgerungstest.designsystem.theme.PrimaryGreen
import kmpexam.resources.generated.resources.*
import org.jetbrains.compose.resources.stringResource


/**
 * Onboarding screen — shown on first launch.
 * Collects language preference, Bundesland, and theme selection.
 */
@Composable
fun OnboardingScreen(
    onComplete: (Language, FederalState) -> Unit = { _, _ -> },
    onLanguageChanged: (Language) -> Unit = {},
    onFederalStateChanged: (FederalState) -> Unit = {},
    onThemeChanged: (ThemeMode) -> Unit = {},
    selectedTheme: ThemeMode = ThemeMode.SYSTEM,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableIntStateOf(0) }
    var selectedLanguage by remember { mutableStateOf(Language.GERMAN) }
    var selectedState by remember { mutableStateOf(FederalState.BERLIN) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        GermanFlagBar(height = 6)

        Spacer(Modifier.height(32.dp))

        when (currentStep) {
            0 -> WelcomeStep(onNext = { currentStep = 1 })
            1 -> LanguageStep(
                selectedLanguage = selectedLanguage,
                onLanguageSelected = {
                    selectedLanguage = it
                    onLanguageChanged(it)
                },
                onNext = { currentStep = 2 }
            )
            2 -> StateStep(
                selectedState = selectedState,
                onStateSelected = {
                    selectedState = it
                    onFederalStateChanged(it)
                },
                onComplete = { currentStep = 3 }
            )
            3 -> ThemeStep(
                selectedTheme = selectedTheme,
                onThemeSelected = onThemeChanged,
                onComplete = { onComplete(selectedLanguage, selectedState) }
            )
        }
    }
}

@Composable
internal fun WelcomeStep(onNext: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(100.dp),
            shape = RoundedCornerShape(24.dp),
            color = PrimaryGreen.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("🇩🇪", style = MaterialTheme.typography.displayLarge)
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(Res.string.onboarding_welcome_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = stringResource(Res.string.onboarding_welcome_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(48.dp))

        AppButton(text = stringResource(Res.string.onboarding_get_started), trailingIcon = "›", onClick = onNext)
    }
}

@Composable
internal fun LanguageStep(
    selectedLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.onboarding_language_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.onboarding_language_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        Language.entries.forEach { lang ->
            val isSelected = lang == selectedLanguage
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) PrimaryGreen.copy(alpha = 0.1f)
                    else MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    if (isSelected) 2.dp else 1.dp,
                    if (isSelected) PrimaryGreen else MaterialTheme.colorScheme.outline
                ),
                onClick = { onLanguageSelected(lang) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = lang.displayName, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                    if (isSelected) {
                        Text("✓", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))

        AppButton(text = stringResource(Res.string.onboarding_continue), trailingIcon = "›", onClick = onNext)
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
internal fun StateStep(
    selectedState: FederalState,
    onStateSelected: (FederalState) -> Unit,
    onComplete: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.onboarding_state_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.onboarding_state_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(20.dp))

        // Scrollable state list
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(FederalState.entries) { state ->
                val isSelected = state == selectedState
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) PrimaryGreen.copy(alpha = 0.1f)
                        else MaterialTheme.colorScheme.surface
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        if (isSelected) 2.dp else 1.dp,
                        if (isSelected) PrimaryGreen else MaterialTheme.colorScheme.outline
                    ),
                    onClick = { onStateSelected(state) }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(state.displayName, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                        if (isSelected) {
                            Text("✓", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        AppButton(text = stringResource(Res.string.onboarding_continue), trailingIcon = "›", onClick = onComplete)
        Spacer(Modifier.height(24.dp))
    }
}

private data class ThemeOption(
    val mode: ThemeMode,
    val icon: String,
    val title: String,
    val desc: String,
)

@Composable
internal fun ThemeStep(
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    onComplete: () -> Unit
) {
    val themeOptions = listOf(
        ThemeOption(ThemeMode.LIGHT, "☀️", stringResource(Res.string.onboarding_theme_light), stringResource(Res.string.onboarding_theme_light_desc)),
        ThemeOption(ThemeMode.DARK, "🌙", stringResource(Res.string.onboarding_theme_dark), stringResource(Res.string.onboarding_theme_dark_desc)),
        ThemeOption(ThemeMode.SYSTEM, "⚙️", stringResource(Res.string.onboarding_theme_system), stringResource(Res.string.onboarding_theme_system_desc)),
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.onboarding_theme_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.onboarding_theme_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        // Split preview: light on left, dark on right
        ThemePreviewPanel(modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(24.dp))

        themeOptions.forEach { option ->
            ThemeOptionCard(
                option = option,
                isSelected = selectedTheme == option.mode,
                onClick = { onThemeSelected(option.mode) }
            )
            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.weight(1f))

        AppButton(text = stringResource(Res.string.onboarding_finish), trailingIcon = "🚀", onClick = onComplete)
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ThemePreviewPanel(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        // Light half
        Surface(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            color = Color(0xFFF5F5F5)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Mock toolbar
                Surface(
                    modifier = Modifier.fillMaxWidth().height(24.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = Color.White
                ) {}
                Spacer(Modifier.height(8.dp))
                // Mock card
                Surface(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(20.dp),
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFDCFCE7)
                            ) {}
                            Spacer(Modifier.width(6.dp))
                            Surface(modifier = Modifier.height(6.dp).weight(1f), shape = RoundedCornerShape(3.dp), color = Color(0xFFE5E7EB)) {}
                        }
                        Spacer(Modifier.height(8.dp))
                        Surface(modifier = Modifier.fillMaxWidth().height(6.dp), shape = RoundedCornerShape(3.dp), color = Color(0xFFE5E7EB)) {}
                        Spacer(Modifier.height(4.dp))
                        Surface(modifier = Modifier.fillMaxWidth(0.6f).height(6.dp), shape = RoundedCornerShape(3.dp), color = Color(0xFFE5E7EB)) {}
                    }
                }
            }
        }
        // Dark half
        Surface(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            color = Color(0xFF1A1A1A)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Surface(
                    modifier = Modifier.fillMaxWidth().height(24.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF2A2A2A)
                ) {}
                Spacer(Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF252525),
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(20.dp),
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF2E7D32)
                            ) {}
                            Spacer(Modifier.width(6.dp))
                            Surface(modifier = Modifier.height(6.dp).weight(1f), shape = RoundedCornerShape(3.dp), color = Color(0xFF3A3A3A)) {}
                        }
                        Spacer(Modifier.height(8.dp))
                        Surface(modifier = Modifier.fillMaxWidth().height(6.dp), shape = RoundedCornerShape(3.dp), color = Color(0xFF3A3A3A)) {}
                        Spacer(Modifier.height(4.dp))
                        Surface(modifier = Modifier.fillMaxWidth(0.6f).height(6.dp), shape = RoundedCornerShape(3.dp), color = Color(0xFF3A3A3A)) {}
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeOptionCard(
    option: ThemeOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) PrimaryGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
        animationSpec = tween(durationMillis = 200),
        label = "border"
    )
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) PrimaryGreen.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 200),
        label = "container"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon container
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(22.dp),
                color = if (isSelected) PrimaryGreen.copy(alpha = 0.15f)
                else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(option.icon, style = MaterialTheme.typography.titleLarge)
                }
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = option.desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Radio indicator
            Box(
                modifier = Modifier.size(22.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Surface(
                        modifier = Modifier.size(22.dp),
                        shape = RoundedCornerShape(11.dp),
                        color = PrimaryGreen
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("✓", style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Surface(
                        modifier = Modifier.size(22.dp),
                        shape = RoundedCornerShape(11.dp),
                        color = Color.Transparent,
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    ) {}
                }
            }
        }
    }
}

