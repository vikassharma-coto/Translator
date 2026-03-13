package com.usbrous.trans.feature_translate.di;

import com.usbrous.trans.feature_translate.domain.repository.TranslationRepository;
import com.usbrous.trans.feature_translate.domain.usecase.TranslateTextUseCase;
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
public final class TranslateModule_ProvideTranslateTextUseCaseFactory implements Factory<TranslateTextUseCase> {
  private final Provider<TranslationRepository> repositoryProvider;

  private TranslateModule_ProvideTranslateTextUseCaseFactory(
      Provider<TranslationRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public TranslateTextUseCase get() {
    return provideTranslateTextUseCase(repositoryProvider.get());
  }

  public static TranslateModule_ProvideTranslateTextUseCaseFactory create(
      Provider<TranslationRepository> repositoryProvider) {
    return new TranslateModule_ProvideTranslateTextUseCaseFactory(repositoryProvider);
  }

  public static TranslateTextUseCase provideTranslateTextUseCase(TranslationRepository repository) {
    return Preconditions.checkNotNullFromProvides(TranslateModule.INSTANCE.provideTranslateTextUseCase(repository));
  }
}
