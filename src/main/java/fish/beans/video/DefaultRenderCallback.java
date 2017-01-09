package fish.beans.video;

import com.sun.jna.Memory;
import javafx.application.Platform;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;

import javax.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.util.Objects;

@Slf4j
@Component
public class DefaultRenderCallback implements RenderCallback {

    private final PixelFormat<ByteBuffer> PIXEL_FORMAT = PixelFormat.getByteBgraInstance();

    @Qualifier("writableimage")
    @Autowired
    private WritableImage writableimage;

    private int maxWidth;
    private int maxHeight;


    @PostConstruct
    public void init() {
        maxWidth = (int) writableimage.getWidth();
        maxHeight = (int) writableimage.getHeight();
    }


    // == public methods ==
    @Override
    public void display(DirectMediaPlayer mediaPlayer, Memory[] nativeBuffers, BufferFormat bufferFormat) {


        maxWidth = (int) writableimage.getWidth();
        maxHeight = (int) writableimage.getHeight();

        if (writableimage == null) {
            return;
        }

        Platform.runLater(() -> renderFrame(mediaPlayer, nativeBuffers, bufferFormat));
    }

    private PixelWriter getPixelWriter() {
        return writableimage.getPixelWriter();
    }

    private void renderFrame(DirectMediaPlayer mediaPlayer, Memory[] nativeBuffers, BufferFormat bufferFormat) {

        // lock native memory buffers
        Memory[] memory = mediaPlayer.lock();

        Memory nativeBuffer = memory == null ? null : memory[0];

        try {
            if(Objects.nonNull(nativeBuffer)) {
                renderImpl(bufferFormat, nativeBuffer);
            }
        } catch (Exception e) {
            log.warn("Exception rendering frame", e);
        } finally {
            mediaPlayer.unlock();
        }

    }

    private void renderImpl(final BufferFormat bufferFormat, final Memory nativeBuffer) {

        ByteBuffer byteBuffer = nativeBuffer.getByteBuffer(0, nativeBuffer.size()-1);

        if(Objects.nonNull(byteBuffer)) {
            getPixelWriter().setPixels(0, 0, maxWidth, maxHeight, PIXEL_FORMAT, byteBuffer, bufferFormat.getPitches()[0]);
        }

    }
}
