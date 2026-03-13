package com.usbrous.trans.feature_translate.di;

import com.usbrous.trans.feature_translate.data.source.TranslationDataSource;
import com.usbrous.trans.feature_translate.domain.repository.TranslationRepository;
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
public final class TranslateModule_ProvideTranslationRepositoryFactory implements Factory<TranslationRepository> {
  private final Provider<TranslationDataSource> dataSourceProvider;

  private TranslateModule_ProvideTranslationRepositoryFactory(
      Provider<TranslationDataSource> dataSourceProvider) {
    this.dataSourceProvider = dataSourceProvider;
  }

  @Override
  public TranslationRepository get() {
    return provideTranslationRepository(dataSourceProvider.get());
  }

  public static TranslateModule_ProvideTranslationRepositoryFactory create(
      Provider<TranslationDataSource> dataSourceProvider) {
    return new TranslateModule_ProvideTranslationRepositoryFactory(dataSourceProvider);
  }

  public static TranslationRepository provideTranslationRepository(
      TranslationDataSource dataSource) {
    return Preconditions.checkNotNullFromProvides(TranslateModule.INSTANCE.provideTranslationRepository(dataSource));
  }
}
