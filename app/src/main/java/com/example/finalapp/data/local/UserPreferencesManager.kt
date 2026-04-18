package com.example.finalapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore("user_prefs")
val CITY = stringPreferencesKey("city")
class UserPreferencesManager(private val context: Context) {

    companion object {
        val USER_TYPE = stringPreferencesKey("user_type")
    }

    val userPreferencesFlow: Flow<UserPreferences> =
        context.dataStore.data.map { prefs ->
            UserPreferences(
                userType = prefs[USER_TYPE] ?: "student",
                city = prefs[CITY] ?: "Lucknow"
            )
        }

    suspend fun saveUserType(type: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_TYPE] = type
        }
    }
    suspend fun saveCity(city: String) {
        context.dataStore.edit { prefs ->
            prefs[CITY] = city
        }
    }

}