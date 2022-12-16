// prints out strings based on order and
// length using frequency of strings
// predicts based on characters before current char
public class MarkovModel {
    private ST<String, Integer> oneArg; // frequency of each kgram

    // array of frequency of each character following each kgram
    private ST<String, int[]> twoArg;

    private int k; // order of Markov Model

    // creates a Markov model of order k based on the specified text
    public MarkovModel(String text, int k) {
        // symbol table for frequency of kgram
        oneArg = new ST<String, Integer>();
        // symbol table for frequency of characters following kgram
        twoArg = new ST<String, int[]>();
        this.k = k;

        // populate oneArg + twoArg
        for (int i = 0; i < text.length(); i++) {
            String circularString = text + text.substring(0, i + 1);
            String kGram = circularString.substring(i, i + k);
            char nextChar = circularString.charAt(i + k);

            if (!oneArg.contains(kGram)) { // key doesn't exist yet
                oneArg.put(kGram, 1);
                int[] values = new int[128]; // empty ASCII array
                values[nextChar]++;
                twoArg.put(kGram, values);
            } else { // key already exists
                int n = oneArg.get(kGram);
                oneArg.put(kGram, n + 1);
                int[] values = twoArg.get(kGram);
                values[nextChar]++;
                twoArg.put(kGram, values);
            }
        }
    }

    // returns the order of the model (also known as k)
    public int order() {
        return k;
    }

    // returns a String representation of the model (more info below)
    public String toString() {
        // st2 is the second symbol table (corresponding to the two-argument
        // freq() method)
        StringBuilder result = new StringBuilder();
        for (String key : twoArg) {
            result.append(key + ": ");

            // get the character frequency array
            int[] frequency = twoArg.get(key);

            // for each non-zero entry, append the character and the frequency
            // trailing space is allowed
            for (int i = 0; i < frequency.length; i++) {
                if (frequency[i] != 0) {
                    result.append((char) i + " " + frequency[i] + " ");
                }
            }
            // append a newline character
            result.append("\n");
        }
        return result.toString();
    }


    // returns the # of times 'kgram' appeared in the input text
    public int freq(String kgram) {
        if (kgram.length() != k)
            throw new IllegalArgumentException("kgram is not of length k.");
        // return the value of kgram key
        if (oneArg.contains(kgram)) return oneArg.get(kgram);
        else return 0;
    }

    // returns the # of times 'c' followed 'kgram' in the input text
    public int freq(String kgram, char c) {
        if (kgram.length() != k)
            throw new IllegalArgumentException("kgram is not of length k.");
        // return value in array at a[c] of the kgram key
        if (twoArg.contains(kgram)) {
            int[] values = twoArg.get(kgram);
            return values[c];
        } else return 0;
    }

    // returns a random character, chosen with weight proportional to the
    // number of times each character followed 'kgram' in the input text
    public char random(String kgram) {
        if (kgram.length() != k)
            throw new IllegalArgumentException("kgram is not of length k.");
        if (twoArg.get(kgram) == null)
            throw new IllegalArgumentException("kgram not in text.");

        int[] frequencies = twoArg.get(kgram);
        char randomChar = (char) StdRandom.discrete(frequencies);
        return randomChar;
    }

    // tests all instance methods to make sure they're working as expected
    public static void main(String[] args) {
        String text1 = "banana";
        MarkovModel model1 = new MarkovModel(text1, 2);
        StdOut.println("freq(\"an\", 'a')    = " + model1.freq("an", 'a'));
        StdOut.println("freq(\"na\", 'b')    = " + model1.freq("na", 'b'));
        StdOut.println("freq(\"na\", 'a')    = " + model1.freq("na", 'a'));
        StdOut.println("freq(\"na\")         = " + model1.freq("na"));
        StdOut.println();

        String text3 = "one fish two fish red fish blue fish";
        MarkovModel model3 = new MarkovModel(text3, 4);
        StdOut.println("freq(\"ish \", 'r') = " + model3.freq("ish ", 'r'));
        StdOut.println("freq(\"ish \", 'x') = " + model3.freq("ish ", 'x'));
        StdOut.println("freq(\"ish \")      = " + model3.freq("ish "));
        StdOut.println("freq(\"tuna\")      = " + model3.freq("tuna"));

        for (int i = 0; i < 10; i++) {
            StdOut.println(model1.random("na"));
        }

        StdOut.println(model1.order());

        String text4 = "aaaaaaaaaaaaXaaaaaaaaaaaaXaaaaaaaaaaaaXaaaaaaaaaaaaXc";
        MarkovModel model4 = new MarkovModel(text4, 2);
        StdOut.println(model4);

        String text5 = "gagggagaggcgagaaa";
        MarkovModel model5 = new MarkovModel(text5, 3);
        StdOut.println(model5.random("gag"));
        StdOut.println(model5.random("aga"));

        StdOut.println(model5);


    }
}
