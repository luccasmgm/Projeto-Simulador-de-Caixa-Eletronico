package br.edu.fatecgru.model;

import br.edu.fatecgru.view.CaixaEletronicoGUI;

public class CaixaEletronico implements ICaixaEletronico {
    // Separaremos o valor e a quantidade de cédulas em uma matriz
    // Matriz 6x2: Coluna 0 = Valor da nota, Coluna 1 = Quantidade disponível
    private int[][] matrizCedulas = new int[6][2];
    
    private int cotaMinima = 0; // Armazena o limite mínimo para o caixa funcionar

    /**
     * Construtor da classe.
     * Ao instanciar o objeto, a matriz recebe o abastecimento inicial obrigatório.
     */
    public CaixaEletronico() {
        // Carga inicial conforme a tabela do projeto
        matrizCedulas[0][0] = 100; matrizCedulas[0][1] = 100;
        matrizCedulas[1][0] = 50;  matrizCedulas[1][1] = 200;
        matrizCedulas[2][0] = 20;  matrizCedulas[2][1] = 300;
        matrizCedulas[3][0] = 10;  matrizCedulas[3][1] = 350;
        matrizCedulas[4][0] = 5;   matrizCedulas[4][1] = 450;
        matrizCedulas[5][0] = 2;   matrizCedulas[5][1] = 500;
    }

    /**
     * Calcula o valor total armazenado dentro do caixa.
     */
    private int calcularValorTotalEmReais() {
        int total = 0;
        for (int i = 0; i < 6; i++) {
            total += (matrizCedulas[i][0] * matrizCedulas[i][1]);
        }
        return total;
    }

    @Override
    public String pegaValorTotalDisponivel() {
        return "Valor total disponível no caixa: R$ " + calcularValorTotalEmReais();
    }

    @Override
    public String sacar(Integer valor) {
    	
    	if (valor <= 0) {
            return "Saque negado: não é possível sacar este valor.";
        }
    	
        // Regra 1: Verificar se o saque fará o caixa atingir a cota mínima
        if (cotaMinima > 0 && (calcularValorTotalEmReais() - valor) < cotaMinima) {
            return "Caixa Vazio: Chame o Operador";
        }

        int valorRestanteParaSaque = valor;
        int totalCedulasNesteSaque = 0;
        
        // Vetor temporário para guardar quantas notas de cada valor usaremos neste saque
        // Isso é necessário pois só podemos descontar da matriz oficial se o saque for 100% aprovado
        int[] cedulasSeparadasParaSaque = new int[6];

        // Lógica do Caixa: percorre da maior nota (100) para a menor (2)
        for (int i = 0; i < 6; i++) {
            int valorDaNota = matrizCedulas[i][0];
            int qtdDisponivelNoCaixa = matrizCedulas[i][1];

            // Se o valor do saque precisa dessa nota e nós temos ela no caixa
            if (valorRestanteParaSaque >= valorDaNota && qtdDisponivelNoCaixa > 0) {
                // Quantas notas seriam necessárias idealmente?
                int qtdNecessaria = valorRestanteParaSaque / valorDaNota;
                
                // Pegamos o que for menor: o que eu preciso ou o que eu tenho disponível
                int qtdParaEntregar = Math.min(qtdNecessaria, qtdDisponivelNoCaixa);

                // Registra no vetor temporário
                cedulasSeparadasParaSaque[i] = qtdParaEntregar;
                
                // Subtrai do valor que ainda falta pagar ao cliente
                valorRestanteParaSaque -= (qtdParaEntregar * valorDaNota);
                
                // Soma a quantidade de papéis que serão emitidos
                totalCedulasNesteSaque += qtdParaEntregar;
            }
        }
        // Separamos em dois IFs, um para caso não tenha dinheiro suficiente no caixa e outro para caso tenha, porém ultrapasse
        // o limite de cédulas EX: Sacar 7800 (o sistema conta 78 notas de 100, porém como o maximo sao 30, ele mostrará a segunda msg.
         
        // Regra 2: Realmente faltou dinheiro (não conseguiu zerar o valor do saque)
        if (valorRestanteParaSaque > 0) {
            return "Saque não realizado por falta de cédulas";
        }

        // Regra 3: Tinha dinheiro, conseguiu separar, mas a gaveta não suporta cuspir tudo isso
        if (totalCedulasNesteSaque > 30) {
            return "Saque negado: O limite físico da máquina é de 30 cédulas por operação.";
        }

        // Se o código chegou até aqui, é pq o saque foi aprovado
        // Agora sim atualizamos a matriz real e montamos a mensagem de sucesso
        StringBuilder comprovante = new StringBuilder();
        comprovante.append("Saque de R$ ").append(valor).append(" aprovado!\nCédulas emitidas:\n");

        for (int i = 0; i < 6; i++) {
            if (cedulasSeparadasParaSaque[i] > 0) {
                // Desconta da matriz oficial
                matrizCedulas[i][1] -= cedulasSeparadasParaSaque[i];
                comprovante.append(cedulasSeparadasParaSaque[i]).append(" nota(s) de R$ ").append(matrizCedulas[i][0]).append("\n");
            }
        }

        comprovante.append("Saldo restante no caixa: R$ ").append(calcularValorTotalEmReais());

        return comprovante.toString();
    }

    @Override
    public String pegaRelatorioCedulas() {
        StringBuilder relatorio = new StringBuilder("Relatório de Cédulas Disponíveis:\n\n");
        for (int i = 0; i < 6; i++) {
            relatorio.append("Notas de R$ ").append(matrizCedulas[i][0])
                     .append(": ").append(matrizCedulas[i][1]).append(" unidades\n");
        }
        return relatorio.toString();
    }

    @Override
    public String reposicaoCedulas(Integer cedula, Integer quantidade) {
        boolean encontrouNota = false;
        int novoTotal = 0;
        
        for (int i = 0; i < 6; i++) {
            if (matrizCedulas[i][0] == cedula) {
                matrizCedulas[i][1] += quantidade; // Soma a nova quantidade ao que já existia
                novoTotal = matrizCedulas[i][1];
                encontrouNota = true;
                break;
            }
        }
        if (encontrouNota) {
            return "Reposição de " + quantidade + " nota(s) de R$ " + cedula + " realizada com sucesso.\nNovo total desta cédula no caixa: " + novoTotal;
        } else {
            return "Valor de cédula inválido! Use apenas 100, 50, 20, 10, 5 ou 2.";
        }
    }

    /**
     * Retorna o valor atual da cota mínima configurada no caixa.
     */
    public int getCotaMinima() {
        return cotaMinima;
    }

    @Override
    public String armazenaCotaMinima(Integer minimo) {
        int valorAnterior = this.cotaMinima;
        this.cotaMinima = minimo;
        return "Cota mínima alterada de R$ " + valorAnterior + " para R$ " + minimo;
    }
    
    /**
     *Inicializa a interface gráfica.
     */
    public static void main(String arg[]) {
        CaixaEletronicoGUI janela = new CaixaEletronicoGUI(CaixaEletronico.class);
        janela.show();
    }
}