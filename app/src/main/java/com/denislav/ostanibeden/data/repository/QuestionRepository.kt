package com.denislav.ostanibeden.data.repository

import com.denislav.ostanibeden.data.local.Question
import com.denislav.ostanibeden.data.local.QuestionDao

class QuestionRepository(
    private val questionDao: QuestionDao
) {

    suspend fun insertQuestion(question: Question) {
        questionDao.insertQuestion(question)
    }

    suspend fun getAllQuestions(): List<Question> {
        return questionDao.getAllQuestions()
    }

    suspend fun updateQuestion(question: Question) {
        questionDao.updateQuestion(question)
    }

    suspend fun deleteQuestion(question: Question) {
        questionDao.deleteQuestion(question)
    }
}