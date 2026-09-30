package com.android.medicalcalculator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

data class CalculatorInfo(
    val id: String,
    val title: String,
    val englishName: String,
    val category: String,
    val keywords: List<String>,
    val route: String,
    val description: String = ""
)

val calculators = listOf(
    CalculatorInfo("cdai", "Crohn 病活动指数（CDAI）", "Crohn's Disease Activity Index · 经典 8 项加权评分", "IBD", listOf("CDAI", "Crohn", "克罗恩病", "克罗恩", "IBD", "炎症性肠病", "疾病活动度"), Routes.CDAI),
    CalculatorInfo("hbi", "Harvey-Bradshaw Index（HBI）", "Crohn 病简化临床活动度指数", "IBD", listOf("HBI", "Harvey Bradshaw", "Crohn", "克罗恩", "活动度", "clinical"), Routes.HBI),
    CalculatorInfo("sescd", "Crohn 病简化内镜评分（SES-CD）", "Simple Endoscopic Score for Crohn's Disease", "IBD", listOf("SES-CD", "SESCD", "Crohn", "克罗恩病", "内镜", "ulcer", "stenosis"), Routes.SESCD),
    CalculatorInfo("cdeis", "Crohn 病内镜严重度指数（CDEIS）", "Crohn's Disease Endoscopic Index of Severity", "IBD", listOf("CDEIS", "Crohn", "克罗恩", "内镜", "deep ulcer", "superficial ulcer", "stenosis"), Routes.CDEIS),
    CalculatorInfo("rutgeerts", "Rutgeerts 术后复发评分", "Postoperative endoscopic recurrence score", "IBD", listOf("Rutgeerts", "modified Rutgeerts", "i0", "i1", "i2a", "i2b", "postoperative", "术后", "复发"), Routes.RUTGEERTS),
    CalculatorInfo("pdai", "肛周疾病活动指数（PDAI）", "Perianal Disease Activity Index", "IBD", listOf("PDAI", "perianal", "肛周", "瘘", "Crohn"), Routes.PDAI),
    CalculatorInfo("mayo", "溃疡性结肠炎 Mayo 评分", "Mayo Score · full, modified and partial", "IBD", listOf("Mayo", "Mayo Score", "UC", "溃疡性结肠炎", "溃结", "内镜", "rectal bleeding"), Routes.MAYO),
    CalculatorInfo("uceis", "UC 内镜严重度指数（UCEIS）", "Ulcerative Colitis Endoscopic Index of Severity", "IBD", listOf("UCEIS", "UC", "ulcerative colitis", "内镜", "vascular pattern", "bleeding", "erosion"), Routes.UCEIS),
    CalculatorInfo("sccai", "简单临床结肠炎活动指数（SCCAI）", "Simple Clinical Colitis Activity Index", "IBD", listOf("SCCAI", "UC", "clinical", "urgency", "blood", "夜间排便"), Routes.SCCAI),
    CalculatorInfo("tw", "Truelove–Witts 严重度指数", "Classic Truelove and Witts Severity Index", "IBD", listOf("Truelove", "Witts", "UC", "severe", "ASUC", "血便"), Routes.TRueloveWitts),
    CalculatorInfo("oxford", "Oxford / Travis Day-3 标准", "ASUC steroid failure risk prediction", "IBD", listOf("Oxford", "Travis", "ASUC", "Day 3", "CRP", "急性重度"), Routes.OXFORD),
    CalculatorInfo("pucai", "儿童 UC 活动指数（PUCAI）", "Pediatric Ulcerative Colitis Activity Index", "IBD", listOf("PUCAI", "pediatric", "儿科", "UC", "儿童结肠炎"), Routes.PUCAI),
    CalculatorInfo("pcdai", "儿童 Crohn 活动指数（PCDAI）", "Pediatric Crohn's Disease Activity Index", "IBD", listOf("PCDAI", "weighted PCDAI", "pediatric", "儿科", "Crohn"), Routes.PCDAI),
    CalculatorInfo("nancy", "Nancy 组织学指数", "Nancy Histological Index", "IBD > 组织学", listOf("Nancy", "histology", "组织学", "UC", "biopsy"), Routes.NANCY),
    CalculatorInfo("rhi", "Robarts 组织病理学指数（RHI）", "Robarts Histopathology Index", "IBD > 组织学", listOf("RHI", "Robarts", "histology", "组织学", "UC"), Routes.RHI),
    CalculatorInfo("geboes", "Geboes 组织学评分", "Geboes Histopathology Score", "IBD > 组织学", listOf("Geboes", "histology", "组织学", "UC", "5.4"), Routes.GEBOES),
    CalculatorInfo("montreal-cd", "Montreal 分类 · Crohn 病", "Montreal Classification for Crohn's disease", "IBD > 疾病分类", listOf("Montreal", "Crohn", "克罗恩", "classification", "A1", "L3", "B1p"), Routes.MONTREAL_CD),
    CalculatorInfo("montreal-uc", "Montreal 分类 · UC", "Montreal Classification for ulcerative colitis", "IBD > 疾病分类", listOf("Montreal", "UC", "E1", "E2", "E3", "classification"), Routes.MONTREAL_UC),
    CalculatorInfo("bmi", "成人 BMI 计算器", "Body Mass Index", "基础评估", listOf("BMI", "体重", "身高", "肥胖", "基础"), Routes.BMI),
    CalculatorInfo("fib4", "FIB-4 指数", "FIB-4 liver fibrosis index", "肝病", listOf("FIB-4", "纤维化", "AST", "ALT", "血小板", "liver"), Routes.FIB4),
    CalculatorInfo("childpugh", "Child-Pugh 评分", "Child-Pugh liver function score", "肝病", listOf("Child-Pugh", "Child Pugh", "肝硬化", "胆红素", "白蛋白", "INR", "腹水", "肝性脑病"), Routes.CHILD)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onOpen: (String) -> Unit) {
    var search by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("全部") }
    var categoryMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val categories = listOf("全部", "IBD", "IBD > 组织学", "IBD > 疾病分类", "肝病", "基础评估")
    val keyword = search.trim().lowercase()
    val filtered = calculators.filter { item ->
        val categoryMatches = category == "全部" || item.category == category || (category == "IBD" && item.category.startsWith("IBD"))
        val searchable = (listOf(item.title, item.englishName, item.description, item.category) + item.keywords).joinToString(" ").lowercase()
        categoryMatches && (keyword.isBlank() || searchable.contains(keyword))
    }.sortedWith(compareBy<CalculatorInfo> { if (it.category.startsWith("IBD")) 0 else 1 }.thenBy { it.title })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Column { Text("医学计算器", fontWeight = FontWeight.SemiBold); Text("临床评分与辅助评估", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text("以 IBD 为重点的临床计算工具", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it; if (it.isNotBlank()) category = "全部" },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("搜索 Crohn、克罗恩、Mayo、histology…") },
                    trailingIcon = { if (search.isNotEmpty()) TextButton(onClick = { search = "" }) { Text("清除") } },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(Modifier.height(10.dp))
                Box {
                    FilterChip(selected = category != "全部", onClick = { categoryMenuExpanded = true }, label = { Text("分类：$category") })
                    DropdownMenu(expanded = categoryMenuExpanded, onDismissRequest = { categoryMenuExpanded = false }) {
                        categories.forEach { option -> DropdownMenuItem(text = { Text(option) }, onClick = { category = option; categoryMenuExpanded = false }) }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(if (keyword.isBlank()) "优先工具" else "搜索结果", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            }
            if (filtered.isEmpty()) {
                item { Text("未找到相关计算器", modifier = Modifier.padding(vertical = 32.dp), color = MaterialTheme.colorScheme.onSurfaceVariant) }
            } else {
                items(filtered, key = { it.id }) { item -> CalculatorCard(item, onOpen) }
            }
            item { Text("本工具仅用于临床评分及辅助评估，不能替代完整病史、体格检查、实验室检查、内镜、影像学及医生临床判断。", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 16.dp)) }
        }
    }
}

@Composable
private fun CalculatorCard(item: CalculatorInfo, onOpen: (String) -> Unit) {
    Card(Modifier.fillMaxWidth().clickable { onOpen(item.route) }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) {
                    Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(item.englishName, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.secondaryContainer) {
                    Text(item.category.removePrefix("IBD > "), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(item.description.ifBlank { "临床评分与辅助评估工具" }, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorPage(title: String, subtitle: String, onBack: () -> Unit, onReference: (() -> Unit)? = null, scopeNotice: String? = null, content: @Composable () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(title, maxLines = 2, overflow = TextOverflow.Clip) },
                navigationIcon = { TextButton(onClick = onBack) { Text("返回") } },
                actions = { if (onReference != null) TextButton(onClick = onReference) { Text("参考依据") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (!scopeNotice.isNullOrBlank()) { Spacer(Modifier.height(8.dp)); NoticeBox(scopeNotice) }
            Spacer(Modifier.height(8.dp)); content(); Spacer(Modifier.height(28.dp))
        }
    }
}

@Composable
fun NoticeBox(text: String) {
    Surface(Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surfaceContainerHigh) {
        Text(text, Modifier.padding(14.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun SectionCard(title: String? = null, content: @Composable () -> Unit) {
    Card(Modifier.fillMaxWidth().padding(vertical = 5.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)) {
        Column(Modifier.padding(16.dp)) {
            if (!title.isNullOrBlank()) { Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold); Spacer(Modifier.height(10.dp)) }
            content()
        }
    }
}

@Composable
fun NumericField(label: String, value: String, onValueChange: (String) -> Unit, unit: String? = null, integerOnly: Boolean = false, placeholder: String = "") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value, onValueChange, Modifier.weight(1f), label = { Text(label) }, placeholder = if (placeholder.isNotBlank()) ({ Text(placeholder) }) else null, keyboardOptions = KeyboardOptions(keyboardType = if (integerOnly) KeyboardType.Number else KeyboardType.Decimal), singleLine = true)
        if (!unit.isNullOrBlank()) Text(unit, Modifier.padding(top = 24.dp), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun ChoiceButton(selected: Boolean, title: String, description: String? = null, onClick: () -> Unit) {
    val content: @Composable () -> Unit = {
        Column(Modifier.fillMaxWidth()) {
            Text(title, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
            if (!description.isNullOrBlank()) Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
    if (selected) Button(onClick, Modifier.fillMaxWidth().padding(vertical = 3.dp)) { content() }
    else OutlinedButton(onClick, Modifier.fillMaxWidth().padding(vertical = 3.dp)) { content() }
}

@Composable
fun ResultCard(title: String, value: String, lines: List<String> = emptyList()) {
    Card(Modifier.fillMaxWidth().padding(vertical = 8.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)) {
        Column(Modifier.padding(18.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
            lines.forEach { Spacer(Modifier.height(6.dp)); Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer) }
        }
    }
}

@Composable
fun ErrorText(text: String?) { if (!text.isNullOrBlank()) Text(text, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(vertical = 8.dp)) }

data class RefRow(val left: String, val right: String)
data class RefSection(val title: String, val body: String? = null, val rows: List<RefRow> = emptyList(), val lines: List<String> = emptyList())
data class ReferencePage(val subtitle: String, val sections: List<RefSection>)

@Composable
fun ReferenceScreen(referencePage: ReferencePage, onBack: () -> Unit) {
    CalculatorPage("参考依据", referencePage.subtitle, onBack) {
        referencePage.sections.forEach { section ->
            SectionCard(section.title) {
                section.body?.let { Text(it) }
                section.rows.forEach { row -> Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(row.left, Modifier.weight(1f)); Spacer(Modifier.width(12.dp)); Text(row.right, fontWeight = FontWeight.Medium) } }
                section.lines.forEach { Text(it, Modifier.padding(vertical = 2.dp)) }
            }
        }
    }
}
