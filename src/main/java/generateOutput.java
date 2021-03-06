/**
 * Created by Alec Wolyniec on 2/16/16.
 */
import java.io.*;

public class generateOutput {
    /*
       Returns an input string printed up to <length> characters long. if <length> is greater than
       the length of the input string, adds <input length> - <length> blank spaces to the output string.
     */
    public static String fillIndent(String input, int length) {
        String output = "";
        for (int i = 0; i < length; i++) {
            if (i >= input.length()) output += " ";
            else output += input.charAt(i);
        }
        return output;
    }

    /*
        Given a set of n attribute-value pairs and a path to an output file, print all pairs and their transaction ids
        in the following format:
        "pair1:obj1;transID1
        pair2:obj2;transID2
        .
        .
        .
        pairn:objn;transIDn "
     */
    public static void printPairFieldsToFile(String path, AttrValPair[] pairs, boolean append) throws IOException {
        FileWriter writah = new FileWriter(path, append);
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i] != null) {
                AttrValPair pair = pairs[i];
                writah.write(pair.getObj() + ":" + pair.getFeat() + ";" + Integer.toString(pair.getTransactionID()) + "\n");
            }
        }
        writah.close();
    }

    //Rounds a double to x significant figures and leading 0's, where x is at least 2
    static double roundDoubleToXSigFigs(double input, int x) {
        //don't count sig figs by the number of digits; leading 0's are not sig figs
        if (x > Double.toString(input).length() - 2) {
            x = Double.toString(input).length() - 2;
        }
        input = Double.parseDouble(Double.toString(input).substring(0, x+2));

        int mul = 1;
        for (int i = 0; i < x-1; i++) {
            mul *= 10;
        }

        long inputRoundX = Math.round(input * mul);
        return ((double)inputRoundX)/mul;
    }

    /*
      Generates a formatted string version of a double, rounded to x - i significant figures, where i is 1 less than the
      number of digits in the scientific notation exponent of the double, if the double is represented in scientific
      notation
     */
    public static String genDoubleString (double doubloon, int sigFigs) {
        String expon = Double.toString(doubloon);
        int lengthMinusExponents = expon.length();
        boolean eflag = false;
        int digitCut = 0;
        for (int i = 0; i < expon.length(); i++) {
            if (expon.charAt(i) == 'E') {
                eflag = true;
                digitCut += expon.length() - i - 3;
                expon = expon.substring(i, expon.length());
                break;
            }
        }
        //ensure there are at least 2 significant figures
        if (sigFigs - digitCut < 2) { digitCut = sigFigs - 2; }
        if (eflag) { lengthMinusExponents -= expon.length(); }
        //no rounding if the string has fewer than "sigFigs" sig-figs
        if (lengthMinusExponents <= sigFigs) { return Double.toString(doubloon); }

        String doubleString = Double.toString(roundDoubleToXSigFigs(doubloon, sigFigs - digitCut)); //rounding errors
        if (eflag) doubleString += expon;
        return doubleString;
    }
}