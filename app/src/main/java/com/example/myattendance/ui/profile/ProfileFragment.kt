package com.example.myattendance.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myattendance.R
import com.example.myattendance.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        binding.fragProfileEmail.text = auth.currentUser?.email

        profileViewModel.profileData.observe(viewLifecycleOwner,{
            profileData ->
            Log.d("profile data profileFragment","$profileData")
            binding.fragProfileName.text = profileData.employeeData?.fullname
            binding.fragProfilePosition.text = profileData.employeeData?.position
            binding.fragProfileEmployeeId.text = profileData.employeeData?.employeeID
            binding.fragProfileEmployeeAddress.text = profileData.employeeData?.address
        })

        binding.fragProfileChangePassword.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_changePasswordActivity)
        }

        binding.fragProfileSignout.setOnClickListener {
            auth.signOut()
//            val uri = Uri.parse("android-app://com.example.phinconattendance/loginfragment")
//            findNavController().navigate(uri)
//            findNavController().navigate(
//                R.id.mainActivity,
//                bundleOf("userId" to 1)
//            )
            findNavController().navigate(
                R.id.action_navigation_profile_to_mainActivity
            )
            requireActivity().finish()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}