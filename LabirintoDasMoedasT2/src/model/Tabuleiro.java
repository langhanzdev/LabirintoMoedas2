package model;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Tabuleiro {

	private Objeto[][] tabuleiro;
	private Agente agente;
	private Queue<Objeto> objetosParaRetornarAoTabuleiro;
	private int iteracoes;

	public Tabuleiro(int opcao) {
		criaTabuleiro(opcao);
		this.objetosParaRetornarAoTabuleiro = new PriorityQueue<Objeto>();
		this.iteracoes = 0;
		
		inicializaTabuleiro();
	}
	
	private void criaTabuleiro(int opcao) {
		if (opcao == 1) {
			this.tabuleiro = new Objeto[8][3];
			criaParedesA();
			criaPorta();
			criaBuracosA();
			criaSacosDeMoedasA();
			criaAgente();
		} else if (opcao == 2) {
			this.tabuleiro = new Objeto[8][6];
			criaParedesA();
			criaPorta();
			criaBuracosA();
			criaSacosDeMoedasA();
			criaAgente();
		} else {
			this.tabuleiro = null;
		}
	}
	
	public void resetTabuleiroOpcao1() {
		this.tabuleiro[agente.getPosicaoX()][agente.getPosicaoY()] = new Livre(agente.getPosicaoX(), agente.getPosicaoY());
		
		this.agente.setCoordenadas(new Coordenadas(0, 0));
		this.agente.setSacosDeMoedas(new ArrayList<SacoDeMoedas>());
		this.agente.setPontuacao(0);
		this.agente.setEstadoAtual(EstadoDoAgente.PROCURANDO_PORTA);
		this.tabuleiro[0][0] = agente;
		
		criaSacosDeMoedasA();
		
		this.objetosParaRetornarAoTabuleiro = new PriorityQueue<Objeto>();
		this.iteracoes = 0;
		
		inicializaTabuleiro();
	}

	public Objeto getAgente() {
		return agente;
	}

	public void setAgente(Agente agente) {
		this.agente = agente;
	}

	private void inicializaTabuleiro() {
		for (int i = 0; i < tabuleiro.length; i++) {
			for (int j = 0; j < tabuleiro[0].length; j++) {
				if (tabuleiro[i][j] == null) {
					tabuleiro[i][j] = new Livre(i, j);
				}
			}
		}
		
		//imprime();
	}
	
	private void criaParedesA() {
		tabuleiro[0][1] = new Muro(0, 1);
		tabuleiro[0][2] = new Muro(0, 2);
		
		tabuleiro[2][0] = new Muro(2, 0);
		tabuleiro[2][1] = new Muro(2, 1);
		
		tabuleiro[4][1] = new Muro(4, 1);
		tabuleiro[4][2] = new Muro(4, 2);
		
		tabuleiro[6][0] = new Muro(6, 0);
		tabuleiro[6][1] = new Muro(6, 1);
	}


	private void criaPorta() {
		tabuleiro[7][0] = new Porta(7, 0);
	}

	private void criaBuracosA() {
		tabuleiro[1][1] = new Buraco(1, 1);
		
		tabuleiro[3][1] = new Buraco(3, 1);
		
		tabuleiro[4][0] = new Buraco(4, 0);
		
		tabuleiro[6][2] = new Buraco(6, 2);
	}
	
	private void criaSacosDeMoedasA() {
		tabuleiro[2][2] = new SacoDeMoedas(2, 2, 10);
		
		tabuleiro[3][2] = new SacoDeMoedas(3, 2, 10);
		
		tabuleiro[5][0] = new SacoDeMoedas(5, 0, 10);
		tabuleiro[5][2] = new SacoDeMoedas(5, 2, 10);
		
		tabuleiro[7][1] = new SacoDeMoedas(7, 1, 10);
	}

	private void criaAgente() {
		Agente a = new Agente(0, 0, this);
		tabuleiro[0][0] = a;
		this.agente = a;
	}
	
	public String pontuacaoLinhaColuna(int i, int j, int espacos) {
		StringBuilder sb = new StringBuilder();
		
		if (((i >= agente.getPosicaoX() - 2 && i <= agente.getPosicaoX() + 2) && j == agente.getPosicaoY())
				|| ((j >= agente.getPosicaoY() - 2 && j <= agente.getPosicaoY() + 2) && i == agente.getPosicaoX())) {
				
				if (i == agente.getPosicaoX() - 2 && verificaSeEMuroOuNulo(agente.getPosicaoX()-1, agente.getPosicaoY())) {
					sb.append("   ");
				} else if (i == agente.getPosicaoX() + 2 && verificaSeEMuroOuNulo(agente.getPosicaoX()+1, agente.getPosicaoY())) {
					sb.append("   ");
				} else if (j == agente.getPosicaoY() - 2 && verificaSeEMuroOuNulo(agente.getPosicaoX(), agente.getPosicaoY()-1)) {
					sb.append("   ");
				} else if (j == agente.getPosicaoY() + 2 && verificaSeEMuroOuNulo(agente.getPosicaoX(), agente.getPosicaoY()+1)) {
					sb.append("   ");
				} else {
					sb.append(" ");
					sb.append(insereEspacos(this.tabuleiro[i][j].toString(), espacos));		
				}
			} else {
				sb.append("   ");
			}
		return sb.toString();
	}

	public void imprimeTabuleiro() {
		StringBuilder sb = new StringBuilder();
		int espacos = 2;
		sb.append("--Tabuleiro--\n");
		for (int i = 0; i < tabuleiro.length; i++) {
			for (int j = 0; j < tabuleiro[0].length; j++) {
				sb.append(" ");
				sb.append(insereEspacos(this.tabuleiro[i][j].toString(), espacos));
			}
			if (i < tabuleiro.length-1) {
				sb.append("\n");
			}
		}
		sb.append("\n-------------");
		System.out.println(sb.toString());
	}
	
	public void imprimirPontuacao() {
		if (agente != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("Pontuação Total: " + agente.getPontuacao() + " pontos \n");
			
			sb.append("Sacos de Moedas: ");
			for (int i = 1; i <= agente.getSacosDeMoedas().size(); i++) {
				sb.append("(" + i + ": " + agente.getSacosDeMoedas().get(i-1).getQuantidadeDeMoedas() + " moedas)");
			}
			
			System.out.println(sb.toString());
			
			//System.out.println("Iterações: " + iteracoes);	
		}
	}
	
	public String montaObjetoParaImprimir(int x, int y, int espacos) {
		StringBuilder sb = new StringBuilder();
		
		if (x >= 0 && x < tabuleiro.length && y >= 0 && y < tabuleiro[0].length) {
			Objeto objeto = this.tabuleiro[x][y];
			sb.append(" ");
			sb.append(insereEspacos(objeto.toString(), espacos));
			
		} else {
			sb.append("   ");
		}
		
		return sb.toString();
	}
	
	public boolean verificaSeEMuroOuNulo(int x, int y) {
		Objeto o = getObjeto(x, y);
		if (o == null || o.getTipo() == TipoDeObjeto.MURO) {
			return true;
		}
		return false;
	}
	
	public void imprimeTabuleiroVisivelPeloAgente() {
		if (agente != null) {
			StringBuilder sb = new StringBuilder();
			int espacos = 2;
			sb.append("\n--Agente--\n");
			for (int i = 0; i < tabuleiro.length; i++) {
				for (int j = 0; j < tabuleiro[0].length; j++) {
					if (((i >= agente.getPosicaoX() - 2 && i <= agente.getPosicaoX() + 2) && j == agente.getPosicaoY())
						|| ((j >= agente.getPosicaoY() - 2 && j <= agente.getPosicaoY() + 2) && i == agente.getPosicaoX())) {
						
						if (i == agente.getPosicaoX() - 2 && verificaSeEMuroOuNulo(agente.getPosicaoX()-1, agente.getPosicaoY())) {
							sb.append("   ");
						} else if (i == agente.getPosicaoX() + 2 && verificaSeEMuroOuNulo(agente.getPosicaoX()+1, agente.getPosicaoY())) {
							sb.append("   ");
						} else if (j == agente.getPosicaoY() - 2 && verificaSeEMuroOuNulo(agente.getPosicaoX(), agente.getPosicaoY()-1)) {
							sb.append("   ");
						} else if (j == agente.getPosicaoY() + 2 && verificaSeEMuroOuNulo(agente.getPosicaoX(), agente.getPosicaoY()+1)) {
							sb.append("   ");
						} else {
							sb.append(" ");
							sb.append(insereEspacos(this.tabuleiro[i][j].toString(), espacos));		
						}
					} else {
						sb.append("   ");
					}
				}
				sb.append("\n");
			}
			sb.append("------------");
			System.out.println(sb.toString());
		}
	}

	public void imprimeTabuleiroVisivelPeloAgente2() {
		if (agente != null) {
			StringBuilder sb = new StringBuilder();
			
			//Linha superior ao agente 2
			Objeto obj = getObjeto(agente.getPosicaoX() - 1, agente.getPosicaoY());
			if (obj != null && obj.getTipo() != TipoDeObjeto.MURO) {
				sb.append(montaObjetoParaImprimir(agente.getPosicaoX() - 2, agente.getPosicaoY(), 8));
				sb.append("\n");
			}
			
			//Linha superior ao agente 1
			sb.append(montaObjetoParaImprimir(agente.getPosicaoX() - 1, agente.getPosicaoY(), 8));
			sb.append("\n");
			
			//Linha do agente
			obj = getObjeto(agente.getPosicaoX(), agente.getPosicaoY() - 1);
			if (obj != null && obj.getTipo() != TipoDeObjeto.MURO) {
				sb.append(montaObjetoParaImprimir(agente.getPosicaoX(), agente.getPosicaoY() - 2, 2));
			} else {
				sb.append("   ");
			}
			sb.append(montaObjetoParaImprimir(agente.getPosicaoX(), agente.getPosicaoY() - 1, 2));
			sb.append(montaObjetoParaImprimir(agente.getPosicaoX(), agente.getPosicaoY(), 2));
			sb.append(montaObjetoParaImprimir(agente.getPosicaoX(), agente.getPosicaoY() + 1, 2));
			obj = getObjeto(agente.getPosicaoX(), agente.getPosicaoY() + 1);
			if (obj != null && obj.getTipo() != TipoDeObjeto.MURO) {
				sb.append(montaObjetoParaImprimir(agente.getPosicaoX(), agente.getPosicaoY() + 2, 2));
			}
			sb.append("\n");
			
			//Linha inferior ao agente 1
			sb.append(montaObjetoParaImprimir(agente.getPosicaoX() + 1, agente.getPosicaoY(), 8));
			sb.append("\n");
			
			//Linha inferior ao agente 2
			obj = getObjeto(agente.getPosicaoX() + 1, agente.getPosicaoY());
			if (obj != null && obj.getTipo() != TipoDeObjeto.MURO) {
				sb.append(montaObjetoParaImprimir(agente.getPosicaoX() + 2, agente.getPosicaoY(), 8));
			}
			
			System.out.println(sb.toString());
		}
	}
	
	public void imprime() {
		imprimeTabuleiro();
		imprimirPontuacao();
		imprimeTabuleiroVisivelPeloAgente();
	}

	private String insereEspacos(String text, int num) {
		while (text.length() < num) {
			text = " " + text;
		}

		return text;
	}

	private Objeto getObjeto(int x, int y) {
		if (x >= 0 && x < tabuleiro.length && y >= 0 && y < tabuleiro[0].length) {
			return tabuleiro[x][y];
		}
		return null;
	}

	public Objeto getObjetoPelasCoordenadas(Coordenadas coordenadas) {
		if (coordenadas.getPosicaoX() >= 0 && coordenadas.getPosicaoX() < tabuleiro.length 
				&& coordenadas.getPosicaoY() >= 0 && coordenadas.getPosicaoY() < tabuleiro[0].length) {
			return tabuleiro[coordenadas.getPosicaoX()][coordenadas.getPosicaoY()];
		}
		return null;
	}

	private void trocaObjeto(Objeto objetoAtual, Objeto objetoNovo) {
		objetoNovo.setCoordenadas(objetoAtual.getCoordenadas());
		tabuleiro[objetoAtual.getPosicaoX()][objetoAtual.getPosicaoY()] = objetoNovo;
	}
	
	public Objeto getObjetoPelaDirecao(Coordenadas coordenadas, Direcao direcao) {
		if (direcao == Direcao.NORTE) {
			return getObjeto(coordenadas.getPosicaoX() - 1, coordenadas.getPosicaoY());

		} else if (direcao == Direcao.SUL) {
			return getObjeto(coordenadas.getPosicaoX() + 1, coordenadas.getPosicaoY());

		} else if (direcao == Direcao.LESTE) {
			return getObjeto(coordenadas.getPosicaoX(), coordenadas.getPosicaoY() + 1);

		} else if (direcao == Direcao.OESTE) {
			return getObjeto(coordenadas.getPosicaoX(), coordenadas.getPosicaoY() - 1);
		}
		return null;
	}
	
	public boolean movimentoValido(Direcao direcao, boolean comPulo) {
		Objeto objeto = getObjetoPelaDirecao(agente.getCoordenadas(), direcao);
		
		if (objeto != null && objeto.getTipo() != TipoDeObjeto.MURO) {
			
			if (objeto.getTipo() != TipoDeObjeto.BURACO && !comPulo) {
				return true;
				
			} else if (objeto.getTipo() == TipoDeObjeto.BURACO && comPulo) {
				Objeto objetoDoObjeto = getObjetoPelaDirecao(objeto.getCoordenadas(), direcao);
				
				if (objetoDoObjeto != null && objetoDoObjeto.getTipo() != TipoDeObjeto.MURO 
						&& objetoDoObjeto.getTipo() != TipoDeObjeto.BURACO) {
					return true;
				}
			} else if (comPulo) {
				return false;
				
			}
		}
		
		//System.out.println("--------- Game Over. ---------");
		return false;
	}
	
	public void moverAgenteEPegarSacoDeMoedas(Direcao direcao) {
		Objeto objeto = getObjetoPelaDirecao(agente.getCoordenadas(), direcao);

		if (movimentoValido(direcao, false) && objeto.getTipo() == TipoDeObjeto.SACO_DE_MOEDAS) {
			SacoDeMoedas sacoDeMoedas = (SacoDeMoedas) objeto;
			agente.getSacosDeMoedas().add(sacoDeMoedas);
			agente.adicionarPontos(sacoDeMoedas.getQuantidadeDeMoedas());
			
			trocaObjeto(objeto, new Livre(objeto.getPosicaoX(), objeto.getPosicaoY()));
			moverAgente(direcao);
		}
	}

	public void moverAgente(Direcao direcao) {
		Objeto objeto = getObjetoPelaDirecao(agente.getCoordenadas(), direcao);

		if (movimentoValido(direcao, false)) {
			if (objeto.getTipo() == TipoDeObjeto.LIVRE) {
				Objeto substituto = null;
				if (!objetosParaRetornarAoTabuleiro.isEmpty()) {
					substituto = objetosParaRetornarAoTabuleiro.remove();
				} else {
					substituto = new Livre(agente.getPosicaoX(), agente.getPosicaoY());
				}

				trocaObjeto(agente, substituto);
				trocaObjeto(objeto, agente);
				iteracoes++;

			} else if (objeto.getTipo() == TipoDeObjeto.PORTA || objeto.getTipo() == TipoDeObjeto.SACO_DE_MOEDAS) {

				Objeto substituto = null;
				if (!objetosParaRetornarAoTabuleiro.isEmpty()) {
					substituto = objetosParaRetornarAoTabuleiro.remove();
				} else {
					substituto = new Livre(agente.getPosicaoX(), agente.getPosicaoY());
				}

				objetosParaRetornarAoTabuleiro.add(objeto);

				trocaObjeto(agente, substituto);
				trocaObjeto(objeto, agente);
				iteracoes++;
			}
		}
	}
	
	public void moverAgenteParaPorta(Direcao direcao) {
		Objeto objeto = getObjetoPelaDirecao(agente.getCoordenadas(), direcao);

		if (movimentoValido(direcao, false)) {
			if (objeto.getTipo() == TipoDeObjeto.PORTA) {
				trocaObjeto(agente, new Livre(agente.getPosicaoX(), agente.getPosicaoY()));
				
				iteracoes++;
				imprimirPontuacao();
				setAgente(null);
			}
		}
	}
	
	public void moverAgenteComPulo(Direcao direcao) {
		Objeto objeto = getObjetoPelaDirecao(agente.getCoordenadas(), direcao);
		
		if (movimentoValido(direcao, true) && objeto.getTipo() == TipoDeObjeto.BURACO) {
			Objeto objetoDoObjeto = getObjetoPelaDirecao(objeto.getCoordenadas(), direcao);
			Objeto substituto = null;
			
			if (!objetosParaRetornarAoTabuleiro.isEmpty()) {
				substituto = objetosParaRetornarAoTabuleiro.remove();
			} else {
				substituto = new Livre(agente.getPosicaoX(), agente.getPosicaoY());
			}
			
			trocaObjeto(agente, substituto);
			
			if (objetoDoObjeto.getTipo() == TipoDeObjeto.PORTA) {
				objetosParaRetornarAoTabuleiro.add(objetoDoObjeto);
			}
			
			trocaObjeto(objetoDoObjeto, agente);
			iteracoes++;
		}
	}
	
	public void moverAgenteComPuloEPegarSacoDeMoedas(Direcao direcao) {
		Objeto objeto = getObjetoPelaDirecao(agente.getCoordenadas(), direcao);

		if (movimentoValido(direcao, true) && objeto.getTipo() == TipoDeObjeto.BURACO) {
			Objeto objetoDoObjeto = getObjetoPelaDirecao(objeto.getCoordenadas(), direcao);
			
			if (objetoDoObjeto.getTipo() == TipoDeObjeto.SACO_DE_MOEDAS) {
				SacoDeMoedas sacoDeMoedas = (SacoDeMoedas) objetoDoObjeto;
				agente.getSacosDeMoedas().add(sacoDeMoedas);
				agente.adicionarPontos(sacoDeMoedas.getQuantidadeDeMoedas());
				
				Objeto substituto = null;
				
				if (!objetosParaRetornarAoTabuleiro.isEmpty()) {
					substituto = objetosParaRetornarAoTabuleiro.remove();
				} else {
					substituto = new Livre(agente.getPosicaoX(), agente.getPosicaoY());
				}
				
				trocaObjeto(agente, substituto);			
				trocaObjeto(objetoDoObjeto, agente);
				iteracoes++;
			}
		}
	}
}
