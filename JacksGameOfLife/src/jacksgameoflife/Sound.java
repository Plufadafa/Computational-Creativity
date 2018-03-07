package jacksgameoflife;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.midi.*;
import javax.swing.JOptionPane;
import java.net.URL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author jackn
 */
public class Sound {
    InputStream iAudio;
    /**
     *
     */
    public void ree() {
        try{
            iAudio = new FileInputStream(new File("/JavaProject\\JacksGameOfLife\\Sounds\\Sounds broken up\\1 EM Down.wav"));
            AudioStream iMusic = new AudioStream(iAudio);
            AudioPlayer.player.start(iMusic);
        } 
        catch (IOException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }

}
