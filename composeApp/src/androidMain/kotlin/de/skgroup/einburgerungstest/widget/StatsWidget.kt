package de.skgroup.einburgerungstest.widget

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import de.skgroup.einburgerungstest.MainActivity
import de.skgroup.einburgerungstest.R
import de.skgroup.einburgerungstest.core.model.WidgetStats
import de.skgroup.einburgerungstest.core.navigation.Deeplinks
import de.skgroup.einburgerungstest.data.widget.WidgetDataExporter

/** Design colors */
private val BackgroundCream = Color(0xFFFFF8F0)
private val PrimaryGreen = Color(0xFF1B5E3B)
private val PrimaryGreenSurface = Color(0xFFE8F5E9)
private val TextPrimary = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF6B6B6B)

/**
 * Android home screen widget displaying learning statistics.
 * Uses Jetpack Glance for declarative widget UI.
 */
class StatsWidget : GlanceAppWidget() {
    
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val stats = try {
            WidgetDataExporter(context).readStats()
        } catch (_: Exception) {
            WidgetStats()
        }
        
        provideContent {
            StatsWidgetContent(context, stats)
        }
    }
}

@Composable
private fun StatsWidgetContent(context: Context, stats: WidgetStats) {
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(ColorProvider(BackgroundCream))
            .padding(12.dp)
    ) {
        // Stats Row - takes available space
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .defaultWeight(),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            StatItem(
                icon = "✓",
                value = stats.correctAnswers.toString(),
                label = context.getString(R.string.widget_correct),
                iconColor = ColorProvider(PrimaryGreen)
            )
            StatItem(
                icon = "🎯",
                value = "${stats.accuracyPercent.toInt()}%",
                label = context.getString(R.string.widget_accuracy)
            )
            StatItem(
                icon = "🔥",
                value = stats.dayStreak.toString(),
                label = context.getString(R.string.widget_streak)
            )
        }
        
        Spacer(GlanceModifier.height(8.dp))
        
        // Buttons Row
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            ActionButton(
                text = context.getString(R.string.widget_learn_button),
                backgroundColor = ColorProvider(PrimaryGreen),
                textColor = ColorProvider(Color.White),
                onClick = actionStartActivity(
                    Intent(context, MainActivity::class.java).apply {
                        action = Intent.ACTION_VIEW
                        data = Uri.parse(Deeplinks.LEARN)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                ),
                modifier = GlanceModifier.defaultWeight().padding(end = 4.dp)
            )
            ActionButton(
                text = context.getString(R.string.widget_exam_button),
                backgroundColor = ColorProvider(PrimaryGreenSurface),
                textColor = ColorProvider(PrimaryGreen),
                onClick = actionStartActivity(
                    Intent(context, MainActivity::class.java).apply {
                        action = Intent.ACTION_VIEW
                        data = Uri.parse(Deeplinks.EXAM)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                ),
                modifier = GlanceModifier.defaultWeight().padding(start = 4.dp)
            )
        }
    }
}

@Composable
private fun RowScope.StatItem(
    icon: String,
    value: String,
    label: String,
    iconColor: ColorProvider = ColorProvider(TextPrimary)
) {
    Column(
        modifier = GlanceModifier.defaultWeight(),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        Text(icon, style = TextStyle(fontSize = 18.sp, color = iconColor))
        Spacer(GlanceModifier.height(2.dp))
        Text(
            value,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ColorProvider(TextPrimary)
            )
        )
        Spacer(GlanceModifier.height(2.dp))
        Text(
            label,
            style = TextStyle(fontSize = 10.sp, color = ColorProvider(TextSecondary))
        )
    }
}

@Composable
private fun ActionButton(
    text: String,
    backgroundColor: ColorProvider,
    textColor: ColorProvider,
    onClick: androidx.glance.action.Action,
    modifier: GlanceModifier = GlanceModifier
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .cornerRadius(8.dp)
            .background(backgroundColor)
            .clickable(onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            style = TextStyle(
                color = textColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
