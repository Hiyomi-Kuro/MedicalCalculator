package com.android.medicalcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.android.medicalcalculator.ui.theme.MedicalCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedicalCalculatorTheme(dynamicColor = true) {
                MedicalCalculatorApp()
            }
        }
    }
}

object Routes {
    const val HOME = "home"
    const val BMI = "bmi"
    const val BMI_REF = "bmi_ref"
    const val FIB4 = "fib4"
    const val FIB4_REF = "fib4_ref"
    const val CHILD = "child"
    const val CHILD_REF = "child_ref"
    const val CDAI = "cdai"
    const val CDAI_REF = "cdai_ref"
    const val SESCD = "sescd"
    const val SESCD_REF = "sescd_ref"
    const val MAYO = "mayo"
    const val MAYO_REF = "mayo_ref"
    const val HBI = "hbi"
    const val HBI_REF = "hbi_ref"
    const val CDEIS = "cdeis"
    const val CDEIS_REF = "cdeis_ref"
    const val RUTGEERTS = "rutgeerts"
    const val RUTGEERTS_REF = "rutgeerts_ref"
    const val PDAI = "pdai"
    const val PDAI_REF = "pdai_ref"
    const val UCEIS = "uceis"
    const val UCEIS_REF = "uceis_ref"
    const val SCCAI = "sccai"
    const val SCCAI_REF = "sccai_ref"
    const val TRueloveWitts = "truelove_witts"
    const val TRueloveWitts_REF = "truelove_witts_ref"
    const val OXFORD = "oxford"
    const val OXFORD_REF = "oxford_ref"
    const val PUCAI = "pucai"
    const val PUCAI_REF = "pucai_ref"
    const val PCDAI = "pcdai"
    const val PCDAI_REF = "pcdai_ref"
    const val NANCY = "nancy"
    const val NANCY_REF = "nancy_ref"
    const val RHI = "rhi"
    const val RHI_REF = "rhi_ref"
    const val GEBOES = "geboes"
    const val GEBOES_REF = "geboes_ref"
    const val MONTREAL_CD = "montreal_cd"
    const val MONTREAL_CD_REF = "montreal_cd_ref"
    const val MONTREAL_UC = "montreal_uc"
    const val MONTREAL_UC_REF = "montreal_uc_ref"
}

private fun parentRoute(route: String): String = when (route) {
    Routes.BMI_REF -> Routes.BMI
    Routes.FIB4_REF -> Routes.FIB4
    Routes.CHILD_REF -> Routes.CHILD
    Routes.CDAI_REF -> Routes.CDAI
    Routes.SESCD_REF -> Routes.SESCD
    Routes.MAYO_REF -> Routes.MAYO
    Routes.HBI_REF -> Routes.HBI
    Routes.CDEIS_REF -> Routes.CDEIS
    Routes.RUTGEERTS_REF -> Routes.RUTGEERTS
    Routes.PDAI_REF -> Routes.PDAI
    Routes.UCEIS_REF -> Routes.UCEIS
    Routes.SCCAI_REF -> Routes.SCCAI
    Routes.TRueloveWitts_REF -> Routes.TRueloveWitts
    Routes.OXFORD_REF -> Routes.OXFORD
    Routes.PUCAI_REF -> Routes.PUCAI
    Routes.PCDAI_REF -> Routes.PCDAI
    Routes.NANCY_REF -> Routes.NANCY
    Routes.RHI_REF -> Routes.RHI
    Routes.GEBOES_REF -> Routes.GEBOES
    Routes.MONTREAL_CD_REF -> Routes.MONTREAL_CD
    Routes.MONTREAL_UC_REF -> Routes.MONTREAL_UC
    else -> Routes.HOME
}

@Composable
fun MedicalCalculatorApp() {
    var route by rememberSaveable { mutableStateOf(Routes.HOME) }

    BackHandler(enabled = route != Routes.HOME) {
        route = parentRoute(route)
    }

    val navigate: (String) -> Unit = { route = it }
    val back: () -> Unit = { route = parentRoute(route) }

    when (route) {
        Routes.HOME -> HomeScreen(onOpen = navigate)
        Routes.BMI -> BmiScreen(onBack = back, onReference = { navigate(Routes.BMI_REF) })
        Routes.BMI_REF -> ReferenceScreen(referencePage = ReferencePages.bmi, onBack = back)
        Routes.FIB4 -> Fib4Screen(onBack = back, onReference = { navigate(Routes.FIB4_REF) })
        Routes.FIB4_REF -> ReferenceScreen(referencePage = ReferencePages.fib4, onBack = back)
        Routes.CHILD -> ChildPughScreen(onBack = back, onReference = { navigate(Routes.CHILD_REF) })
        Routes.CHILD_REF -> ReferenceScreen(referencePage = ReferencePages.childPugh, onBack = back)
        Routes.CDAI -> CdaiScreen(onBack = back, onReference = { navigate(Routes.CDAI_REF) })
        Routes.CDAI_REF -> ReferenceScreen(referencePage = ReferencePages.cdai, onBack = back)
        Routes.SESCD -> SescdScreen(onBack = back, onReference = { navigate(Routes.SESCD_REF) })
        Routes.SESCD_REF -> ReferenceScreen(referencePage = ReferencePages.sescd, onBack = back)
        Routes.MAYO -> MayoScreen(onBack = back, onReference = { navigate(Routes.MAYO_REF) })
        Routes.MAYO_REF -> ReferenceScreen(referencePage = ReferencePages.mayo, onBack = back)
        Routes.HBI -> HbiScreen(onBack = back, onReference = { navigate(Routes.HBI_REF) })
        Routes.HBI_REF -> ReferenceScreen(referencePage = IbdReferencePages.hbi, onBack = back)
        Routes.CDEIS -> CdeisScreen(onBack = back, onReference = { navigate(Routes.CDEIS_REF) })
        Routes.CDEIS_REF -> ReferenceScreen(referencePage = IbdReferencePages.cdeis, onBack = back)
        Routes.RUTGEERTS -> RutgeertsScreen(onBack = back, onReference = { navigate(Routes.RUTGEERTS_REF) })
        Routes.RUTGEERTS_REF -> ReferenceScreen(referencePage = IbdReferencePages.rutgeerts, onBack = back)
        Routes.PDAI -> PdaiScreen(onBack = back, onReference = { navigate(Routes.PDAI_REF) })
        Routes.PDAI_REF -> ReferenceScreen(referencePage = IbdReferencePages.pdai, onBack = back)
        Routes.UCEIS -> UceisScreen(onBack = back, onReference = { navigate(Routes.UCEIS_REF) })
        Routes.UCEIS_REF -> ReferenceScreen(referencePage = IbdReferencePages.uceis, onBack = back)
        Routes.SCCAI -> SccaiScreen(onBack = back, onReference = { navigate(Routes.SCCAI_REF) })
        Routes.SCCAI_REF -> ReferenceScreen(referencePage = IbdReferencePages.sccai, onBack = back)
        Routes.TRueloveWitts -> TrueloveWittsScreen(onBack = back, onReference = { navigate(Routes.TRueloveWitts_REF) })
        Routes.TRueloveWitts_REF -> ReferenceScreen(referencePage = IbdReferencePages.trueloveWitts, onBack = back)
        Routes.OXFORD -> OxfordScreen(onBack = back, onReference = { navigate(Routes.OXFORD_REF) })
        Routes.OXFORD_REF -> ReferenceScreen(referencePage = IbdReferencePages.oxford, onBack = back)
        Routes.PUCAI -> PucaiScreen(onBack = back, onReference = { navigate(Routes.PUCAI_REF) })
        Routes.PUCAI_REF -> ReferenceScreen(referencePage = IbdReferencePages.pucai, onBack = back)
        Routes.PCDAI -> PcdaiScreen(onBack = back, onReference = { navigate(Routes.PCDAI_REF) })
        Routes.PCDAI_REF -> ReferenceScreen(referencePage = IbdReferencePages.pcdai, onBack = back)
        Routes.NANCY -> NancyScreen(onBack = back, onReference = { navigate(Routes.NANCY_REF) })
        Routes.NANCY_REF -> ReferenceScreen(referencePage = IbdReferencePages.nancy, onBack = back)
        Routes.RHI -> RhiScreen(onBack = back, onReference = { navigate(Routes.RHI_REF) })
        Routes.RHI_REF -> ReferenceScreen(referencePage = IbdReferencePages.rhi, onBack = back)
        Routes.GEBOES -> GeboesScreen(onBack = back, onReference = { navigate(Routes.GEBOES_REF) })
        Routes.GEBOES_REF -> ReferenceScreen(referencePage = IbdReferencePages.geboes, onBack = back)
        Routes.MONTREAL_CD -> MontrealCrohnScreen(onBack = back, onReference = { navigate(Routes.MONTREAL_CD_REF) })
        Routes.MONTREAL_CD_REF -> ReferenceScreen(referencePage = IbdReferencePages.montrealCrohn, onBack = back)
        Routes.MONTREAL_UC -> MontrealUcScreen(onBack = back, onReference = { navigate(Routes.MONTREAL_UC_REF) })
        Routes.MONTREAL_UC_REF -> ReferenceScreen(referencePage = IbdReferencePages.montrealUc, onBack = back)
        else -> HomeScreen(onOpen = navigate)
    }
}
