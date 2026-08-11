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

        //Troca o FlowLayout por BorderLayout com 10px de margem
        this.setLayout(new BorderLayout(10, 10));

        ImageIcon icone = new ImageIcon("IconeUNO.png");
        this.setIconImage(icone.getImage());

        //Painel com as informações da mesa no topo
        JPanel painelInfo = new JPanel(new GridLayout(2, 1, 0, 5));
        painelInfo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        this.labelVez = new JLabel("Vez do(a) jogador(a): " + this.jogoAtual.getAtual().getNome());
        this.labelVez.setFont(new Font("Arial", Font.PLAIN, 14));
        this.labelVez.setHorizontalAlignment(SwingConstants.CENTER);

        Carta cartaTopo = this.jogoAtual.getPilhaDescarte().peek();
        String textoTopo = "Topo da mesa: ";
        if(cartaTopo instanceof CartaUNO cTopoUNO) {
            if(cTopoUNO.getAcao() != null) {
                textoTopo += (cTopoUNO.getCor() != null ? cTopoUNO.getCor() + " | " : "") + cTopoUNO.getAcao();
            } else {
                textoTopo += cTopoUNO.getCor() + " | " + cTopoUNO.getNum();
            }
        }
        this.labelTopo = new JLabel(textoTopo);
        this.labelTopo.setFont(new Font("Arial", Font.BOLD, 18));
        this.labelTopo.setHorizontalAlignment(SwingConstants.CENTER);

        painelInfo.add(this.labelVez);
        painelInfo.add(this.labelTopo);
        this.add(painelInfo, BorderLayout.NORTH);

        //Painel central para a mão do jogador
        this.painelMao = new JPanel(new GridLayout(0, 6, 10, 10));
        this.painelMao.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        ArrayList<Carta> mao = this.jogoAtual.getAtual().getMao();

        for(Carta c : mao) {
            String textoCarta = "";
            Color corTexto = Color.BLACK;

            if(c instanceof CartaUNO cUNO) {
                if(cUNO.getAcao() != null) {
                    if(cUNO.getCor() != null) {
                        textoCarta = cUNO.getCor() + " | " + cUNO.getAcao();
                    } else {
                        textoCarta = "" + cUNO.getAcao();
                    }
                } else {
                    textoCarta = cUNO.getCor() + " | " + cUNO.getNum();
                }

                if(cUNO.getCor() != null) {
                    switch(cUNO.getCor().toString()) {
                        case "VERMELHO": corTexto = Color.RED; break;
                        case "AZUL": corTexto = Color.BLUE; break;
                        case "VERDE": corTexto = new Color(0, 153, 0); break;
                        case "AMARELO": corTexto = new Color(204, 153, 0); break;
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
            botaoCarta.setForeground(corTexto);
            botaoCarta.setFont(new Font("Arial", Font.BOLD, 12));

            if(!c.servePJ(cartaTopo, this.jogoAtual)) {
                botaoCarta.setEnabled(false);
            }

            botaoCarta.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
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

                    jogoAtual.getAtual().getMao().remove(c);

                    if(jogoAtual.checarVencedor() != null) {
                        JOptionPane.showMessageDialog(null, "PARABÉNS! O jogador " + jogoAtual.getAtual().getNome() + " venceu a partida!", "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }

                    jogoAtual.getPilhaDescarte().push(c);
                    c.aplicaEfeito(jogoAtual);
                    jogoAtual.getAtual().setJaComprou(false);
                    jogoAtual.passarTurno();

                    dispose();
                    new TelaJogo(jogoAtual);
                }
            });

            this.painelMao.add(botaoCarta);
        }

        //Envolve a grade de cartas em um scroll e remove a borda
        JScrollPane scrollMao = new JScrollPane(this.painelMao);
        scrollMao.setBorder(null);
        this.add(scrollMao, BorderLayout.CENTER);

        //Painel inferior para os botões de ação
        this.painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        this.painelAcoes.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        this.botaoComprar = new JButton("Comprar Carta");
        this.botaoPassar = new JButton("Passar Vez");
        this.botaoComprar.setPreferredSize(new Dimension(150, 40));
        this.botaoPassar.setPreferredSize(new Dimension(150, 40));

        if(this.jogoAtual.getAtual().getJaComprou()) {
            this.botaoComprar.setEnabled(false);
        }

        this.painelAcoes.add(this.botaoComprar);
        this.painelAcoes.add(this.botaoPassar);
        this.add(this.painelAcoes, BorderLayout.SOUTH);

        this.botaoComprar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(jogoAtual.getPilhaCompra().isEmpty()) {
                    jogoAtual.reabastecerBaralho();
                    JOptionPane.showMessageDialog(null, "O baralho de compra foi reabastecido com o descarte!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }
                jogoAtual.getAtual().comprarCarta(jogoAtual.getPilhaCompra());
                jogoAtual.getAtual().setJaComprou(true);
                dispose();
                new TelaJogo(jogoAtual);
            }
        });

        this.botaoPassar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jogoAtual.getAtual().setJaComprou(false);
                jogoAtual.passarTurno();
                dispose();
                new TelaJogo(jogoAtual);
            }
        });

        this.setSize(950, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}