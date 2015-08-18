package com.ground0.recapo.DataHandler;

import android.content.Context;
import android.util.Log;

import com.ground0.recapo.LandingActivity;
import com.ground0.recapo.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Arjun on 10-06-2015.
 */
public class FileWriter {

    Context context;
    String filename;
    File file;

    public File getFile() {
        return file;
    }

    public FileWriter(Context context,String filename)
    {
        this.context=context;
        this.filename=filename;
        File file = new File(context.getCacheDir(), filename );
        if(!file.exists())
        {
            try {
                file.createTempFile(filename,null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.file=file;

    }
    public  void writeObject(Context context, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public  Object readObject(Context context) throws IOException, ClassNotFoundException{
        FileInputStream fis = context.openFileInput(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        Log.i(LandingActivity.TAG,"@File reading obj:"+object.toString());
        return object;
    }
}
