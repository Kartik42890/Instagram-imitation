package com.example.socialharbour

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.socialharbour.Models.User
import com.example.socialharbour.databinding.ActivitySignUpActivityBinding
import com.example.socialharbour.utils.USER_NODE
import com.example.socialharbour.utils.USER_PROFILE_FOLDER
import com.example.socialharbour.utils.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class signUpActivityActivity : AppCompatActivity() {
    val binding by lazy {
        ActivitySignUpActivityBinding.inflate(layoutInflater)
    }
    lateinit var user: User
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri ->
//        println(uri)
        uri?.let {
                uploadImage(uri, USER_PROFILE_FOLDER){
                    imageUrl ->
                    if(imageUrl!=null){
                        user.image=imageUrl
                        binding.profileImage.setImageURI(uri)
                    }
                }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val text = "<font color=#FF000000>Already Have An Account</font> <font color=#1E88E5>login ?</font>"
        binding.login.setText(Html.fromHtml(text))
        user= User()
        if(intent.hasExtra("MODE")){
            if(intent.getIntExtra("MODE",-1)==1){
                binding.registerButton.text="Update Profile"
                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                    .addOnSuccessListener {
                        user = it.toObject<User>()!!
                        if(!user.image.isNullOrEmpty()){
                            Picasso.get().load(user.image).into(binding.profileImage)
                        }
                        binding.name.editText?.setText(user.name)
                        binding.email.editText?.setText(user.email)
                        binding.password.editText?.setText(user.password)
                    }
            }
        }
        binding.registerButton.setOnClickListener {
            if(intent.hasExtra("MODE")){
                if(intent.getIntExtra("MODE",-1)==1){
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(
                            user)
                        .addOnSuccessListener {
                            startActivity(
                                Intent(
                                    this@signUpActivityActivity,
                                    HomeActivity::class.java
                                )
                            )
                            finish()
                        }
                }
            }else {

                if (binding.name.editText?.text.toString().equals("") or
                    binding.email.editText?.text.toString().equals("") or
                    binding.password.editText?.text.toString().equals("")
                ) {
                    Toast.makeText(
                        this@signUpActivityActivity,
                        "Please fill all the Information",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.email.editText?.text.toString(),
                        binding.password.editText?.text.toString()
                    ).addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            user.name = binding.name.editText?.text.toString()
                            user.password = binding.password.editText?.text.toString()
                            user.email = binding.email.editText?.text.toString()
                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid).set(
                                user
                            )
                                .addOnSuccessListener {
                                    startActivity(
                                        Intent(
                                            this@signUpActivityActivity,
                                            HomeActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                        } else {
                            Toast.makeText(
                                this@signUpActivityActivity,
                                result.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        binding.addImage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.login.setOnClickListener {
            startActivity(Intent(this@signUpActivityActivity,LoginActivity::class.java))
            finish()
        }
    }
}