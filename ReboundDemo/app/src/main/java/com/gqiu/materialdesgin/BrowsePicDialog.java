package com.gqiu.materialdesgin;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;

public class BrowsePicDialog extends Dialog implements SpringListener, View.OnClickListener {

    private SimpleDraweeView draweeView;
    private final Spring mSpring = SpringSystem
            .create()
            .createSpring()
            .addListener(this);

    private int mWidth, mHeight;
    private int mScreenW, mScreenH;
    private int mPaddingLeft, mPaddingTop;
    private RelativeLayout root;
    private Resources resources;
    private String mUrl;
    private DismissListener mListener;


    public BrowsePicDialog(Context context, int width, int height, int paddingLeft, int paddingTop, String url, DismissListener listener, boolean spring) {
        super(context, R.style.dialog);
        mWidth = width;
        mHeight = height;
        mPaddingLeft = paddingLeft;
        mPaddingTop = paddingTop;
        mUrl = url;
        mListener = listener;

        resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        mScreenW = dm.widthPixels;
        mScreenH = dm.heightPixels;

        if (spring) {
            mSpring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(40, 6));
        } else {
            mSpring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(200, 25));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        draweeView = (SimpleDraweeView) findViewById(R.id.draweeView);
        root = (RelativeLayout) findViewById(R.id.root);


        DisplayMetrics dm = resources.getDisplayMetrics();
        mScreenW = dm.widthPixels;
        mScreenH = dm.heightPixels;

        draweeView.setLayoutParams(new RelativeLayout.LayoutParams(mWidth, mHeight));
        draweeView.setImageURI(Uri.parse(mUrl));
        draweeView.setOnClickListener(this);

        draweeView.setScaleX(1);
        draweeView.setScaleY(1);
        draweeView.setAlpha(1.0f);
        draweeView.setTranslationX(mPaddingLeft);
        draweeView.setTranslationY(mPaddingTop);


        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                root.postOnAnimation(new Runnable() {
                    @Override
                    public void run() {
                        mSpring.setEndValue(1);
                    }
                });
            }
        });


        Window win = getWindow();
        win.setWindowAnimations(R.style.dialog_anima); //设置窗口弹出动画
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
    }


    @Override
    public void onSpringUpdate(Spring spring) {
        render();
    }

    @Override
    public void onSpringAtRest(Spring spring) {
        if (spring.getCurrentValue() == 0) {
            cancel();
            mListener.dismiss();
        }
    }

    @Override
    public void onSpringActivate(Spring spring) {
        mListener.show();
    }

    @Override
    public void onSpringEndStateChange(Spring spring) {
    }


    private void render() {
        float ww = mWidth;
        float hh = mHeight;
        float sx = mScreenW / ww;
        float sy = mScreenH / hh;
        float s = sx > sy ? sx : sy;
        float xlatX = (float) SpringUtil.mapValueFromRangeToRange(mSpring.getCurrentValue(), 0, 1, mPaddingLeft, 0);
        float xlatY = (float) SpringUtil.mapValueFromRangeToRange(mSpring.getCurrentValue(), 0, 1, mPaddingTop, 0);
        draweeView.setPivotX(0);
        draweeView.setPivotY(0);
        draweeView.setTranslationX(xlatX);
        draweeView.setTranslationY(xlatY);

        float ss = (float) SpringUtil.mapValueFromRangeToRange(mSpring.getCurrentValue(), 0, 1, 1, s);
        draweeView.setScaleX(ss);
        draweeView.setScaleY(ss);
    }

    @Override
    public void onClick(View v) {
        int endValue = mSpring.getEndValue() == 0 ? 1 : 0;
        mSpring.setEndValue(endValue);
    }


    public interface DismissListener {
        void dismiss();

        void show();
    }

}
