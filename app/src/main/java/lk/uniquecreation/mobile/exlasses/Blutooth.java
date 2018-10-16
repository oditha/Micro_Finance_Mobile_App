package lk.uniquecreation.mobile.exlasses;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import lk.uniquecreation.mobile.mobile_app_mc.MainActivity;

public class Blutooth extends AppCompatActivity {

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    public void closeBT() throws IOException {
        try {

            System.out.println("START CLOSE");

            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();

            System.out.println("CLOSED");
//            myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendData(String text) throws IOException {
        try {

            // the text typed by the user
            String msg = text;
//            msg += "\n";

            String currentdate = "Date :" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "\n";
            String currenttime = "Time :" + new SimpleDateFormat("hh:mm:ss").format(new Date()) + "\n";
            String USER = "Cashier : " + MainActivity.USERNAME + "\n";


            mmOutputStream.write("ORITMA MICRO CREDIT INVESTMENT \n".getBytes());
            mmOutputStream.write("******STATION RD, BADULLA******\n".getBytes());
            mmOutputStream.write("---------077 351 0666----------\n".getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write("********PAYMENT RECEIPT********\n".getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write(currentdate.getBytes());
            mmOutputStream.write(currenttime.getBytes());
            mmOutputStream.write(USER.getBytes());
            mmOutputStream.write(msg.getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write("**THIS NOT REQUIRED SIGNATURE**\n".getBytes());
            mmOutputStream.write("*****THANK YOU FOR PAYMENT*****\n".getBytes());
            mmOutputStream.write("----IT BY UNIQUECREATION.LK----\n".getBytes());
            mmOutputStream.write("\n\n".getBytes());

            // tell the user data were sent
//            myLabel.setText("Data sent.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String findBT() {


        String ststus = null;
        System.out.println("START FIND BLUETOOTH PRINTER");
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {

                ststus = "No bluetooth adapter available";
//                status.setText("No bluetooth adapter available");
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("RG-MTP58B")) {
                        mmDevice = device;
                        break;
                    }
                }
            }

            ststus = "Bluetooth device found.";

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(ststus);
        return ststus;
    }

    public String openBT() throws IOException {
        try {

            System.out.println("OPEN BT PRINTER");

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();
            System.out.println("OPENED");
            return "Bluetooth Opened";

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ERROR";
    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
//                                                myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
