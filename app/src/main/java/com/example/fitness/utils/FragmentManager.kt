package com.example.fitness.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fitness.R

object FragmentManager {
    var currentFragment: Fragment? = null //переменная в которой записывается какой фрагмент включен

    fun setFragment(newFragment: Fragment, act: AppCompatActivity){ //setFragment нужен для переклбючения между фрагментами
        val transaction = act.supportFragmentManager.beginTransaction() //transaction тоже нужен для переключения между фрагментами
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out) //добавляем анимацию межде фрагментами
        // in и out - это то как повляется фрагмент и как уходит
        transaction.replace(R.id.placeHolder, newFragment) // указали куда(контейнер) передаем новый фрагмент и что за фрагмент
        transaction.commit() // применить изменения
        currentFragment = newFragment //присвоии переменной значение текущего фрагмента
    }
}