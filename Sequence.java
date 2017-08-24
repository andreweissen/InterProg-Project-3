/**
 * Sequence.java - Handles all program calculations
 * Begun 07/23/17
 * @author Andrew Eissen
 */

package projectthree;

final class Sequence {

    private static int efficiencyCounter = 0;

    /* 
     * As this is a utility class, the standard practice of setting a default constructor private
     * is followed.
     */
    private Sequence() {}

    /**
     * Computes the nth term of sequence iteratively
     * @param n
     * @return result
     */
    public static int computeIterative(int n) {
        // Declaration
        int result, secondPreviousTerm, previousTerm;

        // Initial definition
        result = 0;
        secondPreviousTerm = 0;
        previousTerm = 1;

        for (int i = 0; i <= n; i++) {
            efficiencyCounter++;
            switch (i) {
                case 0:
                    result = 0;
                    break;
                case 1:
                    result = 1;
                    break;
                default:
                    result = (previousTerm * 2) + secondPreviousTerm;
                    secondPreviousTerm = previousTerm;
                    previousTerm = result;
                    break;
            }
        }
        return result;
    }

    /**
     * Computes the nth term of the sequence recursively
     * @param n
     * @return result
     */
    public static int computeRecursive(int n) {
        int result;

        efficiencyCounter++;
        switch (n) {
            case 0:
                result = 0;
                break;
            case 1:
                result = 1;
                break;
            default:
                result = (computeRecursive(n - 1) * 2) + computeRecursive(n - 2);
                break;
        }
        return result;
    }

    /**
     * Getter method for efficiencyCounter variable
     * @return Sequence.efficiencyCounter
     */
    public static int getEfficiency() {
        return Sequence.efficiencyCounter;
    }

    /**
     * Helper method that resets the efficiencyCounter counter on invocation
     * @return void
     */
    public static void resetEfficiency() {
        Sequence.efficiencyCounter = 0;
    }
}