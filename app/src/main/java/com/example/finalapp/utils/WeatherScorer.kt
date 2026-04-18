package com.example.finalapp.utils

object WeatherScorer {

    fun calculateScore(temp: Double): Int {

        return when {
            temp in 20.0..28.0 -> 90
            temp in 15.0..20.0 -> 75
            temp in 28.0..32.0 -> 65
            temp > 35 -> 40
            else -> 55
        }
    }

    fun getMood(score: Int): String {
        return when {
            score >= 85 -> "Perfect day"
            score >= 70 -> "Good day"
            score >= 50 -> "Okayish day"
            else -> "Not a great day"
        }
    }
}