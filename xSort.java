import java.io.*;

/**
 * xSort takes in 3 arguments: number of lines in run, file name with initial
 * runs, and value k indicating how many runs are merged everytime.
 * This solution uses a balanced 2-way merge sort therefore k is aways set to 2.
 * @author: Samuel
 */
public class xSort {
    // main entry into program
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java xSort <m> <file> <k>");
            System.exit(1);
        }

        double m = Double.parseDouble(args[0]);// number of lines in each initial run
        String file = args[1];// name of file with initial runs

        String tape1 = "tape1.txt";
        String tape2 = "tape2.txt";
        String tape3 = "tape3.txt";
        String tape4 = "tape4.txt";

        // create 2 initial tapes to store first set of runs
        createInitialRuns(file, m, tape1, tape2);

        double runLength = m; // keep track of current run length

        int runCounter = 2; // arbitrary value for while loop
        int mergeCounter = 1;

        // continue merging runs until 1 run remaining
        while (runCounter > 1) {
            if (mergeCounter % 2 == 1) {
                runCounter = mergeAllRuns(runLength, tape1, tape2, tape3, tape4);
            } else {
                runCounter = mergeAllRuns(runLength, tape3, tape4, tape1, tape2);
            }
            mergeCounter++;
            runLength *= 2;
        }

        String sortedFile = null;

        // determine which tape is final sorted tape
        if (runCounter == 1) {
            if (mergeCounter % 2 == 1) {
                sortedFile = sortedTape(tape1, tape2);
            } else {
                sortedFile = sortedTape(tape3, tape4);
            }
        }

        // outputs sortedFile to standard output and delete old files
        try {
            BufferedReader reader = new BufferedReader(new FileReader(sortedFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
            deleteFiles(tape1, tape2);
            deleteFiles(tape3, tape4);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * creates initial runs of length m and outputs the odd runs into tape1 and
     * even runs into tape2
     * 
     * @param file  file with sorted m runs to be read in
     * @param m     number of sorted lines in a run
     * @param tape1 output tape for odd runs
     * @param tape2 output tape for even runs
     */
    public static void createInitialRuns(String file, double m, String tape1, String tape2) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file));
                BufferedWriter t1Writer = new BufferedWriter(new FileWriter(tape1));
                BufferedWriter t2Writer = new BufferedWriter(new FileWriter(tape2))) {

            String line; // line from file
            int runCounter = 0; // keeps track of number of runs
            double runSize = 0; // keeps track of number of lines in run

            emptyTape(tape1, tape2); // empty tapes 1 & 2 before adding data inside

            // breaks loop once there is no more data to be read in file
            while ((line = reader.readLine()) != null) {
                // if odd run, add to tape1, else tape 2
                if (runCounter % 2 == 1) {
                    t1Writer.write(line);
                    t1Writer.newLine();
                } else {
                    t2Writer.write(line);
                    t2Writer.newLine();
                }
                runSize++;

                // once runsize reaches m, increment runCounter and reset runsize
                if (runSize == m) {
                    runSize = 0;
                    runCounter++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads lines from t1reader and t2reader, compareing 1st lines of each reader
     * and
     * outputs the smaller line to tapewriter.
     * Continues till every line in a run from both tapes are compared and outputted
     * to
     * tapewriter
     * 
     * @param m          number of lines in a run
     * @param t1Reader   odd tape to be read from
     * @param t2rReader  even tape to be read from
     * @param tapeWriter tape to output compared lines
     */
    private static void mergeSingleRun(double m, BufferedReader t1Reader, BufferedReader t2Reader,
            BufferedWriter tapeWriter) {
        try {
            String t1Line = t1Reader.readLine();
            String t2Line = t2Reader.readLine();
            int t1RunLineCount = 1;
            int t2RunLineCount = 1;
            int runsWithLinesLeft = 0; // indicates which tape(0 is none) has lines left in a run

            for (int i = 1; i < (m * 2); i++) {
                // check if either line is null
                if (t1Line == null) {
                    runsWithLinesLeft = 2; // change runswithlinesleft to 2
                    break;
                } else if (t2Line == null) {
                    runsWithLinesLeft = 1; // change runswithlineleft to 1
                    break;
                }
                // determine which line is smaller and output to tapewriter
                if (t1Line != null && t2Line != null && t1Line.compareTo(t2Line) < 0) {
                    tapeWriter.write(t1Line);

                    // break loop if t1runs reach m or if t1 file is empty
                    if (t1RunLineCount == m || !t1Reader.ready()) {
                        runsWithLinesLeft = 2;
                        tapeWriter.newLine();
                        break;
                        // read another line from t1 and increment t1 line count
                    } else {
                        t1Line = t1Reader.readLine();
                        t1RunLineCount++;
                    }
                    // if t2line is not null, write to file
                } else if (t2Line != null) {
                    tapeWriter.write(t2Line);

                    // break loop if t2 runs reach m or if t2 file is empty
                    if (t2RunLineCount == m || !t2Reader.ready()) {
                        runsWithLinesLeft = 1;
                        tapeWriter.newLine();
                        break;
                        // read another line from t2 and increment t2 line count
                    } else {
                        t2Line = t2Reader.readLine();
                        t2RunLineCount++;
                    }
                }

                tapeWriter.newLine();
            }

            // if tape 1 still has runs with lines left, print all lines in the run to
            // writer
            if (t1Line != null && runsWithLinesLeft == 1) {
                for (int i = t1RunLineCount; i <= m; i++) {
                    tapeWriter.write(t1Line);
                    tapeWriter.newLine();
                    if (i == m || !t1Reader.ready()) {
                        break;
                    } else {
                        t1Line = t1Reader.readLine();
                    }
                }
            }

            // if tape 2 still has runs with lines left, print all lines in the run to
            // writer
            if (t2Line != null && runsWithLinesLeft == 2) {
                for (int i = t2RunLineCount; i <= m; i++) {
                    tapeWriter.write(t2Line);
                    tapeWriter.newLine();
                    if (i == m || !t2Reader.ready()) {
                        break;
                    } else {
                        t2Line = t2Reader.readLine();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * mergeAllRuns method merges run from tapes 1 & 2, into alternating output
     * tapes (3 & 4)
     * and returns the number of runs merged with every call to the method
     * 
     * @param m     number of lines in a run
     * @param tape1 1st tape where runs are read from
     * @param tape2 2nd tape where runs are read from
     * @param tape3 1st output tape file
     * @param tape4 2nd output tape file
     * @return total number of runs merged
     */
    private static int mergeAllRuns(double m, String tape1, String tape2, String tape3, String tape4) {
        int runCounter = 1; // keeps track of num of runs
        try {
            BufferedReader t1Reader = new BufferedReader(new FileReader(tape1));
            BufferedReader t2Reader = new BufferedReader(new FileReader(tape2));
            BufferedWriter t3Writer = new BufferedWriter(new FileWriter(tape3));
            BufferedWriter t4Writer = new BufferedWriter(new FileWriter(tape4));

            emptyTape(tape3, tape4);

            while (true) {
                BufferedWriter tapeWriter = null;
                // write to tape3 if odd, else tape4 if even
                if (runCounter % 2 == 1) {
                    tapeWriter = t3Writer;
                } else {
                    tapeWriter = t4Writer;
                }
                mergeSingleRun(m, t1Reader, t2Reader, tapeWriter);

                // break out of loop if both tapes have reached EOF
                if (!t1Reader.ready() && !t2Reader.ready()) {
                    break;
                } else {
                    runCounter++; // increment run counter
                }
            }
            t1Reader.close();
            t2Reader.close();
            t3Writer.close();
            t4Writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return runCounter;
    }

    // helper methods

    // method checks if files(params tape1 & 2) are empty, if so, deletes content in
    // files
    private static void emptyTape(String tape1, String tape2) {
        try {
            // check if tapes are not empty
            File tape1Check = new File(tape1);
            if (tape1Check.exists() && tape1Check.length() > 0) {
                new FileWriter(tape1, false).close(); // clear content of file
            }
            File tape2Check = new File(tape2);
            if (tape2Check.exists() && tape2Check.length() > 0) {
                new FileWriter(tape2, false).close(); // clear content of file
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // method determines which file (params tape 1 & 2) is the final sorted tape
    private static String sortedTape(String tape1, String tape2) {
        File tapeFile1 = new File(tape1);
        File tapeFile2 = new File(tape2);
        String remainingFile = null; // stores name of file remaining
        if (tapeFile1.exists() && tapeFile1.length() == 0) {
            remainingFile = tape2;
        }
        if (tapeFile2.exists() && tapeFile2.length() == 0) {
            remainingFile = tape1;
        }
        return remainingFile;
    }

    // method deletes file tape1 and tape2
    private static void deleteFiles(String tape1, String tape2) {
        File file1 = new File(tape1);
        File file2 = new File(tape2);
        if (file1.exists() && file2.exists()) {
            file1.delete();
            file2.delete();
        }
    }

}
