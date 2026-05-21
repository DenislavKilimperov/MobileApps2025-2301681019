package com.denislav.ostanibeden.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.lifecycle.LiveData

@Dao
interface QuestionDao {

    @Insert
    suspend fun insertQuestion(question: Question)

    @Update
    suspend fun updateQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)

    @Query("SELECT * FROM questions")
    fun getAllQuestions(): LiveData<List<Question>>

    @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT 15")
    suspend fun getRandomQuestions(): List<Question>
}