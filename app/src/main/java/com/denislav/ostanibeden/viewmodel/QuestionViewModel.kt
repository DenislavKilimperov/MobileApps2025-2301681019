package com.denislav.ostanibeden.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denislav.ostanibeden.data.local.Question
import com.denislav.ostanibeden.data.repository.QuestionRepository
import kotlinx.coroutines.launch

class QuestionViewModel(
    private val repository: QuestionRepository
) : ViewModel() {

    fun insertQuestion(question: Question) {

        viewModelScope.launch {
            repository.insertQuestion(question)
        }
    }

    fun getAllQuestions() {

        viewModelScope.launch {

            val questions = repository.getAllQuestions()

            println(questions)
        }
    }
}