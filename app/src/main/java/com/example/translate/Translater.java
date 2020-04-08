package com.example.translate;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateRemoteModel;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import androidx.annotation.NonNull;

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



	public void deleteModel() {
		FirebaseTranslateRemoteModel deModel =
				new FirebaseTranslateRemoteModel.Builder(FirebaseTranslateLanguage.ZH).build();
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

	public FirebaseTranslateRemoteModel downloadModel() {
		FirebaseModelManager modelManager = FirebaseModelManager.getInstance();

		FirebaseTranslateRemoteModel cnModel =
				new FirebaseTranslateRemoteModel.Builder(FirebaseTranslateLanguage.ZH).build();
		FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
				.requireWifi()
				.build();
		modelManager.download(cnModel, conditions)
				.addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void v) {
						// Model downloaded.
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						// Error.
					}
				});

		return cnModel;
	}




}
