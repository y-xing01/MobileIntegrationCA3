 package com.example.task6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

 class MainActivity : AppCompatActivity() {
    private lateinit var btnGet : Button
    private lateinit var img : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGet = findViewById(R.id.btn_get)
        img = findViewById(R.id.image)

        btnGet.setOnClickListener{
            val siteUrl = "https://www.travelmanagers.com.au/wp-content/uploads/2012/08/AdobeStock_254529936_Railroad-to-Denali-National-Park-Alaska_750x500.jpg"
            val queue = Volley.newRequestQueue(this)
            val imgRequest = ImageRequest(siteUrl,
                {bitmap ->
                    // handle Bitmap image
                    img.setImageBitmap(bitmap)
                },  0,  0, null, null,
                {volleyError ->
                    // handle error
                })
            // Add the request to the RequestQueue
            queue.add(imgRequest)
        }


    }
}