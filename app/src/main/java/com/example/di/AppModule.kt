package com.example.di

import androidx.room.Room
import com.example.data.local.SlopeWatchDatabase
import com.example.data.repository.SlopeWatchRepository
import com.example.data.repository.SlopeWatchRepositoryImpl
import com.example.ui.viewmodel.SlopeWatchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            SlopeWatchDatabase::class.java,
            SlopeWatchDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<SlopeWatchDatabase>().stationDao() }
    single { get<SlopeWatchDatabase>().telemetryDao() }
    single { get<SlopeWatchDatabase>().thresholdDao() }

    single<SlopeWatchRepository> {
        SlopeWatchRepositoryImpl(
            stationDao = get(),
            telemetryDao = get(),
            thresholdDao = get()
        )
    }

    viewModel {
        SlopeWatchViewModel(repository = get())
    }
}
