package model;

public class EstrategiaMuro {
	
	private int posicaoX;
	private int posicaoY;
	private Orientacao orientacao;
	
	public EstrategiaMuro(int posicaoX, int posicaoY, Orientacao orientacao) {
		this.posicaoX = posicaoX;
		this.posicaoY = posicaoY;
		this.orientacao = orientacao;
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

	public Orientacao getOrientacao() {
		return orientacao;
	}

	public void setOrientacao(Orientacao orientacao) {
		this.orientacao = orientacao;
	}
	
}
