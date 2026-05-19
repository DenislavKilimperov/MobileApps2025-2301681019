package com.denislav.ostanibeden

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import com.denislav.ostanibeden.data.local.AppDatabase
import com.denislav.ostanibeden.data.local.Question
import com.denislav.ostanibeden.data.repository.QuestionRepository
import com.denislav.ostanibeden.viewmodel.QuestionViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDatabase.getDatabase(this)

        val repository = QuestionRepository(database.questionDao())

        val viewModel = QuestionViewModel(repository)

        val sampleQuestion = Question(
            questionText = "Коя планета е известна като Червената планета?",
            optionA = "Земя",
            optionB = "Марс",
            optionC = "Юпитер",
            optionD = "Венера",
            correctAnswer = "Марс",
            category = "General",
            difficulty = 1
        )

        viewModel.insertQuestion(sampleQuestion)

        viewModel.getAllQuestions()
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val adminButton = findViewById<Button>(R.id.btnAdminPanel)

        adminButton.setOnClickListener {

            val intent = Intent(this, AdminActivity::class.java)

            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}