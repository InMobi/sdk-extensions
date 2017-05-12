package com.inmobi.showcase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mopub.nativeads.MoPubAdAdapter;
import com.mopub.nativeads.MoPubNativeAdLoadedListener;
import com.mopub.nativeads.MoPubNativeAdPositioning;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NativeStrandListFragment extends ListFragment {

    static final String TITLE = "Strand List Fragment";

    private static final String MY_AD_UNIT_ID = "65ef7ca9d9d94d9380e1ecd302921e86";

    private Picasso mPicasso;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BaseAdapter mOriginalAdapter;
    private MoPubAdAdapter mStrandAdapter;
    private MoPubNativeAdPositioning.MoPubClientPositioning mClientPositioning;
    private ArrayList<FeedItem> mFeedItems;

    // used with the option to insert content feeds
    // the String is a comma-separated list of indices where new content items
    // should be inserted
    // Feed positions begin at zero
    private String mPositionsToInsertContent;

    private final FeedItem[] mTestItems = new FeedItem[5];
    private final FeedItem[] mInsertionItems = new FeedItem[3];

    private static final int NUM_REPLICAS_OF_TEST_FEED = 10;
    private static final int ACTION_INSERT_ITEM = 1;
    private static final int ACTION_REMOVE_ITEM = 2;

    private class ListFragmentSwipeRefreshLayout extends SwipeRefreshLayout {

        public ListFragmentSwipeRefreshLayout(Context context) {
            super(context);
        }

        @Override
        public boolean canChildScrollUp() {
            final ListView listView = getListView();
            return listView.getVisibility() == View.VISIBLE && canListViewScrollUp(listView);
        }

    }

    private static boolean canListViewScrollUp(ListView listView) {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            return ViewCompat.canScrollVertically(listView, -1);
        } else {
            return listView.getChildCount() > 0 &&
                    (listView.getFirstVisiblePosition() > 0
                            || listView.getChildAt(0).getTop() < listView.getPaddingTop());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Populate feeds in onCreateView view as it is to be cleared in onDestroyView.
        mFeedItems = populateContentPositions();
        mPicasso = new Picasso.Builder(getActivity()).build();
        prepareToLoadAds();
        final View listFragmentView = super.onCreateView(inflater, container, savedInstanceState);

        mSwipeRefreshLayout = new ListFragmentSwipeRefreshLayout(container.getContext());
        mSwipeRefreshLayout.addView(listFragmentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSwipeRefreshLayout.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_green_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                Log.d(getClass().getName(), "Requested screen refresh");
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshAds();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        return mSwipeRefreshLayout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_native_strand_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                mStrandAdapter.clearAds();
                return true;
            case R.id.action_scroll_to:
                smoothScrollToPosition();
                return true;
            default:
              return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyView() {
        mPicasso.shutdown();
        if (mStrandAdapter != null) {
            mStrandAdapter.destroy();
        }
        super.onDestroyView();
        Log.i(TITLE, "onDestroyView");
    }

    private void prepareToLoadAds() {
        mOriginalAdapter = new UserItemAdapter(getActivity(), mFeedItems);
        mClientPositioning = new MoPubNativeAdPositioning.MoPubClientPositioning();
        mClientPositioning.addFixedPosition(2)
                .addFixedPosition(4)
                .addFixedPosition(8)
                .enableRepeatingPositions(3);

        if (mStrandAdapter != null) {
            mStrandAdapter.destroy();
        }

        mStrandAdapter = new MoPubAdAdapter(getActivity(), mOriginalAdapter, mClientPositioning);
        mStrandAdapter.setAdLoadedListener(new MoPubNativeAdLoadedListener() {
            @Override
            public void onAdLoaded(int i) {
                Log.i(TITLE, "Strand loaded at position:" + i);
            }

            @Override
            public void onAdRemoved(int i) {
                Log.i(TITLE, "Strand removed from position:" + i);
            }
        });
        final InMobiNativeStrandCustomEvent.InMobiNativeStrandRenderer inMobiAdRenderer =
                new InMobiNativeStrandCustomEvent.InMobiNativeStrandRenderer();
        mStrandAdapter.registerAdRenderer(inMobiAdRenderer);
        mOriginalAdapter.notifyDataSetChanged();
        setListAdapter(mStrandAdapter);
        mStrandAdapter.loadAds(MY_AD_UNIT_ID);
    }




    private void refreshAds() {
        Log.i(TITLE, "Settings changed, reloading Ads");
        if (null == mStrandAdapter || null == getListView()) {
            Log.e(TITLE, "The list-view or the adapter cannot be null!");
        } else {
            mStrandAdapter.refreshAds(getListView(), MY_AD_UNIT_ID);
        }
    }

    private void clearAds() {
        mStrandAdapter.clearAds();
    }

    private void smoothScrollToPosition() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Positions for new items");
        final EditText inputPosition = new EditText(getActivity());
        inputPosition.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogBuilder.setView(inputPosition);

        // Set up the buttons
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String positionToScrollTo = inputPosition.getText().toString();
                int position;
                if (positionToScrollTo.trim().length() > 0) {
                    List<String> pos = Arrays.asList(positionToScrollTo.split(",[ ]*"));
                    try {
                        position = Integer.parseInt(pos.get(0));
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                        return;
                    }

                    if (position >= 0 && position < mOriginalAdapter.getCount()) {
                        Log.v(TITLE, "Scroll to adjusted position: " + mStrandAdapter.getAdjustedPosition(position));
                        mStrandAdapter.smoothScrollToPosition(getListView(), position);
                    }
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialogBuilder.show();
    }

    private void addFeedItems() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Positions for new items");
        final EditText inputPositions = new EditText(getActivity());
        inputPositions.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogBuilder.setView(inputPositions);

        // Set up the buttons
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPositionsToInsertContent = inputPositions.getText().toString();
                if (mPositionsToInsertContent.trim().length() > 0) {
                    List<String> insertionPositions =
                            Arrays.asList(mPositionsToInsertContent.split(",[ ]*"));
                    int i = 0;
                    for (String pos : insertionPositions) {
                        int index;
                        try {
                            index = Integer.parseInt(pos);
                        } catch (NumberFormatException nfe) {
                            nfe.printStackTrace();
                            continue;
                        }
                        if (index >= 0) {
                            if (index < mOriginalAdapter.getCount()) {
                                mFeedItems.add(index, mInsertionItems[i % 3]);
                            } else {
                                mFeedItems.add(mInsertionItems[i % 3]);
                            }
                            i++;
                        }
                    }
                    showDialogToCheckIfStrandPositionsShouldBeAdjusted(insertionPositions,
                            ACTION_INSERT_ITEM);
                    mOriginalAdapter.notifyDataSetChanged();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialogBuilder.show();
    }

    private void removeFeedItems() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Positions for items to remove");
        final EditText inputPositions = new EditText(getActivity());
        inputPositions.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogBuilder.setView(inputPositions);

        // Set up the buttons
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPositionsToInsertContent = inputPositions.getText().toString();
                if (mPositionsToInsertContent.trim().length() > 0) {
                    List<String> insertionPositions =
                            Arrays.asList(mPositionsToInsertContent.split(",[ ]*"));
                    for (String pos : insertionPositions) {
                        int index;
                        try {
                            index = Integer.parseInt(pos);
                        } catch (NumberFormatException nfe) {
                            nfe.printStackTrace();
                            continue;
                        }
                        if (index >= 0 && index < mOriginalAdapter.getCount()) {
                            mFeedItems.remove(index);
                        }
                    }
                    showDialogToCheckIfStrandPositionsShouldBeAdjusted(insertionPositions,
                            ACTION_REMOVE_ITEM);
                    mOriginalAdapter.notifyDataSetChanged();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialogBuilder.show();
    }

    private void showDialogToCheckIfStrandPositionsShouldBeAdjusted(
            final List<String> insertionPositions,
            final int action) {
        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(getActivity());
        confirmationDialog.setTitle("Adjust strand positions?");

        // Set up the buttons
        confirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (String pos : insertionPositions) {
                    int index;
                    try {
                        index = Integer.parseInt(pos);
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                        continue;
                    }
                    if (index >= 0 && index < mOriginalAdapter.getCount()) {
                        switch (action) {
                            default:
                                Log.w(TITLE, "Unknown action code (" + action + ")");
                                break;
                            case ACTION_INSERT_ITEM:
                                mStrandAdapter.insertItem(index);
                                break;
                            case ACTION_REMOVE_ITEM:
                                mStrandAdapter.removeItem(index);
                                break;
                        }
                    }
                }
            }
        });
        confirmationDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        confirmationDialog.show();
    }

    private ArrayList<FeedItem> populateContentPositions() {
        mTestItems[0] = new FeedItem("Neha Jha", "Product Manager", "1:50 AM", "Looking out for a Sponsorship Manager with 5+ yrs exp for a sports tourism company in Bangalore with strong grasp of media planning principles & excellent understanding of target segment, market intelligence and media medium technicalities. For more infos contact me at neha@zyoin.com", "neha_jha", "neha_jha_big");
        mTestItems[1] = new FeedItem("Nazia Firdose", "HR", "9:50 AM", "Please pray for these children in Syria after the death of their mother. The oldest sister has to take care of her younger siblings. -Ayad L Gorgees. ***Please don't scroll past without Typing Amen! because they need our prayers!!", "nazia", "nazia_big");
        mTestItems[2] = new FeedItem("Dharmesh Shah", "Founder at HubSpot", "4:50 PM", "Why, dear God, haven't you started marketing yet? http://dharme.sh/1Ewu63k by @gjain via @Inboundorg", "dharmesh", "dharmesh_big");
        mTestItems[3] = new FeedItem("Piyush Shah", "CPO", "6:50 PM", "With mobile being accepted as the definitive medium to access consumers’ minds and wallets, Brands have begun a multi-million dollar spending race to allure and retain customers.  Read on: https://lnkd.in/e8mcUfc", "piyush", "piyush_big");
        mTestItems[4] = new FeedItem("Jeff Weiner", "CEO at Linkedin", "4:10 AM", "Honored to represent LinkedIn's Economic Graph capabilities at the White House earlier today and partnering to Upskill America.", "jeff", "jeff_big");

        final ArrayList<FeedItem> users = new ArrayList<>();
        users.add(mTestItems[0]);
        users.add(mTestItems[1]);
        users.add(mTestItems[2]);
        users.add(mTestItems[3]);
        users.add(mTestItems[4]);

        for (int j = 0; j < NUM_REPLICAS_OF_TEST_FEED; j++) {
            users.add(mTestItems[0]);
            users.add(mTestItems[1]);
            users.add(mTestItems[2]);
            users.add(mTestItems[3]);
            users.add(mTestItems[4]);
        }

        // items to be used in inserting test feeds
        mInsertionItems[0] = new FeedItem("Manoj Krishnan", "QA Ninja", "4:50 PM", "Why, dear God, haven't you started marketing yet? http://dharme.sh/1Ewu63k by @gjain via @Inboundorg", "dharmesh", "dharmesh_big");
        mInsertionItems[1] = new FeedItem("Dipal Patel", "Revolver Rani", "9:50 AM", "Please pray for these children in Syria after the death of their mother. The oldest sister has to take care of her younger siblings. -Ayad L Gorgees. ***Please don't scroll past without Typing Amen! because they need our prayers!!", "nazia", "nazia_big");
        mInsertionItems[2] = new FeedItem("Boo Boo", "Resident Comedian", "4:50 PM", "Why, dear God, haven't you started marketing yet? http://dharme.sh/1Ewu63k by @gjain via @Inboundorg", "dharmesh", "dharmesh_big");
        return users;
    }

    public class UserItemAdapter extends ArrayAdapter<FeedItem> {
        private Context context;
        private ArrayList<FeedItem> users;
        private LayoutInflater layoutInflater;

        class ContentViewHolder {
            TextView title;
            TextView subtitle;
            TextView time_tt;
            TextView description_tt;
            ImageView thumb_image;
            ImageView big_image;
            ImageView bottom_img;
        }

        public UserItemAdapter(Context context, ArrayList<FeedItem> users) {
            super(context, R.layout.listitem, R.id.title, users);
            this.context = context;
            this.users = users;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (null == rowView) {
                rowView = layoutInflater.inflate(R.layout.listitem, parent, false);
                ContentViewHolder viewHolder = new ContentViewHolder();
                viewHolder.title = (TextView) rowView.findViewById(R.id.title);
                viewHolder.subtitle = (TextView) rowView.findViewById(R.id.subtitle);
                viewHolder.time_tt = (TextView) rowView.findViewById(R.id.time_tt);
                viewHolder.description_tt = (TextView) rowView.findViewById(R.id.description_tt);
                viewHolder.thumb_image = (ImageView) rowView.findViewById(R.id.thumb_image);
                viewHolder.big_image = (ImageView) rowView.findViewById(R.id.big_image);
                viewHolder.bottom_img = (ImageView) rowView.findViewById(R.id.bottom_img);

                rowView.setTag(viewHolder);
            }

            FeedItem user = users.get(position);
            ContentViewHolder holder = (ContentViewHolder) rowView.getTag();
            holder.title.setText(user.title);
            holder.subtitle.setText(user.subtitle);
            holder.time_tt.setText(user.time_tt);
            holder.description_tt.setText(user.description_tt);

            mPicasso.load(getResources().getIdentifier(user.thumb_image, "drawable", getActivity().getPackageName()))
                    .into(holder.thumb_image);

            mPicasso.load(getResources().getIdentifier(user.big_image, "drawable", getActivity().getPackageName()))
                    .into(holder.big_image);

            mPicasso.load(R.drawable.linkedin_bottom)
                    .into(holder.bottom_img);

            return rowView;
        }
    }

    static class FeedItem {
        public String title;
        public String subtitle;
        public String time_tt;
        public String description_tt;
        public String thumb_image;
        public String big_image;

        public FeedItem(String title, String subtitle, String time_tt, String description_tt, String thumb_image, String big_image) {
            this.title = title;
            this.subtitle = subtitle;
            this.time_tt = time_tt;
            this.description_tt = description_tt;
            this.thumb_image = thumb_image;
            this.big_image = big_image;
        }
    }
}
