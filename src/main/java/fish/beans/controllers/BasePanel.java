package fish.beans.controllers;

import fish.beans.service.UTubeApi;
import fish.beans.util.JNASetUp;
import fish.beans.video.VlcjMediaPlayerFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.events.MediaPlayerEventType;

import javax.annotation.PostConstruct;
import java.util.List;


/**
 * Created by ubu on 12.12.16.
 */
@Slf4j
@Component
@Lazy
public class BasePanel {


    @Autowired
    VlcjMediaPlayerFactory vlcFactory;

    @Qualifier("writableimage")
    @Autowired
    private WritableImage writableimage;

    @Autowired
    @Qualifier("utubeapiservice")
    UTubeApi utubeApi;

    @Autowired
    @Qualifier("videosource")
    String videosource;


    @FXML
    private TextField matchText;

    @FXML
    private Label youTubeUrl;

    @FXML
    private ImageView videoImageView;

    private static final String DEMO_VIDEO = "test_vid.mp4";
    private MediaPlayer mediaPlayer;

    @PostConstruct
    void init() {

        mediaPlayer = vlcFactory.mediaPlayer();
    }


    @FXML
    void initialize() {
        mediaPlayer.enableEvents(MediaPlayerEventType.ALL.value());
        videoImageView.setImage(writableimage);
        videoImageView.setFitHeight(360);
        videoImageView.setFitWidth(640);
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void playDemo(ActionEvent actionEvent) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.prepareMedia(videosource);
        mediaPlayer.start();
    }

    public void playYouTube(ActionEvent actionEvent) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        if (matchText.getText() == null || matchText.getText().trim().isEmpty()) {
            youTubeUrl.setText("no text to match");
        } else {
            String result = utubeApi.runApiRequest(matchText.getText());

            mediaPlayer.setPlaySubItems(true);
            mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

                @Override
                public void buffering(MediaPlayer mediaPlayer, float newCache) {
                    System.out.println("Buffering " + newCache);
                }

                @Override
                public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
                    List<String> items = mediaPlayer.subItems();
                    System.out.println(items);
                }
            });
            youTubeUrl.setText(result);
            mediaPlayer.playMedia(result);
        }
    }

    public void stop(ActionEvent actionEvent) {
    }

    public void play(ActionEvent actionEvent) {
    }
}
