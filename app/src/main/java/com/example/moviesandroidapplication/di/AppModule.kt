package com.example.moviesandroidapplication.di


import android.content.Context
import androidx.room.Room
import com.example.moviesandroidapplication.database.MovieDao
import com.example.moviesandroidapplication.database.MovieDatabase
import com.example.moviesandroidapplication.repository.LocalRepository
import com.example.moviesandroidapplication.repository.ServerRepository
import com.example.moviesandroidapplication.services.ApiService
import com.example.moviesandroidapplication.ui.adapter.MoviesListAdapter
import com.example.moviesandroidapplication.utils.Constants.BASE_API_URL
import com.example.moviesandroidapplication.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /****************************  API Dependency *************************************/

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_API_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun providesServerRepository(apiService: ApiService) = ServerRepository(apiService)

    /****************************  Room Database Dependency *************************************/

    @Provides
    @Singleton
    fun providesMoviesDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        MovieDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun providesMoviesDao(db: MovieDatabase) = db.getMovieDao()

    @Provides
    @Singleton
    fun providesLocalRepository(dao: MovieDao) = LocalRepository(dao)


}