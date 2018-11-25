package model;

public class SacoDeMoedas extends Objeto {
	
	private int quantidadeDeMoedas;

	public SacoDeMoedas(int posicaoX, int posicaoY, int quantidadeDeMoedas) {
		super(posicaoX, posicaoY, TipoDeObjeto.SACO_DE_MOEDAS);
		this.quantidadeDeMoedas = quantidadeDeMoedas;
	}

	public int getQuantidadeDeMoedas() {
		return quantidadeDeMoedas;
	}

	public void setQuantidadeDeMoedas(int quantidadeDeMoedas) {
		this.quantidadeDeMoedas = quantidadeDeMoedas;
	}
	
	@Override
	public String toString() {
		return "S";
	}
}
