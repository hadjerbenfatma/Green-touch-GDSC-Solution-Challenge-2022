package com.example.smarthydroponics

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_ec.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_ph.*

class PhActivity : AppCompatActivity() {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ph)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.reference
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
        override fun onDataChange(dataSnapshot: DataSnapshot) {

            val  value = dataSnapshot.child("PH").value
            if (value != null) {
                if(value.hashCode() < 7)

                    textViewPh.text = " ${value.toString()}\n acidic water "
                    else if(value.hashCode()==7)
                    textViewPh.text = " ${value.toString()}\n neutral water "
                else
                    textViewPh.text = " ${value.toString()}\n alkaline water "
                progP.progress=value.hashCode()
            }


            }})
        switchAP.setOnCheckedChangeListener{ compoundButton, onSwitch ->
            if(onSwitch){
                switchAc.visibility=View.VISIBLE
                switchAl.visibility=View.VISIBLE
                minP.visibility=View.INVISIBLE
                maxP.visibility=View.INVISIBLE
                POk.visibility=View.INVISIBLE
            }
            else{
                switchAc.visibility=View.INVISIBLE
                switchAl.visibility=View.INVISIBLE
                minP.visibility=View.VISIBLE
                maxP.visibility=View.VISIBLE
                POk.visibility=View.VISIBLE}
        }
        switchAc.setOnCheckedChangeListener{ compoundButton, onSwitch ->
            if(onSwitch)
                databaseReference.child("acide").setValue("1")
            else
                databaseReference.child("acide").setValue("0")
        }
        switchAl.setOnCheckedChangeListener{ compoundButton, onSwitch ->
            if(onSwitch)
                databaseReference.child("alkaline").setValue("1")
            else
                databaseReference.child("alkaline").setValue("0")
        }
        POk.setOnClickListener{
            databaseReference.child("minP").setValue("${minP.text}")
            databaseReference.child("maxP").setValue("${maxP.text}")
            minP.text.clear()
            maxP.text.clear()
        }


    }
}
