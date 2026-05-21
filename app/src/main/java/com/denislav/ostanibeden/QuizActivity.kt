package com.denislav.ostanibeden

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.denislav.ostanibeden.data.local.AppDatabase
import com.denislav.ostanibeden.data.local.Question
import com.denislav.ostanibeden.data.repository.QuestionRepository
import com.denislav.ostanibeden.viewmodel.QuestionViewModel
import kotlinx.coroutines.launch

class QuizActivity : AppCompatActivity() {

    private lateinit var viewModel: QuestionViewModel
    private lateinit var tvQuestionCounter: TextView
    private lateinit var tvCoins: TextView
    private lateinit var tvQuestionText: TextView

    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button
    private var questionList = listOf<Question>()

    private var currentQuestionIndex = 0

    private var coins = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_quiz)

        val database = AppDatabase.getDatabase(this)
        val repository = QuestionRepository(database.questionDao())

        viewModel = QuestionViewModel(repository)

        tvQuestionCounter = findViewById<TextView>(R.id.tvQuestionCounter)
        tvCoins = findViewById<TextView>(R.id.tvCoins)
        tvQuestionText = findViewById<TextView>(R.id.tvQuestionText)

        btnA = findViewById<Button>(R.id.btnAnswerA)
        btnB = findViewById<Button>(R.id.btnAnswerB)
        btnC = findViewById<Button>(R.id.btnAnswerC)
        btnD = findViewById<Button>(R.id.btnAnswerD)

        lifecycleScope.launch {

            questionList = viewModel.getRandomQuestions()

            showQuestion(
                tvQuestionCounter,
                tvCoins,
                tvQuestionText,
                btnA,
                btnB,
                btnC,
                btnD
            )
        }

        btnA.setOnClickListener {
            checkAnswer(btnA.text.toString())
        }

        btnB.setOnClickListener {
            checkAnswer(btnB.text.toString())
        }

        btnC.setOnClickListener {
            checkAnswer(btnC.text.toString())
        }

        btnD.setOnClickListener {
            checkAnswer(btnD.text.toString())
        }
    }

    private fun showQuestion(
        tvQuestionCounter: TextView,
        tvCoins: TextView,
        tvQuestionText: TextView,
        btnA: Button,
        btnB: Button,
        btnC: Button,
        btnD: Button
    ) {

        if (currentQuestionIndex >= questionList.size) {

            val intent = Intent(this, VictoryActivity::class.java)

            intent.putExtra("coins", coins)

            startActivity(intent)

            finish()

            return
        }

        val question = questionList[currentQuestionIndex]

        tvQuestionCounter.text =
            "Question ${currentQuestionIndex + 1}/${questionList.size}"

        tvCoins.text = "Coins: $coins"

        tvQuestionText.text = question.questionText

        btnA.text = question.optionA
        btnB.text = question.optionB
        btnC.text = question.optionC
        btnD.text = question.optionD
    }

    private fun checkAnswer(selectedAnswer: String) {

        val currentQuestion = questionList[currentQuestionIndex]

        if (selectedAnswer == currentQuestion.correctAnswer) {

            coins += 10

            Toast.makeText(
                this,
                "Correct! +10 coins",
                Toast.LENGTH_SHORT
            ).show()

            currentQuestionIndex++

            showQuestion(
                tvQuestionCounter,
                tvCoins,
                tvQuestionText,
                btnA,
                btnB,
                btnC,
                btnD
            )
        } else {

            Toast.makeText(
                this,
                "Wrong Answer! Game Over!",
                Toast.LENGTH_LONG
            ).show()

            val intent = Intent(this, GameOverActivity::class.java)

            intent.putExtra("coins", coins)

            startActivity(intent)

            finish()
        }
    }
}