package com.android.medicalcalculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private data class IbdOption(val value: Int, val title: String, val description: String = "")

@Composable
private fun IbdOptions(selected: Int?, options: List<IbdOption>, onSelect: (Int) -> Unit) {
    options.forEach { option ->
        ChoiceButton(selected == option.value, option.title, option.description) { onSelect(option.value) }
    }
}

@Composable
fun HbiScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var wellBeing by rememberSaveable { mutableStateOf<Int?>(null) }
    var pain by rememberSaveable { mutableStateOf<Int?>(null) }
    var mass by rememberSaveable { mutableStateOf<Int?>(null) }
    var stools by rememberSaveable { mutableStateOf("") }
    var complications by rememberSaveable { mutableStateOf("") }
    val result = calculateHbi(
        HbiInput(wellBeing ?: -1, pain ?: -1, stools.toIntOrNull() ?: -1, mass ?: -1, complications.toIntOrNull() ?: -1)
    )

    IbdCalculatorPage("HBI", "Harvey-Bradshaw Index · Crohn 病临床活动度", onBack, onReference,
        "HBI 是 CDAI 的简化临床工具，不能替代内镜、影像或炎症生物标志物。") {
        SectionCard("总体健康状况") {
            IbdOptions(wellBeing, listOf(
                IbdOption(0, "总体感觉良好"), IbdOption(1, "稍差"), IbdOption(2, "差"),
                IbdOption(3, "很差"), IbdOption(4, "极差")
            )) { wellBeing = it }
        }
        SectionCard("腹痛") {
            IbdOptions(pain, listOf(IbdOption(0, "无"), IbdOption(1, "轻度"), IbdOption(2, "中度"), IbdOption(3, "重度"))) { pain = it }
        }
        SectionCard("过去 24 小时稀便 / 软便次数") {
            NumericField("液体或软便次数", stools, { stools = it }, "次", integerOnly = true, placeholder = "例如 4")
        }
        SectionCard("腹部包块") {
            IbdOptions(mass, listOf(IbdOption(0, "无"), IbdOption(1, "可疑"), IbdOption(2, "明确"))) { mass = it }
        }
        SectionCard("并发症数量") {
            NumericField("符合的并发症类别数", complications, { complications = it }, "项", integerOnly = true, placeholder = "0")
            Text("每个并发症类别计 1 分；不要把同一类别重复计数。")
        }
        ResetButton {
            wellBeing = null; pain = null; mass = null; stools = ""; complications = ""
        }
        result?.let { ResultCard("HBI 总分", "${it.score} 分", listOf(it.interpretation, "常用界值来自研究分层，不是单独的诊断或治疗决定标准。")) }
    }
}

@Composable
fun CdeisScreen(onBack: () -> Unit, onReference: () -> Unit) {
    val names = listOf("回肠", "右半结肠", "横结肠", "乙状结肠及左半结肠", "直肠")
    var explored by remember { mutableStateOf(List(5) { false }) }
    var deep by remember { mutableStateOf(List(5) { false }) }
    var superficial by remember { mutableStateOf(List(5) { false }) }
    var diseaseSurface by remember { mutableStateOf(List(5) { "" }) }
    var ulcerSurface by remember { mutableStateOf(List(5) { "" }) }
    var ulceratedStenosis by rememberSaveable { mutableStateOf(false) }
    var nonUlceratedStenosis by rememberSaveable { mutableStateOf(false) }

    fun updateBoolean(values: List<Boolean>, index: Int, value: Boolean): List<Boolean> = values.toMutableList().also { it[index] = value }
    fun updateText(values: List<String>, index: Int, value: String): List<String> = values.toMutableList().also { it[index] = value }

    val segments = names.indices.map { index ->
        CdeisSegment(explored[index], deep[index], superficial[index], diseaseSurface[index].toDoubleOrNull(), ulcerSurface[index].toDoubleOrNull())
    }
    val result = calculateCdeis(segments, ulceratedStenosis, nonUlceratedStenosis)

    IbdCalculatorPage("CDEIS", "Crohn's Disease Endoscopic Index of Severity", onBack, onReference,
        "按原始 CDEIS 逐段记录深/浅表溃疡、病变表面、溃疡表面和狭窄。只对实际完成检查的肠段计入分母；未评估不能当作 0 分。") {
        names.forEachIndexed { index, name ->
            SectionCard("${index + 1}. $name") {
                ChoiceButton(explored[index], if (explored[index]) "已评估该肠段" else "未评估：点击标记为已评估") {
                    explored = updateBoolean(explored, index, !explored[index])
                }
                if (explored[index]) {
                    ChoiceButton(deep[index], "存在深溃疡", "计入 12 分") { deep = updateBoolean(deep, index, !deep[index]) }
                    ChoiceButton(superficial[index], "存在浅表溃疡", "计入 6 分") { superficial = updateBoolean(superficial, index, !superficial[index]) }
                    NumericField("病变累及表面", diseaseSurface[index], { diseaseSurface = updateText(diseaseSurface, index, it) }, "0–10", placeholder = "视觉模拟量表")
                    Spacer(Modifier.height(8.dp))
                    NumericField("溃疡累及表面", ulcerSurface[index], { ulcerSurface = updateText(ulcerSurface, index, it) }, "0–10", placeholder = "视觉模拟量表")
                }
            }
        }
        SectionCard("狭窄（任何已评估肠段）") {
            ChoiceButton(ulceratedStenosis, "存在溃疡性狭窄", "加 3 分") { ulceratedStenosis = !ulceratedStenosis }
            ChoiceButton(nonUlceratedStenosis, "存在非溃疡性狭窄", "加 3 分") { nonUlceratedStenosis = !nonUlceratedStenosis }
        }
        ResetButton {
            explored = List(5) { false }; deep = List(5) { false }; superficial = List(5) { false }
            diseaseSurface = List(5) { "" }; ulcerSurface = List(5) { "" }
            ulceratedStenosis = false; nonUlceratedStenosis = false
        }
        result?.let { ResultCard("CDEIS 总分", "%.1f 分".format(it.score), listOf("已评估肠段：${it.exploredSegments}/5", it.interpretation, "CDEIS 常见范围 0–44；界值应结合研究或临床场景解释。")) }
    }
}

@Composable
fun RutgeertsScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var modified by rememberSaveable { mutableStateOf(false) }
    var selected by rememberSaveable { mutableStateOf<String?>(null) }
    val grades = if (modified) listOf(RutgeertsGrade.I0, RutgeertsGrade.I1, RutgeertsGrade.I2A, RutgeertsGrade.I2B, RutgeertsGrade.I3, RutgeertsGrade.I4)
    else listOf(RutgeertsGrade.I0, RutgeertsGrade.I1, RutgeertsGrade.I2, RutgeertsGrade.I3, RutgeertsGrade.I4)
    IbdCalculatorPage("Rutgeerts", "术后 Crohn 病内镜复发评分", onBack, onReference,
        "传统评分将吻合口与新末端回肠病变合并在 i2；modified Rutgeerts 将其分为 i2a（吻合口）和 i2b（新末端回肠）。") {
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (!modified) Button(onClick = { modified = false; selected = null }) { Text("传统") }
            else OutlinedButton(onClick = { modified = false; selected = null }) { Text("传统") }
            if (modified) Button(onClick = { modified = true; selected = null }) { Text("modified") }
            else OutlinedButton(onClick = { modified = true; selected = null }) { Text("modified") }
        }
        SectionCard("选择内镜表现") {
            grades.forEach { grade -> ChoiceButton(selected == grade.name, grade.label, grade.description) { selected = grade.name } }
        }
        ResetButton { selected = null }
        selected?.let { name ->
            val grade = RutgeertsGrade.valueOf(name)
            ResultCard("Rutgeerts 结果", grade.label, listOf(rutgeertsInterpretation(grade), "这是术后内镜分层，不是单独的临床症状评分。"))
        }
    }
}

@Composable
fun PdaiScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var values by remember { mutableStateOf(List(5) { -1 }) }
    val titles = listOf("分泌物", "疼痛 / 活动受限", "性生活受限", "肛周疾病类型", "硬结程度")
    val descriptions = listOf(
        listOf("无", "轻度", "中度", "明显", "重度"),
        listOf("无", "轻微，不影响活动", "影响部分活动", "明显限制活动", "无法进行活动"),
        listOf("无", "轻度", "中度", "明显", "重度"),
        listOf("无", "皮赘/裂隙", "瘘管", "脓肿或多发病变", "复杂/重度病变"),
        listOf("无", "轻度", "中度", "明显", "重度")
    )
    val score = calculatePdai(PdaiInput(values[0], values[1], values[2], values[3], values[4]))
    IbdCalculatorPage("PDAI", "Perianal Disease Activity Index · Crohn 肛周病活动度", onBack, onReference,
        "PDAI 包含 5 个维度，每项 0–4 分，总分 0–20 分；量表同时涉及症状、功能和体检判断。") {
        titles.forEachIndexed { index, title ->
            SectionCard(title) {
                IbdOptions(if (values[index] >= 0) values[index] else null, descriptions[index].mapIndexed { value, label -> IbdOption(value, label) }) {
                    values = values.toMutableList().also { list -> list[index] = it }
                }
            }
        }
        ResetButton { values = List(5) { -1 } }
        score?.let { ResultCard("PDAI 总分", "$it / 20 分", listOf("分数越高表示肛周疾病负担越重。", "PDAI 没有公认的单一普适治疗阈值。")) }
    }
}

@Composable
fun UceisScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var vascular by rememberSaveable { mutableStateOf<Int?>(null) }
    var bleeding by rememberSaveable { mutableStateOf<Int?>(null) }
    var erosions by rememberSaveable { mutableStateOf<Int?>(null) }
    val result = if (vascular != null && bleeding != null && erosions != null) calculateUceis(UceisInput(vascular!!, bleeding!!, erosions!!)) else null
    IbdCalculatorPage("UCEIS", "Ulcerative Colitis Endoscopic Index of Severity", onBack, onReference,
        "本页采用后续验证常用的 0–8 重标定版：血管模式 0–2、出血 0–3、糜烂/溃疡 0–3。原始描述曾用 1–11 编码。") {
        SectionCard("血管模式（0–2）") {
            IbdOptions(vascular, listOf(IbdOption(0, "正常", "血管分支清楚"), IbdOption(1, "斑片状消失", "血管模式部分消失"), IbdOption(2, "完全消失", "血管模式完全消失"))) { vascular = it }
        }
        SectionCard("出血（0–3）") {
            IbdOptions(bleeding, listOf(IbdOption(0, "无可见血"), IbdOption(1, "黏膜表面血点或血丝", "可冲洗"), IbdOption(2, "肠腔内轻度游离血"), IbdOption(3, "明显肠腔内血或冲洗后黏膜渗血"))) { bleeding = it }
        }
        SectionCard("糜烂和溃疡（0–3）") {
            IbdOptions(erosions, listOf(IbdOption(0, "无"), IbdOption(1, "糜烂", "≤5 mm 的浅表缺损"), IbdOption(2, "浅表溃疡", ">5 mm、纤维蛋白覆盖且仍浅表"), IbdOption(3, "深溃疡", "深部溃疡或广泛溃疡"))) { erosions = it }
        }
        ResetButton { vascular = null; bleeding = null; erosions = null }
        result?.let { ResultCard("UCEIS 总分", "${it.score} / 8 分", listOf(it.interpretation, "UCEIS 是内镜严重度指标；请结合完整内镜报告和临床情况。")) }
    }
}

@Composable
fun SccaiScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var day by rememberSaveable { mutableStateOf("") }
    var night by rememberSaveable { mutableStateOf("") }
    var urgency by rememberSaveable { mutableStateOf<Int?>(null) }
    var blood by rememberSaveable { mutableStateOf<Int?>(null) }
    var wellBeing by rememberSaveable { mutableStateOf<Int?>(null) }
    var extra by rememberSaveable { mutableStateOf("") }
    val result = if (day.toIntOrNull() != null && night.toIntOrNull() != null && urgency != null && blood != null && wellBeing != null && extra.toIntOrNull() != null) {
        calculateSccai(SccaiInput(day.toInt(), night.toInt(), urgency!!, blood!!, wellBeing!!, extra.toInt()))
    } else null
    IbdCalculatorPage("SCCAI", "Simple Clinical Colitis Activity Index · UC", onBack, onReference,
        "SCCAI 包括日间/夜间排便、排便紧迫感、血便、总体健康状况和肠外表现。肠外表现按每种表现 1 分。") {
        SectionCard("排便频率") {
            NumericField("日间排便次数", day, { day = it }, "次/日", integerOnly = true, placeholder = "0 或更高")
            Spacer(Modifier.height(8.dp))
            NumericField("夜间排便次数", night, { night = it }, "次/夜", integerOnly = true, placeholder = "0 表示无")
        }
        SectionCard("排便紧迫感") { IbdOptions(urgency, listOf(IbdOption(0, "无"), IbdOption(1, "需要赶快去厕所"), IbdOption(2, "立即需要"), IbdOption(3, "失禁"))) { urgency = it } }
        SectionCard("便中带血") { IbdOptions(blood, listOf(IbdOption(0, "无"), IbdOption(1, "痕量"), IbdOption(2, "偶尔明显血"), IbdOption(3, "通常明显血"))) { blood = it } }
        SectionCard("总体健康状况") { IbdOptions(wellBeing, listOf(IbdOption(0, "很好"), IbdOption(1, "略低于平时"), IbdOption(2, "差"), IbdOption(3, "很差"), IbdOption(4, "糟糕"))) { wellBeing = it } }
        SectionCard("肠外表现") { NumericField("肠外表现种类数", extra, { extra = it }, "种", integerOnly = true, placeholder = "0") }
        ResetButton { day = ""; night = ""; urgency = null; blood = null; wellBeing = null; extra = "" }
        result?.let { ResultCard("SCCAI 总分", "${it.score} 分", listOf(it.interpretation, "ECCO-ESGAR 常用临床缓解界值为 <2；不要把该界值当成诊断标准。")) }
    }
}

@Composable
fun TrueloveWittsScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var stools by rememberSaveable { mutableStateOf("") }
    var bloody by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var temperature by rememberSaveable { mutableStateOf("") }
    var pulse by rememberSaveable { mutableStateOf("") }
    var hemoglobin by rememberSaveable { mutableStateOf("") }
    var esr by rememberSaveable { mutableStateOf("") }
    val result = if (stools.toIntOrNull() != null && bloody != null && temperature.toDoubleOrNull() != null && pulse.toIntOrNull() != null && hemoglobin.toDoubleOrNull() != null && esr.toDoubleOrNull() != null) {
        classifyTrueloveWitts(TrueloveWittsInput(stools.toInt(), bloody!!, temperature.toDouble(), pulse.toInt(), hemoglobin.toDouble(), esr.toDouble()))
    } else null
    IbdCalculatorPage("Truelove–Witts", "经典 Truelove and Witts Severity Index · UC", onBack, onReference,
        "本页采用经典成人 Truelove–Witts 严重度版本：重度要求 ≥6 次血便/日并伴至少一项全身毒性指标。实际 ASUC 判断仍需完整评估。") {
        SectionCard("排便和血便") {
            NumericField("排便次数", stools, { stools = it }, "次/日", integerOnly = true, placeholder = "例如 6")
            IbdOptions(if (bloody == null) null else if (bloody == true) 1 else 0, listOf(IbdOption(0, "不是每次均为血便"), IbdOption(1, "血便"))) { bloody = it == 1 }
        }
        SectionCard("全身指标") {
            NumericField("体温", temperature, { temperature = it }, "°C", placeholder = "例如 37.5")
            NumericField("脉搏", pulse, { pulse = it }, "次/分", integerOnly = true, placeholder = "例如 90")
            NumericField("血红蛋白", hemoglobin, { hemoglobin = it }, "g/dL", placeholder = "例如 11")
            NumericField("ESR", esr, { esr = it }, "mm/h", placeholder = "例如 30")
        }
        ResetButton { stools = ""; bloody = null; temperature = ""; pulse = ""; hemoglobin = ""; esr = "" }
        result?.let {
            val label = when (it) { TrueloveWittsSeverity.MILD -> "轻度"; TrueloveWittsSeverity.MODERATE -> "中度"; TrueloveWittsSeverity.SEVERE -> "重度" }
            ResultCard("Truelove–Witts 分层", label, listOf("经典标准：轻度 <4 次/日且无全身毒性；重度 ≥6 次血便/日并有毒性；其余归中度。", "不能单独替代住院评估和 ASUC 管理路径。"))
        }
    }
}

@Composable
fun OxfordScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var stools by rememberSaveable { mutableStateOf("") }
    var crp by rememberSaveable { mutableStateOf("") }
    val result = if (stools.toIntOrNull() != null && crp.toDoubleOrNull() != null) evaluateOxfordDay3(stools.toInt(), crp.toDouble()) else null
    IbdCalculatorPage("Oxford / Travis", "ASUC Day-3 corticosteroid failure risk criteria", onBack, onReference,
        "这是第 3 天的风险预测辅助工具，不是单独决定治疗或手术的自动决策系统。需结合入院评估、感染排除、内镜和专科团队判断。") {
        SectionCard("Day 3 指标") {
            NumericField("第 3 天排便次数", stools, { stools = it }, "次/日", integerOnly = true, placeholder = "例如 6")
            NumericField("第 3 天 CRP", crp, { crp = it }, "mg/L", placeholder = "例如 45")
        }
        ResetButton { stools = ""; crp = "" }
        result?.let { ResultCard("Oxford / Travis 结果", if (it.meetsCriteria) "满足高风险标准" else "未满足高风险标准", listOf(it.explanation, "原始队列中满足标准者被报道有较高住院结肠切除风险；现代生物制剂时代的表现可能不同。")) }
    }
}

@Composable
fun PucaiScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var pain by rememberSaveable { mutableStateOf<Int?>(null) }
    var bleeding by rememberSaveable { mutableStateOf<Int?>(null) }
    var consistency by rememberSaveable { mutableStateOf<Int?>(null) }
    var stools by rememberSaveable { mutableStateOf("") }
    var nocturnal by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var activity by rememberSaveable { mutableStateOf<Int?>(null) }
    val result = if (pain != null && bleeding != null && consistency != null && stools.toIntOrNull() != null && nocturnal != null && activity != null) calculatePucai(PucaiInput(pain!!, bleeding!!, consistency!!, stools.toInt(), nocturnal!!, activity!!)) else null
    IbdCalculatorPage("PUCAI", "Pediatric Ulcerative Colitis Activity Index · 儿童 UC", onBack, onReference,
        "PUCAI 是儿童 UC 的非侵入性临床活动度指数，总分 0–85；不应用于成人 UC 或替代客观炎症评估。") {
        SectionCard("腹痛") { IbdOptions(pain, listOf(IbdOption(0, "无痛"), IbdOption(5, "疼痛但可以忽略"), IbdOption(10, "疼痛不能忽略"))) { pain = it } }
        SectionCard("直肠出血") { IbdOptions(bleeding, listOf(IbdOption(0, "无"), IbdOption(10, "少量，仅少于一半排便"), IbdOption(20, "少量，多数排便"), IbdOption(30, "大量，超过粪便一半"))) { bleeding = it } }
        SectionCard("大多数粪便的性状") { IbdOptions(consistency, listOf(IbdOption(0, "成形"), IbdOption(5, "部分成形"), IbdOption(10, "完全不成形"))) { consistency = it } }
        SectionCard("排便和活动") {
            NumericField("24 小时排便次数", stools, { stools = it }, "次", integerOnly = true, placeholder = "例如 5")
            IbdOptions(if (nocturnal == null) null else if (nocturnal == true) 1 else 0, listOf(IbdOption(0, "无夜间排便"), IbdOption(1, "有夜间排便"))) { nocturnal = it == 1 }
            IbdOptions(activity, listOf(IbdOption(0, "活动不受限"), IbdOption(5, "偶尔受限"), IbdOption(10, "活动严重受限"))) { activity = it }
        }
        ResetButton { pain = null; bleeding = null; consistency = null; stools = ""; nocturnal = null; activity = null }
        result?.let { ResultCard("PUCAI 总分", "${it.score} 分", listOf(it.interpretation, "儿童 UC 活动度工具；结果需结合年龄、病程、实验室和内镜评估。")) }
    }
}

@Composable
fun PcdaiScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var pain by rememberSaveable { mutableStateOf<Int?>(null) }
    var stools by rememberSaveable { mutableStateOf<Int?>(null) }
    var wellBeing by rememberSaveable { mutableStateOf<Int?>(null) }
    var weight by rememberSaveable { mutableStateOf<Int?>(null) }
    var height by rememberSaveable { mutableStateOf<Int?>(null) }
    var abdomen by rememberSaveable { mutableStateOf<Int?>(null) }
    var perianal by rememberSaveable { mutableStateOf<Int?>(null) }
    var extra by rememberSaveable { mutableStateOf<Int?>(null) }
    var hct by rememberSaveable { mutableStateOf<Double?>(null) }
    var esr by rememberSaveable { mutableStateOf<Double?>(null) }
    var albumin by rememberSaveable { mutableStateOf<Double?>(null) }
    val result = if (listOf(pain, stools, wellBeing, weight, height, abdomen, perianal, extra, hct, esr, albumin).all { it != null }) calculatePcdai(PcdaiInput(pain!!, stools!!, wellBeing!!, weight!!, height!!, abdomen!!, perianal!!, extra!!, hct!!, esr!!, albumin!!)) else null
    val ten = listOf(IbdOption(0, "0 分"), IbdOption(5, "5 分"), IbdOption(10, "10 分"))
    IbdCalculatorPage("PCDAI", "Pediatric Crohn's Disease Activity Index", onBack, onReference,
        "采用原始 PCDAI 的 0–100 分结构：病史、体格/生长、实验室和功能。HCT/ESR 使用 0、2.5、5 分；白蛋白使用 0、5、10 分。") {
        SectionCard("病史（回顾 1 周）") {
            Text("腹痛"); IbdOptions(pain, ten) { pain = it }
            Text("粪便"); IbdOptions(stools, ten) { stools = it }
            Text("一般状况 / 活动受限"); IbdOptions(wellBeing, ten) { wellBeing = it }
        }
        SectionCard("体格和生长") {
            Text("体重变化"); IbdOptions(weight, ten) { weight = it }
            Text("身高 / 生长"); IbdOptions(height, ten) { height = it }
            Text("腹部查体"); IbdOptions(abdomen, ten) { abdomen = it }
            Text("肛周疾病"); IbdOptions(perianal, ten) { perianal = it }
            Text("肠外表现"); IbdOptions(extra, ten) { extra = it }
        }
        SectionCard("实验室") {
            Text("血细胞比容 HCT"); IbdOptions(hct?.let { if (it == 2.5) 2 else it.toInt() }, listOf(IbdOption(0, "0 分"), IbdOption(2, "2.5 分"), IbdOption(5, "5 分"))) { hct = if (it == 2) 2.5 else it.toDouble() }
            Text("ESR"); IbdOptions(esr?.let { if (it == 2.5) 2 else it.toInt() }, listOf(IbdOption(0, "0 分"), IbdOption(2, "2.5 分"), IbdOption(5, "5 分"))) { esr = if (it == 2) 2.5 else it.toDouble() }
            Text("白蛋白"); IbdOptions(albumin?.toInt(), ten) { albumin = it.toDouble() }
        }
        ResetButton { pain = null; stools = null; wellBeing = null; weight = null; height = null; abdomen = null; perianal = null; extra = null; hct = null; esr = null; albumin = null }
        result?.let { ResultCard("PCDAI 总分", "%.1f 分".format(it.score), listOf(it.interpretation, "常用研究阈值：<10 缓解；≥30 常提示中重度活动。具体版本和是否排除身高项需在研究方案中确认。")) }
    }
}

@Composable
fun NancyScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var grade by rememberSaveable { mutableStateOf<Int?>(null) }
    val result = grade?.let { classifyNancy(it) }
    IbdCalculatorPage("Nancy", "Nancy Histological Index · UC 组织学", onBack, onReference,
        "Nancy Index 是 UC 组织学分级，采用层级判读：慢性炎症、急性炎症和溃疡共同决定 0–4 级。") {
        SectionCard("病理分级") {
            IbdOptions(grade, listOf(
                IbdOption(0, "0 级：无明显组织学疾病"), IbdOption(1, "1 级：慢性炎症，无急性炎症"),
                IbdOption(2, "2 级：轻度急性炎症"), IbdOption(3, "3 级：中重度急性炎症"), IbdOption(4, "4 级：溃疡")
            )) { grade = it }
        }
        ResetButton { grade = null }
        result?.let { ResultCard("Nancy Index", it, listOf("常用组织学缓解定义为 0；组织学评分不能脱离取材部位和完整病理描述解释。")) }
    }
}

@Composable
fun RhiScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var chronic by rememberSaveable { mutableStateOf<Int?>(null) }
    var lamina by rememberSaveable { mutableStateOf<Int?>(null) }
    var epithelial by rememberSaveable { mutableStateOf<Int?>(null) }
    var erosion by rememberSaveable { mutableStateOf<Int?>(null) }
    val result = if (chronic != null && lamina != null && epithelial != null && erosion != null) calculateRhi(RhiInput(chronic!!, lamina!!, epithelial!!, erosion!!)) else null
    val options = (0..3).map { IbdOption(it, "$it 分") }
    IbdCalculatorPage("RHI", "Robarts Histopathology Index · UC 组织学", onBack, onReference,
        "RHI 由 4 个病理维度按 1、2、3、5 加权，总分 0–33。请依据病理医师对各维度的原始判读填写。") {
        SectionCard("病理维度") {
            Text("固有层慢性炎症（权重 1）"); IbdOptions(chronic, options) { chronic = it }
            Text("固有层中性粒细胞（权重 2）"); IbdOptions(lamina, options) { lamina = it }
            Text("上皮中性粒细胞（权重 3）"); IbdOptions(epithelial, options) { epithelial = it }
            Text("糜烂或溃疡（权重 5）"); IbdOptions(erosion, options) { erosion = it }
        }
        ResetButton { chronic = null; lamina = null; epithelial = null; erosion = null }
        result?.let { ResultCard("RHI 总分", "$it / 33 分", listOf("计算：慢性炎症×1 + 固有层中性粒细胞×2 + 上皮中性粒细胞×3 + 糜烂/溃疡×5。", "常用研究中 RHI ≤3 常作组织学缓解参考，但终点定义需核对研究方案。")) }
    }
}

@Composable
fun GeboesScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var selected by rememberSaveable { mutableStateOf<String?>(null) }
    val item = geboesSubgrades.firstOrNull { it.code == selected }
    IbdCalculatorPage("Geboes", "Geboes Histopathology Score · UC 组织学", onBack, onReference,
        "Geboes 是层级分级，不是把 0–5 直接相加。以下保留原始 0.0–5.4 亚级；最终级别应由病理医师依据整张切片判读。") {
        SectionCard("选择最高/最严重的适用亚级") {
            geboesSubgrades.forEach { option -> ChoiceButton(selected == option.code, option.code, option.label) { selected = option.code } }
        }
        ResetButton { selected = null }
        item?.let { ResultCard("Geboes 结果", it.code, listOf(it.label, geboesInterpretation(it.code).orEmpty(), "原始量表的层级结构不能用单一线性分数替代。")) }
    }
}

@Composable
fun MontrealCrohnScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var age by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf<String?>(null) }
    var upper by rememberSaveable { mutableStateOf(false) }
    var behaviour by rememberSaveable { mutableStateOf<String?>(null) }
    var perianal by rememberSaveable { mutableStateOf(false) }
    val result = if (age.toIntOrNull() != null && location != null && behaviour != null) classifyMontrealCrohn(MontrealCrohnInput(age.toInt(), location!!, upper, behaviour!!, perianal)) else null
    IbdCalculatorPage("Montreal CD", "Montreal Classification · Crohn 病表型分类", onBack, onReference,
        "这是疾病表型分类，不是疾病活动度评分。A/L/B 分别表示诊断年龄、病变部位和行为；L4 与 p 是修饰符。") {
        SectionCard("Age at diagnosis") { NumericField("诊断年龄", age, { age = it }, "岁", integerOnly = true, placeholder = "≤16=A1，17–40=A2，>40=A3") }
        SectionCard("Location") { IbdOptions(location?.removePrefix("L" )?.toIntOrNull(), listOf(IbdOption(1, "L1：回肠"), IbdOption(2, "L2：结肠"), IbdOption(3, "L3：回结肠"), IbdOption(4, "L4：上消化道"))) { location = "L$it" }; ChoiceButton(upper, "加 L4 修饰符", "合并上消化道病变") { upper = !upper } }
        SectionCard("Behaviour") { IbdOptions(behaviour?.removePrefix("B")?.take(1)?.toIntOrNull(), listOf(IbdOption(1, "B1：非狭窄、非穿透"), IbdOption(2, "B2：狭窄"), IbdOption(3, "B3：穿透"))) { behaviour = "B$it" }; ChoiceButton(perianal, "加 p 修饰符", "合并肛周病变") { perianal = !perianal } }
        ResetButton { age = ""; location = null; upper = false; behaviour = null; perianal = false }
        result?.let { ResultCard("Montreal CD", it.code, listOf("A1：≤16 岁；A2：17–40 岁；A3：>40 岁。", "${it.explanation} 该分类用于描述表型，不能作为活动度或治疗反应评分。")) }
    }
}

@Composable
fun MontrealUcScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var extent by rememberSaveable { mutableStateOf<String?>(null) }
    val result = extent?.let { classifyMontrealUc(it) }
    IbdCalculatorPage("Montreal UC", "Montreal Classification · UC extent", onBack, onReference,
        "Montreal UC 主要分类病变范围；本页不把 S0–S3 严重度修饰符与 E 范围混为同一个数值评分。") {
        SectionCard("Extent") { IbdOptions(extent?.removePrefix("E")?.toIntOrNull(), listOf(IbdOption(1, "E1：直肠炎"), IbdOption(2, "E2：左侧结肠炎"), IbdOption(3, "E3：广泛性结肠炎 / 全结肠炎"))) { extent = "E$it" } }
        ResetButton { extent = null }
        result?.let { ResultCard("Montreal UC", it, listOf("范围可随疾病演变而变化；请以完整内镜报告记录。")) }
    }
}

@Composable
private fun IbdCalculatorPage(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
    onReference: () -> Unit,
    notice: String,
    content: @Composable () -> Unit
) {
    CalculatorPage(title, subtitle, onBack, onReference, scopeNotice = notice, content = content)
}

@Composable
private fun ResetButton(onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) { Text("重置") }
}
