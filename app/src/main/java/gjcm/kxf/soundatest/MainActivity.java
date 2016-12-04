package gjcm.kxf.soundatest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private EditText editText;
    private TextToSpeech speech;
    Notification.Builder builder;
    private NotificationManager notificationManager;
    private Uri soundUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.main_edit);
        speech = new TextToSpeech(this, this);
        builder = new Notification.Builder(this);
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("www.baidu.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setAutoCancel(true);
        builder.setVisibility(Notification.VISIBILITY_PRIVATE);//VISIBILITY_PUBLIC 只有在没有锁屏时会显示通知
        //  VISIBILITY_PRIVATE 任何情况都会显示通知
        // VISIBILITY_SECRET 在安全锁和没有锁屏的情况下显示通知
        builder.setContentTitle("标题");// 设置通知的标题
        builder.setContentText("内容");// 设置通知的内容
        builder.setTicker("状态栏上显示");// 状态栏上显示
        builder.setVibrate(new long[]{0, 200, 100, 200});//
    }

    public void mainPlay(View v) {
        File f = new File(Environment.getExternalStorageDirectory(), "test.wav");
        Uri soundUri = Uri.fromFile(f);
        String edit = editText.getText().toString();
        if (edit == null)
            return;
        if (speech != null && !speech.isSpeaking()) {
            speech.setPitch(0.9f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
            speech.speak(edit,
                    TextToSpeech.QUEUE_FLUSH, null);
            int code = speech.synthesizeToFile(edit, null, f, "");
            Log.i("kxflog", code + "code  soundUri:" + soundUri);
        }
    }

    public void mainSend(View v) {
        builder.setSound(soundUri);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = speech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (speech != null) {
            speech.stop();
            speech.shutdown();
        }
        super.onDestroy();
    }
}
