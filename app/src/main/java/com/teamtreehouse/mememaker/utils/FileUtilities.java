package com.teamtreehouse.mememaker.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Evan Anger on 7/28/14.
 */
public class FileUtilities
{

    private static final String TAG = FileUtilities.class.getSimpleName();

    public static void saveAssetImage(Context context, String assetName)
    {
        File fileDirectory = getFileDirectory(context);
        File fileToWrite = new File(fileDirectory + "/" + assetName);

        Log.i(TAG, fileDirectory.toString());
        Log.i(TAG, fileToWrite.toString());

        AssetManager assetManager = context.getAssets();
        try
        {
            InputStream in = assetManager.open(assetName);
            OutputStream out = new FileOutputStream(fileToWrite);

            copyFile(in, out);

            in.close();
            out.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static File getFileDirectory(Context context)
    {
        String storageType = StorageType.PRIVATE_EXTERNAL;
        if(storageType == StorageType.INTERNAL)
        {
            return context.getFilesDir();
        }
        else
        {
            if(isExternalStorageAvailable())
            {
                if(storageType == StorageType.PRIVATE_EXTERNAL)
                {
                    return context.getExternalFilesDir(null);
                }
                else
                {
                    //public external storage
                    return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                }
            }
            else
            {
                return context.getFilesDir();
            }
        }
    }

    public static boolean isExternalStorageAvailable()
    {
        String state = Environment.getExternalStorageState();
        if(state == Environment.MEDIA_MOUNTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static File[] listFiles(Context context)
    {
        File fileDirectory = getFileDirectory(context);
        Log.i(TAG, fileDirectory.toString());
        File[] filteredFiles = fileDirectory.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File file)
            {
                if(file.getAbsolutePath()
                        .contains(".jpg"))
                {
                    return true;
                }

                return false;
            }
        });

        return filteredFiles;
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }

    public static Uri saveImageForSharing(Context context, Bitmap bitmap, String assetName)
    {
        File fileToWrite = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), assetName);

        try
        {
            FileOutputStream outputStream = new FileOutputStream(fileToWrite);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            return Uri.fromFile(fileToWrite);
        }
    }


    public static void saveImage(Context context, Bitmap bitmap, String name)
    {
        File fileDirectory = getFileDirectory(context);
        File fileToWrite = new File(fileDirectory, name);

        try
        {
            FileOutputStream outputStream = new FileOutputStream(fileToWrite);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

}
