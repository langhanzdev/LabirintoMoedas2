package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Perceptron {
	
	private List<Neuronio> neuronios;

	public Perceptron() {
		neuronios = new ArrayList<>();
		inicializaNeuronios();
	}
	
	public List<Neuronio> getNeuronios() {
		return neuronios;
	}
	
	private void inicializaNeuronios() {		
		for (int i = 0; i < 8; i ++) {
			Neuronio neuronio = new Neuronio();
			double leftLimit = -1;
		    double rightLimit = 1;

		    neuronio.setW0(leftLimit + new Random().nextDouble() * (rightLimit - leftLimit));
			neuronio.setW1(leftLimit + new Random().nextDouble() * (rightLimit - leftLimit));
			neuronio.setW2(leftLimit + new Random().nextDouble() * (rightLimit - leftLimit));
			neuronio.setW3(leftLimit + new Random().nextDouble() * (rightLimit - leftLimit));
			
			neuronios.add(neuronio);
		}
	}
}
