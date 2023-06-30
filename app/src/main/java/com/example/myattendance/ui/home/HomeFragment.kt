package com.example.myattendance.ui.home

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myattendance.OnSelectLocationCallback
import com.example.myattendance.datamodel.Location
import com.example.myattendance.ui.LocationListAdapter
import com.example.myattendance.R
import com.example.myattendance.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), OnSelectLocationCallback {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!! //initialization
    private lateinit var myLocationAdapter : LocationListAdapter //declaration
    val homeViewModel: HomeViewModel by viewModels()
//    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myLocationAdapter = LocationListAdapter(this)
        binding.fragHomeRvLocation.layoutManager = LinearLayoutManager(requireActivity())

        homeViewModel.locations.observe(viewLifecycleOwner,{
            res -> myLocationAdapter.submitList(res)
            Log.d("result livedata: ","$res")
            binding.fragHomeRvLocation.adapter = myLocationAdapter
        })


        binding.fragHomeBtnCheck.setOnClickListener {
            //if there is location selected
            if(homeViewModel.locationSelected!=null){
                homeViewModel.setCheckState()
            }
        }

        homeViewModel.checkState.observe(viewLifecycleOwner,{
            Log.d("checkState in homeFragment","$it")
            if(it!=null){
                binding.fragHomeCheckTitle.text = "CHECK OUT"
                binding.fragHomeBtnCheck.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),
                    R.color.yellow_app))
            }else{
                binding.fragHomeCheckTitle.text = "CHECK IN"
                binding.fragHomeBtnCheck.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.green_app))
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSelectLocation(location: Location) {
        homeViewModel.setSelectedLocation(location)
    }


}