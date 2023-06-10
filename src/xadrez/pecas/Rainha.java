package xadrez.pecas;

import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Rainha extends PecaXadrez {

	public Rainha(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "R";
	}
	
	@Override
	public boolean[][] possiveisMoves() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao p = new Posicao(0, 0); 
		
		//pracima
		p.setValues(posicao.getLinha() - 1, posicao.getColuna());
		while (getTabuleiro().posicaoExistente(p) && !getTabuleiro().haUmaPeca(p)){
			mat[p.getLinha()][p.getColuna()] = true;
			p.setLinha(p.getLinha() - 1);
		
		}
		if(getTabuleiro().posicaoExistente(p) && seExistePecaAdversaria(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//esquerda
		p.setValues(posicao.getLinha(), posicao.getColuna() - 1);
		while (getTabuleiro().posicaoExistente(p) && !getTabuleiro().haUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setColuna(p.getColuna() - 1);
		
		}
		if(getTabuleiro().posicaoExistente(p) && seExistePecaAdversaria(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//pradireita
			p.setValues(posicao.getLinha(), posicao.getColuna() + 1);
			while (getTabuleiro().posicaoExistente(p) && !getTabuleiro().haUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setColuna(p.getColuna() + 1);
				
			}
			if(getTabuleiro().posicaoExistente(p) && seExistePecaAdversaria(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			}
				
		//praba
			p.setValues(posicao.getLinha() + 1, posicao.getColuna());
			while (getTabuleiro().posicaoExistente(p) && !getTabuleiro().haUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setLinha(p.getLinha() + 1);
		
			}
				if(getTabuleiro().posicaoExistente(p) && seExistePecaAdversaria(p)) {
					mat[p.getLinha()][p.getColuna()] = true;
				}
				
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
}	
