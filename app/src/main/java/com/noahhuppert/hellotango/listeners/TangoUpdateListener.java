package com.noahhuppert.hellotango.listeners;

import android.util.Log;

import com.google.atap.tango.ux.TangoUx;
import com.google.atap.tangoservice.Tango;
import com.google.atap.tangoservice.TangoCoordinateFramePair;
import com.google.atap.tangoservice.TangoEvent;
import com.google.atap.tangoservice.TangoPoseData;
import com.google.atap.tangoservice.TangoXyzIjData;

import java.util.ArrayList;
import java.util.List;

public class TangoUpdateListener implements Tango.OnTangoUpdateListener {
    private static final String TAG = TangoUpdateListener.class.getSimpleName();

    private TangoUx tangoUx;
    public List<TangoCoordinateFramePair> framePairs;

    public TangoUpdateListener(TangoUx tangoUx) {
        this.tangoUx = tangoUx;

        framePairs = new ArrayList<>();
        framePairs.add(new TangoCoordinateFramePair(
                TangoPoseData.COORDINATE_FRAME_START_OF_SERVICE,
                TangoPoseData.COORDINATE_FRAME_DEVICE));
    }

    @Override
    public void onPoseAvailable(TangoPoseData tangoPoseData) {
        if (tangoUx != null) tangoUx.updatePoseStatus(tangoPoseData.statusCode);
    }

    @Override
    public void onXyzIjAvailable(TangoXyzIjData tangoXyzIjData) {
        if (tangoUx != null) tangoUx.updateXyzCount(tangoXyzIjData.xyzCount);
    }

    @Override
    public void onFrameAvailable(int i) {

    }

    @Override
    public void onTangoEvent(TangoEvent tangoEvent) {
        if (tangoUx != null) tangoUx.updateTangoEvent(tangoEvent);

        switch(tangoEvent.eventKey) {
            case TangoEvent.DESCRIPTION_FISHEYE_OVER_EXPOSED:
                Log.e(TAG, "Overexposed");
            case TangoEvent.DESCRIPTION_FISHEYE_UNDER_EXPOSED:
                Log.e(TAG, "Underexposed");
            case TangoEvent.DESCRIPTION_TOO_FEW_FEATURES_TRACKED:
                Log.e(TAG, "Too few features tracked");
        }
    }
}
