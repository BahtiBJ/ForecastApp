package com.bbj.myapplication.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextSwitcher
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bbj.myapplication.R
import com.bbj.myapplication.data.SharedPreferenceClient
import com.bbj.myapplication.data.WeatherModel
import com.bbj.myapplication.databinding.MainForecastFragmentBinding
import com.bbj.myapplication.util.Constants
import com.bbj.myapplication.util.DensityUtil
import com.bbj.myapplication.util.NameLanguage
import java.util.*

class MainForecastFragment : Fragment() {

    private var _binding: MainForecastFragmentBinding? = null
    private val binding get() = _binding!!

    private var liveForecast : LiveData<WeatherModel>? = null
    val distance by lazy { DensityUtil.dip2px(requireContext(), 114f).toFloat() }

    val prefClient by lazy {SharedPreferenceClient(requireContext())}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainForecastFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            setAnimToTextSwitcher(temp,feelsLike,description)
        }

        binding.mainScrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                when(scrollY){
                    in 0..distance.toInt() -> {
                        val n = ((distance - scrollY)/distance)
                        binding.temp.alpha = n
                        binding.temp.scaleX = 0.6f + n*0.4f
                        binding.temp.scaleY =0.6f + n*0.4f
                    }
                    in distance.toInt() ..(2*distance.toInt() ) -> {
                        val n = ((2*distance - scrollY)/(distance))
                        binding.feelsLike.alpha = n
                        binding.feelsLike.scaleX = 0.6f + n*0.4f
                        binding.feelsLike.scaleY =0.6f + n*0.4f
                    }
                    in (2*distance.toInt() )..(3*distance.toInt() ) -> {
                        val n = ((3*distance - scrollY)/(distance))
                        binding.description.alpha = n
                        binding.description.scaleX = 0.6f + n*0.4f
                        binding.description.scaleY =0.6f + n*0.4f
                    }
                }
            }
        })

        val viewModel : MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        liveForecast = viewModel.liveForecast
        liveForecast?.observe(viewLifecycleOwner){
            binding.run {
                city.text = prefClient.getCityName(NameLanguage.RU)
                temp.setText(getString(R.string.temp,it.temp.toString()))
                if (it.temp.toInt() == 404)
                    Toast.makeText(requireContext(),"Не удалось получить данные\nиз сети",Toast.LENGTH_LONG).show()
                feelsLike.setText(getString(R.string.feels_like,it.tempFeelsLike.toString()))
                description.setText(it.description.trim().uppercase(Locale.getDefault()))
                minTemp.text = getString(R.string.min_temp,it.tempMin.toString())
                maxTemp.text = getString(R.string.max_temp,it.tempMax.toString())
                humidity.text = getString(R.string.humidity,it.humidity.toString())
                windSpeed.text = getString(R.string.wind_speed,it.windSpeed.toString())
                pressure.text = getString(R.string.pressure,it.pressure.toString())
            }
        }

        viewModel.getTodayForecast()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setAnimToTextSwitcher(vararg textSwitchers: TextSwitcher){
        for (textSwitcher in textSwitchers){
            textSwitcher.setInAnimation(requireContext(),R.anim.translate_up_with_fade_in)
            textSwitcher.setOutAnimation(requireContext(),R.anim.translate_up_with_fade_out)
        }
    }



}