VideoSlimmer
==============
A video converter for Android to compress video using MediaCodec(decode and encode). 一个安卓视频压缩工具,使用了Mediacodec

![screenshot1~](https://raw.github.com/zolad/VideoSlimmer/master/screenshot/screenshot_1.gif)

Features
==============
- Using Mediacodec to decode and encode video,support mp4/3gp format
- High-performance


Dependency
==============
### Add this in your build.gradle file 
```gradle
compile 'com.zolad:videoslimmer:1.0.0'
```

Usage
==============
### Call VideoSlimmer.convertVideo and set params


```java

  /**
   * @param srcPath     string, filepath of source file
   * @param destPath    string, ouput filepath
   * @param outputWidth  pixels, output video width
   * @param outputHeight pixels, output video height
   * @param bitrate  int, in bits per second
   * @param listener  the listenr of convert progress
   */
  VideoSlimmer.convertVideo(srcPath, destPath, 200, 360, 200 * 360 * 30, new VideoSlimmer.ProgressListener() {
                        @Override
                        public void onStart() {
                           //convert start

                        }

                        @Override
                        public void onFinish(boolean result) {
                            //convert finish,result(true is success,false is fail)
                        }


                        @Override
                        public void onProgress(float percent) {
                           //percent of progress
                        }
                    });
        
```


License
==============

    Copyright 2018 Zolad

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
