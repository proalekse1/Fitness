package com.example.fitness.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.example.fitness.R
import com.example.fitness.adapters.ExerciseModel
import com.example.fitness.databinding.ExerciseBinding
import com.example.fitness.utils.FragmentManager
import com.example.fitness.utils.MainViewModel
import com.example.fitness.utils.TimeUtils
import pl.droidsonroids.gif.GifDrawable

class ExercisesFragment : Fragment() {
    private var timer: CountDownTimer? = null
    private lateinit var binding: ExerciseBinding //подключили байндинг
    private var exerciseCounter = 0 // переменная счетчик упражений выполненных
    private var exList: ArrayList<ExerciseModel>? = null // переменная в которую будет записываться список из ExerciseModel
    private var ab: ActionBar? = null //переменная для доступа к экшен бару
    private var currentDay = 0 //для 26 урока чтобы сохранялся чекбокс
    private val model: MainViewModel by activityViewModels() //подключили MutableModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExerciseBinding.inflate(inflater, container, false) // надули байндинг
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //создаем заполненную разметку во фрагменте
        super.onViewCreated(view, savedInstanceState)
        currentDay = model.currentDay //для 26 урока чтобы сохранялся чекбокс
        exerciseCounter = model.getExerciseCount() //возможность выхода во время тренировки и возврат на тоже упражнение
        // Log.d("MyLog", "Counter ${model.getExerciseCount(model.currentDay.toString())}") // Логирование счетчика упражнений
        ab = (activity as AppCompatActivity).supportActionBar //нициализируем экшен бар
        model.mutableListExercise.observe(viewLifecycleOwner){
            exList = it //получили список из переменной
            nextExercise() // показывает упражнение
        }
        binding.bNext.setOnClickListener { // показывает следующее упражнение по нажатию на кнопку
            nextExercise()
        }
    }

    private fun nextExercise(){ // функция которая заполнеяет из массива упражнений наш пустой массив exList
        if(exerciseCounter < exList?.size!!){ //если счетчик упражнений превысит длину массива упражнений. !! - не false
            val ex = exList?.get(exerciseCounter++) ?: return //из exList берем(get) exerciseCounter(номер элемента в массиве)
                                                            // ?: return - проверка на null нашего массива
            setExerciseType(ex)
            showExercise(ex) //передали полученное упражнение в функцию показа упражнений
            showNextExercise()
        } else{
            exerciseCounter++ // увеличиваем счетчик в 26 уроке на 1 чтобы последнее упражнение отметилось выполненным
            FragmentManager.setFragment(DayFinishFragment.newInstance(), activity as AppCompatActivity)
        }
    } 

    private fun showExercise(exercise: ExerciseModel) = with(binding){ //функция которая показывает упражнения с большой гифкой
        imMain.setImageDrawable(GifDrawable(root.context.assets, exercise.image)) // получаем картинку
        tvName.text = exercise.name // получаем название упражнения
        val title = "$exerciseCounter / ${exList?.size}" // создали стринг выражение для показа количества упражнений
        ab?.title = title //передаем в экшн бар наш title и проверяем что ab не равно null
    }

    private fun setExerciseType(exercise: ExerciseModel){ //функция котора выбирает показатьлибо время либо кол-во упражнений
        if(exercise.time.startsWith("x")){ //Если startsWith - начинается с буквы x, то показывает количество упражнений
            timer?.cancel() //останавливает обратный отсчет
            binding.tvTime.text = exercise.time
        } else {
            startTimer(exercise) // Иначе показываем счетчик обратного отсчета
        }
    }

    private fun showNextExercise() = with(binding){ // функция которая показывает следующее упраджнение
        if(exerciseCounter < exList?.size!!){ //если счетчик упражнений превысит длину массива упражнений. !! - не false
            val ex = exList?.get(exerciseCounter) ?: return //из exList берем(get) exerciseCounter(номер элемента в массиве)
            // ?: return - проверка на null нашего массива

            imNext.setImageDrawable(GifDrawable(root.context.assets, ex.image))
            setTimeType(ex)
        } else{
            imNext.setImageDrawable(GifDrawable(root.context.assets, "Molodec.gif")) //когда упражнения закончились показывает поздравление
            tvNextName.text = getString(R.string.done)
        }
    }

    private fun setTimeType(ex: ExerciseModel){
        if(ex.time.startsWith("x")){ //Если startsWith - начинается с буквы x, то показывает количество упражнений
            val name = ex.name + ": " + ex.time
            binding.tvNextName.text = name
        } else {
            val name = ex.name + ": ${TimeUtils.getTime(ex.time.toLong() * 1000)}" //передает название и время следующего упражнения
            binding.tvNextName.text = name
        }
    }

    private fun startTimer(exercise: ExerciseModel) = with(binding){//функция запуска таймера обратного отсчета
        progressBar.max = exercise.time.toInt() * 1000 //максимальное значение прогресс бара берем из массива упражнений
                                                        //* на 1000 потому что в массиве стоит 30, а мы все берем в милисикундах и останется 30 милисикунд
        //timer?.cancel() // если переключились на следующее упражнение отменяем предыдущий обратный отсчет
        timer = object : CountDownTimer(exercise.time.toLong() * 1000, 1){ //11 секунд обратного отсчета, с шагом 100
            override fun onTick(restTime: Long) {
                tvTime.text = TimeUtils.getTime(restTime)
                progressBar.progress = restTime.toInt()
            }

            override fun onFinish() { // когда заканчивается обратный отсчет переключается на новое упражнение
                nextExercise()
            }
        }.start() // запускает таймер
    }

    override fun onDetach() { //остановка таймера если нажали назад
        super.onDetach()
        model.savePref(currentDay.toString(), exerciseCounter - 1) // сохраняем данные о выполненых упражнениях
        timer?.cancel()
    }


    companion object {
        @JvmStatic
        fun newInstance() = ExercisesFragment()
    }
}