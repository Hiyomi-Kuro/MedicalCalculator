package com.android.medicalcalculator

object IbdReferencePages {
    val hbi = ReferencePage("HBI · Harvey-Bradshaw Index", listOf(
        RefSection("用途", body = "Crohn 病的简化临床活动度指数；包含总体健康、腹痛、液体/软便次数、腹部包块和并发症。"),
        RefSection("计算规则", lines = listOf("总分 = 总体健康状况（0–4） + 腹痛（0–3） + 过去 24 小时液体/软便次数 + 腹部包块（0–2） + 并发症类别数。")),
        RefSection("常用解释", rows = listOf(RefRow("临床缓解", "<5"), RefRow("轻度活动", "5–7"), RefRow("中度活动", "8–16"), RefRow("重度活动", ">16"))),
        RefSection("局限", body = "HBI 主要是临床症状工具，不能替代内镜、影像或炎症生物标志物；界值来自常用研究分层。"),
        RefSection("主要来源", lines = listOf("Harvey RF, Bradshaw JM. A simple index of Crohn's disease activity. Lancet. 1980.", "ECCO-ESGAR Guideline for Diagnostic Assessment in IBD, 2019."))
    ))

    val cdeis = ReferencePage("CDEIS · Crohn's Disease Endoscopic Index of Severity", listOf(
        RefSection("用途", body = "评价 Crohn 病回肠结肠内镜活动度；原始评分比 SES-CD 复杂，需要受过训练的内镜医师。"),
        RefSection("分段", body = "回肠、右半结肠、横结肠、乙状结肠/左半结肠、直肠。只对实际探索的肠段计算。"),
        RefSection("计算方式", lines = listOf("每个已评估肠段：深溃疡 0 或 12 分；浅表溃疡 0 或 6 分；病变表面 0–10；溃疡表面 0–10。", "各已评估肠段项目总和 ÷ 已评估肠段数，再加：存在溃疡性狭窄 +3，存在非溃疡性狭窄 +3。", "经典总分范围 0–44。")),
        RefSection("常用研究分层", rows = listOf(RefRow("内镜缓解", "<3"), RefRow("轻度", "3–9"), RefRow("中度", "10–12"), RefRow("重度", ">12"))),
        RefSection("主要来源", lines = listOf("Mary JY 等. Development and validation of an endoscopic index of the severity for Crohn's disease. Gut. 1989;30:983–989.", "ECCO consensus on endoscopy in IBD. J Crohns Colitis. 2013."))
    ))

    val rutgeerts = ReferencePage("Rutgeerts · postoperative Crohn recurrence", listOf(
        RefSection("用途", body = "用于回结肠切除术后新末端回肠/吻合口内镜复发分层，不是症状评分。"),
        RefSection("传统评分", rows = listOf(RefRow("i0", "新末端回肠无病变"), RefRow("i1", "≤5 个阿弗他溃疡"), RefRow("i2", ">5 个阿弗他溃疡且间隔黏膜正常，或跳跃分布大病变/小于 1 cm 溃疡；也包括局限于吻合口的病变"), RefRow("i3", "弥漫性阿弗他回肠炎，间隔黏膜弥漫炎症"), RefRow("i4", "弥漫炎症伴大溃疡、结节/鹅卵石样改变和/或狭窄"))),
        RefSection("modified Rutgeerts", body = "将传统 i2 拆为 i2a（病变局限于吻合口）和 i2b（新末端回肠有符合 i2 的病变，可伴吻合口病变）。i2a 与 i2b 的预后差异仍应结合研究证据，不宜机械套用单一治疗阈值。"),
        RefSection("主要来源", lines = listOf("Rutgeerts P 等. Gastroenterology. 1990.", "Contemporary Management of Postoperative Crohn's Disease after Ileocolonic Resection. 2022."))
    ))

    val pdai = ReferencePage("PDAI · Perianal Disease Activity Index", listOf(
        RefSection("用途", body = "用于 Crohn 肛周疾病活动度和相关功能负担评估。"),
        RefSection("五个维度", body = "分泌物、疼痛/活动受限、性生活受限、肛周疾病类型、硬结程度。每项 0–4 分，总分 0–20 分。"),
        RefSection("解释", body = "分数越高表示肛周疾病负担越重；PDAI 部分验证，缺乏适用于所有场景的单一治疗阈值。"),
        RefSection("主要来源", lines = listOf("Irvine EJ 等. Perianal disease activity index in Crohn's disease. 1995.", "Scoring Indices for Perianal Fistulising Crohn’s Disease: A Systematic Review. J Crohns Colitis. 2024."))
    ))

    val uceis = ReferencePage("UCEIS · Ulcerative Colitis Endoscopic Index of Severity", listOf(
        RefSection("用途", body = "评价 UC 最严重肠段的内镜活动度。"),
        RefSection("重标定版项目", rows = listOf(RefRow("血管模式", "0–2"), RefRow("出血", "0–3"), RefRow("糜烂和溃疡", "0–3"), RefRow("总分", "0–8"))),
        RefSection("常用研究分层", rows = listOf(RefRow("缓解", "0"), RefRow("轻度", "1–3"), RefRow("中度", "4–6"), RefRow("重度", "7–8"))),
        RefSection("版本说明", body = "原始 UCEIS 描述曾将每个正常级别编码为 1，形成 3–11 的总范围；后续验证版将正常级别重标为 0，本工具采用 0–8 版。"),
        RefSection("主要来源", lines = listOf("Travis SPL 等. Developing an instrument to assess the endoscopic severity of ulcerative colitis: UCEIS. Gut. 2012.", "Reliability and Initial Validation of the UCEIS. Gastroenterology. 2014."))
    ))

    val sccai = ReferencePage("SCCAI · Simple Clinical Colitis Activity Index", listOf(
        RefSection("项目", body = "日间排便（1–3=0，4–6=1，7–9=2，>9=3）；夜间排便（0=0，1–3=1，4–6=2，>6=3）；紧迫感 0–3；便血 0–3；总体健康 0–4；肠外表现每种 1 分。"),
        RefSection("解释", body = "ECCO-ESGAR 常用临床缓解界值 SCCAI <2；评分需要结合患者报告、实验室和客观炎症评估。"),
        RefSection("主要来源", lines = listOf("Walmsley RS 等. A simple clinical colitis activity index. Gut. 1998;43:29–32.", "ECCO-ESGAR Guideline for Diagnostic Assessment in IBD, 2019."))
    ))

    val trueloveWitts = ReferencePage("Truelove and Witts Severity Index", listOf(
        RefSection("版本", body = "本工具采用经典成人 Truelove–Witts 严重度版本，不将 modified Truelove–Witts 与经典版本混合。"),
        RefSection("经典严重度概念", body = "轻度通常为 <4 次排便/日且无全身毒性；重度为 ≥6 次血便/日并伴至少一项全身毒性指标（体温、脉搏、血红蛋白或 ESR 异常）；其余归中度。不同指南对指标单位和阈值的表述可能略有差异。"),
        RefSection("注意", body = "严重 UC/ASUC 是需要及时临床评估的高风险状态，不能由单一计算器决定住院、救援治疗或手术。"),
        RefSection("主要来源", lines = listOf("Truelove SC, Witts LJ. Cortisone in ulcerative colitis. BMJ. 1955.", "ECCO Guidelines for Ulcerative Colitis."))
    ))

    val oxford = ReferencePage("Oxford / Travis Day-3 criteria", listOf(
        RefSection("用途", body = "在急性重度 UC（ASUC）静脉激素治疗第 3 天，辅助预测激素治疗失败/住院结肠切除风险。"),
        RefSection("规则", body = "第 3 天排便 >8 次/日，或排便 3–8 次/日且 CRP >45 mg/L，即满足 Oxford/Travis 高风险标准。"),
        RefSection("重要安全提示", body = "这是风险预测工具，不是自动治疗决策系统。原始研究报道满足标准者有较高住院结肠切除风险，但现代生物制剂时代的表现可能不同；必须结合感染排除、生命体征、营养、内镜和 IBD 团队判断。"),
        RefSection("主要来源", lines = listOf("Travis SP 等. Predicting outcome in severe ulcerative colitis. Gut. 1996.", "ECCO consensus on acute severe ulcerative colitis."))
    ))

    val pucai = ReferencePage("PUCAI · Pediatric Ulcerative Colitis Activity Index", listOf(
        RefSection("项目", body = "腹痛 0/5/10；直肠出血 0/10/20/30；大多数粪便性状 0/5/10；24 小时排便 0–2/3–5/6–8/>8 对应 0/5/10/15；夜间排便 0/10；活动受限 0/5/10。总分 0–85。"),
        RefSection("疾病活动分层", rows = listOf(RefRow("缓解", "<10"), RefRow("轻度", "10–34"), RefRow("中度", "35–64"), RefRow("重度", "≥65"))),
        RefSection("适用范围", body = "儿童 UC 的非侵入性临床活动度指数；不要直接用于成人或替代内镜/实验室评估。"),
        RefSection("主要来源", lines = listOf("Turner D 等. Development, validation, and evaluation of PUCAI. Gastroenterology. 2007. PMID: 17681163."))
    ))

    val pcdai = ReferencePage("PCDAI · Pediatric Crohn's Disease Activity Index", listOf(
        RefSection("项目结构", body = "原始 PCDAI 总分 0–100，包含腹痛、粪便、一般状况、体重、身高/生长、腹部查体、肛周疾病、肠外表现、HCT、ESR 和白蛋白。大多数项目为 0/5/10；HCT 和 ESR 为 0/2.5/5。"),
        RefSection("常用解释", rows = listOf(RefRow("无活动", "≤10"), RefRow("轻度", "11–30"), RefRow("中重度", ">30"))),
        RefSection("版本注意", body = "PCDAI 有原始、缩短、加权和修改版本。本工具明确采用原始 PCDAI 的分项结构，不把 weighted PCDAI 公式混入。"),
        RefSection("主要来源", lines = listOf("Hyams JS 等. Development and validation of a pediatric Crohn's disease activity index. J Pediatr Gastroenterol Nutr. 1991;12:439–447. PMID: 1678008.", "ECCO/ESPGHAN pediatric Crohn disease guidance."))
    ))

    val nancy = ReferencePage("Nancy Histological Index", listOf(
        RefSection("用途", body = "UC 活检组织学活动度分级，采用层级结构而非简单相加。"),
        RefSection("0–4 级", rows = listOf(RefRow("0", "无明显组织学疾病"), RefRow("1", "慢性炎症浸润，无急性炎症浸润"), RefRow("2", "轻度急性炎症浸润"), RefRow("3", "中重度急性炎症浸润"), RefRow("4", "溃疡"))),
        RefSection("解释", body = "ECCO 组织学立场文件建议临床和观察性研究优先考虑 Nancy Index；常用组织学缓解定义为 0，但所有阈值都应核对具体研究方案。"),
        RefSection("主要来源", lines = listOf("Marchal-Bressenot A 等. Development and validation of the Nancy histological index. Gut. 2017.", "ECCO Position Paper: Harmonization of the Approach to UC Histopathology. 2020."))
    ))

    val rhi = ReferencePage("RHI · Robarts Histopathology Index", listOf(
        RefSection("计算公式", body = "RHI = 固有层慢性炎症×1 + 固有层中性粒细胞×2 + 上皮中性粒细胞×3 + 糜烂/溃疡×5。每个维度 0–3，总分 0–33。"),
        RefSection("用途和局限", body = "用于 UC 组织学活动度；需有病理医师完成各维度判读。常用研究中 RHI ≤3 可作为组织学缓解参考，但不是所有场景的通用临床阈值。"),
        RefSection("主要来源", lines = listOf("Mosli MH 等. Histologic scoring indices for evaluation of disease activity in ulcerative colitis. Gut. 2017.", "ECCO Position Paper on UC Histopathology. 2020."))
    ))

    val geboes = ReferencePage("Geboes Histopathology Score", listOf(
        RefSection("用途", body = "UC 组织学分级，按结构改变、慢性炎症、固有层嗜酸性/中性粒细胞、上皮中性粒细胞、隐窝破坏、糜烂/溃疡依次分级。"),
        RefSection("原始亚级", body = "0.0–0.3；1.0–1.3；2A.0–2A.3；2B.0–2B.3；3.0–3.3；4.0–4.3；5.0–5.4。总范围通常描述为 0–5.4。"),
        RefSection("关键限制", body = "Geboes 是层级分类，不能简单当作普通 0–5 分量表。不同研究对活动、反应和缓解的界值存在差异；本工具保留亚级并仅提供方向性提示。"),
        RefSection("主要来源", lines = listOf("Geboes K 等. A reproducible grading scale for histological assessment of inflammation in ulcerative colitis. Gut. 2000;47:404–409. PMID: 10940279.", "ECCO histopathology harmonization and comparative index studies."))
    ))

    val montrealCrohn = ReferencePage("Montreal Classification · Crohn disease", listOf(
        RefSection("性质", body = "Montreal 是疾病表型分类，不是活动度评分。"),
        RefSection("编码", body = "A1 ≤16 岁，A2 17–40 岁，A3 >40 岁；L1 回肠，L2 结肠，L3 回结肠，L4 上消化道修饰符；B1 非狭窄非穿透，B2 狭窄，B3 穿透；p 表示合并肛周病变。"),
        RefSection("主要来源", lines = listOf("Silverberg MS 等. The Montreal classification of inflammatory bowel disease. Can J Gastroenterol. 2005.", "Montreal classification consensus paper."))
    ))

    val montrealUc = ReferencePage("Montreal Classification · UC", listOf(
        RefSection("Extent", rows = listOf(RefRow("E1", "直肠炎：局限于直肠"), RefRow("E2", "左侧结肠炎：未超过脾曲"), RefRow("E3", "广泛性结肠炎：超过脾曲"))),
        RefSection("严重度", body = "严重度可以使用单独、明确的临床标准记录；本页不把 S0–S3 与 E1–E3 混成一个数值评分。疾病范围可能随病程变化。"),
        RefSection("主要来源", body = "Silverberg MS 等. The Montreal classification of inflammatory bowel disease. Can J Gastroenterol. 2005.")
    ))
}
