package com.example.smarthydroponics

//import com.google.firebase.database.ktx.database
//import com.google.firebase.database.ktx.getValue
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_ec.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

            val database = FirebaseDatabase.getInstance()
            val databaseReference = database.reference
        switchM.setOnCheckedChangeListener{ compoundButton, onSwitch ->
            if(onSwitch){
                switch1.visibility= View.VISIBLE
                switch2.visibility= View.VISIBLE
                minT.visibility= View.INVISIBLE
                maxT.visibility= View.INVISIBLE
                tOk.visibility= View.INVISIBLE
            }
            else{
                switch1.visibility= View.INVISIBLE
            switch2.visibility= View.INVISIBLE
            minT.visibility= View.VISIBLE
            maxT.visibility= View.VISIBLE
                tOk.visibility= View.VISIBLE}
        }


            val humidityTextView: TextView = findViewById(R.id.textViewH)
        val temperatureTextView: TextView = findViewById(R.id.textViewT)

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val  value = dataSnapshot.child("humidity").value

                    humidityTextView.text = " ${value.toString()} %"
                    if (value != null) {
                        prog1.progress=value.hashCode()
                    }
                    val  value1 = dataSnapshot.child("temperature").value

                    temperatureTextView.text = "  ${ value1.toString()} C°"
                    if (value1 != null) {
                        prog2.progress=value1.hashCode()
                    }




                }
            })
        switch2.setOnCheckedChangeListener{ compoundButton, onSwitch ->
            if(onSwitch)
                databaseReference.child("fan").setValue("1")
            else
                databaseReference.child("fan").setValue("0")
        }
        switch1.setOnCheckedChangeListener{ compoundButton, onSwitch ->
            if(onSwitch)
                databaseReference.child("heater").setValue("1")
            else
                databaseReference.child("heater").setValue("0")
        }
        tOk.setOnClickListener{
            databaseReference.child("minT").setValue("${minT.text}")
            databaseReference.child("maxT").setValue("${maxT.text}")
            minT.text.clear()
            maxT.text.clear()
        }


    }
}
