package com.example.divin_a.ssample2;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.CallLog;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LockDisplay extends AppCompatActivity {


    public void onAttachedToWindow() {
        Window window = getWindow();
        window.addFlags(/*WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | */WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED

                        | WindowManager.LayoutParams.FLAG_DIM_BEHIND
               /* | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD*/);
    }


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_display);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.menu_lock_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            AudioManager mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.dispatchMediaKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));

            return true;
        }
        else if( id==R.id.action_exit )
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

      //  public LockDisplay mActivity;
        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
           // mActivity = act;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1 );
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "FAV";
                case 1:
                    return "MISSED";
                case 2:
                    return "IN";
                case 3:
                    return "OUT";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {


        private class DownloadFilesTask extends AsyncTask<Integer, Integer, LazyContactAdapter> {
            protected LazyContactAdapter doInBackground(Integer... urls )
            {
                int count = urls.length;
                long totalSize = 0;

                LazyContactAdapter adapter = null;
                try {
                    switch (mnType) {
                        case 1:
                            adapter = new LazyContactAdapter(getActivity(),
                                    CalllogUtil.Instance().GetQuickContacts(getActivity()), Color.rgb(100, 100, 100), true);
                            break;
                        case 2:
                              adapter = new LazyContactAdapter(getActivity(),
                                      CalllogUtil.Instance().GetMissedCalls(getActivity()), Color.rgb(170, 15, 15), true);
                            break;
                        case 3:
                            adapter = new LazyContactAdapter(getActivity(),
                                    CalllogUtil.Instance().GetIncomCalls(getActivity()), Color.rgb(42, 150, 25), true);
                            break;
                        case 4:
                             adapter = new LazyContactAdapter(getActivity(),
                                     CalllogUtil.Instance().GetOutCalls(getActivity()), Color.rgb(61, 20, 125), true);
                            break;
                        default:
                            break;
                    }
                }
                catch ( Exception ex )
                {
                    Toast.makeText(getActivity() ,ex.toString(), Toast.LENGTH_LONG).show();
                }

               /// if( null != adapter )
               // {
                    // synchronized ( getActivity() )
                 //   {
                       // listView.setAdapter(adapter);
                  //  }
               // }
                return  adapter;

            }

            protected void onProgressUpdate(Integer... progress) {
              //  setProgressPercent(progress[0]);
            }

            protected void onPostExecute(LazyContactAdapter result)
            {
               // showDialog("Downloaded " + result + " bytes");
                if( null != result )
                 {
                // synchronized ( getActivity() )
                //   {
                 listView.setAdapter(result);
                //  }
                 }
            }
        }


       // public LockDisplay mActivity;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber)
        {
            PlaceholderFragment fragment = new PlaceholderFragment(  );
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment(/*LockDisplay act*/)
        {
            // mActivity = act;
        }

        private ListView listView;
        private int mnType;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_lock_display, container, false);

            mnType = getArguments().getInt(ARG_SECTION_NUMBER);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            listView = (ListView) rootView.findViewById(R.id.listContact);

            DownloadFilesTask task = new DownloadFilesTask();
            task.execute(new Integer[] { 0 });

          /*  new Handler().post(new Runnable() {
                @Override
                public void run() {
                    //Cursor cur = CalllogUtil.GetMissedCalls(getActivity());
                    // callLogCursorAdapter adp = new callLogCursorAdapter(getActivity(), cur);
                    // listView.setAdapter(adp);
                    //String[] fromColumns = {"duration", "number"};
                    //int[] toViews = {R.id.Name, R.id.Number};
                    // SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.contact_dial_row, cur, fromColumns, toViews, 0);

                    LazyContactAdapter adapter = null;
                    try {
                        switch (mnType) {
                            case 1:
                            //ArrayList<ContactInfo> Contacts = new ArrayList<ContactInfo>();
                            //Contacts.add( new ContactInfo("Janet", "8431583661"));
                           // Contacts.add( new ContactInfo("Home", "04842505948"));
                           // Contacts.add( new ContactInfo("Appachan", "09497678644"));
                           // Contacts.add( new ContactInfo("Dell Bridge", "180030103101"));

                                adapter = new LazyContactAdapter(getActivity(), CalllogUtil.Instance().GetQuickContacts(getActivity()), Color.rgb(100, 100, 100));

                                break;
                            case 2:
                                //  adapter = new LazyContactAdapter(getActivity(), CalllogUtil.Instance().GetMissedCalls(getActivity()), Color.rgb(255, 100, 50));
                                break;
                            case 3:
                                //adapter = new LazyContactAdapter(getActivity(), CalllogUtil.Instance().GetIncomCalls(getActivity()), Color.rgb(50, 255, 100));
                                break;
                            case 4:
                                // adapter = new LazyContactAdapter(getActivity(), CalllogUtil.Instance().GetOutCalls(getActivity()), Color.rgb(50, 100, 255));
                                break;
                            default:
                                break;
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
                    }

                    if (null != adapter) {
                        // synchronized ( getActivity() )
                        {
                            listView.setAdapter(adapter);
                        }
                    }
                }
            });*/



            return rootView;
        }
    }
}
