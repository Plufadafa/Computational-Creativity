/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacksgameoflife;

import com.sun.media.sound.WaveFileWriter;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

/**
 *
 * @author jackn
 */
public class StartMenu extends javax.swing.JFrame {

    private ArrayList<javax.swing.JCheckBox> chordsToKeepCheckBoxes = new ArrayList();
    private ArrayList<javax.swing.JLabel> chordLabels = new ArrayList();
    private ArrayList<javax.swing.JCheckBox> strumPatternCheckBoxes = new ArrayList();
    private ArrayList<Boolean> reUsedBlocks = new ArrayList();
    private ArrayList<String> reUsedChords = new ArrayList();
    AudioInputStream finalAudio;
    Clip clip;
    JacksGameOfLife game;
    private final JFrame frame = new JFrame();
    private final Sound sound = new Sound();
    private ArrayList<String> chordStrings;
    private ArrayList<Boolean> strumPattern;
    static private GridAnalyzer gridAnalyzer;
    static private Timer timer;
    private final int scale = 5; // change this to make each live animal appear larger / smaller. This has been done because 1 pixel is too small.
    private final Color dead = Color.black;
    private final Random seedPattern = new Random();
    static private int[][] field = null;

    /**
     * Creates new form StartMenu
     */
    public StartMenu() {
        initComponents();
        //Initialise the list of strum pattern checkboxes.
        strumPatternCheckBoxes.addAll(Arrays.asList(strumPatternCheckBox1, strumPatternCheckBox2, strumPatternCheckBox3, strumPatternCheckBox4, strumPatternCheckBox5, strumPatternCheckBox6, strumPatternCheckBox7, strumPatternCheckBox8));

        //Initialise the list of chord labels 
        chordLabels.addAll(Arrays.asList(bar1Chord1, bar1Chord2, bar1Chord3, bar1Chord4, bar2Chord1, bar2Chord2, bar2Chord3, bar2Chord4));

        //Initialise the list of chords to keep checkBoxes
        chordsToKeepCheckBoxes.addAll(Arrays.asList(bar1Chord1Checkbox, bar1Chord2Checkbox, bar1Chord3Checkbox, bar1Chord4Checkbox, bar2Chord1Checkbox, bar2Chord2Checkbox, bar2Chord3Checkbox, bar2Chord4Checkbox));
    }

    /**
     * When user presses close window.
     */
    public void onExit() {

        timer.cancel();
        System.exit(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * sets up the gridAnalyzer, game and the timer. 
     * @throws Exception
     */
    public void startGame() throws Exception {

        gridAnalyzer = new GridAnalyzer();
        game = new JacksGameOfLife(1000, 1000, gridAnalyzer, strumPattern);
        frame.getContentPane().add(game);

        WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                timer.cancel(); //stops the execution of any further iterations of field. 
                frame.dispose();
            }
        };

        frame.addWindowListener(exitListener);
        frame.pack();
        frame.setVisible(true);

        timer = new Timer();

        class GameTask extends TimerTask {

            public void run() {
                if (gridAnalyzer.checkCoverage(game.getField())) { // if the field has a satisfactory amount of coverage to cease simulation
                    if (field == null) { //if first run, else then field has been set.
                        field = game.getField();
                    }
                    chordStrings = game.generateChords(reUsedChords); //reused chords will be empty if first run. Chord strings used for sound.
                    playButton.setVisible(true); //now sound is ready to be played, allow user to see the play button. 
                    ArrayList<Integer> listOfKeptBlocks = generateListOfKeptBlocks(); //the blocks which the user has chosen to keep.
                    if (!listOfKeptBlocks.isEmpty()) { //if not the first run and blocks have been selected
                        rearrangeFieldToIncludeKeptBlocks(listOfKeptBlocks); //visually changes the field to represent the chords the user chose to keep. 
                        game.setField(field); //update the field. 
                        game.repaint(); //paint the frame with the new field. 
                    }
                    timer.cancel(); //no more simulation should occur, end the timer now. 
                }
                //if the coverage is not satisfactory, then increment the simulation and repaint the frame. 
                game.incrementField();
                game.repaint();
            }
        }

        timer.schedule(new GameTask(), 0, 1 * 100);//setting the timer up to begin simulation process. 
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StartMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StartMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StartMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StartMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    //setters for the new run. Establishing the GUI.
                    StartMenu s = new StartMenu();
                    javax.swing.JButton button = s.getPlayButton();
                    button.setVisible(false);

                    javax.swing.JPanel panel1 = s.getJPanel1();
                    panel1.setVisible(true);
                    s.setJPanel1(panel1);

                    s.setPlayButton(button);
                    s.setVisible(true);

                    javax.swing.JPanel panel3 = s.getJPanel3();
                    panel3.setVisible(false);
                    s.setJPanel3(panel3);// this panel should not be seen until the user is ready to save their tune. 

                } catch (Exception ex) {
                    Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    /**
     *
     * @return
     */
    public javax.swing.JButton getPlayButton() {
        return playButton;
    }

    /**
     *
     * @param button
     */
    public void setPlayButton(javax.swing.JButton button) {
        playButton = button;
    }

    /**
     *
     * @return
     */
    public javax.swing.JPanel getJPanel1() {
        return startPanel;
    }

    /**
     *
     * @param panel
     */
    public void setJPanel1(javax.swing.JPanel panel) {
        startPanel = panel;
    }

    /**
     *
     * @return
     */
    public javax.swing.JPanel getJPanel2() {
        return chooseChordsToKeepPanel;
    }

    /**
     *
     * @param button
     */
    public void setJPanel2(javax.swing.JPanel button) {
        chooseChordsToKeepPanel = button;
    }

    /**
     *
     * @return
     */
    public javax.swing.JPanel getJPanel3() {
        return finalSavePanel;
    }

    /**
     *
     * @param button
     */
    public void setJPanel3(javax.swing.JPanel button) {
        finalSavePanel = button;
    }

    /**
     * This method is called from within the constructor to initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        finalSavePanel = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        bar1FinalDisplayLabel = new javax.swing.JLabel();
        bar2FinalDisplayLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        resetButtonFinal = new javax.swing.JButton();
        saveAudioButton = new javax.swing.JButton();
        startPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        playButton = new javax.swing.JButton();
        strumPatternCheckBox1 = new javax.swing.JCheckBox();
        strumPatternCheckBox2 = new javax.swing.JCheckBox();
        strumPatternCheckBox3 = new javax.swing.JCheckBox();
        strumPatternCheckBox4 = new javax.swing.JCheckBox();
        strumPatternCheckBox5 = new javax.swing.JCheckBox();
        startSimulationButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        strumPatternCheckBox6 = new javax.swing.JCheckBox();
        strumPatternCheckBox7 = new javax.swing.JCheckBox();
        strumPatternCheckBox8 = new javax.swing.JCheckBox();
        chooseChordsToKeepPanel = new javax.swing.JPanel();
        bar1Chord1 = new javax.swing.JLabel();
        bar1Chord1Checkbox = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        bar1Chord2Checkbox = new javax.swing.JCheckBox();
        bar1Chord2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        bar1Chord3Checkbox = new javax.swing.JCheckBox();
        bar1Chord3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        bar1Chord4Checkbox = new javax.swing.JCheckBox();
        bar1Chord4 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        bar2Chord1Checkbox = new javax.swing.JCheckBox();
        bar2Chord1 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        bar2Chord2Checkbox = new javax.swing.JCheckBox();
        bar2Chord2 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        bar2Chord3Checkbox = new javax.swing.JCheckBox();
        bar2Chord3 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        bar2Chord4Checkbox = new javax.swing.JCheckBox();
        bar2Chord4 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        reSeedButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        doneButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setPreferredSize(new java.awt.Dimension(551, 305));
        mainPanel.setLayout(new java.awt.CardLayout());

        saveButton.setText("Save image");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        bar1FinalDisplayLabel.setText("_");

        bar2FinalDisplayLabel.setText("_");

        jLabel9.setText("Bar 1");

        jLabel11.setText("Bar 2");

        resetButtonFinal.setText("New tune");
        resetButtonFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonFinalActionPerformed(evt);
            }
        });

        saveAudioButton.setText("Save audio");
        saveAudioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAudioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout finalSavePanelLayout = new javax.swing.GroupLayout(finalSavePanel);
        finalSavePanel.setLayout(finalSavePanelLayout);
        finalSavePanelLayout.setHorizontalGroup(
            finalSavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, finalSavePanelLayout.createSequentialGroup()
                .addContainerGap(274, Short.MAX_VALUE)
                .addComponent(resetButtonFinal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveAudioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveButton)
                .addContainerGap())
            .addGroup(finalSavePanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(finalSavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11)
                    .addComponent(jLabel9)
                    .addComponent(bar1FinalDisplayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bar2FinalDisplayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        finalSavePanelLayout.setVerticalGroup(
            finalSavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, finalSavePanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bar1FinalDisplayLabel)
                .addGap(14, 14, 14)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bar2FinalDisplayLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                .addGroup(finalSavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resetButtonFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveAudioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        mainPanel.add(finalSavePanel, "card4");

        jLabel2.setText("Bar 2");

        jLabel1.setText("Strum pattern. Ticking a checkbox means that strum in ");

        jLabel3.setText("Bar 1");

        playButton.setText("Play");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        strumPatternCheckBox1.setText("Up + down strum");

        strumPatternCheckBox2.setText("Up + down strum");

        strumPatternCheckBox3.setText("Up + down strum");

        strumPatternCheckBox4.setText("Up + down strum");
        strumPatternCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                strumPatternCheckBox4ActionPerformed(evt);
            }
        });

        strumPatternCheckBox5.setText("Up + down strum");
        strumPatternCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                strumPatternCheckBox5ActionPerformed(evt);
            }
        });

        startSimulationButton.setText("Start simulation");
        startSimulationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startSimulationButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("the pattern will be an up and down strum in one beat.");

        strumPatternCheckBox6.setText("Up + down strum");

        strumPatternCheckBox7.setText("Up + down strum");

        strumPatternCheckBox8.setText("Up + down strum");

        javax.swing.GroupLayout startPanelLayout = new javax.swing.GroupLayout(startPanel);
        startPanel.setLayout(startPanelLayout);
        startPanelLayout.setHorizontalGroup(
            startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, startPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(startPanelLayout.createSequentialGroup()
                        .addComponent(strumPatternCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(strumPatternCheckBox2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(strumPatternCheckBox3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(strumPatternCheckBox4))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(startPanelLayout.createSequentialGroup()
                        .addComponent(strumPatternCheckBox5)
                        .addGap(2, 2, 2)
                        .addComponent(strumPatternCheckBox6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(strumPatternCheckBox7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(strumPatternCheckBox8))
                    .addGroup(startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(99, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, startPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(startSimulationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        startPanelLayout.setVerticalGroup(
            startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(3, 3, 3)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(strumPatternCheckBox1)
                    .addComponent(strumPatternCheckBox2)
                    .addComponent(strumPatternCheckBox3)
                    .addComponent(strumPatternCheckBox4))
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(strumPatternCheckBox5)
                    .addComponent(strumPatternCheckBox6)
                    .addComponent(strumPatternCheckBox7)
                    .addComponent(strumPatternCheckBox8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startSimulationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        mainPanel.add(startPanel, "card2");

        bar1Chord1.setText("jLabel5");

        bar1Chord1Checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bar1Chord1CheckboxActionPerformed(evt);
            }
        });

        jLabel6.setText("Chord 1");

        bar1Chord2Checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bar1Chord2CheckboxActionPerformed(evt);
            }
        });

        bar1Chord2.setText("jLabel5");

        jLabel8.setText("Chord 2");

        bar1Chord3Checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bar1Chord3CheckboxActionPerformed(evt);
            }
        });

        bar1Chord3.setText("jLabel5");

        jLabel10.setText("Chord 3");

        bar1Chord4Checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bar1Chord4CheckboxActionPerformed(evt);
            }
        });

        bar1Chord4.setText("jLabel5");

        jLabel12.setText("Chord 4");

        bar2Chord1Checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bar2Chord1CheckboxActionPerformed(evt);
            }
        });

        bar2Chord1.setText("jLabel5");

        jLabel14.setText("Chord 1");

        bar2Chord2Checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bar2Chord2CheckboxActionPerformed(evt);
            }
        });

        bar2Chord2.setText("jLabel5");

        jLabel16.setText("Chord 2");

        bar2Chord3Checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bar2Chord3CheckboxActionPerformed(evt);
            }
        });

        bar2Chord3.setText("jLabel5");

        jLabel18.setText("Chord 3");

        bar2Chord4Checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bar2Chord4CheckboxActionPerformed(evt);
            }
        });

        bar2Chord4.setText("jLabel5");

        jLabel20.setText("Chord 4");

        jLabel21.setText("Bar 2");

        jLabel22.setText("Bar 1");

        jLabel23.setText("Keep chord in next version");

        reSeedButton.setLabel("Re-Seed ");
        reSeedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reSeedButtonActionPerformed(evt);
            }
        });

        resetButton.setLabel("Reset ");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        doneButton.setText("Done");
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout chooseChordsToKeepPanelLayout = new javax.swing.GroupLayout(chooseChordsToKeepPanel);
        chooseChordsToKeepPanel.setLayout(chooseChordsToKeepPanelLayout);
        chooseChordsToKeepPanelLayout.setHorizontalGroup(
            chooseChordsToKeepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chooseChordsToKeepPanelLayout.createSequentialGroup()
                .addContainerGap(86, Short.MAX_VALUE)
                .addGroup(chooseChordsToKeepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel16)
                    .addComponent(jLabel14)
                    .addGroup(chooseChordsToKeepPanelLayout.createSequentialGroup()
                        .addGap(163, 163, 163)
                        .addComponent(jLabel23))
                    .addGroup(chooseChordsToKeepPanelLayout.createSequentialGroup()
                        .addGroup(chooseChordsToKeepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(chooseChordsToKeepPanelLayout.createSequentialGroup()
                                .addGroup(chooseChordsToKeepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel20))
                                .addGap(72, 72, 72)
                                .addGroup(chooseChordsToKeepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(bar2Chord4)
                                    .addComponent(bar2Chord3)
                                    .addComponent(bar2Chord2)
                                    .addComponent(bar2Chord1)
                                    .addComponent(bar1Chord3)
                                    .addComponent(bar1Chord2)
                                    .addComponent(bar1Chord4)
                                    .addComponent(bar1Chord1)))
                            .addComponent(jLabel22)
                            .addComponent(jLabel21))
                        .addGap(70, 70, 70)
                        .addGroup(chooseChordsToKeepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bar1Chord2Checkbox)
                            .addComponent(bar1Chord1Checkbox)
                            .addGroup(chooseChordsToKeepPanelLayout.createSequentialGroup()
                                .addGroup(chooseChordsToKeepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(bar1Chord3Checkbox)
                                    .addComponent(bar1Chord4Checkbox)
                                    .addGroup(chooseChordsToKeepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(bar2Chord4Checkbox)
                                        .addComponent(bar2Chord3Checkbox)
                                        .addComponent(bar2Chord2Checkbox)
                                        .addComponent(bar2Chord1Checkbox)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                                .addGroup(chooseChordsToKeepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(reSeedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(doneButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );
        chooseChordsToKeepPanelLayout.setVerticalGroup(
            chooseChordsToKeepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chooseChordsToKeepPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chooseChordsToKeepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(chooseChordsToKeepPanelLayout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(chooseChordsToKeepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(chooseChordsToKeepPanelLayout.createSequentialGroup()
                                .addGroup(chooseChordsToKeepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(chooseChordsToKeepPanelLayout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel12))
                                    .addGroup(chooseChordsToKeepPanelLayout.createSequentialGroup()
                                        .addComponent(bar1Chord1Checkbox)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(bar1Chord2Checkbox)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(bar1Chord3Checkbox)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(bar1Chord4Checkbox)))
                                .addGap(7, 7, 7)
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(chooseChordsToKeepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(chooseChordsToKeepPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel18)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel20))
                                    .addGroup(chooseChordsToKeepPanelLayout.createSequentialGroup()
                                        .addGroup(chooseChordsToKeepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(chooseChordsToKeepPanelLayout.createSequentialGroup()
                                                .addComponent(bar2Chord1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(bar2Chord2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(bar2Chord3))
                                            .addGroup(chooseChordsToKeepPanelLayout.createSequentialGroup()
                                                .addComponent(bar2Chord1Checkbox)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(bar2Chord2Checkbox)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(bar2Chord3Checkbox)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(chooseChordsToKeepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(bar2Chord4Checkbox)
                                            .addComponent(bar2Chord4)))))
                            .addGroup(chooseChordsToKeepPanelLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(bar1Chord1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bar1Chord2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bar1Chord3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bar1Chord4)))
                        .addGap(0, 55, Short.MAX_VALUE))
                    .addGroup(chooseChordsToKeepPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(reSeedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(doneButton, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        mainPanel.add(chooseChordsToKeepPanel, "card3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /*
        Formats the integer list of blocks which make up the chords the user has
        chosen to keep for further iteration. This is necessary to preserve the 
        visual aspect of the iterative process. 
        @return returnList an array list of all the blocks which should be kept. 
     */
    private ArrayList<Integer> generateListOfKeptBlocks() {
        ArrayList<Integer> returnList = new ArrayList();
        //if there are no reUsedBlocks  then there isn't any need to continue. 
        if (reUsedBlocks.isEmpty()) {
            return returnList;
        }

        for (int i = 0; i < reUsedBlocks.size(); i++) {
            if (reUsedBlocks.get(i)) {
                returnList.add(i);
            }
        }
        return returnList;
    }
    /*
        Method which saves a copy of the final generated tune as a wav file.
        At the moment, the wav file is saved to the same directory the .jar 
        being run is found in. 
    */
    private void writeWavFromAudio() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        finalAudio = sound.putTuneTogetherFromListOfChords(chordStrings);
        File f = new File(generateName() + ".wav");
        WaveFileWriter writer = new WaveFileWriter();
        writer.write(finalAudio, AudioFileFormat.Type.WAVE, f);
    }

    /*
        This method is run once the visual simulation has come to an end. The blocks
        of rows which the user specified they'd like to keep are passed as an Array
        and the field is then rearranged to include such rows. 
        @param listOfKeptBlocks the blocks of rows which should be preserved
    */
    private void rearrangeFieldToIncludeKeptBlocks(ArrayList<Integer> listOfKeptBlocks) {
        ArrayList<Integer> blockRange = new ArrayList();
        int lowerRange;
        int higherRange;
        int[][] currentGameField = game.getField();

        for (Integer listOfKeptBlock : listOfKeptBlocks) {
            System.out.println(listOfKeptBlock);
            blockRange = getRangeFromBlockNumber(listOfKeptBlock);
            lowerRange = blockRange.get(0);
            higherRange = blockRange.get(1);
            for (int i = lowerRange; i <= higherRange; i++) { //the rows of the block being kept
                for (int j = 0; j < 200; j++) { // 200 columns being replaced to preserve the block.
                    currentGameField[j][i] = field[j][i];
                }
            }
        }

        field = currentGameField;

    }

    /*
    Based on a block number provided, passes back an arrayList with two values.
    The first value is the first row of the block, and the second is the final 
    row of the block. 
    @param blockNumber the number of which group of 25 rows is being 
    looked at.
    */
    private ArrayList<Integer> getRangeFromBlockNumber(int blockNumber) {
        ArrayList<Integer> blockRange = new ArrayList();
        switch (blockNumber) {
            case 0:
                blockRange.add(0);
                blockRange.add(24);
                break;

            case 1:
                blockRange.add(25);
                blockRange.add(49);
                break;

            case 2:
                blockRange.add(50);
                blockRange.add(74);
                break;

            case 3:
                blockRange.add(75);
                blockRange.add(99);
                break;

            case 4:
                blockRange.add(100);
                blockRange.add(124);
                break;

            case 5:
                blockRange.add(125);
                blockRange.add(149);
                break;

            case 6:
                blockRange.add(150);
                blockRange.add(174);
                break;

            case 7:
                blockRange.add(175);
                blockRange.add(199);
                break;

        }
        return blockRange;
    }

    /**
     *
     * @param component 
     * @return the image passed from the component provided
     */
    public BufferedImage getScreenShot(Component component) {

        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
        component.paint(image.getGraphics()); //
        return image;
    }

    /**
     *
     * @param component
     * @param filename
     * @throws Exception
     */
    public void saveScreenshot(Component component, String filename) throws Exception {
        BufferedImage img = getScreenShot(component);
        ImageIO.write(img, "png", new File(filename));
    }

    /**
     *
     * @throws Exception
     */
    public void saveImage() throws Exception {
        game.setField(field);
        saveScreenshot(frame, generateName() + ".png");
    }

    /**
     *  Generates a name for the file based on the chords present. 
     * @return
     */
    public String generateName() {
        String filename = "";
        for (String chordString : chordStrings) {
            filename = filename + chordString.substring(1);
        }
        if (filename.equals("")) {
            filename = "NoChords";
        }
        return filename;
    }

    /*
    The logic executed when the startSimulation button is pressed. 
    begins the game of life simulation. 
    */
    private void startSimulationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startSimulationButtonActionPerformed
        playButton.setVisible(false);
        strumPattern = new ArrayList();

        for (JCheckBox strumPatternCheckBox : strumPatternCheckBoxes) { //re fills the strum pattern list for this new run. 
            strumPattern.add(strumPatternCheckBox.isSelected());
        }

        try {
            startGame();
        } catch (Exception ex) {
            Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_startSimulationButtonActionPerformed

    private void strumPatternCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_strumPatternCheckBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_strumPatternCheckBox5ActionPerformed

    private void strumPatternCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_strumPatternCheckBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_strumPatternCheckBox4ActionPerformed
    
    
    /*
    Plays the tune based on the chords which have been generated. Takes the user to the next
    panel which displays options to reset, finish the tune, and reseed the simulation with the chosen
    chords to persist into the next iteration of the song. 
    */
    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        if (clip != null) { // stops the current clip playing if there is one already playing. 
            clip.stop();
        }

        Line.Info linfo = new Line.Info(Clip.class);
        Line line;
        try {
            line = AudioSystem.getLine(linfo);
            clip = (Clip) line;
        } catch (LineUnavailableException ex) {
            Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            finalAudio = sound.putTuneTogetherFromListOfChords(chordStrings); //play the final tune 
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            clip.open(finalAudio);
        } catch (LineUnavailableException | IOException ex) {
            Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        clip.start();

        chooseChordsToKeepPanel.setVisible(true);
        startPanel.setVisible(false);
        
        //labels for displaying what chords have been generated. 
        for (int i = 0; i < chordLabels.size(); i++) {
            chordLabels.get(i).setText(chordStrings.get(i));
        }
    }//GEN-LAST:event_playButtonActionPerformed

    private void bar1Chord1CheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bar1Chord1CheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bar1Chord1CheckboxActionPerformed

    private void bar1Chord2CheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bar1Chord2CheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bar1Chord2CheckboxActionPerformed

    private void bar1Chord3CheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bar1Chord3CheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bar1Chord3CheckboxActionPerformed

    private void bar1Chord4CheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bar1Chord4CheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bar1Chord4CheckboxActionPerformed

    private void bar2Chord1CheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bar2Chord1CheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bar2Chord1CheckboxActionPerformed

    private void bar2Chord2CheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bar2Chord2CheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bar2Chord2CheckboxActionPerformed

    private void bar2Chord3CheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bar2Chord3CheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bar2Chord3CheckboxActionPerformed

    private void bar2Chord4CheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bar2Chord4CheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bar2Chord4CheckboxActionPerformed

    /*
    reseed the field but remember the chords which have been selected to be 
    kept for the next iteration of the tune. 
    */
    private void reSeedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reSeedButtonActionPerformed

        reUsedBlocks = new ArrayList();
        reUsedChords = new ArrayList();

        for (JCheckBox chordsToKeepCheckBox : chordsToKeepCheckBoxes) {
            reUsedBlocks.add(chordsToKeepCheckBox.isSelected());
        }
        
        for (int i = 0; i < reUsedBlocks.size(); i++) {
            if (reUsedBlocks.get(i)) {
                reUsedChords.add(chordStrings.get(i));
            } else {
                reUsedChords.add("");
            }
        }
        /*
        Changing panels. Panel1 is the starting panel where the simulation can be 
        started again. 
        */
        chooseChordsToKeepPanel.setVisible(false);
        startPanel.setVisible(true);
        
        //Temporary checkbox used to update checkBox in list.
        javax.swing.JCheckBox tempCheckBox = new javax.swing.JCheckBox(); 
        for (int i = 0; i < reUsedBlocks.size(); i++) {
            tempCheckBox = strumPatternCheckBoxes.get(i);
            tempCheckBox.setEnabled(!reUsedBlocks.get(i));
            strumPatternCheckBoxes.set(i, tempCheckBox);    
        }
    }//GEN-LAST:event_reSeedButtonActionPerformed

    /*
    resets the system back to as if it were being started for the first time. 
    */
    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        reUsedBlocks.clear();
        reUsedChords.clear();
        chordStrings.clear();
        strumPattern.clear();
        // important setp, resets all of the checkboxes to their defaults
        for (JCheckBox chordsToKeepCheckBox : chordsToKeepCheckBoxes) {
            chordsToKeepCheckBox.setEnabled(true);
            chordsToKeepCheckBox.setSelected(false);
        }
        
        for (JCheckBox strumPatternCheckBox : strumPatternCheckBoxes) {
            strumPatternCheckBox.setEnabled(true);
            strumPatternCheckBox.setSelected(false);
        }

        chooseChordsToKeepPanel.setVisible(false);
        startPanel.setVisible(true);
        playButton.setVisible(false);
    }//GEN-LAST:event_resetButtonActionPerformed

    /*
    prepares the final screen.
    */
    private void doneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneButtonActionPerformed
        finalSavePanel.setVisible(true);
        chooseChordsToKeepPanel.setVisible(false);

        String bar1 = "";
        String bar2 = "";

        // builds the strings used in the labels which display the chord contents of each bar. 
        for (int i = 0; i < chordStrings.size(); i++) {
            if (i < 4) {
                bar1 = bar1 + " " + chordStrings.get(i).substring(1); //substring is used to chop off the number found at the start of every string. 
            } else {
                bar2 = bar2 + " " + chordStrings.get(i).substring(1);
            }
        }

        bar1FinalDisplayLabel.setText(bar1);
        bar2FinalDisplayLabel.setText(bar2);
    }//GEN-LAST:event_doneButtonActionPerformed

    /*
        calls the saveImage method. Saves a copy of the filed to the same directory as the .jar 
    */
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try {
            saveImage();
        } catch (Exception ex) {
            Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    /*
        reset the system to a state as if it was being run for the first time. 
    */
    private void resetButtonFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonFinalActionPerformed
        finalSavePanel.setVisible(false);
        resetButtonActionPerformed(evt);

    }//GEN-LAST:event_resetButtonFinalActionPerformed

    /*
        Saves a copy of the audio as a .wav file to the same directory as the .jar 
    */
    private void saveAudioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAudioButtonActionPerformed
        try {
            writeWavFromAudio();
        } catch (IOException ex) {
            Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveAudioButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bar1Chord1;
    private javax.swing.JCheckBox bar1Chord1Checkbox;
    private javax.swing.JLabel bar1Chord2;
    private javax.swing.JCheckBox bar1Chord2Checkbox;
    private javax.swing.JLabel bar1Chord3;
    private javax.swing.JCheckBox bar1Chord3Checkbox;
    private javax.swing.JLabel bar1Chord4;
    private javax.swing.JCheckBox bar1Chord4Checkbox;
    private javax.swing.JLabel bar1FinalDisplayLabel;
    private javax.swing.JLabel bar2Chord1;
    private javax.swing.JCheckBox bar2Chord1Checkbox;
    private javax.swing.JLabel bar2Chord2;
    private javax.swing.JCheckBox bar2Chord2Checkbox;
    private javax.swing.JLabel bar2Chord3;
    private javax.swing.JCheckBox bar2Chord3Checkbox;
    private javax.swing.JLabel bar2Chord4;
    private javax.swing.JCheckBox bar2Chord4Checkbox;
    private javax.swing.JLabel bar2FinalDisplayLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JPanel chooseChordsToKeepPanel;
    private javax.swing.JButton doneButton;
    private javax.swing.JPanel finalSavePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton playButton;
    private javax.swing.JButton reSeedButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton resetButtonFinal;
    private javax.swing.JButton saveAudioButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JPanel startPanel;
    private javax.swing.JButton startSimulationButton;
    private javax.swing.JCheckBox strumPatternCheckBox1;
    private javax.swing.JCheckBox strumPatternCheckBox2;
    private javax.swing.JCheckBox strumPatternCheckBox3;
    private javax.swing.JCheckBox strumPatternCheckBox4;
    private javax.swing.JCheckBox strumPatternCheckBox5;
    private javax.swing.JCheckBox strumPatternCheckBox6;
    private javax.swing.JCheckBox strumPatternCheckBox7;
    private javax.swing.JCheckBox strumPatternCheckBox8;
    // End of variables declaration//GEN-END:variables

}
