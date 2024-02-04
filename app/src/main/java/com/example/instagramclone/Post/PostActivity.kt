package com.example.instagramclone.Post

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.instagramclone.HomeActivity
import com.example.instagramclone.Models.Post
import com.example.instagramclone.databinding.ActivityPostBinding
import com.example.instagramclone.utils.POST
import com.example.instagramclone.utils.POST_FOLDER
import com.example.instagramclone.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class PostActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    var imageUrl:String?= null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri ->
        uri?.let {
            uploadImage(uri, POST_FOLDER){
                    url ->
                if(url!=null){
                    binding.selectImage.setImageURI(uri)
                    imageUrl=url
                }
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }
        binding.selectImage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.postButton.setOnClickListener {
                val post:Post= Post(imageUrl!!,binding.caption.editableText.toString())
            Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
            Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(post).addOnSuccessListener {
                startActivity(Intent(this@PostActivity,HomeActivity::class.java))
                finish()
            }
            }
        }
    }
}