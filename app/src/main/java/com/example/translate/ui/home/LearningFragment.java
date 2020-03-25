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


    private TextView mTxtId;
    private TextView mTxtPhrase;
    private TextView mTxtCategory;
    private TextView mTxtLearned;
    private TextView mTxtChineseCharacter;



    private DatabaseHelper myDb;

    private int currentCardNumber = 0;
    ArrayList<String> categoryList = new ArrayList<>();


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
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
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

//        translater.configure().translate("再见")
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

        mTxtChineseCharacter = view.findViewById(R.id.txtChineseCharacter);

//        myDb.insertData("phrase", "category", true);
//        myDb.insertData("phrase", "category", true);
//        myDb.insertData("phrase", "category", true);
        myDb.insertData("one", "numbers", true);
        myDb.insertData("two", "numbers", true);
        myDb.insertData("three", "numbers", true);
        myDb.insertData("four", "numbers", true);
        myDb.insertData("seven", "numbers", false);
        myDb.insertData("hello", "greetings", true);
        myDb.insertData("bye", "greetings", false);
        myDb.insertData("pizza", "food", true);
        myDb.insertData("sushi", "food", true);

        Cursor res = myDb.getCategory("numbers");

        StringBuffer buffer = new StringBuffer();

        while (res.moveToNext()) {
            buffer.append(res.getString(1));
            categoryList.add(res.getString(1));
            System.out.println(categoryList.size());
        }

        System.out.println(buffer.toString());

        mBtnAddValues.setOnClickListener(new View.OnClickListener() {        //adds sample data if the row exists
            @Override
            public void onClick(View view) {
                myDb.insertData("one", "numbers", true);
                myDb.insertData("two", "numbers", true);
                myDb.insertData("three", "numbers", true);
                myDb.insertData("four", "numbers", true);
                myDb.insertData("seven", "numbers", false);
                myDb.insertData("hello", "greetings", true);
                myDb.insertData("bye", "greetings", false);
                myDb.insertData("pizza", "food", true);
                myDb.insertData("sushi", "food", true);
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
                        buffer.append("\nPhrase: " + res.getString(1));
                        buffer.append("\nCategory: " + res.getString(2));
                        buffer.append("\nLearned: " + res.getString(3) + "\n\n");
                    }
                    showMessage("Data", buffer.toString());
                }
            }
        });

        mFabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentCardNumber < categoryList.size()) {
                    mTxtChineseCharacter.setText(categoryList.get(currentCardNumber));
                    System.out.println(currentCardNumber);
                    currentCardNumber++;

                } else {
                    mTxtChineseCharacter.setText("You're Finished!");
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


        return view;
    }
}
