package com.android.medicalcalculator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CoreCalculatorsTest {
    @Test
    fun bmi_fib4_and_child_pugh_boundaries() {
        assertEquals("体重正常", calculateBmi(170.0, 65.0)?.category)
        assertNull(calculateBmi(0.0, 65.0))
        assertEquals(1.41, calculateFib4(50.0, 50.0, 50.0, 250.0)?.value!!, 0.01)
        assertNull(calculateFib4(50.0, 0.0, 50.0, 250.0))
        assertEquals("Child-Pugh A", calculateChildPugh(ChildPughInput(20.0, 40.0, 1.2, 1, 1))?.childClass)
        assertEquals("Child-Pugh C", calculateChildPugh(ChildPughInput(60.0, 20.0, 3.0, 3, 3))?.childClass)
    }

    @Test
    fun cdai_sescd_and_mayo_ranges() {
        val base = CdaiInputModel(0, List(7) { 0 }, List(7) { 0 }, 0, false, 0, "male", 47.0, 170.0, 100.0)
        assertEquals(-10.0, calculateCdai(base)!!, 0.01)
        assertEquals(56, calculateSescd(List(5) { SescdSegmentScore(3, 3, 3, 3) }))
        assertNull(calculateSescd(emptyList()))
        assertEquals(12, calculateMayoScore(listOf(3, 3, 3, 3)))
        assertNull(calculateMayoScore(listOf(0, 1)))
    }
}
