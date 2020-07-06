package com.example.kotlincoroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val IMG = "https://cdn.bulbagarden.net/upload/b/b8/025Pikachu_LG.png"
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //launching a coroutine in the scope of the main thread
        coroutineScope.launch {
            val originalDeferred = coroutineScope.async(Dispatchers.IO) {
                getOriginalBitmap()
            }

            val originalBitmap = originalDeferred.await()
            loadImage(originalBitmap)
            val filteredDeferred = coroutineScope.async(Dispatchers.Default){ applyFilter(originalBitmap) }
            val filteredBitmap = filteredDeferred.await()
            loadImage(filteredBitmap)

        }

    }

    private fun getOriginalBitmap() =

        URL(IMG).openStream().use { BitmapFactory.decodeStream(it) }

    private fun loadImage(bmp: Bitmap) {

        progressBar.visibility = View.GONE
        imageView.setImageBitmap(bmp)
        imageView.visibility = View.VISIBLE

    }

    private fun applyFilter(originalBitmap : Bitmap) = Filter.apply(originalBitmap)

}