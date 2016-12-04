package gjcm.kxf.soundatest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;
import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

//完全自定义处理类
public class KXFPushIntentService extends UmengMessageService implements InitListener {
    private static final String TAG = "kxflog";
    private String strTitle, strMsg,path;

    private SpeechSynthesizer mTts;
private Context mycontext;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("kxflog", "onCreate:--------------- ");
        mTts = SpeechSynthesizer.createSynthesizer(getApplicationContext(), null);
    }

    @Override
    public void onMessage(final Context context, Intent intent) {
        final String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        mycontext=context;
        new Thread() {
            @Override
            public void run() {
                Log.i("kxflog", "onMessage:--------------- ");
                UMessage msg = null;
                try {
                    msg = new UMessage(new JSONObject(message));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                strTitle = msg.title;
                strMsg = msg.text;
//        FlowerCollector.onEvent(this, "tts_play");
//        setParam();
                mTts.setParameter(SpeechConstant.PARAMS, null);
                mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
                mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
                mTts.setParameter(SpeechConstant.SPEED, "50");//音速
                mTts.setParameter(SpeechConstant.PITCH, "50");//设置合成音调
                mTts.setParameter(SpeechConstant.VOLUME, "80");//合成音量
                //设置播放器音频流类型
                mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
                // 设置播放合成音频打断音乐播放，默认为true
                mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
                mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
                 path = Environment.getExternalStorageDirectory() + "/kxfs.wav";
                mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, path);
                int code = mTts.synthesizeToUri(strMsg, path, mTtsListener);
                if (code != ErrorCode.SUCCESS) {
                    Log.e("kxflog", "语音合成失败,错误码: " + code);
                }
//                try {
//                    Thread.sleep(300);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

            }
        }.start();
    }


    /*private void setParam() {
        mTts.setParameter(SpeechConstant.PARAMS, null);
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        mTts.setParameter(SpeechConstant.SPEED, "50");//音速
        mTts.setParameter(SpeechConstant.PITCH, "50");//设置合成音调
        mTts.setParameter(SpeechConstant.VOLUME, "80");//合成音量
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "5");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, sUrl);
    }*/

    @Override
    public void onInit(int code) {
        Log.d("kxflog", "InitListener init() code = " + code);
        if (code != ErrorCode.SUCCESS) {
            Log.d("kxflog", "初始化失败,错误码：" + code);
        } else {
            // 初始化成功，之后可以调用startSpeaking方法
            // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
            // 正确的做法是将onCreate中的startSpeaking调用移至这里
        }
    }

    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            Log.e("kxflog", "onSpeakBegin:-----");
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakResumed() {
            Log.e("kxflog", "onSpeakResumed:-----");
        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {
            Log.e("kxflog", "onSpeakProgress:-----");
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            Intent showintent = new Intent(mycontext, ShowNotificationReceiver.class);
            showintent.putExtra("title", strTitle);
            showintent.putExtra("msg", strMsg);
            showintent.putExtra("sound", path);
            mycontext.sendBroadcast(showintent);
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
            Log.e("kxflog", "onEvent:-----");
        }
    };

    @Override
    public void onDestroy() {
        mTts.stopSpeaking();
        mTts.destroy();

        super.onDestroy();
    }
}
