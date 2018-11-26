package com.rohith.scanner;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class textscanningfragment extends Fragment {

    CameraView cameraView;
    Button button;


    public textscanningfragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraView.stop();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root;
        root =  inflater.inflate(R.layout.fragment_textscanningfragment, container, false);
        button = root.findViewById(R.id.button);
        cameraView =root.findViewById(R.id.cameraview);
        final Activity activity = getActivity();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.start();
                cameraView.captureImage();

            }
        });

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap,cameraView.getWidth(),cameraView.getHeight(),false);
                cameraView.stop();

                detecttxt(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        return root;
    }

    private void detecttxt(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance().getCloudTextRecognizer();
        recognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                processTxt(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void processTxt(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.TextBlock> blocks = firebaseVisionText.getTextBlocks();
        if(blocks.isEmpty()){
            Toast.makeText(getContext(),"No Text",Toast.LENGTH_SHORT).show();
        }
        for(FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()){
            final  String txt = block.getText();
            MaterialStyledDialog dialog1 = new MaterialStyledDialog.Builder(Objects.requireNonNull(getContext()))
                    .setTitle("Scanned")
                    .setDescription(txt)
                    .setStyle(Style.HEADER_WITH_ICON)
                    .setIcon(R.drawable.info)
                    .withDarkerOverlay(true)
                    .withDialogAnimation(true)
                    .setCancelable(false)
                    .setScrollable(true)
                    .setPositiveText(R.string.Navigate)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            String url;
                            if(Patterns.WEB_URL.matcher(txt).matches()) {
                                url = txt;
                            }
                            else {
                                url = "http://www.google.com/#q=" + txt;
                            }
                            Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    })
                    .setNeutralText("Dismiss")
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            dialog1.show();


        }

    }

}
