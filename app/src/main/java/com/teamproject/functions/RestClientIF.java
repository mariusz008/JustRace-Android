package com.teamproject.functions;

import org.json.JSONException;

/**
 * Created by 008M on 2016-05-20.
 */
public interface RestClientIF {
    public void onResponseReceived(String result) throws JSONException;
}
