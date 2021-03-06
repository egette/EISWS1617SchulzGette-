package de.schulzgette.thes_o_naise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.schulzgette.thes_o_naise.Models.ThesenModel;
import de.schulzgette.thes_o_naise.adapter.ViewPagerAdapter;
import de.schulzgette.thes_o_naise.database.Database;

public class ThesenAnsichtActivity extends FragmentActivity {
    static final String ARG_TID = "TID";
    String tid;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    ThesenModel these;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thesen_ansicht);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();

        if(bd != null)
        {
            tid = (String) bd.get("TID");
            updateThesenView(tid);
        }

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), tid);
        viewPager.setAdapter(adapter);

        final TabLayout.Tab pro = tabLayout.newTab();
        final TabLayout.Tab neutral = tabLayout.newTab();
        final TabLayout.Tab contra = tabLayout.newTab();

        pro.setText("Pro");
        neutral.setText("Neutral");
        contra.setText("Contra");

        tabLayout.addTab(pro, 0);
        tabLayout.addTab(neutral, 1);
        tabLayout.addTab(contra, 2);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setSelectedTabIndicatorHeight(15);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("einstellungen", MODE_PRIVATE);
        sharedPreferences.edit().putString("ansicht", tid).apply();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void updateThesenView(String TID) {
        Database db = new Database(getBaseContext());
        these = db.getTheseWithTID(TID);
        TextView tidtext = (TextView) findViewById(R.id.einthesentext);
        TextView waehlerPro = (TextView) findViewById(R.id.wahlerprothese);
        TextView waehlerNeutral = (TextView) findViewById(R.id.wahlerneutralthese);
        TextView waehlerContra = (TextView) findViewById(R.id.wahlercontrathese);
        final RelativeLayout theseninfo = (RelativeLayout) findViewById(R.id.layoutthesentext);
        if(these != null){
            tidtext.setText(these.getThesentext());
            String pro = these.getPro().toString();
            String neutral = these.getNeutral().toString();
            String contra = these.getContra().toString();
            waehlerPro.setText(pro);
            waehlerNeutral.setText(neutral);
            waehlerContra.setText(contra);
        }

        final ImageButton hide = (ImageButton) findViewById(R.id.hidethesentext);

        hide.setOnClickListener(new View.OnClickListener() {
            Boolean isExpanded = false;
            public void onClick(View v) {
                theseninfo.setVisibility((theseninfo.getVisibility() == View.VISIBLE)
                     ? View.GONE : View.VISIBLE);

                hide.setImageResource(isExpanded?R.drawable.ic_expand_less_black_24dp:R.drawable.ic_expand_more_black_24dp);
                if(isExpanded){
                    isExpanded = false;
                }else{
                    isExpanded = true;
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putString(ARG_TID, tid);
    }
}
