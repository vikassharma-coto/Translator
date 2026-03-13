package com.usbrous.trans.feature_translate.di;

import com.usbrous.trans.feature_translate.data.source.MlKitTranslationDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class TranslateModule_ProvideMlKitTranslationDataSourceFactory implements Factory<MlKitTranslationDataSource> {
  @Override
  public MlKitTranslationDataSource get() {
    return provideMlKitTranslationDataSource();
  }

  public static TranslateModule_ProvideMlKitTranslationDataSourceFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MlKitTranslationDataSource provideMlKitTranslationDataSource() {
    return Preconditions.checkNotNullFromProvides(TranslateModule.INSTANCE.provideMlKitTranslationDataSource());
  }

  private static final class InstanceHolder {
    static final TranslateModule_ProvideMlKitTranslationDataSourceFactory INSTANCE = new TranslateModule_ProvideMlKitTranslationDataSourceFactory();
  }
}
