import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextGenerator {

    static Map<String, Integer> freqs = new HashMap<String, Integer>();

    // splits text file up into parallel string arrays matched by person
    // implements cleanText to remove dates/times / unnecessary texts or symbols
    // COMMON WORD: WORKS
    // REMOVE SYMBOLS WORKS
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
            //System.out.println(names[i] + ": " + individualText[i]);
            individualText[i] = removeSymbols(individualText[i]); // removes symbols
            //+ "\n"
            // new line at end of every string for readability
        }
        return individualText; //returns String array
    }

    // helper method for inByPerson, removing common words such at time stamps, dates, months
    public static boolean commonWord(String input) {
        String re = "([1-9]?[1-9]:[0-9][0-9])|(AM|PM)|((Mon),?)|((Tue),?)|((Wed),?)|((Thu),?)|((Fri),?)" +
                "|((Sat),?)|((Sun),?)|((Nov),?)|((Dec),?)|(null)|(,)|(^)";
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
        if (cleanText.indexOf("null") != -1) {
            cleanText.delete(cleanText.indexOf("null"), cleanText.indexOf("null") + 4);
        }
        return cleanText.toString();
    }

    public static String keySentence(String topicWord, String searchFile) {
        StringBuilder keyWordString = new StringBuilder();
        String temp = searchFile;

        while (temp.contains(topicWord)) {
            //System.out.println("temp is now : " + temp);
            int index = temp.indexOf(topicWord);
            int range = 50;
            int length = topicWord.length();

            if (index > range && index < (temp.length() - range - length)) {
                keyWordString.append(temp, index - range, index + length + range);
                temp = temp.substring(index + range + length);

            } else if (index < range && index > (temp.length() - range - length)) { //case for END OF STRING
                keyWordString.append(temp);
                temp = "";
            } else if (index > (temp.length() - range - length)) { //case for END OF STRING
                //System.out.println("temp is more than string, index is: " + index);
                keyWordString.append(temp, index - range, temp.length());
//                System.out.println("appended: " + keyWordString.toString());
                temp = "";
            } else if (index < range) { //case for START OF STRING
                //System.out.println("index is less than string");
                keyWordString.append(temp, 0, index + length + range);
                temp = temp.substring(index + range + length);
            } else {
                temp = "";
            }
            keyWordString.append("\n");
        }
        keyWordString.append("\n");

        //IF KEY WORDS DON"T EXIST NOT ENOUGH
        if (keyWordString.length() < 10) {
            System.out.println("key words weren't found");
            return searchFile;
        }

        return keyWordString.toString();
    }


    public static void simulateMarkov(int k, int t, String[] organizedByPerson, int person1, int person2, String topicWord) {
        String[] names = {"Jasmine", "Sol", "irene", "David", "Joshua", "Sophie"};

        organizedByPerson[person1] = keySentence(topicWord, organizedByPerson[person1]);
        organizedByPerson[person2] = keySentence(topicWord, organizedByPerson[person2]);

        String inputText1 = organizedByPerson[person1];
        String inputText2 = organizedByPerson[person2];

        //printing input texts after finding key sentences
//        System.out.println("INPUT TEXT 1: " + inputText1);
//        System.out.println("INPUT TEXT 2: " + inputText2);

        //print markov model for person n
        MarkovModel model1 = new MarkovModel(inputText1, k);
        MarkovModel model2 = new MarkovModel(inputText2, k);

        int stringPosition = 0;
        for (int count = 0; count < 4; count++) {
            //PERSON 1
            String kGram = inputText1.substring(stringPosition, k + stringPosition); // kgram based on input text
            StdOut.printf(names[person1] + " SAYS: " + kGram);

            for (int i = 0; i < t - k; i++) {
                char temp = model1.random(kGram); // generate new random character
                StdOut.print(temp);
                kGram = kGram + temp;
                kGram = kGram.substring(1, k + 1); // remove first original character
            }
            StdOut.println(); // extra line for readability

            //PERSON 2
            String kGram1 = inputText2.substring(stringPosition, k + stringPosition); // kgram based on input text
            StdOut.printf(names[person2] + " SAYS: " + kGram1);

            for (int i = 0; i < t - k; i++) {
                char temp = model2.random(kGram1); // generate new random character
                StdOut.print(temp);
                kGram1 = kGram1 + temp;
                kGram1 = kGram1.substring(1, k + 1); // remove first original character
            }
            StdOut.println(); // extra line for readability

            stringPosition++;
        }
    }

    public static void deleteNull(String[] organizedByPerson) {
        for (int i = 0; i < organizedByPerson.length; i++) {
            if (organizedByPerson[i].contains("null")) {
                organizedByPerson[i] = organizedByPerson[i].replaceAll("null", "");
            }
            if (organizedByPerson[i].contains("\n")) {
                organizedByPerson[i] = organizedByPerson[i].replaceAll("\n", "");
            }

        }
    }


    //DATA ANALYSIS SECTIONS
    // splits strings and updates frequency tables
    public static void splitString(String input) {
        String[] splitString = input.split(" +");
        for (String s : splitString) {
            //System.out.println(s);
            updateFrequencies(s);
        }
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
        deleteNull(organizedByPerson);

        simulateMarkov(k, t, organizedByPerson, 1, 2, "come");

        //testing delete null
        // String[] nuller = {"null adiawndowi"};
        //deleteNull(nuller);
        //System.out.print(nuller[0]);

        //printing an indivdual person's texts
        //String inputText = organizedByPerson[5];
        //System.out.print(inputText);

        //TESTING splitString
        String splitStringTest = "I like to eat food";
        //splitString(splitStringTest);

        //testing commonWord
        String common = "Tue";
        String tuesdaycomma = "Thu,";
//        System.out.println(commonWord(common));
//        System.out.println(commonWord(tuesdaycomma));


        //TESTING KEY SENTENCES TESTING
        String tester = "I just be that motherfucker bumping n shit and then I just go to the gym and keep on " +
                "eating shit because that's what i like to do when i come to the library so shit man";
        //System.out.print(keySentence("shit", tester));

        //testing simulate
        //PERSON 1
//        String inputText = organizedByPerson[0];
//        MarkovModel model = new MarkovModel(inputText, k);
//
//        for (int count = 0; count < 3; count++) {
//            String kGram = inputText.substring(0, k); // kgram based on input text
//            StdOut.printf(names[0] + " SAYS: ");
//
//            for (int i = 0; i < t - k; i++) {
//                char temp = model.random(kGram); // generate new random character
//                StdOut.print(temp);
//                kGram = kGram + temp;
//                kGram = kGram.substring(1, k + 1); // remove first original character
//            }
//            StdOut.println(); // extra line for readability
//        }


        //first5last TESTING


        // testing new frequency methods
//        for (String personString : organizedByPerson) {
//            splitString(personString);
//        }

//        for (String key : freqs.keySet())
//            System.out.println(key + ": " + freqs.get(key));
//
//        StdOut.print("\n\n\n\n");
//
//        Map<String, Integer> top10Main = topFrequencies();
//        for (String key : top10Main.keySet())
//            System.out.println(key);
//        StdOut.println(); // extra line for readability

    } //end of main

} // end of class
