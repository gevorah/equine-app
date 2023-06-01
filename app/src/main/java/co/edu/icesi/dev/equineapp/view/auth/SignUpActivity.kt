package co.edu.icesi.dev.equineapp.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.databinding.ActivitySignUpBinding
import co.edu.icesi.dev.equineapp.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.top_nav.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        back_title.setText(R.string.action_sign_up)
        back_btn.setOnClickListener {
            finish()
        }

        binding.singUpBtn.setOnClickListener(::register)
    }

    private fun register(view: View) {
        if(!checkIfNullOrBlankOrEmpty(binding.emailFld.text.toString()) &&
            !checkIfNullOrBlankOrEmpty(binding.passwordFld.text.toString()) &&
            !checkIfNullOrBlankOrEmpty(binding.nameFld.text.toString())) {

            Firebase.auth.createUserWithEmailAndPassword(
                binding.emailFld.text.toString(),
                binding.passwordFld.text.toString()
            ).addOnSuccessListener {
                val id = Firebase.auth.currentUser?.uid
                val user = User(
                    id!!,
                    binding.nameFld.text.toString(),
                    binding.emailFld.text.toString()
                )

                Firebase.firestore.collection("users").document(id).set(user)
                    .addOnSuccessListener {
                        sendVerificationEmail()
                        finish()
                    }
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }.addOnSuccessListener {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
        }else
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
    }

    private fun checkIfNullOrBlankOrEmpty(field: String): Boolean {
        if(field.isBlank() && field.isEmpty()){
            return true
        }
        return false
    }

    private fun sendVerificationEmail() {
        Firebase.auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
            Toast.makeText(this,"Verifique su email antes de entrar", Toast.LENGTH_LONG).show()
        }?.addOnFailureListener {
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
        }
    }

}






