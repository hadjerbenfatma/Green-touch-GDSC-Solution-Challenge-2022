package com.example.smarthydroponics.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smarthydroponics.*

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*



class HomeFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnT.setOnClickListener {val intent = Intent(activity, MainActivity::class.java)
            activity?.startActivity(intent)
        }
        btnP.setOnClickListener {
            val intent = Intent(activity, PhActivity::class.java)
            activity?.startActivity(intent)
        }
        btnE.setOnClickListener {val intent = Intent(activity, EcActivity::class.java)
            activity?.startActivity(intent)
        }
        btnL.setOnClickListener {
            val intent = Intent(activity, LightActivity::class.java)
            activity?.startActivity(intent)
        }

        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.reference




        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val  value = dataSnapshot.child("humidity").value

                textViewHu.text = "Hum: ${value.toString()} %"

                val  value1 = dataSnapshot.child("temperature").value

                textViewTe.text = "${ value1.toString()} C°"
                val  value2 = dataSnapshot.child("PH").value

                textViewP.text = "PH: ${ value2.toString()} "
                val  value3 = dataSnapshot.child("EC").value

                textViewE.text = "EC: ${ value3.toString()} "
                val  value4 = dataSnapshot.child("light").value

                textViewL.text = "Light: ${ value4.toString()} "




            }
        })

    }




}


