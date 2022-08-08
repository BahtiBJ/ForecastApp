package com.bbj.myapplication.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bbj.myapplication.R
import com.bbj.myapplication.data.SharedPreferenceClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class CityListActivity : AppCompatActivity() {

    private val listOfCty : ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerListOfCity = findViewById<RecyclerView>(R.id.listOfCity)
        val adapter = CityListAdapter(this, object : RecyclerItemClickListener {
            override fun onClick(value: String, position: Int) {
                SharedPreferenceClient(this@CityListActivity).setCityName(value)
                setResult(Activity.RESULT_OK)
                finish()
            }
        })

        val dividerItemDecoration = DividerItemDecoration(this,RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.list_divider,theme))
        recyclerListOfCity.addItemDecoration(dividerItemDecoration)

        lifecycleScope.launch {
            launch() {
                fillCityList()
            }.join()
            withContext(Dispatchers.Main){
                adapter.cityList = listOfCty
                recyclerListOfCity.adapter = adapter
            }

        }

    }

    private fun fillCityList() {
        val reader = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.cities)))
        var tempString : String? = ""
        while (tempString != null){
            tempString = reader.readLine()
            if (tempString != null){
                listOfCty.add(tempString)
            }
        }
        reader.close()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

}