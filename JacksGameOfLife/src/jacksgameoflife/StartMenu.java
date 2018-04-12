/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacksgameoflife;

import com.sun.media.sound.WaveFileWriter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author jackn
 */
public class StartMenu extends javax.swing.JFrame {

    private ArrayList<Integer> reUsedBlocks = new ArrayList();
    private ArrayList<String> reUsedChords = new ArrayList();

    AudioInputStream finalAudio;
    Clip clip;
    JacksGameOfLife game;
    private JFrame frame = new JFrame();
    private Sound sound = new Sound();
    private ArrayList<String> chordStrings;
    private ArrayList<Integer> strumPattern;
    static private GridAnalyzer gridAnalyzer;
    static private Timer timer;
    private int scale = 5; // change this to make each live animal appear larger / smaller. This has been done because 1 pixel is too small.
    private Color dead = Color.black;
    private Random seedPattern = new Random();
    static private int[][] field = null; //list visualised as so: int [x][y]  XXXXXXX
//                                                             Y
//                                                             Y
//                                                             Y
//                                                             Y   

    /**
     * Creates new form StartMenu
     */
    public StartMenu() {
        initComponents();

    }

    public void onExit() {

        timer.cancel();
        System.exit(JFrame.DISPOSE_ON_CLOSE);
    }

    public void startGame() throws Exception {

        gridAnalyzer = new GridAnalyzer();

        // TODO code application logic here
        game = new JacksGameOfLife(1000, 1000, gridAnalyzer, strumPattern);
        frame.getContentPane().add(game);

        //        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                timer.cancel();
                frame.dispose();
            }
        };
        frame.addWindowListener(exitListener);
        frame.pack();
        frame.setVisible(true);

        timer = new Timer();

        class GameTask extends TimerTask {

            public void run() {
                if (gridAnalyzer.checkCoverage(game.getField())) {
                    if (field == null) {
                        field = game.getField();
                    }
                    chordStrings = game.generateChords(reUsedChords);
                    playButton.setVisible(true);
                    ArrayList<Integer> listOfKeptBlocks = listOfKeptBlocks();
                    if (!listOfKeptBlocks.isEmpty()) { //if not the first run and blocks have been selected
                        rearrangeFieldToIncludeKeptBlocks(listOfKeptBlocks);
                        game.setField(field);
                        game.repaint();
                    }
                    timer.cancel();
                }
                game.incrementField();
                game.repaint();
            }
        }

        timer.schedule(new GameTask(), 0, 1 * 100);
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
                    s.setJPanel3(panel3);

                } catch (Exception ex) {
                    Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public javax.swing.JButton getPlayButton() {
        return playButton;
    }

    public void setPlayButton(javax.swing.JButton button) {
        playButton = button;
    }

    public javax.swing.JPanel getJPanel1() {
        return jPanel1;
    }

    public void setJPanel1(javax.swing.JPanel panel) {
        jPanel1 = panel;
    }

    public javax.swing.JPanel getJPanel2() {
        return jPanel2;
    }

    public void setJPanel2(javax.swing.JPanel button) {
        jPanel2 = button;
    }

    public javax.swing.JPanel getJPanel3() {
        return jPanel3;
    }

    public void setJPanel3(javax.swing.JPanel button) {
        jPanel3 = button;
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
        jPanel3 = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        bar1FinalDisplayLabel = new javax.swing.JLabel();
        bar2FinalDisplayLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        resetButtonFinal = new javax.swing.JButton();
        saveAudioButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        playButton = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        buttonOne = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(274, Short.MAX_VALUE)
                .addComponent(resetButtonFinal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveAudioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveButton)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11)
                    .addComponent(jLabel9)
                    .addComponent(bar1FinalDisplayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bar2FinalDisplayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bar1FinalDisplayLabel)
                .addGap(14, 14, 14)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bar2FinalDisplayLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resetButtonFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveAudioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        mainPanel.add(jPanel3, "card4");

        jLabel2.setText("Bar 2");

        jLabel1.setText("Strum pattern. Ticking a checkbox means that strum in ");

        jLabel3.setText("Bar 1");

        playButton.setText("Play");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Up + down strum");

        jCheckBox2.setText("Up + down strum");

        jCheckBox3.setText("Up + down strum");

        jCheckBox4.setText("Up + down strum");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jCheckBox5.setText("Up + down strum");
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });

        buttonOne.setText("Start simulation");
        buttonOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOneActionPerformed(evt);
            }
        });

        jLabel4.setText("the pattern will be an up and down strum in one beat.");

        jCheckBox6.setText("Up + down strum");

        jCheckBox7.setText("Up + down strum");

        jCheckBox8.setText("Up + down strum");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox4))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jCheckBox5)
                        .addGap(2, 2, 2)
                        .addComponent(jCheckBox6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox8))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(99, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonOne, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(3, 3, 3)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBox4))
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox5)
                    .addComponent(jCheckBox6)
                    .addComponent(jCheckBox7)
                    .addComponent(jCheckBox8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonOne, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        mainPanel.add(jPanel1, "card2");

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(86, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel16)
                    .addComponent(jLabel14)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(163, 163, 163)
                        .addComponent(jLabel23))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel20))
                                .addGap(72, 72, 72)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bar1Chord3Checkbox)
                            .addComponent(bar1Chord2Checkbox)
                            .addComponent(bar1Chord4Checkbox)
                            .addComponent(bar1Chord1Checkbox)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(bar2Chord4Checkbox)
                                    .addComponent(bar2Chord3Checkbox)
                                    .addComponent(bar2Chord2Checkbox)
                                    .addComponent(bar2Chord1Checkbox))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(reSeedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(doneButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(88, 88, 88))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel12))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
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
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel18)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel20))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(bar2Chord1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(bar2Chord2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(bar2Chord3))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(bar2Chord1Checkbox)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(bar2Chord2Checkbox)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(bar2Chord3Checkbox)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(bar2Chord4Checkbox)
                                            .addComponent(bar2Chord4)))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(bar1Chord1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bar1Chord2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bar1Chord3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bar1Chord4)))
                        .addGap(0, 55, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(reSeedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(doneButton, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        mainPanel.add(jPanel2, "card3");

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

    private ArrayList<Integer> listOfKeptBlocks() {
        ArrayList<Integer> returnList = new ArrayList();
        if (reUsedBlocks.isEmpty()) {
            return returnList;
        }

        for (int i = 0; i < reUsedBlocks.size(); i++) {
            if (reUsedBlocks.get(i) == 1) {
                returnList.add(i);
            }
        }
        return returnList;
    }

    private void writeWavFromAudio() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        finalAudio = sound.playTune(chordStrings);
         File f = new File(generateName() + ".wav");
        
        WaveFileWriter writer = new WaveFileWriter();

        writer.write(finalAudio, AudioFileFormat.Type.WAVE, f);
    }

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

    public BufferedImage getScreenShot(Component component) {

        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
        component.paint(image.getGraphics());
        return image;
    }

    public void saveScreenshot(Component component, String filename) throws Exception {
        BufferedImage img = getScreenShot(component);
        ImageIO.write(img, "png", new File(filename));
    }

    public void saveImage() throws Exception {
        game.setField(field);
        saveScreenshot(frame, generateName() + ".png");
    }

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


    private void buttonOneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOneActionPerformed
        playButton.setVisible(false);
        strumPattern = new ArrayList();
        if (jCheckBox1.isSelected()) {
            strumPattern.add(1);
        } else {
            strumPattern.add(0);
        }

        if (jCheckBox2.isSelected()) {
            strumPattern.add(1);
        } else {
            strumPattern.add(0);
        }

        if (jCheckBox3.isSelected()) {
            strumPattern.add(1);
        } else {
            strumPattern.add(0);
        }

        if (jCheckBox4.isSelected()) {
            strumPattern.add(1);
        } else {
            strumPattern.add(0);
        }

        if (jCheckBox5.isSelected()) {
            strumPattern.add(1);
        } else {
            strumPattern.add(0);
        }

        if (jCheckBox6.isSelected()) {
            strumPattern.add(1);
        } else {
            strumPattern.add(0);
        }

        if (jCheckBox7.isSelected()) {
            strumPattern.add(1);
        } else {
            strumPattern.add(0);
        }

        if (jCheckBox8.isSelected()) {
            strumPattern.add(1);
        } else {
            strumPattern.add(0);
        }

        try {
            startGame();
        } catch (Exception ex) {
            Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonOneActionPerformed

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        if (clip != null) {
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
            finalAudio = sound.playTune(chordStrings);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            clip.open(finalAudio);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        clip.start();

        jPanel2.setVisible(true);
        jPanel1.setVisible(false);
        bar1Chord1.setText(chordStrings.get(0));
        bar1Chord2.setText(chordStrings.get(1));
        bar1Chord3.setText(chordStrings.get(2));
        bar1Chord4.setText(chordStrings.get(3));
        bar2Chord1.setText(chordStrings.get(4));
        bar2Chord2.setText(chordStrings.get(5));
        bar2Chord3.setText(chordStrings.get(6));
        bar2Chord4.setText(chordStrings.get(7));


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

    private void reSeedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reSeedButtonActionPerformed

        reUsedBlocks = new ArrayList();
        reUsedChords = new ArrayList();

        if (bar1Chord1Checkbox.isSelected()) {
            reUsedBlocks.add(1);
        } else {
            reUsedBlocks.add(0);
        }
        if (bar1Chord2Checkbox.isSelected()) {
            reUsedBlocks.add(1);
        } else {
            reUsedBlocks.add(0);
        }
        if (bar1Chord3Checkbox.isSelected()) {
            reUsedBlocks.add(1);
        } else {
            reUsedBlocks.add(0);
        }
        if (bar1Chord4Checkbox.isSelected()) {
            reUsedBlocks.add(1);
        } else {
            reUsedBlocks.add(0);
        }
        if (bar2Chord1Checkbox.isSelected()) {
            reUsedBlocks.add(1);
        } else {
            reUsedBlocks.add(0);
        }
        if (bar2Chord2Checkbox.isSelected()) {
            reUsedBlocks.add(1);
        } else {
            reUsedBlocks.add(0);
        }
        if (bar2Chord3Checkbox.isSelected()) {
            reUsedBlocks.add(1);
        } else {
            reUsedBlocks.add(0);
        }
        if (bar2Chord4Checkbox.isSelected()) {
            reUsedBlocks.add(1);
        } else {
            reUsedBlocks.add(0);
        }
        for (int i = 0; i < reUsedBlocks.size(); i++) {
            if (reUsedBlocks.get(i) == 1) {
                reUsedChords.add(chordStrings.get(i));
            } else {
                reUsedChords.add("");
            }
        }
        jPanel2.setVisible(false);
        jPanel1.setVisible(true);

        for (int i = 0; i < reUsedBlocks.size(); i++) {
            switch (i) {
                case 0:
                    if (reUsedBlocks.get(i) == 1) {
                        jCheckBox1.setEnabled(false);
                    } else {
                        jCheckBox1.setEnabled(true);
                    }
                    break;
                case 1:
                    if (reUsedBlocks.get(i) == 1) {
                        jCheckBox2.setEnabled(false);
                    } else {
                        jCheckBox2.setEnabled(true);
                    }
                    break;
                case 2:
                    if (reUsedBlocks.get(i) == 1) {
                        jCheckBox3.setEnabled(false);
                    } else {
                        jCheckBox3.setEnabled(true);
                    }
                    break;
                case 3:
                    if (reUsedBlocks.get(i) == 1) {
                        jCheckBox4.setEnabled(false);
                    } else {
                        jCheckBox4.setEnabled(true);
                    }
                    break;
                case 4:
                    if (reUsedBlocks.get(i) == 1) {
                        jCheckBox5.setEnabled(false);
                    } else {
                        jCheckBox5.setEnabled(true);
                    }
                    break;
                case 5:
                    if (reUsedBlocks.get(i) == 1) {
                        jCheckBox6.setEnabled(false);
                    } else {
                        jCheckBox6.setEnabled(true);
                    }
                    break;
                case 6:
                    if (reUsedBlocks.get(i) == 1) {
                        jCheckBox7.setEnabled(false);
                    } else {
                        jCheckBox7.setEnabled(true);
                    }
                    break;
                case 7:
                    if (reUsedBlocks.get(i) == 1) {
                        jCheckBox8.setEnabled(false);
                    } else {
                        jCheckBox8.setEnabled(true);
                    }
                    break;
            }
        }


    }//GEN-LAST:event_reSeedButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        reUsedBlocks.clear();
        reUsedChords.clear();
        chordStrings.clear();
        strumPattern.clear();
        bar1Chord1Checkbox.setEnabled(true);
        bar1Chord2Checkbox.setEnabled(true);
        bar1Chord3Checkbox.setEnabled(true);
        bar1Chord4Checkbox.setEnabled(true);
        bar2Chord1Checkbox.setEnabled(true);
        bar2Chord2Checkbox.setEnabled(true);
        bar2Chord3Checkbox.setEnabled(true);
        bar2Chord4Checkbox.setEnabled(true);

        bar1Chord1Checkbox.setSelected(false);
        bar1Chord2Checkbox.setSelected(false);
        bar1Chord3Checkbox.setSelected(false);
        bar1Chord4Checkbox.setSelected(false);
        bar2Chord1Checkbox.setSelected(false);
        bar2Chord2Checkbox.setSelected(false);
        bar2Chord3Checkbox.setSelected(false);
        bar2Chord4Checkbox.setSelected(false);

        jCheckBox1.setEnabled(true);
        jCheckBox2.setEnabled(true);
        jCheckBox3.setEnabled(true);
        jCheckBox4.setEnabled(true);
        jCheckBox5.setEnabled(true);
        jCheckBox6.setEnabled(true);
        jCheckBox7.setEnabled(true);
        jCheckBox8.setEnabled(true);

        jCheckBox1.setSelected(false);
        jCheckBox2.setSelected(false);
        jCheckBox3.setSelected(false);
        jCheckBox4.setSelected(false);
        jCheckBox5.setSelected(false);
        jCheckBox6.setSelected(false);
        jCheckBox7.setSelected(false);
        jCheckBox8.setSelected(false);

        jPanel2.setVisible(false);
        jPanel1.setVisible(true);
        playButton.setVisible(false);
    }//GEN-LAST:event_resetButtonActionPerformed

    private void doneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneButtonActionPerformed
        jPanel3.setVisible(true);
        jPanel2.setVisible(false);

        String bar1 = "";
        String bar2 = "";

        for (int i = 0; i < chordStrings.size(); i++) {
            if (i < 4) {
                bar1 = bar1 + " " + chordStrings.get(i).substring(1);
            } else {
                bar2 = bar2 + " " + chordStrings.get(i).substring(1);
            }
        }

        bar1FinalDisplayLabel.setText(bar1);
        bar2FinalDisplayLabel.setText(bar2);
    }//GEN-LAST:event_doneButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try {
            saveImage();
        } catch (Exception ex) {
            Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void resetButtonFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonFinalActionPerformed
        jPanel3.setVisible(false);
        resetButtonActionPerformed(evt);

    }//GEN-LAST:event_resetButtonFinalActionPerformed

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
    private javax.swing.JButton buttonOne;
    private javax.swing.JButton doneButton;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton playButton;
    private javax.swing.JButton reSeedButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton resetButtonFinal;
    private javax.swing.JButton saveAudioButton;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}
