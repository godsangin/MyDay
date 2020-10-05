package com.msproject.myhome.mydays.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.msproject.myhome.mydays.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.model = detailViewModel
        binding.lifecycleOwner = this
        val owner = this
        detailViewModel.activity.postValue(activity!!)
        detailViewModel.initData(owner)
        detailViewModel.fragmentManager.postValue(childFragmentManager)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(detailViewModel: DetailViewModel) =
                DetailFragment().apply {
                    this.detailViewModel = detailViewModel
                }
    }
}