package co.edu.icesi.dev.petscued.view.pets

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.edu.icesi.dev.petscued.R
import co.edu.icesi.dev.petscued.view.PetsPublicationActivity
import kotlinx.android.synthetic.main.activity_lostpet.*

class LostPetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lostpet)

        lostPetBttn.setOnClickListener {

            val intent = Intent(this, PetsPublicationActivity::class.java )
            startActivity(intent)
        }
    }
}