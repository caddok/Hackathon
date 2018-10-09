package com.example.georgi.projecthackathon;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.georgi.projecthackathon.camera.cam.take.CameraActivity;
import com.example.georgi.projecthackathon.camera.cam.take.CameraUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Button mTakePicture;
    private Button mSendPicture;
    private String imageStoragePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTakePicture = findViewById(R.id.take_picture_btn);
        mSendPicture = findViewById(R.id.send_picture_btn);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!CameraUtils.isDeviceSupportCamera(getApplicationContext())) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device doesn't have camera
            finish();
        }

        mSendPicture.setOnClickListener(this);
        mTakePicture.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.take_picture_btn:
                if (requestCameraPermission()) {
                    intent = prepFile();
                    //intent = new Intent(this,CameraActivity.class);
                    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                }
                break;
            case R.id.send_picture_btn:
        }
    }

    private boolean requestCameraPermission() {
        final boolean[] isGranted = new boolean[1];
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            isGranted[0] = true;
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
        return isGranted[0];
    }

    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", (dialog, which) -> CameraUtils.openSettings(MainActivity.this))
                .setNegativeButton("CANCEL", (dialog, which) -> {
                }).show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Refreshing the gallery
            imageStoragePath = CameraUtils.KEY_IMAGE_STORAGE_PATH;
            CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

            // successfully captured the image
            // display it in image view
        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(getApplicationContext(),
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();
        } else {
            // failed to capture image
            Toast.makeText(getApplicationContext(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
