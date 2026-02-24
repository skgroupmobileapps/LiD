package de.skabs.skgroup.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),     // Cards
    large = RoundedCornerShape(16.dp),      // Large cards, modals
    extraLarge = RoundedCornerShape(24.dp)  // Buttons, FABs
)

// Common shapes used throughout the app
val QuizCardShape = RoundedCornerShape(12.dp)
val ButtonShape = RoundedCornerShape(14.dp)
val BottomNavShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
val TopicCardShape = RoundedCornerShape(12.dp)
val StatCardShape = RoundedCornerShape(12.dp)
val ProgressBarShape = RoundedCornerShape(8.dp)
