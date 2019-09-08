package com.pennapps.insulin;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

//import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    public void launchMain(View v) {
        Intent main = new Intent(getActivity(),MainActivity.class);
        String email = ((EditText)getView().findViewById(R.id.et_email)).getText().toString();
        Integer TDD = Integer.parseInt(((EditText)getView().findViewById(R.id.dose)).getText().toString());
        Integer targetBG = Integer.parseInt(((EditText)getView().findViewById(R.id.targetBG)).getText().toString());
        main.putExtra("email", email);
        main.putExtra("TDD", TDD);
        main.putExtra("targetBG", targetBG);
        RadioGroup rg = getView().findViewById(R.id.insulinType);
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
