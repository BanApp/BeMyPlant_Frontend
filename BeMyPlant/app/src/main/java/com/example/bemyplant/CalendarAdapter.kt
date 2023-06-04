package com.example.bemyplant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plantver2.fragment.CalendarFragment

class CalendarAdapter(
    private val context: Context,
    private val dayList: ArrayList<Day>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = dayList[position]
        holder.dayText.text = day.day
        holder.itemView.setOnClickListener {
            // 아이템 클릭 처리
            val isInMonth = day.isInMonth
            //(context as? DiaryActivity)?.onItemClick(holder.itemView, day.day, isInMonth)
            itemClickListener.onItemClick(holder.itemView, day.day, isInMonth)
        }
    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    fun setClickListener(calendarFragment: CalendarFragment) {
    //
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayText: TextView = itemView.findViewById(R.id.dayText)
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, day: String?, isInMonth: Boolean)
    }


}
