package lk.uniquecreation.mobile.mobile_app_mc;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import lk.uniquecreation.mobile.exlasses.Blutooth;

public class DashBoard extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Intent intent = getIntent();
        String logged = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView loggeduser =findViewById(R.id.loggedUser);
        loggeduser.setText("You logged as "+logged);



    }

    public void onBackPressed(){}


    public void openCenters(View view){


        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);

    }

    public void openSettings(View view){


        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);

    }

    public void openNotPad(View view) {


        Intent intent = new Intent(this, NotPaidActivity.class);
        startActivity(intent);

    }

    public void logout(View view){

        Intent Main = new Intent(this, MainActivity.class);
        startActivity(Main);

    }


}
