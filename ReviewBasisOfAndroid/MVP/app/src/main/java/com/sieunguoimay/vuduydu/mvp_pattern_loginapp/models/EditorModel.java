package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.models;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditorModel implements EditorFragmentMVP.Model {
    Context context;
    public EditorModel(Context context){
        this.context = context;
    }
    @Override
    public boolean saveFile(String fileName, String data) {
        if(isExternalStorageWritable()){
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/saved_files");
            if(!myDir.exists()){
                if(myDir.mkdirs())
                    Log.d("DIRECTORY","Created. Absolute path: "+myDir.getPath());
                else
                    Log.d("DIRECTORY","Failed. Absolute path: "+myDir.getPath());
            }
            File file = new File(myDir,fileName);
            if(file.exists()){
                file.delete();
            }

            try {
                FileOutputStream out = new FileOutputStream(file);
                out.write(data.getBytes());
                out.flush();
                out.close();
            }catch(IOException e){
                e.printStackTrace();
            }

            Log.d("FILE_SAVED","Absolute path: "+file.getAbsolutePath());
            return true;
        }

        return false;
    }

    @Override
    public String loadFile(String fileName) {
        if(isExternalStorageReadable()){
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/saved_files");
            if(!myDir.exists()){
                Log.d("READ_FILE_ERROR","Directory not exits");
                return null;
            }
            File file = new File(myDir,fileName);
            if(file.exists()){
                try {
                    FileInputStream fis = new FileInputStream(file);
                    byte[] data = new byte[(int) file.length()];
                    fis.read(data);
                    fis.close();
                    String str = new String(data, "UTF-8");

                    Log.d("FILE_LOADED","Absolute path: "+file.getAbsolutePath());
                    return str;
                }catch(IOException e){
                    e.printStackTrace();
                }
            }else
                Log.d("READ_FILE_ERROR","File not exits");
        }

        return null;
    }



    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getPublicAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), albumName);
        if (!file.mkdirs()) {
            Log.e("PUBLIC_STORAGE", "Directory not created");
        }
        return file;
    }

}
