package com.example.translate.ui.home;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.translate.DatabaseHelper;
import com.example.translate.R;
import com.example.translate.Translater;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class LearningFragment extends Fragment {

    private FloatingActionButton mFabDone;
    private FloatingActionButton mFabSave;
    private FloatingActionButton mFabAnswer;
    private Button mBtnDropTable;
    private Button mBtnAddValues;
    private Button mBtnShowValues;
    private ProgressBar mProgressBar;
    private TextView mTxtId;
    private TextView mTxtPhrase;
    private TextView mTxtCategory;
    private TextView mTxtLearned;
    private TextView mTxtChineseCharacter;
    private TextView mTxtProgress;


    private DatabaseHelper myDb;

    private int currentCardNumber = 0;
    private double progressDouble = 0;
    private int progressInt = 0;
    ArrayList<String> categoryListEn = new ArrayList<>();
    ArrayList<String> categoryListCn = new ArrayList<>();

    public LearningFragment() {
        // Required empty public constructor
    }

    public static LearningFragment newInstance(String param1, String param2) {
        LearningFragment fragment = new LearningFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learning, container, false);

        Translater translater = new Translater();
        translater.checkModelExists(translater.configure());

//        translater.configure().translate("bye")
//                .addOnSuccessListener(
//                        new OnSuccessListener<String>() {
//                            @Override
//                            public void onSuccess(@NonNull String translatedText) {
//                                System.out.println(translatedText);
//
//                                // Translation successful.
//                            }
//                        })
//                .addOnFailureListener(
//                        new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                System.out.println("ERROR");// Error.
//                                // ...
//                            }
//                        });

        myDb = new DatabaseHelper(getContext());
        myDb.dropTable();

        mFabAnswer = view.findViewById(R.id.fabAnswer);
        mFabSave = view.findViewById(R.id.fabSave);
        mFabDone = view.findViewById(R.id.fabDone);
        mBtnDropTable = view.findViewById(R.id.btnDropTable);
        mBtnAddValues = view.findViewById(R.id.btnAddValues);
        mBtnShowValues = view.findViewById(R.id.btnShowValues);
        mProgressBar = view.findViewById(R.id.progressBar);
        mTxtProgress = view.findViewById(R.id.txtProgress);

        mTxtChineseCharacter = view.findViewById(R.id.txtChineseCharacter);

        myDb.insertData("one", "一", "numbers", true, false);
        myDb.insertData("two", "二", "numbers", true, false);
        myDb.insertData("three", "三", "numbers", true, false);
        myDb.insertData("four", "四", "numbers", true, false);
        myDb.insertData("seven", "七", "numbers", false, false);
        myDb.insertData("hello", "你好", "greetings", true, false);
        myDb.insertData("bye", "再见", "greetings", false, false);
        myDb.insertData("pizza", "比萨", "food", true, false);
        myDb.insertData("sushi", "寿司", "food", true, false);

        Cursor res = myDb.getCategory("numbers");

        while (res.moveToNext()) {
            categoryListEn.add(res.getString(1));
            categoryListCn.add(res.getString(2));                              //display English or Chinese
        }

        mProgressBar.setProgress(0);
        mTxtProgress.setText((currentCardNumber + 1) + "/" + categoryListCn.size());

        mTxtChineseCharacter.setText(categoryListCn.get(currentCardNumber));

        mBtnAddValues.setOnClickListener(new View.OnClickListener() {        //adds sample data if the row exists
            @Override
            public void onClick(View view) {
                myDb.insertData("one", "一", "numbers", true, false);
                myDb.insertData("two", "二", "numbers", true, false);
                myDb.insertData("three", "三", "numbers", true, false);
                myDb.insertData("four", "四", "numbers", true, false);
                myDb.insertData("seven", "七", "numbers", false, false);
                myDb.insertData("hello", "你好", "greetings", true, false);
                myDb.insertData("bye", "再见", "greetings", false, false);
                myDb.insertData("pizza", "比萨", "food", true, false);
                myDb.insertData("sushi", "寿司", "food", true, false);
            }
        });

        mBtnShowValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDb.getAllData();
                if (res.getCount() == 0) {
                    showMessage("Error", "Nothing found");
                } else {
                    StringBuffer buffer = new StringBuffer();
                    while (res.moveToNext()) {
                        buffer.append("Id: " + res.getString(0));
                        buffer.append("\nPhraseEn: " + res.getString(1));
                        buffer.append("\nPhraseCn: " + res.getString(2));
                        buffer.append("\nCategory: " + res.getString(3));
                        buffer.append("\nLearned: " + res.getString(4));
                        buffer.append("\nSaved: " + res.getString(5) + "\n\n");
                    }
                    showMessage("Data", buffer.toString());
                }
            }
        });

        mBtnDropTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //myDb.onUpgrade(, 1, 1);
                myDb.dropTable();

            }
        });

        mFabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentCardNumber++;
                if (currentCardNumber < categoryListEn.size()) {

                    mTxtChineseCharacter.setText(categoryListCn.get(currentCardNumber));
                    progressDouble = (double) 100 * (currentCardNumber)/categoryListCn.size();
                    progressInt = (int) progressDouble;
                    mTxtProgress.setText((currentCardNumber + 1) + "/" + categoryListCn.size());
                    mProgressBar.setProgress(progressInt, true);
                } else {
                    mTxtChineseCharacter.setText("You're Finished!");
                    mTxtProgress.setText("");
                    mProgressBar.setProgress(100, true);
                }
            }
        });

        mFabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDb.getSave(categoryListEn.get(currentCardNumber));
                while(res.moveToNext()){
                    if(res.getInt(5) == 0){
                        myDb.updateSave(categoryListEn.get(currentCardNumber), true);
                        mFabSave.setImageResource(R.drawable.baseline_bookmark_white_48);
                    } else {
                        myDb.updateSave(categoryListEn.get(currentCardNumber), false);
                        mFabSave.setImageResource(R.drawable.outline_bookmark_border_white_48);
                    }
                }
            }
        });

        mFabAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTxtChineseCharacter.getText().equals(categoryListCn.get(currentCardNumber))){
                    mTxtChineseCharacter.setText(categoryListEn.get(currentCardNumber));
                    mFabAnswer.setImageResource(R.drawable.baseline_visibility_white_48);
                } else {
                    mTxtChineseCharacter.setText(categoryListCn.get(currentCardNumber));
                    mFabAnswer.setImageResource(R.drawable.outline_visibility_off_white_48);
                }


                //show English version
            }
        });

        return view;
    }
}
