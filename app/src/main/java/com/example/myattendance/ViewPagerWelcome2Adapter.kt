package com.example.myattendance

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myattendance.R

class ViewPagerWelcome2Adapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    val title = listOf<Int>(
        R.string.title_welcome_1,
        R.string.title_welcome_2,
        R.string.title_welcome_3
    )

    val desc = listOf<Int>(
        R.string.desc_welcome_1,
        R.string.desc_welcome_2,
        R.string.desc_welcome_3
    )

    override fun createFragment(position: Int): Fragment {
        val fragment = SliderFragmentWelcome2()
        fragment.arguments = Bundle().apply {
            putInt(SliderFragmentWelcome2.CODE_TITLE,title[position])
            putInt(SliderFragmentWelcome2.CODE_DESC,desc[position])
        }
        return fragment
    }



}