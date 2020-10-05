package com.msproject.myhome.mydays.main.fragment

import android.content.Intent
import android.graphics.RectF
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.msproject.myhome.mydays.databinding.FragmentPlannerBinding
import com.msproject.myhome.mydays.main.event.EventActivity
import kotlinx.android.synthetic.main.fragment_planner.*
import kotlinx.android.synthetic.main.fragment_planner.view.*
import kotlinx.android.synthetic.main.item_plan_time.view.*
import java.text.SimpleDateFormat
import javax.inject.Inject

class PlannerFragment : Fragment() {
    lateinit var plannerViewModel: PlannerViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentPlannerBinding.inflate(inflater)
        binding.model = plannerViewModel
        binding.lifecycleOwner = this
        val owner = this
        plannerViewModel.apply {
            initData(owner)
        }
        setPieChartTouchListener(binding.root)
        return binding.root
    }

    private fun setPieChartTouchListener(view:View){
        view.piechart.setOnTouchListener { view, motionEvent ->
            if(motionEvent.action == MotionEvent.ACTION_UP) {
                val rectF: RectF = piechart.getCircleBox()
                val centerX = rectF.centerX()
                val centerY = rectF.centerY()
                val r = (rectF.right - rectF.left) / 2
                val touchX = motionEvent.x
                val touchY = motionEvent.y
                if (Math.pow((touchX - centerX).toDouble(), 2.0) + Math.pow((touchY - centerY).toDouble(), 2.0) <= Math.pow(r.toDouble(), 2.0)) {
                    if (centerX > touchX && centerY > touchY) {
                        //왼쪽 위
                        addTime(view, 18)
                    } else if (centerX > touchX && centerY < touchY) {
                        //왼쪽 아래
                        addTime(view, 12)
                    } else if (centerX < touchX && centerY > touchY) {
                        //오른쪽 위
                        addTime(view, 0)
                    } else {
                        //오른쪽 아래
                        addTime(view, 6)
                    }
                }
            }
            true
        }
    }

    private fun addTime(view:View, index: Int) {
        val intent = Intent(context, EventActivity::class.java)
        val format = SimpleDateFormat("yyyy-MM-dd")
        intent.putExtra("type", index)
        intent.putExtra("date", format.format(plannerViewModel.date.value))
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, view, view.transitionName)
        startActivity(intent, optionsCompat.toBundle())
//        startActivityForResult(intent, 0)
    }


    companion object {
        @JvmStatic
        fun newInstance(plannerViewModel: PlannerViewModel) =
                PlannerFragment().apply {
                    this.plannerViewModel = plannerViewModel
                }
    }
}