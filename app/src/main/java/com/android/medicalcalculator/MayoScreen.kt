package com.android.medicalcalculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private data class MayoOption(val score: Int, val label: String, val description: String)

private val stoolFrequencyOptions = listOf(
    MayoOption(0, "正常排便次数", "0 分"),
    MayoOption(1, "比正常增加 1～2 次/日", "1 分"),
    MayoOption(2, "比正常增加 3～4 次/日", "2 分"),
    MayoOption(3, "比正常增加 5 次/日", "3 分")
)

private val classicBleedingOptions = listOf(
    MayoOption(0, "无肉眼血", "0 分"),
    MayoOption(1, "不到一半排便可见血丝", "1 分"),
    MayoOption(2, "大多数排便可见明显血液", "2 分"),
    MayoOption(3, "排出纯血，无粪便", "3 分")
)

private val modifiedBleedingOptions = listOf(
    MayoOption(0, "未见血", "0 分"),
    MayoOption(1, "粪便伴血丝", "1 分"),
    MayoOption(2, "粪便中血液超过血丝程度", "2 分"),
    MayoOption(3, "单独排血", "3 分")
)

private val pgaOptions = listOf(
    MayoOption(0, "正常 / 无活动", "0 分"),
    MayoOption(1, "轻度活动", "1 分"),
    MayoOption(2, "中度活动", "2 分"),
    MayoOption(3, "重度活动", "3 分")
)

private val classicEndoscopyOptions = listOf(
    MayoOption(0, "正常或非活动期", "0 分"),
    MayoOption(1, "轻度", "红斑、血管纹理减少、轻度脆性  1 分"),
    MayoOption(2, "中度", "明显红斑、血管纹理消失、脆性、糜烂  2 分"),
    MayoOption(3, "重度", "自发性出血、溃疡  3 分")
)

private val modifiedEndoscopyOptions = listOf(
    MayoOption(0, "黏膜正常", "0 分"),
    MayoOption(1, "轻度", "红斑、血管纹理减少，无脆性  1 分"),
    MayoOption(2, "中度", "明显红斑、血管纹理消失、脆性、糜烂  2 分"),
    MayoOption(3, "重度", "自发性出血、溃疡  3 分")
)

@Composable
private fun MayoOptionGroup(selected: Int?, options: List<MayoOption>, onSelect: (Int) -> Unit) {
    options.forEach { option ->
        ChoiceButton(selected == option.score, option.label, option.description) {
            onSelect(option.score)
        }
    }
}

@Composable
fun MayoScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var mode by rememberSaveable { mutableStateOf("full") }
    var stool by rememberSaveable { mutableStateOf<Int?>(null) }
    var classicBleeding by rememberSaveable { mutableStateOf<Int?>(null) }
    var modifiedBleeding by rememberSaveable { mutableStateOf<Int?>(null) }
    var pga by rememberSaveable { mutableStateOf<Int?>(null) }
    var classicEndoscopy by rememberSaveable { mutableStateOf<Int?>(null) }
    var modifiedEndoscopy by rememberSaveable { mutableStateOf<Int?>(null) }

    val required = when (mode) {
        "modified" -> listOf(stool, modifiedBleeding, modifiedEndoscopy)
        "partial" -> listOf(stool, classicBleeding, pga)
        else -> listOf(stool, classicBleeding, pga, classicEndoscopy)
    }
    val currentScore = if (required.all { it != null }) required.sumOf { it ?: 0 } else null
    val scoreName = when (mode) {
        "modified" -> "改良 Mayo Score"
        "partial" -> "部分 Mayo Score"
        else -> "完整 Mayo Score"
    }
    val scoreMax = if (mode == "full") 12 else 9

    CalculatorPage(
        title = "溃疡性结肠炎 Mayo 评分",
        subtitle = "Mayo Score for Ulcerative Colitis",
        onBack = onBack,
        onReference = onReference
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (mode == "full") Button(onClick = { mode = "full" }) { Text("完整 Mayo") }
            else OutlinedButton(onClick = { mode = "full" }) { Text("完整 Mayo") }

            if (mode == "modified") Button(onClick = { mode = "modified" }) { Text("改良 Mayo") }
            else OutlinedButton(onClick = { mode = "modified" }) { Text("改良 Mayo") }

            if (mode == "partial") Button(onClick = { mode = "partial" }) { Text("部分 Mayo") }
            else OutlinedButton(onClick = { mode = "partial" }) { Text("部分 Mayo") }
        }

        NoticeBox(
            when (mode) {
                "modified" -> "改良 Mayo Score：排便频率、直肠出血和内镜表现（MES），不包含 PGA，共 3 项，总分 0～9 分。"
                "partial" -> "部分 Mayo Score：排便频率、直肠出血和医师总体评价（PGA），不包含内镜项目，共 3 项，总分 0～9 分。"
                else -> "完整 Mayo Score：排便频率、直肠出血、医师总体评价（PGA）和内镜表现（MES），共 4 项，总分 0～12 分。"
            }
        )

        SectionCard("1. 排便频率") {
            Text("与患者平时正常排便次数相比")
            MayoOptionGroup(stool, stoolFrequencyOptions) { stool = it }
        }

        SectionCard("2. 直肠出血") {
            if (mode == "modified") {
                Text("改良 Mayo（mMS）定义")
                MayoOptionGroup(modifiedBleeding, modifiedBleedingOptions) { modifiedBleeding = it }
            } else {
                Text("经典 Mayo 定义")
                MayoOptionGroup(classicBleeding, classicBleedingOptions) { classicBleeding = it }
            }
        }

        if (mode != "modified") {
            SectionCard("3. 医师总体评价（PGA）") {
                Text("综合腹部不适、总体健康感、活动状态及体格检查等进行评价")
                MayoOptionGroup(pga, pgaOptions) { pga = it }
            }
        }

        if (mode != "partial") {
            SectionCard(if (mode == "modified") "3. 内镜表现（MES）" else "4. 内镜表现（MES）") {
                if (mode == "modified") {
                    Text("改良 Mayo（mMS）内镜定义")
                    MayoOptionGroup(modifiedEndoscopy, modifiedEndoscopyOptions) { modifiedEndoscopy = it }
                } else {
                    Text("经典 Mayo Endoscopic Subscore")
                    MayoOptionGroup(classicEndoscopy, classicEndoscopyOptions) { classicEndoscopy = it }
                }
            }
        }

        OutlinedButton(onClick = {
            stool = null
            classicBleeding = null
            modifiedBleeding = null
            pga = null
            classicEndoscopy = null
            modifiedEndoscopy = null
        }) { Text("清空") }

        currentScore?.let {
            ResultCard(scoreName, it.toString(), listOf("总分范围：0～$scoreMax 分"))
        }
        Spacer(Modifier.height(12.dp))
    }
}
