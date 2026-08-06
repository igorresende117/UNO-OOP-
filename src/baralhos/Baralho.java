package baralhos;

import java.util.ArrayList;
import java.util.Collections;

import cartas.Carta;

//Estrutura base (Pilha) para todos os baralhos do jogo
public abstract class Baralho {

    protected ArrayList<Carta> cartas = new ArrayList<>();

    //Embaralha as cartas da estrutura
    public void embaralha() {
        Collections.shuffle(this.cartas);
    }

    //Adiciona uma carta no topo da pilha
    public void push(Carta carta) {
        this.cartas.addLast(carta);
    }

    //Remove e retorna a carta do topo
    public Carta pop() {
        return this.cartas.removeLast();
    }

    //Retorna a carta do topo sem remover
    public Carta peek() {
        return this.cartas.getLast();
    }

    public boolean isEmpty() {
        return this.cartas.isEmpty();
    }
}