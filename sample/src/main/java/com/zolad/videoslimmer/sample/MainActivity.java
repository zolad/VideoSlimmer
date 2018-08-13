package com.zolad.videoslimmer.sample;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zolad.videoslimmer.VideoSlimmer;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/***
 * demo
 * */
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_FOR_VIDEO_FILE = 1000;
    private TextView tv_input, tv_output, tv_indicator, tv_progress;
    private String outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath();

    private String inputPath;
    private String outputPath;

    private ProgressBar pb_compress;

    private long startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<String> permissionReqlist = new ArrayList<String>();

        if (!PermissionUtil.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionReqlist.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }


        String[] reqarray = new String[permissionReqlist.size()];

        for (int i = 0; i < permissionReqlist.size(); i++) {
            reqarray[i] = permissionReqlist.get(i);
        }

        if (reqarray.length > 0)
            ActivityCompat.requestPermissions(this, reqarray, 100);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        Button btn_select = (Button) findViewById(R.id.btn_select);
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_FOR_VIDEO_FILE);
            }
        });


        tv_input = findViewById(R.id.tv_input);
        tv_output = findViewById(R.id.tv_output);
     //   tv_output.setText(outputDir);
        tv_indicator = findViewById(R.id.tv_indicator);
        tv_progress = findViewById(R.id.tv_progress);

        pb_compress = findViewById(R.id.pb_compress);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FOR_VIDEO_FILE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
//                inputPath = data.getData().getPath();
//                tv_input.setText(inputPath);

                try {
                    inputPath = Util.getFilePath(this, data.getData());


                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(inputPath);
                    String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                    String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                    String bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
                    String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    File f = new File(inputPath);
                    long fileSize = f.length();

                    String before = "inputPath:" +inputPath+ "\n" + "width:" + width + "\n" + "height:" + height + "\n" + "bitrate:" + bitrate + "\n"
                            + "fileSize:" + Formatter.formatFileSize(MainActivity.this,fileSize) +"\n" +"duration(ms):"+duration;
                    tv_input.setText(before);

                    final String destPath = outputDir + File.separator + "VIDEOSIMMER_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp4";
                    VideoSlimmer.convertVideo(inputPath, destPath, 200, 360, 200 * 360 * 30, new VideoSlimmer.ProgressListener() {
                        @Override
                        public void onStart() {
//                            tv_indicator.setText("Compressing..." + "\n"
//                                    + "Start at: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                            pb_compress.setVisibility(View.VISIBLE);

                            tv_indicator.setText("");



                        }

                        @Override
                        public void onFinish(boolean result) {

                            if (result) {

                                tv_progress.setText("100%");

//                                String previous = tv_indicator.getText().toString();
//                                tv_indicator.setText(previous + "\n"
//                                        + "Compress Success!" + "\n"
//                                        + "End at: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                                pb_compress.setVisibility(View.INVISIBLE);


                                tv_indicator.setText("Convert Success!");

                                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                retriever.setDataSource(destPath);
                                String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                                String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                                String bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);

                                File f = new File(destPath);
                                long fileSize = f.length();

                                String after = "outputPath:" +destPath+ "\n" + "width:" + width + "\n" + "height:" + height + "\n" + "bitrate:" + bitrate + "\n"
                                        + "fileSize:" + Formatter.formatFileSize(MainActivity.this,fileSize);
                                tv_output.setText(after);


                            } else {

                                tv_progress.setText("0%");

                                tv_indicator.setText("Convert Failed!");
                                pb_compress.setVisibility(View.INVISIBLE);
                                endTime = System.currentTimeMillis();
                                Util.writeFile(MainActivity.this, "Failed Compress!!!" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                            }
                        }


                        @Override
                        public void onProgress(float percent) {
                            tv_progress.setText(String.valueOf(percent) + "%");
                        }
                    });


                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }
}
