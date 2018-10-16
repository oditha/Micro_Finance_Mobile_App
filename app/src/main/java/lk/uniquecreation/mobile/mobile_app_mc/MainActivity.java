package lk.uniquecreation.mobile.mobile_app_mc;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import lk.uniquecreation.mobile.exlasses.Reference;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    public static String STAFFID;
    public static String USERNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        findViewById(R.id.uname).requestFocus();
        findViewById(R.id.uname).requestFocusFromTouch();
    }



    public void login(View view) {

        try {
            Intent intent = new Intent(this, DashBoard.class);

            SoapObject request = new SoapObject(Reference.namespace, Reference.loginService.METHOD_NAME);

            EditText unamef = findViewById(R.id.uname);
            EditText passf = findViewById(R.id.password);
            String uname = null;

            String pass = null;
            try {

                uname = unamef.getText().toString();
                pass = passf.getText().toString();

            } catch (Exception e) {

                e.printStackTrace();

            }
//            String uname = "sanjeewa";
//            String pass = "9122";

            PropertyInfo unameproperty = new PropertyInfo();
            unameproperty.namespace = "";
            unameproperty.name = "uname";
            unameproperty.type = PropertyInfo.STRING_CLASS;
            unameproperty.setValue(uname);
            request.addProperty(unameproperty);


            PropertyInfo passproperty = new PropertyInfo();
            passproperty.namespace = "";
            passproperty.name = "pass";
            passproperty.type = PropertyInfo.STRING_CLASS;
            passproperty.setValue(pass);
            request.addProperty(passproperty);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE transportSE = new HttpTransportSE(Reference.url, Reference.timeout);
            transportSE.call(Reference.loginService.SOAP_ACTION, envelope);

            String toString = envelope.getResponse().toString();
            System.out.println(toString);
            if (!toString.equals("ERROR")) {


                JSONObject jsonObject = new JSONObject(toString);
                USERNAME = jsonObject.get("uname").toString();
                STAFFID = jsonObject.get("uid").toString();


                Toast.makeText(MainActivity.this, "Login Success: Welcome " + USERNAME, Toast.LENGTH_LONG).show();
                intent.putExtra(EXTRA_MESSAGE, USERNAME);
                startActivity(intent);

            } else {

                Toast.makeText(MainActivity.this, "Login Error", Toast.LENGTH_LONG).show();

            }


        } catch (Exception e) {

            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();

            e.printStackTrace();

        }
//        catch (XmlPullParserException e) {
//
//            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//
//        } catch (JSONException e) {
//            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//        }


    }


    public void onBackPressed() {

//        finish();

    }

}
