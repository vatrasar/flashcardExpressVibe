package com.example.flashcardexpress.feature.repeat.domain.model

import android.os.Parcelable
import com.example.flashcardexpress.feature.repeat.domain.model.enums.LearningStage
import kotlinx.parcelize.Parcelize

@Parcelize
data class LearningSessionBackupState(val learningStage: LearningStage,
                                      val isWordAQuestionInFlashcard: Boolean,
                                      val questionsToLearn:List<QuestionToLearn>,
                                      val initialEvaluationMistakes:List<QuestionToLearn>,
                                      val learningQueue:List<QuestionToLearn>,
                                      val currentQuestion: QuestionToLearn,
                                      val questionsCorrectInFirstStage:List<QuestionToLearn> ) : Parcelable
