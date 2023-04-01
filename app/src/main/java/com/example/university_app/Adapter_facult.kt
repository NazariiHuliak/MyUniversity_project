package com.example.university_app


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.currentCoroutineContext

class Adapter_facult(private val dataList: MutableList<FacultiesModel>,private val dialog:Dialog?,private val main_view: View?) :
    RecyclerView.Adapter<Adapter_facult.View_Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_facult,parent,false)
        return View_Holder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: View_Holder, position: Int) {
        val currentItem = dataList[position]
        holder.icon.setImageResource(dialog!!.context.getResources().getIdentifier(currentItem.logo.toString(),"drawable",dialog.context.getPackageName()))
        holder.name.text = currentItem.name


    }

    inner class View_Holder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val icon : ShapeableImageView = itemView.findViewById(R.id.facul_icon)
        val name : TextView = itemView.findViewById(R.id.facult_name)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(view: View) {
            dialog?.dismiss()
            main_view?.findViewById<ImageView>(R.id.info_logo)?.setImageResource(main_view.context.getResources().getIdentifier(dataList[adapterPosition].logo.toString(),"drawable",main_view.context.getPackageName()))
            main_view?.findViewById<TextView>(R.id.info_name)?.setText("Назва: "+dataList[adapterPosition].name.toString())
            main_view?.findViewById<TextView>(R.id.info_location)?.setText("Адреса: "+dataList[adapterPosition].adress.toString())
            main_view?.findViewById<TextView>(R.id.info_phone)?.setText("Телефон: "+dataList[adapterPosition].phone.toString())
            main_view?.findViewById<TextView>(R.id.info_email)?.setText("Пошта: "+dataList[adapterPosition].email.toString())
            main_view?.findViewById<TextView>(R.id.info_site)?.setText("Сайт: "+dataList[adapterPosition].site.toString())
            main_view?.findViewById<TextView>(R.id.info_site)?.setOnClickListener {
                val url = "https://"+dataList[adapterPosition].site.toString()+"/"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                it.context.startActivity(intent)
            }
            val map = main_view?.findViewById<ImageButton>(R.id.map_button)
//            map?.setImageResource(dataList[adapterPosition].mapID.toInt())
            main_view?.findViewById<ImageButton>(R.id.map_button)?.setBackgroundResource(main_view.context.getResources().getIdentifier(dataList[adapterPosition].map.toString(),"drawable",main_view.context.getPackageName()))
            map?.setOnClickListener {
                val url = dataList[adapterPosition].mapURL.toString()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                it.context.startActivity(intent)
            }
        }
    }
}