package com.example.nazanin.storefirebase.controller.fragment.dialogFragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nazanin.storefirebase.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InActiveFragment extends DialogFragment {


    private TextView message;

    public InActiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog,container,false);
        message=view.findViewById(R.id.message);
        message.setText("حساب کاربری شما غیرفعال شده است");
        return view;
    }

}
