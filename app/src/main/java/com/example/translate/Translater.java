package com.example.translate;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateRemoteModel;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

public class Translater {

	public Translater() {

	}

	public FirebaseTranslator configure() {

		FirebaseTranslatorOptions options = new FirebaseTranslatorOptions
				.Builder()
				.setSourceLanguage(FirebaseTranslateLanguage.EN)
				.setTargetLanguage(FirebaseTranslateLanguage.ZH)
				.build();
		final FirebaseTranslator englishChineseTranslater =
				FirebaseNaturalLanguage.getInstance().getTranslator(options);
		return englishChineseTranslater;

	}

	public void checkModelExists(FirebaseTranslator englishChineseTranslater) {

		FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().requireWifi().build();
		englishChineseTranslater.downloadModelIfNeeded(conditions)
				.addOnSuccessListener(
						new OnSuccessListener<Void>() {
							@Override
							public void onSuccess(Void v) {
								System.out.println("MODEL DOWNLOADED SUCCESSFULLY");
								// Model downloaded successfully. Okay to start translating.
								// (Set a flag, unhide the translation UI, etc.)
							}
						})
				.addOnFailureListener(
						new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								System.out.println("MODEL COULD NOT BE DOWNLOADED");
								// Model couldn’t be downloaded or other internal error.
								// ...
							}
						});
	}

	public void translate(String text, FirebaseTranslator englishChineseTranslater) {

		englishChineseTranslater.translate(text)
				.addOnSuccessListener(
						new OnSuccessListener<String>() {
							@Override
							public void onSuccess(@NonNull String translatedText) {
								System.out.println(translatedText);

								// Translation successful.
							}
						})
				.addOnFailureListener(
						new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								System.out.println("ERROR");// Error.
								// ...
							}
						});

	}

	public void deleteModel() {
		FirebaseTranslateRemoteModel deModel =
				new FirebaseTranslateRemoteModel.Builder(FirebaseTranslateLanguage.DE).build();
		FirebaseModelManager modelManager = FirebaseModelManager.getInstance();
		modelManager.deleteDownloadedModel(deModel)
				.addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void v) {
						// Model deleted.
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						// Error.
					}
				});

	}


}
