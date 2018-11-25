package model;

public class Porta extends Objeto {
	
	private boolean aberta;

	public Porta(int posicaoX, int posicaoY) {
		super(posicaoX, posicaoY, TipoDeObjeto.PORTA);
		this.aberta = false;
	}

	public boolean isAberta() {
		return aberta;
	}

	public void setAberta(boolean aberta) {
		this.aberta = aberta;
	}

	@Override
	public String toString() {
		return "P";
	}
}
