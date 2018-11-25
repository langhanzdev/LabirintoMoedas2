package model;

public class Buraco extends Objeto {

	public Buraco(int posicaoX, int posicaoY) {
		super(posicaoX, posicaoY, TipoDeObjeto.BURACO);
	}

	@Override
	public String toString() {
		return "O";
	}
}
