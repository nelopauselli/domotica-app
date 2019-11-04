package com.example.domotica.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.domotica.domain.Device;

import org.json.JSONObject;

public class DeviceSearchTask extends AsyncTask<Void, Integer, Void> {
    private static final int DEVICE_TIMEOUT_MS = 500;
    private Context context;
    private final OnDeviceFound onDeviceFound;
    private ProgressBar progressBar;
    private int i;

    public DeviceSearchTask(Context context, OnDeviceFound onDeviceFound, ProgressBar progressBar) {
        this.context = context;
        this.onDeviceFound=onDeviceFound;
        this.progressBar = progressBar;
    }

    @Override
    protected Void doInBackground(Void... params) {
        RequestQueue queue = Volley.newRequestQueue(context);

        progressBar.setMax(255);

        for (i = 0; i < 256; i++)
            try {
                String ip = "192.168.0." + i;

                String url = "http://" + ip + "/api/device";
                Log.d("DeviceSearchTask", "Buscando IoT Device en " + url);

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(getClass().getSimpleName(), "Response is: " + response);

                        try {
                            Device device = new Device();
                            device.Name = response.getString("name");
                            device.Ip = response.getString("ip");

                            if (onDeviceFound != null)
                                onDeviceFound.onDeviceFounded(device);
                        } catch (Exception ex) {
                            Log.e("DeviceSearchTask", ex.getMessage(), ex);
                        }

                        incrementProgress();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        incrementProgress();

                        String message = null;
                        if (error != null)
                            message = error.getMessage();

                        if (message != null)
                            Log.d("DeviceSearchTask", message);
                        else
                            Log.d("DeviceSearchTask", "error");
                    }
                });

                request.setRetryPolicy(new DefaultRetryPolicy(
                        DEVICE_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                queue.add(request);

            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }

    private void incrementProgress() {
        progressBar.incrementProgressBy(1);
        if(progressBar.getProgress()==progressBar.getMax())
            progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d("DeviceSearchTask", "Progress: "+ values[0]);
    }
}

