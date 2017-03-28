package com.introtoandroid.advancedlayouts;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;


/*
 *  Compare this implementation with the one of SimpleGridFragment.  There
 *  are two ways to do this.  The simplest way is to create the cursor
 *  adaptor in the onActivityCreated callback and not monkey around with
 *  a LoaderManager.    This implementation uses a LoaderManager instead,
 *  so it is more complicated, but also more powerful
 */

public class SimpleListFragment extends ListFragment
        implements AdapterView.OnItemClickListener,
                    LoaderManager.LoaderCallbacks<Cursor> {
    public static final String DEBUG_TAG = "SimpleListFragment";

    SimpleCursorAdapter mAdapter;

    public SimpleListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_simple_list, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Advanced Layouts");
        toolbar.setTitleTextColor(Color.WHITE);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        assert activity.getSupportActionBar() != null;
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.v(DEBUG_TAG, "onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    public static <T> void initLoader(final int loaderId, final Bundle args,
                                      final LoaderManager.LoaderCallbacks<T> callbacks,
                                      final LoaderManager loaderManager) {
        final Loader<T> loader = loaderManager.getLoader(loaderId);
        if (loader != null && loader.isReset()) {
            loaderManager.restartLoader(loaderId, args, callbacks);
        } else {
            loaderManager.initLoader(loaderId, args, callbacks);
        }
    }

    /* remember, this is a fragment class, not an activity class, but the fragment is tied
     * to the activity.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.v(DEBUG_TAG, "SimpleListFragment: onActivityCreated()");

        /* this is an empty adapter which we will use later to display our data.  We fill it
         * with a call to m_adapter.swapCursor() in onLoadFinished().   This is asynchronous
         * which is what we want so the UI thread doesn't get delayed.
         */
        mAdapter =  new SimpleCursorAdapter(getActivity(),
                R.layout.contact_list_simple,
                null,       // this is the slot for the cursor
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                },
                new int[]{
                        R.id.contact_item_simple_text
                }, 0);

        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(this);

        getLoaderManager().initLoader(0,null,this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Cursor phone = (Cursor) parent.getItemAtPosition(position);

        TextView tv = ((TextView) view);
        String name = phone.getString(phone.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        String num = phone.getString(phone.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.NUMBER));

        String displayed = tv.getText().toString();
        if (displayed.compareTo(name) == 0) {
            tv.setText(num);
        } else {
            tv.setText(name);
        }
        Log.v(DEBUG_TAG, "Cursor pos: " +
                phone.getPosition() + "== list pos: " + position);
        Log.v(DEBUG_TAG, "Cursor id: " +
                phone.getString(phone.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone._ID)) +
                "== list id: " + id);
    }

    /*
     * Loaders (https://developer.android.com/reference/android/content/Loader.html)
     * provide asynchronous loading of data.  They are managed via a LoaderManager.
     * This just creates a loader of the ContactsContract and returns it.   This is called by
     * the LoaderManager when we call initLoader().
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Log.v(DEBUG_TAG, "SimpleListFragment: onCreateLoader()");

        String[] requestedColumns = {
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
        };
        CursorLoader loader = new CursorLoader(getActivity(),
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                requestedColumns, null, null, null);
        return loader;
    }


    /**
     * Dislays either the name of the first contact or a message.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(DEBUG_TAG, "SimpleListFragment: onLoadFinished()");

        // We created the adapter with a null cursor.  Now put the cursor in.  Framework
        // will close it.   Don't know if it's necessary to check for null cursor
        if (cursor != null) {
            mAdapter.swapCursor(cursor);
        }

        // The beauty of this class is that if the ListView is empty, then the TextView
        // defined beneath it in the XML file is displayed.  If the ListView is not empty,
        // then the TextView is suppressed.
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Implement
        loader.forceLoad();
    }
}
