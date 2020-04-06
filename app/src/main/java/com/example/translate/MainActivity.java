package com.example.translate;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    private BottomNavigationView bottomBar;
    int[][] states = new int[][]{
            new int[]{android.R.attr.state_enabled}, // enabled
            new int[]{-android.R.attr.state_enabled}, // disabled
            new int[]{-android.R.attr.state_checked}, // unchecked
            new int[]{android.R.attr.state_pressed}  // pressed
    };

    int[] colors = new int[]{
            Color.WHITE,
            Color.GRAY,
            Color.WHITE,
            Color.GRAY
    };

    boolean isTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            initializeDatabase();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);

        bottomBar = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_test_home, R.id.navigation_profile)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomBar, navController);

        final ColorStateList colorList = new ColorStateList(states, colors);
        bottomBar.setItemIconTintList(colorList);
        bottomBar.setItemTextColor(colorList);



        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                enableBottomBar(true);
                isTest = false;
                if (destination.getId() == R.id.navigation_home) {
                    bottomBar.setBackgroundColor(getResources().getColor(R.color.colorGreenDark));
                    setStatusBarColor(R.color.colorGreenDark);
                } else if (destination.getId() == R.id.navigation_test_home) {
                    bottomBar.setBackgroundColor(getResources().getColor(R.color.colorRedDark));
                    setStatusBarColor(R.color.colorRedDark);
                } else if (destination.getId() == R.id.navigation_profile) {
                    bottomBar.setBackgroundColor(getResources().getColor(R.color.colorBlueDark));
                    setStatusBarColor(R.color.colorBlueDark);
                } else if (destination.getId() == R.id.navigation_learning) {
                    isTest = true;
                    bottomBar.setBackgroundColor(Color.parseColor("#444444"));
                    setStatusBarColor(R.color.colorGreenDark);
                    enableBottomBar(false);
                } else if (destination.getId() == R.id.navigation_test) {
                    isTest = true;
                    bottomBar.setBackgroundColor(Color.parseColor("#444444"));
                    enableBottomBar(false);
                } else if (destination.getId() == R.id.navigation_my_list_fragment) {
                    isTest = true;
                    bottomBar.setBackgroundColor(Color.parseColor("#444444"));
                    enableBottomBar(false);
                    setStatusBarColor(R.color.colorBlueDark);
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (isTest) {

        } else {
            super.onBackPressed();
        }
    }

    public void setStatusBarColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(id, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(id));
        }
    }

    public void initializeDatabase() {
        myDb = new DatabaseHelper(this);
        insertWordData();
        insertAchievementData();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    public void insertWordData() {
        myDb.insertData("One", "一", "Yī", "numbers", false, false);
        myDb.insertData("Two", "二", "Èr", "numbers", false, false);
        myDb.insertData("Three", "三", "Sān", "numbers", false, false);
        myDb.insertData("Four", "四", "Sì", "numbers", false, false);
        myDb.insertData("Five", "五", "Wǔ", "numbers", false, false);
        myDb.insertData("Six", "六", "Liù", "numbers", false, false);
        myDb.insertData("Seven", "七", "Qī", "numbers", false, false);
        myDb.insertData("Eight", "八", "Bā", "numbers", false, false);
        myDb.insertData("Nine", "九", "Jiǔ", "numbers", false, false);
        myDb.insertData("Ten", "十", "Shí", "numbers", false, false);
        myDb.insertData("Twenty", "二十", "Èrshí", "numbers", false, false);
        myDb.insertData("Fifty", "五十", "Wǔshí", "numbers", false, false);
        myDb.insertData("One Hundred", "一百", "Yībǎi", "numbers", false, false);
        myDb.insertData("One Thousand", "一千", "Yīqiān", "numbers", false, false);

        myDb.insertData("Hello", "你好", "Nǐ hǎo", "essentials", false, false);
        myDb.insertData("How are you?", "你好吗", "Nǐ hǎo ma", "essentials", false, false);
        myDb.insertData("Thank you", "谢谢", "Xièxiè", "essentials", false, false);
        myDb.insertData("Good", "好", "Hǎo", "essentials", false, false);
        myDb.insertData("Not good", "不好", "Bù hǎo", "essentials", false, false);
        myDb.insertData("I'm sorry", "对不起", "Duìbùqǐ", "essentials", false, false);
        myDb.insertData("Ok!", "好的", "Hǎo de", "essentials", false, false);
        myDb.insertData("Good Morning", "早上好", "Zǎoshang hǎo", "essentials", false, false);
        myDb.insertData("Goodnight", "晚安", "Wǎn'ān", "essentials", false, false);
        myDb.insertData("Good Evening", "晚上好", "Wǎnshàng hǎo", "essentials", false, false);
        myDb.insertData("I am-", "我是", "Wǒ shì", "essentials", false, false);
        myDb.insertData("Bye", "再见", "Zàijiàn", "essentials", false, false);

        myDb.insertData("Apple", "苹果", "Píngguǒ", "food", false, false);
        myDb.insertData("Banana", "香蕉", "Xiāngjiāo", "food", false, false);
        myDb.insertData("Orange", "橙子", "Chéngzi", "food", false, false);
        myDb.insertData("Hamburger", "汉堡包", "Hànbǎobāo", "food", false, false);
        myDb.insertData("Dumpling", "饺子", "Jiǎozi", "food", false, false);
        myDb.insertData("Baifan", "白饭", "Báifàn", "food", false, false);
        myDb.insertData("Noodles", "面条", "Miàntiáo", "food", false, false);
        myDb.insertData("Orange Juice", "橙汁", "Chéngzhī", "food", false, false);
        myDb.insertData("Apple Juice", "苹果汁", "Píngguǒ zhī", "food", false, false);
        myDb.insertData("Coffee", "咖啡", "Kāfēi", "food", false, false);
        myDb.insertData("Tea", "茶", "Chá", "food", false, false);
        myDb.insertData("Pizza", "比萨", "Bǐsà", "food", false, false);
        myDb.insertData("Sushi", "寿司", "Shòusī", "food", false, false);

        myDb.insertData("Police", "警察", "Jǐngchá", "help", false, false);
        myDb.insertData("Police Station", "警察局", "Jǐngchá jú", "help", false, false);
        myDb.insertData("Ambulance", "救护车", "Jiùhù chē", "help", false, false);
        myDb.insertData("Hospital", "医院", "Yīyuàn", "help", false, false);
        myDb.insertData("Fire", "火", "Huǒ", "help", false, false);
        myDb.insertData("Drugstore", "药店", "Yàodiàn", "help", false, false);
        myDb.insertData("Help", "救命", "Jiùmìng", "help", false, false);
        myDb.insertData("Stay Away", "远离", "Yuǎnlí", "help", false, false);
        myDb.insertData("Headache", "头痛", "Tóutòng", "help", false, false);
        myDb.insertData("Hot Water", "热水", "Rè shuǐ", "help", false, false);
        myDb.insertData("Go Away!", "走开", "Zǒu kāi", "help", false, false);


    }

    private void enableBottomBar(boolean enable) {
        for (int i = 0; i < bottomBar.getMenu().size(); i++) {
            bottomBar.getMenu().getItem(i).setEnabled(enable);
        }
    }

    public void insertAchievementData() {
        myDb.insertAchievementData("Number Novice", "Complete the Numbers learning module", 0, 1, false);
        myDb.insertAchievementData("Great Greeter", "Complete the Essentials learning module", 0, 1, false);
        myDb.insertAchievementData("Food Fight", "Complete the Food learning module", 0, 1, false);
        myDb.insertAchievementData("Helping Hand", "Complete the Help learning module", 0, 1, false);
        myDb.insertAchievementData("Dedicated", "Revise your saved words", 0, 1, false);
        myDb.insertAchievementData("Pursuing Perfection", "Revise your mastered words", 0, 1, false);
        myDb.insertAchievementData("Quick Quick Quick", "Complete a test in under 30 seconds", 0, 1, false);
        myDb.insertAchievementData("Self-Improver", "Check out all components in your profile", 0, 3, false);
        myDb.insertAchievementData("Lingo Learner", "Complete all learning modules", 0, 4, false);
        myDb.insertAchievementData("Lingo Legend", "Complete a test without any mistakes", 0, 1, false);
        myDb.insertAchievementData("Lingo Lord", "Complete all test modules", 0, 4, false);
        myDb.insertAchievementData("Nice Nine", "Achieve over 90% for any test", 0, 1, false);
        myDb.insertAchievementData("Excellent Eight", "Achieve over 80% for any test", 0, 1, false);
        myDb.insertAchievementData("Sensational Seven", "Achieve over 70% for any test", 0, 1, false);
        myDb.insertAchievementData("Sexy Six", "Achieve over 60% for any test", 0, 1, false);
        myDb.insertAchievementData("Did you even try?", "Achieve under 30% for any test", 0, 1, false);
        myDb.insertAchievementData("Slick Speedster", "Get an answer correct in less than 2 seconds", 0, 1, false);
        myDb.insertAchievementData("Instant Noodles", "Get an answer correct in less than 1 second", 0, 1, false);
        myDb.insertAchievementData("Off to a Great Start", "Get the first answer wrong", 0, 1, false);
        myDb.insertAchievementData("Abort?", "Get 3 answers wrong in a row", 0, 1, false);
        myDb.insertAchievementData("Abandon Ship!", "Get 5 answers incorrect in a row", 0, 1, false);
        myDb.insertAchievementData("Oh baby a Triple!", "Get 3 answers correct in a row", 0, 1, false);
        myDb.insertAchievementData("Pentakill!", "Get 5 answers correct in a row", 0, 1, false);
        myDb.insertAchievementData("Instant Noodles", "Get an answer correct in less than 1 second", 0, 1, false);
        myDb.insertAchievementData("Addition", "Add 1 word to the My Words section", 0, 10, false);
        myDb.insertAchievementData("Avid Addition", "Add 10 words to the My Words section", 0, 10, false);
        myDb.insertAchievementData("Awesome Addition", "Add 50 words to the My Words section", 0, 50, false);
        myDb.insertAchievementData("Ambitious Addition", "Add 500 words to the My Words section", 0, 500, false);
        myDb.insertAchievementData("Smart Saver", "Save 5 words", 0, 5, false);
        myDb.insertAchievementData("Sophisticated Saver", "Save 20 words", 0, 20, false);
        myDb.insertAchievementData("Foolish Forgetter", "Forget 10 words", 0, 10, false);

    }


}
