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

class EditQuestionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_question)

        val database = AppDatabase.getDatabase(this)
        val repository = QuestionRepository(database.questionDao())
        val viewModel = QuestionViewModel(repository)

        val questionId = intent.getIntExtra("id", 0)

        val etQuestion = findViewById<EditText>(R.id.etEditQuestion)
        val etOptionA = findViewById<EditText>(R.id.etEditOptionA)
        val etOptionB = findViewById<EditText>(R.id.etEditOptionB)
        val etOptionC = findViewById<EditText>(R.id.etEditOptionC)
        val etOptionD = findViewById<EditText>(R.id.etEditOptionD)
        val etCorrectAnswer = findViewById<EditText>(R.id.etEditCorrectAnswer)
        val etCategory = findViewById<EditText>(R.id.etEditCategory)
        val etDifficulty = findViewById<EditText>(R.id.etEditDifficulty)

        val btnSaveChanges = findViewById<Button>(R.id.btnSaveChanges)

        etQuestion.setText(intent.getStringExtra("questionText"))
        etOptionA.setText(intent.getStringExtra("optionA"))
        etOptionB.setText(intent.getStringExtra("optionB"))
        etOptionC.setText(intent.getStringExtra("optionC"))
        etOptionD.setText(intent.getStringExtra("optionD"))
        etCorrectAnswer.setText(intent.getStringExtra("correctAnswer"))
        etCategory.setText(intent.getStringExtra("category"))
        etDifficulty.setText(intent.getIntExtra("difficulty", 1).toString())

        btnSaveChanges.setOnClickListener {

            val updatedQuestion = Question(

                id = questionId,

                questionText = etQuestion.text.toString(),

                optionA = etOptionA.text.toString(),
                optionB = etOptionB.text.toString(),
                optionC = etOptionC.text.toString(),
                optionD = etOptionD.text.toString(),

                correctAnswer = etCorrectAnswer.text.toString(),

                category = etCategory.text.toString(),

                difficulty = etDifficulty.text.toString().toInt()
            )

            viewModel.updateQuestion(updatedQuestion)

            Toast.makeText(
                this,
                "Question Updated",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}