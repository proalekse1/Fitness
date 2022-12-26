package com.example.fitness.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness.R
import com.example.fitness.databinding.ExerciseListItemBinding
import pl.droidsonroids.gif.GifDrawable

//DaysAdapter наследуется от ListAdapter поэтому ставим двоеточие.
class ExerciseAdapter() : ListAdapter<ExerciseModel, ExerciseAdapter.ExerciseHolder>(MyComparator()) { //MyComparator() это параметр класса но ставим круглые скобки потому что это тоже класс

    //DayHolder наследуется от ViewHolder
    class ExerciseHolder(view: View) : RecyclerView.ViewHolder(view){ //класс для заполнения элементов
       //получаем доступ к разметке одного элемента, через байндинг
        private val binding = ExerciseListItemBinding.bind(view)//ExerciseListItemBinding это название разметки exercise_list_item просто менем в байндинг название

        fun setData(exercise: ExerciseModel) = with(binding){ // сюда передаем дата класс
            tvName.text = exercise.name //помещаем упражнения в текст вью
            tvCount.text = exercise.time //колиество упражнений в текст вью
            chB.isChecked = exercise.isDone //отмечает выполненные упражнения в чекбоксе
            imEx.setImageDrawable(GifDrawable(root.context.assets, exercise.image)) //подключаем гифки
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder { //создается вью шаблон
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.exercise_list_item, parent, false)
        return ExerciseHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) { // заполняется вью шаблон
        holder.setData(getItem(position))
    }

    //DiffUtil.ItemCallback нужен чтобы не перерисовывать уже созданные элементы в списке
    class MyComparator : DiffUtil.ItemCallback<ExerciseModel>(){
        override fun areItemsTheSame(oldItem: ExerciseModel, newItem: ExerciseModel): Boolean {
            return oldItem == newItem // сравниваем целиком потому что нет уникальных элементов
        }

        override fun areContentsTheSame(oldItem: ExerciseModel, newItem: ExerciseModel): Boolean {
            return oldItem == newItem
        }
    }
}