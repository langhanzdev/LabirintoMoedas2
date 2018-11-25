package controller;

import model.Agente;
import model.Tabuleiro;

public class Labirinto {
	
	public static void main(String[] args) {
		Tabuleiro tabuleiro = new Tabuleiro(1);
		tabuleiro.imprimeTabuleiro();
		tabuleiro.imprimirPontuacao();
		tabuleiro.imprimeTabuleiroVisivelPeloAgente();
		
		Agente agente = (Agente) tabuleiro.getAgente();
		//agente.ativarModoAutonomo();
		agente.ativarAprendizadoDeMaquina();
	}
}
