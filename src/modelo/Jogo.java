package modelo;

import baralhos.Baralho;
import baralhos.BaralhoCOMUM;
import baralhos.BaralhoUNO;
import baralhos.BaralhoVazio;
import cartas.Carta;
import cartas.CartaCOMUM;
import cartas.CartaUNO;
import cartas.enums.AcaoCOMUM;
import cartas.enums.AcaoUNO;
import cartas.enums.Cor;
import cartas.enums.Naipe;
import jogadores.Jogador;
import jogadores.ListaJogadores;

import java.util.Scanner;

public class Jogo {
    private ListaJogadores lista;
    private Jogador atual; //Ponteiro principal que diz de quem é a vez
    private boolean sentidoHorario;
    private Baralho pilhaCompra;
    private Baralho pilhaDescarte;

    //Contexto da mesa (alterado dinamicamente pelos curingas)
    private Cor corAtualAtiva;
    private Naipe naipeAtualAtivo;
    private ModoJogo modo;

    Scanner scanner = new Scanner(System.in);

    public Jogo() {
        this.lista = new ListaJogadores();
        this.sentidoHorario = true;
        this.iniciarPartida();
    }

    public ListaJogadores getLista() { return this.lista; }

    public Jogador getAtual() { return this.atual; }

    public boolean getSentido() { return this.sentidoHorario; }

    public Baralho getPilhaCompra() { return this.pilhaCompra; }

    public Baralho getPilhaDescarte() { return this.pilhaDescarte; }

    public Cor getCorAtualAtiva() { return this.corAtualAtiva; }

    public Naipe getNaipeAtualAtivo() { return this.naipeAtualAtivo; }

    public ModoJogo getModo() { return this.modo; }

    public void setSentidoHorario(boolean sentido) {
        this.sentidoHorario = sentido;
    }

    public void setCorAtualAtiva(Cor cor) {
        this.corAtualAtiva = cor;
    }

    public void setNaipeAtualAtivo(Naipe naipe) {
        this.naipeAtualAtivo = naipe;
    }

    public void iniciarPartida() {
        CartaUNO cTopoUNO;
        CartaCOMUM cTopoCOMUM;

        //Configuração (Adição/Remoção de Jogadores)
        System.out.println("1- Novo jogador; 2- Remove último jogador; 3- Iniciar" + '\n');
        System.out.println("Digite uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        while(opcao != 3 || this.lista.getQuant() < 2) {
            if(opcao == 1) {
                System.out.println("Digite um nome: ");
                String nome = scanner.nextLine();
                Jogador j = new Jogador(nome);

                this.lista.adicionaJogadorFinal(j);
                this.atual = lista.getHead();
            }
            if(opcao == 2) {
                if(!this.lista.isEmpty()) {
                    System.out.println("Jogador(a) " + this.lista.getTail().getNome() + " removido!");
                    this.lista.removeJogadorFinal();
                } else {
                    System.out.println("Nenhum jogador na lista para remover!");
                }
            }
            if(opcao == 3) {
                System.out.println("É necessário pelo menos 2 jogadores para iniciar a partida!");
            }
            if(opcao > 3 || opcao < 1) {
                System.out.println("Opção inválida!");
            }

            System.out.println("1- Novo jogador; 2- Remove último jogador; 3- Iniciar" + '\n');
            System.out.println("Digite uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();
        }

        //Seleção das Regras e Criação dos Baralhos
        System.out.println("Modo de jogo (1- OFICIAL; 2- CONVENCIONAL): ");
        int modo = scanner.nextInt();
        scanner.nextLine();

        while(modo != 1 && modo != 2) {
            System.out.println("Opção inválida! Modo de jogo (1- OFICIAL; 2- CONVENCIONAL): ");
            modo = scanner.nextInt();
            scanner.nextLine();
        }

        if(modo == 1) {
            this.modo = ModoJogo.OFICIAL;

            this.pilhaCompra = new BaralhoUNO();
            this.pilhaDescarte = new BaralhoVazio();
            this.pilhaDescarte.push(this.pilhaCompra.pop());

            cTopoUNO = (CartaUNO) this.pilhaDescarte.peek();
            this.corAtualAtiva = cTopoUNO.getCor();

            //Distribui 7 cartas para cada jogador inserido na lista
            for(int i = 0; i < this.lista.getQuant(); i++) {
                for(int j = 0; j < 7; j++) {
                    this.atual.comprarCarta(this.pilhaCompra);
                }
                this.atual = atual.getEsq(); //Avança para dar as cartas ao próximo
            }

            //Regra Oficial: o jogo não pode começar com um +4 na mesa
            while(cTopoUNO.getAcao() == AcaoUNO.WILD_MAIS_4) {
                this.pilhaCompra.push(this.pilhaDescarte.pop());
                this.pilhaCompra.embaralha();

                this.pilhaDescarte.push(this.pilhaCompra.pop());
                cTopoUNO = (CartaUNO) this.pilhaDescarte.peek();
            }

            System.out.print("\nCarta inicial na mesa: ");
            cTopoUNO.imprimirCarta();

            //Aplica o efeito da primeira carta no jogador atual
            cTopoUNO.aplicaEfeito(this);

            //Loop Principal da Partida (UNO)
            this.atual.jogarTurno(this);
            while(checarVencedor() == null) {
                passarTurno(); //Gira a lista duplamente encadeada
                System.out.print("\nCarta na mesa: ");
                cTopoUNO.imprimirCarta();
                this.atual.jogarTurno(this);
            }
            System.out.println("\nFIM DE JOGO! O vencedor é: " + checarVencedor().getNome());
        }

        if(modo == 2) {
            this.modo = ModoJogo.CONVENCIONAL;

            this.pilhaCompra = new BaralhoCOMUM();
            this.pilhaDescarte = new BaralhoVazio();
            this.pilhaDescarte.push(this.pilhaCompra.pop());

            cTopoCOMUM = (CartaCOMUM) this.pilhaDescarte.peek();
            this.naipeAtualAtivo = cTopoCOMUM.getNaipe();

            //Distribui 7 cartas para cada jogador inserido na lista
            for(int i = 0; i < this.lista.getQuant(); i++) {
                for(int j = 0; j < 7; j++) {
                    this.atual.comprarCarta(this.pilhaCompra);
                }
                this.atual = atual.getEsq(); //Avança para dar as cartas ao próximo
            }

            //Adaptação: o jogo não pode começar com o Joker Vermelho
            while(cTopoCOMUM.getAcao() == AcaoCOMUM.JOKER_VERMELHO) {
                this.pilhaCompra.push(this.pilhaDescarte.pop());
                this.pilhaCompra.embaralha();

                this.pilhaDescarte.push(this.pilhaCompra.pop());
                //Casting corrigido aqui:
                cTopoCOMUM = (CartaCOMUM) this.pilhaDescarte.peek();
            }

            System.out.print("\nCarta inicial na mesa: ");
            cTopoCOMUM.imprimirCarta();

            //Aplica o efeito da primeira carta no jogador atual
            cTopoCOMUM.aplicaEfeito(this);

            //Loop Principal da Partida (Baralho Comum)
            this.atual.jogarTurno(this);
            while(checarVencedor() == null) {
                passarTurno(); //Gira a lista duplamente encadeada
                System.out.print("\nCarta na mesa: ");
                cTopoCOMUM.imprimirCarta();
                this.atual.jogarTurno(this);
            }
            System.out.println("\nFIM DE JOGO! O vencedor é: " + checarVencedor().getNome());
        }
    }

    public void passarTurno() {
        //Navega na lista circular obedecendo o sentido definido
        if(sentidoHorario) this.atual = atual.getEsq();
        else this.atual = atual.getDir();
    }

    private Jogador checarVencedor() {
        if(this.atual.getMao().isEmpty()) {
            return this.atual;
        }
        return null;
    }

    public void reabastecerBaralho() {
        Carta cartaTopo = this.pilhaDescarte.pop(); //Salva a carta que está visível na mesa

        //Inverte os ponteiros para transformar a pilha de descarte na pilha de compra
        Baralho temp = this.pilhaCompra;
        this.pilhaCompra = this.pilhaDescarte;
        this.pilhaDescarte = temp;

        //Devolve a carta que estava no topo da mesa para a pilha de descarte
        this.pilhaDescarte.push(cartaTopo);

        this.pilhaCompra.embaralha();

        System.out.println("\nO baralho de compra acabou e foi reabastecido!\n");
    }
}