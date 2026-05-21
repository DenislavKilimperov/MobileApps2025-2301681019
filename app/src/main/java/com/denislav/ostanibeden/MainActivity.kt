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

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val adminButton = findViewById<Button>(R.id.btnAdminPanel)

        val startGameButton = findViewById<Button>(R.id.btnStartGame)

        adminButton.setOnClickListener {

            val intent = Intent(this, AdminActivity::class.java)

            startActivity(intent)
        }

        startGameButton.setOnClickListener {

            val intent = Intent(this, QuizActivity::class.java)

            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}