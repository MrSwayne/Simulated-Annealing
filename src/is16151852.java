import java.io.*;
import java.util.Scanner;
import java.util.Collections;
import java.util.ArrayList;
import javax.swing.*;

public class is16151852 {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File file = new File("GA2018-19.txt");
		
		int max = getMax(file);
		int[][] arr = new int[max + 1][max + 1];
		readArr(file, arr);
		printArr(arr);
		int[] rand = getRandoms(max);
		printRand(rand);
	}

	public static int getMax(File file) throws FileNotFoundException, IOException {
		FileReader fr = new FileReader(file);
		Scanner in = new Scanner(fr);

		int max = 0;
		while(in.hasNext()) {
			int n = in.nextInt();
			if(n > max)	max = n;
		} 
		fr.close();
		in.close();
		return max;
	}

	public static void readArr(File file, int[][] arr) throws FileNotFoundException, IOException {
		FileReader fr = new FileReader(file);
		Scanner in = new Scanner(fr);

		while(in.hasNext()) {
			String[] pairsString = in.nextLine().split(" ");

			if(pairsString.length == 2) {
			int n1 = Integer.parseInt(pairsString[0]);
			int n2 = Integer.parseInt(pairsString[1]);
			arr[n1][n2] = 1;
			arr[n2][n1] = 1;
			}
		}
		fr.close();
		in.close();
	}

	public static void printArr(int[][] arr) {
		for(int i = 0;i < arr.length;i++) {
			for(int j = 0;j < arr[i].length;j++)
				System.out.print(arr[i][j] + " ");
			System.out.println();
		}
	}

	public static int[] getRandoms(final int n) {
		ArrayList<Integer> numbers = new ArrayList<>();
		for(int i = 0;i < n + 1;i++)
			numbers.add(i);
		Collections.shuffle(numbers);

		int[] rands = new int[numbers.size()]; 
		for(int i = 0;i < numbers.size();i++)
			rands[i] = numbers.get(i);
		return rands;

	}

	public static void printRand(int[] arr) {
		Scanner in = new Scanner(System.in);
		in.nextLine();
		for(int i = 0;i < arr.length;i++)
			System.out.print(arr[i] + " ");
	}
}