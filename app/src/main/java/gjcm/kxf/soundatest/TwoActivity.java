package gjcm.kxf.soundatest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class TwoActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private EditText editText;
    private TextToSpeech speech;
    private Notification.Builder builder;
    private NotificationManager notificationManager;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.main_edit);
        speech = new TextToSpeech(this, this);


    }

    public void writeNote(View view) {
        try {
         String url=   Environment.getExternalStorageDirectory().getAbsolutePath();
//            writeFile("wode.txt", "woshishishshishish");
            writeFileSdcardFile(url+"/wode.txt", "woshishishshishish");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readeNote(View view) {

        try {
//            readFile("wode.txt");
            readFileSdcardFile("wode.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //写数据
    private void writeFile(String fileName, String writestr) throws IOException {
        try {
            FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);
            byte[] bytes = writestr.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读数据
    private String readFile(String fileName) throws IOException {
        String res = "";
        try {
            FileInputStream fin = openFileInput(fileName);

            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, "UTF-8");
            Log.i("kxflog", "res:-----" + res);
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }public void writeFileSdcardFile(String fileName,String write_str) throws IOException{
        try{

            FileOutputStream fout = new FileOutputStream(fileName);
            byte [] bytes = write_str.getBytes();
            fout.write(bytes);
            fout.close();
        }

        catch(Exception e){
            e.printStackTrace();
        }
    }


    //读SD中的文件
    public String readFileSdcardFile(String fileName) throws IOException{
        String res="";
        try{
            FileInputStream fin = new FileInputStream(fileName);

            int length = fin.available();

            byte [] buffer = new byte[length];
            fin.read(buffer);

            res =  new String(buffer, "UTF-8");

            fin.close();
        }

        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }


    public void settingSound() {
        Log.i("kxflog", "settingSound:-----" + "code");
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

//        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri sound = Uri.parse("file:///storage/emulated/0/test.wav");
        builder.setSound(sound);
        builder.setVibrate(new long[]{0, 200, 100, 200});//
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(362, builder.build());

    }

    public void mainPlay(View v) {
        File f = new File(Environment.getExternalStorageDirectory(), "test.wav");
        Uri soundUri = Uri.fromFile(f);
        String edit = editText.getText().toString();
        if (edit == null)
            return;
        if (speech != null && !speech.isSpeaking()) {
            speech.setPitch(0.9f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
//            speech.speak(edit,
//                    TextToSpeech.QUEUE_FLUSH, null);
            int code = speech.synthesizeToFile(edit, null, f, "");
            Log.i("kxflog", code + "code  soundUri:" + soundUri);
        }
    }

    public void mainSend(View v) {
        new Thread() {
            @Override
            public void run() {
                File f = new File(Environment.getExternalStorageDirectory(), "test.wav");
                String edit = "收入12.80元";
                if (f.exists()) {
                    f.delete();
                }
                int code = speech.synthesizeToFile(edit, null, f, "");
                Log.i("kxflog", code + "code");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                settingSound();

            }
        }.start();


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
