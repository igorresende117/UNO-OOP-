package cartas;

import cartas.enums.AcaoUNO;
import cartas.enums.Cor;
import modelo.Jogo;

//Representa as cartas do baralho oficial de UNO
public class CartaUNO extends Carta {
    private Cor cor;
    private AcaoUNO acao;

    //Construtor para cartas numeradas (0 a 9)
    public CartaUNO(Cor cor, int numero) {
        this.cor = cor;
        this.numero = numero;
        this.acao = null;
    }

    //Construtor para cartas de ação com cor (Pular, Inverter, +2)
    public CartaUNO(Cor cor, AcaoUNO acao) {
        this.cor = cor;
        this.numero = -1; //-1 indica que a carta não possui valor numérico
        this.acao = acao;
    }

    //Construtor para Curingas (Wild, Wild +4)
    public CartaUNO(AcaoUNO acao) {
        this.cor = null; //Curingas nascem sem cor definida
        this.numero = -1;
        this.acao = acao;
    }

    public boolean servePJ(Carta cartaTopo, Jogo contextoJogo) {
        //Curingas podem ser jogados em cima de qualquer carta
        if(this.acao == AcaoUNO.WILD || this.acao == AcaoUNO.WILD_MAIS_4) return true;

        //Downcasting seguro para comparar atributos específicos do baralho UNO
        if(cartaTopo instanceof CartaUNO topoUNO){
            if(this.cor == topoUNO.getCor() ||
                    this.cor == contextoJogo.getCorAtualAtiva() || //Verifica a cor escolhida após um Curinga
                    (this.numero > -1 && this.numero == topoUNO.getNum()) ||
                    (this.acao != null && this.acao == topoUNO.getAcao()))
                return true;
        }
        return false;
    }

    public void aplicaEfeito(Jogo contextoJogo) {
        //Atualiza a cor da mesa automaticamente para cartas que não são curingas
        if (this.acao != AcaoUNO.WILD && this.acao != AcaoUNO.WILD_MAIS_4) {
            contextoJogo.setCorAtualAtiva(this.getCor());
        }

        if (this.acao == AcaoUNO.PULAR) {
            //Pula a vez do alvo
            contextoJogo.passarTurno();
        }
        else if (this.acao == AcaoUNO.INVERTER) {
            //Regra especial: com 2 jogadores, inverter age como pular
            if (contextoJogo.getLista().getQuant() == 2) {
                contextoJogo.passarTurno();
            } else {
                contextoJogo.setSentidoHorario(!contextoJogo.getSentido());
            }
        }
        else if (this.acao == AcaoUNO.MAIS_2) {
            //Passa o turno para o alvo sofrer o efeito e pular a vez
            contextoJogo.passarTurno();

            for(int i = 0; i < 2; i++) {
                //Garante que o baralho não estoure caso esvazie durante a compra
                if(contextoJogo.getPilhaCompra().isEmpty()) {
                    contextoJogo.reabastecerBaralho();
                }
                contextoJogo.getAtual().comprarCarta(contextoJogo.getPilhaCompra());
            }
        }
        else if (this.acao == AcaoUNO.WILD || this.acao == AcaoUNO.WILD_MAIS_4) {
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            System.out.println("\nEscolha a nova cor (1- VERMELHO | 2- AZUL | 3- VERDE | 4- AMARELO): ");
            int opcao = scanner.nextInt();

            Cor novaCor = switch (opcao) {
                case 2 -> Cor.AZUL;
                case 3 -> Cor.VERDE;
                case 4 -> Cor.AMARELO;
                default -> Cor.VERMELHO;
            };

            contextoJogo.setCorAtualAtiva(novaCor);
            System.out.println("A nova cor da mesa agora é: " + novaCor);

            if (this.acao == AcaoUNO.WILD_MAIS_4) {
                //Foca no alvo para ele comprar 4 e pular a vez
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

    public Cor getCor() {
        return this.cor;
    }

    public AcaoUNO getAcao() {
        return this.acao;
    }

    public void imprimirCarta() {
        //Trata os valores vazios para a impressão ficar limpa
        String c = (this.cor == null) ? "Nenhuma" : this.cor.toString();
        String n = (this.numero == -1) ? "Nenhum" : String.valueOf(this.numero);
        String a = (this.acao == null) ? "Nenhuma" : this.acao.toString();

        System.out.println("Cor: " + c + " | Número: " + n + " | Ação: " + a);
    }
}