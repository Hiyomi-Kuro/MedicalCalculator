package com.android.medicalcalculator

import kotlin.math.max
import kotlin.math.sqrt

data class BmiResult(val value: Double, val category: String)

fun calculateBmi(heightCm: Double, weightKg: Double): BmiResult? {
    if (heightCm !in 80.0..250.0 || weightKg !in 20.0..400.0) return null
    val value = weightKg / ((heightCm / 100.0) * (heightCm / 100.0))
    val category = when {
        value < 18.5 -> "体重过低"
        value < 24.0 -> "体重正常"
        value < 28.0 -> "超重"
        else -> "肥胖"
    }
    return BmiResult(value, category)
}

data class Fib4ResultModel(val value: Double)

fun calculateFib4(age: Double, ast: Double, alt: Double, platelets: Double): Fib4ResultModel? {
    if (age !in 18.0..120.0 || ast <= 0 || alt <= 0 || platelets <= 0) return null
    return Fib4ResultModel((age * ast) / (platelets * sqrt(alt)))
}

data class ChildPughInput(val bilirubin: Double, val albumin: Double, val inr: Double, val ascites: Int, val encephalopathy: Int)
data class ChildPughResultModel(val total: Int, val childClass: String)

fun calculateChildPugh(input: ChildPughInput): ChildPughResultModel? {
    if (input.bilirubin <= 0 || input.albumin <= 0 || input.inr <= 0 ||
        input.ascites !in 1..3 || input.encephalopathy !in 1..3
    ) return null
    val bilirubinScore = if (input.bilirubin < 34) 1 else if (input.bilirubin <= 51) 2 else 3
    val albuminScore = if (input.albumin > 35) 1 else if (input.albumin >= 28) 2 else 3
    val inrScore = if (input.inr < 1.7) 1 else if (input.inr <= 2.3) 2 else 3
    val total = bilirubinScore + albuminScore + inrScore + input.ascites + input.encephalopathy
    return ChildPughResultModel(total, if (total <= 6) "Child-Pugh A" else if (total <= 9) "Child-Pugh B" else "Child-Pugh C")
}

data class CdaiInputModel(
    val liquidStoolTotal7Days: Int,
    val abdominalPainDaily: List<Int>,
    val wellbeingDaily: List<Int>,
    val complicationCategories: Int,
    val antidiarrhealUsed: Boolean,
    val abdominalMassScore: Int,
    val sex: String,
    val hematocritPercent: Double,
    val heightCm: Double,
    val weightKg: Double
)

fun calculateCdai(input: CdaiInputModel): Double? {
    if (input.liquidStoolTotal7Days < 0 || input.abdominalPainDaily.size != 7 || input.wellbeingDaily.size != 7 ||
        input.abdominalPainDaily.any { it !in 0..3 } || input.wellbeingDaily.any { it !in 0..4 } ||
        input.complicationCategories < 0 || input.abdominalMassScore !in 0..2 ||
        input.sex !in setOf("male", "female") || input.hematocritPercent !in 0.0..100.0 ||
        input.heightCm <= 0 || input.weightKg <= 0
    ) return null
    val stool = input.liquidStoolTotal7Days * 2.0
    val pain = input.abdominalPainDaily.sum() * 5.0
    val wellbeing = input.wellbeingDaily.sum() * 7.0
    val complications = input.complicationCategories * 20.0
    val medication = if (input.antidiarrhealUsed) 30.0 else 0.0
    val mass = when (input.abdominalMassScore) {
        0 -> 0.0
        1 -> 20.0
        else -> 50.0
    }
    val expectedHct = if (input.sex == "male") 47.0 else 42.0
    val hct = max(0.0, expectedHct - input.hematocritPercent) * 6.0
    val coefficient = if (input.sex == "male") 22.1 else 20.8
    val meters = input.heightCm / 100.0
    val standardWeight = meters * meters * coefficient
    val weight = max(-10.0, 100.0 * (standardWeight - input.weightKg) / standardWeight)
    return stool + pain + wellbeing + complications + medication + mass + hct + weight
}

data class SescdSegmentScore(val ulcerSize: Int, val ulceratedSurface: Int, val affectedSurface: Int, val stenosis: Int)

fun calculateSescd(segments: List<SescdSegmentScore>): Int? {
    if (segments.size != 5 || segments.any { it.ulcerSize !in 0..3 || it.ulceratedSurface !in 0..3 || it.affectedSurface !in 0..3 || it.stenosis !in 0..3 }) return null
    val nonStenosis = segments.sumOf { it.ulcerSize + it.ulceratedSurface + it.affectedSurface }
    val stenosis = minOf(11, segments.sumOf { it.stenosis })
    return nonStenosis + stenosis
}

fun calculateMayoScore(scores: List<Int>): Int? = if (scores.size in 3..4 && scores.all { it in 0..3 }) scores.sum() else null
