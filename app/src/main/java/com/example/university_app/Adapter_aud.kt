package com.example.university_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class Adapter_aud(private val audList : ArrayList<Audiences>) :
    RecyclerView.Adapter<Adapter_aud.View_Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_aud,parent,false)
        return View_Holder(itemView)
    }

    override fun getItemCount(): Int {
        return audList.size
    }

    override fun onBindViewHolder(holder: View_Holder, position: Int) {
        val currentItem = audList[position]
        holder.icon.setImageResource(currentItem.icon)
        holder.room.text = currentItem.heading
    }

    class View_Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val icon : ShapeableImageView = itemView.findViewById(R.id.icon)
        val room : TextView = itemView.findViewById(R.id.room)
    }
}