package jacksgameoflife;

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

   

    String root = "/Sounds/";
    String extension = ".wav";

    InputStream iAudio;
    Clip clip;

    /**
     *
     * @param one
     * @param two
     * @param three
     * @param four
     * @return
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
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

    /**
     *
     * @param listOfChords
     * @return
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
    public AudioInputStream putTuneTogetherFromListOfChords(ArrayList<String> listOfChords) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    
        AudioInputStream one = generateBarAudioFromChords(listOfChords.get(0), listOfChords.get(1), listOfChords.get(2), listOfChords.get(3));
        AudioInputStream two = generateBarAudioFromChords(listOfChords.get(4), listOfChords.get(5), listOfChords.get(6), listOfChords.get(7));

        AudioInputStream finalAudio = new AudioInputStream(
                new SequenceInputStream(one, two),
                one.getFormat(),
                one.getFrameLength() + one.getFrameLength());
        return finalAudio;   
    }
}
