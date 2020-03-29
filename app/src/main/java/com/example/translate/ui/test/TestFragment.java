package com.example.translate.ui.test;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.translate.R;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

        insertSampleData();
        final String testingType = getArguments().getString("testingType");
        getData(testingType);
        setTitle(testingType);


        // Set Text Progress Indicator and advance it

        mProgressBar.setProgress(0);
        mTxtProgress.setText((currentCardNumber + 1) + "/" + categoryListCn.size());

        mTxtChineseCharacter.setText(categoryListCn.get(currentCardNumber));
        mTxtPinyin.setText(categoryListPinyin.get(currentCardNumber));

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

        mBtnAddValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertSampleData();
            }
        });

        mFabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTxtChineseCharacter.setTextSize(64);
                currentCardNumber++;

                //check if saved and update status
                //always make invisible again

                Cursor res = myDb.getSaveStatus(categoryListEn.get(currentCardNumber));
                while (res.moveToNext()) {
                    if (res.getInt(6) == 0) {
                        mFabSave.setImageResource(R.drawable.outline_bookmark_border_white_48);
                    } else {
                        mFabSave.setImageResource(R.drawable.baseline_bookmark_white_48);
                    }
                }

                mTxtPinyin.setText(categoryListPinyin.get(currentCardNumber));
                mFabAnswer.setImageResource(R.drawable.outline_visibility_off_white_48);


                if (currentCardNumber == 0) {
                    mProgressBar.setProgress(0, true);
                } else if (currentCardNumber < categoryListEn.size()) {
                    mTxtChineseCharacter.setText(categoryListCn.get(currentCardNumber));

                    progressDouble = (double) 100 * (currentCardNumber) / categoryListCn.size();
                    progressInt = (int) progressDouble;
                    mTxtProgress.setText((currentCardNumber + 1) + "/" + categoryListCn.size());
                    mProgressBar.setProgress(progressInt, true);
                } else {
                    mTxtProgress.setText("");
                    mProgressBar.setProgress(100, true);

                    showMessage("You're Finished!", "You completed the " + testingType + " learning module!");

                    FragmentManager fm = getFragmentManager();

                    fm.popBackStack();
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

    public void setTitle(String learningType) {
        if(learningType.equals("numbers")){
            mTxtLevelTitle.setText("Level 1 : Numbers");
        } else if (learningType.equals("essentials")){
            mTxtLevelTitle.setText("Level 2 : Essentials");
        } else if (learningType.equals("food")){
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


    public void insertSampleData() {
        myDb.insertData("One", "一", "Yī", "numbers", true, false);
        myDb.insertData("Two", "二", "Èr", "numbers", true, false);
        myDb.insertData("Three", "三", "Sān", "numbers", true, false);
        myDb.insertData("Four", "四", "Sì", "numbers", true, false);
        myDb.insertData("Five", "五", "Wǔ", "numbers", true, false);
        myDb.insertData("Six", "六", "Liù", "numbers", true, false);
        myDb.insertData("Seven", "七", "Qī", "numbers", false, false);
        myDb.insertData("Eight", "八", "Bā", "numbers", false, false);
        myDb.insertData("Nine", "九", "Jiǔ", "numbers", false, false);
        myDb.insertData("Ten", "十", "Shí", "numbers", false, false);
        myDb.insertData("Twenty", "二十", "Èrshí", "numbers", false, false);
        myDb.insertData("Fifty", "五十", "Wǔshí", "numbers", false, false);
        myDb.insertData("One Hundred", "一百", "Yībǎi", "numbers", false, false);
        myDb.insertData("One Thousand", "一千", "Yīqiān", "numbers", false, false);

        myDb.insertData("Hello", "你好", "Nǐ hǎo", "essentials", true, false);
        myDb.insertData("How are you?", "你好吗", "Nǐ hǎo ma", "essentials", true, false);
        myDb.insertData("Thank you", "谢谢", "Xièxiè", "essentials", true, false);
        myDb.insertData("Good", "好", "Hǎo", "essentials", true, false);
        myDb.insertData("Not good", "不好", "Bù hǎo", "essentials", true, false);
        myDb.insertData("I'm sorry", "对不起", "Duìbùqǐ", "essentials", true, false);
        myDb.insertData("Ok!", "好的", "Hǎo de", "essentials", true, false);
        myDb.insertData("Good Morning", "早上好", "Zǎoshang hǎo", "essentials", true, false);
        myDb.insertData("Goodnight", "晚安", "Wǎn'ān", "essentials", true, false);
        myDb.insertData("Good Evening", "晚上好", "Wǎnshàng hǎo", "essentials", true, false);
        myDb.insertData("I am-", "我是", "Wǒ shì", "essentials", true, false);
        myDb.insertData("Bye", "再见", "Zàijiàn", "essentials", false, false);

        myDb.insertData("Apple", "苹果", "Píngguǒ", "food", true, false);
        myDb.insertData("Banana", "香蕉", "Xiāngjiāo", "food", true, false);
        myDb.insertData("Orange", "橙子", "Chéngzi", "food", true, false);
        myDb.insertData("Hamburger", "汉堡包", "Hànbǎobāo", "food", true, false);
        myDb.insertData("Dumpling", "饺子", "Jiǎozi", "food", true, false);
        myDb.insertData("Baifan", "白饭", "Báifàn", "food", true, false);
        myDb.insertData("Noodles", "面条", "Miàntiáo", "food", true, false);
        myDb.insertData("Orange Juice", "橙汁", "Chéngzhī", "food", true, false);
        myDb.insertData("Apple Juice", "苹果汁", "Píngguǒ zhī", "food", true, false);
        myDb.insertData("Coffee", "咖啡", "Kāfēi", "food", true, false);
        myDb.insertData("Tea", "茶", "Chá", "food", true, false);
        myDb.insertData("Pizza", "比萨", "Bǐsà", "food", true, false);
        myDb.insertData("Sushi", "寿司", "Shòusī", "food", true, false);

        myDb.insertData("Police", "警察", "Jǐngchá", "help", true, false);
        myDb.insertData("Police Station", "警察局", "Jǐngchá jú", "help", true, false);
        myDb.insertData("Ambulance", "救护车", "Jiùhù chē", "help", true, false);
        myDb.insertData("Hospital", "医院", "Yīyuàn", "help", true, false);
        myDb.insertData("Fire", "火", "Huǒ", "help", true, false);
        myDb.insertData("Drugstore", "药店", "Yàodiàn", "help", true, false);
        myDb.insertData("Help", "救命", "Jiùmìng", "help", true, false);
        myDb.insertData("Stay Away", "远离", "Yuǎnlí", "help", true, false);
        myDb.insertData("Headache", "头痛", "Tóutòng", "help", true, false);
        myDb.insertData("Hot Water", "热水", "Rè shuǐ", "help", true, false);
        myDb.insertData("Go Away!", "走开", "Zǒu kāi", "help", true, false);
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
