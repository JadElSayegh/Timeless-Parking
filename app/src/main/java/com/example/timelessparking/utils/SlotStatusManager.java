package com.example.timelessparking.utils;

import android.util.Log;
import com.example.timelessparking.adapter.ParkingSlotAdapter;

public class SlotStatusManager {
    private static ParkingSlotAdapter adapter;
    private static final String TAG = "SlotStatusManager";

    // Register the active adapter to manage updates
    public static void registerAdapter(ParkingSlotAdapter a) {
        adapter = a;
        Log.d(TAG, "Adapter registered");
    }

    // Update the status of a specific slot
    public static void updateSlot(String slotName, String status) {
        if (adapter != null && slotName != null && status != null) {
            adapter.updateSlotStatus(slotName, status);
            Log.d(TAG, "Updated slot: " + slotName + " to status: " + status);
        } else {
            Log.w(TAG, "Failed to update slot - Adapter or parameters are null");
        }
    }

    // Optionally unregister adapter
    public static void clearAdapter() {
        adapter = null;
        Log.d(TAG, "Adapter cleared");
    }
}
