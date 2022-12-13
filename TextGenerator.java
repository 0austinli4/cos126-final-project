public class TextGenerator {

    public static String textOrganizing(String text) {
        StringBuilder cleanText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if ((int) text.charAt(i) < 127) {
                cleanText.append(text.charAt(i));
            }
        }
        return cleanText.toString();
    }

    public static String[] textOrganization() {
        String[] names = {"Jasmine", "Sol", "irene", "David", "Joshua", "Sophie"};
        String[] individualText = new String[names.length];

        int person = 0;
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            for (int j = 0; j < names.length; j++) {
                if (word.equals(names[j])) {
                    person = j;
                    individualText[person] = individualText[person] + "\n";
                    word = "";
                    StdIn.readString();
                    break;
                }
            }
            if (!StdIn.isEmpty()) {
                individualText[person] = individualText[person] + word + " ";
            }
        }
        for (int i = 0; i < individualText.length; i++) {
            individualText[i] = individualText[i] + "\n";
        }
        return individualText;
    }

    public static String cleanText(String input) {
        int position = 0;
        String temp = input;
        for (int i = 0; i < input.length(); i++) {
            String word = temp.substring(0, temp.indexOf(" "));
            //CHECK REGEX SHIT
//            if (Pattern.matches("[1-9:0-90-9]", word)) {
//                input.append(text.charAt(i));
//            }
        }
        return input.toString();
    }

    public static String printByPerson(String[] organized, int numPerson) {
        String output = textOrganizing(organized[numPerson]);
        String[] names = {"Jasmine", "Sol", "irene", "David", "Joshua", "Soph"};
        //System.out.print(names[numPerson] + "'s texts: " + output);
        return output;
    }


    // Test generator creates trajectory of length t based on markov class
    public static void main(String[] args) {

        int k = Integer.parseInt(args[0]);  // order k for markov model
        int t = Integer.parseInt(args[1]);  // length of generated trajectory
        //String inputText = textOrganizing(text);
        String[] organizedByPerson = textOrganization();


        //print markov model for person n
        int n = 1;
        String inputText = printByPerson(organizedByPerson, n);

        MarkovModel model = new MarkovModel(inputText, k);
        String kGram = inputText.substring(0, k); // kgram based on input text
        StdOut.printf(kGram);

        for (int i = 0; i < t - k; i++) {
            char temp = model.random(kGram); // generate new random character
            StdOut.print(temp);
            kGram = kGram + temp;
            kGram = kGram.substring(1, k + 1); // remove first original character
        }
        StdOut.println(); // extra line for readability
    }
}
