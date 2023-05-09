package co.edu.icesi.dev.petscued.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.edu.icesi.dev.petscued.R
import kotlinx.android.synthetic.main.activity_filter_publication.*

class FilterPublicationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_publication)

        backButton.setOnClickListener{
            val intent = Intent(this, PetsPublicationActivity::class.java)
            startActivity(intent)
        }

        cleanButton.setOnClickListener{
            editTextType.text.clear()
            editTextColor.text.clear()
            editTextBreed.text.clear()
            editTextLocation.text.clear()
            editTextAge.text.clear()
        }
    }
}