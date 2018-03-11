package jacksgameoflife;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
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
    
    String root = new File("").getAbsolutePath() + "/Sounds/Sounds broken up/";
    

    
    String extension = ".wav";

    InputStream iAudio;
    Clip clip;

    /**
     *
     */
    public void play4Bar(ArrayList<String> listChords) {
        for (String listChord : listChords) {

        }
    }



    public void ree() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        Line.Info linfo = new Line.Info(Clip.class);
        Line line = AudioSystem.getLine(linfo);
        clip = (Clip) line;
        File file = new File(root + dDown + extension);
        File file2 = new File(root + gDownUp + extension);
        File file3 = new File(root + emDownUp + extension);
        File file4 = new File(root + emDown + extension);

        AudioInputStream chord1 = AudioSystem.getAudioInputStream(file);
        AudioInputStream chord2 = AudioSystem.getAudioInputStream(file2);
        AudioInputStream chord3 = AudioSystem.getAudioInputStream(file3);
        AudioInputStream chord4 = AudioSystem.getAudioInputStream(file4);

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

        clip.open(stuckFiles);
        clip.start();

    }

    public void multiRee() throws FileNotFoundException, IOException, InterruptedException {

        ArrayList<String> files = new ArrayList();
        files.add(root + dDown + extension);
        files.add(root + gDownUp + extension);
        files.add(root + emDownUp + extension);

        byte[] buffer = new byte[4096];
        for (String filePath : files) {
            File file = new File(filePath);
            try {
                AudioInputStream is = AudioSystem.getAudioInputStream(file);
                AudioFormat format = is.getFormat();
                SourceDataLine line = AudioSystem.getSourceDataLine(format);
                line.open(format);
                line.start();
                while (is.available() > 0) {
                    int len = is.read(buffer);
                    line.write(buffer, 0, len);
                }
                line.drain();
                line.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            TimeUnit.MICROSECONDS.sleep(1);
        }

    }

}
