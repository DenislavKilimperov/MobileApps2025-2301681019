package com.denislav.ostanibeden.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denislav.ostanibeden.data.local.Question
import com.denislav.ostanibeden.data.repository.QuestionRepository
import kotlinx.coroutines.launch

class QuestionViewModel(
    private val repository: QuestionRepository
) : ViewModel() {

    val allQuestions = repository.getAllQuestions()

    fun insertQuestion(question: Question) {

        viewModelScope.launch {
            repository.insertQuestion(question)
        }
    }

    fun deleteQuestion(question: Question) {

        viewModelScope.launch {

            repository.deleteQuestion(question)
        }
    }

    fun updateQuestion(question: Question) {

        viewModelScope.launch {

            repository.updateQuestion(question)
        }
    }

    suspend fun getQuestionsByCategory(category: String) =
        repository.getQuestionsByCategory(category)
}