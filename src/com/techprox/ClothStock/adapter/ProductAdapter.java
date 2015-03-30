package com.techprox.ClothStock.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.techprox.ClothStock.ProductFullActivity;
import com.techprox.ClothStock.R;
import com.techprox.ClothStock.model.ProductItem;

import java.lang.ref.WeakReference;

/**
 * Created by stkornsmc on 2/7/14 AD.
 */
public class ProductAdapter extends ArrayAdapter<ProductItem> {

    private Context mContext;

    private ImageView productImg;
    private TextView nameTv;
    private TextView cataTv;
    private TextView priceTv;

    private Bitmap mPlaceHolder = null;
    private LruCache<String, Bitmap> mMemoryCache;


    public ProductAdapter(Context context) {
        super(context, 0);
        mContext = context;

        // Get max available VM memory
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.i("kenshop", "maxMemory = " + maxMemory);

        //Divide VM memory for allocate cache
        int cacheSize;
        if(maxMemory < 50000){
            cacheSize = maxMemory / 3;
            Log.i("kenshop", "" + cacheSize);
        }else{
            cacheSize = maxMemory / 4;
            Log.i("kenshop", "" + cacheSize);
        }
        Log.i("LruCache", "cacheSize = " + cacheSize);

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.productlistitem, null);
        productImg = (ImageView) itemView.findViewById(R.id.pro_img);
        nameTv = (TextView) itemView.findViewById(R.id.namepro);
        cataTv = (TextView) itemView.findViewById(R.id.type);
        priceTv = (TextView) itemView.findViewById(R.id.price);

        final String imgPro = getItem(position).imageProduct;
        final String namePro = getItem(position).nameProduct;
        final String cata = getItem(position).cata;
        final int price = getItem(position).price;
        final int proid = getItem(position).id;

        int img = mContext.getResources()
                .getIdentifier(imgPro, "drawable", mContext.getPackageName());

        loadBitmap(img, productImg);

        String[] str = namePro.split(" ");

        nameTv.setText(str[0] + " " + str[1]);
        cataTv.setText(cata);
        priceTv.setText("$"+price);



        return itemView;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {

        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public Bitmap decodeBitmap(Resources res, int resId,
                               int reqWidth, int reqHeight){

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calInSampleSize(options, reqWidth, reqHeight);
        //Toast.makeText(mContext.getApplicationContext(), ""+options.outHeight+" "+options.inSampleSize, Toast.LENGTH_SHORT).show();

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);

    }


    public static int calInSampleSize (BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;

    }



    //Process in Background
    public class BitmapWorkerTask extends AsyncTask<Integer, Void , Bitmap> {

        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        public BitmapWorkerTask(ImageView imageView){
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            final Bitmap bitmap = decodeBitmap(mContext.getResources(), data, 200, 200);
            addBitmapToMemoryCache(String.valueOf(data), bitmap);

            return bitmap;

        }

        protected void onPostExecute(Bitmap bitmap)
        {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    Animation fadein = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
                    imageView.setImageBitmap(bitmap);
                    imageView.startAnimation(fadein);
                }
            }

        }

    }

    public void loadBitmap(int resId, ImageView imageView) {

        final String imageKey = String.valueOf(resId);
        final Bitmap bitmap = getBitmapFromMemCache(imageKey);

        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }else{

            if(cancelPotentialWork(resId, imageView)){
                final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
                final AsyncDrawable asyncDrawable =
                        new AsyncDrawable(mContext.getResources(), mPlaceHolder, task);
                imageView.setImageDrawable(asyncDrawable);
                task.execute(resId);
            }
        }
    }

    public static boolean cancelPotentialWork(int data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            final int bitmapData = bitmapWorkerTask.data;
            if (bitmapData != data) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }


    //Create a dedicated Drawable subclass to store a reference back to the worker task
    public static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask){
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);

        }

        public BitmapWorkerTask getBitmapWorkerTask(){
            return bitmapWorkerTaskReference.get();

        }

    }




}
