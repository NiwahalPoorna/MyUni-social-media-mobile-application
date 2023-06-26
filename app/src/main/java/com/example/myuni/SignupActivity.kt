package com.example.myuni


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myuni.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var  databaseReference: DatabaseReference
    private lateinit var storageReference:StorageReference
    private lateinit var imageUri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        binding.btnext.setOnClickListener {

            val name = binding.username.text.toString()
            val faculty = binding.userFaculty.text.toString()
            val batch = binding.userbatch.text.toString()
            val degree = binding.userDegree.text.toString()

            val user = User(name, faculty, batch, degree)
            if(uid !=null)

                databaseReference.child(uid).setValue(user).addOnCompleteListener{

                    if(it.isSuccessful){

                        uploadProfilePic()

                    }else{

                        Toast.makeText(this@SignupActivity,"Failed to update  profile",Toast.LENGTH_SHORT).show()
                    }


                }
        }



    }
    private fun uploadProfilePic(){
        imageUri=Uri.parse("android.resource://$packageName/${R.drawable.profile}")
        storageReference=FirebaseStorage.getInstance().getReference("Users/"+auth.currentUser?.uid)
        storageReference.putFile(imageUri).addOnSuccessListener{


            Toast.makeText(this@SignupActivity,"Profile successfully updated",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }.addOnFailureListener{

            Toast.makeText(this@SignupActivity,"Failed to uploaded profile",Toast.LENGTH_SHORT).show()
        }

    }
}
