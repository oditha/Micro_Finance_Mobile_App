package lk.uniquecreation.mobile.exlasses;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lk.uniquecreation.mobile.mobile_app_mc.MainActivity;

public class decimalFormats {

    private static DecimalFormat dcamount;
    private static SimpleDateFormat forContract;

    public static String setAmount(double amount){

        if (dcamount==null){

            dcamount = new DecimalFormat("##.00");

        }

        return dcamount.format(amount);


    }

    public static String ContractNo() {

        if (forContract == null) {

            forContract = new SimpleDateFormat("yyMMddhhmmss");

        }

        return forContract.format(new Date())+ MainActivity.STAFFID;

    }

}
