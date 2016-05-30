package com.liuhaoyuan.myreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by liuhaoyuan on 2016/5/27.
 */
public class SplashActivity extends Activity {
    AppCompatImageView iv_splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        iv_splash = (AppCompatImageView) findViewById(R.id.iv_splash);
        copyDatabase();
        startAnimation();
    }

    private void startAnimation(){
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.3f,1);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setFillAfter(true);
        iv_splash.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                jumpToNextPage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void jumpToNextPage() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void copyDatabase() {
        File desFile = new File(getFilesDir(), "reader.db");
        if (desFile.exists()){
            return;
        }
        InputStream in = null;
        FileOutputStream outputStream = null;
        try {
            in = getAssets().open("reader.db");
            outputStream = new FileOutputStream(desFile);
            int len = 0;
            byte[] bs = new byte[1024];
            while ((len = in.read(bs)) != -1) {
                outputStream.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
