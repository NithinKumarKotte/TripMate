package com.example.d27sa.tripmate;

import java.util.ArrayList;




public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(ArrayList<Route> route);
}
