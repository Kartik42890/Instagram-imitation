package com.example.instagramclone

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.instagramclone.Models.User
import com.example.instagramclone.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.createnewaccbtn.setOnClickListener {
            startActivity(Intent(this@LoginActivity,signUpActivityActivity::class.java))
            finish()
        }
        binding.loginButton.setOnClickListener {
            if(binding.email.editText?.text.toString().equals("") or
                binding.password.editText?.text.toString().equals("")){
                Toast.makeText(this@LoginActivity, "Please fill all the information", Toast.LENGTH_SHORT).show()
        }else{
            var user = User(binding.email.editText?.text.toString(),
                binding.password.editText?.text.toString())

                Firebase.auth.signInWithEmailAndPassword(user.email!!, user.password!!).addOnCompleteListener {
                    if (it.isSuccessful){
                        startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this@LoginActivity, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }
    }
}