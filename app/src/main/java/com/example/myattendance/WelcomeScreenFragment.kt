package com.example.myattendance

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.myattendance.R
import com.example.myattendance.databinding.FragmentWelcomeBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WelcomeScreenFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        //to make statusbar dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Your code for API version 30 or above
            requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS)
        } else {
            // Your code for API versions below 30
            requireActivity().window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //instantiate viewpager adapters
        val viewPagerWelcome1Adapter = ViewPagerWelcome1Adapter(this)
        val viewPagerWelcome2Adapter = ViewPagerWelcome2Adapter(this)

        //make dots indicator
        val dots = arrayOfNulls<ImageView>(viewPagerWelcome1Adapter.itemCount)
        for (i in dots.indices) {
            dots[i] = ImageView(requireContext())
            dots[i]?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.circle_indicator_inactive))

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(5,0,5,0)
            binding.fragWelcomeIndicators.addView(dots[i],params)
        }

        //set callback viewpager
        val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                binding.fragWelcomeVp1.currentItem = position
                binding.fragWelcomeTextVp.currentItem = position

                //set active and inactive indicator
                for (i in dots.indices){
                    dots[i]?.setImageResource(R.drawable.circle_indicator_inactive)
                }
                dots[position]?.setImageResource(R.drawable.circle_indicator_active)
            }
        }
        binding.fragWelcomeVp1.registerOnPageChangeCallback(onPageChangeCallback)
        binding.fragWelcomeTextVp.registerOnPageChangeCallback(onPageChangeCallback)

        //set adapter viewpager
        binding.fragWelcomeVp1.adapter = viewPagerWelcome1Adapter
        binding.fragWelcomeTextVp.adapter = viewPagerWelcome2Adapter

        binding.fragWelcomeLoginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_loginFragment)
        }
        binding.fragWelcomeSignupBtn.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_registerFragment)
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}