package model;

public class Neuronio {

	// pesos
	private double w0; 
	private double w1;
	private double w2;
	private double w3;

	public double getW0() {
		return w0;
	}

	public double getW1() {
		return w1;
	}

	public double getW2() {
		return w2;
	}
	
	public double getW3() {
		return w3;
	}

	public double calculaV(int x1, int x2, int x3) {
		return w0 + w1 * x1 + w2 * x2 + w3 * x3;
	} 
	
	// calcula o campo local induzido
	public int calculaY(int x1, int x2, int x3) {
		double v = calculaV(x1, x2, x3);

		if (v >= 0) {
			return 1;
		}
		return 0;
	}

	public void setW0(double w0) {
		this.w0 = w0;
	}

	public void setW1(double w1) {
		this.w1 = w1;
	}

	public void setW2(double w2) {
		this.w2 = w2;
	}
	
	public void setW3(double w3) {
		this.w3 = w3;
	}

	public String toString() {
		return "w0 = " + w0 + " w1= " + w1 + " w2= " + w2 + " w3= " + w3;
	}
}
