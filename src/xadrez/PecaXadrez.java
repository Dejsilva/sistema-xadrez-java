package xadrez;

import jogoTabuleiro.Peca;
import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;

public abstract class PecaXadrez extends Peca {
				
	private Cor cor;
	private int contagemMovimentos;

	public PecaXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}
	
	public int getContagemMovimentos() {
		return contagemMovimentos;
	}
	
	public void incrementarMovimentos() {
		contagemMovimentos++;
	}
	
	public void decrementarMovimentos() {
		contagemMovimentos--;
	}
	
	public XadrezPosicao getXadrezPosicao() {
		return  XadrezPosicao.daPosicao(posicao);
	}
	
	protected boolean seExistePecaAdversaria(Posicao posicao) {
		PecaXadrez p = (PecaXadrez)getTabuleiro().peca(posicao);
		return p != null && p.getCor() != cor;
	}

	
}
