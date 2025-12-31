package com.example.flashcardexpress.feature.questionManagement.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed interface QuestionManagementScreen {
    @Serializable
    data object CreationCategory: QuestionManagementScreen


    @Serializable
    data class CategoryDetails(val categoryId:Int, val categoryName:String): QuestionManagementScreen



}