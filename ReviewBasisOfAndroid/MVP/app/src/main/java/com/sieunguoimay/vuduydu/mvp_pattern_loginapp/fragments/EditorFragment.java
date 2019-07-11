package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.R;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.dialogs.EnterNameDialog;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.models.EditorFragmentMVP;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.models.EditorModel;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.models.EditorPresenter;

public class EditorFragment extends Fragment implements EditorFragmentMVP.View ,EnterNameDialog.EnterNameDialogListner {

    private EditorFragmentMVP.Presenter presenter;
    private TextView tvFileName;
    private EditText etMainScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        presenter = new EditorPresenter(new EditorModel(getContext()));
        presenter.attach(this);
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_editor, container, false);
        etMainScreen = v.findViewById(R.id.etMainScreen);
        tvFileName = v.findViewById(R.id.tvFileName);
        tvFileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rename
                showRenameDialog("new",4);
            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_editor,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.it_saveFile) {
            if (tvFileName.getText().toString() == "") {
                showRenameDialog("file",2);
            } else
                presenter.onSaveFile();
            return true;
        }
        if(item.getItemId() == R.id.it_newFile) {
            showRenameDialog("new file", 0);
        }if(item.getItemId() == R.id.it_openFile){
            showRenameDialog("open file",1);
        }
        return true;
    }


    private void showRenameDialog(String title, int index){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment dialogFragment = new EnterNameDialog();
        ((EnterNameDialog) dialogFragment).setListner(this);
        ((EnterNameDialog) dialogFragment).setId(index);
        ((EnterNameDialog) dialogFragment).setDialogTitle(title);
        dialogFragment.show(getFragmentManager(),"dialog");
    }
    @Override
    public void onNameReturnedFromDialog(String name,int id) {
        tvFileName.setText(name);
        //call presenter to do the saving file stuff
        if(id == 2)
            presenter.onSaveFile();
        //call presenter to do the opening file stuff
        else if(id == 1)
            presenter.onOpenFile();

        //call presenter to do the creating file stuff
        else if(id == 0)
            presenter.onNewFile();

        else if(id == 4)
            tvFileName.setText(name);
    }

    @Override
    public void onResume(){
        super.onResume();

    }



    @Override
    public String getFileContent() {
        return etMainScreen.getText().toString();
    }

    @Override
    public String getFileName() {
        return tvFileName.getText().toString();
    }

    @Override
    public void clearFile() {
        etMainScreen.setText("");
    }

    @Override
    public void setFileContent(String fileContent) {
        etMainScreen.setText(fileContent);
        Toast.makeText(getContext(),"file loaded "+tvFileName.getText().toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFileNameEmptyError() {
        Toast.makeText(getContext(),"File name is empty", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFileSaved() {
        Toast.makeText(getContext(),"File saved to "+tvFileName.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFileNameNotExistError() {
        Toast.makeText(getContext(),"File does not exist "+tvFileName.getText().toString(), Toast.LENGTH_SHORT).show();
    }


}
