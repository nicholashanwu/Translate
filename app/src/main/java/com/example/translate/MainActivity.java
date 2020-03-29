package com.example.translate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if(firstStart){
            initializeDatabase();
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_learning, R.id.navigation_test, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }


    public void initializeDatabase() {
        myDb = new DatabaseHelper(this);
        insertSampleData();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
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

}
