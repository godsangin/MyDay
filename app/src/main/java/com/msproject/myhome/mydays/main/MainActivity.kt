package com.msproject.myhome.mydays.main

import android.content.Intent
import android.graphics.RectF
import android.os.Bundle
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.msproject.myhome.mydays.EventActivity
import com.msproject.myhome.mydays.R
import com.msproject.myhome.mydays.application.MyApplication
import com.msproject.myhome.mydays.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.joda.time.LocalDate
import java.text.SimpleDateFormat
import javax.inject.Inject

class MainActivity :AppCompatActivity(){
    lateinit var mainViewModel: MainViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).appComponent.inject(this)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        mainViewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
        binding.model = mainViewModel
        binding.lifecycleOwner = this
        mainViewModel.initData(this)
        setPieChartTouchListener()
    }

    private fun setPieChartTouchListener(){
        piechart.setOnTouchListener(OnTouchListener { view, motionEvent ->
            val rectF: RectF = piechart.getCircleBox()
            val centerX = rectF.centerX()
            val centerY = rectF.centerY()
            val r = (rectF.right - rectF.left) / 2
            val touchX = motionEvent.x
            val touchY = motionEvent.y
            if (Math.pow((touchX - centerX).toDouble(), 2.0) + Math.pow((touchY - centerY).toDouble(), 2.0) <= Math.pow(r.toDouble(), 2.0)) {
                if (centerX > touchX && centerY > touchY) {
                    //왼쪽 위
                    addTime(18)
                } else if (centerX > touchX && centerY < touchY) {
                    //왼쪽 아래
                    addTime(12)
                } else if (centerX < touchX && centerY > touchY) {
                    //오른쪽 위
                    addTime(0)
                } else {
                    //오른쪽 아래
                    addTime(6)
                }
            }
            false
        })
    }

    private fun addTime(index: Int) {
        val intent = Intent(this@MainActivity, EventActivity::class.java)
        val format = SimpleDateFormat("yyyy-MM-dd")
        intent.putExtra("type", index)
        intent.putExtra("date", format.format(mainViewModel.date.value))
        startActivityForResult(intent, 0)
    }
}