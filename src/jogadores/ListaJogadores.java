package jogadores;

//Lista dupla circular de jogadores
//Esquerda (Esq) == Próximo e Direita (Dir) == Anterior *Sentido não invertido
public class ListaJogadores {
    private Jogador tail;
    private Jogador head;
    private int quant;

    public boolean isEmpty() {
        return tail == null;
    }

    public boolean isFull() {
        //A lista suporta um limite máximo de 7 jogadores
        return quant >= 7;
    }

    public boolean adicionaJogadorFinal(Jogador jogador) {
        if(this.isFull()){
            return false;
        }

        if(this.isEmpty()) {
            //O primeiro jogador inserido aponta para ele mesmo, fechando o círculo inicial
            this.tail = jogador;
            jogador.setEsq(jogador);
            jogador.setDir(jogador);
            this.quant++;
            this.head = jogador; //O primeiro jogador também assume a posição de cabeça (head)
        }
        else {
            //Insere o novo jogador e costura as referências
            jogador.setEsq(this.tail.getEsq());
            this.tail.setEsq(jogador);
            jogador.getEsq().setDir(jogador);
            jogador.setDir(this.tail);

            this.tail = jogador; //O novo jogador assume a posição final (tail)
            this.quant++;
            this.head = this.tail.getEsq(); //Garante que a cabeça continue apontando para o primeiro jogador
        }
        return true;
    }

    public boolean removeJogadorFinal() {
        if(this.isEmpty()){
            return false;
        }

        if(this.quant == 1) {
            //Esvazia a lista quebrando a referência do único jogador
            this.tail = null;
            quant--;
            this.head = null; //Limpa a referência da cabeça
        }
        else {
            //Isola o tail atual e costura a lista entre a cabeça (Esq) e o penúltimo (Dir)
            this.tail.getDir().setEsq(this.tail.getEsq());
            this.tail.getEsq().setDir(this.tail.getDir());

            this.tail = this.tail.getDir(); //O jogador anterior assume a posição de tail
            this.quant--;
            this.head = this.tail.getEsq(); //Atualiza a cabeça para refletir a nova costura da lista
        }
        return true;
    }

    //Retorna a cabeça da lista (jogador que detém o turno atual)
    public Jogador getHead() {
        return this.head;
    }

    //Retorna a cauda da lista (jogador em último no contexto dado)
    public Jogador getTail() {
        return this.tail;
    }

    //Retorna a quantidade da lista
    public int getQuant() {
        return this.quant;
    }

    //Avança a lista em uma posição, passando o turno para o próximo jogador
    public void proximo(boolean sentidoHorario) {
        if(sentidoHorario) {
            //Avança no sentido padrão (horário)
            this.tail = this.tail.getEsq();
            this.head = this.head.getEsq();
        }
        else {
            //Avança no sentido anti-horário (efeito da carta Inverter)
            this.tail = this.tail.getDir();
            this.head = this.head.getDir();
        }
    }
}