package com.example.flashcardexpress.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flashcardexpress.core.data.local.entities.QuestionLinkEntity

@Dao
interface QuestionLinkDao {

    @Query("SELECT linked_question_id FROM question_links WHERE question_id = :questionId")
    suspend fun getLinkedQuestionIds(questionId: Int): List<Int>

    @Query("SELECT ql.* FROM question_links ql INNER JOIN flashcard f ON ql.question_id = f.id WHERE f.category_id = :categoryId")
    suspend fun getAllLinksForCategory(categoryId: Int): List<QuestionLinkEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLink(link: QuestionLinkEntity)

    @Query("DELETE FROM question_links WHERE (question_id = :questionId1 AND linked_question_id = :questionId2) OR (question_id = :questionId2 AND linked_question_id = :questionId1)")
    suspend fun deleteBidirectionalLink(questionId1: Int, questionId2: Int)
}
