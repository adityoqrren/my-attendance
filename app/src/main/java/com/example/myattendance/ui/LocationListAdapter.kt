package com.example.myattendance.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myattendance.datamodel.Location_UIModel
import com.example.myattendance.OnSelectLocationCallback
import com.example.myattendance.datamodel.toLocation
import com.example.myattendance.R
import com.example.myattendance.databinding.RvLocationItemBinding

class LocationListAdapter(private val onSelectLocationCallback: OnSelectLocationCallback) :  ListAdapter<Location_UIModel, LocationListAdapter.MyViewHolder>(DIFF_CALLBACK){

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Location_UIModel>() {
            //membandingkan seluruhnya
            override fun areContentsTheSame(
                oldItem: Location_UIModel,
                newItem: Location_UIModel
            ): Boolean {
                return oldItem == newItem
            }

            //membandingkan identifier unique (misal ID)
            override fun areItemsTheSame(
                oldItem: Location_UIModel,
                newItem: Location_UIModel
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    var adapterSelectedID = -1

    inner class MyViewHolder(private val binding: RvLocationItemBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(location: Location_UIModel){
//            Log.d("cart in list: ","${Location}")
            with(binding){
//                val backgroundColor = if(location.is_choosen) R.color.blue_darker else R.color.white
                val uri = "@drawable/location_${location.image_link}"
                try{
                    val imageID = root.context.resources.getIdentifier(uri, null, root.context.packageName)
                    val myDrawable = ContextCompat.getDrawable(root.context, imageID)
                    rvItemPhoto.setImageDrawable(myDrawable)
                }catch (e: Exception){
                    Log.d("image error","${e.stackTrace}")
                    rvItemPhoto.setImageResource(R.drawable.location_office)
                }
                rvOfficeTitle.text = location.name
                rvOfficeAddress.text = location.address
//                ColorStateList.valueOf(ContextCompat.getColor(root.context,R.color.blue_darker));
//                rvLocationItemContainer.setCardBackgroundColor(ContextCompat.getColor(root.context,backgroundColor))
                if(location.is_choosen){
                    rvLocationItemContainer.setCardBackgroundColor(ContextCompat.getColor(root.context,R.color.blue_darker))
                    rvOfficeTitle.setTextColor(ContextCompat.getColor(root.context,R.color.white))
                    rvOfficeAddress.setTextColor(ContextCompat.getColor(root.context,R.color.white))
                }else{
                    rvLocationItemContainer.setCardBackgroundColor(ContextCompat.getColor(root.context,R.color.white))
                }

                rvLocationItemContainer.setOnClickListener {
                    onSelectLocationCallback.onSelectLocation(location.toLocation())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = RvLocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val menuItem = getItem(position)
        menuItem.let { holder.bind(it) }
    }

//    fun getSwipedData(swipedPosition: Int): Location = getItem(swipedPosition)

//    override fun submitList(list: MutableList<Location>?) {
//        super.submitList(list?.let { ArrayList(it) })
//    }

//    override fun getItemCount(): Int {
//        return allItemsMenu.size
//    }
}