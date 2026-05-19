package com.denislav.ostanibeden

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.denislav.ostanibeden.data.local.AppDatabase
import com.denislav.ostanibeden.data.local.Question
import com.denislav.ostanibeden.data.repository.QuestionRepository
import com.denislav.ostanibeden.viewmodel.QuestionViewModel

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_admin)

        val database = AppDatabase.getDatabase(this)
        val repository = QuestionRepository(database.questionDao())
        val viewModel = QuestionViewModel(repository)

        val etQuestion = findViewById<EditText>(R.id.etQuestion)
        val etOptionA = findViewById<EditText>(R.id.etOptionA)
        val etOptionB = findViewById<EditText>(R.id.etOptionB)
        val etOptionC = findViewById<EditText>(R.id.etOptionC)
        val etOptionD = findViewById<EditText>(R.id.etOptionD)
        val etCorrectAnswer = findViewById<EditText>(R.id.etCorrectAnswer)
        val etCategory = findViewById<EditText>(R.id.etCategory)
        val etDifficulty = findViewById<EditText>(R.id.etDifficulty)

        val btnAddQuestion = findViewById<Button>(R.id.btnAddQuestion)

        btnAddQuestion.setOnClickListener {

            val question = Question(
                questionText = etQuestion.text.toString(),
                optionA = etOptionA.text.toString(),
                optionB = etOptionB.text.toString(),
                optionC = etOptionC.text.toString(),
                optionD = etOptionD.text.toString(),
                correctAnswer = etCorrectAnswer.text.toString(),
                category = etCategory.text.toString(),
                difficulty = etDifficulty.text.toString().toInt()
            )

            viewModel.insertQuestion(question)

            Toast.makeText(
                this,
                "Question Added",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}