package com.msproject.myhome.mydays.di.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/*
 *  Copyright
 *  https://github.com/spotlight21c/DaggerViewModel
 */
@Suppress("UNCHECKED_CAST")
@Singleton
class ViewModelFactory @Inject constructor(private val viewModels:MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}