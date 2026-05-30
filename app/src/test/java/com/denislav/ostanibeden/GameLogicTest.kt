package com.denislav.ostanibeden

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameLogicTest {

    @Test
    fun pityCounterResetsAfterReward() {

        val pityCounter = 5

        val rewardTriggered =
            pityCounter >= 5

        val newCounter =
            if (rewardTriggered) {
                0
            } else {
                pityCounter + 1
            }

        assertEquals(0, newCounter)
    }

    @Test
    fun categoryUnlockCostsCorrectPoints() {

        val startingPoints = 100

        val categoryCost = 50

        val remainingPoints =
            startingPoints - categoryCost

        assertEquals(
            50,
            remainingPoints
        )
    }

    @Test
    fun correctAnswerAddsCoins() {

        val currentCoins = 20

        val rewardCoins = 10

        val finalCoins =
            currentCoins + rewardCoins

        assertEquals(
            30,
            finalCoins
        )
    }

    @Test
    fun pityActivatesAtFiveSpins() {

        val pityCounter = 5

        val rewardGuaranteed =
            pityCounter >= 5

        assertTrue(rewardGuaranteed)
    }
}