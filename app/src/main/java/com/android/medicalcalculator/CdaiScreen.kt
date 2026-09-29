package com.android.medicalcalculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.round

private fun roundToOneDecimal(value: Double): Double = round(value * 10.0) / 10.0

private val cdaiComplications = listOf(
    "关节炎 / 关节痛",
    "虹膜炎 / 葡萄膜炎",
    "结节性红斑 / 坏疽性脓皮病 / 阿弗他口炎",
    "肛裂 / 肛周瘘 / 肛周脓肿",
    "其他瘘",
    "过去 7 天发热 > 37.8°C"
)

@Composable
private fun DayScoreRow(day: Int, selected: Int, maxScore: Int, onSelect: (Int) -> Unit) {
    Text("第 $day 天")
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        (0..maxScore).forEach { score ->
            if (selected == score) {
                Button(onClick = { onSelect(score) }, modifier = Modifier.weight(1f)) { Text(score.toString()) }
            } else {
                OutlinedButton(onClick = { onSelect(score) }, modifier = Modifier.weight(1f)) { Text(score.toString()) }
            }
        }
    }
    Spacer(Modifier.height(6.dp))
}

@Composable
fun CdaiScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var stoolCount by remember { mutableStateOf("") }
    var painDays by remember { mutableStateOf(List(7) { 0 }) }
    var wellbeingDays by remember { mutableStateOf(List(7) { 0 }) }
    var complications by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var antidiarrheal by remember { mutableStateOf<String?>(null) }
    var abdominalMass by remember { mutableStateOf<String?>(null) }
    var sex by remember { mutableStateOf<String?>(null) }
    var hct by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var actualWeight by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<Double?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    val stoolScore = stoolCount.toIntOrNull()?.takeIf { it >= 0 }?.times(2)
    val painScore = painDays.sum() * 5
    val wellbeingScore = wellbeingDays.sum() * 7
    val complicationScore = complications.size * 20
    val antidiarrhealScore = when (antidiarrheal) { "yes" -> 30; "no" -> 0; else -> null }
    val abdominalMassScore = when (abdominalMass) { "none" -> 0; "questionable" -> 20; "definite" -> 50; else -> null }

    val hctValue = hct.toDoubleOrNull()
    val hctScore = if (sex != null && hctValue != null && hctValue > 0 && hctValue <= 100) {
        val expected = if (sex == "male") 47.0 else 42.0
        roundToOneDecimal(max(0.0, expected - hctValue) * 6.0)
    } else null

    val heightValue = height.toDoubleOrNull()
    val standardWeight = if (sex != null && heightValue != null && heightValue > 0) {
        val m = heightValue / 100.0
        val coefficient = if (sex == "male") 22.1 else 20.8
        m * m * coefficient
    } else null

    val actualWeightValue = actualWeight.toDoubleOrNull()
    val weightScore = if (standardWeight != null && standardWeight > 0 && actualWeightValue != null && actualWeightValue > 0) {
        roundToOneDecimal(max(-10.0, 100.0 * (standardWeight - actualWeightValue) / standardWeight))
    } else null

    fun clearResult() {
        result = null
        error = null
    }

    CalculatorPage(
        title = "Crohn 病活动指数（CDAI）",
        subtitle = "经典 8 项加权评分",
        onBack = onBack,
        onReference = onReference,
        scopeNotice = "第 2、3 项默认每天为 0 分，分别代表无腹痛和总体健康状况良好；如实际情况不同，请逐日修改。"
    ) {
        SectionCard("1. 7 天稀便或软便总次数") {
            NumericField("7 天总次数", stoolCount, {
                stoolCount = it
                clearResult()
            }, "次", integerOnly = true, placeholder = "请输入 7 天总次数")
            stoolScore?.let { Text("本项加权得分：$it") }
        }

        SectionCard("2. 7 天腹痛评分") {
            Text("0 无；1 轻度；2 中度；3 重度")
            Spacer(Modifier.height(8.dp))
            painDays.forEachIndexed { index, selected ->
                DayScoreRow(index + 1, selected, 3) { score ->
                    painDays = painDays.toMutableList().also { it[index] = score }
                    clearResult()
                }
            }
            Text("本项加权得分：$painScore")
        }

        SectionCard("3. 7 天总体健康状况评分") {
            Text("0 良好；1 稍差；2 差；3 很差；4 极差")
            Spacer(Modifier.height(8.dp))
            wellbeingDays.forEachIndexed { index, selected ->
                DayScoreRow(index + 1, selected, 4) { score ->
                    wellbeingDays = wellbeingDays.toMutableList().also { it[index] = score }
                    clearResult()
                }
            }
            Text("本项加权得分：$wellbeingScore")
        }

        SectionCard("4. 相关并发症") {
            Text("每个类别计 1 项，可多选")
            cdaiComplications.forEachIndexed { index, label ->
                ChoiceButton(index in complications, label, if (index in complications) "已选择" else null) {
                    complications = if (index in complications) complications - index else complications + index
                    clearResult()
                }
            }
            Text("已选择 ${complications.size} 项，本项加权得分：$complicationScore")
        }

        SectionCard("5. 是否使用止泻药或阿片类药物") {
            ChoiceButton(antidiarrheal == "no", "否", "0 分") { antidiarrheal = "no"; clearResult() }
            ChoiceButton(antidiarrheal == "yes", "是", "30 分") { antidiarrheal = "yes"; clearResult() }
            antidiarrhealScore?.let { Text("本项加权得分：$it") }
        }

        SectionCard("6. 腹部包块") {
            ChoiceButton(abdominalMass == "none", "无", "0 分") { abdominalMass = "none"; clearResult() }
            ChoiceButton(abdominalMass == "questionable", "可疑", "20 分") { abdominalMass = "questionable"; clearResult() }
            ChoiceButton(abdominalMass == "definite", "明确", "50 分") { abdominalMass = "definite"; clearResult() }
            abdominalMassScore?.let { Text("本项加权得分：$it") }
        }

        SectionCard("7. 血细胞比容 HCT") {
            Text("请先选择性别")
            ChoiceButton(sex == "male", "男性", "标准值 47%") { sex = "male"; clearResult() }
            ChoiceButton(sex == "female", "女性", "标准值 42%") { sex = "female"; clearResult() }
            NumericField("HCT", hct, { hct = it; clearResult() }, "%", placeholder = "请输入 HCT")
            hctScore?.let { Text("本项加权得分：$it") }
        }

        SectionCard("8. 体重偏差") {
            Text("性别沿用第 7 项，只需填写身高和当前体重")
            NumericField("身高", height, { height = it; clearResult() }, "cm", placeholder = "请输入身高")
            Spacer(Modifier.height(8.dp))
            NumericField("当前体重", actualWeight, { actualWeight = it; clearResult() }, "kg", placeholder = "请输入当前体重")
            standardWeight?.let { Text("自动计算标准体重：%.1f kg".format(it)) }
            weightScore?.let { Text("本项加权得分：$it") }
        }

        ErrorText(error)
        Button(onClick = {
            error = when {
                stoolScore == null -> "请填写第 1 项"
                antidiarrhealScore == null -> "请选择第 5 项"
                abdominalMassScore == null -> "请选择第 6 项"
                hctScore == null -> "请完成第 7 项"
                weightScore == null -> "请完成第 8 项"
                else -> null
            }
            if (error == null) {
                val total = stoolScore!!.toDouble() +
                    painScore +
                    wellbeingScore +
                    complicationScore +
                    antidiarrhealScore!! +
                    abdominalMassScore!! +
                    hctScore!! +
                    weightScore!!
                result = roundToOneDecimal(total)
            }
        }) { Text("计算 CDAI") }
        Spacer(Modifier.height(6.dp))
        OutlinedButton(onClick = {
            stoolCount = ""
            painDays = List(7) { 0 }
            wellbeingDays = List(7) { 0 }
            complications = emptySet()
            antidiarrheal = null
            abdominalMass = null
            sex = null
            hct = ""
            height = ""
            actualWeight = ""
            clearResult()
        }) { Text("清空") }

        result?.let { score ->
            val (activity, range) = when {
                score < 150 -> "临床缓解" to "CDAI < 150"
                score < 220 -> "轻度至中度活动" to "150 ≤ CDAI < 220"
                score <= 450 -> "中度至重度活动" to "220 ≤ CDAI ≤ 450"
                else -> "重度活动" to "CDAI > 450"
            }
            ResultCard(
                "CDAI 总分",
                score.toString(),
                listOf(
                    activity,
                    range,
                    "CDAI 主要反映临床症状活动度，不能单独替代内镜、影像、生物标志物及整体临床评估。"
                )
            )
        }
    }
}
