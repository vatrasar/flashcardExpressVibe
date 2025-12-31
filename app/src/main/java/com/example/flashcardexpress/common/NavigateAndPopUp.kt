package com.example.flashcardexpress.common

import androidx.navigation.NavController
import com.example.flashcardexpress.feature.questionManagement.navigation.QuestionManagementScreen
import com.example.flashcardexpress.navigation.Screen

inline fun<reified NavigationTarget: Any,reified PreviousRoute:Any> navigateAndPopUp(navController: NavController, navTarget:NavigationTarget) {
    navController.navigate(navTarget) {
        popUpTo<PreviousRoute> {
            inclusive = true
        }
    }
}