package com.feicuiedu.treasure;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.feicuiedu.treasure.commons.ActivityUtils;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 *
 * 作者：yuanchao on 2016/7/22 0022 14:09
 * 邮箱：yuanchao@feicuiedu.com
 */
public class MainMP4Fragment extends Fragment implements TextureView.SurfaceTextureListener {

    private TextureView textureView;// 用来播放视频的view
    private MediaPlayer mediaPlayer;
    private ActivityUtils activityUtils;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        textureView = new TextureView(getContext());
        return textureView;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityUtils = new ActivityUtils(this);
        textureView.setSurfaceTextureListener(this);
    }

    // 的确准备好了
    @Override public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
        try {
            AssetFileDescriptor afd = getContext().getAssets().openFd("welcome.mp4");
            FileDescriptor fd = afd.getFileDescriptor();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fd, afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepareAsync(); // 异步
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override public void onPrepared(MediaPlayer mp) {
                    Surface mpSurface = new Surface(surface);
                    mediaPlayer.setSurface(mpSurface);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            activityUtils.showToast("媒体播放失败");
        }
    }

    @Override public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}