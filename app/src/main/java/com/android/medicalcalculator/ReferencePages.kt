package com.android.medicalcalculator

object ReferencePages {
    val bmi = ReferencePage(
        subtitle = "成人 BMI 计算器",
        sections = listOf(
            RefSection("标准名称", body = "WS/T 428-2013《成人体重判定》"),
            RefSection("适用范围", body = "适用于 18 岁及以上成人的体重状态评价。"),
            RefSection("BMI 分类", rows = listOf(
                RefRow("体重过低", "BMI < 18.5"),
                RefRow("体重正常", "18.5 ≤ BMI < 24.0"),
                RefRow("超重", "24.0 ≤ BMI < 28.0"),
                RefRow("肥胖", "BMI ≥ 28.0")
            )),
            RefSection("计算公式", body = "BMI = 体重（kg）/ [身高（m）× 身高（m）]"),
            RefSection("注意事项", body = "BMI 是体重状态评价指标之一。特殊人群及具体健康风险需结合年龄、体成分、疾病状态和其他临床资料综合判断。"),
            RefSection("来源", body = "中华人民共和国卫生行业标准 WS/T 428-2013《成人体重判定》")
        )
    )

    val fib4 = ReferencePage(
        subtitle = "FIB-4 指数",
        sections = listOf(
            RefSection("用途", body = "FIB-4 是基于年龄、AST、ALT 和血小板计数计算的无创肝纤维化评估指标。"),
            RefSection("计算公式", body = "FIB-4 =（年龄 × AST）/（血小板计数 × √ALT）"),
            RefSection("输入单位", rows = listOf(
                RefRow("年龄", "岁"),
                RefRow("AST", "U/L"),
                RefRow("ALT", "U/L"),
                RefRow("血小板计数", "10^9/L")
            )),
            RefSection("MASLD 风险解释：35-64 岁", rows = listOf(
                RefRow("较低风险", "FIB-4 < 1.3"),
                RefRow("需进一步评估", "1.3 ≤ FIB-4 < 2.67"),
                RefRow("较高可能性", "FIB-4 ≥ 2.67")
            )),
            RefSection("MASLD 风险解释：65 岁及以上", rows = listOf(
                RefRow("较低风险", "FIB-4 < 2.0"),
                RefRow("需进一步评估", "2.0 ≤ FIB-4 < 2.67"),
                RefRow("较高可能性", "FIB-4 ≥ 2.67")
            )),
            RefSection("年龄注意事项", body = "年龄较轻成人中 FIB-4 的判别能力较低；老年人需要采用年龄调整后的界值。结果应结合疾病背景和其他无创评估综合判断。"),
            RefSection("临床注意事项", body = "FIB-4 用于风险分层，不等同于肝纤维化的确诊或组织学分期。处于不确定区间或与临床判断不一致时，可结合 VCTE、ELF 或其他检查进一步评估。"),
            RefSection("主要来源", lines = listOf(
                "Sterling RK 等，Hepatology，2006：FIB-4 原始模型。",
                "AASLD：MASLD / NAFLD 无创肝纤维化风险评估路径。"
            ))
        )
    )

    val childPugh = ReferencePage(
        subtitle = "Child-Pugh 评分",
        sections = listOf(
            RefSection("用途", body = "Child-Pugh 评分用于评估肝硬化患者的肝功能储备情况。"),
            RefSection("评分项目", body = "共包括 5 个项目：总胆红素、白蛋白、INR、腹水和肝性脑病。每项计 1-3 分，总分范围为 5-15 分。"),
            RefSection("总胆红素", rows = listOf(
                RefRow("小于 34 μmol/L", "1 分"),
                RefRow("34-51 μmol/L", "2 分"),
                RefRow("大于 51 μmol/L", "3 分")
            )),
            RefSection("白蛋白", rows = listOf(
                RefRow("大于 35 g/L", "1 分"),
                RefRow("28-35 g/L", "2 分"),
                RefRow("小于 28 g/L", "3 分")
            )),
            RefSection("INR", rows = listOf(
                RefRow("小于 1.7", "1 分"),
                RefRow("1.7-2.3", "2 分"),
                RefRow("大于 2.3", "3 分")
            )),
            RefSection("腹水", rows = listOf(
                RefRow("无腹水", "1 分"),
                RefRow("轻度，可控制", "2 分"),
                RefRow("中度以上，治疗后仍明显", "3 分")
            )),
            RefSection("肝性脑病", rows = listOf(
                RefRow("无", "1 分"),
                RefRow("1-2 级", "2 分"),
                RefRow("3-4 级", "3 分")
            )),
            RefSection("分级", rows = listOf(
                RefRow("Child-Pugh A", "5-6 分"),
                RefRow("Child-Pugh B", "7-9 分"),
                RefRow("Child-Pugh C", "10-15 分")
            )),
            RefSection("注意事项", body = "本计算器采用常用 Child-Pugh 评分标准。某些胆汁淤积性肝病可能采用不同的胆红素评分界值，应根据具体疾病背景选择相应标准。"),
            RefSection("临床说明", body = "Child-Pugh 评分用于肝功能储备评估，不能替代完整的临床判断。腹水和肝性脑病属于临床评价项目，应结合患者实际情况进行判定。")
        )
    )

    val cdai = ReferencePage(
        subtitle = "CDAI - Crohn 病活动指数",
        sections = listOf(
            RefSection("用途", body = "CDAI 用于量化 Crohn 病的临床疾病活动度。评分由 8 个加权项目相加得到。"),
            RefSection("1. 稀便或软便次数", body = "连续 7 天稀便或软便总次数。", lines = listOf("7 天总次数 × 2")),
            RefSection("2. 腹痛", rows = listOf(
                RefRow("无", "0 分/天"), RefRow("轻度", "1 分/天"), RefRow("中度", "2 分/天"), RefRow("重度", "3 分/天")
            ), lines = listOf("7 天评分总和 × 5")),
            RefSection("3. 总体健康状况", rows = listOf(
                RefRow("良好", "0 分/天"), RefRow("稍差", "1 分/天"), RefRow("差", "2 分/天"), RefRow("很差", "3 分/天"), RefRow("极差", "4 分/天")
            ), lines = listOf("7 天评分总和 × 7")),
            RefSection("4. 相关并发症", body = "以下每个类别计 1 项：", lines = listOf(
                "关节炎或关节痛",
                "虹膜炎或葡萄膜炎",
                "结节性红斑、坏疽性脓皮病或阿弗他口炎",
                "肛裂、肛周瘘或肛周脓肿",
                "其他瘘",
                "过去 7 天发热高于 37.8°C",
                "并发症类别数 × 20"
            )),
            RefSection("5. 止泻药或阿片类药物", rows = listOf(RefRow("否", "0 分"), RefRow("是", "30 分"))),
            RefSection("6. 腹部包块", rows = listOf(RefRow("无", "0 分"), RefRow("可疑", "20 分"), RefRow("明确", "50 分"))),
            RefSection("7. 血细胞比容 HCT", lines = listOf(
                "男性参考值：47%；(47 - HCT) × 6",
                "女性参考值：42%；(42 - HCT) × 6",
                "本工具中若该项计算值低于 0，则按 0 分处理。"
            )),
            RefSection("8. 体重偏差", lines = listOf(
                "本工具根据性别和身高自动计算标准体重：",
                "男性：身高(m) × 身高(m) × 22.1",
                "女性：身高(m) × 身高(m) × 20.8",
                "体重项目得分：100 × (标准体重 - 当前体重) / 标准体重",
                "该项目最低记为 -10 分。"
            )),
            RefSection("CDAI 总分", body = "CDAI = 以上 8 个加权项目之和"),
            RefSection("常用活动度解释", rows = listOf(
                RefRow("临床缓解", "＜150"),
                RefRow("轻度至中度活动", "150 ≤ CDAI < 220"),
                RefRow("中度至重度活动", "220 ≤ CDAI ≤ 450"),
                RefRow("重度活动", "＞450")
            )),
            RefSection("临床注意事项", body = "CDAI 主要反映 Crohn 病的临床症状活动度。症状评分与客观炎症程度并不总是一致，不能单独替代内镜、影像、CRP、粪钙卫蛋白等客观评估。"),
            RefSection("主要来源", lines = listOf(
                "Best WR 等，Gastroenterology，1976：Crohn's Disease Activity Index 的建立。",
                "CDAI 后续临床研究及临床试验计算规范。"
            ))
        )
    )

    val sescd = ReferencePage(
        subtitle = "Crohn 病简化内镜评分（SES-CD）",
        sections = listOf(
            RefSection("用途", body = "SES-CD 用于评价 Crohn 病的内镜疾病活动度。分别在 5 个肠段评估 4 个内镜变量，最后将各项得分相加。"),
            RefSection("评估肠段", lines = listOf("1. 回肠", "2. 右半结肠", "3. 横结肠", "4. 左半结肠", "5. 直肠")),
            RefSection("1. 溃疡大小", rows = listOf(
                RefRow("无溃疡", "0 分"),
                RefRow("阿弗他溃疡，0.1～0.5 cm", "1 分"),
                RefRow("较大溃疡，0.5～2.0 cm", "2 分"),
                RefRow("巨大溃疡，＞2.0 cm", "3 分")
            )),
            RefSection("2. 溃疡表面比例", rows = listOf(
                RefRow("无", "0 分"), RefRow("＜10%", "1 分"), RefRow("10%～30%", "2 分"), RefRow("＞30%", "3 分")
            )),
            RefSection("3. 受累表面比例", rows = listOf(
                RefRow("未受累", "0 分"), RefRow("＜50%", "1 分"), RefRow("50%～75%", "2 分"), RefRow("＞75%", "3 分")
            )),
            RefSection("4. 狭窄", rows = listOf(
                RefRow("无狭窄", "0 分"),
                RefRow("单发狭窄，内镜可通过", "1 分"),
                RefRow("多发狭窄，内镜可通过", "2 分"),
                RefRow("狭窄，内镜不能通过", "3 分")
            )),
            RefSection("总分计算", lines = listOf(
                "SES-CD = 5 个肠段中各项可评估得分之和",
                "经典 SES-CD 总分范围为 0～56 分。得分越高，代表内镜病变负荷越重。"
            )),
            RefSection("不可通过狭窄", lines = listOf(
                "如果内镜因狭窄不能继续通过，狭窄近端未实际观察到的肠段属于未评估，不能解释为正常肠段。",
                "当存在未评估肠段时，SES-CD 总分可能低估整体内镜病变负荷，应结合内镜报告及其他影像资料解释。"
            )),
            RefSection("临床解释", body = "SES-CD 的疾病活动度、内镜缓解和内镜反应界值会因研究设计和临床用途不同而有所差异。本工具暂不使用单一界值自动判断疾病严重程度。"),
            RefSection("主要来源", body = "Daperno M 等，Gastrointestinal Endoscopy，2004：Development and validation of a new, simplified endoscopic activity score for Crohn's disease: the SES-CD.")
        )
    )

    val mayo = ReferencePage(
        subtitle = "溃疡性结肠炎 Mayo 评分",
        sections = listOf(
            RefSection("三种评分模式", rows = listOf(
                RefRow("完整 Mayo Score", "4 项  0～12 分"),
                RefRow("改良 Mayo Score（mMS）", "3 项  0～9 分"),
                RefRow("部分 Mayo Score", "3 项  0～9 分")
            )),
            RefSection("完整 Mayo Score", lines = listOf(
                "由以下 4 个分项相加：",
                "排便频率", "直肠出血", "医师总体评价（PGA）", "内镜表现（MES）",
                "总分范围：0～12 分"
            )),
            RefSection("改良 Mayo Score（mMS）", lines = listOf(
                "删除医师总体评价（PGA），由以下 3 个分项相加：",
                "排便频率", "直肠出血", "内镜表现",
                "总分范围：0～9 分",
                "本工具的改良 Mayo 模式采用 FDA 2022 年 Ulcerative Colitis: Developing Drugs for Treatment 指南草案中的 mMS 定义。"
            )),
            RefSection("部分 Mayo Score", lines = listOf(
                "不包含内镜分项，由以下 3 个临床分项相加：",
                "排便频率", "直肠出血", "医师总体评价（PGA）",
                "总分范围：0～9 分"
            )),
            RefSection("排便频率", rows = listOf(
                RefRow("患者正常排便次数", "0 分"),
                RefRow("比正常增加 1～2 次/日", "1 分"),
                RefRow("比正常增加 3～4 次/日", "2 分"),
                RefRow("比正常增加 5 次/日", "3 分")
            ), lines = listOf("三种 Mayo 模式均使用该排便频率分项。")),
            RefSection("经典 Mayo / 部分 Mayo：直肠出血", rows = listOf(
                RefRow("无肉眼血", "0 分"),
                RefRow("不到一半排便可见血丝", "1 分"),
                RefRow("大多数排便可见明显血液", "2 分"),
                RefRow("排出纯血，无粪便", "3 分")
            )),
            RefSection("改良 Mayo：直肠出血", rows = listOf(
                RefRow("未见血", "0 分"),
                RefRow("粪便伴血丝", "1 分"),
                RefRow("粪便中血液超过血丝程度", "2 分"),
                RefRow("单独排血", "3 分")
            ), lines = listOf("采用 FDA mMS 定义。")),
            RefSection("医师总体评价（PGA）", rows = listOf(
                RefRow("正常 / 无活动", "0 分"),
                RefRow("轻度活动", "1 分"),
                RefRow("中度活动", "2 分"),
                RefRow("重度活动", "3 分")
            ), lines = listOf("PGA 用于完整 Mayo 和部分 Mayo，改良 Mayo 不包含该项。")),
            RefSection("经典 Mayo：内镜表现（MES）", rows = listOf(
                RefRow("正常或非活动期", "0 分"),
                RefRow("红斑、血管纹理减少、轻度脆性", "1 分"),
                RefRow("明显红斑、血管纹理消失、脆性、糜烂", "2 分"),
                RefRow("自发性出血、溃疡", "3 分")
            )),
            RefSection("改良 Mayo：内镜表现", rows = listOf(
                RefRow("黏膜正常", "0 分"),
                RefRow("红斑、血管纹理减少，无脆性", "1 分"),
                RefRow("明显红斑、血管纹理消失、脆性、糜烂", "2 分"),
                RefRow("自发性出血、溃疡", "3 分")
            ), lines = listOf("与经典 MES 的重要区别之一：改良 Mayo 的内镜 1 分明确要求无脆性。")),
            RefSection("解释注意事项", body = "完整 Mayo、改良 Mayo 和部分 Mayo 并不是可以直接互换的同一个量表。比较不同时间点或不同研究结果时，应确认所使用的具体评分版本及定义。"),
            RefSection("主要来源", lines = listOf(
                "Mayo Score for Ulcerative Colitis：排便频率、直肠出血、医师总体评价和内镜表现。",
                "U.S. FDA，2022，Ulcerative Colitis: Developing Drugs for Treatment，modified Mayo Score（mMS）定义。",
                "Partial Mayo Score：排便频率、直肠出血和医师总体评价。"
            ))
        )
    )
}
