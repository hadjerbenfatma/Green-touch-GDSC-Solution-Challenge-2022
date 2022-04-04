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


class EcActivity : AppCompatActivity() {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ec)
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

                val  value = dataSnapshot.child("EC").value
                if (value != null) {


                        textViewEC.text = " ${value.toString()}"
                    //progP.progress=value.hashCode()
                }


            }})
        switchAE.setOnCheckedChangeListener{ compoundButton, onSwitch ->
            if(onSwitch){
                switchEC.visibility= View.VISIBLE
                minE.visibility= View.INVISIBLE
                maxE.visibility= View.INVISIBLE
                EOk.visibility= View.INVISIBLE
            }
            else{
                switchEC.visibility= View.INVISIBLE
                minE.visibility= View.VISIBLE
                maxE.visibility= View.VISIBLE
                EOk.visibility= View.VISIBLE

        }
        }
        switchEC.setOnCheckedChangeListener{ compoundButton, onSwitch ->
            if(onSwitch)
                databaseReference.child("NutrientPump").setValue("1")
            else
                databaseReference.child("NutrientPump").setValue("0")
        }
        EOk.setOnClickListener{
            databaseReference.child("minE").setValue("${minE.text}")
            databaseReference.child("maxE").setValue("${maxE.text}")
            minE.text.clear()
            maxE.text.clear()
        }


    }
}
