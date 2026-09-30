package com.android.medicalcalculator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class IbdCalculatorsTest {
    @Test
    fun hbi_calculates_and_rejects_invalid_values() {
        assertEquals(0, calculateHbi(HbiInput(0, 0, 0, 0, 0))?.score)
        assertEquals(20, calculateHbi(HbiInput(4, 3, 10, 2, 1))?.score)
        assertEquals(5, calculateHbi(HbiInput(2, 1, 1, 1, 0))?.score)
        assertNull(calculateHbi(HbiInput(5, 0, 0, 0, 0)))
    }

    @Test
    fun cdeis_uses_only_explored_segments_and_stenosis_addons() {
        val one = CdeisSegment(true, deepUlceration = true, superficialUlceration = false, diseaseSurface = 10.0, ulceratedSurface = 10.0)
        assertEquals(35.0, calculateCdeis(listOf(one), true, false)?.score)
        assertEquals(0.0, calculateCdeis(listOf(CdeisSegment(true, diseaseSurface = 0.0, ulceratedSurface = 0.0)), false, false)?.score)
        assertNull(calculateCdeis(listOf(CdeisSegment(explored = false)), false, false))
        assertNull(calculateCdeis(listOf(CdeisSegment(true, diseaseSurface = 11.0, ulceratedSurface = 0.0)), false, false))
    }

    @Test
    fun uceis_boundary_and_interpretation() {
        assertEquals(0, calculateUceis(UceisInput(0, 0, 0))?.score)
        assertEquals(8, calculateUceis(UceisInput(2, 3, 3))?.score)
        assertEquals("重度内镜活动（常用研究分层 7–8）", calculateUceis(UceisInput(2, 3, 2))?.interpretation)
        assertNull(calculateUceis(UceisInput(3, 0, 0)))
    }

    @Test
    fun sccai_and_pucai_cover_cutoffs() {
        assertEquals(0, calculateSccai(SccaiInput(3, 0, 0, 0, 0, 0))?.score)
        assertEquals(2, calculateSccai(SccaiInput(4, 1, 0, 0, 0, 0))?.score)
        assertEquals(85, calculatePucai(PucaiInput(10, 30, 10, 9, true, 10))?.score)
        assertEquals(0, calculatePucai(PucaiInput(0, 0, 0, 2, false, 0))?.score)
    }

    @Test
    fun oxford_and_truelove_witts_do_not_overstate_decision() {
        assertTrue(evaluateOxfordDay3(9, 0.0)?.meetsCriteria == true)
        assertTrue(evaluateOxfordDay3(5, 46.0)?.meetsCriteria == true)
        assertFalse(evaluateOxfordDay3(5, 45.0)?.meetsCriteria == true)
        assertEquals(TrueloveWittsSeverity.SEVERE, classifyTrueloveWitts(TrueloveWittsInput(6, true, 38.0, 100, 10.0, 40.0)))
        assertEquals(TrueloveWittsSeverity.MILD, classifyTrueloveWitts(TrueloveWittsInput(3, false, 37.0, 70, 12.0, 10.0)))
    }

    @Test
    fun pediatric_and_histology_scores_match_known_ranges() {
        val pcdai = PcdaiInput(0, 0, 0, 0, 0, 0, 0, 0, 0.0, 0.0, 0.0)
        assertEquals(0.0, calculatePcdai(pcdai)?.score!!, 0.0)
        assertEquals(100.0, calculatePcdai(PcdaiInput(10, 10, 10, 10, 10, 10, 10, 10, 5.0, 5.0, 10.0))?.score!!, 0.0)
        assertEquals(33, calculateRhi(RhiInput(3, 3, 3, 3)))
        assertNull(calculateRhi(RhiInput(4, 0, 0, 0)))
        assertEquals("4：溃疡", classifyNancy(4))
        assertNull(classifyNancy(5))
    }

    @Test
    fun classification_tools_preserve_modifiers() {
        val result = classifyMontrealCrohn(MontrealCrohnInput(30, "L3", true, "B1", true))
        assertEquals("A2 L3+L4 B1p", result?.code)
        assertEquals("E2：左侧结肠炎，病变未超过脾曲", classifyMontrealUc("E2"))
        assertEquals("i2a", RutgeertsGrade.I2A.label)
    }
}
