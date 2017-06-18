package base.init;

import android.content.Context;

import base.RRes;
import base.action.CenterRepo;
import base.file.PropUtil;

/**
 * Created by minhua on 2015/10/16.
 */
public class InitFacility {

    public static void init(Context context,String pcgprefix) {
        RRes.initR(context);
        try {
//            CenterRepo.getInsatnce().putinRepo(new PropUtil(context.getAssets().open("url.txt")).getMap());
//
//            PropUtil propconstant = new PropUtil(context.getAssets().open("constants.txt"));
            TelliUtil.initBuisnessPackage(pcgprefix);
//            TelliUtil.init(propconstant.getKeyValue("httpresultkey"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
