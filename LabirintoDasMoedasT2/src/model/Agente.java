package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Agente extends Objeto {

	private List<SacoDeMoedas> sacosDeMoedas;
	private int pontuacao;
	private Tabuleiro tabuleiro;
	private EstadoDoAgente estadoAtual;
	private Perceptron rede = new Perceptron();

	// genetico ---------------------------------------------------------------
	private double[][] populacao;
	//private double[] atual;
	//private double[] melhor1;
	//private double[] melhor2;

	public Agente(int posicaoX, int posicaoY, Tabuleiro tabuleiro) {
		super(posicaoX, posicaoY, TipoDeObjeto.AGENTE);
		this.tabuleiro = tabuleiro;
		this.sacosDeMoedas = new ArrayList<SacoDeMoedas>();
		this.pontuacao = 0;
		this.estadoAtual = EstadoDoAgente.PROCURANDO_PORTA;

		populacao = new double[15][33];
		//atual = new double[33];
		//melhor1 = new double[33];
		//melhor2 = new double[33];
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
					System.out.println("Movimento nï¿½o permitido!");
				}

			} else if (objeto.getTipo() == TipoDeObjeto.SACO_DE_MOEDAS) {
				getTabuleiro().moverAgenteEPegarSacoDeMoedas(direcao);

			} else if (objeto.getTipo() == TipoDeObjeto.PORTA) {
				getTabuleiro().moverAgenteParaPorta(direcao);
				estadoAtual = EstadoDoAgente.FORA_DO_LABIRINTO;

			} else {
				getTabuleiro().moverAgente(direcao);
			}

			//tabuleiro.imprimeTabuleiro();
			//tabuleiro.imprimeTabuleiroVisivelPeloAgente();

		} else {
			System.out.println("Movimento nï¿½o permitido!");
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
			if (objeto.getTipo() == tipoDeObjeto)
				return Direcao.NORTE;

			objeto = tabuleiro.getObjetoPelaDirecao(objeto.coordenadas, Direcao.NORTE);
			if (objeto != null && objeto.getTipo() == tipoDeObjeto)
				return Direcao.NORTE;
		}

		objeto = tabuleiro.getObjetoPelaDirecao(getCoordenadas(), Direcao.SUL);
		if (objeto != null) {
			if (objeto.getTipo() == tipoDeObjeto)
				return Direcao.SUL;

			objeto = tabuleiro.getObjetoPelaDirecao(objeto.coordenadas, Direcao.SUL);
			if (objeto != null && objeto.getTipo() == tipoDeObjeto)
				return Direcao.SUL;
		}

		objeto = tabuleiro.getObjetoPelaDirecao(getCoordenadas(), Direcao.LESTE);
		if (objeto != null) {
			if (objeto.getTipo() == tipoDeObjeto)
				return Direcao.LESTE;

			objeto = tabuleiro.getObjetoPelaDirecao(objeto.coordenadas, Direcao.LESTE);
			if (objeto != null && objeto.getTipo() == tipoDeObjeto)
				return Direcao.LESTE;
		}

		objeto = tabuleiro.getObjetoPelaDirecao(getCoordenadas(), Direcao.OESTE);
		if (objeto != null) {
			if (objeto.getTipo() == tipoDeObjeto)
				return Direcao.OESTE;

			objeto = tabuleiro.getObjetoPelaDirecao(objeto.coordenadas, Direcao.OESTE);
			if (objeto != null && objeto.getTipo() == tipoDeObjeto)
				return Direcao.OESTE;
		}

		return null;
	}

	public boolean direcaoValida(Direcao direcao) {
		if (direcao == null || tabuleiro.getObjetoPelaDirecao(getCoordenadas(), direcao) == null
				|| tabuleiro.getObjetoPelaDirecao(getCoordenadas(), direcao).getTipo() == TipoDeObjeto.MURO
				|| (tabuleiro.getObjetoPelaDirecao(getCoordenadas(), direcao).getTipo() == TipoDeObjeto.BURACO
						&& (tabuleiro.getObjetoPelaDirecao(
								tabuleiro.getObjetoPelaDirecao(getCoordenadas(), direcao).getCoordenadas(),
								direcao) == null
								|| tabuleiro.getObjetoPelaDirecao(
										tabuleiro.getObjetoPelaDirecao(getCoordenadas(), direcao).getCoordenadas(),
										direcao).getTipo() == TipoDeObjeto.MURO
								|| tabuleiro.getObjetoPelaDirecao(
										tabuleiro.getObjetoPelaDirecao(getCoordenadas(), direcao).getCoordenadas(),
										direcao).getTipo() == TipoDeObjeto.BURACO))) {
			return false;
		}
		return true;
	}

	public int retornaValorObjetoParaRede(Objeto objeto) {
		// Livre = 4 Muro = 3 Buraco = 1 Saco = 2 Porta = 5
		int valor = 3; // MURO

		if (objeto != null) {
			if (objeto.getTipo() == TipoDeObjeto.LIVRE) {
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

	public Direcao rodarRedeNeural(Direcao ultimaDirecao, int iteracao, double[] populacao) {			
		// Livre = 4 Muro = 3 Buraco = 1 Saco = 2 Porta = 5
		int valorSul = retornaValorObjetoParaRede(getTabuleiro().getObjetoPelaDirecao(getCoordenadas(), Direcao.SUL));
		int valorLeste = retornaValorObjetoParaRede(
				getTabuleiro().getObjetoPelaDirecao(getCoordenadas(), Direcao.LESTE));
		int valorOeste = retornaValorObjetoParaRede(
				getTabuleiro().getObjetoPelaDirecao(getCoordenadas(), Direcao.OESTE));

		setPesosNeuronios(populacao);

		// Calcula valores camada de entrada
		int valorEntradaNeuronio0 = rede.getNeuronios().get(0).calculaY(valorSul, valorLeste, valorOeste);
		int valorEntradaNeuronio1 = rede.getNeuronios().get(1).calculaY(valorSul, valorLeste, valorOeste);
		int valorEntradaNeuronio2 = rede.getNeuronios().get(2).calculaY(valorSul, valorLeste, valorOeste);

		// Calcula valores movimento camada de saida //Anda = 0, Pulo = 1
		int valorSaidaMovimentoNeuronio0 = rede.getNeuronios().get(3).calculaY(valorEntradaNeuronio0,
				valorEntradaNeuronio1, valorEntradaNeuronio2);
		int valorSaidaMovimentoNeuronio1 = rede.getNeuronios().get(4).calculaY(valorEntradaNeuronio0,
				valorEntradaNeuronio1, valorEntradaNeuronio2);

		// Calcula valores direcao camada de saida Sul = 0, Leste = 1, Oeste = 2
		int valorSaidaDirecaoNeuronio0 = rede.getNeuronios().get(5).calculaY(valorEntradaNeuronio0,
				valorEntradaNeuronio1, valorEntradaNeuronio2);
		int valorSaidaDirecaoNeuronio1 = rede.getNeuronios().get(6).calculaY(valorEntradaNeuronio0,
				valorEntradaNeuronio1, valorEntradaNeuronio2);
		int valorSaidaDirecaoNeuronio2 = rede.getNeuronios().get(7).calculaY(valorEntradaNeuronio0,
				valorEntradaNeuronio1, valorEntradaNeuronio2);

		Direcao direcao = null;

		// Verifica direcao
		if (valorSaidaDirecaoNeuronio0 > valorSaidaDirecaoNeuronio1
				&& valorSaidaDirecaoNeuronio0 > valorSaidaDirecaoNeuronio2) {
			direcao = Direcao.SUL;

		} else if (valorSaidaDirecaoNeuronio1 > valorSaidaDirecaoNeuronio0
				&& valorSaidaDirecaoNeuronio1 > valorSaidaDirecaoNeuronio2) {
			// evita loop
			if (ultimaDirecao != Direcao.OESTE) {
				direcao = Direcao.LESTE;

			} else if (valorSaidaDirecaoNeuronio0 > valorSaidaDirecaoNeuronio2) {
				direcao = Direcao.SUL;

			} else {
				direcao = Direcao.OESTE;
			}

		} else if (valorSaidaDirecaoNeuronio2 > valorSaidaDirecaoNeuronio0
				&& valorSaidaDirecaoNeuronio2 > valorSaidaDirecaoNeuronio1) {
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

		// Imprime movimento
		imprimeMovimento(direcao, moveComPulo, iteracao);

		// Verificar se movimento e valido
		if (getTabuleiro().movimentoValido(direcao, moveComPulo)) {
			populacao[32] = populacao[32] + 2;
			
			// Verifica movimento (anda ou pula)
			if (moveComPulo) {
				getTabuleiro().moverAgenteComPulo(direcao);
			} else if (getTabuleiro().getObjetoPelaDirecao(getCoordenadas(), direcao)
					.getTipo() == TipoDeObjeto.SACO_DE_MOEDAS) {
				getTabuleiro().moverAgenteEPegarSacoDeMoedas(direcao);
			} else {
				getTabuleiro().moverAgente(direcao);
			}

			// Imprime tabuleiro
			getTabuleiro().imprime();
			/*try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		} else {
			setEstadoAtual(EstadoDoAgente.GAME_OVER);
			populacao[32] = populacao[32] - 1;
		}

		return direcao;
	}

	private void imprimeMovimento(Direcao direcao, boolean moveComPulo, int iteracao) {
		System.out.println("Iteração " + iteracao + " - Movimento efetuado pelo agente: Direção "
				+ (direcao == null ? "inválida" : direcao.toString()) + (moveComPulo ? " com pulo" : " sem pulo")
				+ "\n\n");
	}

	private void setPesosNeuronios(double[] populacao) {
		int j = 0;
		for (int i = 0; i < 8; i++) {
			rede.getNeuronios().get(i).setW0(populacao[j]);
			rede.getNeuronios().get(i).setW1(populacao[j + 1]);
			rede.getNeuronios().get(i).setW2(populacao[j + 2]);
			rede.getNeuronios().get(i).setW3(populacao[j + 3]);
			j += 4;
		}

	}

	public void genetico() {
		popular();
	}

	public void proximaGeracao() {
		double[] melhor1 = populacao[0];
		double[] melhor2 = populacao[1];
		
		for (int i = 2; i < populacao.length ; i++) {
			if (populacao[i][32] > melhor1[32]) {
				if (melhor1[32] > melhor2[32]) {
					melhor2 = melhor1;
				}
				melhor1 = populacao[i];
			} else if (populacao[i][32] > melhor2[32]) {
				if (melhor2[32] > melhor1[32]) {
					melhor1 = melhor2;
				}
				melhor2 = populacao[i];
			}
		}

		cruzar(melhor1, melhor2);
		mutar(melhor1, melhor2);
	}

	public void popular() {
		double leftLimit = -1;
		double rightLimit = 1;

		for (int i = 0; i < populacao.length; i++) {
			for (int j = 0; j < populacao[0].length-1; j++) {
				populacao[i][j] = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
			}
		}

		//atual = populacao[0];
		//melhor1 = populacao[1];
		//melhor2 = populacao[2];
		// melhor[31] = 1000;
	}

	public void mutar(double[] melhor1, double[] melhor2) {
		double leftLimit = -1;
		double rightLimit = 1;

		melhor1[new Random().nextInt(32)] = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
		melhor1[new Random().nextInt(32)] = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
		
		melhor2[new Random().nextInt(32)] = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);	
		melhor2[new Random().nextInt(32)] = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);		
	}

	public void cruzar(double[] melhor1, double[] melhor2) {
		double[][] novaPopulacao = new double[populacao.length][populacao[0].length];
		novaPopulacao[0] = melhor1;
		novaPopulacao[1] = melhor2;
		
		for (int i = 2; i < populacao.length; i++) {
			for (int j = 0; j < populacao[i].length; j++) {
				int t = populacao[i].length;
				int t1 = new Random().nextInt(t/4);
				int t2 = new Random().nextInt(t/2-t/4) + t/4;
				int t3 = new Random().nextInt((t/2+t/4) - t/2) + t/2;
				
				if (j < t1 || (j >= t2 && j < t3)) {
					novaPopulacao[i][j] = melhor1[j];
				} else {
					novaPopulacao[i][j] = melhor2[j];
				}
			}
		}
		
		populacao = novaPopulacao.clone();
		melhor1 = populacao[0];
		melhor2 = populacao[1];
	}
	
	public void ativarAprendizadoDeMaquina() {
		//int iteracao = 1;
		int iteracoesMax = 100;
		int[][][] resultados = new int[iteracoesMax][populacao.length][3];
		
		for (int iteracao = 0; iteracao < iteracoesMax; iteracao++) {
			
			//para cada populacao, executa
			for(int pop = 0; pop < populacao.length; pop++) {
				//System.out.println("Iteração " + iteracao +" População " + pop + "\n");
				//getTabuleiro().imprime();
				rodarAprendizadoDeMaquina(populacao[pop], iteracao);
				
				//imprimePopulacao(p);
				//System.out.println("Recompensa final: " + p[32] + "\n");
				//imprimeGeracao();
				
				resultados[iteracao][pop][0] = iteracao;
				resultados[iteracao][pop][1] = pop;
				resultados[iteracao][pop][2] = (int) populacao[pop][32];
				
				//System.out.println("Iteração " + iteracao + ", População " + pop + " Recompensa: "  + populacao[pop][32]);
				
				getTabuleiro().resetTabuleiroOpcao1();
			}
			proximaGeracao();
		}
		
		for (int i = 0; i < resultados.length; i++) {
			for (int j = 0; j < populacao.length; j++) {
				System.out.println("Iteração " + resultados[i][j][0] + ", População " + resultados[i][j][1] + " Recompensa: "  + resultados[i][j][2]);
			}
		}
	}

	public void rodarAprendizadoDeMaquina(double[] populacao, int iteracao) {
		while (estadoAtual != EstadoDoAgente.FORA_DO_LABIRINTO && estadoAtual != EstadoDoAgente.GAME_OVER) {
			Direcao ultimaDirecao = null;

			while (estadoAtual == EstadoDoAgente.PROCURANDO_PORTA) {
				ultimaDirecao = rodarRedeNeural(ultimaDirecao, iteracao, populacao);
				//getTabuleiro().imprime();
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
	}

	private void imprimePopulacao(double[] p) {
		StringBuilder sb = new  StringBuilder();
		sb.append("[");
		for(double d : p) {
			sb.append(d);
			sb.append(',');
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		
		System.out.println(sb.toString());
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
				// Aqui devera utilizar a rede neural, criado metodo abaixo para testar
				// elementos do tabuleiro.
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
			System.out.println("Labirinto concluï¿½do com sucesso!");
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
