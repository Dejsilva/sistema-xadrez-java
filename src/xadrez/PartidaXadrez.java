package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jogoTabuleiro.Peca;
import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.King;
import xadrez.pecas.Peao;
import xadrez.pecas.Torre;

public class PartidaXadrez {
		
	
	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean check;
	private boolean checkMate;	
	
	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturadas = new ArrayList<>();

	
	
	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cor.BRANCO;
		inicialConf();
	}
	
	public int getTurno() {
		return turno;
	}
	
	public Cor getJogadorAtual() {
		return jogadorAtual;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
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
	
	public boolean[][] possiveisMoves(XadrezPosicao origemPosicao) {
		Posicao posicao = origemPosicao.posicionarPosicao();
		validarPosicaoOrigem(posicao);
		return tabuleiro.peca(posicao).possiveisMoves();
	}
	
	public PecaXadrez executeMovimento(XadrezPosicao origemPosicao, XadrezPosicao posicaoDestino) {
		Posicao origem = origemPosicao.posicionarPosicao();
		Posicao destino = posicaoDestino.posicionarPosicao();
		validarPosicaoOrigem(origem);
		validarDestinoPosicao(origem, destino);
		Peca capturarPeca = fazerMover(origem, destino);
		
		if(testarCheck(jogadorAtual)) {
			desfazerMovimento(origem, destino, capturarPeca);
			throw new XadrezException("Você não pode se colocar em check");
			
		}
		
		check = (testarCheck(oponente(jogadorAtual))) ? true : false ;
		
		if(testarCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		}
		else {
			proximoTurno();
		}
		
		return (PecaXadrez)capturarPeca;
	}
	
	private Peca fazerMover(Posicao origem, Posicao destino) {
		PecaXadrez p = (PecaXadrez)tabuleiro.removerPeca(origem);
		p.incrementarMovimentos();
		Peca capturarPeca = tabuleiro.removerPeca(destino);
		tabuleiro.reposicaoPeca(p, destino);
		
		if(capturarPeca != null) {
			pecasNoTabuleiro.remove(capturarPeca);
			pecasCapturadas.add(capturarPeca);
		}
		
		return capturarPeca;
	}
	
	private void desfazerMovimento(Posicao origem, Posicao destino, Peca capturarPeca) {
		PecaXadrez p = (PecaXadrez)tabuleiro.removerPeca(destino);
		p.decrementarMovimentos();
		tabuleiro.reposicaoPeca(p, origem);
		
		if(capturarPeca != null) {
			tabuleiro.reposicaoPeca(capturarPeca, destino);
			pecasCapturadas.remove(capturarPeca);
			pecasNoTabuleiro.add(capturarPeca);
		}
	}
	
	private void validarPosicaoOrigem (Posicao posicao) {
		if(!tabuleiro.haUmaPeca(posicao)) {
			throw new XadrezException("não existe peça na posição de origem");
		}
		if(jogadorAtual != ((PecaXadrez)tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezException("A peça escolhida não é sua");
		}
		if(!tabuleiro.peca(posicao).seExisteMovimentoPossivel()) {
			throw new XadrezException("Não existe movimentos especificos para a peça escolhida ");		
		}
	}
	
	private void validarDestinoPosicao(Posicao origem, Posicao destino) {
		if (!tabuleiro.peca(origem).possivelMove(destino)) {
			throw new XadrezException("A peça escolhida não pode se mover para a posição de destino");
		}
	}
	
	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}
	
	private Cor oponente(Cor cor) {
		return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}
	
	private PecaXadrez king(Cor cor) {
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
		for (Peca p : list) {
			if(p instanceof King) {
				return (PecaXadrez)p;
			}
		}
		throw new IllegalStateException("Não existe o rei da cor " + cor + "no tabuleiro");
	}
	
	private boolean testarCheck(Cor cor) {
		Posicao kingPosicao = king(cor).getXadrezPosicao().posicionarPosicao();
		List<Peca> oponentePecas = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == oponente(cor)).collect(Collectors.toList());
		for(Peca p : oponentePecas) {
			boolean[][] mat = p.possiveisMoves();
			if(mat[kingPosicao.getLinha()][kingPosicao.getColuna()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testarCheckMate(Cor cor) {
		if(!testarCheck(cor)) {
			return false;
		}
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
		for(Peca p : list) {
			boolean mat[][] = p.possiveisMoves();
			for(int i = 0;i<tabuleiro.getLinhas(); i++) {
				for(int j=0;j < tabuleiro.getColunas();j++) {
					if(mat[i][j]) {
						Posicao origem = ((PecaXadrez)p).getXadrezPosicao().posicionarPosicao();
						Posicao destino = new Posicao(i, j);
						Peca caputraPeca = fazerMover(origem, destino);
						boolean testarCheck = testarCheck(cor);
						desfazerMovimento(origem, destino, caputraPeca);
						if(!testarCheck) {
						return false;						
					}
				}
			}
			}
		}
		return true;
	}
	
	private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.reposicaoPeca(peca, new XadrezPosicao(coluna, linha).posicionarPosicao());
		pecasNoTabuleiro.add(peca);
	}
	
	private void inicialConf() {
		colocarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('e', 1, new King(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO));
	
		
		colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('e', 8, new King(tabuleiro, Cor.PRETO));
		colocarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO));
		colocarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO));
		colocarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO));
		colocarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO));
		colocarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO));
		colocarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO));
		colocarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO));
		colocarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO));
		
	}
}
		
	
