package com.bbj.myapplication.di

import android.content.Context
import androidx.room.Room
import com.bbj.myapplication.data.ForecastRepositoryImpl
import com.bbj.myapplication.data.database.DatabaseDAO
import com.bbj.myapplication.data.database.ForecastDataBase
import com.bbj.myapplication.data.retrofit.RetrofitDAO
import com.bbj.myapplication.domain.ForecastRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module(includes = [DBModule::class,RetrofitModule::class])
class AppModules {

    @Provides
    fun getForecastRepository(databaseDAO: DatabaseDAO,retrofitDAO: RetrofitDAO?) : ForecastRepository{
        return ForecastRepositoryImpl(databaseDAO,retrofitDAO)
    }

}

@Module
class DBModule {

    @Provides
    @Singleton
    fun getDataBase(context: Context): ForecastDataBase {
        return Room
            .databaseBuilder(context, ForecastDataBase::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun getDatabaseDAO(forecastDataBase: ForecastDataBase) : DatabaseDAO {
        return forecastDataBase.getDAO()
    }

}

@Module
class RetrofitModule {

    val baseUrl = "https://api.openweathermap.org/data/2.5/"

    @Provides
    @Singleton
    fun getRetrofit(gsonConverterFactory: GsonConverterFactory) : Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    fun getRetrofitDAO(retrofit: Retrofit) : RetrofitDAO{
        return retrofit.create(RetrofitDAO::class.java)
    }

    @Provides
    fun getGsonConverterFactory() : GsonConverterFactory{
        return GsonConverterFactory.create()
    }

}
