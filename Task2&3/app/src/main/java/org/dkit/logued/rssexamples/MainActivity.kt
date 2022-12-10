package org.dkit.logued.rssexamples

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Use an Explicit (named Service) Intent to create service
private lateinit var serviceIntent: Intent
private lateinit var intentFilter: IntentFilter    // for BroadcastReceiver

private lateinit var recyclerView: RecyclerView
private lateinit var resultRssList: ArrayList<RssItem>


// create BroadcastReceiver
private val intentReceiver: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("RSS", "BroadcastReceiver ... in onReceive()")

        // extract the list of RssItem objects from Intent
        val tempList: ArrayList<RssItem> =
            intent?.getParcelableArrayListExtra("RssItemsList") ?:  // Elvis operator ?:
                    throw IllegalStateException("RssItem ArrayList is null")

        Log.d("RSS", "BroadcastReceiver ... onReceive(), output list of RssItems:")

        // Iterate through the list of RssItem objects and print out their details
        // Log output is formatted here for readability in the LogCat pane.
        for (item in tempList) {
            resultRssList.add(RssItem(item.title, item.description, item.pubDate, item.copyright))
            Log.d(
                "RSS", "\nRssItem\nTitle: ${item.title}\ndescription: " +
                        "${item.description}\npubDate: ${item.pubDate}\ncopyright:${item.copyright}\n"
            )
        }

        recyclerView.adapter?.notifyDataSetChanged()
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("RSS", "in onCreate()")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.title = "RSS results"

        resultRssList = arrayListOf()
        val rssItemAdapter = RssItemAdapter(resultRssList)

        recyclerView = findViewById(R.id.newsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = rssItemAdapter

        serviceIntent = Intent(baseContext, RetrieveFeedService::class.java)

        // create intent filter for BroadcastReceiver
        intentFilter = IntentFilter()
        intentFilter.addAction("WORK_COMPLETE_ACTION") //note the same action as broadcast by the Service

        val btnStart: Button = findViewById(R.id.btn_start)
        btnStart.setOnClickListener {

        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(intentReceiver,intentFilter);
        startService(serviceIntent)
    }

    override fun onStop() {
        super.onStop()
        stopService(serviceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(intentReceiver);
    }
}