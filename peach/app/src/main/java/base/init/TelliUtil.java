package base.init;

/**
 * Created by minhua on 2015/10/26.
 */
public class TelliUtil {
    public static String buisnessPackage = "";
    public static void initBuisnessPackage(String buisnessPackage){
        TelliUtil.buisnessPackage = buisnessPackage;
    }

    public static String getBuisnessPackage() {
        return buisnessPackage;
    }

    public static String returnparsename = "";
    public static void init(String returnparsename){
        TelliUtil.returnparsename = returnparsename;
    }

    public static String getReturnparsename() {
        return returnparsename;
    }
}
