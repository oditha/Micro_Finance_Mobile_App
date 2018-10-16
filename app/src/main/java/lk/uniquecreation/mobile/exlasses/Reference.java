package lk.uniquecreation.mobile.exlasses;

public class Reference {


//    public static String url = "http://139.59.69.165:8080/AndroidWebServiceCMC/AndroidService";
    public static String url = "http://139.59.30.89:8080/GlassFishServiceORITMA/AndroidService";
//    public static String url = "http://10.0.3.2:8084/AndroidWebServiceCMC/AndroidService";
    public static int timeout = 10000;
    public static String namespace = "http://services/";

    //inner class

    public static class loginService {
        public static String METHOD_NAME = "loginService";
        public static String SOAP_ACTION = "http://services/loginService";
    }

    public static class loadCenters {
        public static String METHOD_NAME = "loadCenterShedule";
        public static String SOAP_ACTION = "http://services/loadCenterShedule";
    }

    public static class loadPaymentBulk {
        public static String METHOD_NAME = "loadPaymentBulk";
        public static String SOAP_ACTION = "http://services/loadPaymentBulk";
    }
    public static class searchMember {
        public static String METHOD_NAME = "searchNpMember";
        public static String SOAP_ACTION = "http://services/searchNpMember";
    }
    public static class saveSinglePayment {
        public static String METHOD_NAME = "saveSinglePayment";
        public static String SOAP_ACTION = "http://services/saveSinglePayment";
    }

}
