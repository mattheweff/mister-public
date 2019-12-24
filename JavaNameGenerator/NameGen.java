import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;

/**
Author: Matt Fishel, 2019

A fantasy name generator that reads from a formatted .txt and produces a
list of fantasy names from supplied phoenetic chunks. Relies on
PhoeneticFactory.java to supply short consonant and vowel strings from which
syllables and names are formed.

This was a learning project with attention paid to file i/o and exception
handling.
**/

public class NameGen {

    //constants
    public static final int MAX_SYLLABLES = 4;
    public static final int MAX_NAMES = 20;

    //start main
    public static void main(String[] args) {
        String filename = "generatedNames.txt";
        writeGeneratedNames(filename);
    }//end main

    public static void writeGeneratedNames(String filename) {
        try {
            PrintStream writeFile = new PrintStream(new File(filename));
            PhoeneticFactory pf = new PhoeneticFactory();
            for (int i = 0; i < MAX_NAMES; i++) {
                String nameLine = makeName(pf);
                boolean hasNickname = nameLine.length()>8;
                if (hasNickname){
                    String nickname = makeNickname(nameLine);
                    nameLine = (nickname + ", which is short for " + nameLine);
                }
                outputName(writeFile, nameLine);
            }
            writeFile.close();
        } catch (FileNotFoundException fnf) {
            System.out.println(fnf);
        }
    }

    public static void outputName(PrintStream output, String nameLine)
            throws FileNotFoundException{
        if (output == null) {
            throw new FileNotFoundException("Output file is null.");
        }
        output.println(nameLine);
    }

    public static String makeName(PhoeneticFactory pf) {
        Random rand = new Random();
        int chooser = rand.nextInt(2);
        int howLong = rand.nextInt(MAX_SYLLABLES)+1;
        String name = makeFirstSyllable(chooser, pf);
        if (howLong > 1) {
            name = name + makeRestOfName(chooser, howLong, pf);
        }
        return name;
    } // make name

    public static String makeNickname(String name) {
        Random rand = new Random();
        int newStart = rand.nextInt(3);
        int newLength = 6 + ( rand.nextInt( name.length() - 6 ) / 2 );
        String subname = name.substring(newStart,newLength);
        String firstLetter = subname.substring(0,1).toUpperCase();
        String nickname = firstLetter + subname.substring(1);
        return nickname;
    } // make nickname from name

    public static String makeFirstSyllable(int chooser, PhoeneticFactory pf) {
        boolean isFirst = true;
        boolean isVowel = true;
        String syllable = "";
        if (chooser == 0){
            syllable = pf.getPhoeneticChunk(isFirst, !isVowel)
                       + pf.getPhoeneticChunk(!isFirst, isVowel);
        } else {
            syllable = pf.getPhoeneticChunk(isFirst, isVowel)
                       + pf.getPhoeneticChunk(!isFirst, !isVowel);
        }
        return syllable;
    }//makes first syllable

    public static String makeRestOfName(int chooser, int howLong, PhoeneticFactory pf) {
        Random rand = new Random();
        boolean isFirst = true;
        boolean isVowel = true;
        String restOfName = "";
        for (int i = 1; i < howLong; i++){
            if (chooser == 0){
                restOfName = restOfName + pf.getPhoeneticChunk(!isFirst, !isVowel)
                             + pf.getPhoeneticChunk(!isFirst, isVowel);
            } else {
                restOfName = restOfName + pf.getPhoeneticChunk(!isFirst, isVowel)
                             + pf.getPhoeneticChunk(!isFirst, !isVowel);
            }
        } //makes string of vowels then consonants or consonants then vowels
        if (chooser == 0 && rand.nextInt(2) == 0){
            restOfName = restOfName + pf.getPhoeneticChunk(!isFirst, !isVowel);
        } //50% chance to randomly add a consonant after ending vowel
        return restOfName;
    } //makes rest of name after first syllable

}// public class
