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
	private Perceptron rede = new Perceptron();

	public Agente(int posicaoX, int posicaoY, Tabuleiro tabuleiro) {
		super(posicaoX, posicaoY, TipoDeObjeto.AGENTE);
		this.tabuleiro = tabuleiro;
		this.sacosDeMoedas = new ArrayList<SacoDeMoedas>();
		this.pontuacao = 0;
		this.estadoAtual = EstadoDoAgente.PROCURANDO_PORTA;
		genetico();
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
					System.out.println("Movimento n�o permitido!");
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
				Thread.sleep(1000); //?? criar vari�vel ou deixar aqui mesmo?
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} else {
			System.out.println("Movimento n�o permitido!");
		}
	}
	
	public Set<Objeto> getObjetosAdjacentes() {
		Set<Objeto> objetos = new HashSet<>();
		Objeto objeto = null;
		
		objeto = tabuleiro.getObjetoPelaDirecao(getCoordenadas(), Direcao.NORTE);
		if (objeto != null) {
			objetos.add(objeto);
		}
		
		objeto = tabuleiro.getObjetoPelaDirecao(getCoordenadas(), Direcao.SUL);
		if (objeto != null) {
			objetos.add(objeto);
		}
		
		objeto = tabuleiro.getObjetoPelaDirecao(getCoordenadas(), Direcao.LESTE);
		if (objeto != null) {
			objetos.add(objeto);
		}

		objeto = tabuleiro.getObjetoPelaDirecao(getCoordenadas(), Direcao.OESTE);
		if (objeto != null) {
			objetos.add(objeto);
		}

		return objetos;
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
	
	public int retornaValorObjetoParaRede(Objeto objeto) {
		// Livre = 4 Muro = 3 Buraco = 1 Saco = 2 Porta = 5
		int valor = 3; //MURO
		
		if (objeto != null) {
			if(objeto.getTipo() == TipoDeObjeto.LIVRE) {
				valor = 4;
			} else if (objeto.getTipo() == TipoDeObjeto.BURACO) {
				valor = 1;
			} else if (objeto.getTipo() == TipoDeObjeto.SACO_DE_MOEDAS) {
				valor = 2;
			} else if (objeto.getTipo() == TipoDeObjeto.PORTA) {
				valor = 5;
			}
		}
		
		return valor;
	}
	
	public Direcao rodarRedeNeural(Direcao ultimaDirecao, int iteracao) {
		

		// Livre = 4 Muro = 3 Buraco = 1 Saco = 2 Porta = 5
		int valorSul   = retornaValorObjetoParaRede(getTabuleiro().getObjetoPelaDirecao(getCoordenadas(), Direcao.SUL));
		int valorLeste = retornaValorObjetoParaRede(getTabuleiro().getObjetoPelaDirecao(getCoordenadas(), Direcao.LESTE));
		int valorOeste = retornaValorObjetoParaRede(getTabuleiro().getObjetoPelaDirecao(getCoordenadas(), Direcao.OESTE));

		setPesosNeuronios();
		
		// Calcula valores camada de entrada
		int valorEntradaNeuronio0 = rede.getNeuronios().get(0).calculaY(valorSul, valorLeste, valorOeste);
		int valorEntradaNeuronio1 = rede.getNeuronios().get(1).calculaY(valorSul, valorLeste, valorOeste);
		int valorEntradaNeuronio2 = rede.getNeuronios().get(2).calculaY(valorSul, valorLeste, valorOeste);
		
		// Calcula valores movimento camada de saida  //Anda = 0, Pulo = 1
		int valorSaidaMovimentoNeuronio0 = rede.getNeuronios().get(3).calculaY(valorEntradaNeuronio0, valorEntradaNeuronio1, valorEntradaNeuronio2);
		int valorSaidaMovimentoNeuronio1 = rede.getNeuronios().get(4).calculaY(valorEntradaNeuronio0, valorEntradaNeuronio1, valorEntradaNeuronio2);
		
		// Calcula valores direcao camada de saida Sul = 0, Leste = 1, Oeste = 2
		int valorSaidaDirecaoNeuronio0 = rede.getNeuronios().get(5).calculaY(valorEntradaNeuronio0, valorEntradaNeuronio1, valorEntradaNeuronio2);
		int valorSaidaDirecaoNeuronio1 = rede.getNeuronios().get(6).calculaY(valorEntradaNeuronio0, valorEntradaNeuronio1, valorEntradaNeuronio2);
		int valorSaidaDirecaoNeuronio2 = rede.getNeuronios().get(7).calculaY(valorEntradaNeuronio0, valorEntradaNeuronio1, valorEntradaNeuronio2);
				
		Direcao direcao = null;
		
		// Verifica direcao
		if (valorSaidaDirecaoNeuronio0 > valorSaidaDirecaoNeuronio1 && valorSaidaDirecaoNeuronio0 > valorSaidaDirecaoNeuronio2) {
			direcao = Direcao.SUL;
			
		} else if (valorSaidaDirecaoNeuronio1 > valorSaidaDirecaoNeuronio0 && valorSaidaDirecaoNeuronio1 > valorSaidaDirecaoNeuronio2) {
			// evita loop
			if (ultimaDirecao != Direcao.OESTE) {
				direcao = Direcao.LESTE;
				
			} else if (valorSaidaDirecaoNeuronio0 > valorSaidaDirecaoNeuronio2) {
				direcao = Direcao.SUL;
				
			} else {
				direcao = Direcao.OESTE;
			}
			
		} else if (valorSaidaDirecaoNeuronio2 > valorSaidaDirecaoNeuronio0 && valorSaidaDirecaoNeuronio2 > valorSaidaDirecaoNeuronio1) {
			// evita loop
			if (ultimaDirecao != Direcao.LESTE) {
				direcao = Direcao.OESTE;
				
			} else if (valorSaidaDirecaoNeuronio0 > valorSaidaDirecaoNeuronio1) {
				direcao = Direcao.SUL;
				
			} else {
				direcao = Direcao.LESTE;
			}
		}
		
		boolean moveComPulo = false;
		
		if (valorSaidaMovimentoNeuronio1 > valorSaidaMovimentoNeuronio0) {
			moveComPulo = true;
		}
				
		//Imprime movimento
		imprimeMovimento(direcao, moveComPulo, iteracao);
		
		// Verificar se movimento e valido
		if (getTabuleiro().movimentoValido(direcao, moveComPulo)) {
			
			// Verifica movimento (anda ou pula)
			if (moveComPulo) {
				getTabuleiro().moverAgenteComPulo(direcao); 
			} else if (getTabuleiro().getObjetoPelaDirecao(getCoordenadas(), direcao).getTipo() == TipoDeObjeto.SACO_DE_MOEDAS) {
				getTabuleiro().moverAgenteEPegarSacoDeMoedas(direcao);
			} else {
				getTabuleiro().moverAgente(direcao);
			}	
			
			//Imprime tabuleiro
			getTabuleiro().imprime();
		} else {
			setEstadoAtual(EstadoDoAgente.GAME_OVER);
			atual[33] = pontuacao;
		}
		
		return direcao;
	}

	private void imprimeMovimento(Direcao direcao, boolean moveComPulo, int iteracao) {
		System.out.println("Itera��o " + iteracao + " - Movimento efetuado pelo agente: Dire��o " + (direcao == null ? "inv�lida" : direcao.toString()) + (moveComPulo ? " com pulo" : " sem pulo") + "\n\n");
	}

	/**
         * Seta os pesos dos nouronios de acordo com os valores gerados no AG
         */
        private void setPesosNeuronios(){
            
            for(int i=0;i<32;i++){
                rede.getNeuronios().get(i).setW0(atual[i]);
                rede.getNeuronios().get(i).setW1(atual[i+1]);
                rede.getNeuronios().get(i).setW2(atual[i+2]);
                rede.getNeuronios().get(i).setW3(atual[i+3]);
                i += 3;
            }
            
        }

	// genetico ---------------------------------------------------------------
	private double[] populacao = new double[33];
	private double[] atual = new double[33];
	private double[] melhor1 = new double[33];
	private double[] melhor2 = new double[33];

	public void genetico() {
		
		popular();

		
	}
        
        /**
         * Gera a proxima geraçao de pesos
         */
        public void proximaGeracao(){
            
            if(melhor1[33] > melhor2[33]){
                if(atual[33] > melhor1[33]){
                    melhor1 = atual.clone();
                }
            }else{
                if(atual[33] > melhor2[33]){
                    melhor2 = atual.clone();
                }
            }
            
            cruzar();
            mutar();
            
        }

	/**
	 * 
	 * 
	 * Primeira populacao com baus de 0 a 3
	 * 
	 * 
	 */
	public void popular() {

		Random rand = new Random();
                
                for (int i=0;i<31;i++) {
                    populacao[i] = rand.nextDouble();
                }
                
		atual = populacao.clone();
		melhor1 = populacao.clone();
		melhor2 = populacao.clone();
		//melhor[31] = 1000;
	}

	/**
	 * 
	 * 
	 * seleciona duas posicoes aleatorias e troca elas, sempre usa o melhor como
	 * base
	 * 
	 * 
	 */
	public void mutar() {
		Random rand = new Random();
		int t1 = rand.nextInt(32);
		int t2 = rand.nextInt(32);
		
//		atual = melhor.clone();
		
		// Troca duas posicoes aleatorias
//		double a = atual[t1];
//		double b = atual[t2];
		
		atual[t1] = rand.nextDouble();
		atual[t2] = rand.nextDouble();
	}
        
        public void cruzar(){
            
            for(int i=0;i<32;i++){
                if(i<8 || (i>=16 && i<24)){
                    atual[i] = melhor1[i];
                }else{
                    atual[i] = melhor2[i];
                }
            }
            
        }

	/**
	 * 
	 * 
	 * Faz o somatorio da diferenca de todos os baus com todos os baus
	 * 
	 * 
	 * @return
	 * 
	 * 
	 */
	public boolean aptdar() {
		// int soma0=0,soma1=0,soma2=0,soma3=0;
		int[] somas = new int[4];
	/*
		for (int i = 0; i < 16; i++) {
			if (atual[i] == 0)
				somas[0] += listaSacos.get(i).getMoedas();
			if (atual[i] == 1)
				somas[1] += listaSacos.get(i).getMoedas();
			if (atual[i] == 2)
				somas[2] += listaSacos.get(i).getMoedas();
			if (atual[i] == 3)
				somas[3] += listaSacos.get(i).getMoedas();
		}
	 */
		// int diferenca = Math.abs((Math.abs(soma0+soma1))-(Math.abs(soma2+soma3)));
//		int difA = 0;
//		int d;
//		
//		// Soma a diferenca de todos com todos
//		for (int t = 0; t < 4; t++) {
//			d = somas[t];
//			for (int g = 0; g < 4; g++) {
//				difA += Math.abs(d - somas[g]);
//			}
//		}
//
//		int diferenca = difA;
//		atual[16] = diferenca;
//		if (diferenca == 0) { // Achou a resposta
//			melhor = atual.clone(); // Elitiza
//			return true;
//		}
//		
//		if (diferenca < melhor[16]) { // Achou um melhor
//			melhor = atual.clone(); // Elitiza
//		}

		return false;
	}

	public void torneio() {

	}

	public void ativarAprendizadoDeMaquina() {
		while (estadoAtual != EstadoDoAgente.FORA_DO_LABIRINTO && estadoAtual != EstadoDoAgente.GAME_OVER) {
			int iteracao = 0;
			Direcao ultimaDirecao = null;
			
			while (estadoAtual == EstadoDoAgente.PROCURANDO_PORTA) {
				iteracao++;
				ultimaDirecao = rodarRedeNeural(ultimaDirecao, iteracao);
			}
			
			if (estadoAtual == EstadoDoAgente.FORA_DO_LABIRINTO) {
				System.out.println("-*--*--*--*--*--*--*--*--*--*--*-");
				System.out.println("Labirinto conclu�do com sucesso!");
				System.out.println("-*--*--*--*--*--*--*--*--*--*--*-");

			} else if (estadoAtual == EstadoDoAgente.GAME_OVER) {
				System.out.println("-x--x--x--x--x--x--x--x--x--x--x-");
				System.out.println("GAME OVER");
				System.out.println("-x--x--x--x--x--x--x--x--x--x--x-");
			}
			
			if(iteracao < 4) getTabuleiro().resetTabuleiroOpcao1();
		}
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
			System.out.println("Labirinto conclu�do com sucesso!");
			System.out.println("-*--*--*--*--*--*--*--*--*--*--*-\n\n");

		} else if (estadoAtual == EstadoDoAgente.GAME_OVER) {
			System.out.println("-x--x--x--x--x--x--x--x--x--x--x-");
			System.out.println("GAME OVER");
			System.out.println("-x--x--x--x--x--x--x--x--x--x--x-\n\n");
		}
	}
	
	@Override
	public String toString() {
		return "A";
	}
}
