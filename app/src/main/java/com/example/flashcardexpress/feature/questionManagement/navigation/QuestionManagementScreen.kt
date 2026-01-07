package com.example.flashcardexpress.feature.questionManagement.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed interface QuestionManagementScreen {
    @Serializable
    data object CreationCategory: QuestionManagementScreen


    @Serializable
    data class CategoryDetails(val categoryId:Int, val categoryName:String): QuestionManagementScreen

    @Serializable
    data class CategoryEdit(val categoryId:Int, val categoryName:String): QuestionManagementScreen

    @Serializable
    data class CreationQuestion(val categoryId:Int): QuestionManagementScreen


}




