package com.techprox.ClothStock;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by stkornsmc on 2/16/14 AD.
 */
public class BranchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.branchview);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.deadstock_icon2);

        String name = getIntent().getStringExtra("name");

        TextView nametv = (TextView) findViewById(R.id.branchname);
        nametv.setText(name);

        Button rt = (Button) findViewById(R.id.getdirect);
        rt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?&daddr=%f,%f (%s)", 13.754037, 100.586614, "KPN TOWER");
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
