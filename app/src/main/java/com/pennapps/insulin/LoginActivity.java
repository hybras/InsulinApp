package com.pennapps.insulin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;


import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        AuthenticationPagerAdapter pagerAdapter = new AuthenticationPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new LoginFragment());
        pagerAdapter.addFragment(new RegisterFragment());
        viewPager.setAdapter(pagerAdapter);
    }

    class AuthenticationPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        public AuthenticationPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }

    public void launchMain(View v) {
        Intent main = new Intent(this,MainActivity.class);
        String email = ((EditText)findViewById(R.id.et_email)).getText().toString();
        Integer TDD = Integer.parseInt(((EditText)findViewById(R.id.dose)).getText().toString());
        Integer targetBG = Integer.parseInt(((EditText)findViewById(R.id.targetBG)).getText().toString());
        main.putExtra("email", email);
        main.putExtra("TDD", TDD);
        main.putExtra("targetBG", targetBG);
        RadioGroup rg = findViewById(R.id.insulinType);
        boolean insType = false;
        switch (rg.getCheckedRadioButtonId()) {
            case R.id.rapidInsulin:
                insType = true;
                break;
            case R.id.regularInsulin:
                insType = false;
                break;
        }
        main.putExtra("insulinType", insType);
        startActivity(main);
    }

}
