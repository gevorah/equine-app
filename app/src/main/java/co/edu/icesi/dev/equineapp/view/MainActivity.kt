package co.edu.icesi.dev.equineapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.model.User
import co.edu.icesi.dev.equineapp.view.home.HomeFragment
import co.edu.icesi.dev.equineapp.view.profile.ProfileFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import android.Manifest
import android.content.Intent
import co.edu.icesi.dev.equineapp.view.appointment.AppointmentFormFragment
import co.edu.icesi.dev.equineapp.view.login.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        /* if (loadUser() == null || Firebase.auth.currentUser == null || Firebase.auth.currentUser?.isEmailVerified == false) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        } else {*/
        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        val appointmentfragment = AppointmentFormFragment()

        setFragment(appointmentfragment)

        bottom_navigation?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    setFragment(homeFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    setFragment(profileFragment)
                    return@setOnItemSelectedListener true
                }
            }
            true
        }
        //}
    }

    private fun loadUser(): User? {
        val sp = getSharedPreferences("equineapp", AppCompatActivity.MODE_PRIVATE)
        val json = sp.getString("user", "NO_USER")
        if (json == "NO_USER") {
            return null
        } else {
            return Gson().fromJson(json, User::class.java)
        }
    }

    private fun badgeSetup(id: Int, alerts: Int) {
        val badge = bottom_navigation.getOrCreateBadge(id)
        badge.isVisible = true
        badge.number = alerts
    }

    private fun badgeClear(id: Int) {
        val badgeDrawable = bottom_navigation.getBadge(id)
        if (badgeDrawable != null) {
            badgeDrawable.isVisible = false
            badgeDrawable.clearNumber()
        }
    }

    private fun setFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.fl_wrapper, fragment)
        addToBackStack(null)
        commit()
    }
}