package com.usbrous.trans.feature_translate.presentation;

import com.usbrous.trans.feature_translate.domain.usecase.TranslateTextUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class TranslateViewModel_Factory implements Factory<TranslateViewModel> {
  private final Provider<TranslateTextUseCase> translateTextUseCaseProvider;

  private TranslateViewModel_Factory(Provider<TranslateTextUseCase> translateTextUseCaseProvider) {
    this.translateTextUseCaseProvider = translateTextUseCaseProvider;
  }

  @Override
  public TranslateViewModel get() {
    return newInstance(translateTextUseCaseProvider.get());
  }

  public static TranslateViewModel_Factory create(
      Provider<TranslateTextUseCase> translateTextUseCaseProvider) {
    return new TranslateViewModel_Factory(translateTextUseCaseProvider);
  }

  public static TranslateViewModel newInstance(TranslateTextUseCase translateTextUseCase) {
    return new TranslateViewModel(translateTextUseCase);
  }
}
