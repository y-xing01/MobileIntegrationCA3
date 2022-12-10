package org.dkit.logued.serviceexample

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast

class MyService : Service() {

    // allows a client (Activity) to bind to this service.
    //
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Service","MyService... onStartCommand() called.")
        Toast.makeText(this, "MyService started - onStartCommand() called", Toast.LENGTH_LONG).show()

        doWork()

        return START_STICKY
    }

    // called when activity issues a stopService(Intent) request
    override fun onDestroy() {
        Log.d("Service","MyService... onDestroy() called.")
        super.onDestroy()
        Toast.makeText(this, "MyService destroyed - onDestroy() called", Toast.LENGTH_LONG).show()

    }

    fun doWork()
    {
//        try {
//            Thread.sleep(5000)
//        }catch (e: InterruptedException){}

        // Running a task in a separate thread
        // will prevent this Service from blocking the UI
        // in the MainActivity
        //
        val thread = object : Thread() {
            override fun run() {
                Log.i("Service", "MyService ... in doWork() - Thread Started")
                val broadcastIntent = Intent()
                try {
                    // cause this thread to sleep for 5 seconds
                    // this will simulate a long-running task
                    // e.g. download form internet, write to database etc.
                    sleep(5000)
                    val random1 = (0..100).shuffled().last()
                    var flag = false
                    for (i in 2..random1 / 2) {
                        // condition for nonprime number
                        if (random1 % i == 0) {
                            flag = true
                            break
                        }
                    }

                    if (!flag)
                        broadcastIntent.putExtra("IS_PRIME",true,)
                    else
                        broadcastIntent.putExtra("IS_PRIME",false)
                    //When finished the above long-running task
                    // - send a Broadcast to notify the MainActivity

                    //add any extras that want to be returned - in this case just a short message
                    //set user-defined action

                    broadcastIntent.action = "WORK_COMPLETE_ACTION"
                    //broadcast the intent
                    baseContext.sendBroadcast(broadcastIntent)
                    // the broadcast is delivered, by the Android system, to any
                    // registered BroadcastReceivers
                    // that have a corresponding intent-filter

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                Log.i("Service", "MyService ... in doWork() - Thread Finished")
            }
        }
        thread.start()

    }
}