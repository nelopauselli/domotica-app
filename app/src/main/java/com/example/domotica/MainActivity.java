package com.example.domotica;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.domotica.domain.Device;
import com.example.domotica.tasks.DeviceSearchTask;
import com.example.domotica.tasks.OnDeviceFound;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<Device> devices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Context context = this;

        this.swipeRefreshLayout = findViewById(R.id.devices_refresh);
        this.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //devices.list(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Ingrese la IP del dispositivo");
                builder.setMessage("");
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.alert_device_add, null);

                builder.setView(dialogView);
                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                final EditText deviceNameEditText = dialogView.findViewById(R.id.device_name);
                                final EditText deviceIpEditText = dialogView.findViewById(R.id.device_ip);

                                Device device = new Device();
                                device.Name = deviceNameEditText.getText();
                                device.Ip = deviceIpEditText.getText();

                                devices.add(device);
                                adapter.notifyDataSetChanged();

                                Snackbar.make(view, "Nuevo dispositivo agregado", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        recyclerView = findViewById(R.id.devices);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DevicesAdapter(devices);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_device_search) {
            new DeviceSearchTask(this, new OnDeviceFound() {
                @Override
                public void onDeviceFounded(Device device) {
                    Log.d("MainActivity", "Nuevo dispositivo encontrado");
                    devices.add(device);
                    adapter.notifyDataSetChanged();
                }
            }).execute();

            Toast.makeText(this, "Buscando dispositivos en segundo plano", Toast.LENGTH_SHORT);
        }

        return super.onOptionsItemSelected(item);
    }
}