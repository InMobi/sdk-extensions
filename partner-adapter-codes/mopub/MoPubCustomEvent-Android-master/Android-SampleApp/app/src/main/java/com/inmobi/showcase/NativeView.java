package com.inmobi.showcase;

import com.mopub.nativeads.MoPubAdAdapter;
import com.mopub.nativeads.MoPubNative.MoPubNativeNetworkListener;
import com.mopub.nativeads.MoPubNativeAdPositioning;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.ViewBinder;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NativeView extends Activity implements MoPubNativeNetworkListener{

	private static final String MY_AD_UNIT_ID = "9af9709550ba4d74a4421943b48a27f5";
	private ListView myListView;
	private MoPubAdAdapter mAdAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		myListView = (ListView)findViewById(R.id.actionlist);
		final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		for (int i = 0; i < 30; ++i) {
			myAdapter.add("Item " + i);
		}

		ViewBinder viewBinder = new ViewBinder.Builder(R.layout.native_ad_layout)
		.titleId(R.id.native_ad_title)
		.iconImageId(R.id.native_ad_icon_image)
		.callToActionId(R.id.native_ad_cta)
		.build();

		// Set up the positioning behavior your ads should have.
		MoPubNativeAdPositioning.MoPubServerPositioning adPositioning =
				MoPubNativeAdPositioning.serverPositioning();
		MoPubStaticNativeAdRenderer adRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

		// Set up the MoPubAdAdapter
		mAdAdapter = new MoPubAdAdapter(this, myAdapter, adPositioning);
		mAdAdapter.registerAdRenderer(adRenderer);
		myListView.setAdapter(mAdAdapter);
		mAdAdapter.loadAds(MY_AD_UNIT_ID);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onDestroy() {
		myListView.destroyDrawingCache();
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		RequestParameters myRequestParameters = new RequestParameters.Builder()
				.build();

		// Request ads when the user returns to this activity.
		mAdAdapter.loadAds(MY_AD_UNIT_ID, myRequestParameters);
		super.onResume();	
	}

	@Override
	public void onNativeFail(NativeErrorCode arg0) {
		Log.v("NativeView","Native failed to load:"+arg0.toString());
	}

	@Override
	public void onNativeLoad(NativeAd arg0) {
		Log.v("NativeView", "Native ad loaded");
	}
}
