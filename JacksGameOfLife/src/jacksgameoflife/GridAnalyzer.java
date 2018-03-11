/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacksgameoflife;

import java.util.ArrayList;
import java.util.List;

public class GridAnalyzer {

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

    static int numOfPixels = 40000;
    int blockCount = 0;
    int iteration = 0;

    public boolean checkCoverage(int[][] field) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if (field[i][j] == 1) {
                    blockCount++;
                }
            }
        }
        iteration++;
        if (blockCount > 6000) { // 70% of 40000 
            blockCount = 0;
            System.out.println("FINAL RUN: " + iteration + " gave " + blockCount);
            return true;
        }
        System.out.println(iteration + " gave " + blockCount);
        blockCount = 0;
        return false;
    }

    public ArrayList<String> decideChords(int[][] field) {
        ArrayList<ArrayList<Integer>> array = new ArrayList();
        ArrayList<String> returnArray = new ArrayList();
        

        for (int i = 0; i < field.length; i++) {
            ArrayList<Integer> listOfBlocks = new ArrayList();
            for (int j = 0; j < field[0].length; j++) {
                if (field[j][i] == 1) {
                    listOfBlocks.add(j);
                }
                
            }
            array.add(listOfBlocks);
        }
        
        returnArray = analyzeRows(array);
        for (String string : returnArray) {
            System.out.println (string);
        }
        
        return returnArray;
    }
    
    public ArrayList<String> analyzeRows(ArrayList<ArrayList<Integer>> listOfRowsAndTheirBlocks){
        ArrayList<String> returnArray = new ArrayList();
        
        int counter = 0;
        int total = 0;
        for (ArrayList<Integer> listOfRowsAndTheirBlock : listOfRowsAndTheirBlocks) {
            counter = 0;
            total = 0;
            for (Integer integer : listOfRowsAndTheirBlock) {
                total = total + integer;
                counter ++;
            }
            if (counter != 0){
            total = total / counter;
            }else {
                total = 0;
            }
            returnArray.add(getChordFromInt(total));
        }
        
        
        return returnArray; 
    }
    
    public String getChordFromInt(int value){
        
        if (value  <= 20){
            return emDown;
        }
        if (value <= 40){
            return eDown;
        }
        
        if (value <= 60) {
            return amDown;
        }
        
        if (value <= 80) {
            return cDown;
        }
        
        if (value <= 100) {
            return aDown;
        }
        
        if (value <= 120) {
            return fDown;
        }
        
        if (value <= 140) {
            return dmDown;
        }
        
        if (value <= 160) {
            return bmDown;
        }
        
        if (value <= 180) {
            return dDown;
        }
        
        if (value <= 200) {
            return gDown;
        }
        
        return ""; // if returning this then something is fucked.  
        
    }

}
