package com.rohith.scanner;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ResultParser;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class Qrcodescanningfragment extends Fragment {


    CustomTabsIntent.Builder builder;
    Context context;

    public Qrcodescanningfragment() {
        // Required empty public constructor
    }

    private CompoundBarcodeView barcodeView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root ;
        context=getActivity();


        root =  inflater.inflate(R.layout.fragment_qrcodescanningfragment, container, false);
        barcodeView = root.findViewById(R.id.qrscan);
        barcodeView.decodeContinuous(callback);

        builder=new CustomTabsIntent.Builder();
        builder.setShowTitle(true);
        builder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
        builder.addDefaultShareMenuItem();

        return root;
    }


    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(final BarcodeResult result) {
            if(result.getText() != null) {
                MaterialStyledDialog dialog1 = new MaterialStyledDialog.Builder(Objects.requireNonNull(getContext()))
                        .setTitle("Scanned")
                        .setDescription(result.getText())
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
                                if(Patterns.WEB_URL.matcher(result.getText()).matches()) {
                                    url = result.getText();
                                }
                                else {
                                    url = "http://www.google.com/#q=" + result.getText();
                                }


                                CustomTabsIntent customTabsIntent=builder.build();
                                customTabsIntent.launchUrl(context,Uri.parse(url));

                                dialog.dismiss();
                               barcodeView.pause();
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


            }

            barcodeView.pause();
            long TIME_OUT = 3000;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    barcodeView.resume();
                }
            }, TIME_OUT);


        }
        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public void onResume() {
        barcodeView.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        barcodeView.pause();
        super.onPause();
    }

}
