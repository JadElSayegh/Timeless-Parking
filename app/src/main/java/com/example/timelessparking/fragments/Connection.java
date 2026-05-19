package com.example.timelessparking.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.timelessparking.R;
import com.example.timelessparking.bluetooth.BluetoothService;
import com.example.timelessparking.utils.SlotStatusManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Connection extends Fragment {

    private static final int REQUEST_PERMISSIONS = 2;

    private BluetoothAdapter bluetoothAdapter;
    private TextView tvData;

    private String lastUID = "";
    private long lastTimestamp = 0;

    private String registeredUID = "";

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection, container, false);

        tvData = view.findViewById(R.id.tvData);
        Button btnConnect = view.findViewById(R.id.btnConnect);
        Button btnClear = view.findViewById(R.id.btnClear);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        if (bluetoothAdapter == null) {
            Toast.makeText(getContext(), "Bluetooth not supported", Toast.LENGTH_LONG).show();
            requireActivity().finish();
        }

        checkPermissions();

        btnConnect.setOnClickListener(v -> showDeviceList());
        btnClear.setOnClickListener(v -> tvData.setText(""));

        return view;
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{android.Manifest.permission.BLUETOOTH_CONNECT, android.Manifest.permission.BLUETOOTH_SCAN},
                        REQUEST_PERMISSIONS);
            }
        }
    }

    private void showDeviceList() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        final BluetoothDevice[] devices = pairedDevices.toArray(new BluetoothDevice[0]);
        String[] names = new String[devices.length];

        for (int i = 0; i < devices.length; i++) {
            names[i] = devices[i].getName() + "\n" + devices[i].getAddress();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Bluetooth Device");
        builder.setItems(names, (dialog, which) -> connectToDevice(devices[which]));
        builder.show();
    }

    private void connectToDevice(BluetoothDevice device) {
        BluetoothService bluetoothService = BluetoothService.getInstance();
        bluetoothService.setListener(new BluetoothService.BluetoothListener() {
            @Override
            public void onDataReceived(String data) {
                requireActivity().runOnUiThread(() -> {
                    tvData.append(data + "\n");

                    if (data.startsWith("Slot")) {
                        String[] parts = data.split(":");
                        if (parts.length == 2) {
                            String slotName = parts[0].trim(); // e.g., "Slot A1"
                            String status = parts[1].trim();   // e.g., "Busy" or "Available"
                            SlotStatusManager.updateSlot(slotName, status);
                        }
                    }

                    processLine(data); // Still handle RFID logic
                });
            }

            @Override
            public void onConnectionStatus(String status) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show());
            }
        });
        bluetoothService.connect(device);
    }

    private void processLine(String line) {
        if (line.startsWith("FIRST:")) {
            String[] firstParts = line.split(" @");
            if (firstParts.length == 2) {
                String scannedUID = firstParts[0].substring(6).trim();
                long timestamp = Long.parseLong(firstParts[1]);

                String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
                if (userId == null) return;

                db.collection("Users")
                        .document(userId)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                registeredUID = documentSnapshot.getString("rfidCode");
                                if (scannedUID != null && registeredUID != null &&
                                        scannedUID.trim().equalsIgnoreCase(registeredUID.trim())) {
                                    lastUID = scannedUID;
                                    lastTimestamp = timestamp;

                                    String display = "First scan: UID " + lastUID + " at " + formatMillis(lastTimestamp);
                                    requireActivity().runOnUiThread(() -> tvData.append(display + "\n"));
                                } else {
                                    lastUID = "";
                                    lastTimestamp = 0;
                                    requireActivity().runOnUiThread(() ->
                                            Toast.makeText(getContext(),
                                                    "Unrecognized RFID:\n" + scannedUID + "\nExpected:\n" + registeredUID,
                                                    Toast.LENGTH_LONG).show());
                                }
                            }
                        })
                        .addOnFailureListener(Throwable::printStackTrace);
            }

        } else if (line.startsWith("SECOND")) {
            String[] secondParts = line.split(" @| INTERVAL:");
            if (secondParts.length == 3 && !lastUID.isEmpty()) {
                long secondTimestamp = Long.parseLong(secondParts[1]);
                long interval = Long.parseLong(secondParts[2]);

                String display = "Second scan: at " + formatMillis(secondTimestamp) + ", Interval: " + formatDuration(interval);
                requireActivity().runOnUiThread(() -> {
                    tvData.append(display + "\n");
                    showResultPopup(lastUID, interval);
                });
            } else {
                Log.d("RFID_DEBUG", "Second scan ignored: UID mismatch or FIRST scan not done.");
            }
        }
    }


    private String formatMillis(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
    }

    private String formatDuration(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        return String.format("%d hrs %d mins %d secs", hours, minutes % 60, seconds % 60);
    }

    private void showResultPopup(String uid, long intervalMillis) {
        long seconds = intervalMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
        int totalPrice = 5;
        if (hours >= 1) {
            totalPrice += (hours) * 2;
        }

        String receipt = "========== Parking Receipt ==========\n"
                + "UID: " + uid + "\n"
                + "Duration: " + timeFormatted + "\n"
                + "-------------------------------------\n"
                + "Base Fee: $5\n"
                + (hours > 0 ? "Additional: $" + (hours * 2) + "\n" : "")
                + "-------------------------------------\n"
                + "Total: $" + totalPrice + "\n"
                + "=====================================";

        final int finalPrice = totalPrice;

        new AlertDialog.Builder(requireContext())
                .setTitle("Payment Summary")
                .setMessage(receipt)
                .setPositiveButton("Pay", (dialog, which) -> {
                    Toast.makeText(getContext(), "Payment of $" + finalPrice + " confirmed", Toast.LENGTH_SHORT).show();
                    if (isAdded()) {
                        showSuccessAnimation();
                        saveReceiptToFirestore(uid, timeFormatted, finalPrice);
                    }
                })
                .show();
    }

    private void saveReceiptToFirestore(String uid, String timeFormatted, int totalPrice) {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "unknown_user";
        long timestamp = System.currentTimeMillis();

        db.collection("Users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");
                        String licensePlate = documentSnapshot.getString("licensePlate");

                        Map<String, Object> receiptData = new HashMap<>();
                        receiptData.put("uid", uid);
                        receiptData.put("duration", timeFormatted);
                        receiptData.put("total", totalPrice);
                        receiptData.put("timestamp", timestamp);
                        receiptData.put("name", name);
                        receiptData.put("email", email);
                        receiptData.put("licensePlate", licensePlate);

                        // Save receipt under user's own receipts
                        db.collection("Users")
                                .document(userId)
                                .collection("receipts")
                                .add(receiptData);

                        // Save receipt under admin user by UID (Gi3xgfOfYRW2BN9BBv0qG3pH1c03)
                        db.collection("Users")
                                .document("Gi3xgfOfYRW2BN9BBv0qG3pH1c03")  // Admin user UID document
                                .collection("receipts")                   // Receipts subcollection
                                .add(receiptData)
                                .addOnSuccessListener(docRef -> {
                                    Log.d("Firestore", "Admin receipt saved with ID: " + docRef.getId());
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Error saving admin receipt", e);
                                });
                    }
                })
                .addOnFailureListener(Throwable::printStackTrace);
    }



    private void showSuccessAnimation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.dialog_success_animation, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        view.postDelayed(dialog::dismiss, 2000);
    }
}
