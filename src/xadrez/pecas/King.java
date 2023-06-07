package xadrez.pecas;

import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class King extends PecaXadrez {

	public King(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
		
	}
	@Override
	public String toString() {
		return "K";
	}
	
	private boolean podeMover (Posicao posicao) {
		PecaXadrez p = (PecaXadrez)getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
	}
	
	@Override
	public boolean[][] possiveisMoves() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao p = new Posicao(0,0);
		
		
		//acima
		p.setValues(posicao.getLinha() -1, posicao.getColuna());
		if(getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//abaixo
		p.setValues(posicao.getLinha() + 1, posicao.getColuna());
		if(getTabuleiro().posicaoExistente(p) && podeMover(p)) {
		mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//esquerda
		p.setValues(posicao.getLinha(), posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExistente(p) && podeMover(p)) {
		mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//direita
		p.setValues(posicao.getLinha(), posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExistente(p) && podeMover(p)) {
		mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//nw
		p.setValues(posicao.getLinha() -1, posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExistente(p) && podeMover(p)) {
		mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//ne
		p.setValues(posicao.getLinha() -1, posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExistente(p) && podeMover(p)) {
		mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//sw
		p.setValues(posicao.getLinha() + 1, posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExistente(p) && podeMover(p)) {
		mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//se
		p.setValues(posicao.getLinha() + 1, posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExistente(p) && podeMover(p)) {
		mat[p.getLinha()][p.getColuna()] = true;
		}
				
		
		
		return mat;
	}
}
