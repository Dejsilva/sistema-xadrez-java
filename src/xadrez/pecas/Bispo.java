package xadrez.pecas;

import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Bispo extends PecaXadrez {

	public Bispo(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean[][] possiveisMoves() {
boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao p = new Posicao(0, 0); 
		
		//nW
		p.setValues(posicao.getLinha() - 1, posicao.getColuna() - 1);
		while (getTabuleiro().posicaoExistente(p) && !getTabuleiro().haUmaPeca(p)){
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() - 1, p.getColuna() - 1);
		
		}
		if(getTabuleiro().posicaoExistente(p) && seExistePecaAdversaria(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//ne
		p.setValues(posicao.getLinha() - 1 , posicao.getColuna() + 1);
		while (getTabuleiro().posicaoExistente(p) && !getTabuleiro().haUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() - 1 , p.getColuna() + 1);
		
		}
		if(getTabuleiro().posicaoExistente(p) && seExistePecaAdversaria(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//se
			p.setValues(posicao.getLinha() + 1, posicao.getColuna() + 1);
			while (getTabuleiro().posicaoExistente(p) && !getTabuleiro().haUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() + 1, p.getColuna() + 1);
				
			}
			if(getTabuleiro().posicaoExistente(p) && seExistePecaAdversaria(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			}
				
		//sw
			p.setValues(posicao.getLinha() + 1, posicao.getColuna() - 1);
			while (getTabuleiro().posicaoExistente(p) && !getTabuleiro().haUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() + 1, p.getColuna() - 1);
		
			}
				if(getTabuleiro().posicaoExistente(p) && seExistePecaAdversaria(p)) {
					mat[p.getLinha()][p.getColuna()] = true;
				}
		return mat;
	}
	
	@Override
	public String toString() {
		return "B";
	}
}	