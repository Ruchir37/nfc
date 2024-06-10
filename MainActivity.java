package com.sailors.audiorecorder.chatgpt;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sailors.audiorecorder.R;


public class MainActivity extends AppCompatActivity {


    Button startRecord;
    Button stopRecord;

    private static final int REQUEST_CODE_PERMISSIONS = 200;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private AudioRecorder audioRecorder;
    private String pcmFilePath;
    private String wavFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pcmFilePath = getExternalFilesDir(null).getAbsolutePath() + "/audio.pcm";
        wavFilePath = getExternalFilesDir(null).getAbsolutePath() + "/audio.wav";

        startRecord = findViewById(R.id.startRecord);
        stopRecord = findViewById(R.id.stopRecord);

        if (allPermissionsGranted()) {
            initAudioRecorder();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                initAudioRecorder();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void initAudioRecorder() {
        audioRecorder = new AudioRecorder(pcmFilePath);

        startRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording(v);
            }
        });

        stopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording(v);
            }
        });
    }

    public void startRecording(View view) {
        if (audioRecorder != null) {
            audioRecorder.startRecording();
        }
    }

    public void stopRecording(View view) {
        if (audioRecorder != null) {
            audioRecorder.stopRecording();
            PcmToWavConverter.pcmToWav(pcmFilePath, wavFilePath);
            String base64String = Base64Converter.convertFileToBase64(wavFilePath);

            String fileName = "audiobase64.txt";

            FileUtil.writeStringToFile(this, fileName, base64String);
            Log.d("Base64String", base64String);
        }
    }

}
