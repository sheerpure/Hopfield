package hopfield;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.Point;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

public class hopfield implements ActionListener {
	JFrame frame, trainpic, testpic, finalpic;
	JPanel panel1, panel2, panel3, panel4;
	JButton button1, button2;
	JTextArea output, _test;
	JTextArea[] area = { new JTextArea("null"), new JTextArea("null"),
			new JTextArea(" learncycle : ") };
	private String Length = "0", Width = "0";
	JTextArea[] LW = { new JTextArea("Length : " + Length),
			new JTextArea("Width : " + Width) };
	private JMenu[] items = { new JMenu("基本題"), new JMenu("HOPFIELD資料集") };
	private JMenuItem[] trainsamplebasic = { new JMenuItem("A"),
			new JMenuItem("C"), new JMenuItem("L"), new JMenuItem("E"),
			new JMenuItem("U"), new JMenuItem("S") };
	private JMenuItem[] trainsamplehop = { new JMenuItem("1"),
			new JMenuItem("2"), new JMenuItem("3"), new JMenuItem("4"),
			new JMenuItem("5"), new JMenuItem("6"), new JMenuItem("7"),
			new JMenuItem("8"), new JMenuItem("9"), new JMenuItem("10"),
			new JMenuItem("11"), new JMenuItem("12"), new JMenuItem("13"),
			new JMenuItem("14"), new JMenuItem("15") };
	private JMenuItem basic = new JMenuItem("set_basic");
	private JMenuItem hop = new JMenuItem("set_HOPFIELD");
	public int[][] trainDigital, testDigital;
	public int trainNum = 0, testNum = 0, trainepoch = 0, l, w;
	JTextField[] enter = { new JTextField("5000") };
	Train myHopfield;
	private boolean ans = true;
	private boolean draw = false;
	public int[][] dots;
	public String[][] line;
	public Graphics g;
	private boolean learnform = false, testform = false, finalform = false;
	JScrollPane js;

	public static void main(String[] args) {
		new hopfield();
	}

	private Graphics getGraphics() {
		// TODO Auto-generated method stub
		return null;
	}

	public hopfield() {
		frame = new JFrame();
		frame.setTitle("Hopfield");
		frame.setSize(320, 320);
		frame.setLayout(new GridLayout(6, 1));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel1.setSize(80, 100);
		button1 = new JButton("learn");
		button1.addActionListener(this);
		panel1.add(button1);
		area[0].setFont(new Font("標楷體", Font.PLAIN, 18));
		area[0].setBackground(new Color(238, 238, 238));
		panel1.add(area[0]);

		panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel2.setSize(80, 100);
		button2 = new JButton("test");
		button2.setEnabled(false);
		button2.addActionListener(this);
		panel2.add(button2);
		area[1].setFont(new Font("標楷體", Font.PLAIN, 18));
		area[1].setBackground(new Color(238, 238, 238));
		panel2.add(area[1]);
		enter[0].setFont(new Font("", Font.PLAIN, 20));
		panel2.add(enter[0]);

		panel4 = new JPanel(new GridLayout(1, 2));
		area[2].setLayout(new FlowLayout(FlowLayout.CENTER));
		area[2].setBackground(new Color(238, 238, 238));
		area[2].setFont(new Font("", Font.PLAIN, 20));
		panel4.add(area[2]);
		panel4.add(enter[0]);

		panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		LW[0].setFont(new Font("", Font.PLAIN, 20));
		LW[1].setFont(new Font("", Font.PLAIN, 20));
		LW[0].setBackground(new Color(238, 238, 238));
		LW[1].setBackground(new Color(238, 238, 238));
		panel3.add(LW[0]);
		panel3.add(LW[1]);

		frame.add(panel1);
		frame.add(panel2);
		frame.add(panel4);
		frame.add(panel3);

		JMenu menu = new JMenu("題目");
		for (int i = 0; i < items.length; i++) {
			menu.add(items[i]);
		}

		items[0].add(basic);
		basic.addActionListener(this);
		menu.add(items[0]);

		items[1].add(hop);
		hop.addActionListener(this);
		menu.add(items[1]);

		menu.setPopupMenuVisible(true);

		JMenuBar menubar = new JMenuBar();
		menubar.add(menu);

		frame.setJMenuBar(menubar);
		frame.setVisible(true);
		trainpic = new JFrame();
		trainpic.setSize(300, 600);
		trainpic.setTitle("training_data");
		trainpic.setLocation(0, 0);

		testpic = new JFrame();
		testpic.setSize(300, 600);
		testpic.setTitle("testing_data");
		testpic.setLocation(300, 0);

		finalpic = new JFrame();
		finalpic.setSize(300, 600);
		finalpic.setTitle("final_data");
		finalpic.setLocation(600, 0);

	}

	public void getTraining(String train, String Length, String Width,
			int trainNum) throws NumberFormatException, IOException {
		InputStream in = hopfield.class.getResourceAsStream(train);
		InputStreamReader ir = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(ir);
		String str;
		int l = Integer.valueOf(Length);
		int w = Integer.valueOf(Width);
		trainDigital = new int[trainNum][(l * w)];

		int countline = 0, countnum = 0, countpicture = 0;

		while ((str = br.readLine()) != null && countpicture < trainNum) {
			if (!str.equals("")) {
				if (countline == w - 1) {
					char[] arr = str.toCharArray();
					for (int i = 0; i < arr.length; i++) {
						if (arr[i] == ' ') {
							trainDigital[countpicture][countnum] = -1;
						} else {
							trainDigital[countpicture][countnum] = 1;
						}
						countnum++;
					}
					countline = 0;
					countnum = 0;
					countpicture++;
				} else {
					char[] arr = str.toCharArray();
					for (int i = 0; i < arr.length; i++) {
						if (arr[i] == ' ') {
							trainDigital[countpicture][countnum] = -1;
						} else {
							trainDigital[countpicture][countnum] = 1;
						}
						countnum++;
					}
					countline++;
				}
			}
		}
	}

	public void getTesting(String test, String Length, String Width, int testNum)
			throws IOException {
		InputStream in = hopfield.class.getResourceAsStream(test);
		InputStreamReader ir = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(ir);
		String str;
		int l = Integer.valueOf(Length);
		int w = Integer.valueOf(Width);
		testDigital = new int[testNum][(l * w)];

		int countline = 0, countnum = 0, countpicture = 0;

		while ((str = br.readLine()) != null && countpicture < trainNum) {
			if (!str.equals("")) {

				if (countline == w - 1) {

					char[] arr = str.toCharArray();
					for (int i = 0; i < arr.length; i++) {
						if (arr[i] == ' ') {
							testDigital[countpicture][countnum] = -1;
						} else {
							testDigital[countpicture][countnum] = 1;
						}
						countnum++;
					}
					countline = 0;
					countnum = 0;
					countpicture++;

				} else {

					char[] arr = str.toCharArray();
					for (int i = 0; i < arr.length; i++) {
						if (arr[i] == ' ') {
							testDigital[countpicture][countnum] = -1;
						} else {
							testDigital[countpicture][countnum] = 1;
						}
						countnum++;
					}
					countline++;
				}
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String problem = e.getActionCommand();

		l = Integer.valueOf(Length);
		w = Integer.valueOf(Width);


		switch (problem) {
		case "set_basic":
			Length = "10";
			Width = "12";

			LW[0].setText("Length : " + Length);
			LW[1].setText("Width : " + Width);
			trainNum = 6;
			testNum = 3;
			ans = true;

			area[0].setText("基本題Training");
			area[1].setText("基本題Testing");
			try {
				getTraining("基本題Training.txt", Length, Width, trainNum);
				getTesting("基本題Testing.txt", Length, Width, testNum);
			} catch (NumberFormatException | IOException e1) {
				// TODO A uto-generated catch block
				e1.printStackTrace();
			}

			break;
		case "set_HOPFIELD":
			Length = "10";
			Width = "10";

			LW[0].setText("Length : " + Length);
			LW[1].setText("Width : " + Width);
			trainNum = 15;
			testNum = 15;
			ans = false;

			area[0].setText("HOPFIELD_Training資料集");
			area[1].setText("HOPFIELD_Testing資料集");
			try {
				getTraining("HOPFIELD_Training資料集.txt", Length, Width, trainNum);
				getTesting("HOPFIELD_Testing資料集.txt", Length, Width, testNum);
			} catch (NumberFormatException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		}

		String ln;
		ln = enter[0].getText();
		int trainepoch = Integer.valueOf(ln);

		System.out.println();

		if (e.getSource() == button1) {
			myHopfield = new Train(l, w, trainDigital);
			learnform = true;

			output = new JTextArea();
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(new BorderLayout(0, 0));

			String l = "";
			for (int i = 0; i < trainNum; i++) {
				testform = true;
				setDots(trainDigital[i], i, trainNum);

				for (int j = 0; j < w; j++) {
					l = l + line[i][j] + "\n";
					System.out.println(line[i][j]);
				}
				l = l + "\n";
			}
			output.setText(l);
			testform = false;
			js = new JScrollPane();
			contentPane.add(js, BorderLayout.CENTER);
			js.setViewportView(output);
			trainpic.setContentPane(contentPane);

			trainpic.setVisible(true);
			_test = new JTextArea();
			JPanel contentPane2 = new JPanel();
			contentPane2.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane2.setLayout(new BorderLayout(0, 0));

			String l2 = "";
			for (int i = 0; i < testNum; i++) {
				testform = true;
				setDots(testDigital[i], i, testNum);

				for (int j = 0; j < w; j++) {
					l2 = l2 + line[i][j] + "\n";
					System.out.println(line[i][j]);
				}
				l2 = l2 + "\n";
			}
			_test.setText(l2);
			testform = true;
			js = new JScrollPane();
			contentPane2.add(js, BorderLayout.CENTER);
			js.setViewportView(_test);
			testpic.setContentPane(contentPane2);
			testpic.setVisible(true);

			button2.setEnabled(true);
		}

		if (e.getSource() == button2) {
			for (int i = 0; i < testNum; i++) {
				testform = true;
				setDots(testDigital[i], i, testNum);
			}
			testform = false;
			Test test = new Test(l, w, testDigital, myHopfield.weight,
					trainepoch);

			output = new JTextArea();
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(new BorderLayout(0, 0));

			String[] 正確率 = new String[test.new_test.length];
			for (int i = 0; i < test.new_test.length; i++)
				正確率[i] = test.right[i]+"/"+ test.new_test[i].length;

			String l = "";
			for (int i = 0; i < testNum; i++) {
				testform = true;
				setDots(test.new_test[i], i, testNum);

				for (int j = 0; j < w; j++) {
					l = l + line[i][j] + "\n";
					System.out.println(line[i][j]);
				}
				l = l + "\n";
			}
			
			testform = false;

			String q = "";
			for (int i = 0; i < test.new_test.length; i++)
				q = q +(i+1)+" 回想正確率 : "+ 正確率[i]+"\n";
			q=l+"\n"+q;
			output.setText(q);
			js = new JScrollPane();
			contentPane.add(js, BorderLayout.CENTER);
			js.setViewportView(output);
			finalpic.setContentPane(contentPane);

			finalpic.setVisible(true);
		}
	}

	public void setDots(int[] trainDigital, int num, int MAX) {
		int a = 0, b = 0;
		dots = new int[w][l];
		for (int i = 0; i < l * w; i++) {
			if (b == l) {
				a++;
				b = 0;
			}
			dots[a][b] = trainDigital[i];
			if (a < w && b < l) {
				b++;
			}
		}
		draw = true;

		line = new String[MAX][w];

		for (int i = 0; i < w; i++) {
			line[num][i] = "";
			for (int j = 0; j < l; j++) {
				if (dots[i][j] == 1)
					line[num][i] = line[num][i] + dots[i][j] + " ";
				else
					line[num][i] = line[num][i] + "0 ";
			}
		}

	}
}