import static java.lang.Math.*;
public class lab0
{ 
	public static void main(String args[])
	{
		
		long[] b = new long[16];
		double[] x = new double[18];
		double[][] r = new double[16][18];
		for(int i = 17, j = 0; i>1; i--, j++)
			b[j] = i;
		for(int i = 0; i < 18; i++)
			x[i]=-8.0 + 20*random();
		for(int i=0; i<16; i++)
			{
			for(int j=0; j<18; j++)
				{
				if (b[i] == 7)
					r[i][j] = pow((log(abs(x[j]))/2.0/(3.0/2.0)),(pow(E,(pow(E, x[j])))));
				else if (b[i]==3||b[i]==4||b[i]==6||b[i]==10||b[i]==14||b[i]==15||b[i]==16||b[i]==17)
					r[i][j] = pow((0.5*pow((1.0/3.0/asin((x[j]+2)*E/2.0+1)), 3)), 3);
				else
					r[i][j] = cos(asin(pow(E, (-abs(x[j])))));
				if (j==17)
					System.out.printf("%.3f\n", r[i][j]);
				else if(r[i][j]>=0)
					System.out.printf("%.3f  ", r[i][j]);
				else if(r[i][j]<0)
					System.out.printf("%.3f ", r[i][j]);
				else 
					System.out.printf("%f    ", r[i][j]);
				}
			}
	}
}
			
			
	