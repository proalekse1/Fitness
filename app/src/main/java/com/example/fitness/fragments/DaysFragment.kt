package com.example.fitness.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitness.R
import com.example.fitness.adapters.DayModel
import com.example.fitness.adapters.DaysAdapter
import com.example.fitness.adapters.ExerciseModel
import com.example.fitness.databinding.FragmentDaysBinding
import com.example.fitness.utils.DialogManager
import com.example.fitness.utils.FragmentManager
import com.example.fitness.utils.MainViewModel


class DaysFragment : Fragment(), DaysAdapter.Listener { // DaysAdapter.Listener подключенный интерфейс
    private lateinit var adapter: DaysAdapter //28урок проиниц-ли на уровне класса чтобы получить доступ и другим функциям
    private lateinit var binding: FragmentDaysBinding //подключили байндинг
    private val model: MainViewModel by activityViewModels() //подключили MutableModel
    private var ab: ActionBar? = null //переменная для доступа к экшен бару

    override fun onCreate(savedInstanceState: Bundle?) { // 28 урок включаем меню сброса дней, обязательно на фрагменте
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDaysBinding.inflate(inflater, container, false) // надули байндинг
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //создаем заполненную разметку во фрагменте
        super.onViewCreated(view, savedInstanceState)
        model.currentDay = 0 //для 26 урока чтобы чекбокс отмечался
        initRcView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) { //28 урок меню для сброса
        return inflater.inflate(R.menu.main_menu, menu) //надули меню для сброса
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // 28 урок слушатель нажатий для меню сброс
        if(item.itemId == R.id.reset) {
            DialogManager.showDialog( // 29урок покажем диалог и слушатель
                activity as AppCompatActivity,
                R.string.reset_days_message,
                object : DialogManager.Listener{ //интерфейс
                    override fun onClick() { //слушатель
                        model.pref?.edit()?.clear()?.apply() //стираем выполненные упражнения в pref
                        adapter.submitList(fillDaysArray()) //заполняем адаптер из массива и передаем созданную функцию заполнения
                    }
                }
            )

        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRcView() = with(binding){
        adapter = DaysAdapter(this@DaysFragment) // создали экземпляр класса(инициализировали адаптер)
        ab = (activity as AppCompatActivity).supportActionBar //нициализируем экшен бар
        ab?.title = getString(R.string.days) //заполняем экшн бар из стринга
        rcViewDays.layoutManager = LinearLayoutManager(activity as AppCompatActivity) // указываем разметку для заполнения фрагмента по вертикали
        rcViewDays.adapter = adapter // прикреплям к фрагменту адаптер для заполнения элементов
        adapter.submitList(fillDaysArray()) //заполняем адаптер из массива и передаем созданную функцию заполнения
    }

    private fun fillDaysArray(): ArrayList<DayModel>{ //создаем список и заполняем его из DayModel
        val tArray = ArrayList<DayModel>() //создаем экземпляр tArray из класса ArrayList<DayModel>()
        var daysDoneCounter = 0 //27 урок счетчик выполненных дней для прогресс бара
        resources.getStringArray(R.array.day_exercises).forEach { // связали дата класс с массивом
            model.currentDay++ //увеличиваем количество дней на 1
            val exCounter = it.split(",").size //берем размер строки массива с упражнениями
            tArray.add(DayModel(it, 0, model.getExerciseCount() == exCounter)) //заполняем DayModel из массива упражнений, потом daysAdapter заполняет им список
        }
        binding.pB.max = tArray.size //максимальное значение прогресс бара равно размеру массива
        tArray.forEach {
            if(it.isDone) daysDoneCounter++ // считаем сколько дней выполнено
        }
        updateRestDaysUI(tArray.size - daysDoneCounter, tArray.size)//для прогресс бара общее количество минус выполненные дни
        return tArray // возвращаем список
    }

    private fun updateRestDaysUI(restDays: Int, days: Int) = with(binding){ //функция прогресс бара
        val rDays = getString(R.string.rest) + " $restDays " + getString(R.string.rest_days) //собираем стринг названия над прогресс баром
        tvRestDays.text = rDays //теперь передаем в текст вью
        pB.progress = days - restDays //кол-во дней минус оставшиеся дни 20-17, получиться например 3 на три и шагнет прогресс бар
    }


    private fun fillExerciseList(day: DayModel){ //функция заполнения нового дата класса из массива упраженний
        val tempList = ArrayList<ExerciseModel>()//создаем временный список
        day.exercises.split(",").forEach { //функция которая выдаст массив без запятой для каждого элемента
            val exerciseList = resources.getStringArray(R.array.exercise) //бращаемся к первому массиву
            val exercise = exerciseList[it.toInt()] //превращаем стринг в инт
            val exerciseArray = exercise.split("|")// разделяем в первом массиве элементы палочкой
            tempList.add(ExerciseModel(exerciseArray[0], exerciseArray[1], false, exerciseArray[2])) //передаем во временный лист позиции массива
        }
        model.mutableListExercise.value = tempList //записываем значение временного списка в mutablelist.

    }

    companion object {
        @JvmStatic
        fun newInstance() = DaysFragment() //инициализируем наш фрагмент
    }

    override fun onClick(day: DayModel) { //Функция интерфейса DaysAdapter.Listener
        if(!day.isDone) { //29 урок если день не выполнен запускаем как обычно
            fillExerciseList(day)
            model.currentDay = day.dayNumber // записываем номер дня
            FragmentManager.setFragment(
                ExercisesListFragment.newInstance(),
                activity as AppCompatActivity)
        } else{ //29 урок если день выполнен стираем значение выполнен
            DialogManager.showDialog( // 29урок покажем диалог и слушатель
                activity as AppCompatActivity,
                R.string.reset_day_message,
                object : DialogManager.Listener{ //интерфейс
                    override fun onClick() { //слушатель
                        model.savePref(day.dayNumber.toString(), 0) //перезаписываем в дне isDone
                        fillExerciseList(day) //теперь просто заполняем день невыполненными упражнениями
                        model.currentDay = day.dayNumber // записываем номер дня
                        FragmentManager.setFragment(
                            ExercisesListFragment.newInstance(),
                            activity as AppCompatActivity)
                    }
                }
            )

        }

    }
}

















