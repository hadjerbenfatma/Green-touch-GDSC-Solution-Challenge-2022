package com.example.smarthydroponics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.smarthydroponics.fragments.HomeFragment
import com.example.smarthydroponics.fragments.SearchFragment
import com.example.smarthydroponics.fragments.ShopsFragment
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    private val homefragment= HomeFragment()
    private val shopfragment= ShopsFragment()
    private val searchfragment= SearchFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        replaceFragment(homefragment)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.homeFragment -> replaceFragment(homefragment)
                R.id.shopsFragment -> replaceFragment(shopfragment)
                R.id.searchFragment -> replaceFragment(searchfragment)
            }
            true
        }


        }



     private fun replaceFragment(fragment: Fragment){
        if(fragment !=null) {
            val transition = supportFragmentManager.beginTransaction()
            transition.replace(R.id.fragment_container, fragment)
            transition.commit()
        }
    }
}
