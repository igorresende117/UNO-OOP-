package gui;

import modelo.Jogo;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class TelaInicial extends JFrame {

    //Componentes visuais
    private JLabel labelInstrucao;
    private JTextField campoNome;
    private JComboBox<String> comboModo;
    private JButton botaoIniciar;
    private JButton botaoAdicionar;
    private JLabel labelContador;

    //Lista dos nomes passados para a classe Jogo
    private ArrayList<String> nomesJogadores = new ArrayList<>();

    public TelaInicial() {
        super("Configuração Inicial");
        this.setLayout(new FlowLayout());

        ImageIcon icone = new ImageIcon("IconeUNO.png");
        this.setIconImage(icone.getImage());

        //1) Instanciando os componentes
        this.labelInstrucao = new JLabel("Nome do Jogador:");
        this.campoNome = new JTextField(15); //15 é o tamanho da caixa de texto
        this.botaoAdicionar = new JButton("Adicionar");
        this.labelContador = new JLabel("Jogadores adicionados: 0");
        String[] opcoesModo = {"1 - Oficial (UNO)", "2 - Convencional"};
        this.comboModo = new JComboBox<>(opcoesModo);
        this.botaoIniciar = new JButton("Iniciar Partida");

        //2) Adicionando os componentes na janela
        this.add(this.labelInstrucao);
        this.add(this.campoNome);
        this.add(this.botaoAdicionar);
        this.add(this.labelContador);
        this.add(this.comboModo);
        this.add(this.botaoIniciar);

        this.botaoAdicionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Pega o texto digitado e remove espaços vazios
                String nome = campoNome.getText().trim();

                if(!nome.isEmpty()) {
                    nomesJogadores.add(nome);
                    labelContador.setText("Jogadores adicionados: " + nomesJogadores.size());
                    campoNome.setText("");
                }
            }
        });

        this.botaoIniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Garante o mínimo de 2 jogadores conforme as regras
                if (nomesJogadores.size() >= 2) {
                    //getSelectedIndex retorna 0 ou 1, soma 1 para ficar 1 ou 2.
                    int modoEscolhido = comboModo.getSelectedIndex() + 1;

                    Jogo jogo = new Jogo(nomesJogadores, modoEscolhido);

                    //Fecha janela de configuração
                    dispose();

                    //Abre a tela principal passando o motor do jogo pra ela
                    new TelaJogo(jogo);
                } else {
                    //Avisa o usuário se ele tentar começar sozinho
                    JOptionPane.showMessageDialog(null, "Você precisa de pelo menos 2 jogadores para iniciar!");
                }
            }
        });

        //Configurações gerais
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }
}