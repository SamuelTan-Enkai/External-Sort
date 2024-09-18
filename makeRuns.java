import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * makeRuns takes in 2 arguments: number of lines to be sorted in a run and the file to be sorted. 
 * It then sorts the lines in every run using quicksort and outputs the result to the terminal.
 * Quicksort was chosen as it has a space complexity of O(logN) which is inline with the assignemnt
 * where space/memory is limited.
 */
public class makeRuns {
    public static void main(String[] args){
        //check if number of arguments is correct
        if (args.length != 2){  
            System.err.println("Usage: java makeRuns <number> <file>");
            System.exit(1);
        }

        //check if 1st argument is a valid integer
        try{
            int m = Integer.parseInt(args[0]); 
        }catch(NumberFormatException e){
            System.err.println("1st argument must be a valid integer!");
            System.exit(1);
        }

        int m = Integer.parseInt(args[0]);

        //check if given file exists
        String file = args[1]; 
        File actualFile = new File(file);
        if(!actualFile.exists()){
            System.err.println("File: " + file + " not found!");
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            ArrayList<String> run = new ArrayList<String>();
            String line;

            //parse lines from file into run arrayList
            //breaks loop if file is empty
            while((line = reader.readLine()) != null){
                run.add(line);

                //check if arraylist length is m
                if(run.size() == m){
                    //sort and print run
                    sortAndPrintRun(run);
                    //empty run list so it can be filled again
                    run.clear();
                }

            }
            //sort remaining lines in run
            if(!run.isEmpty()){
                sortAndPrintRun(run);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //sort run using quickSort and print them to output
    private static void sortAndPrintRun(ArrayList<String> run){
        quickSort(run, 0, run.size() - 1);
        for(String line : run){
            System.out.println(line);
        }
    }

    /**
     * method that implements quicksort
     * @param run arraylist to be sorted
     * @param start starting index
     * @param end ending index
     */
    private static void quickSort(ArrayList<String> run, int start, int end){
        if (start < end){
            //partition the run and get pivot index
            int partitionIndex = partition(run, start, end);
            //recursively sort elements before and after partition
            quickSort(run, start, partitionIndex -1);
            quickSort(run, partitionIndex + 1, end);
        }
    }

    /**
     * method takes the last line in run as pivot, it then puts the pivot line into its correct position
     * and places all smaller lines to the left of pivot and greater lines to the right of pivot
     * @param run arraylist of runs to be sorted
     * @param start starting index of line to be sorted
     * @param end ending index of line to be sorted
     * @return returns pivot index
     */
    private static int partition(ArrayList<String> run, int start, int end){
        //assign pivot to high
        String pivot = run.get(end);
        //indicates the right position of pivot 
        int i = start - 1;
        for(int j = start; j <= end -1; j++){
            //if current line is smaller than pivot's line
            if(run.get(j).compareTo(pivot) < 0){
                i++;
                swap(run, i, j);
            }
        }
        swap(run, i + 1, end);
        return i + 1;
    }

    //swap function swaps elements in i and j
    private static void swap(ArrayList<String> run, int i, int j){
        String temp = run.get(i);
        run.set(i, run.get(j));
        run.set(j, temp);
    }
}
