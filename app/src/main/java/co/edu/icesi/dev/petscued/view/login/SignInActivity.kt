package co.edu.icesi.dev.petscued.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import co.edu.icesi.dev.petscued.R
import co.edu.icesi.dev.petscued.databinding.ActivitySignInBinding
import co.edu.icesi.dev.petscued.model.User
import co.edu.icesi.dev.petscued.view.home.HomePublicationActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.top_nav.*

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        back_title.setText(R.string.action_sign_in)
        back_btn.setOnClickListener {
            finish()
        }

        binding.signInBtn.setOnClickListener{
            val email = binding.emailFld.text.toString()
            val password = binding.passwordFld.text.toString()
            Firebase.auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                val fbuser = Firebase.auth.currentUser
                if(fbuser!!.isEmailVerified){
                   Firebase.firestore.collection("users").document(fbuser.uid).get().addOnSuccessListener {
                       val user = it.toObject(User::class.java)
                       saveUser(user!!)
                       startActivity(Intent(this, HomePublicationActivity::class.java))
                       finish()
                   }
                } else{
                    Toast.makeText(this, "Su email no ha sido verificado", Toast.LENGTH_LONG).show()
                }

            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        }

        binding.lostPasswordTxt.setOnClickListener {
            Firebase.auth.sendPasswordResetEmail(binding.emailFld?.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this, "Revise su correo "+binding.emailFld?.text.toString(), Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun saveUser ( user: User){
        val sp = getSharedPreferences("pets-cued", MODE_PRIVATE)
        val json = Gson().toJson(user)
        sp.edit().putString("user", json).apply()
    }
}