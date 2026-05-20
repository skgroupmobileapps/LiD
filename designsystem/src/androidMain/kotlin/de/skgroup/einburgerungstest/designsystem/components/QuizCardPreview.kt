package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun QuizCardPreview() {
    PreviewSurface {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            QuizCard(label = "A", text = "Default answer option")
            QuizCard(label = "B", text = "Selected answer option", state = QuizCardState.SELECTED)
            QuizCard(label = "C", text = "Correct answer option", state = QuizCardState.CORRECT)
            QuizCard(label = "D", text = "Wrong answer option", state = QuizCardState.WRONG)
        }
    }
}

@Preview
@Composable
private fun QuizCardDarkPreview() {
    PreviewSurface(darkTheme = true) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            QuizCard(label = "A", text = "Default answer option")
            QuizCard(label = "B", text = "Selected answer option", state = QuizCardState.SELECTED)
            QuizCard(label = "C", text = "Correct answer option", state = QuizCardState.CORRECT)
            QuizCard(label = "D", text = "Wrong answer option", state = QuizCardState.WRONG)
        }
    }
}
