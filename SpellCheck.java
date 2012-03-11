/*
 * SpellCheck.java
 * by Sam Gavis-Hughson
 * 
 * Compilation: javac SpellCheck.java
 * Execution: java SpellCheck
 * 
 * Dependencies: Dictionary.java
 * 
 * This is a client program for Dictionary.java.  It takes Strings as inputs and generates
 * REs following a prescribed set of rules to represent those Strings.  It then calls
 * the dictionary matches() method to compare the REs to the contents of the dictionary
 * and print out the result.
 */
import java.util.Scanner;
import java.lang.*;
import java.io.*;

public class SpellCheck {
    
    //Read in individual inputs from StdIn
    public static String reader() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    //Creates Regular Expression from input string.  This can be compared to 
    //dictionary entries.  
    public static String makeRE(String s) {
        char a = '~';
        String re = "";
        String consonants = "[a-zA-Z&&[^aeiouAEIOU]]";
        for (int i = 0; i < s.length(); i++) {
            String myChar = Character.toString(s.charAt(i));
            if (myChar.matches(consonants)) {
                if (Character.toLowerCase(s.charAt(i)) != a) {
                    re += "[" + Character.toLowerCase(s.charAt(i)) 
                        + Character.toUpperCase(s.charAt(i)) + "]+";
                    a = Character.toLowerCase(s.charAt(i));
                }
            }
            else {
                if (a != 'a') {
                    re += "[aeiouAEIOU]+";
                    a = 'a';
                }
            }
        }       
        return re;
    }
    
    //Main method loops until program is terminated.  Creates new dictionary and then
    //compares REs created from user input to dictionary, printing out the results.
    public static void main(String[] args) throws IOException {
        Dictionary dict = new Dictionary();
        if (dict.sizeNotAdded() != 0) {
        System.out.println(dict.sizeNotAdded() 
                               + " Strings not added because they contain non-English alphabet characters.");
        }
        while (true) {
            System.out.print("> ");
            String test = SpellCheck.reader();
            String re = SpellCheck.makeRE(test);
            System.out.println(dict.matcher(re, test.charAt(0)));
        }
    }
}