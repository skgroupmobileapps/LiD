package de.skgroup.einburgerungstest.core.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Coroutine-based countdown timer.
 *
 * Emits remaining time in milliseconds every second.
 */
object Timer {

    /**
     * Create a countdown timer flow.
     *
     * @param totalTimeMs Total time in milliseconds
     * @param intervalMs Tick interval in milliseconds (default 1 second)
     * @return Flow emitting remaining time in ms, ending with 0
     */
    fun countdown(
        totalTimeMs: Long,
        intervalMs: Long = 1000L
    ): Flow<Long> = flow {
        var remaining = totalTimeMs
        emit(remaining)
        while (remaining > 0) {
            delay(intervalMs)
            remaining = (remaining - intervalMs).coerceAtLeast(0)
            emit(remaining)
        }
    }

    /**
     * Format milliseconds into "MM:SS" string.
     */
    fun formatTime(timeMs: Long): String {
        val totalSeconds = timeMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    }

    /**
     * Format milliseconds into "HH:MM:SS" string.
     */
    fun formatTimeLong(timeMs: Long): String {
        val totalSeconds = timeMs / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    }
}
