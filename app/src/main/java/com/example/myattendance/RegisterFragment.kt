package com.example.myattendance

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.myattendance.datamodel.User
import com.example.myattendance.databinding.FragmentRegisterBinding
import com.example.myattendance.datamodel.EmployeeData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var queryValueListener: ValueEventListener
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

        val employeeEmail = binding.fragRegisterEmailEt.text
        val username = binding.fragRegisterUsernameEt.text
        val fullname = binding.fragRegisterFullnameEt.text
        val password = binding.fragRegisterPasswordEt.text
        val repeatPassword = binding.fragRegisterRepeat.text

        binding.fragRegisterBtnRegister.setOnClickListener {
            if(employeeEmail.isNullOrEmpty()){
                Toast.makeText(requireContext(),"fill the username please",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
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

            //check to employeeData, is there any data that has the same email and fullname?
            //if yes, create user with firebase auth
            //if no, show a dialog that explain email or fullname not match to employee data
            queryValueListener = databaseRef.child("employee_data").orderByChild("email").equalTo(employeeEmail.toString())
                .addValueEventListener(
                object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            //Log.d("check employee snapshot","${snapshot.value}")
                            val firstData = snapshot.children.first()
                            //Log.d("check employee firstData","${firstData.value}")
                            val employeeFullname = firstData.child("fullname").value as String
                            if(fullname.toString()==employeeFullname){
//                                Toast.makeText(requireContext(),"email and fullname match",Toast.LENGTH_LONG).show()
                                val employeeID = firstData.child("employee_id").value as String
                                val email = firstData.child("email").value as String
                                val address = firstData.child("address").value as String
                                val position = firstData.child("position").value as String
                                auth.createUserWithEmailAndPassword(employeeEmail.toString().trim(), password.toString()).addOnCompleteListener(requireActivity()){
                                    task ->
                                    if(task.isSuccessful){
                                        val user = auth.currentUser
                                        val uid = user?.uid
                                        Log.d("REGISTER FIREBASE", "createUserWithEmail:success $uid")
                                        val employeeDataObj = EmployeeData(employeeID,address,email,employeeFullname,position)
                                        createUser(uid as String, username.toString(), employeeDataObj)
                                        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                                        action.registerToLogin = uid
                                        findNavController().navigate(

                                            action,

                                            //Pass the fragment id where you want to land on the back press at fragmentC,
                                            //true/false, in case you want to remove the fragmentA from the back stack use the TRUE and vice versa
                                            NavOptions.Builder().setPopUpTo(R.id.welcome_fragment, false).build()
                                        )
                                    }else{
                                        //Log.w("REGISTER FIREBASE", "createUserWithEmail:failure ${task.exception?.message}")
                                        Toast.makeText(requireContext(), "Authentication failed. ${task.exception?.localizedMessage}",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }else{
                                Toast.makeText(requireContext(),"fullname is wrong",Toast.LENGTH_LONG).show()
                            }
                        }else{
                            Toast.makeText(requireContext(),"email is not registered as employee email",Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("register error","$error")
                    }
            })

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseRef.removeEventListener(queryValueListener);
    }

    fun createUser(uid:String, username:String, employeeData:EmployeeData){
        //make data
        val user = User(username,employeeData)
        //set data to field
        databaseRef.child("users").child(uid).setValue(user)
    }
}