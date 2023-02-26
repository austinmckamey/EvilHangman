package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {

    private String chosen = "";
    private Set<String> dict = new TreeSet<>();
    private final Map<String, Set<String>> subsets = new TreeMap<>();
    private final SortedSet<Character> guessedLetters = new TreeSet<>();

    public EvilHangmanGame() {

    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        dict.clear();
        Scanner input = new Scanner(dictionary);
        while(input.hasNext()) {
            String word = input.next();
            if(word.length() == wordLength) {
                dict.add(word);
            }
        }
        if(dict.size() == 0) {
            throw new EmptyDictionaryException("Dictionary is empty.\n");
        }
    }

    @Override
    public Set<String> makeGuess(char g) throws GuessAlreadyMadeException {
        Character guess = Character.toLowerCase(g);
        if(guessedLetters.contains(guess)) {
            throw new GuessAlreadyMadeException("Guess already made! Enter guess: ");
        }
        guessedLetters.add(guess);
        for(String word : dict) {
            String key = getSubsetKey(word, guess);
            if(subsets.containsKey(key)) {
                subsets.get(key).add(word);
            }
            else {
                Set<String> newSubset = new TreeSet<>();
                newSubset.add(word);
                subsets.put(key, newSubset);
            }
        }
        Set<String> largest = new TreeSet<>();
        String temp = "";
        int max = 0;
        String c = String.valueOf(guess);
        for(Map.Entry<String, Set<String>> subset : subsets.entrySet()) {
            if (subset.getValue().size() > max) {
                max = subset.getValue().size();
                temp = subset.getKey();
                largest = subset.getValue();
            }
            else if(subset.getValue().size() == max) {
                if(subset.getKey().chars().filter(ch -> ch == g).count() <
                        temp.chars().filter(ch -> ch == g).count()) {
                    temp = subset.getKey();
                    largest = subset.getValue();
                }
                else if(subset.getKey().chars().filter(ch -> ch == g).count() ==
                        temp.chars().filter(ch -> ch == g).count()) {
                    if (subset.getKey().lastIndexOf(c) >= temp.lastIndexOf(c)) {
                        if (subset.getKey().indexOf(c) > temp.indexOf(c)) {
                            temp = subset.getKey();
                            largest = subset.getValue();
                        }
                    }
                }
            }
        }
        StringBuilder combined = new StringBuilder(temp);
        for(int i = 0; i < chosen.length(); ++i) {
            if(Character.isLetter(chosen.charAt(i))) {
                combined.deleteCharAt(i);
                combined.insert(i, chosen.charAt(i));
            }
        }
        chosen = combined.toString();
        dict = largest;
        subsets.clear();
        return largest;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    private String getSubsetKey(String word, char guessedLetter) {
        StringBuilder key = new StringBuilder();
        for(int i = 0; i < word.length(); ++i) {
            if(word.charAt(i) != guessedLetter) {
                key.append('-');
            }
            else {
                key.append(word.charAt(i));
            }
        }
        return key.toString();
    }

    public String getChosen() {
        return chosen;
    }
    
    public String getFinal() {
        String lostWord = null;
        for(String words : dict) {
            lostWord = words;
        }
        return lostWord;
    }
}
