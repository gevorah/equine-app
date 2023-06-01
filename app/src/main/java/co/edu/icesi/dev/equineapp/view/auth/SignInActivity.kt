package co.edu.icesi.dev.equineapp.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.databinding.ActivitySignInBinding
import co.edu.icesi.dev.equineapp.model.User
import co.edu.icesi.dev.equineapp.view.MainActivity
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
            if(!checkIfNullOrBlankOrEmpty(binding.emailFld.text.toString()) &&
                !checkIfNullOrBlankOrEmpty(binding.passwordFld.text.toString())){

                val email: String = binding.emailFld.text.toString()
                val password: String = binding.passwordFld.text.toString()
                Firebase.auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    val fbuser = Firebase.auth.currentUser
                    if(fbuser!!.isEmailVerified){
                        Firebase.firestore.collection("users").document(fbuser.uid).get().addOnSuccessListener {
                            val user = it.toObject(User::class.java)
                            saveUser(user!!)
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                    } else{
                        Toast.makeText(this, "Su email no ha sido verificado", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG).show()
            }
        }

        binding.lostPasswordTxt.setOnClickListener {
            if(binding.emailFld.text?.isBlank() != true && binding.emailFld.text?.isEmpty() != true){
                Firebase.auth.sendPasswordResetEmail(binding.emailFld.text.toString())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Revise su correo "+binding.emailFld.text.toString(), Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
            }else
                Toast.makeText(this, "Escriba su correo para recuperar su contraseña", Toast.LENGTH_SHORT).show()
        }

        binding.signUpTxt.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkIfNullOrBlankOrEmpty(field: String): Boolean {
        if(field.isBlank() && field.isEmpty()){
            return true
        }
        return false
    }

    private fun saveUser (user: User){
        val sp = getSharedPreferences("pets-cued", MODE_PRIVATE)
        val json = Gson().toJson(user)
        sp.edit().putString("user", json).apply()
    }
}