package xadrez.pecas;

import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;

public class Peao extends PecaXadrez{
	
	private PartidaXadrez partidaXadrez;

	public Peao(Tabuleiro tabuleiro, Cor cor, PartidaXadrez partidaXadrez) {
		super(tabuleiro, cor);
		this.partidaXadrez = partidaXadrez;
	}

	@Override
	public boolean[][] possiveisMoves() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao p = new Posicao(0, 0); 
		
		if(getCor() == Cor.BRANCO) {
			p.setValues(posicao.getLinha() - 1, posicao.getColuna());
			if(getTabuleiro().posicaoExistente(p) && !getTabuleiro().haUmaPeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
				p.setValues(posicao.getLinha() - 2, posicao.getColuna());
				Posicao p2 = new Posicao(posicao.getLinha() -1, posicao.getColuna());
				if(getTabuleiro().posicaoExistente(p) && !getTabuleiro().haUmaPeca(p) && getTabuleiro().posicaoExistente(p2) && !getTabuleiro().haUmaPeca(p2) && getContagemMovimentos() == 0) {
					mat[p.getLinha()][p.getColuna()] = true;
				}
				p.setValues(posicao.getLinha() - 1, posicao.getColuna() - 1);
				if(getTabuleiro().posicaoExistente(p) &&  seExistePecaAdversaria(p)){
					mat[p.getLinha()][p.getColuna()] = true;
				}
				p.setValues(posicao.getLinha() - 1, posicao.getColuna() + 1);
				if(getTabuleiro().posicaoExistente(p) &&  seExistePecaAdversaria(p)){
					mat[p.getLinha()][p.getColuna()] = true;
				}
				
				// #especialmove em passant branco
				if(posicao.getLinha() == 3) {
					Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
					if(getTabuleiro().posicaoExistente(esquerda) && seExistePecaAdversaria(esquerda) && getTabuleiro().peca(esquerda) == partidaXadrez.getEnPassantVulnerable()) {
						mat[esquerda.getLinha() - 1][esquerda.getColuna()] = true;
					}
					Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
					if(getTabuleiro().posicaoExistente(direita) && seExistePecaAdversaria(direita) && getTabuleiro().peca(direita) == partidaXadrez.getEnPassantVulnerable()) {
						mat[direita.getLinha() - 1][direita.getColuna()] = true;
					}
				}
		}
		else {
			p.setValues(posicao.getLinha() + 1, posicao.getColuna());
			if(getTabuleiro().posicaoExistente(p) && !getTabuleiro().haUmaPeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
				p.setValues(posicao.getLinha() + 2, posicao.getColuna());
				Posicao p2 = new Posicao(posicao.getLinha() +1, posicao.getColuna());
				if(getTabuleiro().posicaoExistente(p) && !getTabuleiro().haUmaPeca(p) && getTabuleiro().posicaoExistente(p2) && !getTabuleiro().haUmaPeca(p2) && getContagemMovimentos() == 0) {
					mat[p.getLinha()][p.getColuna()] = true;
				}
				p.setValues(posicao.getLinha() + 1, posicao.getColuna() - 1);
				if(getTabuleiro().posicaoExistente(p) &&  seExistePecaAdversaria(p)){
					mat[p.getLinha()][p.getColuna()] = true;
				}
				p.setValues(posicao.getLinha() + 1, posicao.getColuna() - 1);
				if(getTabuleiro().posicaoExistente(p) &&  seExistePecaAdversaria(p)){
					mat[p.getLinha()][p.getColuna()] = true;
				}
				
				// #especialmove em passant preto
				if(posicao.getLinha() == 4) {
					Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
					if(getTabuleiro().posicaoExistente(esquerda) && seExistePecaAdversaria(esquerda) && getTabuleiro().peca(esquerda) == partidaXadrez.getEnPassantVulnerable()) {
						mat[esquerda.getLinha() + 1][esquerda.getColuna()] = true;
					}
					Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
					if(getTabuleiro().posicaoExistente(direita) && seExistePecaAdversaria(direita) && getTabuleiro().peca(direita) == partidaXadrez.getEnPassantVulnerable()) {
						mat[direita.getLinha() + 1][direita.getColuna()] = true;
					}
				}
			
		}
		
		
		return mat;
	}
	
	
	@Override
	public String toString() {
		return "P";
	}

}
