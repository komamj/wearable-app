package com.komamj.wearable.weather.data.source

import com.komamj.wearable.weather.data.source.local.LocalDataSource
import com.komamj.wearable.weather.data.source.remote.RemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : WeatherRepository {
}