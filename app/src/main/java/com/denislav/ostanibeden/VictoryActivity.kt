package com.denislav.ostanibeden

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VictoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_victory)

        val coins = intent.getIntExtra("coins", 0)

        val tvVictoryCoins =
            findViewById<TextView>(R.id.tvVictoryCoins)

        val btnVictoryMenu =
            findViewById<Button>(R.id.btnVictoryMenu)

        tvVictoryCoins.text = "Final Coins: $coins"

        btnVictoryMenu.setOnClickListener {

            val intent =
                Intent(this, MainActivity::class.java)

            startActivity(intent)

            finish()
        }
    }
}