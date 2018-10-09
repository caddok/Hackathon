package com.example.georgi.projecthackathon.camera.cam.take;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.example.georgi.projecthackathon.BuildConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraUtils {

    // Gallery directory name to store the images or videos
    private static final String GALLERY_DIRECTORY_NAME = "Camera Demo";

    // key to store image path in savedInstance state
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";

    // Bitmap sampling size
    static final int BITMAP_SAMPLE_SIZE = 8;

    // Image and Video file extensions
    static final String IMAGE_EXTENSION = "jpg";

    /**
     * Refreshes gallery on adding new image. Gallery won't be refreshed
     * on older devices until device is rebooted
     */
    public static void refreshGallery(Context context, String filePath) {
        // ScanFile so it will be appeared on Gallery
        MediaScannerConnection.scanFile(
                context,
                new String[]{filePath},
                null,
                (path, uri) -> {
                });
    }

    static boolean checkPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Downsizing the bitmap to avoid OutOfMemory exceptions
     */
    static Bitmap optimizeBitmap(int sampleSize, String filePath) {
        // bitmap factory
        BitmapFactory.Options options = new BitmapFactory.Options();

        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = sampleSize;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * Checks whether device has camera or not. This method not necessary if
     * android:required="true" is used in manifest file
     */
    public static boolean isDeviceSupportCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * Open device app settings to allow user to enable permissions
     */
    public static void openSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", BuildConfig.APPLICATION_ID, null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static Uri getOutputMediaFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
    }

    /**
     * Creates and returns the image before opening the camera
     */
    public static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                GALLERY_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(GALLERY_DIRECTORY_NAME, "Oops! Failed create "
                        + GALLERY_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Preparing media file naming convention
        // adds timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + "." + IMAGE_EXTENSION);
        return mediaFile;
    }
}