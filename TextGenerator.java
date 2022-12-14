import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextGenerator {

    static Map<String, Integer> freqs = new HashMap<String, Integer>();

    // splits text file up into parallel string arrays matched by person
    // implements cleanText to remove dates/times / unnecessary texts or symbols
    public static String[] cleanText(String[] names) {
        String[] individualText = new String[names.length];

        int person = 0;

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

            if (!commonWord(word)) {
                individualText[person] = individualText[person] + word + " ";
            }
        }
        for (int i = 0; i < individualText.length; i++) {
            individualText[i] = removeSymbols(individualText[i]) + "\n"; // removes symbols
            // new line at end of every string for readability
        }
        return individualText; //returns String array
    }

    // helper method for inByPerson, removing common words such at time stamps, dates, months
    public static boolean commonWord(String input) {
        String re = "([1-9]?[1-9]:[0-9][0-9])|(AM|PM)|((Mon),?)|((Tue),?)|((Wed),?)|((Thu),?)|((Fri),?)" +
                "|((Sat),?)|((Sun),?)|((Nov),?)|((Dec),?)|(null)";
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    // helper method for inByPerson, removes all non-ascii characters and returns String
    public static String removeSymbols(String text) {
        StringBuilder cleanText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if ((int) text.charAt(i) < 127) {
                cleanText.append(text.charAt(i));
            }
        }
        return cleanText.toString();
    }

    // prints the text of individual person, also returns their textString
    public static String printByPerson(String[] organized, String[] names, int numPerson) {
        System.out.print(names[numPerson] + "'s texts: " + organized[numPerson]);
        return organized[numPerson];
    }

    // SIMULATE A TEXT CONVERSATION
    public static String simulate(String topicWords, String input2) {

        String temp = input2;
        int position = 0;
        int index;
        while (temp.contains(" ")) {
            index = temp.indexOf(" ");
            String word = (temp.substring(position, index));

            /* INSERT YOUR METHOD HERE - WHATEVER YOU WANT TO DO WITH THIS STRING */

            position = index + 1;
            temp = temp.substring(position);
            position = 0;
        }
        System.out.print(temp); //LAST WORD
        return "";
    }

    public static String first5last5(String topicWord, String searchFile) {
        StringBuilder keyWordString = new StringBuilder();
        String temp = searchFile;

        while (temp.contains(topicWord)) {
            int index = temp.indexOf(topicWord);
            System.out.println("index of mother is: " + index);
            int range = 20;

            if (index > range && index < (temp.length() - range - 5)) {
                keyWordString.append(temp, index - range, index + 5 + range);
                temp = temp.substring(index + range + 1);
                System.out.println("temp is: " + temp);
            } else if (index > (temp.length() - range - 5)) { //case for END OF STRING
                keyWordString.append(temp, index - range, searchFile.length());
                temp = "";
            }
            keyWordString.append("\n");

        }

        return keyWordString.toString();
    }


    // splits larger text in string (i.e. full sentence) into individual strings -
    // maybe put them in an array? or just add code to this method for frequency table
    public static void splitString(String input) {
        String temp = input;
        int position = 0;
        int index;
        while (temp.contains(" ")) {
            index = temp.indexOf(" ");
            String word = (temp.substring(position, index));

            /* INSERT YOUR METHOD HERE - WHATEVER YOU WANT TO DO WITH THIS STRING */
            updateFrequencies(word);

            position = index + 1;
            temp = temp.substring(position);
            position = 0;
        }
        updateFrequencies(temp);
        System.out.print(temp); //LAST WORD
    }

    public static void updateFrequencies(String word) {
        if (word != null && !word.equals("")) {
            if (!freqs.containsKey(word))
                freqs.put(word, 1);
            else
                freqs.put(word, freqs.get(word) + 1);
        }
    }

    public static String getLeast(Map<String, Integer> top10, String first) {
        String least = first;

        for (String key : top10.keySet()) {
            if (top10.get(key) < top10.get(least)) {
                least = key;
            }
        }
        return least;
    }


    public static Map<String, Integer> topFrequencies() {
        int first10Check = 0;
        String firstString = "";

        Map<String, Integer> top10 = new HashMap<String, Integer>();

        for (String key : freqs.keySet()) {
            if (first10Check > 10) {
                break;
            }

            first10Check++;
            top10.put(key, freqs.get(key));
        }


        for (String key : freqs.keySet()) {
            String least = firstString;
            least = getLeast(top10, least);
            if (!key.equals(least)) {
                if (freqs.get(key) > top10.get(least)) {
                    top10.remove(least);
                    top10.put(key, freqs.get(key));
                }
            }
        }
        return top10;
    }

    // Test generator creates trajectory of length t based on markov class
    public static void main(String[] args) {

        int k = Integer.parseInt(args[0]);  // order k for markov model
        int t = Integer.parseInt(args[1]);  // length of generated trajectory

        //initiating an array with all texts person has sent from text file
        String[] names = {"Jasmine", "Sol", "irene", "David", "Joshua", "Sophie"};
        String[] organizedByPerson = cleanText(names);

        //printing an indivdual person's texts
        int n = 3;
        //String inputText = printByPerson(organizedByPerson, names, n);
        String inputText = organizedByPerson[5];
        //System.out.print(inputText);

        //TESTING splitString
        String splitStringTest = "I like to eat food";
        //splitString(splitStringTest);

        //testing commonWord
        String common = "Tue";
        String tuesdaycomma = "Thu,";
//        System.out.println(commonWord(common));
//        System.out.println(commonWord(tuesdaycomma));

        //first5last TESTING
        String tester = "I really like to eat the food that my mother cooks every night when I come home and then I " +
                "really really like when I get to eat my mother good food always";
        System.out.print(first5last5("mother", tester));

        // testing new frequency methods
        for (String personString : organizedByPerson) {
            splitString(personString);
        }
        for (String key : freqs.keySet())
            System.out.println(key + ": " + freqs.get(key));

        StdOut.print("\n\n\n\n");

        Map<String, Integer> top10Main = topFrequencies();
        for (String key : top10Main.keySet())
            System.out.println(key);
    }
//
    // print markov model for person n
//        MarkovModel model = new MarkovModel(inputText, k);
//        String kGram = inputText.substring(0, k); // kgram based on input text
//        StdOut.printf(kGram);
//
//        for (int i = 0; i < t - k; i++) {
//            char temp = model.random(kGram); // generate new random character
//            StdOut.print(temp);
//            kGram = kGram + temp;
//            kGram = kGram.substring(1, k + 1); // remove first original character
//        }
//        StdOut.println(); // extra line for readability
}
}

//extra shit
////testing what StdIn counts for different strings
////    public static void testStdIn() {
////        while (!StdIn.isEmpty()) {
////            System.out.println(StdIn.readString());
////        }
////    }
