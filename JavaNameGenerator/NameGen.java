import java.util.*;
import java.io.*;

public class NameGen {

   //constants
   public static final int MAX_SYLLABLES = 4;
   public static final int MAX_NAMES = 20;
   
   //I would really like to figure out how to make an object accessible to all my methods by reading the source file. There is definitely a way.
     
   //start main  
   public static void main(String[] args) {
      String filename = "generatedNames.txt";
      try {
         writeGeneratedNames(filename);
      } catch (Exception e){
         e.printStackTrace();
      }
   }//end main
   
   public static void writeGeneratedNames(String filename) {
      try {
         PrintStream writeFile = new PrintStream(new File(filename));
         for (int i = 0; i < MAX_NAMES; i++) outputName(writeFile);
         writeFile.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void outputName(PrintStream output) {
      try {String name = makeName();
         boolean hasNickname = name.length()>8;
         if (hasNickname){
            String nickname = makeNickname(name);
            output.print(nickname + ", which is short for ");
         }
         output.println(name);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static String makeName() {
      Random rand = new Random();
      try {
         String[][] consonants = getConsonants();
         String[][] vowels = getVowels();
         int chooser = rand.nextInt(2);
         int howLong = rand.nextInt(MAX_SYLLABLES)+1;
         String firstSyllable = makeFirstSyllable(chooser,consonants,vowels);
         String name = firstSyllable;
         if (howLong>1){
            name = name + makeRestOfName(chooser,howLong,consonants,vowels);
         }
         return name;
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
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

   public static String makeFirstSyllable(int chooser,String[][] consonants,String[][] vowels){
      boolean isFirst = true;
      String syllable = "";
      if (chooser==0){
         syllable = choosePhoenetic(isFirst,consonants) + choosePhoenetic(!isFirst,vowels);
      } else {
         syllable = choosePhoenetic(isFirst,vowels) + choosePhoenetic(!isFirst,consonants);
      }
      return syllable;
   }//makes first syllable

   public static String makeRestOfName(int chooser,int howLong,String[][] consonants,String[][] vowels){
      Random rand = new Random();
      boolean isFirst = true;
      String restOfName = "";
      for (int i = 1; i< howLong; i++){
         if (chooser==0){
            restOfName = restOfName + choosePhoenetic(!isFirst,consonants) + choosePhoenetic(!isFirst,vowels);
         } else {
            restOfName = restOfName + choosePhoenetic(!isFirst,vowels) + choosePhoenetic(!isFirst,consonants);
         }
      } //makes string of vowels then consonants or consonants then vowels
      if (chooser==0 && rand.nextInt(2)==0){
         restOfName = restOfName + choosePhoenetic(!isFirst,consonants);
      } //50% chance to randomly add a consonant after ending vowel
      return restOfName;
   } //makes rest of name after first syllable

   public static String[] getPhoeneticChunks (String varName) throws FileNotFoundException {
      Scanner lineScanner = new Scanner(new File("sound_source.txt"));
      while (lineScanner.hasNextLine()){
         String s = lineScanner.nextLine();
         int endOfFirstToken = s.indexOf(' ');
         if (endOfFirstToken == -1) continue;
         String firstToken = s.substring(0,endOfFirstToken);
         if (firstToken.equals(varName)) {
            Scanner tokenCounter = new Scanner(s);
            int tokenCount = 0;
            while (tokenCounter.hasNext()){
               tokenCount++;
               tokenCounter.next();
            }
            tokenCounter.close();
            String[] tokenArray = new String[tokenCount-1];
            Scanner tokenScanner = new Scanner(s);
            tokenScanner.next();
            for (int i = 0; i < tokenCount - 1; i++) {
               tokenArray[i] = tokenScanner.next();
            }
            tokenScanner.close();
            lineScanner.close();
            return tokenArray;
         }
      }
      //only happens if something is wrong with the data:
      lineScanner.close();
      String[] nothing = {"look at the data"};
      return nothing;
   }

   public static final String[][] getConsonants() {
      String[][] consonants = new String[6][];
      try {
      consonants[0] = getPhoeneticChunks("COMMON_LEAD_CONSONANTS:");
      consonants[1] = getPhoeneticChunks("UNUSUAL_LEAD_CONSONANTS:");  
      consonants[2] = getPhoeneticChunks("EXOTIC_LEAD_CONSONANTS:");
      consonants[3] = getPhoeneticChunks("COMMON_FOLLOW_CONSONANTS:");
      consonants[4] = getPhoeneticChunks("UNUSUAL_FOLLOW_CONSONANTS:");
      consonants[5] = getPhoeneticChunks("EXOTIC_FOLLOW_CONSONANTS:");
      } catch (Exception e){
         e.printStackTrace();
         return null;
      }
      return consonants;
   }
   
   public static final String[][] getVowels() {
      String[][] vowels = new String[6][];
      try {
      vowels[0] = getPhoeneticChunks("COMMON_LEAD_VOWELS:");
      vowels[1] = getPhoeneticChunks("UNUSUAL_LEAD_VOWELS:");
      vowels[2] = getPhoeneticChunks("EXOTIC_LEAD_VOWELS:");
      vowels[3] = getPhoeneticChunks("COMMON_FOLLOW_VOWELS:");
      vowels[4] = getPhoeneticChunks("UNUSUAL_FOLLOW_VOWELS:");
      vowels[5] = getPhoeneticChunks("EXOTIC_FOLLOW_VOWELS:");
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
      return vowels;
   }

   public static String choosePhoenetic(boolean isFirst,String[][] phoenetics) {
      Random rand = new Random();
      int category = rand.nextInt(10);
      String phoenetic = "";
      if (isFirst) {
         if (category<6) phoenetic = phoenetics[0][rand.nextInt(phoenetics[0].length)];//common lead
         else if (category<9) phoenetic = phoenetics[1][rand.nextInt(phoenetics[1].length)];//unusual lead
         else phoenetic = phoenetics[2][rand.nextInt(phoenetics[2].length)];//exotic lead
      } else if (!isFirst) {
         if (category<6) phoenetic = phoenetics[3][rand.nextInt(phoenetics[3].length)];//common follow
         else if (category<9) phoenetic = phoenetics[4][rand.nextInt(phoenetics[4].length)];//unusual follow
         else phoenetic = phoenetics[5][rand.nextInt(phoenetics[5].length)];//exotic follow
      }
      return phoenetic;
   }  
    
}// public class
