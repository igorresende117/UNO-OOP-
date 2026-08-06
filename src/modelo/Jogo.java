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

    //Recebe os nomes e o modo diretamente da Interface Gráfica
    public Jogo(java.util.ArrayList<String> nomesJogadores, int modoEscolhido) {
        this.lista = new ListaJogadores();
        this.sentidoHorario = true;

        //Cadastra os jogadores na lista duplamente encadeada
        for (String nome : nomesJogadores) {
            this.lista.adicionaJogadorFinal(new Jogador(nome));
        }

        this.atual = lista.getHead(); //Define o primeiro a jogar
        this.iniciarPartida(modoEscolhido);
    }

    public void iniciarPartida(int modo) {
        CartaUNO cTopoUNO;
        CartaCOMUM cTopoCOMUM;

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
                this.atual = atual.getEsq();
            }

            //O jogo não pode começar com um +4 na mesa
            while(cTopoUNO.getAcao() == AcaoUNO.WILD_MAIS_4) {
                this.pilhaCompra.push(this.pilhaDescarte.pop());
                this.pilhaCompra.embaralha();

                this.pilhaDescarte.push(this.pilhaCompra.pop());
                cTopoUNO = (CartaUNO) this.pilhaDescarte.peek();
            }

            //Aplica o efeito da primeira carta no jogador atual
            cTopoUNO.aplicaEfeito(this);
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
        }
    }

    public void passarTurno() {
        //Navega na lista circular obedecendo o sentido definido
        if(sentidoHorario) this.atual = atual.getEsq();
        else this.atual = atual.getDir();
    }

    public Jogador checarVencedor() {
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