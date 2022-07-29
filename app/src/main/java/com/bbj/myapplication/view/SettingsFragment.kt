package com.bbj.myapplication.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bbj.myapplication.R


class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_city_pick, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel : MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == Activity.RESULT_OK){
                Handler(Looper.getMainLooper()).postDelayed({
                    viewModel.getTodayForecast()
                },1000)
            }
            (requireActivity() as MainActivity).returnToFirstPage()
        }

        val chooseCityButton = view.findViewById<Button>(R.id.chooseCity)
        chooseCityButton.setOnClickListener{
            val intent = Intent(requireActivity(),CityListActivity::class.java)
            launcher.launch(intent)
            requireActivity().overridePendingTransition(R.anim.falling,R.anim.fadding)
        }
    }


}