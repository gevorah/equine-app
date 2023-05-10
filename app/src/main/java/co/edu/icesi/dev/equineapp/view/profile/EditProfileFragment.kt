package co.edu.icesi.dev.equineapp.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import co.edu.icesi.dev.equineapp.databinding.FragmentEditProfileBinding
import co.edu.icesi.dev.equineapp.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.gson.Gson

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        binding.saveBtn.setOnClickListener(::editProfile)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.user = loadUser()!!
        binding.editNameTxt.setText(user.name)
        binding.editEmailTxt.setText(user.email)
        binding.editPassword1Txt.doOnTextChanged { text, start, before, count ->
            if (text != null) onChangeNewPWD(text)
        }
        binding.editPassword2Txt.doOnTextChanged { text, start, before, count ->
            if (text != null) onChangeRepNewPWD(text)
        }
    }

    private fun editProfile(view: View) {
        val name = binding.editNameTxt.text.toString()
        val email = binding.editEmailTxt.text.toString()
        val oldPassword = binding.editPassword1Txt.text.toString()
        val newPassword = binding.editPassword2Txt.text.toString()

        if (oldPassword.isNotBlank() || newPassword.isNotBlank()) {
            changePassword(oldPassword, newPassword)
        }
        if (name != user.name || email != user.email) {
            user.name = name
            user.email = email
            Firebase.firestore.collection("users").document(user.id).set(user)

            if (Firebase.auth.currentUser?.email != user.email) {
                Firebase.auth.currentUser?.updateEmail(email)
            }
            saveUser(user)
        }
        Toast.makeText(context, "Usuario actualizado", Toast.LENGTH_SHORT).show()
        requireActivity().supportFragmentManager.popBackStackImmediate()
    }

    private fun onChangeNewPWD(s: CharSequence) {
        if (s.length < 6) {
            binding.editPassword1Txt.error = "La contraseña está muy corta, mínimo 6 caracteres"
        } else binding.editPassword1Txt.error = null;
    }
    private fun onChangeRepNewPWD(s: CharSequence) {
        if (s.toString() != binding.editPassword1Txt.text.toString()) {
            binding.editPassword2Txt.error = "Las contraseñas no coinciden"
        } else binding.editPassword2Txt.error = null;
    }
    private fun changePassword(newPassword: String, repeatNewPassword: String) {
        if(binding.editPassword1Txt.error == null && binding.editPassword2Txt.error == null) {
            if (newPassword.isNotBlank() && repeatNewPassword.isNotBlank()) {
                binding.editPassword1Txt.error = null
                binding.editPassword2Txt.error = null
            } else {
                val msg = "El campo no puede estar vacío"
                if (newPassword.isBlank()) binding.editPassword1Txt.error = msg
                else if (repeatNewPassword.isBlank()) binding.editPassword2Txt.error = msg
                return
            }

            Firebase.auth.currentUser?.updatePassword(newPassword)
        }
    }

    private fun loadUser(): User? {
        val sp = requireActivity().getSharedPreferences("pets-cued", AppCompatActivity.MODE_PRIVATE)
        val json = sp.getString("user", "NO_USER")
        return if (json == "NO_USER") {
            null
        } else {
            Gson().fromJson(json, User::class.java)
        }
    }

    private fun saveUser(user: User) {
        val sp = requireActivity().getSharedPreferences("pets-cued", AppCompatActivity.MODE_PRIVATE)
        val json = Gson().toJson(user)
        sp.edit().putString("user", json).apply()
    }
}