package com.example.translate.ui.home;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.translate.DatabaseHelper;
import com.example.translate.R;
import com.example.translate.Translater;
import com.example.translate.ui.Phrase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class LearningFragment extends Fragment {

    private FloatingActionButton mFabDone;
    private FloatingActionButton mFabSave;
    private FloatingActionButton mFabAnswer;
    private Button mBtnDropTable;
    private Button mBtnShowValues;
    private ProgressBar mProgressBar;

    private TextView mTxtChineseCharacter;
    private TextView mTxtPinyin;
    private TextView mTxtProgress;
    private TextView mTxtLevelTitle;
    private TextView mTxtSavedMessage;
    private TextView mTxtAnswerMessage;
    private TextView mTxtUnsavedMessage;

    private DatabaseHelper myDb;

    private int currentCardNumber = 0;
    private double progressDouble = 0;
    private int progressInt = 0;

    private ArrayList<Phrase> phraseList = new ArrayList<>();

    public LearningFragment() {
    }

    public static LearningFragment newInstance(String param1, String param2) {
        LearningFragment fragment = new LearningFragment();
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

        View view = inflater.inflate(R.layout.fragment_learning, container, false);

        Translater translater = new Translater();
        translater.checkModelExists(translater.configure());
//        translate stuff here
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

        mFabAnswer = view.findViewById(R.id.fabAnswer);
        mFabSave = view.findViewById(R.id.fabSave);
        mFabDone = view.findViewById(R.id.fabDone);
        mBtnDropTable = view.findViewById(R.id.btnDropTable);

        mBtnShowValues = view.findViewById(R.id.btnShowValues);
        mProgressBar = view.findViewById(R.id.progressBar);
        mTxtProgress = view.findViewById(R.id.txtProgress);
        mTxtChineseCharacter = view.findViewById(R.id.txtChineseCharacter);
        mTxtPinyin = view.findViewById(R.id.txtPinyin);
        mTxtLevelTitle = view.findViewById(R.id.txtLevelTitle);
        mTxtSavedMessage = view.findViewById(R.id.txtSavedMessage);
        mTxtAnswerMessage = view.findViewById(R.id.txtAnswerMessage);
        mTxtUnsavedMessage = view.findViewById(R.id.txtUnsavedMessage);

        final String learningType = getArguments().getString("learningType");

        getData(learningType);
        setTitle(learningType);
        setParameters();
        mTxtAnswerMessage.setVisibility(View.GONE);
        mTxtSavedMessage.setVisibility(View.GONE);
        mTxtUnsavedMessage.setVisibility(View.GONE);


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

                if (currentCardNumber < phraseList.size()) {
                    mTxtChineseCharacter.setText(phraseList.get(currentCardNumber).getPhraseCn());
                    mTxtPinyin.setText(phraseList.get(currentCardNumber).getPinyin());

                    mTxtSavedMessage.setVisibility(View.GONE);
                    YoYo.with(Techniques.FadeOutDown).duration(300).playOn(mTxtSavedMessage);
                    mTxtUnsavedMessage.setVisibility(View.GONE);
                    YoYo.with(Techniques.FadeOutDown).duration(300).playOn(mTxtUnsavedMessage);
                    YoYo.with(Techniques.FadeOutDown).duration(300).playOn(mTxtAnswerMessage);

                    progressDouble = (double) 100 * (currentCardNumber) / phraseList.size();
                    progressInt = (int) progressDouble;
                    mTxtProgress.setText((currentCardNumber + 1) + "/" + phraseList.size());
                    mProgressBar.setProgress(progressInt, true);

                    if(phraseList.get(currentCardNumber).getSaved().equals("1")){
                        mFabSave.setImageResource(R.drawable.baseline_bookmark_white_48);
                        System.out.println("saved");
                    } else {
                        mFabSave.setImageResource(R.drawable.outline_bookmark_border_white_48);
                        System.out.println("not saved");
                    }
                } else {
                    mTxtProgress.setText("");
                    mProgressBar.setProgress(99, true);

                    showMessage("You're Finished!", "You completed the " + learningType + " learning module!");

                    Navigation.findNavController(getView()).navigate(R.id.action_navigation_learning_to_navigation_home);
//
                }
            }
        });

        mFabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phraseList.get(currentCardNumber).getSaved().equals("1")){
                    myDb.updateSave(phraseList.get(currentCardNumber).getId(), false);
                    phraseList.get(currentCardNumber).setSaved("0");

                    mTxtSavedMessage.setVisibility(View.GONE);
                    YoYo.with(Techniques.FadeOutDown).duration(300).playOn(mTxtSavedMessage);

                    mTxtUnsavedMessage.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInUp).duration(300).playOn(mTxtUnsavedMessage);


                    mFabSave.setImageResource(R.drawable.outline_bookmark_border_white_48);
                } else {
                    myDb.updateSave(phraseList.get(currentCardNumber).getId(), true);
                    phraseList.get(currentCardNumber).setSaved("1");

                    mTxtUnsavedMessage.setVisibility(View.GONE);
                    YoYo.with(Techniques.FadeOutDown).duration(300).playOn(mTxtUnsavedMessage);

                    mTxtSavedMessage.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInUp).duration(300).playOn(mTxtSavedMessage);



                    mFabSave.setImageResource(R.drawable.baseline_bookmark_white_48);
                }
            }
        });

        mFabAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTxtPinyin.getText().equals(phraseList.get(currentCardNumber).getPinyin())) {
                    mTxtPinyin.setText(phraseList.get(currentCardNumber).getPhraseEn());

                    mTxtAnswerMessage.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInUp).duration(300).playOn(mTxtAnswerMessage);

                    mFabAnswer.setImageResource(R.drawable.baseline_visibility_white_48);
                } else {
                    mTxtPinyin.setText(phraseList.get(currentCardNumber).getPinyin());


                    YoYo.with(Techniques.FadeOutDown).duration(300).playOn(mTxtAnswerMessage);
                    //mTxtAnswerMessage.setVisibility(View.GONE);

                    mFabAnswer.setImageResource(R.drawable.outline_visibility_off_white_48);
                }
            }
        });

        return view;
    }

    public void setParameters() {
        mProgressBar.setProgress(0, true);
        mTxtProgress.setText((currentCardNumber + 1) + "/" + phraseList.size());

        mTxtChineseCharacter.setText(phraseList.get(currentCardNumber).getPhraseCn());
        mTxtPinyin.setText(phraseList.get(currentCardNumber).getPinyin());

        if(phraseList.get(currentCardNumber).getSaved().equals("true")){
            mFabSave.setImageResource(R.drawable.baseline_bookmark_white_48);
        } else {
            mFabSave.setImageResource(R.drawable.outline_bookmark_border_white_48);
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
            mTxtLevelTitle.setText("Level 4 : Help");
        }

    }

    public void getData(String learningType) {
        Cursor res;
        if (learningType.equals("saved")) {
            res = myDb.getSaved();
        } else if (learningType.equals("learned")) {
            res = myDb.getLearned();
        } else {
            res = myDb.getCategory(learningType);
        }

        while (res.moveToNext()) {
            phraseList.add(new Phrase(res.getString(0),
                    res.getString(1),
                    res.getString(2),
                    res.getString(3),
                    res.getString(4),
                    res.getString(5),
                    res.getString(6)));
        }
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
