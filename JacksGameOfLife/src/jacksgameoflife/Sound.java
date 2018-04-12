package jacksgameoflife;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author jackn
 */
public class Sound {

    String emDown = "1 Em Down";
    String emDownUp = "1 Em DownUp";

    String eDown = "2 E Down";
    String eDownUp = "2 E DownUp";

    String amDown = "3 Am Down";
    String amDownUp = "3 Am DownUp";

    String cDown = "4 C Down";
    String cDownUp = "4 C DownUp";

    String aDown = "5 A Down";
    String aDownUp = "5 A DownUp";

    String fDown = "6 F Down";
    String fDownUp = "6 F DownUp";

    String dmDown = "7 Dm Down";
    String dmDownUp = "7 Dm DownUp";

    String bmDown = "8 Bm Down";
    String bmDownUp = "8 Bm DownUp";

    String dDown = "9 D Down";
    String dDownUp = "9 D DownUp";

    String gDown = "10 G Down";
    String gDownUp = "10 G DownUp";

    String root = "/Sounds/";
    String extension = ".wav";

    InputStream iAudio;
    Clip clip;

    public AudioInputStream generateBarAudioFromChords(String one, String two, String three, String four) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        Line.Info linfo = new Line.Info(Clip.class);
        Line line = AudioSystem.getLine(linfo);
        clip = (Clip) line;
        
        InputStream path = getClass().getResourceAsStream(root + one + extension);
        InputStream bufferedIn1 = new BufferedInputStream(path);
        InputStream path2 = getClass().getResourceAsStream(root + two + extension);
        InputStream bufferedIn2 = new BufferedInputStream(path2);
        InputStream path3 = getClass().getResourceAsStream(root + three + extension);
        InputStream bufferedIn3 = new BufferedInputStream(path3);
        InputStream path4 = getClass().getResourceAsStream(root + four + extension);
        InputStream bufferedIn4 = new BufferedInputStream(path4);
        
//        File path = new File(root + one + extension);
//        File path2 = new File(root + two + extension);
//        File path3 = new File(root + three + extension);
//        File path4 = new File(root + four + extension);

        AudioInputStream chord1 = AudioSystem.getAudioInputStream(bufferedIn1);
        AudioInputStream chord2 = AudioSystem.getAudioInputStream(bufferedIn2);
        AudioInputStream chord3 = AudioSystem.getAudioInputStream(bufferedIn3);
        AudioInputStream chord4 = AudioSystem.getAudioInputStream(bufferedIn4);

        AudioInputStream stuckFiles1 = new AudioInputStream(
                new SequenceInputStream(chord1, chord2),
                chord1.getFormat(),
                chord1.getFrameLength() + chord2.getFrameLength());

        AudioInputStream stuckFiles2 = new AudioInputStream(
                new SequenceInputStream(chord3, chord4),
                chord3.getFormat(),
                chord3.getFrameLength() + chord4.getFrameLength());

        AudioInputStream stuckFiles = new AudioInputStream(
                new SequenceInputStream(stuckFiles1, stuckFiles2),
                stuckFiles1.getFormat(),
                stuckFiles1.getFrameLength() + stuckFiles2.getFrameLength());

        return stuckFiles;

    }

    public AudioInputStream playTune(ArrayList<String> listOfChords) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    
        AudioInputStream one = generateBarAudioFromChords(listOfChords.get(0), listOfChords.get(1), listOfChords.get(2), listOfChords.get(3));
        AudioInputStream two = generateBarAudioFromChords(listOfChords.get(4), listOfChords.get(5), listOfChords.get(6), listOfChords.get(7));

        AudioInputStream finalAudio = new AudioInputStream(
                new SequenceInputStream(one, two),
                one.getFormat(),
                one.getFrameLength() + one.getFrameLength());
        return finalAudio;
        
//        clip.open(finalAudio);
//        clip.start();
    }
    
    
}
