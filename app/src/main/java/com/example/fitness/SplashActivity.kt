package com.example.fitness

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var timer: CountDownTimer //создаем таймер

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        timer = object : CountDownTimer(3000, 1000){ //2000 (сколько) это милисекунды т.е. 2 секунды. 1000  это интервал с которым будет отсчет
            override fun onTick(p0: Long) { //метод отсчета времени с выше указанным интервалом. Здесь писать ничего не надо

            }

            override fun onFinish() { // отсчет закончился и запускаем из него следующий активити
                startActivity(Intent(this@SplashActivity, MainActivity::class.java)) //запускаем наш Активити, и на какой активити хотим перейти
            }

        }.start()//запускаем таймер

    }
    override fun onDestroy(){ //останавливаем таймер
        super.onDestroy()
        timer.cancel()
    }
}