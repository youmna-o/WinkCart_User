package com.example.winkcart_user.application.dependencyInjection


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.compose.ui.platform.LocalContext
import com.example.winkcart_user.data.local.LocalDataSource
import com.example.winkcart_user.data.local.LocalDataSourceImpl
import com.example.winkcart_user.data.local.settings.SettingsDao
import com.example.winkcart_user.data.local.settings.SettingsDaoImpl
import com.example.winkcart_user.data.remote.RemoteDataSource
import com.example.winkcart_user.data.remote.RemoteDataSourceImpl
import com.example.winkcart_user.data.remote.retrofit.CurrencyService
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_user.data.remote.retrofit.Services
import com.example.winkcart_user.data.repository.FirebaseRepo
import com.example.winkcart_user.data.repository.FirebaseRepoImp
import com.example.winkcart_user.data.repository.ProductRepo
import com.example.winkcart_user.data.repository.ProductRepoImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DependencyInjectionModule {

    @Module
    @InstallIn(SingletonComponent::class)
    object DependencyInjectionModule {

        @Provides
        @Singleton
        fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
            return context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        }

        @Provides
        @Singleton
        fun provideSettingsDao(sharedPreferences: SharedPreferences): SettingsDao {
            return SettingsDaoImpl(sharedPreferences)
        }

        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


//        @Provides
//        @Singleton
//        fun provideUserServices(): Services {
//            return RetrofitHelper.shopifyService
//        }
//        @Provides
//        @Singleton
//        fun provideCurrencyService(): CurrencyService {
//            return RetrofitHelper.currencyService
//        }

        @Provides
        @Singleton
        fun provideRemoteDataSource(userServices: RetrofitHelper): RemoteDataSource {
            return RemoteDataSourceImpl(userServices)
        }

        @Provides
        @Singleton
        fun provideLocalDataSource(settingsDao: SettingsDao): LocalDataSource {
            return LocalDataSourceImpl(settingsDao)
        }

        @Provides
        @Singleton
        fun provideProductRepo(remoteDataSource: RemoteDataSource , localDataSource: LocalDataSource): ProductRepo {
            return ProductRepoImpl(remoteDataSource , localDataSource)
        }
        @Provides
        @Singleton
        fun provideFirbaseRepo(remoteDataSource: RemoteDataSource ): FirebaseRepo {
            return FirebaseRepoImp(remoteDataSource )
        }
    }


}
