package com.techprox.ClothStock;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by stkornsmc on 2/11/14 AD.
 */
public class ContactFragment extends Fragment {

    TextView tel;
    TextView email;

    private GoogleMap map;
    private com.google.android.gms.maps.MapFragment mapFrag;

    private static final LatLng KPN = new LatLng(13.754037, 100.586614);


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        mapFrag = (com.google.android.gms.maps.MapFragment) fm.findFragmentById(R.id.shopmap);
        if (mapFrag == null) {
            mapFrag = com.google.android.gms.maps.MapFragment.newInstance();
            fm.beginTransaction().replace(R.id.shopmap, mapFrag).commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contactview, container, false);

        tel = (TextView) view.findViewById(R.id.telephone);
        email = (TextView) view.findViewById(R.id.email);

        tel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        tel.setTextColor(Color.WHITE);
                        break;
                    case MotionEvent.ACTION_UP:
                        tel.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        tel.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                         break;
                    default:
                        tel.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));

                }


                return false;
            }
        });

        email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        email.setTextColor(Color.GRAY);
                        break;
                    case MotionEvent.ACTION_UP:
                        email.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        email.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                        break;
                    default:
                        email.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));

                }


                return false;
            }
        });


        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "087-039-3393"));
                startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(getActivity());
                builder.setType("message/rfc822");
                builder.addEmailTo("engineer@techprox.com");
                builder.setSubject(getActivity().getString(R.string.app_name));
                builder.setChooserTitle("Send Email");
                builder.startChooser();
            }
        });

        return view;
    }

    private void initilizeMap() {
        if (map == null) {
            map = mapFrag.getMap();

            //check if map is created successfully or not
            if (map == null) {
                Toast.makeText(getActivity(), "Sorry! unable to crete maps", Toast.LENGTH_SHORT).show();
            }
        }
        map.getUiSettings().setZoomControlsEnabled(false);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(KPN, 17));


        map.addMarker(new MarkerOptions()
                .position(KPN)
                .title("KPN Tower"));





    }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            map = mapFrag.getMap();
        }

        try {
            //Load map
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mapFrag != null)
            getFragmentManager().beginTransaction().remove(mapFrag).commit();
    }
}
