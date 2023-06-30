package com.example.myattendance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myattendance.databinding.SliderWelcome2LayoutBinding

class SliderFragmentWelcome2 : Fragment() {

    private var _binding: SliderWelcome2LayoutBinding? = null
    private val binding get() = _binding!!

    companion object{
        val CODE_TITLE = "CODE TITLE"
        val CODE_DESC = "CODE DESC"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SliderWelcome2LayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(CODE_TITLE) && it.containsKey(CODE_DESC) }?.apply {
            val resourceString_title = getInt(CODE_TITLE)
            val resourceString_desc = getInt(CODE_DESC)
            Log.d("slider fragment title",getString(resourceString_title))
            binding.sliderWelcome2Title.text = getString(resourceString_title)
            binding.sliderWelcome2Desc.text = getString(resourceString_desc)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}