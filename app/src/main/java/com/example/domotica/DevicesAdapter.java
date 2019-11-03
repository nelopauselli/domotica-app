package com.example.domotica;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.domotica.domain.Device;

import java.util.List;

class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder> {
    private List<Device> devices;

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, ipTextView;

        public DeviceViewHolder(View v) {
            super(v);
            nameTextView = v.findViewById(R.id.device_name);
            ipTextView = v.findViewById(R.id.device_ip);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DevicesAdapter(List<Device> devices) {
        this.devices = devices;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.device_item, parent, false);

        DeviceViewHolder viewHolder = new DeviceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        Device device = devices.get(position);
        holder.nameTextView.setText(device.Name);
        holder.ipTextView.setText(device.Ip);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return devices.size();
    }
}
