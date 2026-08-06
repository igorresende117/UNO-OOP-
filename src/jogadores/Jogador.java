package jogadores;

import java.util.ArrayList;

import baralhos.Baralho;
import cartas.Carta;
import modelo.Jogo;

//Representa um jogador e atua como um nó na Doubly Linked List
public class Jogador {
    private String nome;
    private ArrayList<Carta> mao = new ArrayList<>();

    //Flags de controle para travar ações repetidas no mesmo turno
    private boolean jaComprouNesteTurno;

    //Ponteiros da lista duplamente encadeada
    private Jogador jogadorEsq;
    private Jogador jogadorDir;

    public Jogador(String nome) {
        this.nome = nome;
        this.jaComprouNesteTurno = false;
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

    // Getters e Setters para a GUI controlar o limite de compra por turno
    public boolean getJaComprou() {
        return this.jaComprouNesteTurno;
    }

    public void setJaComprou(boolean status) {
        this.jaComprouNesteTurno = status;
    }

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
}