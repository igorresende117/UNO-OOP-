package baralhos;

import cartas.CartaCOMUM;
import cartas.enums.AcaoCOMUM;
import cartas.enums.Naipe;

//Instancia e embaralha as 54 cartas do baralho tradicional
public class BaralhoCOMUM extends Baralho {

    public BaralhoCOMUM() {
        //No baralho comum, as numéricas vão de 1 (Ás) a 10
        int i = 1;
        CartaCOMUM cAux;

        //Numeradas
        while(i <= 10) {
            cAux = new CartaCOMUM(Naipe.COPAS, i); this.cartas.add(cAux);
            cAux = new CartaCOMUM(Naipe.ESPADAS, i); this.cartas.add(cAux);
            cAux = new CartaCOMUM(Naipe.OUROS, i); this.cartas.add(cAux);
            cAux = new CartaCOMUM(Naipe.PAUS, i); this.cartas.add(cAux);
            i++;
        }

        //Valetes
        cAux = new CartaCOMUM(Naipe.COPAS, AcaoCOMUM.VALETE); this.cartas.add(cAux);
        cAux = new CartaCOMUM(Naipe.ESPADAS, AcaoCOMUM.VALETE); this.cartas.add(cAux);
        cAux = new CartaCOMUM(Naipe.OUROS, AcaoCOMUM.VALETE); this.cartas.add(cAux);
        cAux = new CartaCOMUM(Naipe.PAUS, AcaoCOMUM.VALETE); this.cartas.add(cAux);

        //Damas
        cAux = new CartaCOMUM(Naipe.COPAS, AcaoCOMUM.DAMA); this.cartas.add(cAux);
        cAux = new CartaCOMUM(Naipe.ESPADAS, AcaoCOMUM.DAMA); this.cartas.add(cAux);
        cAux = new CartaCOMUM(Naipe.OUROS, AcaoCOMUM.DAMA); this.cartas.add(cAux);
        cAux = new CartaCOMUM(Naipe.PAUS, AcaoCOMUM.DAMA); this.cartas.add(cAux);

        //Reis
        cAux = new CartaCOMUM(Naipe.COPAS, AcaoCOMUM.REI); this.cartas.add(cAux);
        cAux = new CartaCOMUM(Naipe.ESPADAS, AcaoCOMUM.REI); this.cartas.add(cAux);
        cAux = new CartaCOMUM(Naipe.OUROS, AcaoCOMUM.REI); this.cartas.add(cAux);
        cAux = new CartaCOMUM(Naipe.PAUS, AcaoCOMUM.REI); this.cartas.add(cAux);

        //Curingas
        cAux = new CartaCOMUM(AcaoCOMUM.JOKER_PRETO); this.cartas.add(cAux);
        cAux = new CartaCOMUM(AcaoCOMUM.JOKER_VERMELHO); this.cartas.add(cAux);

        this.embaralha();
    }
}