package com.example.translate.ui.test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.translate.DatabaseHelper;
import com.example.translate.R;
import com.example.translate.Translater;
import com.example.translate.ui.Phrase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class TestFragment extends Fragment {

	private FloatingActionButton mFabSubmit;

	private TextView mTxtChineseCharacter;
	private TextView mTxtLevelTitle;
	private TextView mTxtMessage;

	private RadioGroup mRbGroup;
	private RadioButton mRbAnswerOne;
	private RadioButton mRbAnswerTwo;
	private RadioButton mRbAnswerThree;
	private TextView mTxtProgress;
	private ProgressBar mProgressBar;
	private TextView mTxtScore;

	private DatabaseHelper myDb;

	private String testingType;
	private int currentCardNumber = 0;
	private double progressDouble = 0;
	private int progressInt = 0;
	private int score = 0;
	private double percentage;
    private int learned = 0;
    private int mastered = 0;
    private int forgotten = 0;

	private boolean answered;
	private ArrayList<Phrase> phraseList = new ArrayList<>();
	private int answerIndex;

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

		testingType = getArguments().getString("testingType");
		getData(testingType);
		setTitle(testingType);
		setParameters();

		mTxtMessage.setText("");
		showNextQuestion();

		mFabSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!answered) {
					if (mRbAnswerOne.isChecked() || mRbAnswerTwo.isChecked() || mRbAnswerThree.isChecked()) {
						YoYo.with(Techniques.FadeOutUp).duration(300).playOn(mTxtMessage);
						checkAnswer(answerIndex);
					} else {
						mTxtMessage.setText("Please choose an answer");
						YoYo.with(Techniques.FadeInDown).duration(300).playOn(mTxtMessage);

					}
				} else {
					showNextQuestion();
				}
			}
		});


		// Set Text Progress Indicator and advance it

		return view;
	}
	//////////////////////////////////////////////////////////////////////////////////////

	public void setParameters() {
		mProgressBar.setProgress(1);
		mTxtProgress.setText((currentCardNumber + 1) + "/" + phraseList.size());

		mTxtChineseCharacter.setText(phraseList.get(currentCardNumber).getPhraseCn());

	}

	public void setTitle(String testingType) {
		if (testingType.equals("numbers")) {
			mTxtLevelTitle.setText("Level 1 : Numbers");
		} else if (testingType.equals("essentials")) {
			mTxtLevelTitle.setText("Level 2 : Essentials");
		} else if (testingType.equals("food")) {
			mTxtLevelTitle.setText("Level 3 : Food");
		} else {
			mTxtLevelTitle.setText("Level 4 : Help");
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
			phraseList.add(new Phrase(res.getString(0),
					res.getString(1),
					res.getString(2),
					res.getString(3),
					res.getString(4),
					res.getString(5),
					res.getString(6)));
		}
	}


	private void showMessage(String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_alert_dialog, null);
		TextView txtTitle = view.findViewById(R.id.title);
		ImageButton imageButton = view.findViewById(R.id.image);
        TextView mTxtMessage = view.findViewById(R.id.message);
        TextView mTxtLearned = view.findViewById(R.id.txtLearned);
        TextView mTxtMastered = view.findViewById(R.id.txtMastered);
        TextView mTxtForgot = view.findViewById(R.id.txtForgot);

		if (percentage > 85) {
			imageButton.setImageResource(R.mipmap.over_95);
		} else if (percentage > 75) {
			imageButton.setImageResource(R.mipmap.over_75);
		} else if (percentage > 65) {
			imageButton.setImageResource(R.mipmap.over_65);
		} else if (percentage > 50) {
			imageButton.setImageResource(R.mipmap.over_50);
		} else if (percentage > 40) {
			imageButton.setImageResource(R.mipmap.over_40);
		} else if (percentage > 30) {
			imageButton.setImageResource(R.mipmap.over_30);
		} else {
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
        mTxtLearned.setText("You learned " + learned + " new words!");
        mTxtMastered.setText("You mastered " + mastered + " words!");
        mTxtForgot.setText("You forgot " + forgotten + " words...");

		builder.setView(view);
		builder.show();
	}

	private void checkAnswer(int answerIndex) {
		answered = true;
		RadioButton rbSelected = getView().findViewById(mRbGroup.getCheckedRadioButtonId());
		int answerNum = mRbGroup.indexOfChild(rbSelected) + 1;
		if (answerNum == answerIndex) {
			YoYo.with(Techniques.Bounce).duration(500).playOn(mRbGroup.getChildAt(answerIndex - 1));
			score++;
			mTxtScore.setText(Integer.toString(score));

            if (myDb.getLearnedStatus(phraseList.get(currentCardNumber - 1).getPhraseEn()).equals("true")) {
                mastered++;
                myDb.updateLearned(phraseList.get(currentCardNumber - 1).getPhraseEn(), true);
            } else {
                learned++;
                myDb.updateLearned(phraseList.get(currentCardNumber - 1).getPhraseEn(), true);
            }

		} else {
			YoYo.with(Techniques.Shake).duration(500).playOn(mRbGroup.getChildAt(answerIndex - 1));
            myDb.updateLearned(phraseList.get(currentCardNumber - 1).getPhraseEn(), false);
            forgotten++;

		}
		showSolution(answerIndex);
	}

	private void showSolution(int answerIndex) {
		RadioButton rbSelected = getView().findViewById(mRbGroup.getCheckedRadioButtonId());
		//answerIndex = mRbGroup.indexOfChild(rbSelected) + 1;
		mRbAnswerOne.setTextColor(Color.RED);
		mRbAnswerTwo.setTextColor(Color.RED);
		mRbAnswerThree.setTextColor(Color.RED);

		switch (answerIndex) {
			case 1:
				mRbAnswerOne.setTextColor(Color.GREEN);
				break;
			case 2:
				mRbAnswerTwo.setTextColor(Color.GREEN);
				break;
			case 3:
				mRbAnswerThree.setTextColor(Color.GREEN);
				break;
		}


	}


	private void showNextQuestion() {

		mRbGroup.clearCheck();

		if (currentCardNumber < phraseList.size()) {

			mTxtChineseCharacter.setText(phraseList.get(currentCardNumber).getPhraseCn());

			progressDouble = (double) 100 * (currentCardNumber) / phraseList.size();
			progressInt = (int) progressDouble;
			mTxtProgress.setText((currentCardNumber + 1) + "/" + phraseList.size());
			mProgressBar.setProgress(progressInt, true);

			answerList = (ArrayList<String>) getAnswerList(currentCardNumber).clone();


			mRbAnswerOne.setText(answerList.get(0));
			mRbAnswerTwo.setText(answerList.get(1));
			mRbAnswerThree.setText(answerList.get(2));
			mRbAnswerOne.setTextColor(Color.parseColor("#444444"));
			mRbAnswerTwo.setTextColor(Color.parseColor("#444444"));
			mRbAnswerThree.setTextColor(Color.parseColor("#444444"));

			answered = false;
			currentCardNumber++;

		} else {
			finishTest();
		}

	}

	private void finishTest() {
		mTxtProgress.setText("");
		mProgressBar.setProgress(99, true);
        percentage = 100 * (double) score / phraseList.size();
        System.out.println(percentage);
        System.out.println(answerList.size());

		showMessage("You're Finished!");


    }

	private ArrayList<String> getAnswerList(int currentCardNumber) {
		answerList.clear();
		answerList.add(phraseList.get(currentCardNumber).getPhraseEn());
		boolean unique = false;
		int numOne = (int) (Math.random() * phraseList.size());
		int numTwo = (int) (Math.random() * phraseList.size());

		while (unique == false) {
			numOne = (int) (Math.random() * phraseList.size());
			numTwo = (int) (Math.random() * phraseList.size());
			if (currentCardNumber == numOne) {
				numOne = (int) (Math.random() * phraseList.size());
			} else if (currentCardNumber == numTwo) {
				numTwo = (int) (Math.random() * phraseList.size());
			} else if (numOne == numTwo) {
				numTwo = (int) (Math.random() * phraseList.size());
			} else {
				unique = true;
			}

		}
		answerList.add(phraseList.get(numOne).getPhraseEn());
		answerList.add(phraseList.get(numTwo).getPhraseEn());

		Collections.shuffle(answerList);

		answerIndex = answerList.indexOf(phraseList.get(currentCardNumber).getPhraseEn()) + 1;
		return answerList;
	}
}
