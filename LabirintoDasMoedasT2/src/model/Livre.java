package model;

public class Livre extends Objeto {

	public Livre(int posicaoX, int posicaoY) {
		super(posicaoX, posicaoY, TipoDeObjeto.LIVRE);
	}

	@Override
	public String toString() {
		return "-";
	}
	
}
