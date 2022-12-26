package com.example.fitness.adapters

data class DayModel(
    var exercises: String, // переменная для упражнений из массива
    var dayNumber: Int, // переменная для 24 урока в которой передается во фрагмент со списком упражнений какой сейчас день
    var isDone: Boolean // переменная для чекбокса
)
