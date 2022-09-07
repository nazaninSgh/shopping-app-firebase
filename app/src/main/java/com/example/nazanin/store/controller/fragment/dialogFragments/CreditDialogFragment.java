package com.example.nazanin.store.controller.fragment.dialogFragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nazanin.store.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreditDialogFragment extends DialogFragment {

    private TextView message;

    public CreditDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.dialog,container,false);
        message=view.findViewById(R.id.message);
        message.setText("اعتبار شما کافی نمی باشد");
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
