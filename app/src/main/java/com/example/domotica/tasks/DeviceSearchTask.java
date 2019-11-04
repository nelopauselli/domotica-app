package com.example.domotica.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
    private Context context;
    private final OnDeviceFound onDeviceFound;
    private int i;

    public DeviceSearchTask(Context context, OnDeviceFound onDeviceFound) {
        this.context = context;
        this.onDeviceFound=onDeviceFound;
    }

    @Override
    protected Void doInBackground(Void... params) {
        RequestQueue queue = Volley.newRequestQueue(context);

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
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                queue.add(request);

            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        values[0] = i * 100 / 255;
    }
}
