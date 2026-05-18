package com.denislav.ostanibeden.data.local

import androidx.room.*

@Dao
interface QuestionDao {

    @Insert
    suspend fun insertQuestion(question: Question)

    @Update
    suspend fun updateQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)

    @Query("SELECT * FROM questions")
    suspend fun getAllQuestions(): List<Question>
}