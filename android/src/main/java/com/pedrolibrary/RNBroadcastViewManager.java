
package com.pedrolibrary;

import java.io.IOException;
import java.net.SocketException;
import java.util.Map;

import android.content.Context;
import android.media.AudioManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import com.pedro.encoder.input.video.CameraOpenException;
import com.pedro.rtplibrary.rtmp.RtmpCamera2;
import net.ossrs.rtmp.ConnectCheckerRtmp;


import static com.facebook.react.common.ReactConstants.TAG;


public class RNBroadcastViewManager extends SimpleViewManager<SurfaceView> implements
        ConnectCheckerRtmp, SurfaceHolder.Callback {

  public static final String REACT_CLASS = "RNPedroBroadcastView";
  public static final int COMMAND_STOP_PUBLISH = 1;
  private SurfaceView mCameraView;
  private RtmpCamera2 rtmpCamera2;
  private ThemedReactContext mContext = null;
  private Boolean isLive = false;


  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  public SurfaceView createViewInstance(ThemedReactContext context) {
    this.mContext = context;

    mCameraView = new SurfaceView(context);
    mCameraView.getHolder().addCallback(this);

    rtmpCamera2 = new RtmpCamera2(mCameraView, this);
    rtmpCamera2.setReTries(10);
  }

  private void handleException(Exception e) {
    try {
      rtmpCamera2.stopStream();
    } catch (Exception e1) {
      //
    }
  }

  public void startStreaming(String url) {
    if (rtmpCamera2.isRecording()
            || rtmpCamera2.prepareAudio() && rtmpCamera2.prepareVideo()) {
      rtmpCamera2.startStream(url);
      isLive = true;
    } else {
//      Toast.makeText(this, "Error preparing stream, This device cant do it",
//              Toast.LENGTH_SHORT).show();
      isLive = false;
    }
  }

  public void stopStreaming() {
    rtmpCamera2.stopStream();
    isLive = false;
  }

  @ReactProp(name = "publish")
  public void started(SurfaceView view, @Nullable String publish) {
    System.out.println("Starting: " + publish + "live: " + this.isLive);

    if (!publish.isEmpty()) {
      AudioManager am = (AudioManager) this.mContext.getSystemService(Context.AUDIO_SERVICE);
      am.setSpeakerphoneOn(true);
      am.setMode(AudioManager.MODE_IN_COMMUNICATION);
      this.startStreaming(publish);
    }
    else if (publish.isEmpty() && this.isLive){
      AudioManager am = (AudioManager) this.mContext.getSystemService(Context.AUDIO_SERVICE);
      am.setSpeakerphoneOn(true);
      am.setMode(AudioManager.MODE_NORMAL);
      this.stopStreaming();
    }
  }

  @Override
  public Map<String,Integer> getCommandsMap() {
    return MapBuilder.of(
            "stopPublish",
            COMMAND_STOP_PUBLISH);
  }

  @Override
  public void receiveCommand(
          SurfaceView view,
          int commandType,
          @Nullable ReadableArray args) {
    Assertions.assertNotNull(view);
    Assertions.assertNotNull(args);
    switch (commandType) {
      case COMMAND_STOP_PUBLISH: {
        AudioManager am = (AudioManager) this.mContext.getSystemService(Context.AUDIO_SERVICE);
        am.setSpeakerphoneOn(true);
        am.setMode(AudioManager.MODE_NORMAL);
        this.stopStreaming();
        return;
      }

      default:
        throw new IllegalArgumentException(String.format(
                "Unsupported command %d received by %s.",
                commandType,
                getClass().getSimpleName()));
    }
  }

//  @Override
//  public void onRtmpConnected(String msg) {
////    Toast.makeText(this.mContext, msg, Toast.LENGTH_SHORT).show();
//    WritableMap event = Arguments.createMap();
//    this.mContext.getJSModule(RCTEventEmitter.class).receiveEvent(
//            this.mCameraView.getId(),
//            "onBroadcastStart",
//            event);
//  }

  /**
   * This method maps the sending of the "onClick" event to the JS "onClick" function.
   */
  @Nullable @Override
  public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
    return MapBuilder.<String, Object>builder()
//            .put("onBroadcastStart",
//                    MapBuilder.of("registrationName", "onBroadcastStart"))
            .build();
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
    if (rtmpCamera2.isStreaming()) {
      stopStreaming();
    }
    rtmpCamera2.stopPreview();
  }
}

