package cartas;

import modelo.Jogo;

public abstract class Carta {

    //Acesso apenas pela classe e subclasses
    protected int numero;

    public int getNum() {
        return numero;
    }

    //Valida se a carta pode ser jogada na mesa de acordo com a carta no topo do descarte
    public abstract boolean servePJ(Carta cartaTopo, Jogo contextoJogo);

    //Executa a ação da carta alterando o estado da partida
    public abstract void aplicaEfeito(Jogo contextoJogo);

    //Imprime a carta
    public abstract void imprimirCarta();
}