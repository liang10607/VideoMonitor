package com.liang.videomonitor.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Created by prafly-software on 2017/4/28.
 */

public class VideoSurfceView extends SurfaceView {

    public VideoSurfceView(Context context) {
        super(context);
    }

    public VideoSurfceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VideoSurfceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public VideoSurfceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }




}
