package com.usbrous.trans.feature_translate.di;

import com.usbrous.trans.feature_translate.data.source.MlKitTranslationDataSource;
import com.usbrous.trans.feature_translate.data.source.TranslationDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class TranslateModule_ProvideTranslationDataSourceFactory implements Factory<TranslationDataSource> {
  private final Provider<MlKitTranslationDataSource> mlKitDataSourceProvider;

  private TranslateModule_ProvideTranslationDataSourceFactory(
      Provider<MlKitTranslationDataSource> mlKitDataSourceProvider) {
    this.mlKitDataSourceProvider = mlKitDataSourceProvider;
  }

  @Override
  public TranslationDataSource get() {
    return provideTranslationDataSource(mlKitDataSourceProvider.get());
  }

  public static TranslateModule_ProvideTranslationDataSourceFactory create(
      Provider<MlKitTranslationDataSource> mlKitDataSourceProvider) {
    return new TranslateModule_ProvideTranslationDataSourceFactory(mlKitDataSourceProvider);
  }

  public static TranslationDataSource provideTranslationDataSource(
      MlKitTranslationDataSource mlKitDataSource) {
    return Preconditions.checkNotNullFromProvides(TranslateModule.INSTANCE.provideTranslationDataSource(mlKitDataSource));
  }
}
