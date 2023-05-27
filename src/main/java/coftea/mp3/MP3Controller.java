package coftea.mp3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.*;

public class MP3Controller implements Initializable {
    @FXML
    AnchorPane backgroundPane;
    @FXML
    Label mediaLabel;
    @FXML
    ProgressBar mediaProgressBar;
    @FXML
    Slider volumeSlider;
    @FXML
    ComboBox<String> speedComboBox;
    @FXML
    Button playButton,pauseButton,resetButton,previousButton,nextButton;
    private Media media;
    private MediaPlayer player;

    private List<File> songs;
    private int songNumber;
    private final double[] speeds ={0.25,0.5,0.75,1,1.25,1.5,1.75,2};
    private Timer timer;
    private boolean running;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songs = new ArrayList<>();
        File directory = new File("D:\\ADDS\\APPLICATIONS\\JavaFX\\MusicsForJavaFX");
        File[] files = directory.listFiles();
        if(files != null){
            songs.addAll(Arrays.asList(files));
        }
        media = new Media(songs.get(songNumber).toURI().toString());
        player = new MediaPlayer(media);
        mediaLabel.setText(songs.get(songNumber).getName());

        for (double speed : speeds) {
            speedComboBox.getItems().add(speed +"x");
        }
        speedComboBox.setOnAction(this::changeSpeed);
        volumeSlider.valueProperty().addListener(
                (a,b,c)-> player.setVolume(volumeSlider.getValue()*0.01)
        );
    }

    public void play(){
        if(mediaProgressBar.getProgress()==1){
            mediaProgressBar.setProgress(0.0);
            player.seek(Duration.seconds(0.0));
        }
        initTimer();
        changeSpeed(null);
        player.setVolume(volumeSlider.getValue());
        player.play();
    }
    public void pause(){
        cancelTimer();
        player.pause();
    }
    public void reset(){
        player.seek(Duration.seconds(0.0));
        mediaProgressBar.setProgress(0);
    }
    public void previous(){
        if(songNumber > 0){
            songNumber--;
        }else{
            songNumber = 0;
        }
        player.stop();
        if(running){
            cancelTimer();
        }
        media = new Media(songs.get(songNumber).toURI().toString());
        player = new MediaPlayer(media);
        mediaLabel.setText(songs.get(songNumber).getName());
        player.setVolume(volumeSlider.getValue()*0.01);
        play();
    }
    public void nextMedia(){
        if(songNumber < songs.size() -1){
            songNumber++;
        }else{
            songNumber = 0;
        }
        player.stop();
        if(running){
            cancelTimer();
        }
        media = new Media(songs.get(songNumber).toURI().toString());
        player = new MediaPlayer(media);
        mediaLabel.setText(songs.get(songNumber).getName());
        player.setVolume(volumeSlider.getValue()*0.01);
        play();
    }
    public void changeSpeed(ActionEvent event){
        if(speedComboBox.getValue()==null){
            player.setRate(1);
        }else {
            String comboBoxValue = speedComboBox.getValue().substring(0, speedComboBox.getValue().length() - 1);
            player.setRate(Double.parseDouble(comboBoxValue));
        }
    }
    public void initTimer(){
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                running = true;
                double current = player.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                mediaProgressBar.setProgress(current / end);
                if (current / end == 1) {
                    cancelTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(task,1000,1000);
    }
    public void cancelTimer(){
        running = false;
        timer.cancel();
    }
    public void changeDurationMedia(){
        mediaProgressBar.setOnMouseClicked(mouseEvent ->
        {
            double mouseX = mouseEvent.getX();
            double progressBarWidth = mediaProgressBar.getWidth();
            double currentProgress = mouseX / progressBarWidth;
            mediaProgressBar.setProgress(currentProgress);

            double fullMediaTime = player.getMedia().getDuration().toSeconds();
            double currentMediaTime =(mouseX * fullMediaTime)/progressBarWidth;
            player.seek(Duration.seconds(currentMediaTime));
        });
    }
}