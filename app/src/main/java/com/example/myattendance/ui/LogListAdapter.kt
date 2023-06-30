package com.example.myattendance.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myattendance.datamodel.CheckData
import com.example.myattendance.helper.getTimeAMPM
import com.example.myattendance.R
import com.example.myattendance.databinding.RvLogsItemBinding

class LogListAdapter() :  ListAdapter<CheckData, LogListAdapter.MyViewHolder>(DIFF_CALLBACK){

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CheckData>() {
            //membandingkan seluruhnya
            override fun areContentsTheSame(
                oldItem: CheckData,
                newItem: CheckData
            ): Boolean {
                return false
            }

            //membandingkan identifier unique (misal ID)
            override fun areItemsTheSame(
                oldItem: CheckData,
                newItem: CheckData
            ): Boolean {
                return false
            }
        }
    }

    var adapterSelectedID = -1

    inner class MyViewHolder(private val binding: RvLogsItemBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(checkData: CheckData){
//            Log.d("cart in list: ","${Location}")
            with(binding){
//                val backgroundColor = if(location.is_choosen) R.color.blue_darker else R.color.white
                val uri = "@drawable/location_${checkData.location?.image_link}"
                try{
                    val imageID = root.context.resources.getIdentifier(uri, null, root.context.packageName)
                    val myDrawable = ContextCompat.getDrawable(root.context, imageID)
                    rvLogsItemPhoto.setImageDrawable(myDrawable)
                }catch (e: Exception){
                    Log.d("image error","${e.stackTrace}")
                    rvLogsItemPhoto.setImageResource(R.drawable.location_office)
                }
                val y = if(checkData.checked==1) "Check out " else "Check in"
                rvLogsTitle.text = "$y - ${checkData.location?.name} - ${getTimeAMPM(checkData.date_checked)}"
                rvLogsAddress.text = checkData.location?.address
//                ColorStateList.valueOf(ContextCompat.getColor(root.context,R.color.blue_darker));
//                rvLocationItemContainer.setCardBackgroundColor(ContextCompat.getColor(root.context,backgroundColor))

//                rvLocationItemContainer.setOnClickListener {
//                    onSelectLocationCallback.onSelectLocation(checkData.id)
//                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = RvLogsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val menuItem = getItem(position)
        menuItem.let { holder.bind(it) }
    }
    
}