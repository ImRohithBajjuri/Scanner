package com.rohith.scanner;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class pagerAdapter extends FragmentStatePagerAdapter {
    public pagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i){
            case 0 :
                return new textscanningfragment();


            case 1:
                return new Qrcodescanningfragment();


            case 2:
                return new Historyfragment();

                default:
                    return null;
        }



    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {


        switch (position){

            case 0:
                return "Text scanner";

            case 1:
                return "QR/Bar code";

            case 2:
                return "History";


                default:
                    return null;

        }


    }
}
