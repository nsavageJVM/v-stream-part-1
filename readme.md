### Video Streaming Demo App
for vlcj please see [vlcj](https://github.com/caprica/vlcj)  
based on [vlcj-javafx-demo](https://github.com/caprica/vlcj-javafx)  
see  [blog Java video streaming part 1](http://nsavagejvm.netlify.com/2017/01/java-video-streaming-part-1/) for details  

### Set up
in src/main/resources/application.properties repalace `your api key` with api key from you tube  

replace USER_HOME_DIR with the name of your home directory

#### Debian  
`./gradlew clean build -x test`  

`java -jar`  `-Dvs.properties=/home/USER_HOME_DIR/aJavaFXSpectrum/v-stream/runtime.properties` `build/libs/v-stream-0.0.1-SNAPSHOT.jar`

#### Debian set runtime.properties

app.video.source=/home/USER_HOME_DIR/aJavaFXSpectrum/v-stream/test_vid.mp4  
install vlc  
app.video.native.dir=/usr/lib

#### Windows 7
`./gradlew clean build -x test`  
  
`java -jar`  `-Dvs.properties=C:\\Users\\USER_HOME_DIR\\v-stream-part-1\\runtime.properties`   `build/libs/v-stream-0.0.1-SNAPSHOT.jar`

#### Windows 7 set runtime.properties

app.video.source=C:\\Users\\USER_HOME_DIR\\v-stream-part-1\\test_vid.mp4  
install vlc then if 64 bit  
app.video.native.dir=C:\\Program Files\\VideoLAN\\VLC  
if 32 bit  
app.video.native.dir=C:\\Program Files(x86)\\VideoLAN\\VLC
