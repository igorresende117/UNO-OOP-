package gui;

import cartas.Carta;
import cartas.CartaCOMUM;
import cartas.CartaUNO;
import modelo.Jogo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class TelaJogo extends JFrame {

    //Guarda o jogo que veio da tela inicial
    private Jogo jogoAtual;

    private JLabel labelVez;
    private JLabel labelTopo;

    //Painel que vai agrupar os botões das cartas em grade para não vazar
    private JPanel painelMao;

    //Painel e botões para as ações do turno
    private JPanel painelAcoes;
    private JButton botaoComprar;
    private JButton botaoPassar;

    public TelaJogo(Jogo jogo) {
        super("Mesa de Jogo");
        this.jogoAtual = jogo;
        this.setLayout(new FlowLayout());

        ImageIcon icone = new ImageIcon("IconeUNO.png");
        this.setIconImage(icone.getImage());

        //Puxa o nome do jogador atual do jogo (definido na tela inicial)
        this.labelVez = new JLabel("Vez do(a) jogador(a): " + this.jogoAtual.getAtual().getNome());
        this.add(this.labelVez);

        //Cria o painel para as cartas usando GridLayout (0 linhas livres, até 6 colunas por linha)
        this.painelMao = new JPanel(new GridLayout(0, 6, 5, 5));

        //Puxa a mão do jogador atual e a carta do topo da mesa
        ArrayList<Carta> mao = this.jogoAtual.getAtual().getMao();
        Carta cartaTopo = this.jogoAtual.getPilhaDescarte().peek();

        //Formata o texto da carta do topo
        String textoTopo = "Topo da mesa: ";
        if(cartaTopo instanceof CartaUNO cTopoUNO) {
            if(cTopoUNO.getAcao() != null) {
                textoTopo += (cTopoUNO.getCor() != null ? cTopoUNO.getCor() + " | " : "") + cTopoUNO.getAcao();
            } else {
                textoTopo += cTopoUNO.getCor() + " | " + cTopoUNO.getNum();
            }
        }

        this.labelTopo = new JLabel(textoTopo);
        this.labelTopo.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(this.labelTopo);

        //Cria um botão para cada carta na mão
        for(Carta c : mao) {
            String textoCarta = "";
            Color corTexto = Color.BLACK; //(Curingas)

            if(c instanceof CartaUNO cUNO) {
                //Limpa os nulls e -1 do texto
                if(cUNO.getAcao() != null) {
                    if(cUNO.getCor() != null) {
                        textoCarta = cUNO.getCor() + " | " + cUNO.getAcao();
                    } else {
                        textoCarta = "" + cUNO.getAcao();
                    }
                } else {
                    textoCarta = cUNO.getCor() + " | " + cUNO.getNum();
                }

                //Pega a cor para pintar a letra
                if(cUNO.getCor() != null) {
                    switch(cUNO.getCor().toString()) {
                        case "VERMELHO": corTexto = Color.RED; break;
                        case "AZUL": corTexto = Color.BLUE; break;
                        case "VERDE": corTexto = new Color(0, 153, 0); break; //Verde mais escuro pra ler bem
                        case "AMARELO": corTexto = new Color(204, 153, 0); break; //Amarelo mais escuro pra ler bem
                    }
                }
            }
            else if(c instanceof CartaCOMUM cCOMUM) {
                if(cCOMUM.getAcao() != null) {
                    textoCarta = cCOMUM.getNaipe() + " | " + cCOMUM.getAcao();
                } else {
                    textoCarta = cCOMUM.getNaipe() + " | " + cCOMUM.getNum();
                }
            }

            JButton botaoCarta = new JButton(textoCarta);
            botaoCarta.setForeground(corTexto); //Muda a cor da letra

            //Desabilita o botão se a carta não puder ser jogada em cima do topo
            if(!c.servePJ(cartaTopo, this.jogoAtual)) {
                botaoCarta.setEnabled(false);
            }

            //Ação de clicar e jogar a carta na mesa
            botaoCarta.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //Se for Curinga do UNO, abre um pop-up para escolher a cor
                    if (c instanceof CartaUNO cUNO && (cUNO.getAcao() == cartas.enums.AcaoUNO.WILD || cUNO.getAcao() == cartas.enums.AcaoUNO.WILD_MAIS_4)) {
                        String[] cores = {"Vermelho", "Azul", "Verde", "Amarelo"};
                        int escolha = JOptionPane.showOptionDialog(null, "Escolha a nova cor para a mesa:", "Carta Curinga",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, cores, cores[0]);

                        cartas.enums.Cor novaCor = switch (escolha) {
                            case 1 -> cartas.enums.Cor.AZUL;
                            case 2 -> cartas.enums.Cor.VERDE;
                            case 3 -> cartas.enums.Cor.AMARELO;
                            default -> cartas.enums.Cor.VERMELHO;
                        };
                        jogoAtual.setCorAtualAtiva(novaCor);
                    }
                    //Se for Curinga Comum (Joker), abre um pop-up para escolher o naipe
                    else if (c instanceof CartaCOMUM cCOMUM && (cCOMUM.getAcao() == cartas.enums.AcaoCOMUM.JOKER_PRETO || cCOMUM.getAcao() == cartas.enums.AcaoCOMUM.JOKER_VERMELHO)) {
                        String[] naipes = {"Copas", "Paus", "Espadas", "Ouros"};
                        int escolha = JOptionPane.showOptionDialog(null, "Escolha o novo naipe para a mesa:", "Carta Curinga",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, naipes, naipes[0]);

                        cartas.enums.Naipe novoNaipe = switch (escolha) {
                            case 1 -> cartas.enums.Naipe.PAUS;
                            case 2 -> cartas.enums.Naipe.ESPADAS;
                            case 3 -> cartas.enums.Naipe.OUROS;
                            default -> cartas.enums.Naipe.COPAS;
                        };
                        jogoAtual.setNaipeAtualAtivo(novoNaipe);
                    }

                    //1. Remove a carta da mão do jogador atual
                    jogoAtual.getAtual().getMao().remove(c);

                    //Checa vencedor
                    if(jogoAtual.checarVencedor() != null) {
                        JOptionPane.showMessageDialog(null, "PARABÉNS! O jogador " + jogoAtual.getAtual().getNome() + " venceu a partida!", "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);

                        System.exit(0); //Fecha o jogo
                    }

                    //2. Empilha a carta no descarte
                    jogoAtual.getPilhaDescarte().push(c);

                    //3. Aplica o efeito correspondente no motor do jogo
                    c.aplicaEfeito(jogoAtual);

                    //4. Reseta a flag de compra, passa o turno para o próximo e atualiza a tela
                    jogoAtual.getAtual().setJaComprou(false);
                    jogoAtual.passarTurno();

                    dispose();
                    new TelaJogo(jogoAtual);
                }
            });

            this.painelMao.add(botaoCarta);
        }

        //Adiciona o painel da mão completo na janela
        this.add(this.painelMao);

        //Cria um painel separado para os botões de ação não misturarem com as cartas
        this.painelAcoes = new JPanel(new FlowLayout());

        this.botaoComprar = new JButton("Comprar Carta");
        this.botaoPassar = new JButton("Passar Vez");

        //Trava o botão de compra se o jogador já tiver puxado carta neste turno
        if(this.jogoAtual.getAtual().getJaComprou()) {
            this.botaoComprar.setEnabled(false);
        }

        this.painelAcoes.add(this.botaoComprar);
        this.painelAcoes.add(this.botaoPassar);

        //Adiciona o painel de ações na janela
        this.add(this.painelAcoes);

        //Ação do botão de comprar
        this.botaoComprar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Se o baralho principal estiver vazio, reabastecer
                if(jogoAtual.getPilhaCompra().isEmpty()) {
                    jogoAtual.reabastecerBaralho();
                    JOptionPane.showMessageDialog(null, "O baralho de compra foi reabastecido com o descarte!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }

                //Adiciona a carta na mão e marca que já comprou, sem passar o turno
                jogoAtual.getAtual().comprarCarta(jogoAtual.getPilhaCompra());
                jogoAtual.getAtual().setJaComprou(true);

                //Recarrega a tela para desabilitar o botão e mostrar a carta nova
                dispose();
                new TelaJogo(jogoAtual);
            }
        });

        //Ação do botão de passar a vez
        this.botaoPassar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Reseta a flag de compra do jogador atual antes de girar a mesa
                jogoAtual.getAtual().setJaComprou(false);
                jogoAtual.passarTurno();

                dispose();
                new TelaJogo(jogoAtual);
            }
        });

        this.setSize(1100, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}