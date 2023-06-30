package com.example.myattendance.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myattendance.helper.Filter
import com.example.myattendance.ui.LogListAdapter
import com.example.myattendance.R
import com.example.myattendance.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val historyViewModel: HistoryViewModel by viewModels()
    private lateinit var logListAdapter: LogListAdapter
//    private lateinit var filterButtonList: List<Button>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val historyViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
//        WindowCompat.setDecorFitsSystemWindows(requireActivity().window,true)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.fragHistoryRadioFilter.setOnCheckedChangeListener { radioGroup, id ->
              when (id) {
                R.id.frag_history_f1 -> historyViewModel.setFilter(Filter.DAY)
                R.id.frag_history_f2 -> historyViewModel.setFilter(Filter.WEEK)
                R.id.frag_history_f3 -> historyViewModel.setFilter(Filter.MONTH)
                R.id.frag_history_f4 -> historyViewModel.setFilter(Filter.YEAR)
            }
        }

        logListAdapter = LogListAdapter()
        binding.fragHistoryRvItems.adapter = logListAdapter
        binding.fragHistoryRvItems.layoutManager = LinearLayoutManager(requireContext())

        historyViewModel.logCheckHistory1.observe(viewLifecycleOwner,{
            logListAdapter.submitList(it)
        })
    }

//    fun setStyleOfButtons(indexSelected: Int){
//        filterButtonList.forEachIndexed { index, button ->
//            if(index==indexSelected){
//                button
//            }else{
//
//            }
//        }
//    }
}