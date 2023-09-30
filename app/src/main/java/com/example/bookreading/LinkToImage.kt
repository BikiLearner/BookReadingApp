package com.example.travelinindia.activities.impClasses

import android.graphics.BitmapFactory
import android.os.Looper
import android.widget.ImageView
import java.util.concurrent.Executors

class LinkToImage {
    open fun loadImg(url:String,img: ImageView){
        val executor= Executors.newSingleThreadExecutor()
        val handler= android.os.Handler(Looper.getMainLooper())

        executor.execute{
            try {
                val `in`=java.net.URL(url).openStream()
                val image= BitmapFactory.decodeStream(`in`)
                handler.post{
                    img.setImageBitmap(image)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}