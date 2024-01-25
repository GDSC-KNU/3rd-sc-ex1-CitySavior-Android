package com.citysavior.android.di

import com.citysavior.android.data.repository.achievement.AchievementRepositoryImpl
import com.citysavior.android.data.repository.auth.AuthRepositoryImpl
import com.citysavior.android.data.repository.auth.JwtTokenRepositoryImpl
import com.citysavior.android.data.repository.report.ReportRepositoryImpl
import com.citysavior.android.data.repository.user.UserRepositoryImpl
import com.citysavior.android.domain.repository.achievement.AchievementRepository
import com.citysavior.android.domain.repository.auth.AuthRepository
import com.citysavior.android.domain.repository.auth.JwtTokenRepository
import com.citysavior.android.domain.repository.report.ReportRepository
import com.citysavior.android.domain.repository.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Singleton
    @Binds
    abstract fun provideAchievementRepository(
        achievementRepositoryImpl: AchievementRepositoryImpl
    ) : AchievementRepository


    @Singleton
    @Binds
    abstract fun provideAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ) : AuthRepository


    @Singleton
    @Binds
    abstract fun provideJwtTokenRepository(
        jwtTokenRepositoryImpl: JwtTokenRepositoryImpl
    ) : JwtTokenRepository


    @Singleton
    @Binds
    abstract fun provideReportRepository(
        reportRepositoryImpl: ReportRepositoryImpl
    ) : ReportRepository

    @Singleton
    @Binds
    abstract fun providerUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ) : UserRepository
}