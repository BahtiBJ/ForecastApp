package com.bbj.myapplication.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bbj.myapplication.R

class CityListAdapter(
    private val context: Context,
    val recyclerItemClickListener: RecyclerItemClickListener
) : RecyclerView.Adapter<CityListAdapter.ViewHolder>() {

    var cityList: ArrayList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_city_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cityName.text = cityList[position].split("/")[1]
        holder.itemView.setOnClickListener {
            recyclerItemClickListener.onClick(cityList[position],position)
        }
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityName: TextView = itemView.findViewById(R.id.cityItem)
    }
}