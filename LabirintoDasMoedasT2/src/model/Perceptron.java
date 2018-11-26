package model;

import java.util.ArrayList;
import java.util.List;

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
			neuronios.add(neuronio);
		}
	}
}
