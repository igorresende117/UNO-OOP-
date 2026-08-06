package jogadores;

import java.util.ArrayList;
import java.util.Scanner;

import baralhos.Baralho;
import cartas.Carta;
import cartas.CartaCOMUM;
import cartas.CartaUNO;
import modelo.Jogo;

//Representa um jogador e atua como um nó na Doubly Linked List
public class Jogador {
    private String nome;
    private ArrayList<Carta> mao = new ArrayList<>();

    //Flags de controle para travar ações repetidas no mesmo turno
    private boolean jaComprouNesteTurno;
    private boolean jaJogouNesteTurno;

    //Ponteiros da lista duplamente encadeada
    private Jogador jogadorEsq;
    private Jogador jogadorDir;

    public Jogador(String nome) {
        this.nome = nome;
        this.jaComprouNesteTurno = false;
        this.jaJogouNesteTurno = false;
    }

    public void setEsq(Jogador jogador) {
        this.jogadorEsq = jogador;
    }

    public void setDir(Jogador jogador) {
        this.jogadorDir = jogador;
    }

    public Jogador getEsq() {
        return jogadorEsq;
    }

    public Jogador getDir() {
        return jogadorDir;
    }

    public String getNome() { return this.nome; }

    public ArrayList<Carta> getMao() { return this.mao; }

    public void comprarCarta(Baralho pilhaCompra) {
        this.mao.add(pilhaCompra.pop());
    }

    //Retorna Array de índices das cartas jogáveis da mão do jogador
    private ArrayList<Integer> separarCartasJogaveis(Jogo contextoJogo) {
        Carta cartaTopo = contextoJogo.getPilhaDescarte().peek();
        ArrayList<Integer> jogaveis = new ArrayList<>();
        int i = 0;

        for(Carta c : this.mao) {
            if(c.servePJ(cartaTopo, contextoJogo)) {
                jogaveis.add(i);
            }
            i++;
        }
        return jogaveis;
    }

    //Interação do jogador durante o seu turno
    public void jogarTurno(Jogo contextoJogo) {
        ArrayList<Integer> jogaveis = separarCartasJogaveis(contextoJogo);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Mão atual: ");
        int i = 1;

        for(Carta c : this.mao) {
            if(c instanceof CartaUNO cUNO) {
                System.out.println("Carta " + i + ":");
                System.out.println("Número: " + cUNO.getNum());
                System.out.println("Cor: " + cUNO.getCor());
                System.out.println("Ação: " + cUNO.getAcao());
                System.out.println("\n");
            }
            if(c instanceof CartaCOMUM cCOMUM) {
                System.out.println("Carta " + i + ":");
                System.out.println("Número: " + cCOMUM.getNum());
                System.out.println("Naipe: " + cCOMUM.getNaipe());
                System.out.println("Ação: " + cCOMUM.getAcao());
                System.out.println("\n");
            }
            i++;
        }

        System.out.println("Jogar, Comprar ou Passar (J/C/P): ");
        String resposta = scanner.nextLine();

        //Laço principal do turno: só encerra se o jogador passar a vez (P) TENDO JOGADO ou COMPRADO
        while(!resposta.equalsIgnoreCase("P") || (!this.jaJogouNesteTurno && !this.jaComprouNesteTurno)) {

            if (resposta.equalsIgnoreCase("P")) {
                //Se tentar passar a vez sem ter feito nada, o laço barra e pede nova ação
                System.out.println("Você precisa jogar ou comprar antes de passar a vez!");
            }
            else if (resposta.equalsIgnoreCase("C")) {
                //Não pode comprar se já tiver comprado ou jogado neste turno
                if (this.jaComprouNesteTurno || this.jaJogouNesteTurno) {
                    System.out.println("Você não pode comprar agora!");
                } else {
                    if(contextoJogo.getPilhaCompra().isEmpty()) {
                        contextoJogo.reabastecerBaralho();
                    }

                    this.comprarCarta(contextoJogo.getPilhaCompra());

                    //Recalcula as cartas jogáveis caso a carta comprada sirva na mesa
                    jogaveis = separarCartasJogaveis(contextoJogo);
                    this.jaComprouNesteTurno = true;
                    System.out.println("Carta comprada!");
                }
            }
            else if (resposta.equalsIgnoreCase("J")) {
                //Impede que o jogador jogue mais de uma carta por turno
                if (this.jaJogouNesteTurno) {
                    System.out.println("Você já jogou neste turno! Digite 'P' para passar a vez.");
                }
                else if (jogaveis.isEmpty() && !this.jaComprouNesteTurno) {
                    System.out.println("Você não tem cartas compatíveis com o topo, precisa comprar (C)!");
                }
                else if (jogaveis.isEmpty() && this.jaComprouNesteTurno) {
                    System.out.println("Você não tem cartas compatíveis com o topo e já comprou, digite 'P' para passar!");
                }
                else {
                    System.out.println("Escolha uma carta jogável: ");

                    //Imprime as cartas que servem na mesa
                    for(int idx : jogaveis) {
                        System.out.println("Carta " + idx + ":");
                        this.mao.get(idx).imprimirCarta();
                    }

                    int opcao = scanner.nextInt();
                    scanner.nextLine();

                    //Trava se o usuário digitar uma opção inválida
                    while (!jogaveis.contains(opcao)) {
                        System.out.println("Opção inválida! Escolha um dos números correspondentes às cartas acima:");
                        opcao = scanner.nextInt();
                        scanner.nextLine();
                    }

                    //Puxa a carta escolhida da mão do jogador
                    Carta cartaJogada = this.mao.remove(opcao);

                    //Coloca a carta na mesa ANTES de aplicar o efeito (para cartas de compra não quebrarem o reabastecimento)
                    contextoJogo.getPilhaDescarte().push(cartaJogada);

                    //Chama o efeito correto seja UNO ou COMUM
                    cartaJogada.aplicaEfeito(contextoJogo);

                    this.jaJogouNesteTurno = true;
                    System.out.println("Carta jogada! Digite 'P' para passar a vez.");
                }
            }
            else {
                System.out.println("Resposta inválida!");
            }

            //Pede um novo input se o turno ainda não foi finalizado corretamente
            if (!resposta.equalsIgnoreCase("P") || (!this.jaJogouNesteTurno && !this.jaComprouNesteTurno)) {
                System.out.println("Jogar, Comprar ou Passar (J/C/P): ");
                resposta = scanner.nextLine();
            }
        }

        //Reseta as flags para o próximo turno desse mesmo jogador
        this.jaComprouNesteTurno = false;
        this.jaJogouNesteTurno = false;
    }

    //Imprime todas as cartas atuais na mão do jogador
    public void imprimirMao() {
        System.out.println("Mão do(a) jogador(a) " + this.nome + ":");
        int cont = 1;

        for(Carta c : this.mao) {
            if(c instanceof CartaUNO cUNO) {
                System.out.println("Carta " + cont + ": Cor: " + cUNO.getCor() + " | Número: " + cUNO.getNum() + " | Ação: " + cUNO.getAcao());
            }
            if(c instanceof CartaCOMUM cCOMUM) {
                System.out.println("Carta " + cont + ": Naipe: " + cCOMUM.getNaipe() + " | Número: " + cCOMUM.getNum() + " | Ação: " + cCOMUM.getAcao());
            }
            cont++;
        }
        System.out.println("-----------------------------------");
    }
}