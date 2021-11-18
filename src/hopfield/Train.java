package hopfield;

public class Train {
	public float[][] weight,I;

	public Train(int l,int w, int[][] train) {
		weight = new float[train[0].length][train[0].length];
		I = new float[train[0].length][train[0].length];
		for(int i=0;i<train[0].length;i++){	
			for(int j=0;j<train[0].length;j++){
				weight[i][j]=0;
			}
		}
		
		for(int i=0;i<train.length;i++){
			for(int j=0;j<train[0].length;j++){
				for(int k=0;k<train[0].length;k++){
					weight[j][k]=weight[j][k]+train[i][j]*train[i][k];
				}
			}
		}
		
		for(int j=0;j<train[0].length;j++){
			for(int k=0;k<train[0].length;k++){
				weight[j][k]=weight[j][k]/train[0].length;
				if(j==k){
					I[j][k] = 1*train.length/train[0].length;
				}else{
					I[j][k] = 0;
				}
			}
		}
		
		for(int j=0;j<train[0].length;j++){
			for(int k=0;k<train[0].length;k++){
				weight[j][k] = weight[j][k] - I[j][k];
			}
		}
		
		
	}
}
