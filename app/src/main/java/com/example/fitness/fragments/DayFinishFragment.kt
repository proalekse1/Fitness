package com.example.fitness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.fitness.R
import com.example.fitness.databinding.DayFinishBinding
import com.example.fitness.utils.FragmentManager
import pl.droidsonroids.gif.GifDrawable


class DayFinishFragment : Fragment() {

    private lateinit var binding: DayFinishBinding //подключили байндинг к разметке
    private var ab: ActionBar? = null //переменная для доступа к экшен бару

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DayFinishBinding.inflate(inflater, container, false) // надули байндинг
        // Inflate the layout for this fragment
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //создаем заполненную разметку во фрагменте
        super.onViewCreated(view, savedInstanceState)
        ab = (activity as AppCompatActivity).supportActionBar //нициализируем экшен бар
        ab?.title = getString(R.string.molodec) //передаем стринг в экшн бар
        binding.imMain.setImageDrawable(GifDrawable((activity as AppCompatActivity).assets, "Molodec.gif")) //когда упражнения закончились показывает поздравление
        binding.bDone.setOnClickListener { //слушатель нажатий
            FragmentManager.setFragment(DaysFragment.newInstance(), activity as AppCompatActivity)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DayFinishFragment()
    }
}