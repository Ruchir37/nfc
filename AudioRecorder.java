package com.sailors.audiorecorder.chatgpt;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.FileOutputStream;
import java.io.IOException;

public class AudioRecorder {
    private static final int SAMPLE_RATE = 44100;
    private boolean isRecording = false;
    private AudioRecord audioRecord;
    private String filePath;

    public AudioRecorder(String filePath) {
        this.filePath = filePath;
    }

    public void startRecording() {
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

        audioRecord.startRecording();
        isRecording = true;

        new Thread(() -> {
            writeAudioDataToFile(bufferSize);
        }).start();
    }

    private void writeAudioDataToFile(int bufferSize) {
        byte[] audioData = new byte[bufferSize];
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(filePath);
            while (isRecording) {
                int read = audioRecord.read(audioData, 0, bufferSize);
                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                    fos.write(audioData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRecording() {
        if (audioRecord != null) {
            isRecording = false;
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }
}
