package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.XadrezException;
import xadrez.XadrezPosicao;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		PartidaXadrez partidaXadrez = new PartidaXadrez();
		List<PecaXadrez> capturar = new ArrayList<>();
		
		while(!partidaXadrez.getCheckMate()) {
			try {
				UI.clearScreen();
				UI.printPartida(partidaXadrez, capturar);
				System.out.println();
				System.out.print("Origem: ");
				XadrezPosicao origem = UI.leituraPosicao(sc);
				
				boolean[][] possiveisMoveis = partidaXadrez.possiveisMoves(origem);
				UI.clearScreen();
				UI.printTabuleiro(partidaXadrez.getPecas(), possiveisMoveis);
				System.out.println();
				System.out.print("Destino: ");
				XadrezPosicao destino = UI.leituraPosicao(sc);
				
				PecaXadrez capturarPeca = partidaXadrez.executeMovimento(origem, destino);
				
				if(capturarPeca != null) {
					capturar.add(capturarPeca);
				}
				
				if(partidaXadrez.getPromocao() != null) {
					System.out.print("Entre com peça de promoção (C/B/R/T: )");
					String tipo = sc.nextLine();
					partidaXadrez.substituirPecaPromo(tipo);
				}
			}
			catch (XadrezException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
	}
		UI.clearScreen();
		UI.printPartida(partidaXadrez, capturar);

}
}
