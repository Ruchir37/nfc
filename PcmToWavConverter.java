package com.sailors.audiorecorder.chatgpt;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PcmToWavConverter {
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNELS = 1;
    private static final int BITS_PER_SAMPLE = 16;

    public static void pcmToWav(String pcmPath, String wavPath) {
        FileInputStream pcmInputStream = null;
        FileOutputStream wavOutputStream = null;

        try {
            pcmInputStream = new FileInputStream(pcmPath);
            wavOutputStream = new FileOutputStream(wavPath);

            long pcmSize = pcmInputStream.getChannel().size();
            long dataSize = pcmSize + 36;

            writeWavHeader(wavOutputStream, pcmSize, dataSize);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = pcmInputStream.read(buffer)) != -1) {
                wavOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pcmInputStream != null) {
                    pcmInputStream.close();
                }
                if (wavOutputStream != null) {
                    wavOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeWavHeader(FileOutputStream out, long pcmSize, long dataSize) throws IOException {
        out.write("RIFF".getBytes());
        out.write(intToByteArray((int) dataSize));
        out.write("WAVE".getBytes());
        out.write("fmt ".getBytes());
        out.write(intToByteArray(16));
        out.write(shortToByteArray((short) 1));
        out.write(shortToByteArray((short) CHANNELS));
        out.write(intToByteArray(SAMPLE_RATE));
        out.write(intToByteArray(SAMPLE_RATE * CHANNELS * BITS_PER_SAMPLE / 8));
        out.write(shortToByteArray((short) (CHANNELS * BITS_PER_SAMPLE / 8)));
        out.write(shortToByteArray((short) BITS_PER_SAMPLE));
        out.write("data".getBytes());
        out.write(intToByteArray((int) pcmSize));
    }

    private static byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value & 0xff),
                (byte) ((value >> 8) & 0xff),
                (byte) ((value >> 16) & 0xff),
                (byte) ((value >> 24) & 0xff)
        };
    }

    private static byte[] shortToByteArray(short value) {
        return new byte[]{
                (byte) (value & 0xff),
                (byte) ((value >> 8) & 0xff)
        };
    }
}
