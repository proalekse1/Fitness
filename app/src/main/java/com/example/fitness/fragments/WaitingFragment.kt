package com.example.fitness.fragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.fitness.R
import com.example.fitness.databinding.WaitingFragmentBinding
import com.example.fitness.utils.FragmentManager
import com.example.fitness.utils.TimeUtils


const val COUNT_DOWN_TIME = 8000L //11000 это 11 секунд, L это лонг иначе ругается

class WaitingFragment : Fragment() {

    private lateinit var binding: WaitingFragmentBinding //подключили байндинг к разметке
    private lateinit var timer: CountDownTimer //таймер обратного отсчета
    private var ab: ActionBar? = null //переменная для доступа к экшен бару

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WaitingFragmentBinding.inflate(inflater, container, false) // надули байндинг
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //создаем заполненную разметку во фрагменте
        super.onViewCreated(view, savedInstanceState)
        ab = (activity as AppCompatActivity).supportActionBar //нициализируем экшен бар
        ab?.title = getString(R.string.waiting) //передаем стринг в экшн бар
        binding.pBar.max = COUNT_DOWN_TIME.toInt() //максимальное значение прогресс бара 11000.
                                                    // Превращаем счетчик в инт т.к. прогресс бар принимает только инт
        startTimer() //запускаем таймер
    }

    private fun startTimer() = with(binding){//функция запуска таймера обратного отсчета
        timer = object : CountDownTimer(COUNT_DOWN_TIME, 1){ //11 секунд обратного отсчета, с шагом 100
            override fun onTick(restTime: Long) {
                tvTimer.text = TimeUtils.getTime(restTime)
                pBar.progress = restTime.toInt()
            }

            override fun onFinish() {
            //можно запустить для проверки Toast.makeText(activity, "Готово", Toast.LENGTH_LONG).show() //тост после окончания обратного отсчета
                FragmentManager.setFragment(ExercisesFragment.newInstance(), activity as AppCompatActivity) //запускаем фрагмент с большой гифкой
            }
        }.start() // запускает таймер

    }

    override fun onDetach() {
        super.onDetach()
        timer.cancel()
    }


    companion object {
        @JvmStatic
        fun newInstance() = WaitingFragment()
    }
}