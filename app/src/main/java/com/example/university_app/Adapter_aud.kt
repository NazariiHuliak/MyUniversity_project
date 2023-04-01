package com.example.university_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import com.example.university_app.AuditoryFragment
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.*


class AuditoryAdapter(private val audList: List<Audiences>, private val dataList: MutableList<AudienseModel>) : RecyclerView.Adapter<AuditoryAdapter.AuditoryViewHolder>()  {






    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuditoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_aud, parent, false)
        return AuditoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: AuditoryViewHolder, position: Int) {
        val auditory = audList[position]
        holder.icon.setImageResource(auditory.icon)
        holder.auditoryNameTextView.text = auditory.heading
    }

    override fun getItemCount(): Int {
        return audList.size
    }


    inner class AuditoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val auditoryNameTextView: TextView = itemView.findViewById(R.id.room)
        val icon : ShapeableImageView = itemView.findViewById(R.id.icon)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(auditory: Audiences) {

                val icon : ShapeableImageView = itemView.findViewById(R.id.icon)
                val name : TextView = itemView.findViewById(R.id.room)
        }

        override fun onClick(view: View) {
            val context = view.context
            val auditory = audList[adapterPosition]

            val dialog = Dialog(context)
            dialog.setContentView(R.layout.info_aud)
            dialog.setTitle(auditory.heading)
            val temp: Drawable? = ContextCompat.getDrawable(context, auditory.icon)
            val tempIconId = auditory.icon


            dialog.findViewById<ImageView>(R.id.lock).setImageDrawable(temp)
            val currentTime = Calendar.getInstance().timeInMillis
            val calendar = Calendar.getInstance()

            val timeParts = dataList[adapterPosition].start_lesson.split(":")
            val hours = timeParts[0].toInt()
            val minutes = timeParts[1].toInt()
            calendar.set(Calendar.HOUR_OF_DAY, hours)
            calendar.set(Calendar.MINUTE, minutes)
            val startTime = calendar.timeInMillis

            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            // якщо вільно
            if (tempIconId == 2131230879) {

                dialog.findViewById<TextView>(R.id.info_lock).setText("Вільно")
                if (getDayOfWeekString(dayOfWeek) == dataList[adapterPosition].day) {

                    if (currentTime > startTime) {
                        dialog.findViewById<TextView>(R.id.info_time)
                            .setText("Найближче заняття завтра")
                    } else {
                        dialog.findViewById<TextView>(R.id.info_time)
                            .setText("Найближче заняття ${dataList[adapterPosition].start_lesson} - ${dataList[adapterPosition].end_lesson}")
                    }
                }
                else{
                    dialog.findViewById<TextView>(R.id.info_time)
                        .setText("Найближче заняття завтра")
                }
            }
            else{
                dialog.findViewById<TextView>(R.id.info_lock).setText("Проводиться заняття")
            }

//            val lessonText = "${dataList[adapterPosition].start_lesson} - ${dataList[adapterPosition].end_lesson}"
//            dialog.findViewById<TextView>(R.id.info_time).text = lessonText
            dialog.findViewById<TextView>(R.id.info_subject).setText(dataList[adapterPosition].subject)
            dialog.findViewById<TextView>(R.id.info_teacher).setText(dataList[adapterPosition].tutor)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val auditoryNumberTextView = dialog.findViewById<TextView>(R.id.room)
            auditoryNumberTextView.text = auditory.heading.toString()
            dialog.show()
        }
        private fun getDayOfWeekString(dayOfWeek: Int): String {
            return when (dayOfWeek) {
                Calendar.SUNDAY -> "Неділя"
                Calendar.MONDAY -> "Понеділок"
                Calendar.TUESDAY -> "Вівторок"
                Calendar.WEDNESDAY -> "Середа"
                Calendar.THURSDAY -> "Четвер"
                Calendar.FRIDAY -> "П'ятниця"
                Calendar.SATURDAY -> "Субота"
                else -> throw IllegalArgumentException("Invalid day of week: $dayOfWeek")
            }

    }
}}
