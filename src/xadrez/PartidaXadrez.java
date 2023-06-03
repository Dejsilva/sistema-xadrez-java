package xadrez;

import jogoTabuleiro.Peca;
import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.pecas.King;
import xadrez.pecas.Torre;

public class PartidaXadrez {
		
	private Tabuleiro tabuleiro;
	
	
	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		inicialConf();
	}
	
	public PecaXadrez[][] getPecas() {
		PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for(int i=0; i<tabuleiro.getLinhas(); i++) {
			for(int j = 0; j <tabuleiro.getColunas(); j++) {
				mat[i][j] =  (PecaXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}
	
	public PecaXadrez executeMovimento(XadrezPosicao origemPosicao, XadrezPosicao posicaoDestino) {
		Posicao origem = origemPosicao.posicionarPosicao();
		Posicao destino = posicaoDestino.posicionarPosicao();
		validarPosicaoOrigem(origem);
		Peca capturarPeca = fazerMover(origem, destino);
		return (PecaXadrez)capturarPeca;
	}
	
	private Peca fazerMover(Posicao origem, Posicao destino) {
		Peca p = tabuleiro.removerPeca(origem);
		Peca capturaPeca = tabuleiro.removerPeca(destino);
		tabuleiro.reposicaoPeca(p, destino);
		return capturaPeca;
	}
	
	private void validarPosicaoOrigem (Posicao posicao) {
		if(!tabuleiro.haUmaPeca(posicao)) {
			throw new XadrezException("não existe peça na posição de origem");
		}
		if(tabuleiro.peca(posicao).seExisteMovimentoPossivel()) {
			throw new XadrezException("Não existe movimentos especificos para a peça escolido ");		
		}
	}
	
	private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.reposicaoPeca(peca, new XadrezPosicao(coluna, linha).posicionarPosicao());
	}
	
	private void inicialConf() {
		colocarNovaPeca('c', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('c', 2, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('d', 2, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('e', 2, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('e', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('d', 1, new King(tabuleiro, Cor.BRANCO));

		colocarNovaPeca('c', 7, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('c', 8, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('d', 7, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('e', 7, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('e', 8, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('d', 8, new King(tabuleiro, Cor.PRETO));
	}
}
		
	
