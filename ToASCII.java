import java.text.Normalizer;
import java.text.Normalizer.Form;

/******************************************************************************
 *  Description:  Translates a Unicode string to an ASCII version:
 *                1. Replaces each character with a diacritic
 *                   (i.e., a mark or accent above or below a character)
 *                   with the corresponding ASCII character.
 *                2. Replaces each non-ASCII character with a space.
 *****************************************************************************/

public class ToASCII {

    public static String translate(String text) {
        if (text == null) return null;

        // normalize Unicode
        text = Normalizer.normalize(text, Form.NFD);

        // remove diacritic marks
        text = text.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // remove non-ASCII characters
        text = text.replaceAll("[^\\x00-\\x7F]", " ");

        return text;
    }

    // test client
    public static void main(String[] args) {
        String text = "a ā ă ą c ç é ē î ï ĩ í ĝ ġ ń ñ ö š ŝ ś û ů ŷ Á Ç 😀 ł đ ħ œ ⺆";
        System.out.println("BEFORE: " + text);
        System.out.println("AFTER:  " + translate(text));
    }
}
