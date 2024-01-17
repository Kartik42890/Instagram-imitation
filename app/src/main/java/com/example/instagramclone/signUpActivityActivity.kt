package com.example.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.instagramclone.databinding.ActivitySignUpActivityBinding
import com.google.firebase.auth.FirebaseAuth

class signUpActivityActivity : AppCompatActivity() {
    val binding by lazy {
        ActivitySignUpActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.registerButton.setOnClickListener {
            if (binding.name.editText?.text.toString().equals("") or
            binding.email.editText?.text.toString().equals("") or
            binding.password.editText?.text.toString().equals("")){
                Toast.makeText(this@signUpActivityActivity, "Please fill all the Information", Toast.LENGTH_SHORT).show()
            }else{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.email.editText?.text.toString(),
                    binding.password.editText?.text.toString()
                ).addOnCompleteListener {
                    result ->
                    if(result.isSuccessful){
                        Toast.makeText(this@signUpActivityActivity, "Login Successfully", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@signUpActivityActivity, result.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}