/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacksgameoflife;

import java.util.ArrayList;

/**
 *
 * @author jackn
 */
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
    ArrayList<String> reUsedChords;

    /**
     *
     * @param field
     * @return
     */
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
//            System.out.println("FINAL RUN: " + iteration + " gave " + blockCount);
            return true;
        }
//        System.out.println(iteration + " gave " + blockCount);
        blockCount = 0;
        return false;
    }

    /**
     *
     * @param field
     * @param strumPattern
     * @param reUsedChords
     * @return
     */
    public ArrayList<String> decideChords(int[][] field, ArrayList<Boolean> strumPattern, ArrayList<String> reUsedChords) {
        this.reUsedChords = reUsedChords;
        ArrayList<Integer> array = new ArrayList();
        int average = 0;
        int count = 0;

        for (int t = 0; t < field.length; t++) { // get the average values of each line
            ArrayList<Integer> listOfBlocks = new ArrayList(); //all average values for this current line. 

            for (int j = 0; j < field[0].length; j++) {
                if (field[t][j] == 1) {
                    listOfBlocks.add(j);
                }

            }
            ArrayList<Integer> listOfLiveBlocks = new ArrayList();

            for (Integer listOfBlock : listOfBlocks) {
                average = average + listOfBlock;
                count++;
            }
            if (average != 0) {
                average = average / count;
            } else {
                average = 0;
            }

            array.add(average); // one value for the 200 blocks of this row. 
            average = 0;
            count = 0;
        }

        ArrayList<Integer> averagedTo25Blocks = new ArrayList();

        int index = 0;
        int blockCount = 0;
        int rollingAverage = 0;

        //Break up the average list into average values of 25 blocks. 
        for (Integer integer : array) {
            if (blockCount == 24) {

                if (rollingAverage != 0) {
                    averagedTo25Blocks.add(rollingAverage / 25);

                } else {
                    averagedTo25Blocks.add(0);
                }
                rollingAverage = 0;
                blockCount = 0;
            } else {
                rollingAverage = rollingAverage + integer;
                blockCount++;
            }
        }

        ArrayList<String> returnListOfChords = matchValuesToChords(averagedTo25Blocks, strumPattern);

        return returnListOfChords;
    }

    /**
     * Translates the average block values read from the grid into a list of 8 Strings.
     * These strings inside the returned list are the filenames of the wav files 
     * which should be played. Along with an ArrayList of ints which make up the values,
     * the method also reads a list of booleans which correspond to the type of strum 
     * the chord should be played with. 
     * @param blockValue the calculated averages of each block of 25 rows. 
     * @param strumPattern
     * @return An arrayList of strings which make up all of the chords to be played. 
     */
    public ArrayList<String> matchValuesToChords(ArrayList<Integer> blockValue, ArrayList<Boolean> strumPattern) {

        ArrayList<String> finalisedChordSelection = new ArrayList();
        boolean lookAtReUsedChords = false;
        if (!reUsedChords.isEmpty()) { // no need to look at the re used chords if this is the first run or if there are none. 
            lookAtReUsedChords = true; // if there are chords to be reUsed then the for loop will have to consider looking inside the list. 
        }
        for (int i = 0; i < blockValue.size(); i++) { // Translate average block values to the chords as well as strumming patterns. 
            //The blockValue will be length 8 and so is the strumPattern list. 
            if (lookAtReUsedChords) { //should look at the list given 
                String s = reUsedChords.get(i); // if s == "" then the user has not specified to re use the chord in this block. Else, use the string that exists. 
                if (s.equals("")) {// this blockDoesn't have a reUsedChord.
                    finalisedChordSelection.add(getSpecificStringFromAverageValueAndStrum(blockValue.get(i), strumPattern.get(i)));
                } else {
                    finalisedChordSelection.add(s); // no need to analyze the value again. 
                }
            } else {
                finalisedChordSelection.add(getSpecificStringFromAverageValueAndStrum(blockValue.get(i), strumPattern.get(i)));
            }

        }
        return finalisedChordSelection;
    }

    /**
     *Takes in a value and a boolean. The value is the average count of the rows
     * which are being analyzed. A higher value means the system should play a higher
     * pitched chord. The boolean is used to specify whether the chord should 
     * be played with an Down/Up motion, or simply a Down strum. 
     * @param value
     * @param strum
     * @return The string of the chord filename which should be played. 
     */
    public String getSpecificStringFromAverageValueAndStrum(int value, Boolean strum) {

        // if strum is true then for this chord, the user has chosen to have a down up strum pattern.
        // if it's false then they want to preserve the regular down strum.
        
        if (value < 20) {
            if (strum) {
                return emDownUp;
            } else {
                return emDown;
            }
        }

        if (value < 40) {
            if (strum) {
                return eDownUp;
            } else {
                return eDown;
            }
        }

        if (value < 60) {
            if (strum) {
                return amDownUp;
            } else {
                return amDown;
            }
        }

        if (value < 80) {
            if (strum) {
                return cDownUp;
            } else {
                return cDown;
            }
        }

        if (value < 100) {
            if (strum) {
                return aDownUp;
            } else {
                return aDown;
            }
        }

        if (value < 120) {
            if (strum) {
                return fDownUp;
            } else {
                return fDown;
            }
        }

        if (value < 140) {
            if (strum) {
                return dmDownUp;
            } else {
                return dmDown;
            }
        }

        if (value < 160) {
            if (strum) {
                return bmDownUp;
            } else {
                return bmDown;
            }
        }

        if (value < 180) {
            if (strum) {
                return dDownUp;
            } else {
                return dDown;
            }
        }

        if (strum) {
            return gDownUp;
        } else {
            return gDown;
        }

    }

}
