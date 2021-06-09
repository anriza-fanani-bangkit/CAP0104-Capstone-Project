package com.example.angkoot.ui.ordering

import androidx.lifecycle.ViewModel
import com.example.angkoot.data.AngkootRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderingViewModel @Inject constructor(
    private val repository: AngkootRepository
) : ViewModel() {
    fun SearchPlaces() {}
}