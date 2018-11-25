package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class Agente extends Objeto {

	private List<SacoDeMoedas> sacosDeMoedas;
	private int pontuacao;
	private Tabuleiro tabuleiro;
	private EstadoDoAgente estadoAtual;

	public Agente(int posicaoX, int posicaoY, Tabuleiro tabuleiro) {
		super(posicaoX, posicaoY, TipoDeObjeto.AGENTE);
		this.tabuleiro = tabuleiro;
		this.sacosDeMoedas = new ArrayList<SacoDeMoedas>();
		this.pontuacao = 0;
		this.estadoAtual = EstadoDoAgente.PROCURANDO_PORTA;
	}

	public List<SacoDeMoedas> getSacosDeMoedas() {
		return sacosDeMoedas;
	}

	public void setSacosDeMoedas(List<SacoDeMoedas> sacosDeMoedas) {
		this.sacosDeMoedas = sacosDeMoedas;
	}

	public int getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}

	public Tabuleiro getTabuleiro() {
		return tabuleiro;
	}

	public void setTabuleiro(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
	}

	public void adicionarPontos(int pontos) {
		setPontuacao(getPontuacao() + pontos);
	}

	public EstadoDoAgente getEstadoAtual() {
		return estadoAtual;
	}

	public void setEstadoAtual(EstadoDoAgente estadoAtual) {
		this.estadoAtual = estadoAtual;
	}

	public void mover(Direcao direcao) {
		Objeto objeto = getTabuleiro().getObjetoPelaDirecao(getCoordenadas(), direcao);

		if (objeto != null) {
			if (objeto.getTipo() == TipoDeObjeto.BURACO) {
				Objeto objetoDoObjeto = getTabuleiro().getObjetoPelaDirecao(objeto.getCoordenadas(), direcao);
				
				if (objetoDoObjeto != null && objetoDoObjeto.getTipo() != TipoDeObjeto.MURO
						&& objetoDoObjeto.getTipo() != TipoDeObjeto.BURACO) {
					if (objetoDoObjeto.getTipo() == TipoDeObjeto.SACO_DE_MOEDAS) {
						getTabuleiro().moverAgenteComPuloEPegarSacoDeMoedas(direcao);
					
					} else if (objetoDoObjeto.getTipo() == TipoDeObjeto.PORTA) {
						getTabuleiro().moverAgenteParaPorta(direcao);
						estadoAtual = EstadoDoAgente.FORA_DO_LABIRINTO;
						
					} else {
						getTabuleiro().moverAgenteComPulo(direcao);
					}
				} else {
					System.out.println("Movimento não permitido!");
				}
	
			} else if (objeto.getTipo() == TipoDeObjeto.SACO_DE_MOEDAS) {
				getTabuleiro().moverAgenteEPegarSacoDeMoedas(direcao);
	
			} else if (objeto.getTipo() == TipoDeObjeto.PORTA) {
				getTabuleiro().moverAgenteParaPorta(direcao);
				estadoAtual = EstadoDoAgente.FORA_DO_LABIRINTO;
				
			} else {
				getTabuleiro().moverAgente(direcao);
			}
	
			tabuleiro.imprimeTabuleiro();
			tabuleiro.imprimeTabuleiroVisivelPeloAgente();
	
			try {
				Thread.sleep(1000); //?? criar variável ou deixar aqui mesmo?
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} else {
			System.out.println("Movimento não permitido!");
		}
	}
	
	public Set<Objeto> getObjetosAVista() {
		Set<Objeto> objetos = new HashSet<>();
		Objeto objeto = null;
		
		objeto = tabuleiro.getObjetoPelaDirecao(getCoordenadas(), Direcao.NORTE);
		if (objeto != null) {
			objetos.add(objeto);
			objeto = tabuleiro.getObjetoPelaDirecao(objeto.coordenadas, Direcao.NORTE);
			if (objeto != null && objeto.getTipo() != TipoDeObjeto.MURO) {
				objetos.add(objeto);
			}
		}
		
		objeto = tabuleiro.getObjetoPelaDirecao(getCoordenadas(), Direcao.SUL);
		if (objeto != null) {
			objetos.add(objeto);
			objeto = tabuleiro.getObjetoPelaDirecao(objeto.coordenadas, Direcao.SUL);
			if (objeto != null && objeto.getTipo() != TipoDeObjeto.MURO) {
				objetos.add(objeto);
			}
		}
		
		objeto = tabuleiro.getObjetoPelaDirecao(getCoordenadas(), Direcao.LESTE);
		if (objeto != null) {
			objetos.add(objeto);
			objeto = tabuleiro.getObjetoPelaDirecao(objeto.coordenadas, Direcao.LESTE);
			if (objeto != null && objeto.getTipo() != TipoDeObjeto.MURO) {
				objetos.add(objeto);
			}
		}

		objeto = tabuleiro.getObjetoPelaDirecao(getCoordenadas(), Direcao.OESTE);
		if (objeto != null) {
			objetos.add(objeto);
			objeto = tabuleiro.getObjetoPelaDirecao(objeto.coordenadas, Direcao.OESTE);
			if (objeto != null && objeto.getTipo() != TipoDeObjeto.MURO) {
				objetos.add(objeto);
			}
		}

		return objetos;
	}
	
	private Direcao getDirecaoTipoDeObjetoAVista(TipoDeObjeto tipoDeObjeto) {
		Objeto objeto = null;
		
		objeto = tabuleiro.getObjetoPelaDirecao(getCoordenadas(), Direcao.NORTE);
		if (objeto != null) {
			if (objeto.getTipo() == tipoDeObjeto) return Direcao.NORTE;
			
			objeto = tabuleiro.getObjetoPelaDirecao(objeto.coordenadas, Direcao.NORTE);
			if (objeto != null && objeto.getTipo() == tipoDeObjeto) return Direcao.NORTE;
		}
		
		objeto = tabuleiro.getObjetoPelaDirecao(getCoordenadas(), Direcao.SUL);
		if (objeto != null) {
			if (objeto.getTipo() == tipoDeObjeto) return Direcao.SUL;
			
			objeto = tabuleiro.getObjetoPelaDirecao(objeto.coordenadas, Direcao.SUL);
			if (objeto != null && objeto.getTipo() == tipoDeObjeto) return Direcao.SUL;
		}
		
		objeto = tabuleiro.getObjetoPelaDirecao(getCoordenadas(), Direcao.LESTE);
		if (objeto != null) {
			if (objeto.getTipo() == tipoDeObjeto) return Direcao.LESTE;
			
			objeto = tabuleiro.getObjetoPelaDirecao(objeto.coordenadas, Direcao.LESTE);
			if (objeto != null && objeto.getTipo() == tipoDeObjeto) return Direcao.LESTE;
		}

		objeto = tabuleiro.getObjetoPelaDirecao(getCoordenadas(), Direcao.OESTE);
		if (objeto != null) {
			if (objeto.getTipo() == tipoDeObjeto) return Direcao.OESTE;
			
			objeto = tabuleiro.getObjetoPelaDirecao(objeto.coordenadas, Direcao.OESTE);
			if (objeto != null && objeto.getTipo() == tipoDeObjeto) return Direcao.OESTE;
		}


		return null;
	}
	
	public boolean direcaoValida(Direcao direcao) {
		if (direcao == null || tabuleiro.getObjetoPelaDirecao(getCoordenadas(),direcao) == null
				|| tabuleiro.getObjetoPelaDirecao(getCoordenadas(),direcao).getTipo() == TipoDeObjeto.MURO
				|| (tabuleiro.getObjetoPelaDirecao(getCoordenadas(),direcao).getTipo() == TipoDeObjeto.BURACO
						&& (tabuleiro.getObjetoPelaDirecao(tabuleiro.getObjetoPelaDirecao(getCoordenadas(),direcao).getCoordenadas(),direcao) == null
								|| tabuleiro.getObjetoPelaDirecao(tabuleiro.getObjetoPelaDirecao(getCoordenadas(),direcao).getCoordenadas(),direcao)
										.getTipo() == TipoDeObjeto.MURO
								|| tabuleiro.getObjetoPelaDirecao(tabuleiro.getObjetoPelaDirecao(getCoordenadas(),direcao).getCoordenadas(),direcao)
										.getTipo() == TipoDeObjeto.BURACO))) {
			return false;
		}
		return true;
	}

	public Direcao estrategiaMoverAutonomo(Direcao direcao) {
		boolean achouAlgoImportante = false;

		if (getDirecaoTipoDeObjetoAVista(TipoDeObjeto.SACO_DE_MOEDAS) != null) {
			direcao = getDirecaoTipoDeObjetoAVista(TipoDeObjeto.SACO_DE_MOEDAS);
			achouAlgoImportante = true;
		}
		
		Random random = new Random();
		if (!direcaoValida(direcao) || (!achouAlgoImportante && random.nextInt(5) == 3)) {
			if (direcaoValida(Direcao.SUL)) {
				direcao = Direcao.SUL;
			} else if (direcaoValida(Direcao.LESTE)) {
				direcao = Direcao.LESTE;
			} else if (direcaoValida(Direcao.OESTE)) {
				direcao = Direcao.OESTE;
			} else if (direcaoValida(Direcao.NORTE)) {
				direcao = Direcao.NORTE;
			}
		}
		
		mover(direcao);
		tabuleiro.imprimirPontuacao();
		return direcao;
	}

	public void ativarModoAutonomo() {		
		while (estadoAtual != EstadoDoAgente.FORA_DO_LABIRINTO && estadoAtual != EstadoDoAgente.GAME_OVER) {

			Direcao direcao = null;

			while (estadoAtual == EstadoDoAgente.PROCURANDO_PORTA) {
				// Aqui devera utilizar a rede neural, criado metodo abaixo para testar elementos do tabuleiro.
				direcao = estrategiaMoverAutonomo(direcao);
			}
			
			int qtdSacosDeMoedas = getSacosDeMoedas().size();
			while (estadoAtual == EstadoDoAgente.PROCURANDO_SACOS_DE_MOEDA) {
				
				if (qtdSacosDeMoedas != getSacosDeMoedas().size()) {
					setEstadoAtual(EstadoDoAgente.PROCURANDO_PORTA);
					break;
				}
				
				qtdSacosDeMoedas = getSacosDeMoedas().size();
				
				direcao = estrategiaMoverAutonomo(direcao);
			}

		}

		if (estadoAtual == EstadoDoAgente.FORA_DO_LABIRINTO) {
			System.out.println("-*--*--*--*--*--*--*--*--*--*--*-");
			System.out.println("Labirinto concluído com sucesso!");
			System.out.println("-*--*--*--*--*--*--*--*--*--*--*-");

		} else if (estadoAtual == EstadoDoAgente.GAME_OVER) {
			System.out.println("-x--x--x--x--x--x--x--x--x--x--x-");
			System.out.println("GAME OVER");
			System.out.println("-x--x--x--x--x--x--x--x--x--x--x-");

		}
	}
	
	@Override
	public String toString() {
		return "A";
	}
}
