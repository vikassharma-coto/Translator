package com.usbrous.trans.feature_translate.di

import com.usbrous.trans.feature_translate.data.repository.TranslationRepositoryImpl
import com.usbrous.trans.feature_translate.data.source.MlKitTranslationDataSource
import com.usbrous.trans.feature_translate.data.source.TranslationDataSource
import com.usbrous.trans.feature_translate.domain.repository.TranslationRepository
import com.usbrous.trans.feature_translate.domain.usecase.TranslateTextUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TranslateModule {

    @Provides
    @Singleton
    fun provideMlKitTranslationDataSource(): MlKitTranslationDataSource {
        return MlKitTranslationDataSource()
    }

    @Provides
    @Singleton
    fun provideTranslationDataSource(
        mlKitDataSource: MlKitTranslationDataSource
    ): TranslationDataSource {
        return mlKitDataSource
    }

    @Provides
    @Singleton
    fun provideTranslationRepository(
        dataSource: TranslationDataSource
    ): TranslationRepository {
        return TranslationRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideTranslateTextUseCase(
        repository: TranslationRepository
    ): TranslateTextUseCase {
        return TranslateTextUseCase(repository)
    }
}
