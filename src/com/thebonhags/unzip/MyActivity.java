package com.thebonhags.unzip;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
            FileInputStream fileInputStream = new FileInputStream("/sdcard/Download/Blue_Chips_2-(DatPiff.com).zip");
            ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
            ZipEntry zipEntry = null;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                // Attempt to play it
                zipInputStream.read();

            }

            ZipFile zipFile = new ZipFile("/sdcard/Download/Blue_Chips_2-(DatPiff.com).zip");
            Enumeration entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                Log.d("UNZIP", entry.getName());
                InputStream inputStream = zipFile.getInputStream(entry);

                int streamType = AudioManager.STREAM_MUSIC;
                int sampleRateInHz = 44100;
                int channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
                int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
                int bufferSizeInBytes = 44100; //AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
                int mode = AudioTrack.MODE_STREAM;

                AudioTrack audioTrack = new AudioTrack(streamType,
                        sampleRateInHz,
                        channelConfig,
                        audioFormat,
                        bufferSizeInBytes,
                        mode);

                byte[] buffer = new byte[bufferSizeInBytes];
                Log.d("UNZIP", "Allocated new buffer of size " + bufferSizeInBytes);

                int bytesRead = 0;

                audioTrack.play();

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    Log.d("UNZIP", "Bytes read: " + bytesRead);
                    int bytesWritten = audioTrack.write(buffer, 0, bytesRead);
                    Log.d("UNZIP", "Bytes written: " + bytesWritten);
                }

                inputStream.close();

                audioTrack.stop();
                audioTrack.release();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
