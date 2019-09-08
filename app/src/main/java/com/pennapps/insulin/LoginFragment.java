package com.pennapps.insulin;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void launchMain(View v) {
        Intent main = new Intent(getActivity(),MainActivity.class);
        String email = ((EditText)getActivity().findViewById(R.id.et_email)).getText().toString();
        main.putExtra("email", email);
        startActivity(main);
    }

}
