package com.example.finalapp.utils

import java.util.*

object SuggestionEngine {

    fun getSuggestion(temp: Double, hour: Int, userType: String): String {

        val timeContext = when (hour) {
            in 5..8 -> "morning"
            in 9..12 -> "late morning"
            in 13..17 -> "afternoon"
            in 18..21 -> "evening"
            else -> "night"
        }

        return when {
            temp > 35 -> random(
                "Too hot. Stay inside.",
                "Heat is high. Avoid outdoors.",
                "Not a great time to step out."
            )

            temp in 20.0..30.0 -> random(
                "Perfect weather to be productive.",
                "Great time to go out or study.",
                "Balanced conditions. Use it well."
            )

            else -> random(
                "Decent weather. Plan flexibly.",
                "Nothing extreme. Continue your routine."
            )
        }
    }
    private fun random(vararg options: String): String {
        return options.random()
    }

    private fun studentLogic(temp: Double, time: String): String {
        return when {
            temp > 35 -> "Too hot. Stay inside and study during the $time."
            temp in 20.0..30.0 && time == "morning" -> "Fresh morning. Good time to revise or study."
            time == "evening" -> "Good time for light study or relaxation."
            else -> "Balanced time. Continue your study routine."
        }
    }

    private fun fitnessLogic(temp: Double, time: String): String {
        return when {
            temp > 35 -> "Skip outdoor workout. Do indoor training."
            temp in 15.0..28.0 && time == "morning" -> "Perfect weather for a workout."
            time == "evening" -> "Evening walk or gym session works well."
            else -> "Light activity recommended."
        }
    }

    private fun generalLogic(temp: Double, time: String): String {
        return when {
            temp < 10 -> "Too cold. Stay cozy indoors."
            temp in 20.0..30.0 -> "Great weather during the $time. Go out or be productive."
            temp > 35 -> "Extreme heat. Avoid going outside."
            else -> "Decent weather. Plan flexibly."
        }
    }
}