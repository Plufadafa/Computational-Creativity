/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacksgameoflife;

import java.util.Timer;
import java.util.TimerTask;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author jackn
 */
public class JacksGameOfLife extends JPanel {

    static private GridAnalyzer gridAnalyzer;
    static private Timer timer;
    private int scale = 5; // change this to make each live animal appear larger / smaller. This has been done because 1 pixel is too small.
    private Color dead = Color.black;
    private Random seedPattern = new Random();
    static private int[][] field; //list visualised as so: int [x][y]  XXXXXXX
//                                                             Y
//                                                             Y
//                                                             Y
//                                                             Y     

    public JacksGameOfLife(int fieldX, int fieldY) {
        int scaledX = fieldX / scale;
        int scaledY = fieldY / scale;
        field = new int[scaledX][scaledY];
        seedFieldRandom();
    }

    public void incrementField() {
        for (int x = 0; x < field.length; x++) { //going through each row
            for (int y = 0; y < field[x].length; y++) { //columns of that row
                checkAlive(x, y);
            }
        }
    }

    public void checkAlive(int x, int y) {
        //visualised by a compass.
        int n = 0;
        int e = 0;
        int s = 0;
        int w = 0;
        int ne = 0;
        int se = 0;
        int nw = 0;
        int sw = 0;

        if (x < field.length - 1) { // account for start at 0.
            e = field[x + 1][y];

            if (y < field.length - 1) {
                se = field[x + 1][y + 1];
            }
            if (y > 0) {
                ne = field[x + 1][y - 1];
            }
        }
        if (x > 0) {
            w = field[x - 1][y];
            if (y < field.length - 1) {
                sw = field[x - 1][y + 1];
            }
            if (y > 0) {
                nw = field[x - 1][y - 1];
            }
        }
        if (y < field.length - 1) {
            s = field[x][y + 1];
        }
        if (y > 0) {
            n = field[x][y - 1];
        }

        int totalValue = n + e + s + w + ne + nw + se + sw;

        if (field[x][y] == 1) { //if animal is presnt 
            if (totalValue < 2) {
                field[x][y] = 0; //dies of loneliness :'(
            }
            if (totalValue > 3) {
                field[x][y] = 0; //dies of overheating >.<
            }
        } else { //else there isn't an animal on cell
            if (totalValue == 3) { //perfect conditions for a little animal to be born <3
                field[x][y] = 1;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Sound sound = new Sound();
        gridAnalyzer = new GridAnalyzer();
        sound.ree();

        // TODO code application logic here
        JFrame frame = new JFrame();
        JacksGameOfLife game = new JacksGameOfLife(1000, 1000);
        frame.getContentPane().add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        timer = new Timer();
        
        class GameTask extends TimerTask {
            public void run() {
                if (gridAnalyzer.checkCoverage(field)){
                    game.generateChords();
                    timer.cancel();
                }
                game.incrementField();
                game.repaint();             
            }
        }
        
        timer.schedule(new GameTask(), 0,  3*100);
    }
    
    private void generateChords(){
        gridAnalyzer.decideChords(field);
        System.out.println("FUUUUUUCK");
    }

    private void seedFieldRandom() {
        for (int[] x : field) {
            for (int y = 0; y < x.length; y++) {
                if (seedPattern.nextInt(70) < 10) { //if random int less than 5 (max is 50) then...
                    x[y] = seedPattern.nextInt(2);
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics graphic) {

        super.paintComponent(graphic);
        Color stand = graphic.getColor();
        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field[x].length; y++) {
                if (field[x][y] == 1) { //if pixel if alive
                    graphic.setColor(dead);
                    graphic.fillRect(x * scale, y * scale, scale, scale);
                }
            }
        }
        graphic.setColor(stand);
    }

//only way to change the preferred size of the frame.
    @Override
    public Dimension getPreferredSize() {
        int fieldX = field.length * scale;
        int fieldY = field[0].length * scale; // field[0] is the number of columns i.e height. 
        return new Dimension(fieldX, fieldY);
    }

}