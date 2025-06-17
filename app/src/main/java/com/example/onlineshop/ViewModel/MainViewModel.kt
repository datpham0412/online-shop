package com.example.onlineshop.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.onlineshop.Model.CategoryModel
import com.example.onlineshop.Model.ItemsModel
import com.example.onlineshop.Model.SliderModel
import com.example.onlineshop.Repository.MainRepository

class MainViewModel():ViewModel() {
    private val repository=MainRepository()
    fun loadBanner():LiveData<MutableList<SliderModel>>{
        return repository.loadBanner()
    }
    fun loadCategory():LiveData<MutableList<CategoryModel>>{
        return repository.loadCategory()
    }

    fun loadPopular() : LiveData<MutableList<ItemsModel>>{
        return repository.loadPopular()
    }

    fun loadFiltered(id:String) : LiveData<MutableList<ItemsModel>>{
        return repository.loadFiltered(id)
    }

}