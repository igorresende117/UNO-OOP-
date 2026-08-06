package baralhos;

import cartas.Carta;
import cartas.CartaCOMUM;
import cartas.CartaUNO;

//Baralho inicialmente vazio (usado para a pilha de descarte)
public class BaralhoVazio extends Baralho {

    public void imprime() {
        if (this.isEmpty()) {
            System.out.println("A pilha de cartas está vazia.");
            return;
        }

        int i = 1;
        for(Carta c : this.cartas) {
            //Downcasting para identificar o tipo de carta na pilha
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
    }
}