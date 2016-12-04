package gjcm.kxf.soundatest;

import android.os.Environment;

import java.io.File;

/**
 * Created by kxf on 2016/12/2.
 */
public class Tools {
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }
}
