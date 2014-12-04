package com.gubertravel.guber;

import java.util.ArrayList;

/**
 * Created by kevinlau on 10/12/2014.
 */
public interface AsyncResponse {
    void markMap(ArrayList<String> output);
    void getUberCost(String cost);
}
