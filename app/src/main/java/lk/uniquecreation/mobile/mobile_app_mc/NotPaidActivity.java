package lk.uniquecreation.mobile.mobile_app_mc;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import lk.uniquecreation.mobile.exlasses.Blutooth;
import lk.uniquecreation.mobile.exlasses.Reference;
import lk.uniquecreation.mobile.exlasses.decimalFormats;

public class NotPaidActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_paid);
        EditText member = findViewById(R.id.editText);
        member.requestFocus();
    }

    public void loadMember(View view) {

        try {
            EditText member = findViewById(R.id.editText);
            String memberno = member.getText().toString();

            SoapObject request = new SoapObject(Reference.namespace, Reference.searchMember.METHOD_NAME);

            PropertyInfo memberNO = new PropertyInfo();
            memberNO.namespace = "";
            memberNO.name = "member";
            memberNO.type = PropertyInfo.STRING_CLASS;
            memberNO.setValue(memberno);

            request.addProperty(memberNO);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(Reference.url, Reference.timeout);
            transportSE.call(Reference.searchMember.SOAP_ACTION, envelope);

            String response = envelope.getResponse().toString();

            TextView idloan = findViewById(R.id.lblIdLoan);
            TextView memberid = findViewById(R.id.lblMemberNo);
            TextView membername = findViewById(R.id.lblMemberName);
            TextView contactno = findViewById(R.id.lblContractNo);
            TextView center = findViewById(R.id.lblCenter);
            TextView centerday = findViewById(R.id.lblCenterDay);
            TextView loanamount = findViewById(R.id.lblLoanAmount);
            TextView installment = findViewById(R.id.lblInstallment);
            TextView balance = findViewById(R.id.lblBalance);
            TextView arrears = findViewById(R.id.lblArreas);
            EditText payment = findViewById(R.id.txtPaid);

            JSONArray jsonArray = new JSONArray(response);

            JSONObject jsonObject = jsonArray.getJSONObject(0);

            idloan.setText(jsonObject.get("IdLoan").toString());
            memberid.setText("Member No : " + memberno);
            membername.setText("Member Name : " + jsonObject.get("memberName").toString());
            contactno.setText("Contact No : " + jsonObject.get("contractNo").toString());
            center.setText("Center : " + jsonObject.get("center").toString());
            centerday.setText("Center Day : " + jsonObject.get("dday").toString());
            loanamount.setText("Loan + Interest : " + decimalFormats.setAmount(Double.parseDouble(jsonObject.get("loanamount").toString())));
            installment.setText("Installment : " + decimalFormats.setAmount(Double.parseDouble(jsonObject.get("installment").toString())));
            payment.setText(jsonObject.get("installment").toString());
            balance.setText("Balance : " + decimalFormats.setAmount(Double.parseDouble(jsonObject.get("balance").toString())));
            arrears.setText("Arrears : " + decimalFormats.setAmount(Double.parseDouble(jsonObject.get("Arrears").toString())));

            EditText paid = findViewById(R.id.txtPaid);
            paid.requestFocus();
            paid.setSelection(paid.getText().length());

        } catch (Exception e) {

            Toast.makeText(NotPaidActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }


    }

    public void makePayments(View view) {

        final TextView idloan = findViewById(R.id.lblIdLoan);
        final TextView member = findViewById(R.id.lblMemberNo);
        final TextView membername = findViewById(R.id.lblMemberName);
        final TextView contactno = findViewById(R.id.lblContractNo);
        final TextView center = findViewById(R.id.lblCenter);
        final TextView centerday = findViewById(R.id.lblCenterDay);
        final TextView loanamount = findViewById(R.id.lblLoanAmount);
        final TextView installment = findViewById(R.id.lblInstallment);
        final TextView balance = findViewById(R.id.lblBalance);
        final TextView arrears = findViewById(R.id.lblArreas);
        final EditText payment = findViewById(R.id.txtPaid);

        new AlertDialog.Builder(this)
                .setTitle("Confirm Payment")
                .setMessage("Do you really want to Save?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            JSONArray array = new JSONArray();


                            JSONObject object = new JSONObject();


                            object.put("repayment", installment.getText().toString().split(" : ")[1]);
                            object.put("Paid", payment.getText().toString());
                            object.put("acDay", centerday.getText().toString().split(" : ")[1]);
                            object.put("Loan", loanamount.getText().toString().split(" : ")[1]);
                            object.put("idLoan", idloan.getText().toString());
                            object.put("staffid", MainActivity.STAFFID);
                            object.put("username", MainActivity.USERNAME);

                            array.put(object);


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
//                            String response = "DONE";

                            double curBalance  = Double.parseDouble(balance.getText().toString().split(" : ")[1]);
                            double paid = Double.parseDouble(payment.getText().toString());
                            double narrears = Double.parseDouble(arrears.getText().toString().split(" : ")[1]);

//                            double curBalance  = 10500;
//                            double paid = Double.parseDouble(payment.getText().toString());
//                            double narrears = 2500;

                            double newBalance = curBalance - paid;
                            double newArrears = narrears - paid;

                            if (newBalance < 0) {

                                newBalance = 0.00;
                            }

                            if (newArrears < 0) {

                                newArrears = 0.00;
                            }



                            if (response.equals("DONE")) {

                                Toast.makeText(NotPaidActivity.this, "Payment Saved", Toast.LENGTH_SHORT).show();
                                Blutooth bt = new Blutooth();
                                bt.findBT();
                                bt.openBT();


                                String PRINT_DATA = "";
                                PRINT_DATA = member.getText()+"\n"
                                            +membername.getText()+"\n"
                                            +contactno.getText()+"\n"
                                            +loanamount.getText()+"\n"
                                            +"Paid Amount : "+decimalFormats.setAmount(Double.parseDouble(payment.getText().toString()))+"\n"
                                            +"Arrears : "+decimalFormats.setAmount(newArrears)+"\n"
                                            +"Pre. Balance : "+decimalFormats.setAmount(curBalance)+"\n"
                                            +"New Balance : "+decimalFormats.setAmount(newBalance)+"\n"
                                ;

                                bt.sendData(PRINT_DATA);
                                bt.closeBT();


                            } else {

                                Toast.makeText(NotPaidActivity.this, "Payment Error", Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {

                            Toast.makeText(NotPaidActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                            e.printStackTrace();

                        }

                    }
                })
                .setNegativeButton(android.R.string.no, null).show();

    }

}
