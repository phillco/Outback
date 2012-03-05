package kangaroo

/**
 * Converts ISBNs to and from the 10/13 digit format. Adapted from http://www.polariscomputing.com/isbn_jav.htm.
 */
class IsbnConverter {
    private static String CheckDigits = new String("0123456789X0");

    /**
     * Converts the given 13-digit ISBN to a 10-digit ISBN.
     */
    static String convertTo10Digit(String ISBN) {

        if (!ISBN)
            return "";

        String s9;
        int i, n, v;
        boolean ErrorOccurred;
        ErrorOccurred = false;
        s9 = ISBN.substring(3, 12);
        n = 0;
        for (i = 0; i < 9; i++) {
            if (!ErrorOccurred) {
                v = CharToInt(s9.charAt(i));
                if (v == -1) ErrorOccurred = true;
                else n = n + (10 - i) * v;
            }
        }
        if (ErrorOccurred) return "ERROR";
        else {
            n = 11 - (n % 11);
            return s9 + CheckDigits.substring(n, n + 1);
        }
    }

    /**
     * Converts the given 10-digit ISBN to a 13-digit ISBN.
     */
    static String convertTo13Digit(String ISBN) {

        if (!ISBN)
            return "";

        String s12;
        int i, n, v;
        boolean ErrorOccurred;
        ErrorOccurred = false;
        s12 = "978" + ISBN.substring(0, 9);
        n = 0;
        for (i = 0; i < 12; i++) {
            if (!ErrorOccurred) {
                v = CharToInt(s12.charAt(i));
                if (v == -1) ErrorOccurred = true;
                else {
                    if ((i % 2) == 0) n = n + v;
                    else n = n + 3 * v;
                }
            }
        }
        if (ErrorOccurred) return "ERROR";
        else {
            n = n % 10;
            if (n != 0) n = 10 - n;
            return s12 + CheckDigits.substring(n, n + 1);
        }
    }

    /////////////// Change a character to its integer value ///////////////
    static int CharToInt(char a) {
        switch (a) {
            case '0': return 0;
            case '1': return 1;
            case '2': return 2;
            case '3': return 3;
            case '4': return 4;
            case '5': return 5;
            case '6': return 6;
            case '7': return 7;
            case '8': return 8;
            case '9': return 9;
            default: return -1;
        }
    }
}
