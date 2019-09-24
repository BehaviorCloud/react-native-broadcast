
package com.pedrolibrary;

import java.util.Map;

import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import static com.facebook.react.common.ReactConstants.TAG;


public class RNBroadcastViewManager extends SimpleViewManager<RNBroadcastView> {

  public static final String REACT_CLASS = "RNBroadcastView";
  public static final int COMMAND_STOP_PUBLISH = 1;
  private RNBroadcastView mBroadcastView = null;

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  public RNBroadcastView createViewInstance(ThemedReactContext context) {
    mBroadcastView = new RNBroadcastView(context);

    Log.d(TAG, "About to return from createViewInstance");

    return mBroadcastView;
  }

  @ReactProp(name = "publish")
  public void started(RNBroadcastView view, @Nullable String publish) {
    System.out.println("Starting: " + publish);

    mBroadcastView.setPublish(publish);
  }

  @Override
  public Map<String,Integer> getCommandsMap() {
    return MapBuilder.of(
            "stopPublish",
            COMMAND_STOP_PUBLISH);
  }

  @Override
  public void receiveCommand(
          RNBroadcastView view,
          int commandType,
          @Nullable ReadableArray args) {
    Assertions.assertNotNull(view);
    Assertions.assertNotNull(args);
    switch (commandType) {
      case COMMAND_STOP_PUBLISH: {
        mBroadcastView.handleStopPublish();
        return;
      }

      default:
        throw new IllegalArgumentException(String.format(
                "Unsupported command %d received by %s.",
                commandType,
                getClass().getSimpleName()));
    }
  }

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


}

