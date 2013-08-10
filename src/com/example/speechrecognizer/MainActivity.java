package com.example.speechrecognizer;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity
 * @author naotaro
 *
 */
@SuppressLint("ShowToast")
public class MainActivity extends Activity implements OnClickListener, RecognitionListener{
    Object mSpeechRecognizer;
    private TextView text1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // listener登録
        ((Button)findViewById(R.id.button1)).setOnClickListener(this);
        text1 = (TextView)findViewById(R.id.textView1);
        //SpeechRecognizer生成
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        ((SpeechRecognizer) mSpeechRecognizer).setRecognitionListener(this);
    }

    /*
     * ボタンクリックイベント
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button1:
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
            //  音声認識開始
            ((SpeechRecognizer) mSpeechRecognizer).startListening(intent);
            break;
        default:
            break;
        }
    }
    // 音声認識準備完了
    @Override
    public void onReadyForSpeech(Bundle params) {
        Toast.makeText(this, "音声認識準備完了", Toast.LENGTH_SHORT);
        text1.setText("音声認識　準備完了！");
    }
     
    // 音声入力開始
    @Override
    public void onBeginningOfSpeech() {
        Toast.makeText(this, "入力開始", Toast.LENGTH_SHORT);
        text1.setText("音声入力　開始！");
    }
    
    // 録音データのフィードバック用
    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.v("DEBUG","onBufferReceived");
    }
    
    // audio stream のサウンドレベルが変わったとき
    @Override
    public void onRmsChanged(float rmsdB) {
        Log.v("DEBUG","recieve : " + rmsdB + "dB");
    }
    
    // 音声入力終了
    @Override
    public void onEndOfSpeech() {
        Toast.makeText(this, "入力終了", Toast.LENGTH_SHORT);
        text1.setText("音声入力　終了！");
    }
    
    // ネットワークエラー又は、音声認識エラー
    @Override
    public void onError(int error) {
        switch (error) {
        case SpeechRecognizer.ERROR_AUDIO:
            // 音声データ保存失敗
            Toast.makeText(this, "音声データ保存失敗", Toast.LENGTH_LONG);
            break;
        case SpeechRecognizer.ERROR_CLIENT:
            // Android端末内のエラー(その他)
            Toast.makeText(this, "Android端末内のエラー(その他)", Toast.LENGTH_LONG);
            break;
        case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
            // 権限無し
            Toast.makeText(this, "権限無し", Toast.LENGTH_SHORT);
            break;
        case SpeechRecognizer.ERROR_NETWORK:
            // ネットワークエラー(その他)
            Toast.makeText(this, "ネットワークエラー(その他)", Toast.LENGTH_LONG);
            Log.e("DEBUG", "network error");
            break;
        case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
            Toast.makeText(this, "ネットワークタイムアウトエラー", Toast.LENGTH_LONG);
            // ネットワークタイムアウトエラー
            Log.e("DEBUG", "network timeout");
            break;
        case SpeechRecognizer.ERROR_NO_MATCH:
            // 音声認識結果無し
            Toast.makeText(this, "音声認識結果無し", Toast.LENGTH_LONG);
            break;
        case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
            // RecognitionServiceへ要求出せず
            Toast.makeText(this, "RecognitionServiceへ要求出せず", Toast.LENGTH_LONG);
            break;
        case SpeechRecognizer.ERROR_SERVER:
            // Server側からエラー通知
            Toast.makeText(this, "Server側からエラー通知", Toast.LENGTH_LONG);
            break;
        case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
            // 音声入力無し
            Toast.makeText(this, "音声入力無し", Toast.LENGTH_LONG);
            Toast.makeText(this, "no input?", Toast.LENGTH_LONG);
            break;
        default:
        }
    }
    
    // イベント発生時に呼び出される
    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.v("DEBUG","onEvent");
    }
    
    // 部分的な認識結果が得られる場合に呼び出される
    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.v("DEBUG","onPartialResults");
    }
    
    // 音声認識結果
    @Override
    public void onResults(Bundle results) {
        // 音声認識結果リストを受け取る
        ArrayList<String> recData = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        // ArrayListから配列に変換
        final String[] str_items = (String[])recData.toArray(new String[recData.size()]);
        // アラートリストダイアログを表示する
        new AlertDialog.Builder(MainActivity.this)
        .setTitle("音声結果")
        .setItems(str_items, new DialogInterface.OnClickListener(){
            // リストを選択した時
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, str_items[which] + "を選択しました", Toast.LENGTH_LONG).show();
            }
        }
        ).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
