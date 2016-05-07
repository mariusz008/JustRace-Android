package com.teamproject.dao;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class NetworkDAO {

    public String request(String uri) throws IOException {
        // Use the GET method, which submits the search terms in the URL.
        HttpGet httpGet = new HttpGet(uri);
        // how to handle response data.
        ResponseHandler<String> responseHander = new BasicResponseHandler();

        // marry the request and the response.
        HttpClient httpClient = new DefaultHttpClient();
        String returnString = null;
            returnString = httpClient.execute(httpGet, responseHander);
        return returnString;
    }
}
