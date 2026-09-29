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
            MedicalCalculatorTheme(dynamicColor = false) {
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
}

private fun parentRoute(route: String): String = when (route) {
    Routes.BMI_REF -> Routes.BMI
    Routes.FIB4_REF -> Routes.FIB4
    Routes.CHILD_REF -> Routes.CHILD
    Routes.CDAI_REF -> Routes.CDAI
    Routes.SESCD_REF -> Routes.SESCD
    Routes.MAYO_REF -> Routes.MAYO
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
        else -> HomeScreen(onOpen = navigate)
    }
}
