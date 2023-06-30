package com.example.myattendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myattendance.databinding.SliderWelcome1LayoutBinding

class SliderFragmentWelcome1 : Fragment() {

    private var _binding: SliderWelcome1LayoutBinding? = null
    private val binding get() = _binding!!

    companion object{
        val IMAGE_SENT = "IMAGE_SENT"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SliderWelcome1LayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(IMAGE_SENT) }?.apply {
            val resourceDrawable = getInt(IMAGE_SENT)
            binding.slider1Img.setImageResource(resourceDrawable)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}