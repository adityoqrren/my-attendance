package com.example.myattendance

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myattendance.datamodel.User
import com.example.myattendance.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    lateinit var databaseRef : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        databaseRef = Firebase.database.reference

        val username = binding.fragRegisterUsernameEt.text
        val fullname = binding.fragRegisterFullnameEt.text
        val password = binding.fragRegisterPasswordEt.text
        val repeatPassword = binding.fragRegisterRepeat.text

        binding.fragRegisterBtnRegister.setOnClickListener {
            if(username.isNullOrEmpty()){
                Toast.makeText(requireContext(),"fill the username please",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(fullname.isNullOrEmpty()){
                Toast.makeText(requireContext(),"fill the fullname please",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(password.isNullOrEmpty()){
                Toast.makeText(requireContext(),"fill the password please",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(password.toString().length<6){
                Toast.makeText(requireContext(),"password must at least 6 characters",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!password.toString().equals(repeatPassword.toString())){
                Toast.makeText(requireContext(),"password and password repeated are not same",Toast.LENGTH_SHORT).show()
                Log.d("check register password","${password.equals(repeatPassword)}")
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(username.toString().trim(), password.toString()).addOnCompleteListener(requireActivity()){
                task ->
                if(task.isSuccessful){
                    val user = auth.currentUser
                    val uid = user?.uid
                    Log.d("REGISTER FIREBASE", "createUserWithEmail:success $uid")
                    createUser(uid as String, username.toString(), fullname.toString())
                    val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                    action.registerToLogin = uid
                    findNavController().navigate(action)
                }else{
                    Log.w("REGISTER FIREBASE", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun createUser(uid:String, username:String, fullname:String){
        //make data
        val user = User(username,fullname)
        //set data to field
        databaseRef.child("users").child(uid).setValue(user)
    }
}