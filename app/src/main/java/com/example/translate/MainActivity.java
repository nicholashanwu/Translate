package com.example.translate;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;

import com.example.translate.ui.profile.Achievement;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    private BottomNavigationView bottomBar;
    public static ArrayList<Achievement> achievementList;

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

        bottomBar = (BottomNavigationView) findViewById(R.id.nav_view);

        //
        int i = bottomBar.getSelectedItemId();
        ColorStateList colorTint = bottomBar.getItemIconTintList();
        System.out.println(colorTint);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_test_home, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomBar, navController);

        achievementList = getAchievements();
        System.out.println(achievementList);

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


    public void insertAchievementData() {
        myDb.insertAchievementData("Number Novice", "Complete the first learning module: Numbers", 0, 1, false);
        myDb.insertAchievementData("Great Greeter", "Complete the second learning module: Essentials", 0, 1, false);
        myDb.insertAchievementData("Food Fight", "Complete the third learning module: Food", 0, 1, false);
        myDb.insertAchievementData("Helping Hand", "Complete the fourth learning module: Help", 0, 1, false);
        myDb.insertAchievementData("Dedicated", "Revise your saved words", 0, 1, false);
        myDb.insertAchievementData("Pursuing Perfection", "Revise your mastered words", 0, 1, false);
        myDb.insertAchievementData("Quick Quick Quick", "Complete a test in under 30 seconds", 0, 1, false);
        myDb.insertAchievementData("Self-Improver", "Check out all components in your profile", 0, 3, false);
        myDb.insertAchievementData("Lingo Learner", "Complete all learning modules", 0, 4, false);
        myDb.insertAchievementData("Lingo Legend", "Complete a test without any mistakes", 0, 1, false);
        myDb.insertAchievementData("Number Novice", "Complete the first learning module: Numbers", 0, 1, false);
        myDb.insertAchievementData("Number Novice", "Complete the first learning module: Numbers", 0, 1, false);
        myDb.insertAchievementData("Number Novice", "Complete the first learning module: Numbers", 0, 1, false);

    }

    public ArrayList<Achievement> getAchievements() {

        DatabaseHelper myDb = new DatabaseHelper(this);

        ArrayList<Achievement> achievementList = new ArrayList<>();

        Cursor res = myDb.getAchievements();
        while (res.moveToNext()) {
            achievementList.add(new Achievement(res.getString(0),
                    res.getString(1),
                    res.getString(2),
                    res.getInt(3),
                    res.getInt(4),
                    res.getString(5)));
        }

        return achievementList;
    }



    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }

}
