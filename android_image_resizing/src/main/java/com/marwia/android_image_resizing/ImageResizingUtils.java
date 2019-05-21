package com.marwia.android_image_resizing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageResizingUtils {

    /**
     * Metodo per ridurre le dimensioni del bitmap e cercare di prevenire l'eccezzione outOfMemory
     * @param f
     * @param WIDTH
     * @param HEIGHT
     * @return
     */
    public static Bitmap resizeImageFile(File f, int WIDTH, int HEIGHT){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale>= WIDTH && o.outHeight/scale>= HEIGHT)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            // now bitmap will be size reduced (if necessary)
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

        } catch (FileNotFoundException e) {}
        return null;
    }

    /**
     * This method rotates the bitmap according to EXIF info of the image.
     * @param imageFile - Image File necessary to find the image path and get EXIF info.
     * @param bitmap - The bitmap where the image has been loaded from file.
     * @return Rotated bitmap.
     * @throws IOException
     */
    public static Bitmap rotateImageIfNecessary(File imageFile, Bitmap bitmap) throws IOException {
        ExifInterface ei = new ExifInterface(imageFile.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }

        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
