package com.example.domotica;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.domotica.domain.Device;

import java.util.List;

class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder> {
    private final Context context;
    private List<Device> devices;

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public TextView nameTextView, ipTextView;

        public DeviceViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.device_layout);
            nameTextView = view.findViewById(R.id.device_name);
            ipTextView = view.findViewById(R.id.device_ip);
        }
    }

    public DevicesAdapter(Context context, List<Device> devices) {
        this.context = context;
        this.devices = devices;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.device_item, parent, false);

        DeviceViewHolder viewHolder = new DeviceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, final int position) {
        Device device = devices.get(position);
        holder.nameTextView.setText(device.Name);
        holder.ipTextView.setText(device.Ip);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DeviceActivity.class);

                Device device = devices.get(position);

                i.putExtra("deviceID", device.ID);

                context.startActivity(i);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return devices.size();
    }
}
