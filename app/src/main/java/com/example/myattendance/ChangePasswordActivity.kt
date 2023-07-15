package com.example.myattendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.example.myattendance.databinding.ActivityChangePasswordBinding
import com.example.myattendance.ui.profile.ProfileViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val userFirebase = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(this.window, false)


        binding.changePsdBtnConfirm.setOnClickListener {
            val currentPassword = binding.changePsdCurrentPsd.text.toString()
            val newPassword = binding.changePsdNewPsd.text.toString()
            val retypeNewPassword = binding.changePsdRetypeNewPsd.text.toString()

            if(currentPassword.isEmpty()||newPassword.isEmpty()||retypeNewPassword.isEmpty()){
                Toast.makeText(this,"please fill all fields",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!newPassword.equals(retypeNewPassword)){
                Toast.makeText(this,"new password and new password retyped are not matched",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userFirebase?.let {
                user ->
                val credential = EmailAuthProvider.getCredential(user.email.toString(),currentPassword)
                user.reauthenticate(credential).addOnSuccessListener {
                    Log.d("check process change password", "old password is right")
                    user.updatePassword(newPassword).addOnSuccessListener {
                        Toast.makeText(this,"success updating password",Toast.LENGTH_LONG).show()
                        finish()
                    }.addOnFailureListener {
                        Log.d("check process change password", "failed updating password $it")
                        Toast.makeText(this,"there's problem on updating password. try it later",Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    Log.d("check process change password","failed re-authenticating $it")
                    Toast.makeText(this,"old password is not right",Toast.LENGTH_LONG).show()
                }
            }
//            finish()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

       // findNavController(R.id.mobile_navigation).navigate(R)
    }
}