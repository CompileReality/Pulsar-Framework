package GameEngine.Sound;

import GameEngine.GameFactory;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioFactory extends PlayableAudio{

    File audioFile;
    AudioInputStream audioStream;
    Clip clip = AudioSystem.getClip();

    public AudioFactory(File audioFile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.audioFile = audioFile;
        audioStream = AudioSystem.getAudioInputStream(audioFile);
        clip.open(audioStream);
        super.clip = this.clip;
    }

    public AudioFactory(String Path, GameFactory game) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        audioFile = new File(game.property.ResourceFolder, Path);
        audioStream = AudioSystem.getAudioInputStream(audioFile);
        clip.open(audioStream);
        super.clip = this.clip;
    }
}
