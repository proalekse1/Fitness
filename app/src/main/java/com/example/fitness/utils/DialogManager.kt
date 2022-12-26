package com.example.fitness.utils

import android.app.AlertDialog
import android.content.Context
import com.example.fitness.R

object DialogManager { //класс для диалога
    fun showDialog(context: Context, mId: Int, listener: Listener){
        val builder = AlertDialog.Builder(context) //переменная для вывода диалога
        var dialog: AlertDialog? = null
        builder.setTitle(R.string.alert) // заголовок сообщения
        builder.setMessage(mId) //тут мы можем выбрать диалог
        builder.setPositiveButton(R.string.reset){ _,_ -> //позитивная кнопка сбросить
            listener.onClick() //слушатель
            dialog?.dismiss()
        }
        builder.setNegativeButton(R.string.back){ _,_ -> // негативная кнопка отмена
            dialog?.dismiss()
        }
        dialog = builder.create()
        dialog.show()
}

interface Listener{ //нтерфейс для слушателя нажатий
    fun onClick()
  }
}