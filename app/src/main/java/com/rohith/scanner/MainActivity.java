package com.rohith.scanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;

    ViewPager viewPager;
    public final int requestCode = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, requestCode);
        }

            tabLayout=findViewById(R.id.tablayout);

        viewPager=findViewById(R.id.viewpager);
        pagerAdapter pagerAdapter=new pagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);




        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i>0){
                    textscanningfragment.cameraView.stop();
                    Qrcodescanningfragment.barcodeView.resume();
                }
                else if (i==0){
                    textscanningfragment.cameraView.start();
                    Qrcodescanningfragment.barcodeView.pause();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        tabLayout.setupWithViewPager(viewPager);
    }
}
