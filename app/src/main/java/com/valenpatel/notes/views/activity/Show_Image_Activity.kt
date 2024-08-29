package com.valenpatel.notes.views.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.valenpatel.notes.R

class Show_Image_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

    var a = intent.getStringExtra("image1")

        val b = intent.getStringExtra("image2")
        val c = intent.getStringExtra("image3")
        val d = intent.getStringExtra("image4")
//        var  imageView : ImageView = findViewById(R.id.touchImgShowImage)
//
//        var image = dataClass.image1
//        Log.d("hello", "onCreate: "+image)
//        Toast.makeText(this, ""+image, Toast.LENGTH_SHORT).show()
        if (a!=null){
            Glide.with(this)
                .asBitmap()
                .load(a)
                .placeholder(R.drawable.baseline_image_24)
//            .error(R.drawable.ic_launcher_background)
                .into(findViewById<ImageView>(R.id.touchImgShowImage))
        }
        else if (b!=null){
            Glide.with(this)
                .asBitmap()
                .load(b)
                .placeholder(R.drawable.baseline_image_24)
//            .error(R.drawable.ic_launcher_background)
                .into(findViewById<ImageView>(R.id.touchImgShowImage))
        }else if (c!=null){
            Glide.with(this)
                .asBitmap()
                .load(c)
                .placeholder(R.drawable.baseline_image_24)
//            .error(R.drawable.ic_launcher_background)
                .into(findViewById<ImageView>(R.id.touchImgShowImage))
        }else {
            Glide.with(this)
                .asBitmap()
                .load(d)
                .placeholder(R.drawable.baseline_image_24)
//            .error(R.drawable.ic_launcher_background)
                .into(findViewById<ImageView>(R.id.touchImgShowImage))
        }
    }
}