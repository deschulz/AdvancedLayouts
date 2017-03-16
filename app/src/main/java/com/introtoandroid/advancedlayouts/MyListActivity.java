package com.introtoandroid.advancedlayouts;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MyListActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);

        /* This just grabs the toolbar and makes it an ActionBar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] items = {"Basic Layout", "List Layout", "Grid View"};

        /* use textview.xml and the items[] strings to make a list adapter.  What is
         * displayed is a list of textview objects, which are nothing more than TextViews
         * with some configuration information */
        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.textview, items);

        /* menu_list is a ListView id defined in menu_layout.xml */
        ListView av = (ListView) findViewById(R.id.menu_list);
        av.setAdapter(adapter);
        av.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                // This is sort of dumb.  Case 0 goes back to the basiclayout, case
                // 1 and 2 just change the label.  The view that is passed in is the
                // control (view) that was clicked
                Log.d(AdvancedLayoutsActivity.DEBUG_TAG, "pos: " + position + " , id: " + id);
                switch (position) {
                    case 0:
                        Intent intent = new Intent(getApplicationContext(),
                                BasicLayoutActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        TextView tv = (TextView) view;
                        tv.setText("Changed");
                        break;
                    case 2:
                        String original = (String) parent
                                .getItemAtPosition(position);
                        Log.d(AdvancedLayoutsActivity.DEBUG_TAG, "original string: "
                                + original);
                        ((TextView) view).setText("Updated");
                        break;
                }
            }
        });
    }

    /* this is when you click on the up arrow.   I think that shouldUpRecreateTask
     * interacts with the backstack to decide whether to return true or false
     * https://developer.android.com/training/implementing-navigation/ancestral.html
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}