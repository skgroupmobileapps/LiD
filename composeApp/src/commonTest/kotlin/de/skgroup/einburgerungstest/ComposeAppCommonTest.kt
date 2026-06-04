package de.skgroup.einburgerungstest

import de.skgroup.einburgerungstest.navigation.decodeReviewQuestionIds
import de.skgroup.einburgerungstest.navigation.encodeReviewQuestionIds
import kotlin.test.Test
import kotlin.test.assertEquals

class ComposeAppCommonTest {

    @Test
    fun reviewIds_roundTrip_keepsAllMistakes() {
        val encoded = encodeReviewQuestionIds(listOf(11, 205, 77))
        val decoded = decodeReviewQuestionIds(encoded)

        assertEquals(listOf(11, 205, 77), decoded)
    }

    @Test
    fun reviewIds_legacyCommaPayload_staysSupported() {
        val decoded = decodeReviewQuestionIds("11,205,77")

        assertEquals(listOf(11, 205, 77), decoded)
    }
}