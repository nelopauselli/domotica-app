package com.example.domotica;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.domotica.domain.Device;
import com.example.domotica.repositories.DeviceRepository;

import org.json.JSONArray;
import org.json.JSONObject;

public class DeviceActivity extends AppCompatActivity {
    private DeviceRepository deviceRepository = new DeviceRepository(this);
    private long deviceID;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();

        this.deviceID = intent.getLongExtra("deviceID", 0);
        Log.d("DeviceActivity", "DeviceID: " + deviceID);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Context context=this;

        Device device = deviceRepository.get(this.deviceID);


        if (device != null) {
            toolbar.setTitle(device.Name);

            final TextView stateTextView = findViewById(R.id.device_state);
            stateTextView.setText(R.string.state_connecting);

            TextView deviceIpTextView = findViewById(R.id.device_ip);
            deviceIpTextView.setText(device.Ip);

            RequestQueue queue = Volley.newRequestQueue(this);

            String url = "http://" + device.Ip + "/api/device";
            Log.d("DeviceActivity", "Obteniendo información del dispositivo " + url);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("DeviceActivity", "Response is: " + response);
                    LayoutInflater inflater = getLayoutInflater();

                    try {
                        stateTextView.setText(R.string.state_connected);

                        LinearLayout compomentsLayout = findViewById(R.id.components);

                        JSONArray components = response.getJSONArray("components");
                        for (int c = 0; c < components.length(); c++) {
                            JSONObject componentJson = components.getJSONObject(c);

                            String displayName=componentJson.getString("displayName");
                            Log.d("DeviceActivity", "Component.displayName: "+displayName);

                            View view = inflater.inflate(R.layout.component_item, null);
                            TextView componentName = view.findViewById(R.id.component_name);
                            componentName.setText(displayName);

                            JSONArray actionsJson = componentJson.getJSONArray("actions");
                            LinearLayout actionsLayout = view.findViewById(R.id.component_actions);
                            for (int a = 0; a < actionsJson.length(); a++) {
                                String action = actionsJson.getString(a);

                                Button button = new Button(context);
                                button.setText(action);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Button button = (Button) view;

                                        Snackbar.make(view, "TODO: " +button.getText(), Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                });

                                actionsLayout.addView(button);
                            }
                            compomentsLayout.addView(view);
                        }

                    } catch (Exception ex) {
                        stateTextView.setText(R.string.state_error);
                        Log.e("DeviceActivity", ex.getMessage(), ex);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    stateTextView.setText(R.string.state_error);

                    String message = null;
                    if (error != null)
                        message = error.getMessage();

                    if (message != null)
                        Log.d("DeviceActivity", message);
                    else
                        Log.d("DeviceActivity", "error");
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(request);
        }
    }
}
