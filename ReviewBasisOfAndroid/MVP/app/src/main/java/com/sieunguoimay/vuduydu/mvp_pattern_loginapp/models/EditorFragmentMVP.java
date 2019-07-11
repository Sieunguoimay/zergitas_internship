package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.models;

public interface EditorFragmentMVP {
    interface View{
        String getFileContent();
        String getFileName();

        void clearFile();
        void setFileContent(String fileContent);
        void showFileNameEmptyError();
        void showFileSaved();
        void showFileNameNotExistError();
    }
    interface Presenter{
        void attach(View view);
        void onSaveFile();
        void onNewFile();
        void onOpenFile();
    }
    interface Model{
        boolean saveFile(String fileName, String data);
        String loadFile(String fileName);
    }
}
