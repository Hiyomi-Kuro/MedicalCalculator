# Medical Calculator

面向临床评分与辅助评估场景的 Android 医学计算器，当前以炎症性肠病（IBD）相关量表为重点，同时提供常用肝病与基础评估工具。

应用使用 **Kotlin + Jetpack Compose + Material 3** 开发。所有计算与参考内容均在本地完成；当前 `AndroidManifest.xml` 未声明网络权限，因此现有功能可离线使用。

> **医学声明**
>
> 本项目仅用于临床评分、教学与辅助评估，不能替代完整病史、体格检查、实验室检查、内镜、影像学、病理检查及医生的临床判断。量表阈值、版本和适用人群可能因指南、研究设计或临床场景而异，正式使用前应核对原始文献和本机构规范。

## 功能概览

当前主页登记 **21 个医学计算/分类工具**，支持：

- 中文名称、英文名称及关键词搜索；
- 按 IBD、IBD 组织学、IBD 疾病分类、肝病、基础评估分类筛选；
- 计算结果与常用临床解释；
- 每个工具独立的“参考依据”页面；
- 输入范围校验、重置/清空；
- Android 系统返回键与页面返回导航；
- Material 3 动态配色。

## 已实现工具

### IBD：Crohn 病

| 工具 | 用途 |
| --- | --- |
| CDAI | Crohn's Disease Activity Index，经典 8 项加权临床活动度评分 |
| HBI | Harvey-Bradshaw Index，Crohn 病简化临床活动度指数 |
| SES-CD | Simple Endoscopic Score for Crohn's Disease，Crohn 病简化内镜评分 |
| CDEIS | Crohn's Disease Endoscopic Index of Severity，Crohn 病内镜严重度指数 |
| Rutgeerts | 回结肠切除术后内镜复发分层，包含传统及 modified Rutgeerts 的 i2a/i2b |
| PDAI | Perianal Disease Activity Index，Crohn 肛周疾病活动指数 |

### IBD：溃疡性结肠炎与 ASUC

| 工具 | 用途 |
| --- | --- |
| Mayo Score | 支持完整 Mayo、modified Mayo 和 partial Mayo |
| UCEIS | Ulcerative Colitis Endoscopic Index of Severity |
| SCCAI | Simple Clinical Colitis Activity Index |
| Truelove–Witts | 经典成人 UC 严重度分层 |
| Oxford / Travis Day-3 | ASUC 静脉激素治疗第 3 天失败风险标准 |
| PUCAI | Pediatric Ulcerative Colitis Activity Index |

### IBD：儿童 Crohn 病

| 工具 | 用途 |
| --- | --- |
| PCDAI | Pediatric Crohn's Disease Activity Index，采用原始 PCDAI 分项结构 |

### IBD：组织学

| 工具 | 用途 |
| --- | --- |
| Nancy Histological Index | UC 组织学 0–4 级分层 |
| RHI | Robarts Histopathology Index |
| Geboes | Geboes Histopathology Score，保留 0.0–5.4 亚级结构 |

### IBD：疾病分类

| 工具 | 用途 |
| --- | --- |
| Montreal CD | Crohn 病 Montreal A/L/B 表型分类，支持 L4 与 p 修饰符 |
| Montreal UC | UC Montreal E1/E2/E3 病变范围分类 |

### 肝病与基础评估

| 工具 | 用途 |
| --- | --- |
| FIB-4 | 基于年龄、AST、ALT、血小板计数的肝纤维化风险分层 |
| Child-Pugh | 基于总胆红素、白蛋白、INR、腹水和肝性脑病的肝功能储备评分 |
| 成人 BMI | 按中国成人 BMI 界值进行体重状态评价 |

## 参考依据

应用内为各工具提供独立的“参考依据”页面，包含计算规则、适用范围、常用阈值、版本说明、局限性和主要文献来源。

当前源码中列出的参考来源包括但不限于：

- WS/T 428-2013《成人体重判定》；
- Sterling 等关于 FIB-4 的原始研究，以及 AASLD 相关风险分层路径；
- Best 等关于 CDAI 的原始研究；
- Daperno 等关于 SES-CD 的开发与验证研究；
- Mayo / modified Mayo / partial Mayo 的相关定义；
- Harvey-Bradshaw Index、CDEIS、Rutgeerts、PDAI、UCEIS、SCCAI；
- Truelove and Witts、Oxford / Travis Day-3；
- PUCAI、PCDAI；
- Nancy、RHI、Geboes；
- Montreal IBD classification。

具体条目以应用内“参考依据”页面和对应源码为准。

## 技术栈

| 项目 | 当前配置 |
| --- | --- |
| 语言 | Kotlin 2.2.10 |
| UI | Jetpack Compose + Material 3 |
| Android Gradle Plugin | 9.3.3 |
| Gradle Wrapper | 9.5.0 |
| Compose BOM | 2026.02.01 |
| compileSdk | Android 36.1 |
| targetSdk | 36 |
| minSdk | 24 |
| Java source/target compatibility | Java 11 |
| applicationId | `com.android.medicalcalculator` |

主要 AndroidX 依赖包括 Core KTX、Lifecycle Runtime KTX、Activity Compose、Compose UI、Material 3、Compose UI Tooling 与测试组件。

## 项目结构

```text
MedicalCalculator/
├─ app/
│  ├─ build.gradle.kts
│  └─ src/
│     ├─ main/
│     │  ├─ AndroidManifest.xml
│     │  ├─ java/com/android/medicalcalculator/
│     │  │  ├─ MainActivity.kt
│     │  │  ├─ CommonUi.kt
│     │  │  ├─ CoreCalculators.kt
│     │  │  ├─ BasicCalculatorScreens.kt
│     │  │  ├─ CdaiScreen.kt
│     │  │  ├─ SescdScreen.kt
│     │  │  ├─ MayoScreen.kt
│     │  │  ├─ IbdCalculators.kt
│     │  │  ├─ IbdScreens.kt
│     │  │  ├─ ReferencePages.kt
│     │  │  ├─ IbdReferencePages.kt
│     │  │  └─ ui/theme/
│     │  └─ res/
│     ├─ test/
│     └─ androidTest/
├─ gradle/
│  ├─ libs.versions.toml
│  └─ wrapper/
├─ build.gradle.kts
├─ settings.gradle.kts
├─ gradlew
└─ gradlew.bat
```

### 代码职责

- `MainActivity.kt`：应用入口与轻量级页面路由。
- `CommonUi.kt`：主页、搜索/分类、通用输入控件、结果卡片、参考依据页面等 Compose UI。
- `CoreCalculators.kt`：BMI、FIB-4、Child-Pugh、CDAI、SES-CD、Mayo 等核心计算函数。
- `BasicCalculatorScreens.kt`：BMI、FIB-4、Child-Pugh 页面。
- `CdaiScreen.kt`、`SescdScreen.kt`、`MayoScreen.kt`：对应 IBD 重点量表页面。
- `IbdCalculators.kt`：HBI、CDEIS、PDAI、UCEIS、SCCAI、PUCAI、PCDAI、RHI、Montreal 等计算/分类逻辑。
- `IbdScreens.kt`：扩展 IBD 工具的 Compose 页面。
- `ReferencePages.kt`、`IbdReferencePages.kt`：各量表的计算依据、阈值说明、局限性与文献来源。

## 构建与运行

### 1. 环境

推荐使用支持当前 **AGP 9.3.3 / Gradle 9.5.0** 的 Android Studio，并配置相应 Android SDK。

项目需要安装：

- Android SDK Platform 36；
- Android SDK Build Tools（由 Android Studio / Gradle 按项目配置解析）；
- 与当前 Android Gradle Plugin 兼容的 JDK。

### 2. Android Studio

1. 使用 Android Studio 打开项目根目录。
2. 等待 Gradle Sync 完成。
3. 选择 Android 7.0（API 24）或更高版本的模拟器/真机。
4. 运行 `app` 配置。

### 3. 命令行构建

Windows：

```powershell
.\gradlew.bat assembleDebug
```

macOS / Linux：

```bash
./gradlew assembleDebug
```

Debug APK 默认生成到：

```text
app/build/outputs/apk/debug/app-debug.apk
```

## 测试

运行本地单元测试：

```powershell
.\gradlew.bat testDebugUnitTest
```

运行设备/模拟器上的 instrumentation tests：

```powershell
.\gradlew.bat connectedDebugAndroidTest
```

### 当前测试状态

当前 `app/src/test` 和 `app/src/androidTest` 仍主要是 Android Studio 默认示例测试，尚未覆盖各医学量表的公式、边界值和异常输入。

如果项目用于研究或临床辅助，建议优先补充：

1. 每个量表的原始文献示例或独立人工核算用例；
2. 所有关键阈值的边界测试；
3. 无效输入、缺失输入和极端值测试；
4. 不同量表版本之间的回归测试；
5. UI 输入与底层计算函数的一致性测试。

## 设计说明

### 本地优先

当前 Manifest 未声明 `INTERNET` 权限，计算逻辑与参考资料均随应用本地打包，因此现有功能不依赖服务器。

### 计算与解释分离

底层计算逻辑主要集中在 `CoreCalculators.kt` 和 `IbdCalculators.kt`，Compose 页面负责输入、展示和提示。对于部分量表，界值解释会特别注明“常用研究分层”“风险预测”或“不能替代临床判断”，避免把研究终点直接当作诊断或治疗决策。

### 版本意识

多个 IBD 工具存在不同版本或使用场景。本项目在 UI 与参考页面中明确区分，例如：

- full Mayo、modified Mayo 与 partial Mayo；
- 传统 Rutgeerts 与 modified Rutgeerts；
- 经典 Truelove–Witts；
- UCEIS 0–8 重标定版本；
- 原始 PCDAI；
- Montreal 分类作为疾病表型/范围分类，而非活动度评分。

## 隐私

当前版本：

- 不申请网络权限；
- 不包含账号系统；
- 不上传计算输入；
- 不依赖远程 API。

仍建议在临床环境中避免录入不必要的可识别患者信息，并遵循所在机构的数据安全与隐私规范。

## 开发注意事项

本项目属于医学计算软件，新增或修改量表时建议同时完成：

1. 明确量表版本、适用人群和计分单位；
2. 将计算逻辑与 UI 分离；
3. 为公式和阈值增加单元测试；
4. 在“参考依据”页面记录原始文献或指南；
5. 对研究阈值与临床决策阈值进行明确区分；
6. 对未评估项目、不可通过狭窄等特殊情况避免默认按正常值处理。

---

**Medical Calculator** 目前定位为以 IBD 为重点、可离线运行的临床评分与辅助评估 Android 应用。
