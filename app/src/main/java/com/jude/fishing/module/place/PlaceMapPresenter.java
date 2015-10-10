package com.jude.fishing.module.place;

import android.os.Bundle;

import com.jude.beam.bijection.Presenter;
import com.jude.fishing.model.PlaceModel;

/**
 * Created by Mr.Jude on 2015/9/28.
 */
public class PlaceMapPresenter extends Presenter<PlaceMapFragment> {

    @Override
    protected void onCreate(PlaceMapFragment view, Bundle savedState) {
        super.onCreate(view, savedState);
    }

    @Override
    protected void onCreateView(PlaceMapFragment view) {
        super.onCreateView(view);
        PlaceModel.getInstance().getAllPlaces().subscribe(placeBrief -> getView().addMarker(placeBrief));
    }
}