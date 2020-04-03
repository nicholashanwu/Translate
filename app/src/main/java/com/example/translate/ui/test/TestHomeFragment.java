package com.example.translate.ui.test;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.translate.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TestHomeFragment extends Fragment {

    private Button mBtnStartNumbers;
    private Button mBtnStartGreetings;
    private Button mBtnStartFood;
    private Button mBtnStartHelp;
    private CircleImageView mBtnProfileImageTest;

    public TestHomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_test_home, container, false);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

		mBtnStartNumbers = view.findViewById(R.id.btnStartNumbers);
		mBtnStartGreetings = view.findViewById(R.id.btnStartGreetings);
		mBtnStartFood = view.findViewById(R.id.btnStartFood);
		mBtnStartHelp = view.findViewById(R.id.btnStartHelp);
		mBtnProfileImageTest = view.findViewById(R.id.btnProfileImageTest);

        Picasso.get().load(R.mipmap.tzuyu).resize(144, 144).into(mBtnProfileImageTest);

        mBtnStartNumbers.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("testingType", "numbers");
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_test_home_to_testFragment, bundle);
            }
        });

        mBtnStartGreetings.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("testingType", "essentials");
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_test_home_to_testFragment, bundle);

            }
        });

        mBtnStartFood.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("testingType", "food");
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_test_home_to_testFragment, bundle);
            }
        });

        mBtnStartHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("testingType", "help");
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_test_home_to_testFragment, bundle);

            }
        });

        mBtnProfileImageTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(getView()).navigate(R.id.action_navigation_test_home_to_navigation_profile);

            }
        });

    }

}
