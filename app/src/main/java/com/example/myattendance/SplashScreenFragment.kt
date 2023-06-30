package com.example.myattendance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myattendance.databinding.FragmentSplashScreenBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 * Use the [SplashScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    lateinit var databaseRef : DatabaseReference
    private val SPLASH_SCREEN_TIME_OUT : Long = 2000;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseRef = Firebase.database.reference

//        //splash screen
//        Handler(Looper.getMainLooper()).postDelayed({
//            findNavController().navigate(R.id.action_splash_screen_to_welcome)
//        },SPLASH_SCREEN_TIME_OUT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    fun generateLocFromFirebase(){
//        val myLocsQuery = databaseRef.child("locations")
//        myLocsQuery.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for(postSnapshot in dataSnapshot.children){
//                    val generated_id = postSnapshot.key
//                    val id = postSnapshot.child("id").value
//                    val name = postSnapshot.child("name").value
//                    val location = postSnapshot.child("address").value
//                    Log.d("location firebase data: ", "$generated_id - $id - $name - $location")
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.w("location firebase: ", "loadPost:onCancelled", error.toException())
//            }
//        })
//    }

//    fun createUser(){
//        //get the id of new field (field that will be added by data)
//        val key = databaseRef.child("users").push()
//        //make data
//        val post = User( "User 1", "My Name is User 1", "sayasayasaya")
//        //set data to field
//        key.setValue(post)
//    }

}