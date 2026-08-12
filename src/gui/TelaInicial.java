package gui;

import modelo.Jogo;

import java.awt.*;
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

        //Usa BorderLayout para estruturar a janela inteira
        this.setLayout(new BorderLayout());

        try {
            java.io.InputStream streamIcone = getClass().getResourceAsStream("/IconeUNO.png");
            if (streamIcone != null) {
                java.awt.Image imagem = javax.imageio.ImageIO.read(streamIcone);
                this.setIconImage(imagem);
            }
        } catch (Exception ex) {
            System.out.println("Ícone não encontrado pelo executável.");
        }

        //Cria um painel central alinhado verticalmente com margens
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        //1) Instanciando e centralizando os componentes
        this.labelInstrucao = new JLabel("Nome do Jogador:");
        this.labelInstrucao.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.campoNome = new JTextField();
        this.campoNome.setMaximumSize(new Dimension(250, 30));
        this.campoNome.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.botaoAdicionar = new JButton("Adicionar");
        this.botaoAdicionar.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.labelContador = new JLabel("Jogadores adicionados: 0");
        this.labelContador.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] opcoesModo = {"1 - Oficial (UNO)", "2 - Convencional"};
        this.comboModo = new JComboBox<>(opcoesModo);
        this.comboModo.setMaximumSize(new Dimension(200, 30));
        this.comboModo.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.botaoIniciar = new JButton("Iniciar Partida");
        this.botaoIniciar.setAlignmentX(Component.CENTER_ALIGNMENT);

        //2) Adicionando os componentes no painel com espaçamentos
        painelCentral.add(this.labelInstrucao);
        painelCentral.add(Box.createRigidArea(new Dimension(0, 5)));
        painelCentral.add(this.campoNome);
        painelCentral.add(Box.createRigidArea(new Dimension(0, 10)));
        painelCentral.add(this.botaoAdicionar);
        painelCentral.add(Box.createRigidArea(new Dimension(0, 15)));
        painelCentral.add(this.labelContador);
        painelCentral.add(Box.createRigidArea(new Dimension(0, 20)));
        painelCentral.add(new JLabel("Modo de Jogo:") {{ setAlignmentX(Component.CENTER_ALIGNMENT); }});
        painelCentral.add(Box.createRigidArea(new Dimension(0, 5)));
        painelCentral.add(this.comboModo);
        painelCentral.add(Box.createRigidArea(new Dimension(0, 25)));
        painelCentral.add(this.botaoIniciar);

        this.add(painelCentral, BorderLayout.CENTER);

        this.botaoAdicionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
                if (nomesJogadores.size() >= 2) {
                    int modoEscolhido = comboModo.getSelectedIndex() + 1;
                    Jogo jogo = new Jogo(nomesJogadores, modoEscolhido);
                    dispose();
                    new TelaJogo(jogo);
                } else {
                    JOptionPane.showMessageDialog(null, "Você precisa de pelo menos 2 jogadores para iniciar!");
                }
            }
        });

        this.setSize(400, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }
}