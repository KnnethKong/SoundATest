package gjcm.kxf.soundatest;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by kxf on 2016/12/2.
 */
public class MyApplication extends Application {
    public String token;

    @Override
    public void onCreate() {
        super.onCreate();
        SpeechUtility.createUtility(this,  SpeechConstant.APPID +"=58426087," + SpeechConstant.FORCE_LOGIN +"=true");

        final PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        mPushAgent.setDebugMode(true);
//        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
//            @Override
//            public void dealWithCustomAction(Context context, UMessage msg) {
//                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//            }
//        };
//        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                token = deviceToken;
                Log.i("kxflog", "------------token:" + token);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.i("kxflog", "------------token:" + s1 + s);
            }
        });
//        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
        mPushAgent.setPushIntentServiceClass(KXFPushIntentService.class);

        String rid = mPushAgent.getRegistrationId();
        Log.e("kxflog", "rid:" + rid);
    }
}
