package com.android.medicalcalculator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

data class CalculatorInfo(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val keywords: List<String>,
    val route: String
)

val calculators = listOf(
    CalculatorInfo("bmi", "成人 BMI 计算器", "根据身高和体重计算身体质量指数", "基础评估", listOf("BMI", "体重", "身高", "肥胖"), Routes.BMI),
    CalculatorInfo("fib4", "FIB-4 指数", "肝纤维化无创评估工具", "肝病", listOf("FIB-4", "纤维化", "AST", "ALT", "血小板"), Routes.FIB4),
    CalculatorInfo("childpugh", "Child-Pugh 评分", "肝硬化肝功能储备评估工具", "肝病", listOf("Child-Pugh", "Child Pugh", "肝硬化", "胆红素", "白蛋白", "INR", "腹水", "肝性脑病"), Routes.CHILD),
    CalculatorInfo("cdai", "Crohn 病活动指数（CDAI）", "经典 8 项加权评分", "IBD", listOf("CDAI", "Crohn", "克罗恩病", "克罗恩", "IBD", "炎症性肠病", "疾病活动度"), Routes.CDAI),
    CalculatorInfo("sescd", "Crohn 病简化内镜评分（SES-CD）", "5 个肠段 4 项内镜评分", "IBD", listOf("SES-CD", "SESCD", "Crohn", "克罗恩病", "克罗恩", "IBD", "内镜", "内镜评分", "溃疡", "狭窄"), Routes.SESCD),
    CalculatorInfo("mayoscore", "溃疡性结肠炎 Mayo 评分", "完整、改良及部分 Mayo 评分", "IBD", listOf("Mayo", "Mayo Score", "溃疡性结肠炎", "溃结", "UC", "IBD", "直肠出血", "内镜评分"), Routes.MAYO)
)

@Composable
fun HomeScreen(onOpen: (String) -> Unit) {
    var search by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("全部") }
    val categories = listOf("全部") + calculators.map { it.category }.distinct()
    val keyword = search.trim().lowercase()
    val filtered = calculators.filter { item ->
        val matchesCategory = category == "全部" || item.category == category
        val searchable = (listOf(item.title, item.description, item.category) + item.keywords).joinToString(" ").lowercase()
        matchesCategory && (keyword.isBlank() || searchable.contains(keyword))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("医学计算器", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("常用临床医学评分与计算工具", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = search,
            onValueChange = {
                search = it
                if (it.trim().isNotEmpty()) category = "全部"
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("搜索计算器，如 BMI、FIB-4") },
            singleLine = true,
            trailingIcon = {
                if (search.isNotEmpty()) {
                    TextButton(onClick = {
                        search = ""
                        category = "全部"
                    }) { Text("清除") }
                }
            }
        )

        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { item ->
                if (category == item) {
                    Button(onClick = { category = item }) { Text(item) }
                } else {
                    OutlinedButton(onClick = { category = item }) { Text(item) }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text(if (category == "全部") "全部工具" else category, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        if (filtered.isEmpty()) {
            Text("未找到相关计算器", modifier = Modifier.padding(vertical = 24.dp))
        } else {
            filtered.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onOpen(item.route) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                            Spacer(Modifier.width(8.dp))
                            Surface(shape = RoundedCornerShape(12.dp), tonalElevation = 2.dp) {
                                Text(item.category, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(item.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun CalculatorPage(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
    onReference: (() -> Unit)? = null,
    scopeNotice: String? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(onClick = onBack) { Text("← 返回") }
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(subtitle, style = MaterialTheme.typography.bodyMedium)
        if (onReference != null) {
            TextButton(onClick = onReference) { Text("参考依据") }
        }
        if (!scopeNotice.isNullOrBlank()) {
            NoticeBox(scopeNotice)
        }
        Spacer(Modifier.height(8.dp))
        content()
        Spacer(Modifier.height(28.dp))
    }
}

@Composable
fun NoticeBox(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp
    ) {
        Text(text, modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun SectionCard(title: String? = null, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            if (!title.isNullOrBlank()) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(10.dp))
            }
            content()
        }
    }
}

@Composable
fun NumericField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    unit: String? = null,
    integerOnly: Boolean = false,
    placeholder: String = ""
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            label = { Text(label) },
            placeholder = if (placeholder.isNotBlank()) ({ Text(placeholder) }) else null,
            keyboardOptions = KeyboardOptions(keyboardType = if (integerOnly) KeyboardType.Number else KeyboardType.Decimal),
            singleLine = true
        )
        if (!unit.isNullOrBlank()) {
            Text(unit, modifier = Modifier.padding(top = 24.dp))
        }
    }
}

@Composable
fun ChoiceButton(
    selected: Boolean,
    title: String,
    description: String? = null,
    onClick: () -> Unit
) {
    val inner: @Composable () -> Unit = {
        Column(Modifier.fillMaxWidth()) {
            Text(title, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
            if (!description.isNullOrBlank()) {
                Text(description, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
    if (selected) {
        Button(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)) { inner() }
    } else {
        OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)) { inner() }
    }
}

@Composable
fun ResultCard(title: String, value: String, lines: List<String> = emptyList()) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            lines.forEach {
                Spacer(Modifier.height(4.dp))
                Text(it)
            }
        }
    }
}

@Composable
fun ErrorText(text: String?) {
    if (!text.isNullOrBlank()) {
        Text(text, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(vertical = 8.dp))
    }
}

data class RefRow(val left: String, val right: String)
data class RefSection(
    val title: String,
    val body: String? = null,
    val rows: List<RefRow> = emptyList(),
    val lines: List<String> = emptyList()
)
data class ReferencePage(val subtitle: String, val sections: List<RefSection>)

@Composable
fun ReferenceScreen(referencePage: ReferencePage, onBack: () -> Unit) {
    CalculatorPage(
        title = "参考依据",
        subtitle = referencePage.subtitle,
        onBack = onBack
    ) {
        referencePage.sections.forEach { section ->
            SectionCard(section.title) {
                section.body?.let { Text(it) }
                section.rows.forEach { row ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(row.left, modifier = Modifier.weight(1f))
                        Spacer(Modifier.width(12.dp))
                        Text(row.right, fontWeight = FontWeight.Medium)
                    }
                }
                section.lines.forEach {
                    Text(it, modifier = Modifier.padding(vertical = 2.dp))
                }
            }
        }
    }
}
