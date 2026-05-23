package com.denislav.ostanibeden

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.denislav.ostanibeden.data.local.AppDatabase
import com.denislav.ostanibeden.data.local.Question
import com.denislav.ostanibeden.data.repository.PlayerRepository
import com.denislav.ostanibeden.data.repository.QuestionRepository
import com.denislav.ostanibeden.viewmodel.PlayerViewModel
import com.denislav.ostanibeden.viewmodel.QuestionViewModel
import kotlinx.coroutines.launch

class QuizActivity : AppCompatActivity() {

    private lateinit var questionViewModel: QuestionViewModel
    private lateinit var playerViewModel: PlayerViewModel

    private lateinit var tvQuestionCounter: TextView
    private lateinit var tvCoins: TextView
    private lateinit var tvQuestionText: TextView

    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button

    private lateinit var btn5050: Button
    private lateinit var btnAudience: Button
    private lateinit var btnFriend: Button

    private var questionList = listOf<Question>()

    private var currentQuestionIndex = 0

    private var coins = 0

    private var used5050 = false
    private var usedAudience = false
    private var usedFriend = false

    private var disable5050Curse = false
    private var fakeAudienceCurse = false
    private var halfCoinsCurse = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_quiz)

        initializeDatabase()

        initializeViews()

        loadQuestions()

        loadCurseEffects()

        btn5050 = findViewById(R.id.btn5050)
        btnAudience = findViewById(R.id.btnAudience)
        btnFriend = findViewById(R.id.btnFriend)

        setupButtonListeners()
    }

    private fun initializeDatabase() {

        val database = AppDatabase.getDatabase(this)

        val questionRepository =
            QuestionRepository(database.questionDao())

        val playerRepository =
            PlayerRepository(database.playerDao())

        questionViewModel =
            QuestionViewModel(questionRepository)

        playerViewModel =
            PlayerViewModel(playerRepository)
    }

    private fun initializeViews() {

        tvQuestionCounter =
            findViewById(R.id.tvQuestionCounter)

        tvCoins =
            findViewById(R.id.tvCoins)

        tvQuestionText =
            findViewById(R.id.tvQuestionText)

        btnA =
            findViewById(R.id.btnAnswerA)

        btnB =
            findViewById(R.id.btnAnswerB)

        btnC =
            findViewById(R.id.btnAnswerC)

        btnD =
            findViewById(R.id.btnAnswerD)
    }

    private fun loadQuestions() {

        lifecycleScope.launch {

            questionList =
                questionViewModel.getRandomQuestions()

            if (questionList.isEmpty()) {

                Toast.makeText(
                    this@QuizActivity,
                    "No questions in database!",
                    Toast.LENGTH_LONG
                ).show()

                finish()

            } else {

                showQuestion()
            }
        }
    }

    private fun loadCurseEffects() {

        lifecycleScope.launch {

            val player =
                playerViewModel.getPlayer()

            if (player != null) {

                disable5050Curse =
                    player.disable5050

                fakeAudienceCurse =
                    player.fakeAudience

                halfCoinsCurse =
                    player.halfCoins
            }
        }
    }

    private fun setupButtonListeners() {

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

        btn5050.setOnClickListener {

            use5050Joker()
        }

        btnAudience.setOnClickListener {

            useAudienceJoker()
        }

        btnFriend.setOnClickListener {

            useFriendJoker()
        }
    }

    private fun showQuestion() {

        if (currentQuestionIndex >= questionList.size) {

            openVictoryScreen()

            return
        }

        val question =
            questionList[currentQuestionIndex]

        tvQuestionCounter.text =
            "Question ${currentQuestionIndex + 1}/${questionList.size}"

        tvCoins.text =
            "Coins: $coins"

        tvQuestionText.text =
            question.questionText

        btnA.text =
            question.optionA

        btnB.text =
            question.optionB

        btnC.text =
            question.optionC

        btnD.text =
            question.optionD

        btnA.isEnabled = true
        btnB.isEnabled = true
        btnC.isEnabled = true
        btnD.isEnabled = true

        updateJokerButtons()
    }

    private fun checkAnswer(selectedAnswer: String) {

        val currentQuestion =
            questionList[currentQuestionIndex]

        if (selectedAnswer == currentQuestion.correctAnswer) {

            handleCorrectAnswer()

        } else {

            handleWrongAnswer()
        }
    }

    private fun updateJokerButtons() {

        lifecycleScope.launch {

            val player =
                playerViewModel.getPlayer()

            if (player != null) {

                btn5050.isEnabled =
                    !used5050 ||
                            player.extra5050 > 0

                btnAudience.isEnabled =
                    !usedAudience ||
                            player.extraAudience > 0

                btnFriend.isEnabled =
                    !usedFriend ||
                            player.extraFriend > 0
            }
        }
    }

    private fun use5050Joker() {

        if (disable5050Curse) {

            Toast.makeText(
                this,
                "💀 50/50 CURSED",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (used5050) {

            lifecycleScope.launch {

                val player = playerViewModel.getPlayer()

                if (player != null &&
                    player.extra5050 > 0) {

                    val updatedPlayer = player.copy(
                        extra5050 =
                            player.extra5050 - 1
                    )

                    playerViewModel.updatePlayer(updatedPlayer)

                    runOnUiThread {

                        Toast.makeText(
                            this@QuizActivity,
                            "Extra 50/50 Used!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {

                    runOnUiThread {

                        Toast.makeText(
                            this@QuizActivity,
                            "50/50 already used",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    return@launch
                }
            }
        }

        if (!used5050) {

            used5050 = true
        }

        val currentQuestion =
            questionList[currentQuestionIndex]

        val buttons = listOf(btnA, btnB, btnC, btnD)

        val wrongButtons = buttons.filter {

            it.text != currentQuestion.correctAnswer
        }

        wrongButtons.shuffled()
            .take(2)
            .forEach {

                it.isEnabled = false
                it.text = ""
            }

        Toast.makeText(
            this,
            "50/50 Used",
            Toast.LENGTH_SHORT
        ).show()

        updateJokerButtons()
    }

    private fun useAudienceJoker() {

        if (usedAudience) {

            lifecycleScope.launch {

                val player = playerViewModel.getPlayer()

                if (player != null &&
                    player.extraAudience > 0) {

                    val updatedPlayer = player.copy(
                        extraAudience =
                            player.extraAudience - 1
                    )

                    playerViewModel.updatePlayer(updatedPlayer)

                    runOnUiThread {

                        Toast.makeText(
                            this@QuizActivity,
                            "Extra Audience Joker Used!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {

                    runOnUiThread {

                        Toast.makeText(
                            this@QuizActivity,
                            "Audience joker already used",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    return@launch
                }
            }
        }

        if (!usedAudience) {

            usedAudience = true
        }

        val currentQuestion =
            questionList[currentQuestionIndex]

        val randomChance = (1..100).random()

        val suggestedAnswer = if (fakeAudienceCurse) {

            listOf(
                currentQuestion.optionA,
                currentQuestion.optionB,
                currentQuestion.optionC,
                currentQuestion.optionD
            ).filter {

                it != currentQuestion.correctAnswer

            }.random()

        } else {

            if (randomChance <= 65) {

                currentQuestion.correctAnswer

            } else {

                listOf(
                    currentQuestion.optionA,
                    currentQuestion.optionB,
                    currentQuestion.optionC,
                    currentQuestion.optionD
                ).filter {

                    it != currentQuestion.correctAnswer

                }.random()
            }
        }

        Toast.makeText(
            this,
            "Audience suggests: $suggestedAnswer",
            Toast.LENGTH_LONG
        ).show()

        updateJokerButtons()
    }

    private fun useFriendJoker() {

        if (usedFriend) {

            lifecycleScope.launch {

                val player = playerViewModel.getPlayer()

                if (player != null &&
                    player.extraFriend > 0) {

                    val updatedPlayer = player.copy(
                        extraFriend =
                            player.extraFriend - 1
                    )

                    playerViewModel.updatePlayer(updatedPlayer)

                    runOnUiThread {

                        Toast.makeText(
                            this@QuizActivity,
                            "Extra Friend Joker Used!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {

                    runOnUiThread {

                        Toast.makeText(
                            this@QuizActivity,
                            "Friend joker already used",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    return@launch
                }
            }
        }

        if (!usedFriend) {

            usedFriend = true
        }

        val currentQuestion =
            questionList[currentQuestionIndex]

        val randomChance = (1..100).random()

        val friendAnswer = if (randomChance <= 50) {

            currentQuestion.correctAnswer

        } else {

            listOf(
                currentQuestion.optionA,
                currentQuestion.optionB,
                currentQuestion.optionC,
                currentQuestion.optionD
            ).filter {

                it != currentQuestion.correctAnswer

            }.random()
        }

        Toast.makeText(
            this,
            "Friend says: Bro I think it's $friendAnswer",
            Toast.LENGTH_LONG
        ).show()

        updateJokerButtons()
    }

    private fun handleCorrectAnswer() {

        if (halfCoinsCurse) {

            coins += 5

        } else {

            coins += 10
        }

        val earnedCoins = if (halfCoinsCurse) {
            5
        } else {
            10
        }

        Toast.makeText(
            this,
            "Correct! +$earnedCoins Coins",
            Toast.LENGTH_SHORT
        ).show()

        currentQuestionIndex++

        showQuestion()
    }

    private fun handleWrongAnswer() {

        Toast.makeText(
            this,
            "Wrong Answer! Game Over!",
            Toast.LENGTH_LONG
        ).show()

        saveGameResults()

        val intent =
            Intent(this, GameOverActivity::class.java)

        intent.putExtra("coins", coins)

        startActivity(intent)

        finish()
    }

    private fun openVictoryScreen() {

        saveGameResults()

        val intent =
            Intent(this, VictoryActivity::class.java)

        intent.putExtra("coins", coins)

        startActivity(intent)

        finish()
    }

    private fun saveGameResults() {

        lifecycleScope.launch {

            val player =
                playerViewModel.getPlayer()

            if (player != null) {

                val updatedPlayer = player.copy(

                    totalCoins =
                        player.totalCoins + coins,

                    disable5050 = false,

                    fakeAudience = false,

                    halfCoins = false
                )

                playerViewModel.updatePlayer(
                    updatedPlayer
                )
            }
        }
    }
}