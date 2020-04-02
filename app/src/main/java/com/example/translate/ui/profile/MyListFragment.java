package com.example.translate.ui.profile;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.translate.DatabaseHelper;
import com.example.translate.R;
import com.example.translate.Translater;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import name.pilgr.pipinyin.PiPinyin;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyListFragment extends Fragment {

    private TextInputLayout mTextInputWord;
    private FloatingActionButton mBtnAddWord;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Phrase> customPhraseList = new ArrayList<Phrase>();

    public MyListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_list, container, false);

        mRecyclerView = view.findViewById(R.id.rvMyWords);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        //getWords

        DatabaseHelper myDb = new DatabaseHelper(getActivity());


        Cursor res = myDb.getCategory("custom");
        if (res.getCount() == 0) {
            showMessage("No Data", "You haven't added any of your own words yet");
        } else {
            while (res.moveToNext()) {
                customPhraseList.add(new Phrase(res.getString(1),
                        res.getString(2),
                        res.getString(3)));
                System.out.println("not empty");
            }
        }

        mAdapter = new PhraseAdapter(customPhraseList);
        mRecyclerView.setAdapter(mAdapter);

        mTextInputWord = view.findViewById(R.id.text_input_phrase);
        mBtnAddWord = view.findViewById(R.id.btnAddWord);

        mBtnAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTextInputWord.getEditText().getText().toString().trim().isEmpty()) {
                    mTextInputWord.setError("field cannot be empty");
                } else {
                    mTextInputWord.setError(null);
                    translate(mTextInputWord.getEditText().getText().toString().trim());
                    mAdapter.notifyDataSetChanged();

                }
            }
        });

        return view;
    }

    public void addWord(String phraseEn, String phraseCn) {
        DatabaseHelper myDb = new DatabaseHelper(getContext());
        PiPinyin piPinyin = new PiPinyin(getActivity());
        String phrasePinyin = piPinyin.toPinyin(phraseCn, " ");

        myDb.insertData(phraseEn, phraseCn, phrasePinyin, "custom", false, false);

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

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
