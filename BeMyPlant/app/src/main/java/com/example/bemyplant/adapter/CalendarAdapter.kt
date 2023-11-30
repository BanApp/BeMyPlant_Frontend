package com.example.bemyplant.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bemyplant.Day
import com.example.bemyplant.R
import com.example.bemyplant.model.DiaryRealmManager
import com.example.bemyplant.module.DiaryModule
import io.realm.Realm
import io.realm.RealmConfiguration

class CalendarAdapter(
    private val context: Context,
    private val dayList: ArrayList<Day>,
    private val itemClickListener: ItemClickListener,
    //private val dateImageMap: HashMap<String, Int> //더미 데이터 - 키가 날짜, 값이 이미지인 맵 (추후 DB의 데이터로 변환 필요)
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    private lateinit var diaryRealmManager: DiaryRealmManager
    lateinit var realm: Realm

    /*init {
        val realm = Realm.getDefaultInstance()
        diaryRealmManager = DiaryRealmManager(realm)
    }
*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val configDiary : RealmConfiguration = RealmConfiguration.Builder()
            .name("diary.realm") // 생성할 realm 파일 이름 지정
            .deleteRealmIfMigrationNeeded()
            .modules(DiaryModule())
            .allowWritesOnUiThread(true) // sdhan : UI thread에서 realm에 접근할수 있게 허용
            .build()
        realm = Realm.getInstance(configDiary)
        diaryRealmManager = DiaryRealmManager(realm)
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
        val date = "${day.year}/${String.format("%02d", day.month)}/${String.format("%02d", day.day)}"

        // DB조회: DB에서 해당하는 날짜에 대해 데이터가 있는지 조회
        val image = diaryRealmManager.find(date)?.Image // 해당 날짜에 맞는 이미지 가져옴
        // db의 bitarrary -> bitmap
        if (image != null){
            holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image!!.size))
            Log.d("calendar", "calendar adapter.kt: db 조회 후 imageView에 그림 그리기 성공")
        }
        Log.d("calendar", "calendar adapter.kt: db 조회성공")

        holder.itemView.setOnClickListener {
            // 아이템 클릭 처리
            //val isInMonth = day.isInMonth
            itemClickListener.onItemClick(holder.itemView, day)
        }
    }

    override fun getItemCount(): Int {
        return dayList.size
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
