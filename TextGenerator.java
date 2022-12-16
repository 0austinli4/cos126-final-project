// imports data from text file and splits into
// individual arrays for each person
// generates text simulations of markov model and finds
// frequencies of words in text files

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//generates text simulations of markov model and finds frequencies
public class TextGenerator {

    // represents frequency of each string in text file
    static Map<String, Integer> freqs = new HashMap<>();

    // splits text file up into parallel string arrays matching by person
    // implements cleanText and commonWord methods to remove times/symbols/unnecessary symbols
    // returns array of strings based on names
    public static String[] cleanText(String[] names) {
        String[] individualText = new String[names.length];

        int person = 0;
        // reading in strings based on people's names
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            for (int j = 0; j < names.length; j++) {
                if (word.equals(names[j])) {
                    person = j; //switching people
                    individualText[person] = individualText[person] + "\n";
                    word = ""; // to remove repetitive names in array itself
                    StdIn.readString(); //removes last name from contact info
                    break;
                }
            }
            // preventing words from imessage generated text
            if (!commonWord(word)) {
                individualText[person] = individualText[person] + word + " ";
            }
        }
        //remomves null text and symbols based on ascii table
        for (int i = 0; i < individualText.length; i++) {
            if (individualText[i] != null) {
                individualText[i] = removeSymbols(individualText[i]); // removes symbols
            }
        }
        return individualText; //returns String array
    }


    // helper method for cleanText, removing imessage
    // inputted text such at time stamps, dates, replies
    // using regular expressions
    private static boolean commonWord(String input) {
        String re = "([1-9]?[1-9]:[0-9][0-9])|(AM|PM)|((Mon),?)|" +
                "((Tue),?)|((Wed),?)|((Thu),?)|((Fri),?)" +
                "|((Sat),?)|((Sun),?)|((Nov),?)|((Dec),?)" +
                "|(null)|(,)|(^)|(Reply)|(Replies)";
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    // helper method for cleanText, removes all non-ascii characters
    // (such as symbols) and returns the String
    // returns string without symbols
    private static String removeSymbols(String text) {

        StringBuilder cleanText = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            if ((int) text.charAt(i) < 127) {
                cleanText.append(text.charAt(i));
            }
        }

        return cleanText.toString();
    }

    // splits larger sentence strings into sections of keyword strings
    // finds index of keyWord --> takes text 20 char before and after it
    // returns string including the key phrases
    private static String keySentence(String topicWord, String searchFile) {
        StringBuilder keyWordString = new StringBuilder();
        String temp = searchFile;


        // searches a string for a word, then adds to the text
        // +/- 50 characters from that string and returns it
        while (temp.contains(topicWord)) {
            //System.out.println("temp is now : " + temp);
            int index = temp.indexOf(topicWord);
            int range = 50;
            int length = topicWord.length();

            if (index > range && index < (temp.length() - range - length)) {
                keyWordString.append(temp, index - range, index + length + range);
                temp = temp.substring(index + range + length);

            } else if (index < range && index > (temp.length() - range - length)) {
                //case for END OF STRING
                keyWordString.append(temp);
                temp = "";
            } else if (index > (temp.length() - range - length)) {
                //case for END OF STRING
                keyWordString.append(temp, index - range, temp.length());
                temp = "";
            } else if (index < range) { //case for START OF STRING
                keyWordString.append(temp, 0, index + length + range);
                temp = temp.substring(index + range + length);
            } else {
                temp = "";
            }
            keyWordString.append("\n");
        }
        keyWordString.append("\n");

        //if keywords don't exist
        if (keyWordString.length() < 20) {
            System.out.println("key words weren't found");
            return searchFile;
        }

        return keyWordString.toString();
    }

    // simulates markov model for interaction between two people over a key word
    // arguments: order, length, String array organized
    // per person, person1, person2, and topic word
    public static void simulateMarkov(int k, int t, String[] organizedByPerson,
                                      int person1, int person2, String topicWord) {
        String[] names = {"Jasmine", "Sol", "irene", "David", "Joshua", "Sophie"};

        String inputText1 = keySentence(topicWord, organizedByPerson[person1]);
        String inputText2 = keySentence(topicWord, organizedByPerson[person2]);

        //print input texts after finding key sentences
        //System.out.println("INPUT TEXT 1: " + inputText1);
        //System.out.println("INPUT TEXT 2: " + inputText2);

        //print markov model for person n
        MarkovModel model1 = new MarkovModel(inputText1, k);
        MarkovModel model2 = new MarkovModel(inputText2, k);

        int min = Math.min(inputText1.length(), inputText2.length());
        int numOfTexts = 4;
        // loop through for numOfTexts text messages
        for (int count = 0; count < numOfTexts; count++) {

            // markov model starts at a random position
            // in input text rather than first position,
            // to vary messages
            int randPosition = StdRandom.uniformInt(0, min - k - 1);

            //PERSON 1
            String kGram = inputText1.substring(randPosition,
                    randPosition + k); // kgram based on input text
            StdOut.printf(names[person1] + " SAYS: " + kGram);

            for (int i = 0; i < t - k; i++) {
                char temp = model1.random(kGram); // generate new random character
                StdOut.print(temp);
                kGram = kGram + temp;
                kGram = kGram.substring(1, k + 1); // remove first original character
            }
            StdOut.println(); // extra line for readability

            //PERSON 2
            String kGram1 = inputText2.substring(randPosition,
                    randPosition + k); // kgram based on input text
            StdOut.printf(names[person2] + " SAYS: " + kGram1);

            for (int i = 0; i < t - k; i++) {
                char temp = model2.random(kGram1); // generate new random character
                StdOut.print(temp);
                kGram1 = kGram1 + temp;
                kGram1 = kGram1.substring(1, k + 1); // remove first original character
            }
            StdOut.println(); // extra line for readability

        }
    }

    // method for String array of texts for
    // individual people, to prevent null objects and
    // new line breaks
    public static void deleteNull(String[] organizedByPerson) {
        for (int i = 0; i < organizedByPerson.length; i++) {
            if (organizedByPerson[i] != null) {
                if (organizedByPerson[i].contains("null")) {
                    organizedByPerson[i] = organizedByPerson[i].replaceAll("null", "");
                }
                if (organizedByPerson[i].contains("\n")) {
                    organizedByPerson[i] = organizedByPerson[i].replaceAll("\n", "");
                }
            }
        }
    }

    /* DATA ANALYSIS SECTIONS */

    // splits one larger string into individual
    // strings and updates frequencies
    public static void splitString(String input) {
        String[] splitString = input.split(" +");
        for (String s : splitString) {
            updateFrequencies(s);
        }
    }

    // helper method for splitString,
    // updates frequencies of strings in hashmap
    private static void updateFrequencies(String word) {
        if (word != null && !word.equals("")) {
            if (!freqs.containsKey(word))
                freqs.put(word, 1);
            else
                freqs.put(word, freqs.get(word) + 1);
        }
    }

    // finds the top 10 frequent strings and places in HashMap
    // returns hashmap of top 10 strings
    public static Map<String, Integer> topFrequencies() {
        Map<String, Integer> tempFreq = freqs;

        Map<String, Integer> top10 = new HashMap<>();

        int max = 0;
        String maxKey = "";
        for (int i = 0; i < 10; i++) {
            for (String key : tempFreq.keySet()) {
                if (tempFreq.get(key) > max && !genericWord(key)) {
                    max = tempFreq.get(key);
                    maxKey = key;
                }
            }
            top10.put(maxKey, max);
            tempFreq.put(maxKey, 0);
            max = 0;
            maxKey = "";
        }
        return top10;
    }

    // removes generic words from frequency hashMap using Regex
    // returns true if string matches
    // list of common words taken from: https://gist.github.com/gravitymonkey/2406023
    private static boolean genericWord(String input) {
        String re = "(the)|(and)|(a)|(to)|(in)|(is)|(you)|(that)|(it)|(he)|(was)|" +
                "(for)|(on)|(are)|(as)|(with)|(his)|(they)|(i)|(at)|(be)|(this)|(have)|" +
                "(from)|(or)|(one)|(had)|(by)|(word)|(but)|(not)|(what)|(all)|(were)|(we)" +
                "|(when)|(your)|(can)|(said)|(there)|(use)|(am)|(each)|(which)|(1)" +
                "|(do)|(how)|(their)|(if)|(will)|(up)|(other)|(about)|(out)|(many)" +
                "|(then)|(them)|(these)|(so)|(some)|(her)|(would)|(make)|(like)|(him)" +
                "|(into)|(time)|(has)|(just)|(rn)|(ok)|(me)|(now)|(i)|(no)|(way)|(im)" +
                "|(u)|(guys)|(my)|(here)|(r)|(of)|(get)|(go)|(I)|(oh)|(Im)|(where)|(come)" +
                "|(anyone)|(Yes)|(its)|(wanna)|(No)|(Ok)|(still)|(ur)";
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    // displays statistics methods including frequency, and top 3
    // most frequent speakers in the chat
    public static void displayStats(String[] organizedByPerson,
                                    String[] names, Map<String, Integer> top10Main) {
        int counter = 1;
        int person = 0;
        int[] top3People = new int[3];
        String[] top3 = new String[3];
        int max = 0;
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < organizedByPerson.length; i++) {
                if (organizedByPerson[i].length() > max) {
                    max = organizedByPerson[i].length();
                    organizedByPerson[i] = "a";
                    person = i;
                }
            }
            top3People[j] = max;
            top3[j] = names[person];
            max = 0;
        }
        System.out.println("TOP SPEAKERS IN THE CHAT \n");
        for (int j = 0; j < 3; j++) {
            System.out.println(j + ": " + top3[j] + " wrote "
                    + top3People[j] + " characters!");
        }
        System.out.println();
        System.out.println("MOST COMMON WORDS IN THE CHAT \n");
        for (String key : top10Main.keySet()) {
            System.out.println("WORD " + counter + ": " + key +
                    " was said " + top10Main.get(key) + " times!");
            counter++;
        }
        System.out.println(); // extra line for readability
    }

    // main method
    // includes tests for all methods
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);  // order k for markov model
        int t = Integer.parseInt(args[1]);  // length of generated trajectory

        // initiating an array with all texts person has sent from text file
        String[] names = {"Jasmine", "Sol", "irene", "David", "Joshua", "Sophie"};

        //*ALL TEXTS SHOULD INPUT TEST.TXT IN ORDER TO PREVENT PRINTING OF TOO MANY LINES*//

        // *TESTS FOR CLEANTEXT, deleteNull, and RemoveSymbols
        // note: since deleteNull and RemoveSymbols both rely on null values that we can't recreate in strings
        // and we only find in textFiles, we are testing the methods together
        String[] organizedByPerson = cleanText(names);
        deleteNull(organizedByPerson);

        System.out.println("TEST 1: cleanText + deleteNull + removeSymbols  \n SHOULD PRINT ALL " +
                "THE TEXTS PERSON AT INDEX 0 HAS SENT," +
                "without any nulls or symbols, [IT SHOULD MATCH JASMINE.TXT" +
                "WITHOUT NEW LINES");
        System.out.println(organizedByPerson[0] + "\n");

        //TESTING COMMON WORD
        String[] commonWords = {"12:09", "Reply", "food", "AM"};
        System.out.println("TEST 2: commonWords");
        for (String word : commonWords) {
            System.out.println(commonWord(word));
        }
        System.out.println("\n");

        //TESTING KEY SENTENCE
        System.out.println("TEST 3: should print 50 characters before and after every instance of key word" +
                ", including if it's close to the end / beginning. \n should print ");

        String testKeys = "I really like to eat food that's why i eat it every day. \n" +
                "TMRW IS THE DAY THE USA REASSERTS ITS DOMINANCE ON THE KINGDOM" +
                "My favorite type of food in the united states of america" +
                "is bannasn! I think it's the best type of food out of all the ones out there!";

        System.out.println(keySentence("food", testKeys)); // case for topic word in sentence
        System.out.println(keySentence("england", testKeys)); // case for topic word not in sentence

        System.out.println("\n\n\n");
        // simulates markov over topic word
        System.out.println("TEST 4: simulates markov based on topic, print 4 lines each: " + "\n");
        simulateMarkov(k, t, organizedByPerson, 3, 5, "food");

        //TESTING splitString and update frequencies *split string implements updating frequencies"
        System.out.println("\n\n\n");
        System.out.println("TEST 5: tests splitString and updateFrequencies, printing frequency chart");
        String splitter = "I like to pizza mark mark mark eat a marshmallow banana apple apple apricot " +
                "food computer princeton marshmallow banana banana banana banana " +
                "university every food day every water water water water banana " +
                "food a lot food banana food pizza food pizza food day day day food";
        splitString(splitter);

        for (String key : freqs.keySet()) {
            System.out.println(key + ": " + freqs.get(key));
        }

        System.out.println("\n\n\n");
        System.out.println("TEST 6: TopFrequencies and genericWord");
        Map<String, Integer> topOnes = topFrequencies();
        for (String key : topOnes.keySet()) {
            System.out.println(key + ": " + topOnes.get(key));
        }

        System.out.println("\n\n\n");
        System.out.println("TEST 7: TopFrequencies and genericWord");
        displayStats(organizedByPerson, names, topOnes);
    }

}
