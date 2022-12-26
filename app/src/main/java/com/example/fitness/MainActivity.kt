package com.example.fitness

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.fitness.fragments.DaysFragment
import com.example.fitness.utils.FragmentManager
import com.example.fitness.utils.MainViewModel

class MainActivity : AppCompatActivity() {
    private val model: MainViewModel by viewModels() //подключили ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model.pref = getSharedPreferences("main", MODE_PRIVATE) // подключили таблицу из которой получим количество выполненных упражнений
        FragmentManager.setFragment(DaysFragment.newInstance(), this) // запустили первый фрагмент на активити
    }

    override fun onBackPressed() { // функция нажатия на кнопку стрелочка назад
        if(FragmentManager.currentFragment is DaysFragment) super.onBackPressed() //функция закрытия
        else FragmentManager.setFragment(DaysFragment.newInstance(), this) // запускаем первый фрагмент
    }
}













