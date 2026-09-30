package com.android.medicalcalculator

import kotlin.math.round

private fun oneDecimal(value: Double): Double = round(value * 10.0) / 10.0

data class HbiInput(
    val generalWellBeing: Int,
    val abdominalPain: Int,
    val liquidStoolsPerDay: Int,
    val abdominalMass: Int,
    val complications: Int
)

data class HbiResult(val score: Int, val interpretation: String)

fun calculateHbi(input: HbiInput): HbiResult? {
    if (input.generalWellBeing !in 0..4 ||
        input.abdominalPain !in 0..3 ||
        input.liquidStoolsPerDay < 0 ||
        input.abdominalMass !in 0..2 ||
        input.complications < 0
    ) return null

    val score = input.generalWellBeing + input.abdominalPain +
        input.liquidStoolsPerDay + input.abdominalMass + input.complications
    val interpretation = when {
        score < 5 -> "常用解释：临床缓解（HBI < 5）"
        score <= 7 -> "轻度活动（常用研究分层：5–7）"
        score <= 16 -> "中度活动（常用研究分层：8–16）"
        else -> "重度活动（常用研究分层：>16）"
    }
    return HbiResult(score, interpretation)
}

data class CdeisSegment(
    val explored: Boolean = false,
    val deepUlceration: Boolean = false,
    val superficialUlceration: Boolean = false,
    val diseaseSurface: Double? = null,
    val ulceratedSurface: Double? = null
)

data class CdeisResult(
    val score: Double,
    val exploredSegments: Int,
    val interpretation: String
)

fun calculateCdeis(
    segments: List<CdeisSegment>,
    ulceratedStenosis: Boolean,
    nonUlceratedStenosis: Boolean
): CdeisResult? {
    val explored = segments.filter { it.explored }
    if (explored.isEmpty()) return null
    if (explored.any {
            it.diseaseSurface == null || it.ulceratedSurface == null ||
                it.diseaseSurface !in 0.0..10.0 || it.ulceratedSurface !in 0.0..10.0
        }) return null

    val total = explored.sumOf { segment ->
        (if (segment.deepUlceration) 12.0 else 0.0) +
            (if (segment.superficialUlceration) 6.0 else 0.0) +
            (segment.diseaseSurface ?: 0.0) +
            (segment.ulceratedSurface ?: 0.0)
    }
    val score = total / explored.size +
        (if (ulceratedStenosis) 3.0 else 0.0) +
        (if (nonUlceratedStenosis) 3.0 else 0.0)
    val interpretation = when {
        score < 3.0 -> "常用研究分层：内镜缓解（<3）"
        score <= 9.0 -> "常用研究分层：轻度内镜活动（3–9）"
        score <= 12.0 -> "常用研究分层：中度内镜活动（10–12）"
        else -> "常用研究分层：重度内镜活动（>12）"
    }
    return CdeisResult(oneDecimal(score), explored.size, interpretation)
}

enum class RutgeertsGrade(
    val label: String,
    val description: String,
    val modified: Boolean = false
) {
    I0("i0", "回肠末端无病变"),
    I1("i1", "不超过 5 个阿弗他溃疡"),
    I2("i2", "超过 5 个阿弗他溃疡且病变间黏膜正常，或跳跃分布的大病变/溃疡；传统评分也包括局限于回结肠吻合口的病变"),
    I2A("i2a", "病变局限于回结肠吻合口（modified Rutgeerts）", modified = true),
    I2B("i2b", "新末端回肠有符合 i2 的病变，可伴吻合口病变（modified Rutgeerts）", modified = true),
    I3("i3", "弥漫性阿弗他回肠炎，病变间黏膜弥漫性炎症"),
    I4("i4", "弥漫性炎症，伴大溃疡、结节/鹅卵石样改变和/或狭窄")
}

fun rutgeertsInterpretation(grade: RutgeertsGrade): String = when (grade) {
    RutgeertsGrade.I0 -> "无内镜复发"
    RutgeertsGrade.I1, RutgeertsGrade.I2, RutgeertsGrade.I2A, RutgeertsGrade.I2B -> "轻度内镜复发分层；i2 的预后含义及 i2a/i2b 的差异仍需结合具体研究和临床情况"
    RutgeertsGrade.I3, RutgeertsGrade.I4 -> "中重度内镜复发分层"
}

data class PdaiInput(
    val discharge: Int,
    val painOrActivityRestriction: Int,
    val sexualActivityRestriction: Int,
    val diseaseType: Int,
    val induration: Int
)

fun calculatePdai(input: PdaiInput): Int? {
    val values = listOf(
        input.discharge,
        input.painOrActivityRestriction,
        input.sexualActivityRestriction,
        input.diseaseType,
        input.induration
    )
    return if (values.all { it in 0..4 }) values.sum() else null
}

data class UceisInput(val vascularPattern: Int, val bleeding: Int, val erosionsAndUlcers: Int)
data class UceisResult(val score: Int, val interpretation: String)

fun calculateUceis(input: UceisInput): UceisResult? {
    if (input.vascularPattern !in 0..2 || input.bleeding !in 0..3 || input.erosionsAndUlcers !in 0..3) return null
    val score = input.vascularPattern + input.bleeding + input.erosionsAndUlcers
    val interpretation = when (score) {
        0 -> "无明显内镜活动（重标定版 UCEIS 0）"
        in 1..3 -> "轻度内镜活动（常用研究分层 1–3）"
        in 4..6 -> "中度内镜活动（常用研究分层 4–6）"
        else -> "重度内镜活动（常用研究分层 7–8）"
    }
    return UceisResult(score, interpretation)
}

data class SccaiInput(
    val daytimeFrequency: Int,
    val nocturnalFrequency: Int,
    val urgency: Int,
    val bloodInStool: Int,
    val generalWellBeing: Int,
    val extracolonicManifestations: Int
)

data class SccaiResult(val score: Int, val interpretation: String)

fun calculateSccai(input: SccaiInput): SccaiResult? {
    if (input.daytimeFrequency < 0 || input.nocturnalFrequency < 0 ||
        input.urgency !in 0..3 || input.bloodInStool !in 0..3 ||
        input.generalWellBeing !in 0..4 || input.extracolonicManifestations < 0
    ) return null
    val daytime = when {
        input.daytimeFrequency <= 3 -> 0
        input.daytimeFrequency <= 6 -> 1
        input.daytimeFrequency <= 9 -> 2
        else -> 3
    }
    val nocturnal = when {
        input.nocturnalFrequency == 0 -> 0
        input.nocturnalFrequency <= 3 -> 1
        input.nocturnalFrequency <= 6 -> 2
        else -> 3
    }
    val score = daytime + nocturnal + input.urgency + input.bloodInStool +
        input.generalWellBeing + input.extracolonicManifestations
    return SccaiResult(
        score,
        if (score < 2) "常用解释：临床缓解（SCCAI < 2）" else "SCCAI ≥ 2：请结合症状、炎症指标和客观评估"
    )
}

enum class TrueloveWittsSeverity { MILD, MODERATE, SEVERE }

data class TrueloveWittsInput(
    val stoolsPerDay: Int,
    val bloodyStools: Boolean,
    val temperatureC: Double,
    val pulsePerMinute: Int,
    val hemoglobinGPerDl: Double,
    val esrMmPerHour: Double
)

fun classifyTrueloveWitts(input: TrueloveWittsInput): TrueloveWittsSeverity? {
    if (input.stoolsPerDay < 0 || input.temperatureC <= 0 || input.pulsePerMinute < 0 ||
        input.hemoglobinGPerDl <= 0 || input.esrMmPerHour < 0
    ) return null
    val systemicToxicity = input.temperatureC > 37.8 ||
        input.pulsePerMinute > 90 || input.hemoglobinGPerDl < 10.5 || input.esrMmPerHour > 30
    return when {
        input.stoolsPerDay >= 6 && input.bloodyStools && systemicToxicity -> TrueloveWittsSeverity.SEVERE
        input.stoolsPerDay < 4 && !systemicToxicity -> TrueloveWittsSeverity.MILD
        else -> TrueloveWittsSeverity.MODERATE
    }
}

data class OxfordResult(val meetsCriteria: Boolean, val explanation: String)

fun evaluateOxfordDay3(stoolFrequency: Int, crpMgPerL: Double): OxfordResult? {
    if (stoolFrequency < 0 || crpMgPerL < 0) return null
    val meets = stoolFrequency > 8 || (stoolFrequency in 3..8 && crpMgPerL > 45.0)
    return OxfordResult(
        meets,
        if (meets) "满足 Oxford/Travis Day-3 高风险标准：>8 次/日，或 3–8 次/日且 CRP >45 mg/L。"
        else "未满足 Oxford/Travis Day-3 高风险标准。"
    )
}

data class PucaiInput(
    val abdominalPain: Int,
    val rectalBleeding: Int,
    val stoolConsistency: Int,
    val stoolsPer24h: Int,
    val nocturnalStools: Boolean,
    val activityLevel: Int
)

data class PucaiResult(val score: Int, val interpretation: String)

fun calculatePucai(input: PucaiInput): PucaiResult? {
    if (input.abdominalPain !in 0..10 || input.rectalBleeding !in 0..30 ||
        input.stoolConsistency !in 0..10 || input.stoolsPer24h < 0 ||
        input.activityLevel !in 0..10
    ) return null
    val frequency = when {
        input.stoolsPer24h <= 2 -> 0
        input.stoolsPer24h <= 5 -> 5
        input.stoolsPer24h <= 8 -> 10
        else -> 15
    }
    val score = input.abdominalPain + input.rectalBleeding + input.stoolConsistency +
        frequency + (if (input.nocturnalStools) 10 else 0) + input.activityLevel
    val interpretation = when {
        score < 10 -> "缓解 / 无活动（PUCAI < 10）"
        score <= 34 -> "轻度活动（10–34）"
        score <= 64 -> "中度活动（35–64）"
        else -> "重度活动（≥65）"
    }
    return PucaiResult(score, interpretation)
}

data class PcdaiInput(
    val abdominalPain: Int,
    val stools: Int,
    val generalWellBeing: Int,
    val weight: Int,
    val height: Int,
    val abdominalExam: Int,
    val perianalDisease: Int,
    val extraintestinalManifestations: Int,
    val hematocrit: Double,
    val esr: Double,
    val albumin: Double
)

data class PcdaiResult(val score: Double, val interpretation: String)

fun calculatePcdai(input: PcdaiInput): PcdaiResult? {
    if (input.abdominalPain !in 0..10 || input.stools !in 0..10 || input.generalWellBeing !in 0..10 ||
        input.weight !in 0..10 || input.height !in 0..10 || input.abdominalExam !in 0..10 ||
        input.perianalDisease !in 0..10 || input.extraintestinalManifestations !in 0..10 ||
        input.hematocrit !in 0.0..5.0 || input.esr !in 0.0..5.0 || input.albumin !in 0.0..10.0
    ) return null
    val score = input.abdominalPain + input.stools + input.generalWellBeing + input.weight + input.height +
        input.abdominalExam + input.perianalDisease + input.extraintestinalManifestations +
        input.hematocrit + input.esr + input.albumin
    val interpretation = when {
        score <= 10.0 -> "常用解释：无活动 / 缓解（≤10）"
        score <= 30.0 -> "常用解释：轻度活动（11–30）"
        else -> "常用解释：中重度活动（>30）"
    }
    return PcdaiResult(score, interpretation)
}

fun classifyNancy(grade: Int): String? = when (grade) {
    0 -> "0：无明显组织学疾病"
    1 -> "1：慢性炎症浸润，无急性炎症浸润"
    2 -> "2：轻度急性炎症浸润"
    3 -> "3：中重度急性炎症浸润"
    4 -> "4：溃疡"
    else -> null
}

data class RhiInput(
    val chronicInflammation: Int,
    val laminaPropriaNeutrophils: Int,
    val epithelialNeutrophils: Int,
    val erosionOrUlceration: Int
)

fun calculateRhi(input: RhiInput): Int? {
    val values = listOf(input.chronicInflammation, input.laminaPropriaNeutrophils, input.epithelialNeutrophils, input.erosionOrUlceration)
    if (values.any { it !in 0..3 }) return null
    return input.chronicInflammation + 2 * input.laminaPropriaNeutrophils +
        3 * input.epithelialNeutrophils + 5 * input.erosionOrUlceration
}

data class GeboesSubgrade(val code: String, val label: String, val ordinal: Double)

val geboesSubgrades = listOf(
    GeboesSubgrade("0.0", "结构：无异常", 0.0),
    GeboesSubgrade("0.1", "结构：轻度异常", 0.1),
    GeboesSubgrade("0.2", "结构：轻/中度弥漫或多灶异常", 0.2),
    GeboesSubgrade("0.3", "结构：重度弥漫或多灶异常", 0.3),
    GeboesSubgrade("1.0", "慢性炎症：无增加", 1.0),
    GeboesSubgrade("1.1", "慢性炎症：轻度但明确增加", 1.1),
    GeboesSubgrade("1.2", "慢性炎症：中度增加", 1.2),
    GeboesSubgrade("1.3", "慢性炎症：显著增加", 1.3),
    GeboesSubgrade("2A.0", "固有层嗜酸性粒细胞：无增加", 2.0),
    GeboesSubgrade("2A.1", "固有层嗜酸性粒细胞：轻度但明确增加", 2.1),
    GeboesSubgrade("2A.2", "固有层嗜酸性粒细胞：中度增加", 2.2),
    GeboesSubgrade("2A.3", "固有层嗜酸性粒细胞：显著增加", 2.3),
    GeboesSubgrade("2B.0", "固有层中性粒细胞：无", 2.0),
    GeboesSubgrade("2B.1", "固有层中性粒细胞：轻度但明确增加", 2.1),
    GeboesSubgrade("2B.2", "固有层中性粒细胞：中度增加", 2.2),
    GeboesSubgrade("2B.3", "固有层中性粒细胞：显著增加", 2.3),
    GeboesSubgrade("3.0", "上皮中性粒细胞：无", 3.0),
    GeboesSubgrade("3.1", "上皮中性粒细胞：<5% 隐窝受累", 3.1),
    GeboesSubgrade("3.2", "上皮中性粒细胞：<50% 隐窝受累", 3.2),
    GeboesSubgrade("3.3", "上皮中性粒细胞：>50% 隐窝受累", 3.3),
    GeboesSubgrade("4.0", "隐窝破坏：无", 4.0),
    GeboesSubgrade("4.1", "隐窝破坏：可能，隐窝局部中性粒细胞过多", 4.1),
    GeboesSubgrade("4.2", "隐窝破坏：可能，明显变薄", 4.2),
    GeboesSubgrade("4.3", "隐窝破坏：明确", 4.3),
    GeboesSubgrade("5.0", "糜烂/溃疡：无", 5.0),
    GeboesSubgrade("5.1", "修复中的上皮伴邻近炎症", 5.1),
    GeboesSubgrade("5.2", "可能糜烂，局灶剥脱", 5.2),
    GeboesSubgrade("5.3", "明确糜烂", 5.3),
    GeboesSubgrade("5.4", "溃疡或肉芽组织", 5.4)
)

fun geboesInterpretation(code: String): String? {
    val item = geboesSubgrades.firstOrNull { it.code == code } ?: return null
    return when {
        item.ordinal <= 2.0 -> "常用研究解释：组织学缓解常采用 ≤2.0；具体终点需确认研究方案。"
        item.ordinal <= 3.0 -> "提示组织学活动可能存在；不同研究对反应/缓解界值定义不同。"
        else -> "提示活动性组织学改变；请结合病理报告全文。"
    }
}

data class MontrealCrohnInput(
    val ageAtDiagnosis: Int,
    val location: String,
    val upperGiModifier: Boolean,
    val behaviour: String,
    val perianalModifier: Boolean
)

data class MontrealCrohnResult(val code: String, val explanation: String)

fun classifyMontrealCrohn(input: MontrealCrohnInput): MontrealCrohnResult? {
    val age = when {
        input.ageAtDiagnosis <= 16 -> "A1"
        input.ageAtDiagnosis in 17..40 -> "A2"
        input.ageAtDiagnosis > 40 -> "A3"
        else -> return null
    }
    if (input.location !in setOf("L1", "L2", "L3", "L4") || input.behaviour !in setOf("B1", "B2", "B3")) return null
    val location = if (input.upperGiModifier && input.location != "L4") "${input.location}+L4" else input.location
    val behaviour = input.behaviour + if (input.perianalModifier) "p" else ""
    return MontrealCrohnResult(
        "$age $location $behaviour",
        "A：诊断年龄；L：病变部位；B：疾病行为；p：合并肛周病变修饰符。"
    )
}

fun classifyMontrealUc(extent: String): String? = when (extent) {
    "E1" -> "E1：直肠炎，病变局限于直肠"
    "E2" -> "E2：左侧结肠炎，病变未超过脾曲"
    "E3" -> "E3：广泛性结肠炎/全结肠炎，病变超过脾曲"
    else -> null
}
