package hopfield;

public class Test {
	public int[][] new_test;
	public int[] right;

	public Test(int l, int w, int[][] test, float[][] weight, int Ncycle) {
		new_test = new int[test.length][test[0].length];
		right = new int[test.length];
		for (int i = 0; i < test.length; i++) {
			for (int Icycle = 0; Icycle < Ncycle; Icycle++) {
				for (int j = 0; j < test[0].length; j++) {
					float sum = 0;
					for (int k = 0; k < test[0].length; k++) {
						sum = sum + test[i][k] * weight[k][j];
					}
					if (sum < 0) {
						new_test[i][j] = -1;
					} else if (sum == 0) {
						new_test[i][j] = test[i][j];
					} else if (sum > 0) {
						new_test[i][j] = +1;
					}
				}
				int check_recall_ok = 1;
				for (int j = 0; j < test[0].length; j++) {
					if (test[i][j] != new_test[i][j])
						check_recall_ok = 0;
				}
				if (check_recall_ok == 1)
					break;

			}
		}
		for (int i = 0; i < test.length; i++) {
			for (int j = 0; j < test[0].length; j++) {
				if (test[i][j] == new_test[i][j])
					right[i]++;
				System.out.println(right[i]);
			}
		}
	}
}
