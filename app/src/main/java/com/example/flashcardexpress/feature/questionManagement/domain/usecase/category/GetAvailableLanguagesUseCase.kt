package com.example.flashcardexpress.feature.questionManagement.domain.usecase.category

import java.util.Locale
import javax.inject.Inject

/**
 * Use case for getting a list of all available languages on the device.
 *
 * Invoked by:
 * - [CreationCategoryViewModel]
 * - [CategoryEditViewModel]
 */
class GetAvailableLanguagesUseCase @Inject constructor() {
    operator fun invoke(): List<String> {
        return Locale.getAvailableLocales()
            .map { it.language }
            .filter { it.isNotEmpty() }
            .distinct()
            .map { Locale.Builder().setLanguage(it).build() }
            .map { it.getDisplayLanguage(Locale.getDefault()) }
            .filter { it.isNotEmpty() }
            .distinct()
            .map { it.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString() } }
            .sorted()
    }
}
