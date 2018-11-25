package model;

public class Coordenadas {
	
	private int posicaoX;
	private int posicaoY;
	
	public Coordenadas(int posicaoX, int posicaoY) {
		this.posicaoX = posicaoX;
		this.posicaoY = posicaoY;
	}
	
	public int getPosicaoX() {
		return posicaoX;
	}
	
	public void setPosicaoX(int posicaoX) {
		this.posicaoX = posicaoX;
	}
	
	public int getPosicaoY() {
		return posicaoY;
	}
	
	public void setPosicaoY(int posicaoY) {
		this.posicaoY = posicaoY;
	}
	
	@Override
	public String toString() {
		return getPosicaoX() + " - " + getPosicaoY();
	}

}
