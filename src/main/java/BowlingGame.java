import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BowlingGame {


    private static int getBowlingScore(String bowlingCode) {

        int[] firstScore = new int[10];
        int[] secondScore = new int[10];
        String[] flag = new String[20];

        String[] result = getSplitFrame(bowlingCode);
        getFrameMark(result, firstScore, secondScore, flag);
        int[] oneGradeScore = calculationEachFrame(firstScore, secondScore, flag);

        return calculationFinalScore(result, flag, oneGradeScore, firstScore, secondScore);
    }

    private static String[] getSplitFrame(String inputString) {

        String[] result = new String[2];
        String lastScore = " ";
        String[] splitFrame = inputString.split("\\|\\|");
        if (inputString.length() - splitFrame[0].length() == 2) {
            lastScore = null;
        } else {

            lastScore = splitFrame[1];
        }
        result[0] = splitFrame[0];
        result[1] = lastScore;

        return result;
    }

    private static void getFrameMark(String[] inputArray, int[] first, int[] second, String[] flag) {
        String[] splitHeader = inputArray[0].split("\\|");
        for (int i = 0; i < splitHeader.length; i++) {
            if (splitHeader[i].length() == 1) {
                first[i] = 10;
                second[i] = 0;
                flag[i] = "strike";
            } else {
                String[] oneGrade = splitHeader[i].split("");
                if (isInteger(oneGrade[0])) {
                    first[i] = Integer.valueOf(oneGrade[0]);
                    if (isInteger(oneGrade[1])) {
                        second[i] = Integer.valueOf(oneGrade[1]);
                        flag[i] = "none";
                    } else if (oneGrade[1].equals("-")) {
                        second[i] = 0;
                        flag[i] = "none";
                    } else if (oneGrade[1].equals("/")) {
                        second[i] = 10 - first[i];
                        flag[i] = "spare";
                    }
                } else {

                    first[i] = 0;
                    if (isInteger(oneGrade[1])) {
                        second[i] = Integer.valueOf(oneGrade[1]);
                        flag[i] = "neither";
                    } else if (oneGrade[i].equals("/")) {
                        second[i] = 10;
                        flag[i] = "spare";
                    } else {
                        second[i] = 0;
                        flag[i] = "none";
                    }
                }


            }
        }
    }

    private static int[] calculationEachFrame(int[] first, int[] second, String[] flag) {

        int[] result = new int[10];
        for (int j = 0; j <= 7; j++) {
            if (flag[j].equals("strike")) {
                result[j] = first[j] + second[j] + first[j + 1] + second[j + 1];
                if (flag[j + 1].equals("strike")) {
                    result[j] += first[j + 2] + second[j + 2];
                }
            } else if (flag[j].equals("spare")) {
                result[j] = first[j] + second[j] + first[j + 1];
            } else {
                result[j] = first[j] + second[j];
            }
        }
        return result;

    }

    private static int calculationFinalScore(String[] result, String[] flag, int[] oneGradeScore, int[] first, int[] second) {
        int sumScore = 0;
        int lastOne = 0, lastTwo = 0;
        String[] splitExtra;
        if (result[1] != null && result[1].length() > 1) {
            splitExtra = result[1].split("");
            if (splitExtra[0].equals("X")) {
                lastOne = 10;
                if (splitExtra[1].equals("X")) {
                    lastTwo = 10;
                }
                if (isInteger(splitExtra[1])) {
                    lastOne += Integer.valueOf(splitExtra[0]);
                }
            }
            if (isInteger(splitExtra[0])) {
                lastOne += Integer.valueOf(splitExtra[0]);
            }
            if (isInteger(splitExtra[1])) {
                lastTwo += Integer.valueOf(splitExtra[1]);
            }

        } else if (result[1] != null && result[1].length() == 1) {

            if (isInteger(result[1])) {
                lastOne += Integer.valueOf(result[1]);
            }
        }


        if (flag[8].equals("strike")) {
            oneGradeScore[8] = first[8] + second[8] + first[9] + second[9];
            if (flag[9].equals("strike")) {
                oneGradeScore[8] += lastOne;
            }
        } else if (flag[8].equals("spare")) {
            oneGradeScore[8] = first[8] + second[8] + first[9];
        } else {
            oneGradeScore[8] = first[8] + second[8];
        }


        if (flag[9].equals("strike")) {
            oneGradeScore[9] = first[9] + second[9] + lastOne + lastTwo;
        } else if (flag[9].equals("spare")) {
            oneGradeScore[9] = first[9] + second[9] + lastOne;
        } else {
            oneGradeScore[9] = first[9] + second[9];
        }

        for (int k = 0; k <= 9; k++) {
            sumScore += oneGradeScore[k];
        }
        return sumScore;

    }

    private static boolean isInteger(String input) {
        Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
        return mer.find();
    }

    public static void main(String[] args) {
        String input1 = "X|X|X|X|X|X|X|X|X|X||XX";
        String input2 = "5/|5/|5/|5/|5/|5/|5/|5/|5/|5/||5";
        String input3 = "9-|9-|9-|9-|9-|9-|9-|9-|9-|9-||";
        String input4 = "X|7/|9-|X|-8|8/|-6|X|X|X||81";

        getBowlingScore(input1);
        getBowlingScore(input2);
        getBowlingScore(input3);
        getBowlingScore(input4);
    }
}
