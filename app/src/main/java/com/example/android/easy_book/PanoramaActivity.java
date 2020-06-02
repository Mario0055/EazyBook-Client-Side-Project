package com.example.android.easy_book;

import android.os.Bundle;
import android.widget.ImageView;

import com.gjiazhe.panoramaimageview.GyroscopeObserver;
import com.gjiazhe.panoramaimageview.PanoramaImageView;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import androidx.appcompat.app.AppCompatActivity;

public class PanoramaActivity extends AppCompatActivity {
    public VrPanoramaView mVRPanoramaView;
    ImageView imageView;
    private GyroscopeObserver gyroscopeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);
        imageView = findViewById(R.id.panorama_view);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int resId = bundle.getInt("resId");
            imageView.setImageResource(resId);
        }
        //view image in panorama
        gyroscopeObserver = new GyroscopeObserver();
        gyroscopeObserver.setMaxRotateRadian(Math.PI / 4);
        PanoramaImageView panoramaImageView = (PanoramaImageView) findViewById(R.id.panorama_view);
        panoramaImageView.setGyroscopeObserver(gyroscopeObserver);
    }

    //360 photo


     @Override
    protected void onPostResume() {
        super.onPostResume();
        gyroscopeObserver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gyroscopeObserver.unregister();
    }

}
