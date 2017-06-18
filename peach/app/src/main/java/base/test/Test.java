package base.test;

import com.xygame.sg.imageloader.FileManager;

import java.util.Date;

import base.action.Action;
import base.file.PropUtil;
import base.frame.VisitUnit;

/**
 * Created by minhua on 2015/12/9.
 */
public class Test {
    public static String TEST_PATH = FileManager.getSaveFilePath() + "xy_sg/test/";
    private static int count;
    private static VisitUnit visitUnit = new VisitUnit();
    private static boolean stop;

    public static void test() {
        try {
            PropUtil proputil = new PropUtil("test");
            for (String key :
                    proputil.getMap().keySet()) {
                if (key.equals("")) {
                    continue;
                }
                final PropUtil p = new PropUtil(key.substring(key.lastIndexOf("/") + 1));
                if (proputil.getMap().get(key).toString().equals("") || !proputil.getMap().get(key).toString().contains("httpTest")) {
                    continue;
                }
                final int limit = 10;
                for (int i = 0; i < 100 * 10000; i++) {
                    if (stop) {
                        stop = false;
                        break;
                    }
                    new Action(proputil.getMap().get(key).toString(), null, null, visitUnit) {
                        @Override
                        public void catchException(Param aparam, Exception object) {
                            if (count == limit) {
                                stop = true;
                                count = 0;
                                return;
                            }
                            p.updateProperties(new Date().toString(), object.getMessage() + " cause: " + object.getCause().getMessage());
                            count++;
                            super.catchException(aparam, object);
                        }


                    }.run();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void tests(){
        test();
//        for(int i = 0;i<10000;i++){
//
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//
//            }
//        }.start();

    }
}
