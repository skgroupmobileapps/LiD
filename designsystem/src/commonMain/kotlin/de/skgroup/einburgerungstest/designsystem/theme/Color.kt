package de.skgroup.einburgerungstest.designsystem.theme

import androidx.compose.ui.graphics.Color

// Primary — Forest Green palette
val PrimaryGreen = Color(0xFF1B5E3B)
val PrimaryGreenDark = Color(0xFF0D3B25)
val PrimaryGreenLight = Color(0xFF2D8F5E)
val PrimaryGreenSurface = Color(0xFFE8F5E9)

// Background & Surface
val BackgroundCream = Color(0xFFFFF8F0)
val BackgroundDark = Color(0xFF121212)
val SurfaceWhite = Color(0xFFFFFFFF)
val SurfaceDark = Color(0xFF1E1E1E)
val SurfaceCardDark = Color(0xFF2C2C2C)

// Borders
val CardBorder = Color(0xFFE8E0D8)
val CardBorderDark = Color(0xFF3C3C3C)

// Text
val TextPrimary = Color(0xFF1A1A1A)
val TextPrimaryDark = Color(0xFFF5F5F5)
val TextSecondary = Color(0xFF6B6B6B)
val TextSecondaryDark = Color(0xFFB0B0B0)

// Accents
val AccentGold = Color(0xFFFFB800)
val AccentRed = Color(0xFFE53E3E)
val AccentPurple = Color(0xFF7B61FF)
val AccentOrange = Color(0xFFFF8C42)
val AccentPink = Color(0xFFFF6B9D)
val AccentTeal = Color(0xFF38B2AC)

// Semantic — light-mode state surfaces
val SuccessGreen = Color(0xFF38A169)
val SuccessGreenLight = Color(0xFFE6F7ED)
val ErrorRed = Color(0xFFE53E3E)
val ErrorRedLight = Color(0xFFFFF0F0)
val WarningYellow = Color(0xFFECC94B)

// Semantic — dark-mode state surfaces (opaque dark tints, readable with white text)
val SuccessGreenSurfaceDark = Color(0xFF1B3A2E) // white text: ~16:1
val ErrorRedSurfaceDark = Color(0xFF3D1515)     // white text: ~17:1
val PrimaryGreenSurfaceDark = Color(0xFF0D2E1A) // white text: ~17:1

// Semantic — readable accent text on dark surfaces
val SuccessGreenTextDark = Color(0xFF68D391)    // on SurfaceDark: ~7.3:1

// Accents — darker gold variant for readable text on light backgrounds
// AccentGold (#FFB800) on white = 1.70:1 (fails WCAG); this passes 4.5:1
val AccentGoldDark = Color(0xFFB7860B)

// Semantic — readable error text on light error surfaces
// ErrorRed (#E53E3E) on ErrorRedLight (#FFF0F0) = 3.56:1 (marginal for normal text)
// ErrorRedDark on ErrorRedLight = 5.6:1 (passes WCAG AA)
val ErrorRedDark = Color(0xFFB71C1C)

// German flag
val GermanBlack = Color(0xFF000000)
val GermanRed = Color(0xFFDD0000)
val GermanGold = Color(0xFFFFCC00)

// Topic-specific colors (matching mockup icon backgrounds)
val TopicDemocracy = Color(0xFF1B5E3B)
val TopicRights = Color(0xFF7B61FF)
val TopicHistory = Color(0xFFFF8C42)
val TopicSociety = Color(0xFFE53E3E)
val TopicSymbols = Color(0xFFFF6B9D)
val TopicState = Color(0xFF38B2AC)
