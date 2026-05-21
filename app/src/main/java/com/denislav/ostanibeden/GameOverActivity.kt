package com.denislav.ostanibeden

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_game_over)

        val tvFinalCoins = findViewById<TextView>(R.id.tvFinalCoins)

        val btnBackToMenu =
            findViewById<Button>(R.id.btnBackToMenu)

        val coins = intent.getIntExtra("coins", 0)

        tvFinalCoins.text = "Coins Earned: $coins"

        btnBackToMenu.setOnClickListener {

            val intent =
                Intent(this, MainActivity::class.java)

            startActivity(intent)

            finish()
        }
    }
}