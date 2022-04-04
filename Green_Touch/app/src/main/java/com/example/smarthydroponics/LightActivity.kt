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
import kotlinx.android.synthetic.main.activity_light.*


class LightActivity : AppCompatActivity() {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light)
        //supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
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

                val  value = dataSnapshot.child("Light").value
                if (value != null) {


                    textViewLight.text = " ${value.toString()}"
                   // progP.progress=value.hashCode()
                }


            }})
        switchAL.setOnCheckedChangeListener{ compoundButton, onSwitch ->
            if(onSwitch){
                databaseReference.child("Lauto").setValue("0")
                switchL.visibility= View.VISIBLE
                minL.visibility= View.INVISIBLE
                maxL.visibility= View.INVISIBLE
                LOk.visibility= View.INVISIBLE
            }
            else{
                databaseReference.child("Lauto").setValue("1")
                switchL.visibility= View.INVISIBLE
                minL.visibility= View.VISIBLE
                maxL.visibility= View.VISIBLE
                LOk.visibility= View.VISIBLE}
        }
        switchL.setOnCheckedChangeListener{ compoundButton, onSwitch ->
            if(onSwitch)
                databaseReference.child("Lamp").setValue("1")
            else
                databaseReference.child("Lamp").setValue("0")
        }
        LOk.setOnClickListener{
            databaseReference.child("minL").setValue("${minL.text}")
            databaseReference.child("maxL").setValue("${maxL.text}")
            minL.text.clear()
            maxL.text.clear()
        }


    }
}
