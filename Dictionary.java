/*
 * Dictionary.java
 * by Sam Gavis-Hughson
 * 
 * Compilation: javac Dictionary.java
 * Execution: java Dictionary
 * 
 * Dictionary object.  Imports a set of words from a text file (.txt, .dic) and sorts
 * them into alphabetical order, storing them initially into an ArrayList and transferring
 * them to a String array for easier management.  Also sorts out any Strings in the text
 * file that contain characters other than those in the standard English alphabet and
 * stores them in a seperate array.
 * 
 * This does not make any assumptions as to the content of the text file.  It provides
 * means to catch words that contain extraneous characters and when sorting sorts 
 * lowercase versions of words to ensure that they are in alphabetical order regardless
 * of whether they are upper or lower case.
 */ 

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Dictionary {
    private String[] dict, notAdded;
    private int[] index = new int[26];
    private String charset = "UTF-8", fileName;
    private String alpha = "abcdefghijklmnopqrstuvwxyz", vowels = "aeiou";
    private String vowelsRE = "[aeiouAEIOU]";
    private char bc = '\b', nl = '\n'; //backspace and new line characters
    
    
    //Constructor creates dictionary.  Reads in Strings from text file to an ArrayList,
    //transfers to a String array, and sorts into alphabetical order.
    public Dictionary() throws IOException {
        Scanner sc = new Scanner(System.in);
        //eg. dictionary.dic if in the same directory, ../dictionary.dic if up 1 
        //directory, etc.
        System.out.print("Please enter relative dictionary location: "); 
        fileName = sc.nextLine();
        read();
        sort();
    }
    
    //Scanner takes input from FileInputStream, reads in dictionary to ArrayList.
    //Sorts out Strings with incorrect characters.
    private void read() throws IOException {
        ArrayList<String> tempdict = new ArrayList<String>();
        ArrayList<String> tempnot = new ArrayList<String>();
        int i = 0;
        Scanner sc = new Scanner(new FileInputStream(fileName), charset);
        try {
            System.out.print("Loading Dictionary...  ");
            while (sc.hasNext()) {
                String next = sc.next();
                if (next.matches("[a-zA-Z]+")) 
                    tempdict.add(next);
                else tempnot.add(next);
                //Shows location in alphabet while scanning dictionary (does not work 
                //well if the dictionary isn't in alphabetical order but at least indicates
                //that the program is running.
                if (i % 500 == 0) 
                    System.out.print(bc + Character.toString(Character.toUpperCase(next.charAt(0))));
            }
        }
        finally {
            sc.close();
        }
        System.out.print(bc + "Done." + nl);
        dict = new String[tempdict.size()];
        notAdded = new String[tempnot.size()];
        tempdict.toArray(dict);
        tempnot.toArray(notAdded);
    }
    
    //Initial sort method calls merge-sort. If dictionary is already in alphabetical
    //order this takes an insignificant amount of time.
    public void sort() {
        sort(dict, 0, dict.length);
        return;
    }
    
    //Recursive merge-sort method.  Puts dict[] into alphabetical order.
    public void sort(String[] dict, int lo, int hi) {
        int N = hi - lo; 
        if (N <= 1) return; 
        int mid = lo + N/2; 
        sort(dict, lo, mid); 
        sort(dict, mid, hi); 
        
        String[] aux = new String[N]; 
        int i = lo, j = mid; 
        for (int k = 0; k < N; k++) 
        { 
            if      (i == mid) aux[k] = dict[j++]; 
            else if (j == hi)  aux[k] = dict[i++]; 
            else if (dict[j].toLowerCase().compareTo(dict[i].toLowerCase()) < 0) 
                aux[k] = dict[j++]; 
            else    aux[k] = dict[i++]; 
        } 
        
        for (int k = 0; k < N; k++) 
            dict[lo + k] = aux[k];
    } 
    
    //Returns an array with the index of the first word starting with each letter of the
    //alphabet in the dictionary.  Allows to sort through only words starting with a 
    //specific letter.
    public int[] index() {
        char a = '~';
        for (int i = 0; i < dict.length; i++) {
            if (Character.toLowerCase(dict[i].charAt(0)) != a) {
                index[alpha.indexOf(Character.toLowerCase(dict[i].charAt(0)))] = i;
                a = Character.toLowerCase(dict[i].charAt(0));
            }
        }
        return index;
    }
    
    //Takes in String (regular expression) and first letter of word to compare to 
    //dictionary.  If found, returns word, otherwise returns "NO SUGGESTION".
    public String matcher(String s, char first) {
        String toreturn = "";
        int nextindex = 0;
        first = Character.toLowerCase(first);
        //If the word starts with a vowel, words starting with all vowels must be checked.
        //vowelMatcher accomodates that.  Called once for each vowel.
        if (Character.toString(first).matches(vowelsRE)) {
            for (int i = 0; i < 5; i++) {
                if (!vowelMatcher(s, vowels.charAt(i)).matches("~"))
                    toreturn = vowelMatcher(s, vowels.charAt(i));
            }
            if (toreturn == "") toreturn = "NO SUGGESTION";
        }
        else {
            for (int i = index[alpha.indexOf(first)] + 1; i < 26; i++) {
                if (i != 0) {nextindex = index[i]; break;}
            }
            if (nextindex == 0) nextindex = dict.length;
            for (int i = index[alpha.indexOf(first)]; i < nextindex; i++) {
                if (dict[i].matches(s)) toreturn = dict[i];
            }
            if (toreturn == "") toreturn = "NO SUGGESTION";
        }
        return toreturn;
    }       
    
    //Private second matcher method for checking words starting with a specific vowel.
    private String vowelMatcher(String s, char first) {
        String toreturn = "";
        int nextindex = 0;
        for (int i = index[alpha.indexOf(first)] + 1; i < 26; i++) {
            if (i != 0) {nextindex = index[i]; break;}
        }
        if (nextindex == 0) nextindex = dict.length;
        for (int i = index[alpha.indexOf(first)]; i < nextindex; i++) {
            if (dict[i].matches(s)) toreturn = dict[i];
        }
        if (toreturn == "") toreturn = "~";
        return toreturn;
    }
    
    //Displays contents of dictionary.
    public void show() {
        for (String s : dict) System.out.println(s);
    }
    
    //Returns size of dictionary.
    public int size() {
        return dict.length;
    }
    
    //Displays Strings not added to dictionary.
    public void showNotAdded() {
        for (String s : notAdded) System.out.println(s);
    }
    
    //Returns number of Strings not added to dictionary.
    public int sizeNotAdded() {
        return notAdded.length;
    }
    
    //Returns word at index i in dictionary.
    public String wordAt(int i) {
        return dict[i];
    }
    
    //Test client for Dictionary object    
    public static void main(String[] args) throws IOException {
        Dictionary d = new Dictionary();
        char nl = '\n';
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        System.out.println("Added (" + d.size() + "):");
        d.show();
        System.out.println(nl + "Index:");
        int[] index = d.index();
        for (int i = 0; i < 26; i++) System.out.println(alpha.charAt(i) + " " + index[i]);
        System.out.println(nl + "Not Added (" + d.sizeNotAdded() + "):");
        d.showNotAdded();
    }
}