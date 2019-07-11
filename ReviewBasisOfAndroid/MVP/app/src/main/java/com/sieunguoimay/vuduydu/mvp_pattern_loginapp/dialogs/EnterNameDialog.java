package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.R;

public class EnterNameDialog extends DialogFragment {

    private TextView tvEnterNameDialogTitle;
    private EnterNameDialogListner listner;
    private EditText etName;
    private String title;
    private int id;
    public void setId(int id){this.id = id;}
    public void setListner(EnterNameDialogListner listner){this.listner = listner;}
    public void setDialogTitle(String title){this.title = title;}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_enter_name_dialog, container,false);

        etName = rootView.findViewById(R.id.etName);
        tvEnterNameDialogTitle = rootView.findViewById(R.id.tvEnterNameDialogTitle);
        tvEnterNameDialogTitle.setText("Enter "+title+" name");
        rootView.findViewById(R.id.btOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onNameReturnedFromDialog(etName.getText().toString(),id);
                dismiss();
            }
        });
        rootView.findViewById(R.id.btCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        // Do something else
        return rootView;
    }

    public interface EnterNameDialogListner{
        void onNameReturnedFromDialog(String name, int id);
    }
}
