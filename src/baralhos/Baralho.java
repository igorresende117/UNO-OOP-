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
        this.cartas.add(carta);
    }

    //Remove e retorna a carta do topo
    public Carta pop() {
        // Pega o tamanho da lista - 1 para acessar e remover o último elemento
        return this.cartas.remove(this.cartas.size() - 1);
    }

    //Retorna a carta do topo sem remover
    public Carta peek() {
        return this.cartas.get(this.cartas.size() - 1);
    }

    public boolean isEmpty() {
        return this.cartas.isEmpty();
    }
}