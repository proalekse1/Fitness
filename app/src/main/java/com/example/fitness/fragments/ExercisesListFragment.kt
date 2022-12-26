package com.example.fitness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitness.R
import com.example.fitness.adapters.ExerciseAdapter
import com.example.fitness.databinding.ExercisesListFragmentBinding
import com.example.fitness.utils.FragmentManager
import com.example.fitness.utils.MainViewModel

class ExercisesListFragment : Fragment() {

    private lateinit var binding: ExercisesListFragmentBinding //подключили байндинг
    private lateinit var adapter: ExerciseAdapter //подключили адаптер
    private var ab: ActionBar? = null //переменная для доступа к экшен бару
    private val model: MainViewModel by activityViewModels() //подключили MutableModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExercisesListFragmentBinding.inflate(inflater, container, false) // надули байндинг
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //создаем заполненную разметку во фрагменте
        super.onViewCreated(view, savedInstanceState)
        init() //запускаем с адаптером разметкой и т.д.
        model.mutableListExercise.observe(viewLifecycleOwner){
            for (i in 0 until model.getExerciseCount()) { //цикл для отмечания чекбокса выполненых упражнений
                it[i] = it[i].copy(isDone = true)
            }
            adapter.submitList(it)
        }
    }

    private fun init() = with(binding){ //Инициализировали адаптер
        ab = (activity as AppCompatActivity).supportActionBar //нициализируем экшен бар
        ab?.title = getString(R.string.exercises) //заполняем экшн бар из стринга
        adapter = ExerciseAdapter()
        rcView.layoutManager = LinearLayoutManager(activity) //инициализировали разметку recyclerView и сказали что список будет идти по вертикали
        rcView.adapter = adapter
        bStart.setOnClickListener { //слушатель нажатий на кнопку в этом фрагменте
            FragmentManager.setFragment(WaitingFragment.newInstance(), activity as AppCompatActivity) //переключаемся на фрагмент с обратным отсчетом
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExercisesListFragment()
    }
}