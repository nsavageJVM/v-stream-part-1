package fish.beans.video;

import fish.beans.util.JNASetUp;
import javafx.scene.image.WritableImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Component
@Slf4j
public class VlcjMediaPlayerFactory {

    private DirectMediaPlayer directMediaPlayer;
    private MediaPlayerFactory vlcjFactory;

    @Autowired
    private DefaultRenderCallback renderCallback;

    @Qualifier("jnasetup")
    @Autowired
    private JNASetUp jnasetup;


    @Qualifier("writableimage")
    @Autowired
    private WritableImage writableimage;

    public DirectMediaPlayer mediaPlayer() {
        return directMediaPlayer;
    }

    @PostConstruct
    public void init() {
        jnasetup.initialize();
        // vlcj Factory class
        vlcjFactory = new MediaPlayerFactory();

        directMediaPlayer = vlcjFactory.newDirectMediaPlayer(new VLCJBufferFormatCallback(), renderCallback);
    }

    class VLCJBufferFormatCallback implements BufferFormatCallback {

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {

            return new RV32BufferFormat((int) writableimage.getWidth(), (int) writableimage.getHeight());

        }
    }

    @PreDestroy
    public void onDestroy() {

        if (directMediaPlayer != null) {
            directMediaPlayer.release();
        }

        if (vlcjFactory != null) {
            vlcjFactory.release();
        }
    }





}
