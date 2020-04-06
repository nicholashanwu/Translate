package com.example.translate.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.translate.DatabaseHelper;
import com.example.translate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class LearningFragment extends Fragment {

    private FloatingActionButton mFabSave;
    private FloatingActionButton mFabAnswer;

    private ProgressBar mProgressBar;

    private ImageButton mBtnBack;

    private TextView mTxtChineseCharacter;
    private TextView mTxtPinyin;
    private TextView mTxtProgress;
    private TextView mTxtLevelTitle;
    private TextView mTxtSavedMessage;
    private TextView mTxtAnswerMessage;
    private TextView mTxtUnsavedMessage;

    private Cursor res;

    private DatabaseHelper myDb;

    private int progressInt = 0;

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myDb = new DatabaseHelper(getContext());

        mFabAnswer = view.findViewById(R.id.fabAnswer);
        mFabSave = view.findViewById(R.id.fabSave);
        FloatingActionButton mFabDone = view.findViewById(R.id.fabDone);
        mProgressBar = view.findViewById(R.id.progressBar);
        mTxtProgress = view.findViewById(R.id.txtProgress);
        mTxtChineseCharacter = view.findViewById(R.id.txtChineseCharacter);
        mTxtPinyin = view.findViewById(R.id.txtPinyin);
        mTxtLevelTitle = view.findViewById(R.id.txtLevelTitle);
        mTxtSavedMessage = view.findViewById(R.id.txtSavedMessage);
        mTxtAnswerMessage = view.findViewById(R.id.txtAnswerMessage);
        mTxtUnsavedMessage = view.findViewById(R.id.txtUnsavedMessage);
        mBtnBack = view.findViewById(R.id.btnBack);

        final String learningType = getArguments().getString("learningType");

        res = getData(learningType);

        setTitle(learningType);
        setParameters(res);
        mTxtAnswerMessage.setVisibility(View.GONE);
        mTxtSavedMessage.setVisibility(View.GONE);
        mTxtUnsavedMessage.setVisibility(View.GONE);


        mFabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                res.move(1);
                mFabAnswer.setImageResource(R.drawable.outline_visibility_off_white_48);

                if (res.getPosition() < res.getCount()) {
                    mTxtChineseCharacter.setText(res.getString(2));
                    mTxtPinyin.setText(res.getString(3));

                    mTxtSavedMessage.setVisibility(View.GONE);
                    YoYo.with(Techniques.FadeOutDown).duration(300).playOn(mTxtSavedMessage);
                    mTxtUnsavedMessage.setVisibility(View.GONE);
                    YoYo.with(Techniques.FadeOutDown).duration(300).playOn(mTxtUnsavedMessage);
                    YoYo.with(Techniques.FadeOutDown).duration(300).playOn(mTxtAnswerMessage);

                    progressInt = (int) 100 * res.getPosition() / res.getCount();
                    mTxtProgress.setText((res.getPosition() + 1) + "/" + res.getCount());
                    mProgressBar.setProgress(progressInt, true);

                    if (res.getString(6).equals("1")) {
                        mFabSave.setImageResource(R.drawable.baseline_bookmark_white_48);
                    } else {
                        mFabSave.setImageResource(R.drawable.outline_bookmark_border_white_48);
                    }

                } else {
                    mTxtProgress.setText("");
                    mProgressBar.setProgress(100, true);

                    showMessage("You're Finished!", "You completed the " + learningType + " learning module!");
                    res.close();

                    if (learningType.equals("custom")) {
                        Navigation.findNavController(getView()).navigate(R.id.action_navigation_learning_to_navigation_my_list_fragment);
                    } else if (learningType.equals("saved") || learningType.equals("learned")) {
                        Navigation.findNavController(getView()).navigate(R.id.action_navigation_learning_to_navigation_profile);
                    } else {
                        Navigation.findNavController(getView()).navigate(R.id.action_navigation_learning_to_navigation_home);
                    }

                    mProgressBar.setProgress(0, true);
                }
            }
        });

        mFabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: FIX

                if (res.getString(6).equals("1")) {
                    myDb.updateSave(res.getString(1), false);

                    mTxtSavedMessage.setVisibility(View.GONE);
                    YoYo.with(Techniques.FadeOutDown).duration(300).playOn(mTxtSavedMessage);

                    mTxtUnsavedMessage.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInUp).duration(300).playOn(mTxtUnsavedMessage);

                    mFabSave.setImageResource(R.drawable.outline_bookmark_border_white_48);
                } else {
                    myDb.updateSave(res.getString(1), true);

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
                if (mTxtPinyin.getText().equals(res.getString(3))) {
                    mTxtPinyin.setText(res.getString(1));

                    mTxtAnswerMessage.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInUp).duration(300).playOn(mTxtAnswerMessage);

                    mFabAnswer.setImageResource(R.drawable.baseline_visibility_white_48);
                } else {
                    mTxtPinyin.setText(res.getString(3));

                    YoYo.with(Techniques.FadeOutDown).duration(300).playOn(mTxtAnswerMessage);

                    mFabAnswer.setImageResource(R.drawable.outline_visibility_off_white_48);
                }
            }
        });


        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBackConfirmation("Are you sure you want to exit?", "");
            }
        });

    }

    public void setParameters(Cursor res) {
        mProgressBar.setProgress(0, true);
        mTxtProgress.setText("1/" + res.getCount());
        res.moveToFirst();

        mTxtChineseCharacter.setText(res.getString(2));
        mTxtPinyin.setText(res.getString(3));

        if (res.getString(6).equals("1")) {
            mFabSave.setImageResource(R.drawable.baseline_bookmark_white_48);
        } else {
            mFabSave.setImageResource(R.drawable.outline_bookmark_border_white_48);
        }
    }

    public void setTitle(String learningType) {
        if (learningType.equals("numbers")) {
            mTxtLevelTitle.setText("Numbers");
        } else if (learningType.equals("essentials")) {
            mTxtLevelTitle.setText("Essentials");
        } else if (learningType.equals("food")) {
            mTxtLevelTitle.setText("Food");
        } else if (learningType.equals("help")) {
            mTxtLevelTitle.setText("Help");
        } else if (learningType.equals("saved")) {
            mTxtLevelTitle.setText("Saved Words");
        } else if (learningType.equals("learned")) {
            mTxtLevelTitle.setText("Mastered Words");
        } else if (learningType.equals("custom")) {
            mTxtLevelTitle.setText("Your Words");
        }
    }

    public Cursor getData(String learningType) {
        Cursor res;
        if (learningType.equals("saved")) {
            res = myDb.getSaved();
        } else if (learningType.equals("learned")) {
            res = myDb.getLearned();
        } else {
            res = myDb.getCategory(learningType);
        }

        return res;
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void showBackConfirmation(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.Green));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_alert_dialog_learning, null);
        TextView txtTitle = view.findViewById(R.id.title);
        ImageButton imageButton = view.findViewById(R.id.image);

        imageButton.setImageResource(R.mipmap.over_30);


        builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_learning_to_navigation_home);
                res.close();
            }
        });


        txtTitle.setText(title);
        builder.setView(view);
        builder.show();

    }

}
