package lk.uniquecreation.mobile.mobile_app_mc;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.uniquecreation.mobile.exlasses.Blutooth;
import lk.uniquecreation.mobile.exlasses.PaymentHolder;
import lk.uniquecreation.mobile.exlasses.Reference;
import lk.uniquecreation.mobile.exlasses.decimalFormats;

public class PaymentActivity extends AppCompatActivity {

    Map<String, String> cenid = new HashMap<>();
    Map<String, PaymentHolder> mapPayment = new HashMap<>();

    List<String> members = new ArrayList<>();

    int current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        loadCenters();

    }

    public void loadMembers(View view) {

        System.out.println("START PRICE : " + mapPayment.size());

        mapPayment.clear();

        System.out.println("END PRICE : " + mapPayment.size());

        Spinner spinner = findViewById(R.id.spinner);
        String selected = String.valueOf(spinner.getSelectedItem());

        String center = cenid.get(selected.split(" - ")[0]);
        SoapObject request = new SoapObject(Reference.namespace, Reference.loadPaymentBulk.METHOD_NAME);

        PropertyInfo idcenter = new PropertyInfo();
        idcenter.namespace = "";
        idcenter.name = "center";
        idcenter.type = PropertyInfo.STRING_CLASS;
        idcenter.setValue(center);
        request.addProperty(idcenter);

        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(Reference.url, Reference.timeout);
            transportSE.call(Reference.loadPaymentBulk.SOAP_ACTION, envelope);
            String response = envelope.getResponse().toString();
            members.clear();
//            System.out.println("RES " + response);
            if (!response.equals("NOANY")) {


                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jo = jsonArray.getJSONObject(i);
                    members.add(jo.get("memberno").toString());

                }

            }


            if (members.size() == 0) {

                Toast.makeText(PaymentActivity.this, "No any active loans in this centre", Toast.LENGTH_LONG).show();


            } else {

                System.out.println("CALLMETHOD");
                Collections.sort(members);
                loadRecords(0);


            }

        } catch (IOException e) {
            Toast.makeText(PaymentActivity.this, e.toString(), Toast.LENGTH_LONG).show();

            e.printStackTrace();
        } catch (XmlPullParserException e) {
            Toast.makeText(PaymentActivity.this, e.toString(), Toast.LENGTH_LONG).show();

            e.printStackTrace();
        } catch (Exception e) {
            Toast.makeText(PaymentActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }

    public void ignorePayment(View view) {

        try {

            current++;

            loadRecords(current);

        } catch (Exception e) {

            Toast.makeText(PaymentActivity.this, "No members remaining. Try next centre", Toast.LENGTH_LONG).show();


        }

    }

    public void nextMember(View view) {

        try {


            TextView idloan = findViewById(R.id.lblIdLoan);
            TextView member = findViewById(R.id.lblMemberNo);
            TextView membername = findViewById(R.id.lblMemberName);
            TextView contactno = findViewById(R.id.lblContractNo);
            TextView center = findViewById(R.id.lblCenter);
            TextView centerday = findViewById(R.id.lblCenterDay);
            TextView loanamount = findViewById(R.id.lblLoanAmount);
            TextView installment = findViewById(R.id.lblInstallment);
            TextView balance = findViewById(R.id.lblBalance);
            TextView arrears = findViewById(R.id.lblArreas);
            EditText payment = findViewById(R.id.txtPaid);

            PaymentHolder holder = new PaymentHolder();
            holder.setArrears(arrears.getText().toString());
            holder.setBalance(balance.getText().toString());
            holder.setCenter(center.getText().toString());
            holder.setCenterday(centerday.getText().toString());
            holder.setContractno(contactno.getText().toString());
            holder.setIdloan(idloan.getText().toString());
            holder.setInstallment(installment.getText().toString());
            holder.setLoanamount(loanamount.getText().toString());
            holder.setMembername(membername.getText().toString());
            holder.setMemberno(member.getText().toString());
            holder.setPayment(payment.getText().toString());

            mapPayment.put(idloan.getText().toString(), holder);
            System.out.println("LIST SIZE _ " + mapPayment.size());

            System.out.println("Saved " + idloan.getText());
//        System.out.println(members.get(current));
            current++;
            System.out.println(current);
            if (current > members.size() - 1) {

                TextView viewById = findViewById(R.id.lblIdLoan);
                System.out.println("Saved " + viewById.getText());
//            System.out.println(members.get(current));
                Toast.makeText(PaymentActivity.this, "No members remaining. Try next centre", Toast.LENGTH_LONG).show();

            } else {


                loadRecords(current);

            }
        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(PaymentActivity.this, e.toString(), Toast.LENGTH_LONG).show();

        }

    }

    public void loadRecords(int get) {

        try {
            System.out.println("INMETHOD");
            current = get;
            String memno = members.get(get);
            SoapObject request2 = new SoapObject(Reference.namespace, Reference.searchMember.METHOD_NAME);
            PropertyInfo memberno = new PropertyInfo();
            memberno.namespace = "";
            memberno.name = "member";
            memberno.setValue(members.get(get));
            request2.addProperty(memberno);

            SoapSerializationEnvelope envelope2 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope2.setOutputSoapObject(request2);
            HttpTransportSE httpTransportSE = new HttpTransportSE(Reference.url, Reference.timeout);
            httpTransportSE.call(Reference.searchMember.SOAP_ACTION, envelope2);

            String response2 = envelope2.getResponse().toString();
//            System.out.println(response2);

            if (response2.equals("NOANY") || response2.equals("NOMEMBER")) {

                Toast.makeText(PaymentActivity.this, "Invalid Member or inactive loan", Toast.LENGTH_LONG).show();


            } else {


                TextView idloan = findViewById(R.id.lblIdLoan);
                TextView member = findViewById(R.id.lblMemberNo);
                TextView membername = findViewById(R.id.lblMemberName);
                TextView contactno = findViewById(R.id.lblContractNo);
                TextView center = findViewById(R.id.lblCenter);
                TextView centerday = findViewById(R.id.lblCenterDay);
                TextView loanamount = findViewById(R.id.lblLoanAmount);
                TextView installment = findViewById(R.id.lblInstallment);
                TextView balance = findViewById(R.id.lblBalance);
                TextView arrears = findViewById(R.id.lblArreas);
                EditText payment = findViewById(R.id.txtPaid);


                JSONArray jsonArray = new JSONArray(response2);

                JSONObject jsonObject = jsonArray.getJSONObject(0);

                idloan.setText(jsonObject.get("IdLoan").toString());
                member.setText("Member No : " + memno);
                membername.setText("Member Name : " + jsonObject.get("memberName").toString());
                contactno.setText("Contact No : " + jsonObject.get("contractNo").toString());
                center.setText("Center : " + jsonObject.get("center").toString());
                centerday.setText("Center Day : " + jsonObject.get("dday").toString());
                loanamount.setText("Loan + Interest : " + decimalFormats.setAmount(Double.parseDouble(jsonObject.get("loanamount").toString())));
                installment.setText("Installment : " + decimalFormats.setAmount(Double.parseDouble(jsonObject.get("installment").toString())));
                payment.setText(jsonObject.get("installment").toString());
                balance.setText("Balance : " + decimalFormats.setAmount(Double.parseDouble(jsonObject.get("balance").toString())));
                arrears.setText("Arrears : " + decimalFormats.setAmount(Double.parseDouble(jsonObject.get("Arrears").toString())));

                payment.requestFocus();
                payment.setSelection(payment.getText().length());

            }

        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(PaymentActivity.this, e.toString(), Toast.LENGTH_LONG).show();

        } catch (XmlPullParserException e) {

            e.printStackTrace();
            Toast.makeText(PaymentActivity.this, e.toString(), Toast.LENGTH_LONG).show();

        } catch (JSONException e) {

            Toast.makeText(PaymentActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void confirmPayment(View view) {

        new AlertDialog.Builder(this)
                .setTitle("Confirm Payment")
                .setMessage("Do you really want to Save?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            JSONArray array = new JSONArray();
                            for (Map.Entry<String, PaymentHolder> entry : mapPayment.entrySet()) {

                                JSONObject object = new JSONObject();
                                PaymentHolder value = entry.getValue();

                                object.put("repayment", value.getInstallment().split(" : ")[1]);
                                object.put("Paid", value.getPayment());
                                object.put("acDay", value.getCenterday().split(" : ")[1]);
                                object.put("Loan", value.getLoanamount().split(" : ")[1]);
                                object.put("idLoan", value.getIdloan());
                                object.put("staffid", MainActivity.STAFFID);
                                object.put("username", MainActivity.USERNAME);
                                array.put(object);

                            }

                            System.out.println(array.toString());


                            SoapObject request = new SoapObject(Reference.namespace, Reference.saveSinglePayment.METHOD_NAME);
                            PropertyInfo json = new PropertyInfo();
                            json.namespace = "";
                            json.name = "json";
                            json.type = PropertyInfo.STRING_CLASS;
                            json.setValue(array.toString());

                            request.addProperty(json);

                            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            envelope.setOutputSoapObject(request);

                            HttpTransportSE transportSE = new HttpTransportSE(Reference.url, Reference.timeout);
                            transportSE.call(Reference.saveSinglePayment.SOAP_ACTION, envelope);
                            String response = envelope.getResponse().toString();


                            if (response.equals("DONE")) {

                                Toast.makeText(PaymentActivity.this, "Payments Complete", Toast.LENGTH_SHORT).show();
                                Collections.sort(members);

                                for (Map.Entry<String, PaymentHolder> mp : mapPayment.entrySet()) {

                                    Blutooth bt = new Blutooth();
                                    bt.findBT();
                                    bt.openBT();
                                    PaymentHolder value = mp.getValue();

                                    double curBalance = Double.parseDouble(value.getBalance().split(" : ")[1]);
                                    double paid = Double.parseDouble(value.getPayment().toString());
                                    double narrears = Double.parseDouble(value.getArrears().split(" : ")[1]);

//                            double curBalance  = 10500;
//                            double paid = Double.parseDouble(payment.getText().toString());
//                            double narrears = 2500;

                                    double newBalance = curBalance - paid;

                                    if (newBalance < 0) {

                                        newBalance = 0.00;
                                    }

                                    double newArrears = narrears - paid;

                                    if (newArrears < 0) {

                                        newArrears = 0.00;
                                    }


                                    String PRINT_DATA = "";
                                    PRINT_DATA = value.getMembername() + "\n"
                                            + value.getMemberno() + "\n"
                                            + value.getContractno() + "\n"
                                            + value.getLoanamount() + "\n"
                                            + "Paid Amount : " + decimalFormats.setAmount(Double.parseDouble(value.getPayment())) + "\n"
                                            + "Arrears : " + decimalFormats.setAmount(newArrears) + "\n"
                                            + "Pre. Balance : " + decimalFormats.setAmount(curBalance) + "\n"
                                            + "New Balance : " + decimalFormats.setAmount(newBalance) + "\n"
                                    ;

                                    bt.sendData(PRINT_DATA);
                                    bt.closeBT();
                                    Thread.sleep(3500);
                                }

                            } else {

                                Toast.makeText(PaymentActivity.this, "Payment Error", Toast.LENGTH_SHORT).show();


                            }


                        } catch (Exception e) {

                            Toast.makeText(PaymentActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                            e.printStackTrace();

                        }

                    }
                })
                .setNegativeButton(android.R.string.no, null).show();

    }

    private void loadCenters() {

        System.out.println("START LOAD");
        Spinner spinner = findViewById(R.id.spinner);
        SoapObject request = new SoapObject(Reference.namespace, Reference.loadCenters.METHOD_NAME);

        PropertyInfo staffid = new PropertyInfo();
        staffid.namespace = "";
        staffid.name = "staffid";
        staffid.type = PropertyInfo.STRING_CLASS;
        staffid.setValue(MainActivity.STAFFID);
        request.addProperty(staffid);


        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE transportSE = new HttpTransportSE(Reference.url, Reference.timeout);
            transportSE.call(Reference.loadCenters.SOAP_ACTION, envelope);
            String toString = envelope.getResponse().toString();

            JSONArray jsonArray = new JSONArray(toString);
            List<String> list = new ArrayList<>();
            cenid.clear();
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jo = jsonArray.getJSONObject(i);

                String centerno = jo.get("centerno").toString();
                String idcenter = jo.get("idcenter").toString();
                String centername = jo.get("centername").toString();

                cenid.put(centerno, idcenter);
                list.add(centerno + " - " + centername);

            }

            Collections.sort(list);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(PaymentActivity.this, e.toString(), Toast.LENGTH_LONG).show();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Toast.makeText(PaymentActivity.this, e.toString(), Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(PaymentActivity.this, e.toString(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(PaymentActivity.this, e.toString(), Toast.LENGTH_LONG).show();

        }


    }
}
