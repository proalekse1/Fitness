package com.example.fitness.adapters

data class ExerciseModel(
    var name: String, // название упражнения из массива
    var time: String, // время выполнения
    var isDone: Boolean, // для чекбокса выполнено или нет упражнение
    var image: String // гифка
)