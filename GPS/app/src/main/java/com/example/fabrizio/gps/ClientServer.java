package com.example.fabrizio.gps;


import android.content.Context;
import android.widget.Toast;

import com.example.fabrizio.gps.*;
import com.example.fabrizio.gps.api.CalculatorApi;

import java.io.IOException;
import java.net.SocketTimeoutException;


public class ClientServer implements Runnable
{

    private volatile String result;
    CalculatorApi apiInstance = new CalculatorApi();
    private Double lat;
    private Double lon;
    private Double alt;

    public ClientServer(Double lat, Double lon,Double alt) {


        this.lat = lat;
        this.lon = lon;
        this.alt=alt;
    }
    public String getValue()
    {
        return result;
    }
    public void run() {

        try
        {
            result = apiInstance.compute(lat.toString(), lon.toString(), alt.toString());
            System.out.println(result);

        }
       catch (ApiException e)
        {
            System.err.println("Exception when calling CalculatorApi#compute");
            e.printStackTrace();
        }


    }
}




