package cartas;

import cartas.enums.AcaoCOMUM;
import cartas.enums.Naipe;
import modelo.Jogo;

//Representa as cartas do baralho tradicional
public class CartaCOMUM extends Carta {
    private Naipe naipe;
    private AcaoCOMUM acao;

    //Construtor para cartas numeradas (Ás a 10)
    public CartaCOMUM(Naipe naipe, int numero) {
        this.naipe = naipe;
        this.numero = numero;
        this.acao = null;
    }

    //Construtor para figuras (Valete, Dama, Rei)
    public CartaCOMUM(Naipe naipe, AcaoCOMUM acao) {
        this.naipe = naipe;
        this.numero = -1; //-1 indica que a carta não possui valor numérico
        this.acao = acao;
    }

    //Construtor para Curingas (Jokers)
    public CartaCOMUM(AcaoCOMUM acao) {
        this.naipe = null; //Curingas nascem sem naipe definido
        this.numero = -1;
        this.acao = acao;
    }

    public boolean servePJ(Carta cartaTopo, Jogo contextoJogo) {
        //Curingas podem ser jogados em cima de qualquer carta
        if(this.acao == AcaoCOMUM.JOKER_PRETO || this.acao == AcaoCOMUM.JOKER_VERMELHO) return true;

        //Downcasting seguro para comparar atributos específicos do baralho comum
        if(cartaTopo instanceof CartaCOMUM topoCOMUM){
            if(this.naipe == topoCOMUM.getNaipe() ||
                    this.naipe == contextoJogo.getNaipeAtualAtivo() || //Verifica o naipe escolhido após um Curinga
                    (this.numero > -1 && this.numero == topoCOMUM.getNum()) ||
                    (this.acao != null && this.acao == topoCOMUM.getAcao()))
                return true;
        }
        return false;
    }

    public void aplicaEfeito(Jogo contextoJogo) {
        //Atualiza o naipe da mesa automaticamente para cartas que não são curingas
        if (this.acao != AcaoCOMUM.JOKER_PRETO && this.acao != AcaoCOMUM.JOKER_VERMELHO) {
            contextoJogo.setNaipeAtualAtivo(this.getNaipe());
        }

        if (this.acao == AcaoCOMUM.VALETE) {
            //Valete age como Pular: pula a vez do alvo
            contextoJogo.passarTurno();
        }
        else if (this.acao == AcaoCOMUM.DAMA) {
            //Dama age como Inverter. Regra especial: com 2 jogadores, inverter vira pular
            if (contextoJogo.getLista().getQuant() == 2) {
                contextoJogo.passarTurno();
            } else {
                contextoJogo.setSentidoHorario(!contextoJogo.getSentido());
            }
        }
        else if (this.acao == AcaoCOMUM.REI) {
            //Rei age como +2. Passa o turno para o alvo sofrer o efeito e pular a vez
            contextoJogo.passarTurno();

            for(int i = 0; i < 2; i++) {
                //Garante que o baralho não estoure caso haja muitos ataques em sequência
                if(contextoJogo.getPilhaCompra().isEmpty()) {
                    contextoJogo.reabastecerBaralho();
                }
                contextoJogo.getAtual().comprarCarta(contextoJogo.getPilhaCompra());
            }
        }
        else if (this.acao == AcaoCOMUM.JOKER_PRETO || this.acao == AcaoCOMUM.JOKER_VERMELHO) {
            if (this.acao == AcaoCOMUM.JOKER_VERMELHO) {
                //Joker Vermelho age como +4. Foca no alvo para ele comprar 4 e pular a vez
                contextoJogo.passarTurno();

                for(int i = 0; i < 4; i++) {
                    if(contextoJogo.getPilhaCompra().isEmpty()) {
                        contextoJogo.reabastecerBaralho();
                    }
                    contextoJogo.getAtual().comprarCarta(contextoJogo.getPilhaCompra());
                }
            }
        }
    }

    public Naipe getNaipe() {
        return this.naipe;
    }

    public AcaoCOMUM getAcao() {
        return this.acao;
    }

    public void imprimirCarta() {
        //Trata os valores vazios para a impressão ficar limpa
        String n = (this.numero == -1) ? "Nenhum" : String.valueOf(this.numero);
        String na = (this.naipe == null) ? "Nenhum" : this.naipe.toString();
        String a = (this.acao == null) ? "Nenhuma" : this.acao.toString();

        System.out.println("Naipe: " + na + " | Número: " + n + " | Ação: " + a);
    }
}