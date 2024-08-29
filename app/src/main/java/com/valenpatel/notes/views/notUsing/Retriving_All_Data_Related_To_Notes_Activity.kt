package com.valenpatel.notes.views.notUsing

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.valenpatel.notes.R
import com.valenpatel.notes.views.activity.Show_Image_Activity
import com.valenpatel.notes.model.DataClass

class Retriving_All_Data_Related_To_Notes_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retriving_all_data_related_to_notes)


        findViewById<ImageView>(R.id.imageViewRetrieveAll).setOnClickListener {
            finish()
        }
        var dataClass: DataClass = intent.getSerializableExtra("value") as DataClass

        findViewById<ImageView>(R.id.shareImageRetrieveAll).setOnClickListener {
            var shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"

            var a = StringBuilder()
            a.append("title : " + dataClass.title)
            a.append("\n\nDescription :" + dataClass.description)
            a.append("\n\nPrice : " + dataClass.price)
            a.append("\n\nQuantity : " + dataClass.quantity)
            a.append("\n\nMore : " + dataClass.more)
            a.append("\n\nDate : " + dataClass.date)
            a.append("\n\nTime : " + dataClass.time)
            a.append("\n\nClick on the Link given below to open Image if provided by the User")
            a.append("\n\nImage 1 : " + dataClass.image1)
            a.append("\n\nImage 2 : " + dataClass.image2)
            a.append("\n\nImage 3 : " + dataClass.image3)
            a.append("\n\nImage 4 : " + dataClass.image4)

            shareIntent.putExtra(Intent.EXTRA_TEXT, "" + a)
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }


        findViewById<TextView>(R.id.textViewEditRetrieve).setOnClickListener {
            val intent = Intent(this, Update_Data_Activity::class.java)
            intent.putExtra("user", dataClass)
            startActivity(intent)

        }
        findViewById<TextView>(R.id.titleRetrieveAll).text = dataClass.title
        findViewById<TextView>(R.id.descriptionRetrieveAll).text = dataClass.description
        findViewById<TextView>(R.id.priceRetrieveAll).text = dataClass.price
        findViewById<TextView>(R.id.quantityRetrieveAll).text = dataClass.quantity
        findViewById<TextView>(R.id.moreRetrieveAll).text = dataClass.more
        findViewById<TextView>(R.id.dateRetrieveAll).text = dataClass.date
        findViewById<TextView>(R.id.timeRetrieveAll).text = dataClass.time

        findViewById<ImageView>(R.id.imageRetrieveAll).setOnClickListener {
            var intent = Intent(this, Show_Image_Activity::class.java)
            intent.putExtra("image1", dataClass.image1)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.image2RetrieveAll).setOnClickListener {
            var intent = Intent(this, Show_Image_Activity::class.java)
            intent.putExtra("image2", dataClass.image2)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.image3RetrieveAll).setOnClickListener {
            var intent = Intent(this, Show_Image_Activity::class.java)
            intent.putExtra("image3", dataClass.image3)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.image4RetrieveAll).setOnClickListener {
            var intent = Intent(this, Show_Image_Activity::class.java)
            intent.putExtra("image4", dataClass.image4)
            startActivity(intent)
        }



        Glide.with(this@Retriving_All_Data_Related_To_Notes_Activity)
            .asBitmap()
            .load(dataClass.image1)
            .placeholder(R.drawable.baseline_image_24_black)
//            .error(R.drawable.ic_launcher_background)
            .into(findViewById<ImageView>(R.id.imageRetrieveAll))

        Glide.with(this@Retriving_All_Data_Related_To_Notes_Activity)
            .asBitmap()
            .load(dataClass.image2)
            .placeholder(R.drawable.baseline_image_24_black)
            .into(findViewById<ImageView>(R.id.image2RetrieveAll))

        Glide.with(this@Retriving_All_Data_Related_To_Notes_Activity)
            .asBitmap()
            .load(dataClass.image3)
            .placeholder(R.drawable.baseline_image_24_black)
            .into(findViewById<ImageView>(R.id.image3RetrieveAll))

        Glide.with(this@Retriving_All_Data_Related_To_Notes_Activity)
            .asBitmap()
            .load(dataClass.image4)
            .placeholder(R.drawable.baseline_image_24_black)
            .into(findViewById<ImageView>(R.id.image4RetrieveAll))

    }
}




