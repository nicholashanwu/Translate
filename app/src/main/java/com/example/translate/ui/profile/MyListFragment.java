package com.example.translate.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.translate.DatabaseHelper;
import com.example.translate.Phrase;
import com.example.translate.R;
import com.example.translate.Translater;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import name.pilgr.pipinyin.PiPinyin;

public class MyListFragment extends Fragment {

    private TextInputLayout mTextInputWord;
    private PhraseAdapter mAdapter;
    private ArrayList<Phrase> customPhraseList = new ArrayList<Phrase>();
    private TextView mTxtPlaceholder;

    private DatabaseHelper myDb;
    private ExtendedFloatingActionButton mBtnBack;

    private Cursor res;
    boolean modelDownloaded;

    private Translater translater;

    public MyListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_list, container, false);

        translater = new Translater();
        translater.checkModelExists(translater.configure());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextInputWord = view.findViewById(R.id.text_input_phrase);
        FloatingActionButton mFabAddWord = view.findViewById(R.id.fabAddWord);
        FloatingActionButton mFabDeleteAll = view.findViewById(R.id.fabDeleteAll);
        FloatingActionButton mFabLearn = view.findViewById(R.id.fabLearn);
        FloatingActionButton mFabTest = view.findViewById(R.id.fabTest);
        CircleImageView mBtnProfileImageMyList = view.findViewById(R.id.btnProfileImageMyList);

        mTxtPlaceholder = view.findViewById(R.id.txtPlaceholder);
        mBtnBack = view.findViewById(R.id.btnBack);

        myDb = new DatabaseHelper(getActivity());

        final RecyclerView mRecyclerView = view.findViewById(R.id.rvMyWords);
        res = myDb.getCategory("custom");

        if (res.getCount() == 0) {
            mTxtPlaceholder.setVisibility(View.VISIBLE);
        } else {
            mTxtPlaceholder.setVisibility(View.GONE);
            while (res.moveToNext()) {
                customPhraseList.add(new Phrase(
                        res.getString(0),
                        res.getString(1),
                        res.getString(2),
                        res.getString(3),
                        res.getString(4),
                        res.getString(5),
                        res.getString(6))
                );
            }
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new PhraseAdapter(customPhraseList);
        mRecyclerView.setAdapter(mAdapter);

        Glide.with(getContext()).load(R.drawable.tzuyu).apply(new RequestOptions().override(200, 200)).into(mBtnProfileImageMyList);


        mFabTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mAdapter.getPhraseList().size() <= 2) {
                    showMessage("No Enough Data", "You need at least three words to take a test!");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("testingType", "custom");
                    Navigation.findNavController(getView()).navigate(R.id.action_navigation_my_list_fragment_to_navigation_test, bundle);
                }

            }
        });

        mFabAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mTextInputWord.getEditText().getText().toString().trim().isEmpty()) {
                    mTextInputWord.setError("field cannot be empty");
                } else if (mTextInputWord.getEditText().getText().toString().length() > 15) {
                    mTextInputWord.setError("too many characters");
                } else {
                    closeKeyboard(view);
                    boolean exists = false;
                    for (int i = 0; i < customPhraseList.size(); i++) {
                        if (customPhraseList.get(i).getPhraseEn().equals(mTextInputWord.getEditText().getText().toString())) {
                            mTextInputWord.setError("phrase already exists");
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        mTextInputWord.setError(null);
                        translate(mTextInputWord.getEditText().getText().toString().trim(), view);
                        mTextInputWord.getEditText().clearAnimation();
                    }
                }
            }

        });

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (res != null) {
                    res.close();
                    myDb.close();
                }
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_my_list_fragment_to_navigation_profile);
            }
        });

        mAdapter.setOnItemClickListener(new PhraseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onDeleteClick(int position) {
                myDb.deletePhrase(customPhraseList.get(position).getPhraseEn());
                customPhraseList.remove(position);
                mAdapter.notifyItemRemoved(position);

                if (customPhraseList.isEmpty()) {
                    mTxtPlaceholder.setVisibility(getView().VISIBLE);
                } else {
                    mTxtPlaceholder.setVisibility(getView().GONE);
                }
            }
        });

        mFabLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAdapter.getPhraseList().size() == 0) {
                    showMessage("No Data", "You haven't added any of your own words yet");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("learningType", "custom");
                    Navigation.findNavController(getView()).navigate(R.id.action_navigation_my_list_fragment_to_navigation_learning, bundle);
                }
            }
        });

        mFabDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mAdapter.getPhraseList().size() == 0) {
                    showMessage("No Data", "You haven't added any of your own words yet");
                } else {
                    showDeleteConfirmation("Delete All Words?", "Delete all words?", view);

                }
            }
        });

    }

    public void translate(final String phrase, final View view) {

        translater.configure().translate(phrase)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                addWord(phrase, translatedText, view);
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

    public void addWord(String phraseEn, String phraseCn, View view) {
        PiPinyin piPinyin = new PiPinyin(getActivity());
        String phrasePinyin = piPinyin.toPinyin(phraseCn, " ");

        myDb.insertData(phraseEn, phraseCn, phrasePinyin, "custom", false, false);
        mAdapter.notifyItemInserted(customPhraseList.size());
        customPhraseList.add(new Phrase("hi", phraseEn, phraseCn, phrasePinyin, "custom", "0", "0"));


        if (customPhraseList.isEmpty()) {
            mTxtPlaceholder.setVisibility(View.VISIBLE);
        } else {
            mTxtPlaceholder.setVisibility(View.GONE);

        }

        if (customPhraseList.size() >= 500) {
            if (myDb.progressAchievement("Ambitious Addition")) {
                showAchievement("Ambitious Addition");
            }
        } else if (customPhraseList.size() >= 50) {
            if (myDb.progressAchievement("Awesome Addition")) {
                showAchievement("Awesome Addition");
            }
        } else if (customPhraseList.size() >= 10) {
            if (myDb.progressAchievement("Avid Addition")) {
                showAchievement("Avid Addition");
            }
        }


        // TODO: SHOW A REFRESH ICON
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void closeKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        }
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

    public void showDeleteConfirmation(String title, String message, final View anotherView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.Green));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_alert_dialog_profile, null);
        TextView txtTitle = view.findViewById(R.id.title);
        ImageButton imageButton = view.findViewById(R.id.image);

        imageButton.setImageResource(R.mipmap.under_30);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myDb.clearMyList();
                customPhraseList.clear();
                mAdapter.notifyDataSetChanged();
                mTxtPlaceholder.setVisibility(View.VISIBLE);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        txtTitle.setText(title);

        builder.setView(view);
        builder.show();
    }


}
