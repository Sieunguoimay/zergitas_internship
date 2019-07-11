package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.models;

public class EditorPresenter implements EditorFragmentMVP.Presenter {
    EditorFragmentMVP.View view;
    EditorFragmentMVP.Model model;

    public EditorPresenter(EditorFragmentMVP.Model model){
        this.model =model;
    }

    @Override
    public void attach(EditorFragmentMVP.View view) {
        this.view = view;
    }

    @Override
    public void onSaveFile() {
        String fileName = view.getFileName();
        String fileContent = view.getFileContent();
        if(fileName.isEmpty()){
            view.showFileNameEmptyError();
        }else{
            model.saveFile(fileName, fileContent);
            view.showFileSaved();
        }
    }

    @Override
    public void onNewFile() {
        view.clearFile();
    }

    @Override
    public void onOpenFile() {
        String fileName = view.getFileName();
        if(fileName.isEmpty()){
            view.showFileNameEmptyError();
        }else{
            String fileContent = model.loadFile(fileName);
            if(fileContent==null){
                view.showFileNameNotExistError();
            }else{
                view.setFileContent(fileContent);
            }
        }
    }
}
