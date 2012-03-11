/*
 * SpellCheck.java
 * by Sam Gavis-Hughson
 * 
 * Compilation: javac WordGenerator.java
 * Execution: java WordGenerator
 * 
 * Dependencies: Dictionary.java
 * 
 * This program takes random words from a Dictionary object and converts them to an 
 * String which is an incorrect spelling of the word based on specific rules.  For each
 * character, if it is a vowel, it will be replaced by an arbitrary number of random 
 * vowels, both upper and lower case.  Otherwise, it will be replaced by an arbitrary
 * number of capital and lowercase versions of itself.
 */

import java.util.Scanner;
import java.lang.Character;
import java.io.*;

public class WordGenerator {
    public static void main(String[] args) throws IOException {
        String vowels = "aeiou";
        int MAXCHAR = 10;
        Scanner sc = new Scanner(System.in);
        Dictionary dict = new Dictionary();
        System.out.print("Number of words to generate: ");
        int numWords = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < numWords; i++) {
            String finalWord = "";
            String initWord = (dict.wordAt((int) (Math.random() * dict.size())));
            for (int j = 0; j < initWord.length(); j++) {
                if (Character.toString(initWord.charAt(j)).matches("[aeiouAEIOU]")) {
                    for (int k = 0; k < Math.random() * MAXCHAR; k++) {
                        int rand = (int) (Math.random() * 5);
                        char c;
                        if (rand < 1) c = vowels.charAt(0);
                        else if (rand < 2) c = vowels.charAt(1);
                        else if (rand < 3) c = vowels.charAt(2);
                        else if (rand < 4) c = vowels.charAt(3);
                        else c = vowels.charAt(4);
                        if (Math.random() < 0.5) finalWord += Character.toLowerCase(c);
                        else finalWord += Character.toUpperCase(c);
                    }
                }
                else {
                    for (int k = 0; k < Math.random() * MAXCHAR; k++) {
                        if (Math.random() < 0.5) finalWord 
                            += Character.toLowerCase(initWord.charAt(j));
                        else finalWord += Character.toUpperCase(initWord.charAt(j));
                    }
                }
            }
            System.out.println(finalWord);
        }
    }
}
        