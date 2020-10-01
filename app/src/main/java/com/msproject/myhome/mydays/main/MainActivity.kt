package com.msproject.myhome.mydays.main

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.msproject.myhome.mydays.R
import com.msproject.myhome.mydays.application.MyApplication
import com.msproject.myhome.mydays.base.BaseActivity
import com.msproject.myhome.mydays.databinding.ActivityMainBinding
import com.msproject.myhome.mydays.main.challenge.ChallengeActivity
import com.msproject.myhome.mydays.main.fragment.DetailViewModel
import com.msproject.myhome.mydays.main.fragment.DetailFragment
import com.msproject.myhome.mydays.main.fragment.PlannerFragment
import com.msproject.myhome.mydays.main.fragment.PlannerViewModel
import com.msproject.myhome.mydays.main.toolbar.ToolbarViewModel
import com.msproject.myhome.mydays.utils.BackPressedForFinish
import kotlinx.android.synthetic.main.activity_main.view.*
import java.text.SimpleDateFormat
import javax.inject.Inject

class MainActivity :BaseActivity(){
    lateinit var plannerViewModel: PlannerViewModel
    lateinit var detailViewModel: DetailViewModel
    lateinit var toolbarViewModel: ToolbarViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var binding:ActivityMainBinding
    lateinit var backPressedForFinish: BackPressedForFinish

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).appComponent.inject(this)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        backPressedForFinish = BackPressedForFinish(this, getString(R.string.toast_finish_app))
        setActionBar(binding.root)
        plannerViewModel = ViewModelProviders.of(this, viewModelFactory)[PlannerViewModel::class.java]
        detailViewModel = ViewModelProviders.of(this, viewModelFactory)[DetailViewModel::class.java]
        toolbarViewModel = ViewModelProviders.of(this, viewModelFactory)[ToolbarViewModel::class.java]
        binding.model = plannerViewModel
        binding.toolbarModel = toolbarViewModel
        binding.lifecycleOwner = this
        val owner = this
        plannerViewModel.apply {
            initData(owner)
            showCalendarEvent.observe(owner, Observer {
                showDatePickerDialog()
            })
            _isPlanner.observe(owner, Observer {
                if(it){
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frame, PlannerFragment.newInstance(plannerViewModel)).commitAllowingStateLoss()
                }
                else{
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frame, DetailFragment.newInstance(detailViewModel)).commitAllowingStateLoss()
                }
            })
        }
        toolbarViewModel.drawerEvent.observe(owner, Observer {
            openDrawer()
        })
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, PlannerFragment.newInstance(plannerViewModel)).commitAllowingStateLoss()
    }


    private fun showDatePickerDialog(){
        val datePickerDialog = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            DatePickerDialog(MainActivity@this)
        } else {
            null
        }
        val datePickerCallback = object:DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                val monthText = if(month + 1 < 10) "0" + (month+1).toString() else (month+1).toString()
                val dayText = if(dayOfMonth < 10) "0" + (dayOfMonth).toString() else (dayOfMonth).toString()
                val text = "$year-$monthText-$dayText"
                val format = SimpleDateFormat("yyyy-MM-dd")
                plannerViewModel._date.postValue(format.parse(text))
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            datePickerDialog?.setOnDateSetListener(datePickerCallback)
            datePickerDialog?.show()
        }
    }

    fun setActionBar(view:View){
        setSupportActionBar(view.toolbar as Toolbar)
        view.nav_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.achievement -> {
                    val intent = Intent(MainActivity@this, ChallengeActivity::class.java)
                    startActivity(intent)
                }
                R.id.send_email -> {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.setType("plain/text")
                    val address = arrayOf(getString(R.string.developer_email_address))
                    intent.putExtra(Intent.EXTRA_EMAIL, address)
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mydays_report) + Build.MODEL + "/" + Build.VERSION.RELEASE)
                    startActivity(intent)
                }
                R.id.setting -> {

                }
            }
            true
        }
    }

    fun openDrawer(){
        binding.root.drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if(binding.root.drawer_layout.isDrawerOpen(GravityCompat.START)){
            binding.root.drawer_layout.closeDrawer(GravityCompat.START)
        }
        else{
            backPressedForFinish.onBackPressed()
        }
    }

}