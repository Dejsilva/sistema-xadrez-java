package xadrez;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jogoTabuleiro.Peca;
import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Cavalo;
import xadrez.pecas.King;
import xadrez.pecas.Peao;
import xadrez.pecas.Rainha;
import xadrez.pecas.Torre;

public class PartidaXadrez {
		
	
	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean check;
	private boolean checkMate;	
	private PecaXadrez enPassantVulnerable;
	private PecaXadrez promocao;
	
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
	
	public PecaXadrez getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	
	public PecaXadrez getPromocao() {
		return promocao;
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
		
		PecaXadrez moverPeca = (PecaXadrez)tabuleiro.peca(destino);
		
		//#especial movimento promoção
		promocao = null;
		if(moverPeca instanceof Peao) {
			if((moverPeca.getCor() == Cor.BRANCO && destino.getLinha() == 0) ||  (moverPeca.getCor() == Cor.PRETO && destino.getLinha() == 7)) {
				promocao = (PecaXadrez)tabuleiro.peca(destino);
				promocao = substituirPecaPromo("R");				
			}	
		}
		
		
		check = (testarCheck(oponente(jogadorAtual))) ? true : false ;
		
		if(testarCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		}
		else {
			proximoTurno();
		}
		
		//#especial movimento Passant
		if(moverPeca instanceof Peao && (destino.getLinha() == origem.getLinha() -2 || destino.getLinha() == origem.getLinha() +2)) {
			enPassantVulnerable = moverPeca;
		}
		else {
			enPassantVulnerable = null;
		}
		return (PecaXadrez)capturarPeca;
	}
	
	public PecaXadrez substituirPecaPromo(String tipo) {
		if(promocao == null) {
			throw new IllegalStateException("não há peça para ser promovida");
		}
		if(!tipo.equals("B") && !tipo.equals("C")  && !tipo.equals("T")  && !tipo.equals("R")) {
			return promocao;
		}
		
		Posicao pos = promocao.getXadrezPosicao().posicionarPosicao();
		Peca p = tabuleiro.removerPeca(pos);
		pecasNoTabuleiro.remove(p);
		
		PecaXadrez novaPeca = novaPeca(tipo, promocao.getCor());
		tabuleiro.reposicaoPeca(novaPeca, pos);
		pecasNoTabuleiro.add(novaPeca);
		
		return novaPeca;
	}
	
	private PecaXadrez novaPeca (String tipo, Cor cor)
	{
		if(tipo.equals("B")) return new Bispo(tabuleiro, cor);
		if(tipo.equals("C")) return new Cavalo(tabuleiro, cor);
		if(tipo.equals("R")) return new Rainha(tabuleiro, cor);
		return new Torre(tabuleiro, cor);
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
		
		// #especial roque pequeno
		if(p instanceof King && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(origemT);
			tabuleiro.reposicaoPeca(torre, destinoT);
			torre.incrementarMovimentos();
		}
		
		// #especial grande roque
				if(p instanceof King && destino.getColuna() == origem.getColuna() - 2) {
					Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
					Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
					PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(origemT);
					tabuleiro.reposicaoPeca(torre, destinoT);
					torre.incrementarMovimentos();
				}
				
				//#especial movimento en passant
				if(p instanceof Peao) {
					if(origem.getColuna() != destino.getColuna() && capturarPeca == null) {
						Posicao peaoPosicao;
						if(p.getCor() == Cor.BRANCO) {
							peaoPosicao = new Posicao(destino.getLinha() + 1, destino.getColuna());
						}
						else {
							peaoPosicao = new Posicao(destino.getLinha() - 1, destino.getColuna());
						}
						capturarPeca = tabuleiro.removerPeca(peaoPosicao);
						pecasCapturadas.add(capturarPeca);
						pecasNoTabuleiro.remove(capturarPeca);
					}
					
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
		
		// #especial roque pequeno
				if(p instanceof King && destino.getColuna() == origem.getColuna() + 2) {
					Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
					Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
					PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(destinoT);
					tabuleiro.reposicaoPeca(torre, origemT);
					torre.decrementarMovimentos();
				}
				
				// #especial grande roque
						if(p instanceof King && destino.getColuna() == origem.getColuna() - 2) {
							Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
							Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
							PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(destinoT);
							tabuleiro.reposicaoPeca(torre, origemT);
							torre.decrementarMovimentos();
						}
						
						//#especial movimento en passant
						if(p instanceof Peao) {
							if(origem.getColuna() != destino.getColuna() && capturarPeca == enPassantVulnerable) {
								PecaXadrez peao = (PecaXadrez)tabuleiro.removerPeca(destino);
								Posicao peaoPosicao;
								if(p.getCor() == Cor.BRANCO) {
									peaoPosicao = new Posicao(3 , destino.getColuna());
								}
								else {
									peaoPosicao = new Posicao(4 , destino.getColuna());
								}
								tabuleiro.reposicaoPeca(peao, peaoPosicao);
							}
							
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
		colocarNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('e', 1, new King(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO, this));
	
		
		colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
		colocarNovaPeca('e', 8, new King(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));	
		colocarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO, this));
		
	}
}
		
	
