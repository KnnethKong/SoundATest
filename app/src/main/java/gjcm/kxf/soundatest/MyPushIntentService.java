package gjcm.kxf.soundatest;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

//完全自定义处理类
public class MyPushIntentService extends UmengMessageService implements TextToSpeech.OnInitListener {
    private static final String TAG = "kxflog";
    private TextToSpeech speech;
    private String strTitle, strMsg;
    private File f;

    @Override
    public void onMessage(Context context, Intent intent) {
        try {
         String fileurl=   Environment.getExternalStorageDirectory().toString()+"/test.wav";
            speech = new TextToSpeech(getApplicationContext(), this);
            String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
            strTitle = msg.title;
            strMsg = msg.text;
            f = new File(fileurl);
            if (f.exists()) {
                f.delete();
            }

            int code = speech.synthesizeToFile(strMsg, null,f,null);
            Log.i("kxflog", code + "code" + "      edit:" + strMsg);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent showintent = new Intent(this, ShowNotificationReceiver.class);
            showintent.putExtra("title",strTitle);
            showintent.putExtra("msg",strMsg);
            showintent.putExtra("sound",strMsg);
            context.sendBroadcast(showintent);
        } catch (Exception e) {
            e.printStackTrace();
        }


        /**   speech = new TextToSpeech(getApplicationContext(), this);
         new Thread() {
        @Override public void run() {
        try {
        UMessage msg = new UMessage(new JSONObject(message));
        strTitle = msg.title;
        strMsg = msg.text;

        f = new File(Environment.getExternalStorageDirectory(), "test.wav");
        if (f.exists()) {
        f.delete();
        }
        int code = speech.synthesizeToFile(strMsg, null, f, "");
        Log.i("kxflog", code + "code" + "      edit:" + strMsg);
        try {
        Thread.sleep(500);
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
        Notification.Builder builder = new Notification.Builder(MyPushIntentService.this);
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("www.baidu.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(MyPushIntentService.this, 0, mIntent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setAutoCancel(true);
        builder.setVisibility(Notification.VISIBILITY_PRIVATE);//VISIBILITY_PUBLIC 只有在没有锁屏时会显示通知
        //  VISIBILITY_PRIVATE 任何情况都会显示通知
        // VISIBILITY_SECRET 在安全锁和没有锁屏的情况下显示通知
        builder.setContentTitle(strTitle);
        builder.setContentText(strMsg);
        builder.setTicker("状态栏上显示");// 状态栏上显示
        //      Uri uri=  Uri.fromFile(f);
        //        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri sound = Uri.parse("file:///storage/emulated/0/test.wav");
        builder.setSound(sound);
        builder.setVibrate(new long[]{0, 200, 100, 200});//
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(362, builder.build());
        } catch (JSONException e) {
        e.printStackTrace();
        }
        }
        }.start();
         try {
         UMessage msg = new UMessage(new JSONObject(message));
         JSONObject json = new JSONObject(msg.custom);
         String topic = json.getString("topic");
         UmLog.d(TAG, "topic=" + topic);
         if (topic != null && topic.equals("appName:startService")) {
         // 在友盟portal上新建自定义消息，自定义消息文本如下
         //{"topic":"appName:startService"}
         if (Helper.isServiceRunning(context, MyNotificationService.class.getName()))
         return;
         Intent intent1 = new Intent();
         intent1.setClass(context, MyNotificationService.class);
         context.startService(intent1);
         } else if (topic != null && topic.equals("appName:stopService")) {
         // 在友盟portal上新建自定义消息，自定义消息文本如下
         //{"topic":"appName:stopService"}
         if (!Helper.isServiceRunning(context, MyNotificationService.class.getName()))
         return;
         Intent intent1 = new Intent();
         intent1.setClass(context, MyNotificationService.class);
         context.stopService(intent1);
         }
         } catch (Exception e) {
         UmLog.e(TAG, e.getMessage());
         }**/
    }

    /**
     * private void settingSound() {
     * Log.i("kxflog", "settingSound:-----" + "code");
     * Notification.Builder builder = new Notification.Builder(this);
     * Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("www.baidu.com"));
     * PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0);
     * builder.setContentIntent(pendingIntent);
     * builder.setSmallIcon(R.mipmap.ic_launcher);
     * builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
     * builder.setAutoCancel(true);
     * builder.setVisibility(Notification.VISIBILITY_PRIVATE);//VISIBILITY_PUBLIC 只有在没有锁屏时会显示通知
     * //  VISIBILITY_PRIVATE 任何情况都会显示通知
     * // VISIBILITY_SECRET 在安全锁和没有锁屏的情况下显示通知
     * builder.setContentTitle(strTitle);// 设置通知的标题
     * builder.setContentText(strMsg);// 设置通知的内容
     * builder.setTicker("状态栏上显示");// 状态栏上显示
     * //      Uri uri=  Uri.fromFile(f);
     * //        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
     * Uri sound = Uri.parse("file:///storage/emulated/0/test.wav");
     * builder.setSound(sound);
     * builder.setVibrate(new long[]{0, 200, 100, 200});//
     * NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
     * notificationManager.notify(362, builder.build());
     * <p/>
     * }
     **/
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = speech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("kxflog", "数据丢失或不支持:-----");

                Toast.makeText(this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (speech != null) {
            speech.stop();
            speech.shutdown();
        }
        super.onDestroy();
    }
}
