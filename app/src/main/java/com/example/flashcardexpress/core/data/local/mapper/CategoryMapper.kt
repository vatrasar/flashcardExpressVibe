package com.example.flashcardexpress.core.data.local.mapper

import com.example.flashcardexpress.core.data.local.entities.CategoryEntity
import com.example.flashcardexpress.core.domain.model.Category

fun CategoryEntity.toDomain(): Category{
    return Category(
        id = id,
        name = name

    )
}

