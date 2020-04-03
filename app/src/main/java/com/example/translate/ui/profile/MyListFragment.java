package com.example.translate.ui.profile;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.translate.DatabaseHelper;
import com.example.translate.Phrase;
import com.example.translate.R;
import com.example.translate.Translater;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import name.pilgr.pipinyin.PiPinyin;

public class MyListFragment extends Fragment {

	private TextInputLayout mTextInputWord;
	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private CircleImageView mBtnProfileImageMyList;
	private ArrayList<Phrase> customPhraseList = new ArrayList<Phrase>();

	private FloatingActionButton mFabAddWord;
	private FloatingActionButton mFabDeleteAll;
	private FloatingActionButton mFabDelete;
	private FloatingActionButton mFabLearn;

	public MyListFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_my_list, container, false);

		DatabaseHelper myDb = new DatabaseHelper(getActivity());

		Cursor res = myDb.getCategory("custom");
		if (res.getCount() == 0) {
			showMessage("No Data", "You haven't added any of your own words yet");
		} else {
			while (res.moveToNext()) {
				customPhraseList.add(new Phrase(
						res.getString(0),
						res.getString(1),
						res.getString(2),
						res.getString(3),
						res.getString(4),
						res.getString(5),
						res.getString(6)
				));
			}
		}

		mRecyclerView = view.findViewById(R.id.rvMyWords);
		mRecyclerView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(this.getContext());
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setNestedScrollingEnabled(false);
		mAdapter = new PhraseAdapter(customPhraseList);
		mRecyclerView.setAdapter(mAdapter);

		mTextInputWord = view.findViewById(R.id.text_input_phrase);
		mFabAddWord = view.findViewById(R.id.fabAddWord);
		mFabDeleteAll = view.findViewById(R.id.fabDeleteAll);
		mFabLearn = view.findViewById(R.id.fabLearn);

		mBtnProfileImageMyList = view.findViewById(R.id.btnProfileImageMyList);


		Picasso.get().load(R.mipmap.tzuyu).resize(240, 240).into(mBtnProfileImageMyList);

		mFabAddWord.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mTextInputWord.getEditText().getText().toString().trim().isEmpty()) {
					mTextInputWord.setError("field cannot be empty");
				} else if (mTextInputWord.getEditText().getText().toString().length() > 15) {
					mTextInputWord.setError("too many characters");
				} else {
					mTextInputWord.setError(null);
					translate(mTextInputWord.getEditText().getText().toString().trim());
//					mAdapter = new PhraseAdapter(customPhraseList);
//					mAdapter.notifyDataSetChanged();
//					mRecyclerView.setAdapter(mAdapter);
					System.out.println(customPhraseList.toString());


				}
			}
		});

		mFabLearn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putString("learningType", "custom");
				Navigation.findNavController(getView()).navigate(R.id.action_navigation_my_list_fragment_to_navigation_learning, bundle);

			}
		});

		mFabDeleteAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DatabaseHelper myDb = new DatabaseHelper(getActivity());
				myDb.clearMyList();
				customPhraseList.clear();
				mAdapter = new PhraseAdapter(customPhraseList);
				mRecyclerView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
			}
		});

		return view;
	}

	public void addWord(String phraseEn, String phraseCn) {
		DatabaseHelper myDb = new DatabaseHelper(getContext());
		PiPinyin piPinyin = new PiPinyin(getActivity());
		String phrasePinyin = piPinyin.toPinyin(phraseCn, " ");

		myDb.insertData(phraseEn, phraseCn, phrasePinyin, "custom", false, false);
		customPhraseList.add(new Phrase("1", phraseEn, phraseCn, phrasePinyin, "custom", "false", "false"));

		FragmentTransaction ftr = getFragmentManager().beginTransaction();
		ftr.detach(MyListFragment.this).attach(MyListFragment.this).commit();
		// TODO: SHOW A REFRESH ICON
	}

	public void translate(final String phrase) {
		Translater translater = new Translater();
		translater.checkModelExists(translater.configure());
		translater.configure().translate(phrase)
				.addOnSuccessListener(
						new OnSuccessListener<String>() {
							@Override
							public void onSuccess(@NonNull String translatedText) {
								addWord(phrase, translatedText);
							}
						})
				.addOnFailureListener(
						new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								System.out.println("ERROR");// Error.
							}
						});
	}

	private void showMessage(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle(title);
		builder.setMessage(message);
		builder.show();
	}

}
