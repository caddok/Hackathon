package com.example.georgi.projecthackathon.camera.cam.take;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.georgi.projecthackathon.R;


public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = getIntent();

        Camera deviceCamera = Camera.open();

        Button captureButton = findViewById(R.id.button);
        captureButton.setOnClickListener(v -> takePicture(deviceCamera));
    }

    private void takePicture(Camera deviceCamera) {
        deviceCamera.takePicture(
                null,
                null,
                (data, camera) -> {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if (bitmap == null) {
                        Toast.makeText(CameraActivity.this, "Captured image is empty", Toast.LENGTH_LONG).show();
                        return;
                    }

                });
    }
}
