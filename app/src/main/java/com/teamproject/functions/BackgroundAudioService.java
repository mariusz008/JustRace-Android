package com.teamproject.functions;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.teamproject.activity.R;

/**
 * Created by 008M on 2016-05-28.
 */
public class BackgroundAudioService extends Service implements MediaPlayer.OnCompletionListener {
    MediaPlayer mediaPlayer;
    private int Rx;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mediaPlayer = MediaPlayer.create(this, R.raw.opuscilestrase);
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer = MediaPlayer.create(this, flags);
            mediaPlayer.start();
        }
        return START_STICKY;
    }

    public void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }


    public void onCompletion(MediaPlayer _mediaPlayer) {
        stopSelf();
    }

    public void setRx(int rx) {
        Rx = rx;
    }
}
