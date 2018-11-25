package model;

public class Muro extends Objeto{

	public Muro(int posicaoX, int posicaoY) {
		super(posicaoX, posicaoY, TipoDeObjeto.MURO);
	}

	@Override
	public String toString() {
		return "M";
	}
}
