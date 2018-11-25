package model;

public abstract class Objeto {

	protected Coordenadas coordenadas;
	protected TipoDeObjeto tipo;

	public Objeto(int posicaoX, int posicaoY, TipoDeObjeto tipo) {
		this.coordenadas = new Coordenadas(posicaoX, posicaoY);
		this.tipo = tipo;
	}

	public int getPosicaoX() {
		return this.coordenadas.getPosicaoX();
	}

	public void setPosicaoX(int posicaoX) {
		this.coordenadas.setPosicaoX(posicaoX);
	}

	public int getPosicaoY() {
		return this.coordenadas.getPosicaoY();
	}

	public void setPosicaoY(int posicaoY) {
		this.coordenadas.setPosicaoY(posicaoY);
	}

	public Coordenadas getCoordenadas() {
		return this.coordenadas;
	}

	public void setCoordenadas(Coordenadas coordenadas) {
		this.coordenadas = coordenadas;
	}

	public TipoDeObjeto getTipo() {
		return tipo;
	}

	public void setTipo(TipoDeObjeto tipo) {
		this.tipo = tipo;
	}
	
}
