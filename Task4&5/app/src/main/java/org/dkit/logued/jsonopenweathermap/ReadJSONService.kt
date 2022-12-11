package org.dkit.logued.jsonopenweathermap

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

class   ReadJSONService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        doWork(intent)
        return START_STICKY
    }

    private fun doWork(intent: Intent?) {

        val urlString = intent?.getStringExtra("urlString") ?: ""
        val myThread = Thread {
            val jsonString = retrieveSiteContent(urlString)
            // intent
            val broadcastIntent = Intent()
            broadcastIntent.putExtra("jsonString", jsonString)
            broadcastIntent.action = "JSON_RETRIEVED"
            baseContext.sendBroadcast(broadcastIntent)

            //stop the service
            stopSelf()
        }
        myThread.start()
    }

    private fun retrieveSiteContent(urlString: String): String {

        var returnString = ""
        try {
            val url = URL(urlString)
            val urlConnection = url.openConnection() as HttpURLConnection
            val reader = urlConnection.inputStream.bufferedReader()
            returnString = reader.readText()
            reader.close()
            urlConnection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return returnString
    }
}
