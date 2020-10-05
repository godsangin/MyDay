package com.msproject.myhome.mydays.main.challenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.msproject.myhome.mydays.R
import com.msproject.myhome.mydays.application.MyApplication
import com.msproject.myhome.mydays.base.BaseActivity
import com.msproject.myhome.mydays.databinding.ActivityChallengeBinding
import com.msproject.myhome.mydays.main.toolbar.ToolbarViewModel
import javax.inject.Inject

class ChallengeActivity : BaseActivity() {
    lateinit var challengeViewModel: ChallengeViewModel
    lateinit var toolbarViewModel: ToolbarViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).appComponent.inject(this)
        val binding = DataBindingUtil.setContentView<ActivityChallengeBinding>(this, R.layout.activity_challenge)
        challengeViewModel = ViewModelProviders.of(this, viewModelFactory)[ChallengeViewModel::class.java]
        toolbarViewModel = ViewModelProviders.of(this, viewModelFactory)[ToolbarViewModel::class.java]
        binding.lifecycleOwner = this
        val owner = this

        binding.model = challengeViewModel
        binding.toolbarModel = toolbarViewModel
        challengeViewModel.initData(owner)
        toolbarViewModel.apply {
            titleText.postValue(getString(R.string.text_title_daily_graph))
            backButtonEvent.observe(owner, Observer {
                onBackPressed()
            })
        }
    }
}