package org.dkit.logued.jsonopenweathermap

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

private val urlString = "https://api.nytimes.com/svc/news/v3/content/all/all.json?api-key=6RDo4Al2U7Z4em2yuAytYXXR6Ep0k0d8"

// Use an Explicit (named Service) Intent to create service
private lateinit var serviceIntent: Intent
private lateinit var intentFilter: IntentFilter    // for BroadcastReceiver
private lateinit var resultJsonString: String
//private lateinit var item: ConstraintLayout

// References to TextViews are declared here so that their scope is throughout the whole file
// which means they can be accessed bt the BroadcastReceiver and the Activity classes.
//private lateinit var item: TextView
private lateinit var textView2: TextView
private lateinit var textView3: TextView
private lateinit var textView4: TextView

// create BroadcastReceiver
private val intentReceiver: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        // extract the JSON String from the Intent extra
        resultJsonString = intent?.getStringExtra("jsonString")
            ?: throw IllegalStateException("JSON string is null")
        object : Thread() {
            override fun run() {
                try {
                    val jsonObj =
                        JSONObject(resultJsonString)

                    val titleArray: JSONArray = jsonObj.getJSONArray("results")
                    val titleObject: JSONObject =
                        titleArray[0] as JSONObject // get the first weather object (at index position 0)
                    val titleDescription: String = titleObject.getString("title")

                    val urlArray: JSONArray = jsonObj.getJSONArray("results")
                    val urlObject: JSONObject =
                        urlArray[0] as JSONObject // get the first weather object (at index position 0)
                    val urlDescription: String = urlObject.getString("url")

                    val updated_dateArray: JSONArray = jsonObj.getJSONArray("results")
                    val updated_dateObject: JSONObject =
                        updated_dateArray[0] as JSONObject // get the first weather object (at index position 0)
                    val updated_dateDescription: String = updated_dateObject.getString("updated_date")

                   var mainHandler1 = Handler(Looper.getMainLooper());
                   var myRunnable1 = object : Thread() {
                        override fun run() {
                            textView2.text = "Title : " + titleDescription
                            textView2.setOnClickListener{
                                val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
                                builder?.setMessage(urlDescription)
                                builder?.show()
                            }
                        }

                    }
                    mainHandler1.post(myRunnable1);

                    var mainHandler2 = Handler(Looper.getMainLooper());
                    var myRunnable2 = object : Thread() {
                        override fun run() {
                            textView3.text = "URL : " + urlDescription
                            textView3.setOnClickListener{
                                val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
                                builder?.setMessage(titleDescription)
                                builder?.show()
                            }
                        }
                    }
                    mainHandler2.post(myRunnable2);



                    var mainHandler3 = Handler(Looper.getMainLooper());
                    var myRunnable3 = object : Thread() {
                        override fun run() {
                            textView4.text = "Updated Date: " + updated_dateDescription
                            textView4.setOnClickListener{
                                val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
                                builder?.setMessage(urlDescription)
                                builder?.show()
                            }
                        }
                    }
                    mainHandler3.post(myRunnable3);
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView2 = findViewById<TextView>(R.id.textView2) // cast to TextView
        textView3 = findViewById<TextView>(R.id.textView3) // cast to TextView
        textView4 = findViewById<TextView>(R.id.textView4) // cast to TextView

        val intentFilter = IntentFilter()
        intentFilter.addAction("JSON_RETRIEVED") //note the same action as broadcast by the Service
        registerReceiver(intentReceiver, intentFilter)

        startService(
            Intent(baseContext, ReadJSONService::class.java).putExtra(
                "urlString",
                urlString
            )
        )

    }
}
