package com.android.medicalcalculator

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import kotlin.math.sqrt

@Composable
fun BmiScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var height by rememberSaveable { mutableStateOf("") }
    var weight by rememberSaveable { mutableStateOf("") }
    var result by remember { mutableStateOf<Pair<Double, String>?>(null) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    fun clearResult() {
        result = null
        error = null
    }

    CalculatorPage(
        title = "成人 BMI 计算器",
        subtitle = "根据身高和体重计算身体质量指数",
        onBack = onBack,
        onReference = onReference,
        scopeNotice = "适用于 18 岁及以上成人，不用于儿童生长评价。"
    ) {
        SectionCard {
            NumericField("身高", height, {
                height = it
                clearResult()
            }, "cm", placeholder = "请输入身高")
            Spacer(Modifier.height(10.dp))
            NumericField("体重", weight, {
                weight = it
                clearResult()
            }, "kg", placeholder = "请输入体重")
            ErrorText(error)
            Button(onClick = {
                val h = height.toDoubleOrNull()
                val w = weight.toDoubleOrNull()
                error = when {
                    h == null || w == null -> "请输入有效的身高和体重"
                    h < 80 || h > 250 -> "请检查身高：身高应输入 cm，本计算器接受 80～250 cm。"
                    w < 20 || w > 400 -> "请检查体重：体重应输入 kg，本计算器接受 20～400 kg。"
                    else -> null
                }
                if (error == null && h != null && w != null) {
                    val bmi = w / ((h / 100.0) * (h / 100.0))
                    val category = when {
                        bmi < 18.5 -> "体重过低"
                        bmi < 24.0 -> "体重正常"
                        bmi < 28.0 -> "超重"
                        else -> "肥胖"
                    }
                    result = bmi to category
                }
            }) { Text("计算 BMI") }
            Spacer(Modifier.height(6.dp))
            OutlinedButton(onClick = {
                height = ""
                weight = ""
                clearResult()
            }) { Text("清空") }
        }

        result?.let { (bmi, category) ->
            ResultCard(
                "BMI 计算结果",
                "%.1f kg/m²".format(bmi),
                listOf(
                    "分类：$category",
                    "结果仅用于体重状态评估，具体健康状况需结合临床情况综合判断。"
                )
            )
        }
    }
}

data class Fib4Result(val value: Double, val riskText: String, val note: String)

@Composable
fun Fib4Screen(onBack: () -> Unit, onReference: () -> Unit) {
    var age by rememberSaveable { mutableStateOf("") }
    var ast by rememberSaveable { mutableStateOf("") }
    var alt by rememberSaveable { mutableStateOf("") }
    var platelets by rememberSaveable { mutableStateOf("") }
    var result by remember { mutableStateOf<Fib4Result?>(null) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    fun clearResult() {
        result = null
        error = null
    }

    CalculatorPage(
        title = "FIB-4 指数",
        subtitle = "肝纤维化无创评估工具",
        onBack = onBack,
        onReference = onReference,
        scopeNotice = "以下风险解释采用 MASLD 成人常用风险分层路径。"
    ) {
        SectionCard {
            NumericField("年龄", age, { age = it; clearResult() }, "岁", placeholder = "请输入年龄")
            Spacer(Modifier.height(8.dp))
            NumericField("AST", ast, { ast = it; clearResult() }, "U/L", placeholder = "请输入 AST")
            Spacer(Modifier.height(8.dp))
            NumericField("ALT", alt, { alt = it; clearResult() }, "U/L", placeholder = "请输入 ALT")
            Spacer(Modifier.height(8.dp))
            NumericField("血小板计数", platelets, { platelets = it; clearResult() }, "10^9/L", placeholder = "请输入血小板计数")
            ErrorText(error)
            Button(onClick = {
                val a = age.toDoubleOrNull()
                val asv = ast.toDoubleOrNull()
                val alv = alt.toDoubleOrNull()
                val p = platelets.toDoubleOrNull()
                error = when {
                    a == null || asv == null || alv == null || p == null -> "请完整填写所有数据"
                    a < 18 || a > 120 -> "请检查年龄：请输入合理的成人年龄。"
                    asv <= 0 || alv <= 0 || p <= 0 -> "请检查检验结果：AST、ALT 和血小板计数必须大于 0。"
                    else -> null
                }
                if (error == null && a != null && asv != null && alv != null && p != null) {
                    val fib4 = (a * asv) / (p * sqrt(alv))
                    val pair = when {
                        a < 35 -> "该年龄段不宜单独使用 FIB-4 分层" to
                            "FIB-4 在 35 岁以下成人中的准确性较低。存在代谢危险因素或临床怀疑时，建议结合 VCTE、ELF 等其他无创评估。"
                        a < 65 && fib4 < 1.3 -> "进展期肝纤维化风险较低" to
                            "在 MASLD 风险分层中，通常可继续随访，并结合患者整体临床情况决定复查时间。"
                        a < 65 && fib4 < 2.67 -> "结果处于不确定区间" to
                            "建议进一步采用 VCTE、ELF 等二线无创肝纤维化评估。"
                        a < 65 -> "进展期肝纤维化可能性较高" to
                            "建议进一步进行无创评估并结合肝病专科临床判断。"
                        fib4 < 2.0 -> "进展期肝纤维化风险较低" to
                            "65 岁及以上人群采用年龄调整后的低风险界值。仍需结合整体临床情况判断。"
                        fib4 < 2.67 -> "结果处于不确定区间" to
                            "建议进一步采用 VCTE、ELF 等二线无创肝纤维化评估。"
                        else -> "进展期肝纤维化可能性较高" to
                            "建议进一步进行无创评估并结合肝病专科临床判断。"
                    }
                    result = Fib4Result(fib4, pair.first, pair.second)
                }
            }) { Text("计算 FIB-4") }
            Spacer(Modifier.height(6.dp))
            OutlinedButton(onClick = {
                age = ""
                ast = ""
                alt = ""
                platelets = ""
                clearResult()
            }) { Text("清空") }
        }

        result?.let {
            ResultCard(
                "FIB-4 计算结果",
                "%.2f".format(it.value),
                listOf(it.riskText, it.note)
            )
        }
    }
}

data class ChildResult(
    val bilirubinScore: Int,
    val albuminScore: Int,
    val inrScore: Int,
    val total: Int,
    val childClass: String
)

@Composable
fun ChildPughScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var bilirubin by rememberSaveable { mutableStateOf("") }
    var albumin by rememberSaveable { mutableStateOf("") }
    var inr by rememberSaveable { mutableStateOf("") }
    var ascitesScore by rememberSaveable { mutableStateOf<Int?>(null) }
    var encephalopathyScore by rememberSaveable { mutableStateOf<Int?>(null) }
    var result by remember { mutableStateOf<ChildResult?>(null) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    fun clearResult() {
        result = null
        error = null
    }

    CalculatorPage(
        title = "Child-Pugh 评分",
        subtitle = "肝硬化肝功能储备评估工具",
        onBack = onBack,
        onReference = onReference
    ) {
        SectionCard {
            NumericField("总胆红素", bilirubin, { bilirubin = it; clearResult() }, "μmol/L", placeholder = "请输入总胆红素")
            Spacer(Modifier.height(8.dp))
            NumericField("白蛋白", albumin, { albumin = it; clearResult() }, "g/L", placeholder = "请输入白蛋白")
            Spacer(Modifier.height(8.dp))
            NumericField("INR", inr, { inr = it; clearResult() }, placeholder = "请输入 INR")
        }

        SectionCard("腹水") {
            listOf(
                Triple(1, "无腹水", "1 分"),
                Triple(2, "轻度 / 利尿剂可控制", "2 分"),
                Triple(3, "中度以上 / 利尿剂治疗后仍明显", "3 分")
            ).forEach { (score, label, desc) ->
                ChoiceButton(ascitesScore == score, label, desc) {
                    ascitesScore = score
                    clearResult()
                }
            }
        }

        SectionCard("肝性脑病") {
            listOf(
                Triple(1, "无肝性脑病", "1 分"),
                Triple(2, "1-2 级", "2 分"),
                Triple(3, "3-4 级", "3 分")
            ).forEach { (score, label, desc) ->
                ChoiceButton(encephalopathyScore == score, label, desc) {
                    encephalopathyScore = score
                    clearResult()
                }
            }
        }

        ErrorText(error)
        Button(onClick = {
            val b = bilirubin.toDoubleOrNull()
            val a = albumin.toDoubleOrNull()
            val i = inr.toDoubleOrNull()
            error = when {
                b == null || a == null || i == null -> "请完整填写检验结果"
                b <= 0 || a <= 0 || i <= 0 -> "检验结果必须大于 0"
                ascitesScore == null || encephalopathyScore == null -> "请选择腹水和肝性脑病"
                else -> null
            }
            if (error == null && b != null && a != null && i != null) {
                val bs = if (b < 34) 1 else if (b <= 51) 2 else 3
                val als = if (a > 35) 1 else if (a >= 28) 2 else 3
                val iscore = if (i < 1.7) 1 else if (i <= 2.3) 2 else 3
                val total = bs + als + iscore + ascitesScore!! + encephalopathyScore!!
                val cls = if (total <= 6) "Child-Pugh A" else if (total <= 9) "Child-Pugh B" else "Child-Pugh C"
                result = ChildResult(bs, als, iscore, total, cls)
            }
        }) { Text("计算 Child-Pugh") }
        Spacer(Modifier.height(6.dp))
        OutlinedButton(onClick = {
            bilirubin = ""
            albumin = ""
            inr = ""
            ascitesScore = null
            encephalopathyScore = null
            clearResult()
        }) { Text("清空") }

        result?.let {
            ResultCard(
                "Child-Pugh 总分",
                "${it.total} 分",
                listOf(
                    it.childClass,
                    "总胆红素：${it.bilirubinScore} 分",
                    "白蛋白：${it.albuminScore} 分",
                    "INR：${it.inrScore} 分",
                    "腹水：${ascitesScore} 分",
                    "肝性脑病：${encephalopathyScore} 分"
                )
            )
        }
    }
}
