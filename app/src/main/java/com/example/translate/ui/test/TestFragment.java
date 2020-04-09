package com.example.translate.ui.test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.translate.DatabaseHelper;
import com.example.translate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class TestFragment extends Fragment {

    private TextView mTxtChineseCharacter;
    private TextView mTxtLevelTitle;
    private TextView mTxtMessage;
    private ImageView mIvReaction;

    private RadioGroup mRbGroup;
    private RadioButton mRbAnswerOne;
    private RadioButton mRbAnswerTwo;
    private RadioButton mRbAnswerThree;
    private ProgressBar mProgressBar;
    private TextView mTxtProgress;
    private TextView mTxtScore;
    private TextView mTxtTimer;
    private FloatingActionButton mFabSubmit;

    private ImageButton mBtnBack;

    private DatabaseHelper myDb;

    private Cursor res;

    private int percentage;
    private int score = 0;
    private int learned = 0;
    private int mastered = 0;
    private int forgotten = 0;
    private int wrongStreak = 0;
    private int rightStreak = 0;

    public boolean answered;
    public boolean timedOut;

    private int answerIndex;
    private static final long START_TIME_IN_MILLIS = 11000;

    private TextView mTextViewCountDown;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private String testingType;


    private ArrayList<String> answerList = new ArrayList<>();

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


        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myDb = new DatabaseHelper(getContext());

        mProgressBar = view.findViewById(R.id.pbTest);
        mTxtProgress = view.findViewById(R.id.txtProgress);
        mTxtChineseCharacter = view.findViewById(R.id.txtChineseCharacter);
        mTxtLevelTitle = view.findViewById(R.id.txtLevelTitle);
        mTxtMessage = view.findViewById(R.id.txtMessage);

        mRbGroup = view.findViewById(R.id.rbGroup);
        mRbAnswerOne = view.findViewById(R.id.rbAnswerOne);
        mRbAnswerTwo = view.findViewById(R.id.rbAnswerTwo);
        mRbAnswerThree = view.findViewById(R.id.rbAnswerThree);
        mFabSubmit = view.findViewById(R.id.fabSubmitAnswer);
        mTxtScore = view.findViewById(R.id.txtScore);
        mIvReaction = view.findViewById(R.id.ivReaction);
        mTxtTimer = view.findViewById(R.id.txtTimer);
        mBtnBack = view.findViewById(R.id.btnBack);

        testingType = getArguments().getString("testingType");

        res = getData(testingType);

        getData(testingType);
        setTitle(testingType);
        setParameters(res);

        mTxtMessage.setText("");


        showNextQuestion(res);

        /////////////
        mTextViewCountDown = view.findViewById(R.id.txtTimer);

        updateCountDownText();


        mFabSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!answered) {
                    if (mTimerRunning && (mRbAnswerOne.isChecked() || mRbAnswerTwo.isChecked() || mRbAnswerThree.isChecked())) {

                        checkAnswer(answerIndex, res);
                        pauseTimer();
                    } else if (mTimerRunning) {
                        mTxtMessage.setText("Please choose an answer");
                        YoYo.with(Techniques.FadeInDown).duration(300).playOn(mTxtMessage);
                        YoYo.with(Techniques.FadeOutUp).duration(300).delay(700).playOn(mTxtMessage);
                    } else {
                        resetTimer();
                        showNextQuestion(res);
                        for (int i = 0; i < mRbGroup.getChildCount(); i++) {
                            mRbGroup.getChildAt(i).setEnabled(true);
                        }
                    }
                } else {
                    resetTimer();
                    showNextQuestion(res);
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

    private void showNextQuestion(Cursor res) {

        mRbGroup.clearCheck();

        if (res.getPosition() < res.getCount()) {

            mTxtChineseCharacter.setText(res.getString(2));

            double progressDouble = (double) 100 * res.getPosition() / res.getCount();
            int progressInt = (int) progressDouble;
            mTxtProgress.setText((res.getPosition() + 1) + "/" + res.getCount());
            mProgressBar.setProgress(progressInt, true);

            answerList = (ArrayList<String>) getAnswerList(res.getPosition(), res).clone();

            if (mTimerRunning) {
                pauseTimer();
            } else {
                startTimer(answerIndex, res);
            }


            mRbAnswerOne.setText(answerList.get(0));
            mRbAnswerTwo.setText(answerList.get(1));
            mRbAnswerThree.setText(answerList.get(2));
            mRbAnswerOne.setTextColor(Color.parseColor("#444444"));
            mRbAnswerTwo.setTextColor(Color.parseColor("#444444"));
            mRbAnswerThree.setTextColor(Color.parseColor("#444444"));

            answered = false;


        } else {
            finishTest(res);
        }

    }

    private void checkAnswer(int answerIndex, Cursor res) {
        answered = true;
        RadioButton rbSelected = getView().findViewById(mRbGroup.getCheckedRadioButtonId());
        int answerNum = mRbGroup.indexOfChild(rbSelected) + 1;

        if (answerNum == answerIndex) {

            showReaction(true);
            rightStreak++;
            wrongStreak = 0;
            score++;
            mTxtScore.setText("Score: " + Integer.toString(score));
            System.out.println(res.getString(5));

            if (res.getString(5).equals("1")) {
                mastered++;
                myDb.updateLearned(res.getString(1), true);
            } else {
                learned++;
                myDb.updateLearned(res.getString(1), true);
            }

            if (rightStreak == 3) {
                if (myDb.progressAchievement("Oh Baby a Triple!")) {
                    showAchievement("Oh Baby a Triple!");
                }
            } else if (rightStreak == 5) {
                if (myDb.progressAchievement("Pentakill!")) {
                    showAchievement("Pentakill!");
                }
            }


        } else {

            wrongStreak++;
            rightStreak = 0;
            showReaction(false);

            if (res.getPosition() == 0) {
                if (myDb.progressAchievement("Off to a Great Start")) {
                    showAchievement("Off to a Great Start");
                }
            }
            if (res.getString(5).equals("1")) {
                forgotten++;
                myDb.updateLearned(res.getString(1), false);
            }

            if (wrongStreak == 3) {
                if (myDb.progressAchievement("Abort?")) {
                    showAchievement("Abort?");
                }
            } else if (wrongStreak == 5) {
                if (myDb.progressAchievement("Abandon Ship!")) {
                    showAchievement("Abandon Ship!");
                }
            }



        }
        showSolution(answerIndex, res);
    }

    private void showSolution(int answerIndex, Cursor res) {
        RadioButton rbSelected = getView().findViewById(mRbGroup.getCheckedRadioButtonId());

        mRbAnswerOne.setTextColor(getResources().getColor(R.color.colorRed));
        mRbAnswerTwo.setTextColor(getResources().getColor(R.color.colorRed));
        mRbAnswerThree.setTextColor(getResources().getColor(R.color.colorRed));

        switch (answerIndex) {
            case 1:
                mRbAnswerOne.setTextColor(getResources().getColor(R.color.colorGreen));
                mRbAnswerTwo.setTextColor(getResources().getColor(R.color.colorRed));
                mRbAnswerThree.setTextColor(getResources().getColor(R.color.colorRed));
                break;
            case 2:
                mRbAnswerTwo.setTextColor(getResources().getColor(R.color.colorGreen));
                mRbAnswerOne.setTextColor(getResources().getColor(R.color.colorRed));
                mRbAnswerThree.setTextColor(getResources().getColor(R.color.colorRed));
                break;
            case 3:
                mRbAnswerThree.setTextColor(getResources().getColor(R.color.colorGreen));
                mRbAnswerOne.setTextColor(getResources().getColor(R.color.colorRed));
                mRbAnswerTwo.setTextColor(getResources().getColor(R.color.colorRed));
                break;

        }
        res.move(1);
    }

    private void finishTest(Cursor res) {
        mTxtProgress.setText("");
        mProgressBar.setProgress(99, true);
        percentage = 100 * score / res.getCount();


        System.out.println(percentage);

        if (testingType.equals("numbers")) {
            if (myDb.progressAchievement("Number Cruncher")) {
                showAchievement("Number Cruncher");
                if (myDb.progressAchievement("Lingo Lord")) {
                    showAchievement("Lingo Lord");
                }
            }
        } else if (testingType.equals("essentials")) {
            if (myDb.progressAchievement("The Nice Guy")) {
                showAchievement("The Nice Guy");
                if (myDb.progressAchievement("Lingo Lord")) {
                    showAchievement("Lingo Lord");
                }
            }
        } else if (testingType.equals("food")) {
            if (myDb.progressAchievement("Shef")) {
                showAchievement("Shef");
                if (myDb.progressAchievement("Lingo Lord")) {
                    showAchievement("Lingo Lord");
                }
            }
        } else if (testingType.equals("help")) {
            if (myDb.progressAchievement("Public Service")) {
                showAchievement("Public Service");
                if (myDb.progressAchievement("Lingo Lord")) {
                    showAchievement("Lingo Lord");
                }
            }
        }


        if (percentage == 100) {
            if (myDb.progressAchievement("Lingo Legend")) {
                showAchievement("Lingo Legend");
            }
        } else if (percentage >= 90 && percentage < 100) {
            if (myDb.progressAchievement("Nice Nine")) {
                showAchievement("Nice Nine");
            }
        } else if (percentage >= 80 && percentage < 90) {
            if (myDb.progressAchievement("Excellent Eight")) {
                showAchievement("Excellent Eight");
            }
        } else if (percentage >= 70 && percentage < 80) {
            if (myDb.progressAchievement("Sensational Seven")) {
                showAchievement("Sensational Seven");
            }
        } else if (percentage >= 60 && percentage < 70) {
            if (myDb.progressAchievement("Sexy Six")) {
                showAchievement("Sexy Six");
            }
        } else if (percentage < 30) {
            if (myDb.progressAchievement("Did you even try?")) {
                showAchievement("Did you even try?");
            }
        } else {

        }

        myDb.updateScore("Tests Taken");

        showMessage("You're Finished!");
        res.close();
    }

    private ArrayList<String> getAnswerList(int currentCardNumber, Cursor res) {
        answerList.clear();
        answerList.add(res.getString(1));
        boolean unique = false;
        int numOne = (int) (Math.random() * res.getCount());
        int numTwo = (int) (Math.random() * res.getCount());

        while (!unique) {
            numOne = (int) (Math.random() * res.getCount());
            numTwo = (int) (Math.random() * res.getCount());
            if (currentCardNumber == numOne) {
                numOne = (int) (Math.random() * res.getCount());
            } else if (currentCardNumber == numTwo) {
                numTwo = (int) (Math.random() * res.getCount());
            } else if (numOne == numTwo) {
                numTwo = (int) (Math.random() * res.getCount());
            } else {
                unique = true;
            }

        }
        currentCardNumber = res.getPosition();

        res.moveToPosition(numOne);
        answerList.add(res.getString(1));
        res.moveToPosition(numTwo);
        answerList.add(res.getString(1));
        res.moveToPosition(currentCardNumber);

        Collections.shuffle(answerList);

        answerIndex = answerList.indexOf(res.getString(1)) + 1;
        return answerList;
    }

    public Cursor getData(String testingType) {
        Cursor res;
        if (testingType.equals("saved")) {
            res = myDb.getSaved();
        } else if (testingType.equals("learned")) {
            res = myDb.getLearned();
        } else {
            res = myDb.getCategory(testingType);
        }

        return res;

    }

    private void showMessage(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.Red));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_alert_dialog, null);
        TextView txtTitle = view.findViewById(R.id.title);
        ImageButton imageButton = view.findViewById(R.id.image);
        TextView mTxtMessage = view.findViewById(R.id.message);
        TextView mTxtLearned = view.findViewById(R.id.txtLearned);
        TextView mTxtMastered = view.findViewById(R.id.txtMastered);
        TextView mTxtForgot = view.findViewById(R.id.txtForgot);

        if (percentage > 85) {
            myDb.updateScore("HD");
            imageButton.setImageResource(R.mipmap.over_95);
        } else if (percentage > 75) {
            myDb.updateScore("D");
            imageButton.setImageResource(R.mipmap.over_75);
        } else if (percentage > 65) {
            myDb.updateScore("C");
            imageButton.setImageResource(R.mipmap.over_65);
        } else if (percentage > 50) {
            myDb.updateScore("P");
            imageButton.setImageResource(R.mipmap.over_50);
        } else if (percentage > 40) {
            myDb.updateScore("F");
            imageButton.setImageResource(R.mipmap.over_40);
        } else if (percentage > 30) {
            myDb.updateScore("F");
            imageButton.setImageResource(R.mipmap.over_30);
        } else {
            myDb.updateScore("F");
            imageButton.setImageResource(R.mipmap.under_30);
        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_test_to_navigation_test_home);
            }
        });

        txtTitle.setText(title);
        mTxtMessage.setText((int) percentage + "%");
        mTxtLearned.setText("Learned " + learned + " new words!");
        mTxtMastered.setText("Mastered " + mastered + " words!");
        mTxtForgot.setText("Forgot " + forgotten + " words...");

        builder.setView(view);
        builder.show();
    }

    public void setTitle(String testingType) {
        if (testingType.equals("numbers")) {
            mTxtLevelTitle.setText("Numbers");
        } else if (testingType.equals("essentials")) {
            mTxtLevelTitle.setText("Essentials");
        } else if (testingType.equals("food")) {
            mTxtLevelTitle.setText("Food");
        } else {
            mTxtLevelTitle.setText("Help");
        }

    }

    public void setParameters(Cursor res) {
        mProgressBar.setProgress(1);
        mTxtProgress.setText("1/" + res.getCount());
        res.moveToFirst();
        mIvReaction.setVisibility(View.GONE);

        mTxtChineseCharacter.setText(res.getString(2));
    }

    public void showReaction(boolean correct) {

        ArrayList<Integer> goodList = new ArrayList<>();
        goodList.add(R.mipmap.over_95);
        goodList.add(R.mipmap.over_75);
        goodList.add(R.mipmap.over_65);
        ArrayList<Integer> badList = new ArrayList<>();
        badList.add(R.mipmap.over_30);
        badList.add(R.mipmap.over_40);
        badList.add(R.mipmap.under_30);
        Collections.shuffle(goodList);
        Collections.shuffle(badList);

        if (correct) {
            mIvReaction.setImageResource(goodList.get(0));
            mIvReaction.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeInUp).duration(300).playOn(mIvReaction);
            YoYo.with(Techniques.FadeOutUp).duration(300).delay(700).playOn(mIvReaction);
            YoYo.with(Techniques.Bounce).duration(300).playOn(mRbGroup.getChildAt(answerIndex - 1));
        } else {
            mIvReaction.setImageResource(badList.get(0));
            mIvReaction.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeInUp).duration(300).playOn(mIvReaction);
            YoYo.with(Techniques.FadeOutUp).duration(300).delay(700).playOn(mIvReaction);
            YoYo.with(Techniques.Shake).duration(300).playOn(mRbGroup.getChildAt(answerIndex - 1));
        }

    }

    private void startTimer(final int answerIndex, final Cursor res) {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;

                for (int i = 0; i < mRbGroup.getChildCount(); i++) {
                    mRbGroup.getChildAt(i).setEnabled(false);
                }

                showReaction(false);

                showSolution(answerIndex, res);

            }
        }.start();

        mTimerRunning = true;

    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;

    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();

    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void showBackConfirmation(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.Red));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_alert_dialog_test, null);
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
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_test_to_navigation_test_home);
                pauseTimer();
                res.close();
            }
        });

        txtTitle.setText(title);
        builder.setView(view);
        builder.show();

    }

    private void showAchievement(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.Yellow));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_alert_dialog_achievement, null);
        TextView txtTitle = view.findViewById(R.id.title);
        ImageButton imageButton = view.findViewById(R.id.image);

        imageButton.setImageResource(R.mipmap.over_95);

        builder.setPositiveButton("AWESOME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }

        });

        txtTitle.setText(title);
        builder.setView(view);
        builder.show();
    }


}
