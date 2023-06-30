package com.example.myattendance

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myattendance.R

class ViewPagerWelcome1Adapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    val sliderImages = listOf<Int>(
        R.drawable.icons_time_attendance_clocks,
        R.drawable.icons_employee_self_service,
        R.drawable.people_with_mask
    )

    override fun createFragment(position: Int): Fragment {
        val fragment = SliderFragmentWelcome1()
        fragment.arguments = Bundle().apply {
            putInt(SliderFragmentWelcome1.IMAGE_SENT,sliderImages[position])
        }
        return fragment
    }

}