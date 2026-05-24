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

        val btnShare =
            findViewById<Button>(R.id.btnShare)

        val btnVictoryMenu =
            findViewById<Button>(R.id.btnVictoryMenu)

        tvVictoryCoins.text = "Final Coins: $coins"

        btnShare.setOnClickListener {

            val shareText =

                "🏆 I won $coins coins in " +
                        "Ostani Beden!\n" +
                        "Can you beat me? 😈"

            val shareIntent = Intent(

                Intent.ACTION_SEND
            )

            shareIntent.type = "text/plain"

            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                shareText
            )

            startActivity(

                Intent.createChooser(
                    shareIntent,
                    "Share your result"
                )
            )
        }

        btnVictoryMenu.setOnClickListener {

            val intent =
                Intent(this, MainActivity::class.java)

            startActivity(intent)

            finish()
        }
    }
}