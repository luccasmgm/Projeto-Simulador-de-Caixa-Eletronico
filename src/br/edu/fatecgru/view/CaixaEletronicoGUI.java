package br.edu.fatecgru.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;           // Importante para pegar a data e hora atual
import java.time.format.DateTimeFormatter;// Importante para formatar a data no padrão brasileiro

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane; // Importante para as caixas de diálogo
import javax.swing.JScrollPane; // Importante para a tela de extrato
import javax.swing.JTextArea;   // Importante para a tela de extrato

import br.edu.fatecgru.model.CaixaEletronico;

public class CaixaEletronicoGUI {

    private JFrame frame;
    
    // Declaração do objeto da nossa regra de negócio
    private CaixaEletronico caixa; 
    
    // Armazena o histórico completo de operações realizadas na interface
    private StringBuilder extratoGlobal;

    /**
     * Inicializa a interface e a regra de negócio.
     */
    public CaixaEletronicoGUI(Class<?> classe) {
        // Inicializa a regra de negócio junto com a telaa
        caixa = new CaixaEletronico(); 
        extratoGlobal = new StringBuilder("=== EXTRATO GERAL DE OPERAÇÕES ===\n\n");
        initialize();
    }

    /**
     * Grava as operações realizadas no extrato com data e hora.
     */
    private void registrarNoExtrato(String acao, String resultado) {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        extratoGlobal.append("[").append(agora.format(formatter)).append("] ").append(acao).append("\n");
        extratoGlobal.append("-> ").append(resultado.replace("\n", "\n   ")).append("\n");
        extratoGlobal.append("--------------------------------------------------\n");
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.getContentPane().setBackground(new Color(255, 192, 203)); // Fundo rosa mantido!
        frame.getContentPane().setForeground(new Color(192, 192, 192));
        frame.setBounds(100, 100, 450, 480); // Aumentei um pouco a altura para caber o novo Label
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        // --- MÓDULO DO CLIENTE ---
        
        JLabel lblModuloCliente = new JLabel("Módulo do Cliente:");
        lblModuloCliente.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblModuloCliente.setBounds(89, 62, 149, 33);
        frame.getContentPane().add(lblModuloCliente);
        
        JButton btnEfetuarSaque = new JButton("Efetuar Saque");
        btnEfetuarSaque.setBackground(new Color(255, 255, 255)); 
        btnEfetuarSaque.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnEfetuarSaque.setBounds(89, 91, 258, 28);
        
        // Evento de Clique: Efetuar Saque
        btnEfetuarSaque.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Solicita o valor ao usuário
                    String entrada = JOptionPane.showInputDialog(frame, "Digite o valor do saque:");
                    // Verifica se o usuário não cancelou ou enviou vazio
                    if (entrada != null && !entrada.trim().isEmpty()) {
                        int valor = Integer.parseInt(entrada);
                        // Envia o valor para a lógica no Model e recebe a resposta
                        String resultado = caixa.sacar(valor);
                        
                        // Grava a tentativa no extrato
                        registrarNoExtrato("Tentativa de Saque: R$ " + valor, resultado);
                        
                        // Exibe a resposta (sucesso ou falha)
                        JOptionPane.showMessageDialog(frame, resultado, "Aviso de Saque", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Por favor, digite um valor numérico válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        frame.getContentPane().add(btnEfetuarSaque);
        
        // --- MÓDULO DO ADMINISTRADOR ---
        
        JLabel lblModuloDoAdministrador = new JLabel("Módulo do Administrador:");
        lblModuloDoAdministrador.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblModuloDoAdministrador.setBounds(89, 145, 200, 28);
        frame.getContentPane().add(lblModuloDoAdministrador);
        
        JButton btnRelatrioDeCdulas = new JButton("Relatório de Cédulas");
        btnRelatrioDeCdulas.setBackground(new Color(255, 255, 255));
        btnRelatrioDeCdulas.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnRelatrioDeCdulas.setBounds(89, 172, 258, 28);
        
        // Evento de Clique: Relatório de Cédulas
        btnRelatrioDeCdulas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String relatorio = caixa.pegaRelatorioCedulas();
                JOptionPane.showMessageDialog(frame, relatorio, "Relatório de Cédulas", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        frame.getContentPane().add(btnRelatrioDeCdulas);
        
        JButton btnValorTotalDisponvel = new JButton("Valor total disponível");
        btnValorTotalDisponvel.setBackground(new Color(255, 255, 255));
        btnValorTotalDisponvel.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnValorTotalDisponvel.setBounds(89, 211, 258, 28);
        
        // Evento de Clique: Valor Total
        btnValorTotalDisponvel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String total = caixa.pegaValorTotalDisponivel();
                JOptionPane.showMessageDialog(frame, total, "Saldo do Caixa", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        frame.getContentPane().add(btnValorTotalDisponvel);
        
        JButton btnReposioDeCdulas = new JButton("Reposição de Cédulas");
        btnReposioDeCdulas.setBackground(new Color(255, 255, 255));
        btnReposioDeCdulas.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnReposioDeCdulas.setBounds(89, 250, 258, 28);
        
        // Evento de Clique: Reposição de Cédulas
        btnReposioDeCdulas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String strCedula = JOptionPane.showInputDialog(frame, "Qual a nota que deseja repor? (100, 50, 20, 10, 5 ou 2):");
                    if (strCedula != null && !strCedula.trim().isEmpty()) {
                        int cedula = Integer.parseInt(strCedula);
                        
                        String strQtd = JOptionPane.showInputDialog(frame, "Quantas cédulas de R$ " + cedula + " você vai colocar?");
                        if (strQtd != null && !strQtd.trim().isEmpty()) {
                            int quantidade = Integer.parseInt(strQtd);
                            String resultado = caixa.reposicaoCedulas(cedula, quantidade);
                            
                            // Grava a reposição no extrato
                            registrarNoExtrato("Reposição de Cédulas", resultado);
                            
                            JOptionPane.showMessageDialog(frame, resultado, "Aviso de Reposição", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Entrada inválida. Digite apenas números inteiros.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        frame.getContentPane().add(btnReposioDeCdulas);
        
        JButton btnCotaMnima = new JButton("Cota Mínima");
        btnCotaMnima.setBackground(new Color(255, 255, 255));
        btnCotaMnima.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnCotaMnima.setBounds(89, 289, 258, 28);
        
        // Evento de Clique: Cota Mínima
        btnCotaMnima.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String cotaAtual = caixa.getCotaMinima() == 0
                            ? "Nenhuma cota mínima definida."
                            : "Cota mínima atual: R$ " + caixa.getCotaMinima();
                    String entrada = JOptionPane.showInputDialog(frame, cotaAtual + "\n\nDigite o novo valor para a cota mínima do caixa:");
                    if (entrada != null && !entrada.trim().isEmpty()) {
                        int valorMinimo = Integer.parseInt(entrada);
                        String resultado = caixa.armazenaCotaMinima(valorMinimo);
                        
                        // Grava a alteração da cota no extrato
                        registrarNoExtrato("Alteração de Cota Mínima", resultado);
                        
                        JOptionPane.showMessageDialog(frame, resultado, "Aviso de Cota", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Por favor, digite um valor numérico válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        frame.getContentPane().add(btnCotaMnima);
        
        // ---
        
        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(new Color(255, 255, 255));
        btnSair.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnSair.setBounds(89, 362, 258, 28);
        
        // Evento de Clique: Sair e mostrar Extrato
        btnSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lê o extrato completo diretamente da variável da GUI
                JTextArea textArea = new JTextArea(extratoGlobal.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);  
                scrollPane.setPreferredSize(new java.awt.Dimension(400, 400));
                
                JOptionPane.showMessageDialog(frame, scrollPane, "Extrato Final", JOptionPane.INFORMATION_MESSAGE);
                
                // Encerra a execução do programa Java
                frame.dispose();
                System.exit(0);
            }
        });
        frame.getContentPane().add(btnSair);
        
        // Título Principal
        JLabel lblTitulo = new JLabel("Caixa Eletrônico 24 Horas");
        lblTitulo.setBackground(new Color(255, 255, 0));
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
        lblTitulo.setBounds(127, 3, 200, 41);
        frame.getContentPane().add(lblTitulo);
    }

    /**
     * Exibe a janela principal da aplicação.
     */
    public void show() {
        if (this.frame != null) {
            this.frame.setVisible(true);
        }
    }
}