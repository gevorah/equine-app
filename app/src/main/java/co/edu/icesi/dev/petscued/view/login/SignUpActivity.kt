package co.edu.icesi.dev.petscued.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import co.edu.icesi.dev.petscued.R
import co.edu.icesi.dev.petscued.databinding.ActivitySignUpBinding
import co.edu.icesi.dev.petscued.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.top_nav.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        back_title.setText(R.string.action_sign_up)
        back_btn.setOnClickListener {
            finish()
        }

        binding.signUpBtn.setOnClickListener(::register)

    }

    private fun register(view: View) {
        Firebase.auth.createUserWithEmailAndPassword(
            binding.emailFld.text.toString(),
            binding.passwordFld.text.toString()
            ).addOnSuccessListener {

            val id = Firebase.auth.currentUser?.uid
                val user = User(id!!, binding.nameFld.text.toString(),
                    binding.emailFld.text.toString())

            Firebase.firestore.collection("users").document(id).set(user).addOnSuccessListener{
                sendVerificationEmail()
                finish()
            }
        }.addOnFailureListener{
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
        }.addOnSuccessListener {
            val intent = Intent(this, SignInActivity::class.java )
            startActivity(intent)
        }
    }

    private fun sendVerificationEmail() {
        Firebase.auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
            Toast.makeText(this,"Verifique su email antes de entrar", Toast.LENGTH_LONG).show()
        }?.addOnFailureListener {
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
        }
    }

}
