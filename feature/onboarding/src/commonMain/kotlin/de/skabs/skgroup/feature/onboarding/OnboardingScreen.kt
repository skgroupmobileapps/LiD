package de.skabs.skgroup.feature.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.skabs.skgroup.core.model.FederalState
import de.skabs.skgroup.core.model.Language
import de.skabs.skgroup.designsystem.components.AppButton
import de.skabs.skgroup.designsystem.components.GermanFlagBar
import de.skabs.skgroup.designsystem.theme.PrimaryGreen
import kmpexam.resources.generated.resources.*
import org.jetbrains.compose.resources.stringResource


/**
 * Onboarding screen — shown on first launch.
 * Collects language preference and Bundesland selection.
 */
@Composable

fun OnboardingScreen(
    onComplete: (Language, FederalState) -> Unit = { _, _ -> },
    onLanguageChanged: (Language) -> Unit = {},
    onFederalStateChanged: (FederalState) -> Unit = {},
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
                onComplete = { onComplete(selectedLanguage, selectedState) }
            )
        }
    }
}

@Composable
private fun WelcomeStep(onNext: () -> Unit) {
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
private fun LanguageStep(
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
                        Text("✓", style = MaterialTheme.typography.titleMedium, color = PrimaryGreen, fontWeight = FontWeight.Bold)
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
private fun StateStep(
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
                            Text("✓", color = PrimaryGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        AppButton(text = stringResource(Res.string.onboarding_finish), trailingIcon = "🚀", onClick = onComplete)
        Spacer(Modifier.height(24.dp))
    }
}
