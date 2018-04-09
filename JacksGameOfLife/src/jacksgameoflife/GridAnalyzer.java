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
    ArrayList<String> reUsedChords;

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

    public ArrayList<String> decideChords(int[][] field, ArrayList<Integer> strumPattern, ArrayList<String> reUsedChords) {
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

    public ArrayList<String> matchValuesToChords(ArrayList<Integer> numberList, ArrayList<Integer> strumPattern) {

        ArrayList<String> finalisedChordSelection = new ArrayList();
        boolean lookAtReUsedChords = false;
        if (!reUsedChords.isEmpty()) {
            lookAtReUsedChords = true;
        }
        for (int i = 0; i < numberList.size(); i++) { // Translate average block values to the chords as well as strumming patterns. 
            //The numberList will be length 8 and so is the strumPattern list. 
            if (lookAtReUsedChords) { //should look at the list given 
                String s = reUsedChords.get(i);
                if (s.equals("")) {// this blockDoesn't have a reUsedChord.
                    finalisedChordSelection.add(getSpecificStringFromAverageValueAndStrum(numberList.get(i), strumPattern.get(i)));
                } else {
                    finalisedChordSelection.add(s);
                }
            }else{
                finalisedChordSelection.add(getSpecificStringFromAverageValueAndStrum(numberList.get(i), strumPattern.get(i)));
            }

        }

        return finalisedChordSelection;
    }

    public String getSpecificStringFromAverageValueAndStrum(int value, int strum) {
        if (value < 20) {
            if (strum == 0) {
                return emDown;
            } else {
                return emDownUp;
            }
        }
        if (value < 40) {
            if (strum == 0) {
                return eDown;
            } else {
                return eDownUp;
            }
        }
        if (value < 60) {
            if (strum == 0) {
                return amDown;
            } else {
                return amDownUp;
            }
        }
        if (value < 80) {
            if (strum == 0) {
                return cDown;
            } else {
                return cDownUp;
            }
        }
        if (value < 100) {
            if (strum == 0) {
                return aDown;
            } else {
                return aDownUp;
            }
        }
        if (value < 120) {
            if (strum == 0) {
                return fDown;
            } else {
                return fDownUp;
            }
        }
        if (value < 140) {
            if (strum == 0) {
                return dmDown;
            } else {
                return dmDownUp;
            }
        }
        if (value < 160) {
            if (strum == 0) {
                return bmDown;
            } else {
                return bmDownUp;
            }
        }
        if (value < 180) {
            if (strum == 0) {
                return dDown;
            } else {
                return dDownUp;
            }
        }

        if (strum == 0) {
            return gDown;
        } else {
            return gDownUp;
        }

    }

}
