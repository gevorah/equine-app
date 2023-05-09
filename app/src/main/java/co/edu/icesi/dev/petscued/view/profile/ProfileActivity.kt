package co.edu.icesi.dev.petscued.view.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import co.edu.icesi.dev.petscued.databinding.ActivityProfileBinding
import co.edu.icesi.dev.petscued.model.User
import co.edu.icesi.dev.petscued.view.MainActivity
import com.google.gson.Gson

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding:ActivityProfileBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Cargar el usuario de los SP
        val user = loadUser()
        if(user == null || Firebase.auth.currentUser == null || Firebase.auth.currentUser?.isEmailVerified == false){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }else{
            this.user = user
            Toast.makeText(this, "Hola ${user.name}", Toast.LENGTH_LONG).show()
        }
        Firebase.messaging.subscribeToTopic(user.id)

        binding.logoutTV.setOnClickListener(::logout)
        binding.changePasswordBtn.setOnClickListener(::changePassword)
    }

    private fun logout(view: View){
        finish()
        val user = loadUser()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        user?.let { it1 -> Firebase.messaging.unsubscribeFromTopic(it1.id) }
        val sp = getSharedPreferences("pets-cued", MODE_PRIVATE)
        sp.edit().clear().apply()
        Firebase.auth.signOut()
    }

    fun loadUser(): User? {
        val sp = getSharedPreferences("pets-cued", MODE_PRIVATE)
        val json = sp.getString("user", "NO_USER")
        if(json == "NO_USER"){
            return null
        }else{
            return Gson().fromJson(json, User::class.java)
        }
    }

    private fun changePassword(view: View){
        val newPassword = binding.newpassET.editText?.text.toString()

        if(newPassword.isEmpty()){
            binding.newpassET.error = "El campo no puede estar vacío"
            return
        }else binding.newpassET.error = null

        if(newPassword.length < 6){
            binding.newpassET.error = "La contraseña está muy corta, mínimo 6 caracteres"
            return
        }else binding.newpassET.error = null

        val builder = AlertDialog.Builder(this)
            .setTitle("Cambio de contraseña")
            .setMessage("¿Desea cambiar la contraseña?")
            .setPositiveButton("Si"){ dialog, _ ->
                Firebase.auth.currentUser?.updatePassword(newPassword)?.addOnSuccessListener {
                    Toast.makeText(this, "Contraseña cambiada!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }?.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
            .setNegativeButton("No"){ dialog, _ ->
                dialog.dismiss()
            }
        builder.show()
    }
}