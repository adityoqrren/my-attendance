package com.example.myattendance

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myattendance.R
import com.example.myattendance.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    lateinit var databaseRef : DatabaseReference
    val args : LoginFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        Log.d("Login Fragment","args: ${args.registerToLogin}")
        if(args.registerToLogin!=null){
            Toast.makeText(requireContext(),"Register success. Please sign ini",Toast.LENGTH_LONG).show()
        }

        binding.fragLoginBtnLogin.setOnClickListener {
            val username = binding.fragLoginUsernameEt.text.toString().trim()
            val password = binding.fragLoginPasswordEt.text.toString()

            if(username.isNotEmpty() && password.isNotEmpty()){
                auth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOGIN FIREBASE", "signInWithEmail:success")
                            val user = auth.currentUser
                            Log.d("LOGIN FIREBASE","current user $user")
                            findNavController().navigate(R.id.action_loginFragment_to_dashboardActivity)
                            requireActivity().finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LOGIN FIREBASE", "signInWithEmail:failure $username", task.exception)
                            Toast.makeText(requireContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                Toast.makeText(requireContext(),"fill your username and password",Toast.LENGTH_LONG).show()
            }
        }

        binding.fragLoginToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.fragLoginForgotPass.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}