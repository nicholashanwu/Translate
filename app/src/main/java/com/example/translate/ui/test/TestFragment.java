package com.example.translate.ui.test;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.translate.DatabaseHelper;
import com.example.translate.R;
import com.example.translate.Translater;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class TestFragment extends Fragment {

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
    private TextView mTxtPinyin;
    private TextView mTxtProgress;
    private TextView mTxtLevelTitle;

    private DatabaseHelper myDb;

    private int currentCardNumber = 0;
    private double progressDouble = 0;
    private int progressInt = 0;

    ArrayList<String> categoryListEn = new ArrayList<>();
    ArrayList<String> categoryListCn = new ArrayList<>();
    ArrayList<String> categoryListPinyin = new ArrayList<>();

    public TestFragment() {
    }


    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);


        Translater translater = new Translater();
        translater.checkModelExists(translater.configure());
        //translate stuff here
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

        //myDb.dropTable();

        mFabAnswer = view.findViewById(R.id.fabAnswer);
        mFabSave = view.findViewById(R.id.fabSave);
        mFabDone = view.findViewById(R.id.fabDone);
        mBtnDropTable = view.findViewById(R.id.btnDropTable);
        mBtnAddValues = view.findViewById(R.id.btnAddValues);
        mBtnShowValues = view.findViewById(R.id.btnShowValues);
        mProgressBar = view.findViewById(R.id.progressBar);
        mTxtProgress = view.findViewById(R.id.txtProgress);
        mTxtChineseCharacter = view.findViewById(R.id.txtChineseCharacter);
        mTxtPinyin = view.findViewById(R.id.txtPinyin);
        mTxtLevelTitle = view.findViewById(R.id.txtLevelTitle);

        final String testingType = getArguments().getString("testingType");
        getData(testingType);
        setTitle(testingType);
        setParameters();

        // Set Text Progress Indicator and advance it



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
                        buffer.append("\nPhrasePinyin: " + res.getString(3));
                        buffer.append("\nCategory: " + res.getString(4));
                        buffer.append("\nLearned: " + res.getString(5));
                        buffer.append("\nSaved: " + res.getString(6) + "\n\n");
                    }
                    showMessage("Data", buffer.toString());
                }
            }
        });

        mBtnDropTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDb.dropTable();
            }
        });


        mFabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentCardNumber++;

                mFabAnswer.setImageResource(R.drawable.outline_visibility_off_white_48);

                if (currentCardNumber < categoryListEn.size()) {
                    mTxtChineseCharacter.setText(categoryListCn.get(currentCardNumber));
                    mTxtPinyin.setText(categoryListPinyin.get(currentCardNumber));

                    progressDouble = (double) 100 * (currentCardNumber) / categoryListCn.size();
                    progressInt = (int) progressDouble;
                    mTxtProgress.setText((currentCardNumber + 1) + "/" + categoryListCn.size());
                    mProgressBar.setProgress(progressInt, true);

                    Cursor res = myDb.getSaveStatus(categoryListEn.get(currentCardNumber));
                    while (res.moveToNext()) {
                        if (res.getInt(6) == 0) {
                            mFabSave.setImageResource(R.drawable.outline_bookmark_border_white_48);
                        } else {
                            mFabSave.setImageResource(R.drawable.baseline_bookmark_white_48);
                        }
                    }

                } else {
                    mTxtProgress.setText("");
                    mProgressBar.setProgress(100, true);

                    showMessage("You're Finished!", "You completed the " + testingType + " learning module!");

                    FragmentManager fm = getFragmentManager();

                    fm.popBackStack();
                    mProgressBar.setProgress(0, true);
                }
            }
        });

        mFabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDb.getSaveStatus(categoryListEn.get(currentCardNumber));
                while (res.moveToNext()) {
                    if (res.getInt(6) == 0) {
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
                if (mTxtPinyin.getText().equals(categoryListPinyin.get(currentCardNumber))) {
                    mTxtPinyin.setText(categoryListEn.get(currentCardNumber));
                    mFabAnswer.setImageResource(R.drawable.baseline_visibility_white_48);
                } else {
                    mTxtPinyin.setText(categoryListPinyin.get(currentCardNumber));
                    mFabAnswer.setImageResource(R.drawable.outline_visibility_off_white_48);
                }
            }
        });

        return view;
    }

    public void setParameters(){
        mProgressBar.setProgress(1);
        mTxtProgress.setText((currentCardNumber + 1) + "/" + categoryListCn.size());

        mTxtChineseCharacter.setText(categoryListCn.get(currentCardNumber));
        mTxtPinyin.setText(categoryListPinyin.get(currentCardNumber));

        Cursor res = myDb.getSaveStatus(categoryListEn.get(currentCardNumber));
        while (res.moveToNext()) {
            if (res.getInt(6) == 0) {
                System.out.println("notSaved");
                mFabSave.setImageResource(R.drawable.outline_bookmark_border_white_48);
            } else {
                System.out.println("Saved");
                mFabSave.setImageResource(R.drawable.baseline_bookmark_white_48);
            }
        }
    }

    public void setTitle(String learningType) {
        if (learningType.equals("numbers")) {
            mTxtLevelTitle.setText("Level 1 : Numbers");
        } else if (learningType.equals("essentials")) {
            mTxtLevelTitle.setText("Level 2 : Essentials");
        } else if (learningType.equals("food")) {
            mTxtLevelTitle.setText("Level 3 : Food");
        } else {
            mTxtLevelTitle.setText("Level 4: Help");
        }

    }

    public void getData(String testingType) {
        Cursor res;
        if (testingType.equals("saved")) {
            res = myDb.getSaved();
        } else if (testingType.equals("learned")) {
            res = myDb.getLearned();
        } else {
            res = myDb.getCategory(testingType);
        }

        while (res.moveToNext()) {
            categoryListEn.add(res.getString(1));
            categoryListCn.add(res.getString(2));
            categoryListPinyin.add(res.getString(3));

        }
    }


    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
