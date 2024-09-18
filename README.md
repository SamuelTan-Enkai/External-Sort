# External-Sort
#### makeRuns.java 
This program compares lines in a file lexicographically and sorts them using the quicksort algorithm with an 0(nlogn) time complexity.

The program takes in 2 arguments:
1. number: the number of lines to be sorted in a run
2. file: the file to be sorted

Example Usage: java makeRuns \<number> \<file> 

Example Output would be a file with every given number of lines sorted in lexicographical order.

#### xSort.java
This program implements a balanced 2-way merge sort algorithm by using 4 'tapes' to store the runs and merge them. The initial 2 runs are created by reading the sorted lines from the input file and writing them to 2 tapes, tape1 & tape2. The program then repeatedly merges the runs from these tapes into 2 output tapes, tape3 & tape4, until only 1 run remains. The final sorted run is then outputted to standard output and the temp files are deleted. 

Parameters:
1. The number of lines in each initial run
2. The name of the file with the initial runs
3. The value k (number of tapes), which is always set to 2 in 2-way merge sort.

Example Usage: java xSort 100 input.txt 2

#### Note
The 2 programs can be chained together with:

`java makeRuns 100 input.txt | java xSort 100 2`
 


