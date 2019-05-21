package com.marwia.android_image_resizing;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static com.marwia.android_image_resizing.ImageResizingUtils.resizeImageFile;
import static com.marwia.android_image_resizing.ImageResizingUtils.rotateImageIfNecessary;


/**
 * Asynchronous task to resize/reduce an image.
 * Created by Mariusz on 10/01/18.
 */

public class ImageResizingAsyncTask extends AsyncTask<File, Void, byte[]> {

    private ImageResizingAsyncResponse delegate;
    private int max_width;
    private int max_height;
    private long max_file_length;// in MB
    private int quality;

    public ImageResizingAsyncTask(ImageResizingAsyncResponse delegate, int max_width, int max_height, long max_file_length, int quality) {
        this.delegate = delegate;
        this.max_width = max_width;
        this.max_height = max_height;
        this.max_file_length = max_file_length * 1024 * 1024;//convert to MB
        this.quality = quality;
    }

    @Override
    protected void onPostExecute(byte[] result) {
        if (delegate != null)
            delegate.imageResizingFinish(result);
    }

    @Override
    protected byte[] doInBackground(File... files) {
        try {
            // resize the image
            Bitmap bmp = resizeImageFile(files[0], max_width, max_height);
            // rotate the image
            bmp = rotateImageIfNecessary(files[0], bmp);
            // compress the image
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (files[0].length() > max_file_length)
                bmp.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            else
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);//convert only to JPEG

            byte[] byteArray = stream.toByteArray();

            stream.flush();
            stream.close();

            return byteArray;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Interface created in order to get the result from resizing task.
     */
    public interface ImageResizingAsyncResponse {
        void imageResizingFinish(byte[] byteArray);
    }
}
