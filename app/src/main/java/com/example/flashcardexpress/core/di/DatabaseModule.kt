package com.example.flashcardexpress.core.di

import android.content.Context
import androidx.room.Room.databaseBuilder
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.flashcardexpress.core.data.local.FlashcardDb
import com.example.flashcardexpress.core.data.local.dao.CategoryDao
import com.example.flashcardexpress.core.data.local.dao.QuestionDao
import com.example.flashcardexpress.core.data.local.dao.QuestionLinkDao
import com.example.flashcardexpress.core.data.local.dao.RepetitionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE category ADD COLUMN language TEXT NOT NULL DEFAULT 'English'")
    }
}

private val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE category ADD COLUMN learned_words_count INTEGER NOT NULL DEFAULT 0")
    }
}

private val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS question_links (" +
                "question_id INTEGER NOT NULL, " +
                "linked_question_id INTEGER NOT NULL, " +
                "PRIMARY KEY(question_id, linked_question_id), " +
                "FOREIGN KEY(question_id) REFERENCES flashcard(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(linked_question_id) REFERENCES flashcard(id) ON DELETE CASCADE" +
            ")"
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS index_question_links_question_id ON question_links (question_id)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_question_links_linked_question_id ON question_links (linked_question_id)")
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): FlashcardDb {
        return databaseBuilder(
            context,
            FlashcardDb::class.java,
            FlashcardDb.DATABASE_NAME
        )
            .addMigrations(MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8)
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    fun provideFlashcardDao(database: FlashcardDb): CategoryDao {

        return database.flashcardDao()
    }

    @Provides
    fun provideQuestionDao(database: FlashcardDb): QuestionDao {
        return database.questionDao()
    }

    @Provides
    fun provideRepetitionDao(database: FlashcardDb): RepetitionDao {
        return database.repetitionDao()
    }

    @Provides
    fun provideQuestionLinkDao(database: FlashcardDb): QuestionLinkDao {
        return database.questionLinkDao()
    }

}