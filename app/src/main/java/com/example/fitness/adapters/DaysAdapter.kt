package com.example.fitness.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness.R
import com.example.fitness.databinding.DaysListItemBinding

//DaysAdapter наследуется от ListAdapter поэтому ставим двоеточие.
class DaysAdapter(var listener: Listener) : ListAdapter<DayModel, DaysAdapter.DayHolder>(MyComparator()) { //MyComparator() это параметр класса но ставим круглые скобки потому что это тоже класс

    //DayHolder наследуется от ViewHolder
    class DayHolder(view: View) : RecyclerView.ViewHolder(view){ //класс для заполнения элементов
       //получаем доступ к разметке одного элемента, через байндинг
        private val binding = DaysListItemBinding.bind(view)//DaysListItemBinding это название разметки days_list_item просто менем в байндинг название

        fun setData(day: DayModel, listener: Listener) = with(binding){ // сюда передаем дата класс
            val name = root.context.getString(R.string.day) + " ${adapterPosition + 1}" //берем day из string и прибавляем по 1 дню
            tvName.text = name //помещаем дни в текст вью

            val exCounter = root.context.getString(R.string.exercise) + ":" +
                    day.exercises.split(",").size.toString()//счетчик упражнений. split разделяет строку на массив

            tvExCounter.text = exCounter//помещаем упражнения в текст вью
            checkBox.isChecked = day.isDone //для заполнения чекбокса
            itemView.setOnClickListener{ listener.onClick(day.copy(dayNumber = adapterPosition + 1)) } //слушатель нажатий для переключения во второй фрагмент
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder { //создается вью шаблон
        val view = LayoutInflater.from(parent.context). //берем инфлейтер из активити поэтому надо указать контекст
        inflate(R.layout.days_list_item, parent, false) //надуваем разметку одного элемента

        return DayHolder(view)
    }

    override fun onBindViewHolder(holder: DayHolder, position: Int) { // заполняется вью шаблон
        holder.setData(getItem(position), listener) // подключаем интерфейс к каждому элементу списка
    }

    //DiffUtil.ItemCallback нужен чтобы не перерисовывать уже созданные элементы в списке
    class MyComparator : DiffUtil.ItemCallback<DayModel>(){
        override fun areItemsTheSame(oldItem: DayModel, newItem: DayModel): Boolean {
            return oldItem == newItem // сравниваем целиком потому что нет уникальных элементов
        }

        override fun areContentsTheSame(oldItem: DayModel, newItem: DayModel): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener{  //Интерфейс для связи адаптера и дата класса DayModel
        fun onClick(day: DayModel)
    }

}