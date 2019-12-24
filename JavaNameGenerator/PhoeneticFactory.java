import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class PhoeneticFactory {
    private List<String> commonLeadVowels;
    private List<String> unusualLeadVowels;
    private List<String> exoticLeadVowels;
    private List<String> commonFollowVowels;
    private List<String> unusualFollowVowels;
    private List<String> exoticFollowVowels;
    private List<String> commonLeadConsonants;
    private List<String> unusualLeadConsonants;
    private List<String> exoticLeadConsonants;
    private List<String> commonFollowConsonants;
    private List<String> unusualFollowConsonants;
    private List<String> exoticFollowConsonants;

    public PhoeneticFactory() {
        try {
            commonLeadVowels = getPhoeneticChunks("COMMON_LEAD_VOWELS:");
            unusualLeadVowels = getPhoeneticChunks("UNUSUAL_LEAD_VOWELS:");
            exoticLeadVowels = getPhoeneticChunks("EXOTIC_LEAD_VOWELS:");
            commonFollowVowels = getPhoeneticChunks("COMMON_FOLLOW_VOWELS:");
            unusualFollowVowels = getPhoeneticChunks("UNUSUAL_FOLLOW_VOWELS:");
            exoticFollowVowels = getPhoeneticChunks("EXOTIC_FOLLOW_VOWELS:");
            commonLeadConsonants = getPhoeneticChunks("COMMON_LEAD_CONSONANTS:");
            unusualLeadConsonants = getPhoeneticChunks("UNUSUAL_LEAD_CONSONANTS:");
            exoticLeadConsonants = getPhoeneticChunks("EXOTIC_LEAD_CONSONANTS:");
            commonFollowConsonants = getPhoeneticChunks("COMMON_FOLLOW_CONSONANTS:");
            unusualFollowConsonants = getPhoeneticChunks("UNUSUAL_FOLLOW_CONSONANTS:");
            exoticFollowConsonants = getPhoeneticChunks("EXOTIC_FOLLOW_CONSONANTS:");
        } catch (FileNotFoundException fnf) {
            System.out.println(fnf);
        }
    }

    private List<String> getPhoeneticChunks (String varName) throws FileNotFoundException {
        File source = new File("sound_source.txt");
        if (source == null) throw new FileNotFoundException("Source is null.");
        Scanner lineScanner = new Scanner(source);
        while (lineScanner.hasNextLine()){
            String s = lineScanner.nextLine();
            int endOfFirstToken = s.indexOf(' ');
            if (endOfFirstToken == -1) continue;
            String firstToken = s.substring(0, endOfFirstToken);
            if (firstToken.equals(varName)) {
                Scanner chunkScanner = new Scanner(s);
                chunkScanner.next();
                List<String> listOfChunks = new ArrayList<>(32);
                while (chunkScanner.hasNext()) {
                    listOfChunks.add(chunkScanner.next());
                }
                chunkScanner.close();
                return listOfChunks;
            }
        }
        //only happens if something is wrong with the data:
        throw new FileNotFoundException("Check the data.");
    }

    public String getPhoeneticChunk(boolean isFirst, boolean isVowel) {
        Random rand = new Random();
        int category = rand.nextInt(100);
        if (isFirst && isVowel) {
            if (category < 65) return commonLeadVowels.get(
                    rand.nextInt(commonLeadVowels.size()));//common lead
            else if (category < 90) return unusualLeadVowels.get(
                    rand.nextInt(unusualLeadVowels.size()));//unusual lead
            else return exoticLeadVowels.get(
                    rand.nextInt(exoticLeadVowels.size()));//exotic lead
        } else if (!isFirst && isVowel) {
            if (category < 65) return commonFollowVowels.get(
                    rand.nextInt(commonFollowVowels.size()));//common follow
            else if (category < 90) return unusualFollowVowels.get(
                    rand.nextInt(unusualFollowVowels.size()));//unusual follow
            else return exoticFollowVowels.get(
                    rand.nextInt(exoticFollowVowels.size()));//exotic follow
        } else if (isFirst && !isVowel) {
            if (category < 65) return commonLeadConsonants.get(
                    rand.nextInt(commonLeadConsonants.size()));
            else if (category < 90) return unusualLeadConsonants.get(
                    rand.nextInt(unusualLeadConsonants.size()));
            else return exoticLeadConsonants.get(
                    rand.nextInt(exoticLeadConsonants.size()));
        } else {
            if (category < 65) return commonFollowConsonants.get(
                    rand.nextInt(commonFollowConsonants.size()));
            else if (category < 90) return unusualFollowConsonants.get(
                    rand.nextInt(unusualFollowConsonants.size()));
            else return exoticFollowConsonants.get(
                    rand.nextInt(exoticFollowConsonants.size()));
        }
    }
}
