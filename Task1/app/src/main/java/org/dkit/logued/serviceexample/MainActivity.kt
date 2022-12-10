package org.dkit.logued.serviceexample

/** Demonstrates Creation of a background Service that executes a simulated
 * task in a Thread (runnable). When the task completes, the thread runnable
 * generates a Broadcast.  A BroadcastReceiver is created in the MainActivity
 * and it is registered to listen for the Broadcast.  When the Broadcast is
 * received, the BroadcastReceiver simply displays a Toast.  Normally,
 * the MainActivity would retrieve data from the Service object when it is
 * notified of its availability by the broadcast.
 */
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity(), View.OnClickListener {

    // Use an Explicit (named Service) Intent to create service
    private lateinit var serviceIntent: Intent
    private lateinit var textView: TextView

    private lateinit var intentFilter : IntentFilter    // for BroadcastReceiver

    // create BroadcastReceiver
    private val intentReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("Service","BroadcastReceiver ... in onReceive() .. received a Broadcast from Service")

            val isPrime = intent?.getBooleanExtra("IS_PRIME", false)

            Toast.makeText(
                baseContext, "Work Complete! - message received: $isPrime",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serviceIntent = Intent(baseContext, MyService::class.java)

        textView = findViewById(R.id.textView)

        // Access buttons and set this Activity as listener to both buttons
        val button1: Button = findViewById(R.id.button1)
        button1.setOnClickListener(this)    // this activity listens for button presses
        val button2: Button = findViewById(R.id.button2)
        button2.setOnClickListener(this)    // this activity listens for button presses

        // create an intent filter that will filter (match) broadcasts with the
        // action called "WORK_COMPLETE_ACTION".
        // This filter is supplied when registering the BroadcastReceive
        // so only those broadcasts matching the filter are acted upon.
        intentFilter = IntentFilter()
        intentFilter.addAction("WORK_COMPLETE_ACTION") //note the same action as broadcast by the Service
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.button1 -> {
                Log.d("Service","MainActivity ...in onClick() - calling startService()")
                startService(serviceIntent)
                textView.text = "startService() requested."
            }
            R.id.button2 -> {
                Log.d("Service","MainActivity ...in onClick() - calling stopService()")
                stopService(serviceIntent)
                textView.text = "stopService() requested."
            }
            else -> {
            }      // do nothing
        }
    }

    override fun onStart() {
        super.onStart()
        // register the BroadcastReceiver (intentReceiver)
        // to receive broadcasts that match the intentFilter
        registerReceiver(intentReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(intentReceiver)
    }

    // The location of Registering and UnRegistering of BroadcastReceivers depends
    // on what service a particular Service is providing to the client.
    // i.e. do we put them in onStart(), onStop(), or onPause() etc????
    // Careful study of the documentation is required to select the correct locations
    // based on the use-case. DL

}