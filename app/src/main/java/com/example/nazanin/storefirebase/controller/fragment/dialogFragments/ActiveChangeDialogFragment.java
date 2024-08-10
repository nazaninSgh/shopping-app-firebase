package com.example.nazanin.storefirebase.controller.fragment.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.nazanin.storefirebase.R;
import com.example.nazanin.storefirebase.controller.activity.admin.SearchCustomerActivity;
import com.example.nazanin.storefirebase.model.DAO.CustomerManager;

public class ActiveChangeDialogFragment extends AppCompatDialogFragment {
    private boolean ifchecked;
    private TextView ask;
    private int customer_id;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ifchecked=getArguments().getBoolean("ifchecked");
        customer_id=getArguments().getInt("id");
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.fragment_change_active_state_dialog,null);
        ask=view.findViewById(R.id.ask);
        if (ifchecked){
            ask.setText("آیا تمایل به فعال کردن کاربر مورد نظر دارید؟");
        }
        else {
            ask.setText("آیا تمایل به غیرفعال کردن کاربر مورد نظر دارید؟");
        }
        builder.setView(view).setNegativeButton("لغو", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((SearchCustomerActivity)getActivity()).isActiveCheckbox.setChecked(!ifchecked);
            }
        }).setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CustomerManager manager=new CustomerManager(getContext());
                manager.updateCustomerActiveState(customer_id,ifchecked);

            }
        });
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        ((SearchCustomerActivity)getActivity()).isActiveCheckbox.setChecked(!ifchecked);
    }
}
