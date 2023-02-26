package hangman;

import java.io.*;
import java.util.Scanner;

public class EvilHangman {

    public static void main(String[] args) throws EmptyDictionaryException, IOException {
        File file = new File(args[0]);
        int wordlength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);

        EvilHangmanGame game = new EvilHangmanGame();
        game.startGame(file, wordlength);
        while(true) {
            if(guesses == 0) {
                System.out.print("Sorry, you lost! The word was: " + game.getFinal() + '\n');
                break;
            }
            if(game.getChosen().length() != 0 && !game.getChosen().contains("-")) {
                System.out.print("You win! You guessed the word: " + game.getChosen() + '\n');
                break;
            }
            if(guesses == 1) {
                System.out.print("You have " + guesses + " guess left\n");
            }
            else {
                System.out.print("You have " + guesses + " guesses left\n");
            }
            System.out.print("Used letters: " + game.getGuessedLetters() + '\n');
            System.out.print("Word: " + game.getChosen() + '\n');
            boolean validGuess = false;
            while(!validGuess) {
                Scanner sc = new Scanner(System.in);
                System.out.print("Enter guess: ");
                String input = sc.next();
                char guess = input.charAt(0);
                char g = Character.toLowerCase(guess);
                if (Character.isLetter(guess) && input.length() == 1) {
                    try {
                        game.makeGuess(guess);
                        validGuess = true;
                        if (game.getChosen().contains(String.valueOf(guess))) {
                            int count = (int) game.getChosen().chars().filter(ch -> ch == guess).count();
                            if (count == 1) {
                                System.out.print("Yes, there is " + count + " " + g + "\n\n");
                            } else {
                                System.out.print("Yes, there are " + count + " " + g + "'s\n\n");
                            }
                        } else {
                            System.out.print("Sorry, there are no " + g + "'s" + "\n\n");
                            --guesses;
                        }
                    } catch (GuessAlreadyMadeException e) {
                        System.out.print("Guess already made! ");
                    }
                } else {
                    System.out.print("Invalid input! ");
                }
            }
        }
    }
}
