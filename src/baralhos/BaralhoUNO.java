package baralhos;

import cartas.CartaUNO;
import cartas.enums.AcaoUNO;
import cartas.enums.Cor;

//Instancia e embaralha as 108 cartas do baralho oficial de UNO
public class BaralhoUNO extends Baralho {

    public BaralhoUNO() {
        int i = 0;
        CartaUNO cAux;

        //Numeradas
        while(i < 10) {
            if(i == 0) {
                //Regra: O baralho possui apenas um '0' de cada cor
                cAux = new CartaUNO(Cor.AMARELO, i); this.cartas.add(cAux);
                cAux = new CartaUNO(Cor.AZUL, i); this.cartas.add(cAux);
                cAux = new CartaUNO(Cor.VERDE, i); this.cartas.add(cAux);
                cAux = new CartaUNO(Cor.VERMELHO, i); this.cartas.add(cAux);
            }
            else {
                //Regra: Números de 1 a 9 possuem duas cópias por cor
                cAux = new CartaUNO(Cor.AMARELO, i); this.cartas.add(cAux);
                cAux = new CartaUNO(Cor.AMARELO, i); this.cartas.add(cAux);
                cAux = new CartaUNO(Cor.AZUL, i); this.cartas.add(cAux);
                cAux = new CartaUNO(Cor.AZUL, i); this.cartas.add(cAux);
                cAux = new CartaUNO(Cor.VERDE, i); this.cartas.add(cAux);
                cAux = new CartaUNO(Cor.VERDE, i); this.cartas.add(cAux);
                cAux = new CartaUNO(Cor.VERMELHO, i); this.cartas.add(cAux);
                cAux = new CartaUNO(Cor.VERMELHO, i); this.cartas.add(cAux);
            }
            i++;
        }

        //Comprar duas (+2)
        cAux = new CartaUNO(Cor.AMARELO, AcaoUNO.MAIS_2); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.AMARELO, AcaoUNO.MAIS_2); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.AZUL, AcaoUNO.MAIS_2); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.AZUL, AcaoUNO.MAIS_2); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.VERDE, AcaoUNO.MAIS_2); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.VERDE, AcaoUNO.MAIS_2); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.VERMELHO, AcaoUNO.MAIS_2); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.VERMELHO, AcaoUNO.MAIS_2); this.cartas.add(cAux);

        //Inverter
        cAux = new CartaUNO(Cor.AMARELO, AcaoUNO.INVERTER); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.AMARELO, AcaoUNO.INVERTER); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.AZUL, AcaoUNO.INVERTER); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.AZUL, AcaoUNO.INVERTER); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.VERDE, AcaoUNO.INVERTER); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.VERDE, AcaoUNO.INVERTER); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.VERMELHO, AcaoUNO.INVERTER); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.VERMELHO, AcaoUNO.INVERTER); this.cartas.add(cAux);

        //Bloqueio (Pular)
        cAux = new CartaUNO(Cor.AMARELO, AcaoUNO.PULAR); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.AMARELO, AcaoUNO.PULAR); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.AZUL, AcaoUNO.PULAR); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.AZUL, AcaoUNO.PULAR); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.VERDE, AcaoUNO.PULAR); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.VERDE, AcaoUNO.PULAR); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.VERMELHO, AcaoUNO.PULAR); this.cartas.add(cAux);
        cAux = new CartaUNO(Cor.VERMELHO, AcaoUNO.PULAR); this.cartas.add(cAux);

        //Curingas (Wild e Wild +4)
        for(i = 0; i < 4; i++) {
            cAux = new CartaUNO(AcaoUNO.WILD); this.cartas.add(cAux);
            cAux = new CartaUNO(AcaoUNO.WILD_MAIS_4); this.cartas.add(cAux);
        }

        this.embaralha();
    }
}