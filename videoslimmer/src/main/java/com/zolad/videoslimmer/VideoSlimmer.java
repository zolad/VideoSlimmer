package com.zolad.videoslimmer;


/**
 * Video Slimmer
 * a library to convert video to smaller mp4 file
 * can set new width ,height, and bitrate ,progress listner
 */
public class VideoSlimmer {


    public static VideoSlimTask convertVideo(String srcPath, String destPath, int outputWidth, int outputHeight, int bitrate, ProgressListener listener) {
        VideoSlimTask task = new VideoSlimTask(listener);
        task.execute(srcPath, destPath, outputWidth, outputHeight, bitrate);
        return task;
    }


    public static interface ProgressListener {

        void onStart();
        void onFinish(boolean result);
        void onProgress(float progress);

    }

}
