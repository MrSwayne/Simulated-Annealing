import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.math.*;

public class is16151852 {

	private static double temperature = 20d;
	private static final double COOL = 5;
	private static double cr = 0.5d;
	private static double fitness = 10d;

	private static JTextField tempField;
	private static JTextField coolingField;
	private static JButton okBtn;

	private static ArrayList<Double> evo = new ArrayList<Double>();
	private static ArrayList<Point> points;
	private static double chunk;

	private static JPanel container = new JPanel();
	private static JPanel visualPanel = new JPanel();
	private static final File file = new File("GA2018-19.txt");

	public static void main(String[] args) throws FileNotFoundException, IOException {
		getInitial();
	}

		public static void getInitial() throws FileNotFoundException, IOException {
		JFrame frame = new JFrame();
		frame.setTitle("Simulated Annealing");

		JPanel temperaturePanel = new JPanel();
		JLabel tempL = new JLabel("Temperature: ");
		tempField = new JTextField("", 10);
		temperaturePanel.add(tempL);
		temperaturePanel.add(tempField);

		JPanel coolingPanel = new JPanel();
		JLabel coolL = new JLabel("Cooling Rate: ");
		coolingField = new JTextField("", 10);
		coolingPanel.add(coolL);
		coolingPanel.add(coolingField);

		okBtn = new JButton("Ok");


		okBtn.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					temperature = Double.parseDouble(tempField.getText());
					cr = Double.parseDouble(coolingField.getText());

					int max = getMax(file);
					int[] ordering = getOrdering(max);


					int[][] arr = new int[max + 1][max + 1];
					readArr(file, arr);
					printArr(arr);

					fitness = calculateFitness(arr, calculateChunk(ordering), ordering);
					//visualise(arr, ordering, calculateChunk(ordering));
					while(temperature > 0) {
						int[] tempOrdering = new int[ordering.length];

						for(int i = 0;i < ordering.length;i++)
							tempOrdering[i] = ordering[i];
						double tempFitness;

						for(int i = 0;i < temperature;i++) {
							mutate(tempOrdering);
						}

						chunk = calculateChunk(tempOrdering);
						tempFitness = calculateFitness(arr, chunk, tempOrdering);

						if(tempFitness < fitness) {
							fitness = tempFitness;
							for(int i = 0;i < tempOrdering.length;i++)
								ordering[i] = tempOrdering[i];
						}

						temperature -= cr;
						evo.add(fitness);
					}
					print(evo);
					visualise(arr, ordering, chunk);
					

				} catch(NumberFormatException ex) {
					System.err.println("Numbers only pls");
				} catch(FileNotFoundException ex) {
					System.err.println("Error finding file");
				} catch (IOException ex) {
					System.err.println("Error io exception");
				}
			}
		});

		container.add(temperaturePanel);
		container.add(coolingPanel);
		container.add(okBtn);
		frame.add(container);
		frame.pack();
		frame.setVisible(true);
	}

	public static void print(ArrayList<Double> evo) {
		try {
			File file = new File("out.txt");
			PrintWriter apw = new PrintWriter(file);
			for(int i = 0;i < evo.size();i++) {
				System.out.println(evo.get(i));
				apw.println(evo.get(i));
			}
			apw.close();
		} catch(IOException e) {
			System.err.println("Couldn't create output file");
		}
	}

	public static void visualise(int[][] arr, int[] ordering, double chunk) {


		for(int i = 0;i < ordering.length;i++) {
			//System.out.print(ordering[i] + " ");
		}

		new Visualisation(arr, ordering, chunk);

	}





	public static void mutate(int[] ordering) {
		int x1 = (int)(Math.random() * ordering.length);
		int x2 = (int)(Math.random() * ordering.length);

		int temp;
		temp = ordering[x1];
		ordering[x1] = ordering[x2];
		ordering[x2] = temp;
	}

	public static double calculateFitness(int[][] arr, double chunk, int[] ordering) {
		calculateXY(chunk, ordering);
		double sum = 0;
		for(int i = 0;i < arr.length;i++) {
			for(int j = 0;j < arr[i].length / 2;j++) {
				if(arr[i][j] == 1) {
					int ind1 = getPosOf(ordering, i);
					int ind2 = getPosOf(ordering, j);

					sum += getDistance(points.get(ind1), points.get(ind2));
				}
			}
		}
		return sum;
	}

	public static double getDistance(Point p1, Point p2) {
		double a = p1.x * p1.x;
		double b = p2.x * p2.x;
		return Math.sqrt(a + b);
	}

	public static int getPosOf(int[] ordering,int x) {
		for(int i = 0;i < ordering.length;i++) {
			if(ordering[i] == x) return i;
		}
		return -1;
	}

	public static void calculateXY(double chunk, int[] ordering) {
		points = new ArrayList<Point>();
		for(int i = 0;i < ordering.length;i++) {
			double x = Math.cos(i * chunk);
			double y = Math.sin(i * chunk);
			points.add(new Point(x, y));
		}
	}

	public static double calculateChunk(int[] ordering) {
		return (2 * Math.PI) / Math.abs(ordering.length);
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
			for(int j = 0;j < arr[i].length;j++) {
				//System.out.print(arr[i][j] + " ");
			}
			//System.out.println();
		}
	}

	public static int[] getOrdering(final int n) {
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

class Point {

	public double x;
	public double y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
}

class Visualisation extends JFrame {

	private int[][] adj;
	private int[] current_ordering;
	private int v = 0;
	private double chunk;


	public Visualisation(int[][] adj, int[] current_ordering, double chunk) {
		this.adj = adj;
		this.current_ordering = current_ordering;
		this.chunk = chunk;

		this.v = current_ordering.length;
		this.setSize(500,500);
		this.setTitle("Visualisation");
		setVisible(true);
	}

	public void paint(Graphics g) {
		super.paint(g);
		int radius = 100;
		int mov = 200;
		double w = v;

		for(int i = 0;i < v;i++) {
			for(int j = i + 1;j < v;j++) {
				if(adj[current_ordering[i]][current_ordering[j]]== 1) {
					g.drawLine(
						(int)(((double) Math.cos(i*chunk)) * radius + mov),
						(int)(((double) Math.sin(i*chunk)) * radius + mov),
						(int)(((double) Math.cos(j*chunk)) * radius + mov),
						(int)(((double) Math.sin(j*chunk)) * radius + mov));
				}
			}
		}
	}
}