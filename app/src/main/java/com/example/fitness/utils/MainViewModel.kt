package com.example.fitness.utils

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitness.adapters.ExerciseModel

class MainViewModel : ViewModel() {
    val mutableListExercise = MutableLiveData<ArrayList<ExerciseModel>>() // Этот объект в себе хранит список из упражнений
    var pref: SharedPreferences? = null //SharedPreferences cлужит для сохранение в постоянное хрпанилище андроид
    var currentDay = 0

    fun savePref(key: String, value: Int){ //функция сохранения выполненных упражнений
        pref?.edit()?.putInt(key, value)?.apply()
    }

    fun getExerciseCount(): Int{ //функция получения сохраненного результата
        return pref?.getInt(currentDay.toString(), 0) ?: 0
    }
}