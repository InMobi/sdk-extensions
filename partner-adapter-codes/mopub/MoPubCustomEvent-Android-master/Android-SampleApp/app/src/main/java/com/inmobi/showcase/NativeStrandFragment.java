package com.inmobi.showcase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;

import com.inmobi.showcase.InMobiNativeStrandCustomEvent.InMobiNativeStrandRenderer;

public class NativeStrandFragment extends Fragment implements MoPubNative.MoPubNativeNetworkListener {

    static final String TITLE = "SimpleNativeStrand";

    private static final String MY_AD_UNIT_ID = "65ef7ca9d9d94d9380e1ecd302921e86";

    private ViewGroup mContainer;

    private MoPubNative mMopubNative;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_native_strand, container, false);
        mContainer = (ViewGroup)fragmentView.findViewById(R.id.strandContainer);
        InMobiNativeStrandRenderer inMobiAdRenderer = new InMobiNativeStrandCustomEvent.InMobiNativeStrandRenderer();
        mMopubNative = new MoPubNative(getActivity(), MY_AD_UNIT_ID, this);
        mMopubNative.registerAdRenderer(inMobiAdRenderer);
        mMopubNative.makeRequest();
        return fragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_native_strand, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                clearAd();
                return true;

            case R.id.action_reload:
                reloadAd();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void clearAd() {
        //Remove the child view
        mContainer.removeAllViews();
    }

    private void reloadAd() {
        clearAd();
        mMopubNative.makeRequest();
    }

    @Override
    public void onDestroyView() {
        mContainer.removeAllViews();
        super.onDestroyView();
    }

    @Override
    public void onNativeLoad(NativeAd nativeAd) {
        Log.i(TITLE, "Strand load successful");
        View adWrapper = nativeAd.createAdView(this.getActivity(),mContainer);
        nativeAd.renderAdView(adWrapper);
        mContainer.addView(adWrapper);
    }

    @Override
    public void onNativeFail(NativeErrorCode nativeErrorCode) {
        Log.w(TITLE, "Native failed to load:" + nativeErrorCode);
    }
}
