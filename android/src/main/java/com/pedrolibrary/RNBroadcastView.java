package com.pedrolibrary;

import android.content.Context;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.react.uimanager.ThemedReactContext;
import com.pedro.encoder.input.video.CameraOpenException;
import com.pedro.rtplibrary.rtmp.RtmpCamera2;
import net.ossrs.rtmp.ConnectCheckerRtmp;

import static com.facebook.react.common.ReactConstants.TAG;


public class RNBroadcastView extends FrameLayout implements
        ConnectCheckerRtmp, SurfaceHolder.Callback {
    private SurfaceView mCameraView;
    private ThemedReactContext mContext = null;
    private RtmpCamera2 rtmpCamera2;
    private Boolean isLive = false;
    private Boolean surfaceExists = false;
    private String streamUrl = "";

    public RNBroadcastView(@NonNull ThemedReactContext context) {
        super(context);
        // context.addLifecycleEventListener(this);
        mContext = context;

        mCameraView = new SurfaceView(context);
        mCameraView.getHolder().addCallback(this);

        addView(mCameraView);

        rtmpCamera2 = new RtmpCamera2(mCameraView, this);
        rtmpCamera2.setReTries(10);

        Log.d(TAG, "About to return from RNBroadcastView");
    }

    private void handleException(Exception e) {
        try {
            rtmpCamera2.stopStream();
        } catch (Exception e1) {
            //
        }
    }

    public void setPublish(String publishUrl) {
        if (!publishUrl.isEmpty()) {
            AudioManager am = (AudioManager) this.mContext.getSystemService(Context.AUDIO_SERVICE);
            am.setSpeakerphoneOn(true);
            am.setMode(AudioManager.MODE_IN_COMMUNICATION);
            streamUrl = publishUrl;
            this.startStreaming();
        }
        else if (publishUrl.isEmpty() && this.isLive){
            AudioManager am = (AudioManager) this.mContext.getSystemService(Context.AUDIO_SERVICE);
            am.setSpeakerphoneOn(true);
            am.setMode(AudioManager.MODE_NORMAL);
            this.stopStreaming();
        }
    }

    public void handleStopPublish() {
        AudioManager am = (AudioManager) this.mContext.getSystemService(Context.AUDIO_SERVICE);
        am.setSpeakerphoneOn(true);
        am.setMode(AudioManager.MODE_NORMAL);
        this.stopStreaming();
    }

    public void startStreaming() {
        if (surfaceExists && !streamUrl.isEmpty()) {
            if (rtmpCamera2.isRecording()
                    || rtmpCamera2.prepareAudio() && rtmpCamera2.prepareVideo()) {
                rtmpCamera2.startStream(streamUrl);
                isLive = true;
            } else {
//      Toast.makeText(this, "Error preparing stream, This device cant do it",
//              Toast.LENGTH_SHORT).show();
                isLive = false;
            }
        }
    }

    public void stopStreaming() {
        rtmpCamera2.stopStream();
        isLive = false;
    }

    @Override
    public void onConnectionSuccessRtmp() {
//    runOnUiThread(new Runnable() {
//      @Override
//      public void run() {
//        Toast.makeText(MainActivity.this, "Connection success", Toast.LENGTH_SHORT)
//                .show();
//      }
//    });
    }

    @Override
    public void onConnectionFailedRtmp(final String reason) {
//    runOnUiThread(new Runnable() {
//      @Override
//      public void run() {
//        if (rtmpCamera2.shouldRetry(reason)) {
//          Toast.makeText(MainActivity.this, "Retry", Toast.LENGTH_SHORT)
//                  .show();
//          rtmpCamera2.reTry(5000);  //Wait 5s and retry connect stream
//        } else {
//          Toast.makeText(MainActivity.this, "Connection failed. " + reason, Toast.LENGTH_SHORT).show();
//          //rtmpCamera2.stopStream();
//          // button.setText(R.string.start_button);
//          stopClick();
//        }
//      }
//    });
    }

    @Override
    public void onNewBitrateRtmp(long bitrate) {

    }

    @Override
    public void onDisconnectRtmp() {
//    runOnUiThread(new Runnable() {
//      @Override
//      public void run() {
//        Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
//      }
//    });
    }

    @Override
    public void onAuthErrorRtmp() {
//    runOnUiThread(new Runnable() {
//      @Override
//      public void run() {
//        Toast.makeText(MainActivity.this, "Auth error", Toast.LENGTH_SHORT).show();
//      }
//    });
    }

    @Override
    public void onAuthSuccessRtmp() {
//    runOnUiThread(new Runnable() {
//      @Override
//      public void run() {
//        Toast.makeText(MainActivity.this, "Auth success", Toast.LENGTH_SHORT).show();
//      }
//    });
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "SurfaceCreated");

        // Set the surface exists flag to true and
        // hit the startStreaming function in case we already have a URL
        surfaceExists = true;
        startStreaming();

//    if (rtmpCamera2.isRecording()
//            || rtmpCamera2.prepareAudio() && rtmpCamera2.prepareVideo()) {
//      rtmpCamera2.startStream("rtmp://"+Constants.STREAMING_BASE_URL+":"+Constants.STREAMING_PORT+"/live/" + mDevicePath);
//      isServicePersisted = true;
//      dismissStreamDialog();
//    } else {
//      Toast.makeText(this, "Error preparing stream, This device cant do it",
//              Toast.LENGTH_SHORT).show();
//      isServicePersisted = false;
//      showStreamDialog();
//    }
//
//    updateServiceIndicator();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d(TAG, "SurfaceChanged");
        rtmpCamera2.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        surfaceExists = false;
        if (rtmpCamera2.isStreaming()) {
            stopStreaming();
        }
        rtmpCamera2.stopPreview();
    }
}
