package co.edu.icesi.dev.petscued.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.edu.icesi.dev.petscued.R
import co.edu.icesi.dev.petscued.view.home.HomePublicationActivity
import co.edu.icesi.dev.petscued.view.login.LoginActivity
import co.edu.icesi.dev.petscued.view.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_home.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}