package com.example.test500;

import android.os.Bundle;
import android.widget.TabHost;
import com.actionbarsherlock.app.SherlockActivity;

/**
 * Created by Artem on 05.04.14.
 */
public class AddContent extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);

        		tabs.setup();

        		TabHost.TabSpec spec = tabs.newTabSpec("tag1");

        		spec.setContent(R.id.tab1);
        		spec.setIndicator("Кот");
        		tabs.addTab(spec);

        		spec = tabs.newTabSpec("tag2");
        		spec.setContent(R.id.tab2);
        		spec.setIndicator("Кошка");
        		tabs.addTab(spec);

        		spec = tabs.newTabSpec("tag3");
        		spec.setContent(R.id.tab3);
        		spec.setIndicator("Котёнок");
        		tabs.addTab(spec);

        		tabs.setCurrentTab(0);

    }


}
