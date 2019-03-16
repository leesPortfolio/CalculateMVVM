package com.lbc.practice.calculator.view.claculator;

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.lbc.practice.calculator.R
import com.lbc.practice.calculator.R.id.tv_rx_text
import com.lbc.practice.calculator.databinding.ActivityCalculatorBinding
import com.lbc.practice.calculator.databinding.ActivityProblemSolvingBinding
import com.lbc.practice.calculator.util.CalculateManager
import com.lbc.practice.calculator.util.MusicManager
import com.lbc.practice.calculator.viewmodel.CalculatorViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class CalculatorActivity : DaggerAppCompatActivity() {

    lateinit var binding: ActivityCalculatorBinding

    var resourceids: IntArray? = IntArray(10)
    var resouceCal: Int = 0
    var start = false

    @Inject
    lateinit var music: MusicManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var calc: CalculateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_calculator)
        val viewmodel = ViewModelProviders.of(this, viewModelFactory).get(CalculatorViewModel::class.java)
        binding.let {
            it.viewmodel = viewmodel
            it.setLifecycleOwner(this)
        }

        binding.viewmodel?.text?.observe(this, Observer {
            if (it!!.length > 0) {
                when (it.toString()[it.length - 1]) {
                    '+', '-', 'X', '/' ->
                        binding.tvRxText.text = calc.calculate(it.toString().substring(0, it.length - 1))
                    '1', '2', '3', '4', '5', '6', '7', '8', '9' ->
                        music.numberSound(application, resourceids!![Integer.parseInt(it.toString()[it.length - 1].toString())], Integer.parseInt(it.toString()[it.length - 1].toString()))
                    '0' -> if (it.length > 1) {
                        music.numberSound(application, resourceids!![0], 0)
                    }
                }
            }
        })

        init()
    }


    fun init() {
        var resourceId: Int
        start =true
        for (i in 0..9) {
            resourceId = resources.getIdentifier("num$i", "raw", packageName)
            resourceids!![i] = resourceId
        }
        resouceCal = resources.getIdentifier("cal", "raw", packageName)
        music.calSongStart(application, resouceCal)
    }

    override fun onStop() {
        super.onStop()
        music.calSongStop()
    }

    override fun onResume() {
        super.onResume()
        if(start) {
            music.calSongStart(application, resouceCal)
        }
    }

}