package com.shadow.navbug.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

inline fun ViewModel.io(crossinline suspender: suspend () -> Unit) {
    viewModelScope.launch(Dispatchers.IO) {
        suspender()
    }
}