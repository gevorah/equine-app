package co.edu.icesi.dev.equineapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.model.User
import co.edu.icesi.dev.equineapp.view.home.HomeFragment
import co.edu.icesi.dev.equineapp.view.profile.ProfileFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest
import android.content.Intent
import co.edu.icesi.dev.equineapp.view.appointments.AppointmentsFragment
import co.edu.icesi.dev.equineapp.view.auth.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        if (loadUser() == null || Firebase.auth.currentUser == null || Firebase.auth.currentUser?.isEmailVerified == false) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        } else {
            val homeFragment = HomeFragment()
            val profileFragment = ProfileFragment()
            val appointmentsFragment = AppointmentsFragment()

            setFragment(homeFragment)

            bottom_navigation?.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.navigation_home -> {
                        setFragment(homeFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.navigation_appointments -> {
                        setFragment(appointmentsFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.navigation_profile -> {
                        setFragment(profileFragment)
                        return@setOnItemSelectedListener true
                    }
                }
                true
            }
        }
    }

    private fun loadUser(): User? {
        val sp = getSharedPreferences("equine-app", AppCompatActivity.MODE_PRIVATE)
        val json = sp.getString("user", "NO_USER")
        return if (json == "NO_USER") {
            null
        } else {
            Gson().fromJson(json, User::class.java)
        }
    }

    private fun setFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.fl_wrapper, fragment)
        addToBackStack(null)
        commit()
    }
}