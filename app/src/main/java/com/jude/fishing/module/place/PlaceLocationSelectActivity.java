package com.jude.fishing.module.place;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.BeamBaseActivity;
import com.jude.fishing.R;
import com.jude.fishing.model.AccountModel;
import com.jude.fishing.model.LocationModel;
import com.jude.fishing.model.RongYunModel;
import com.jude.fishing.model.entities.Location;
import com.jude.swipbackhelper.SwipeBackHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.rong.message.LocationMessage;


/**
 * Created by Mr.Jude on 2015/10/10.
 */
@RequiresPresenter(PlaceLocationSelectPresenter.class)
public class PlaceLocationSelectActivity extends BeamBaseActivity<PlaceLocationSelectPresenter> implements AMap.OnMapClickListener, GeocodeSearch.OnGeocodeSearchListener, AMap.OnMarkerClickListener {
    @InjectView(R.id.map)
    MapView mMapView;
    @InjectView(R.id.address)
    TextView address;
    @InjectView(R.id.ok)
    Button ok;

    private AMap aMap;
    private Marker mMyLocation;
    private UiSettings mUiSettings;
    private GeocodeSearch mGeocoderSearch;
    private LatLng mPoint;
    private String mAddress;
    private String mBriefAddress;

    private boolean canSelect;

    private LocationMessage locationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_activity_select_location);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        ButterKnife.inject(this);
        canSelect = getIntent().getBooleanExtra("can",false);
        locationMessage = getIntent().getParcelableExtra("location");
        mMapView.onCreate(savedInstanceState);
        initMap();
        if (locationMessage!=null){
            ok.setVisibility(View.GONE);
        }
        ok.setOnClickListener(v -> {
            if (RongYunModel.getInstance().getLastLocationCallback()!=null){
                RongYunModel.getInstance().getLastLocationCallback().onSuccess(locationMessage);
                RongYunModel.getInstance().setLastLocationCallback(null);
            }else {
                Intent i = new Intent();
                i.putExtra("point", mPoint);
                i.putExtra("address", mAddress);
                i.putExtra("briefAddress", mBriefAddress);
                setResult(Activity.RESULT_OK, i);
            }
            finish();
        });
    }

    private void initMap() {
        aMap = mMapView.getMap();
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setScaleControlsEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(false);

        //焊死位置，未得许可不能更改
        if (canSelect||AccountModel.getInstance().checkIsSuper()){
            aMap.setOnMapClickListener(this);
        }
        aMap.setOnMarkerClickListener(this);
        mGeocoderSearch = new GeocodeSearch(this);
        mGeocoderSearch.setOnGeocodeSearchListener(this);
        initMyPoint();
    }

    private void initMyPoint() {
        Location location = LocationModel.getInstance().getCurLocation();
        moveTo(location.getLatitude(), location.getLongitude(), 13);
        mGeocoderSearch.getFromLocationAsyn(new RegeocodeQuery(new LatLonPoint(location.getLatitude(), location.getLongitude()), 50, GeocodeSearch.AMAP));

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));
        mMyLocation = aMap.addMarker(markerOption);
        mPoint = new LatLng(location.getLatitude(),location.getLongitude());
        LocationModel.getInstance().registerLocationChange(newLocation -> mMyLocation.setPosition(new LatLng(location.latitude,location.longitude)));
    }

    private void moveTo(double lat, double lng, float zoom) {
        aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(lat, lng), zoom, 0, 0
        )));
    }

    private Marker mLastMarker;

    @Override
    public void onMapClick(LatLng latLng) {
        if (mLastMarker != null) mLastMarker.destroy();
        mPoint = latLng;
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_point_bigger_red));
        mLastMarker = aMap.addMarker(markerOption);
        mGeocoderSearch.getFromLocationAsyn(new RegeocodeQuery(new LatLonPoint(latLng.latitude,latLng.longitude), 50,GeocodeSearch.AMAP));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(mMyLocation)){
            if (mLastMarker != null) mLastMarker.destroy();
            mPoint = marker.getPosition();
            mGeocoderSearch.getFromLocationAsyn(new RegeocodeQuery(new LatLonPoint(marker.getPosition().latitude,marker.getPosition().longitude), 50,GeocodeSearch.AMAP));
        }
        return true;
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        address.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress());
        mAddress = regeocodeResult.getRegeocodeAddress().getFormatAddress();
        mBriefAddress = regeocodeResult.getRegeocodeAddress().getTownship();
        Uri uri = Uri.parse("http://m.amap.com/navi").buildUpon()
                .appendQueryParameter("dest",LocationModel.getInstance().getCurLocation().getLatitude()+","+LocationModel.getInstance().getCurLocation().getLongitude())
                .appendQueryParameter("destName",mAddress)
                .appendQueryParameter("hideRouteIcon","1")
                .appendQueryParameter("key","474871845dfffa03a26cfcb0ab4fb4f1")
                .build();
        locationMessage = LocationMessage.obtain(LocationModel.getInstance().getCurLocation().getLatitude(),LocationModel.getInstance().getCurLocation().getLongitude(),
                mAddress, uri);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }


}
