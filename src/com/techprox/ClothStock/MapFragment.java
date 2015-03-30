package com.techprox.ClothStock;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by stkornsmc on 2/11/14 AD.
 */
public class MapFragment extends Fragment {

    private GoogleMap map;
    private com.google.android.gms.maps.MapFragment mapFrag;

    private static final LatLng BANGKOK = new LatLng(13.7572084, 100.5656302);
    private static final LatLng CHIANGMAI = new LatLng(18.769039, 98.976332);
    private static final LatLng PHITSANULOK = new LatLng(16.8149052, 100.283823);
    private static final LatLng PHETCHABURI = new LatLng(13.0960221, 99.955821);
    private static final LatLng PATTAYA = new LatLng(12.9168268, 100.9217167);
    private static final LatLng MAHASARAKHAM = new LatLng(15.9590334, 103.2141746);



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        mapFrag = (com.google.android.gms.maps.MapFragment) fm.findFragmentById(R.id.map);
        if (mapFrag == null) {
            mapFrag = com.google.android.gms.maps.MapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mapFrag).commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mapview, container, false);

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
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(15.3335417, 101.1233754), 6));


        map.addMarker(new MarkerOptions()
                .position(BANGKOK)
                .title("Central rama IV"));

        map.addMarker(new MarkerOptions()
                .position(CHIANGMAI)
                .title("Central Festival Chiangmai"));

        map.addMarker(new MarkerOptions()
                .position(PHITSANULOK)
                .title("Central Phisanulok"));

        map.addMarker(new MarkerOptions()
                .position(PHETCHABURI)
                .title("Phetchaburi"));

        map.addMarker(new MarkerOptions()
                .position(PATTAYA)
                .title("Pataya"));

        map.addMarker(new MarkerOptions()
                .position(MAHASARAKHAM)
                .title("Maha Sarakham"));


        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent i = new Intent(getActivity(), BranchActivity.class);
                i.putExtra("name", marker.getTitle());
                startActivity(i);
            }
        });
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
