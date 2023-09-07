package com.example.bemyplant

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bemyplant.fragment.CalendarFragment

class CalendarAdapter(
    private val context: Context,
    private val dayList: ArrayList<Day>,
    private val itemClickListener: ItemClickListener,

    private val dateImageMap: HashMap<String, Int> //더미 데이터 - 키가 날짜, 값이 이미지인 맵 (추후 DB의 데이터로 변환 필요)
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = dayList[position]
        holder.dayTextView.text = day.day.toString()


        // 현재 달의 날짜인 경우 검은색으로 텍스트 색상 설정
        if (day.isInMonth) {
            holder.dayTextView.setTextColor(Color.BLACK)
        } else {
            // 이전 달과 다음 달의 날짜인 경우 회색으로 텍스트 색상 설정
            holder.dayTextView.setTextColor(Color.GRAY)
        }

        // 해당 날짜에 맞는 이미지 설정
        val date = "${day.year}-${String.format("%02d", day.month)}-${String.format("%02d", day.day)}"
        val imageResId = dateImageMap[date] // 해당 날짜에 맞는 이미지 ID를 가져옴

        if (imageResId != null) {
            holder.imageView.setImageResource(imageResId)
        } else {
            holder.imageView.setImageDrawable(null)
        }



        holder.itemView.setOnClickListener {
            // 아이템 클릭 처리
            val isInMonth = day.isInMonth
            //(context as? DiaryActivity)?.onItemClick(holder.itemView, day.day, isInMonth)
            itemClickListener.onItemClick(holder.itemView, day)
        }
    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    fun setClickListener(calendarFragment: CalendarFragment) {
    //
    }

    fun updateData(newDayList: ArrayList<Day>) {
        dayList.clear()
        dayList.addAll(newDayList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView: TextView = itemView.findViewById(R.id.dayTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView) // imageView 뷰 추가
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, day: Day)
    }


}
