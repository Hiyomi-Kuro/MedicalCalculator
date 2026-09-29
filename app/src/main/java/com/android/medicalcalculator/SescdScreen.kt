package com.android.medicalcalculator

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class SescdSegment(
    val id: String,
    val name: String,
    val ulcerSize: Int? = null,
    val ulceratedSurface: Int? = null,
    val affectedSurface: Int? = null,
    val stenosis: Int? = null,
    val unavailable: Boolean = false
) {
    fun subtotal(): Int? =
        if (ulcerSize != null && ulceratedSurface != null && affectedSurface != null && stenosis != null) {
            ulcerSize + ulceratedSurface + affectedSurface + stenosis
        } else null
}

private fun defaultSescdSegments() = listOf(
    SescdSegment("ileum", "回肠"),
    SescdSegment("rightColon", "右半结肠"),
    SescdSegment("transverseColon", "横结肠"),
    SescdSegment("leftColon", "左半结肠"),
    SescdSegment("rectum", "直肠")
)

private data class ScoreOption(val score: Int, val label: String, val description: String)

private val ulcerSizeOptions = listOf(
    ScoreOption(0, "无溃疡", "0 分"),
    ScoreOption(1, "阿弗他溃疡", "0.1～0.5 cm  1 分"),
    ScoreOption(2, "较大溃疡", "0.5～2.0 cm  2 分"),
    ScoreOption(3, "巨大溃疡", "＞2.0 cm  3 分")
)
private val ulceratedSurfaceOptions = listOf(
    ScoreOption(0, "无", "0%  0 分"),
    ScoreOption(1, "＜10%", "1 分"),
    ScoreOption(2, "10%～30%", "2 分"),
    ScoreOption(3, "＞30%", "3 分")
)
private val affectedSurfaceOptions = listOf(
    ScoreOption(0, "未受累", "0 分"),
    ScoreOption(1, "＜50%", "1 分"),
    ScoreOption(2, "50%～75%", "2 分"),
    ScoreOption(3, "＞75%", "3 分")
)
private val stenosisOptions = listOf(
    ScoreOption(0, "无狭窄", "0 分"),
    ScoreOption(1, "单发狭窄，内镜可通过", "1 分"),
    ScoreOption(2, "多发狭窄，内镜可通过", "2 分"),
    ScoreOption(3, "狭窄，内镜不能通过", "3 分")
)

@Composable
private fun SescdOptionGroup(
    title: String,
    selected: Int?,
    options: List<ScoreOption>,
    onSelect: (Int) -> Unit
) {
    Text(title)
    options.forEach { option ->
        ChoiceButton(selected == option.score, option.label, option.description) {
            onSelect(option.score)
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
fun SescdScreen(onBack: () -> Unit, onReference: () -> Unit) {
    var segments by remember { mutableStateOf(defaultSescdSegments()) }

    fun updateSimple(index: Int, field: String, score: Int) {
        segments = segments.mapIndexed { i, segment ->
            if (i != index) segment
            else when (field) {
                "ulcerSize" -> segment.copy(ulcerSize = score)
                "ulceratedSurface" -> segment.copy(ulceratedSurface = score)
                "affectedSurface" -> segment.copy(affectedSurface = score)
                else -> segment
            }
        }
    }

    fun updateStenosis(index: Int, score: Int) {
        var updated = segments.mapIndexed { i, segment ->
            if (i == index) segment.copy(stenosis = score) else segment
        }

        var blockingIndex = -1
        for (i in updated.indices.reversed()) {
            if (!updated[i].unavailable && updated[i].stenosis == 3) {
                blockingIndex = i
                break
            }
        }

        updated = updated.mapIndexed { i, segment ->
            val unavailable = blockingIndex >= 0 && i < blockingIndex
            if (unavailable) {
                segment.copy(
                    ulcerSize = null,
                    ulceratedSurface = null,
                    affectedSurface = null,
                    stenosis = null,
                    unavailable = true
                )
            } else {
                segment.copy(unavailable = false)
            }
        }
        segments = updated
    }

    val available = segments.filter { !it.unavailable }
    val totalScore = if (available.all { it.subtotal() != null }) {
        available.sumOf { it.subtotal() ?: 0 }
    } else null
    val hasUnavailable = segments.any { it.unavailable }

    CalculatorPage(
        title = "Crohn 病简化内镜评分（SES-CD）",
        subtitle = "Simple Endoscopic Score for Crohn's Disease",
        onBack = onBack,
        onReference = onReference,
        scopeNotice = "分别评估 5 个肠段的溃疡大小、溃疡表面、受累表面和狭窄情况。"
    ) {
        segments.forEachIndexed { index, segment ->
            SectionCard("${index + 1}. ${segment.name}") {
                if (segment.unavailable) {
                    NoticeBox("因远端存在内镜不能通过的狭窄，本肠段未评估")
                } else {
                    SescdOptionGroup("1. 溃疡大小", segment.ulcerSize, ulcerSizeOptions) {
                        updateSimple(index, "ulcerSize", it)
                    }
                    SescdOptionGroup("2. 溃疡表面比例", segment.ulceratedSurface, ulceratedSurfaceOptions) {
                        updateSimple(index, "ulceratedSurface", it)
                    }
                    SescdOptionGroup("3. 受累表面比例", segment.affectedSurface, affectedSurfaceOptions) {
                        updateSimple(index, "affectedSurface", it)
                    }
                    SescdOptionGroup("4. 狭窄", segment.stenosis, stenosisOptions) {
                        updateStenosis(index, it)
                    }
                    segment.subtotal()?.let { Text("本肠段小计：$it 分") }
                }
            }
        }

        OutlinedButton(onClick = { segments = defaultSescdSegments() }) {
            Text("清空")
        }

        totalScore?.let {
            val lines = if (hasUnavailable) {
                listOf("存在因内镜不能通过狭窄而未评估的近端肠段，当前总分可能低估整体内镜病变负荷。")
            } else emptyList()
            ResultCard("SES-CD 总分", it.toString(), lines)
        }
    }
}
