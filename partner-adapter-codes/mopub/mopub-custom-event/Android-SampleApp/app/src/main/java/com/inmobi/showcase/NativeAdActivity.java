package com.inmobi.showcase;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


@SuppressLint("NewApi")
public class NativeAdActivity extends FragmentActivity implements ActionBar.TabListener{

		private static final String TAG = NativeAdActivity.class.getSimpleName();

		private static final int POSITION_SIMPLE_STRAND_FRAGMENT = 0;

		private static final int POSITION_STRAND_LIST_FRAGMENT = 1;


	/**
		 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
		 * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
		 * derivative, which will keep every loaded fragment in memory. If this becomes too memory
		 * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
		 */
		AppSectionsPagerAdapter mAppSectionsPagerAdapter;

		/**
		 * The {@link ViewPager} that will display the three primary sections of the app, one at a
		 * time.
		 */
		ViewPager mViewPager;

		@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_native_ad);
			//Native strands only work in portrait mode as of now.
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

			mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

			final ActionBar actionBar = getActionBar();

			actionBar.setHomeButtonEnabled(false);

			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mAppSectionsPagerAdapter);
			mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					actionBar.setSelectedNavigationItem(position);
				}
			});

			//OffScreen limit is set to 1 to avoid multiple fragments with ads being in memory.
			mViewPager.setOffscreenPageLimit(1);

			for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
				actionBar.addTab(
						actionBar.newTab()
								.setText(mAppSectionsPagerAdapter.getPageTitle(i))
								.setTabListener(this));
			}

		}

		@Override
		public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		}

		@SuppressLint("NewApi")
		@Override
		public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
			mViewPager.setCurrentItem(tab.getPosition());
		}

		@Override
		public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		}

		/**
		 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
		 * sections of the app.
		 */
		public static class AppSectionsPagerAdapter extends FragmentStatePagerAdapter {

			private SparseArray<Fragment> mRegisteredFragments = new SparseArray<>();

			public AppSectionsPagerAdapter(FragmentManager fm) {
				super(fm);
			}

			@Override
			public Fragment getItem(int i) {
				switch (i) {
					case POSITION_SIMPLE_STRAND_FRAGMENT:
						return new NativeStrandFragment();

					case POSITION_STRAND_LIST_FRAGMENT:
						return new NativeStrandListFragment();

					default:
						return new DummySectionFragment();
				}
			}

			@Override
			public int getCount() {
				return 2;
				//return 2;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				Fragment fragment = (Fragment) super.instantiateItem(container, position);
				mRegisteredFragments.put(position, fragment);
				return fragment;
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				mRegisteredFragments.remove(position);
				super.destroyItem(container, position, object);
			}

			public Fragment getRegisteredFragment(int position) {
				return mRegisteredFragments.get(position);
			}

			@Override
			public CharSequence getPageTitle(int position) {
				switch(position) {
					case POSITION_SIMPLE_STRAND_FRAGMENT:
						return NativeStrandFragment.TITLE;

					case POSITION_STRAND_LIST_FRAGMENT:
						return NativeStrandListFragment.TITLE;

					default:
						return "unknown Fragment";
				}
			}
		}

		/**
		 * A dummy fragment representing a section of the app, but that simply displays dummy text.
		 */
		public static class DummySectionFragment extends Fragment {

			@Override
			public View onCreateView(LayoutInflater inflater, ViewGroup container,
									 Bundle savedInstanceState) {
				View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
				((TextView) rootView.findViewById(android.R.id.text1)).setText(
						getString(R.string.dummy_section_text));
				return rootView;
			}
		}
	}
/*

	RequestParameters myRequestParameters;
	private ListView myListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.listview);




		*//*		myListView = (ListView)findViewById(R.id.actionlist);
		final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		for (int i = 0; i < 30; ++i) {
			myAdapter.add("Item " + i);
		}

		ViewBinder viewBinder = new ViewBinder.Builder(R.layout.native_ad_layout)
		.build();

		// Set up the positioning behavior your ads should have.
		MoPubNativeAdPositioning.MoPubServerPositioning adPositioning =
				MoPubNativeAdPositioning.serverPositioning();
		MoPubStaticNativeAdRenderer adRenderer = new MoPubStaticNativeAdRenderer(viewBinder);
		// Set up the MoPubAdAdapter
		mAdAdapter = new MoPubAdAdapter(this, myAdapter, adPositioning);
		mAdAdapter.registerAdRenderer(adRenderer);
		myListView.setAdapter(mAdAdapter);
		mAdAdapter.loadAds(MY_AD_UNIT_ID);*//*


		InMobiNativeStrandRenderer inMobiAdRenderer = new InMobiNativeStrandRenderer();
		MoPubNative mopubNative = new MoPubNative(this, MY_AD_UNIT_ID, this);
		mopubNative.registerAdRenderer(inMobiAdRenderer);
		mopubNative.makeRequest();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//myListView.destroyDrawingCache();
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onNativeFail(NativeErrorCode arg0) {
		// TODO Auto-generated method stub
		Log.v("NativeAdActivity","Native failed to load:"+arg0.toString());
	}

	@Override
	public void onNativeLoad(NativeAd nativeAd) {
		// TODO Auto-generated method stub
		Log.v("NativeAdActivity", "Native ad loaded");
		ViewGroup viewGroup = (ViewGroup)findViewById(R.id.content);

		View adWrapper = nativeAd.createAdView(viewGroup);
		nativeAd.renderAdView(adWrapper);
		viewGroup.addView(adWrapper);
	}*/
