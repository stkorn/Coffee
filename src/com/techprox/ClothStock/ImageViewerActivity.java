package com.techprox.ClothStock;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.techprox.ClothStock.TouchImageView;

/**
 * Created by stkornsmc on 1/9/14 AD.
 */
public class ImageViewerActivity extends Activity {

    private TouchImageView img;
    private int resImg;
    private Button galCloseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageviewer);
        img = (TouchImageView) findViewById(R.id.imgDisplay);
        galCloseBtn = (Button) findViewById(R.id.galClose);

        galCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        resImg = getResources().getIdentifier(getIntent().getStringExtra("image"), "drawable", getPackageName());
        img.setImageResource(resImg);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home : onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        img.setImageDrawable(null);
    }
}
