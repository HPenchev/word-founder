import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class WordFounder {
    private static final Collection<String> allWords;

    static {
        try {
            allWords = loadAllWords();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        Collection<String> validNineLetterWords =
                allWords.stream().filter(w -> w.length() == 9 && isWordValidAfterOneLetterRemoval(w))
                        .toList();

        long endTime = System.nanoTime();


        System.out.println("Execution time: " + (endTime - startTime) / 1_000_000 + " milliseconds " +
                "and filtered " + validNineLetterWords.size() + " words");

    }

    private static boolean isWordValidAfterOneLetterRemoval(String word) {
        if(word.equals("A") || word.equals("I")) {
            return true;
        }

        if(!allWords.contains(word) || !(word.contains("I") || word.contains("A"))) {
            return false;
        }

        for(int i = 0; i < word.length(); i++) {
            if (isWordValidAfterOneLetterRemoval(word.substring(0, i) + word.substring(i + 1))) {
                return true;
            }
        }

        return false;
    }

    private static Collection<String> loadAllWords() throws IOException, URISyntaxException {
        try(BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(
                                    new URI("https://raw.githubusercontent.com/nikiiv/" +
                                            "JavaCodingTestOne/master/scrabble-words.txt")
                                            .toURL()
                                            .openConnection()
                                            .getInputStream()))) {

            return br.lines().skip(2).collect(Collectors.toCollection(HashSet::new));
        }
    }
}
