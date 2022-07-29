package com.bbj.myapplication.di

import android.content.Context
import com.bbj.myapplication.data.ForecastRepositoryImpl
import com.bbj.myapplication.data.WeatherModel
import com.bbj.myapplication.domain.GettingTodayForecastUseCase
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModules::class])
@Singleton
interface AppComponent {

    fun getForecastUseCase() : GettingTodayForecastUseCase

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun setContext(context : Context) : Builder

        fun build() : AppComponent

    }

}