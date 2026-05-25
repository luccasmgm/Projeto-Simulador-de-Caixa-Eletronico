# Projeto Caixa Eletrônico - POO

Este repositório contém o código-fonte do projeto "Caixa Eletrônico", desenvolvido como requisito de avaliação prática da disciplina de Programação Orientada a Objetos (POO) do 4º semestre do curso de Análise e Desenvolvimento de Sistemas na Fatec Guarulhos, orientado pelo Professor Jadir Custodio Mendonça Junior.

## Sobre o Projeto

O sistema simula o funcionamento completo de um terminal de caixa eletrônico 24 horas. O projeto foi construído em Java e dividido em duas camadas principais para separar a regra de negócio da interface visual, respeitando as boas práticas de desenvolvimento de software. A interface gráfica (GUI) foi desenvolvida utilizando a biblioteca nativa Swing.

## Funcionalidades Implementadas

O sistema atende a dois perfis de uso (Cliente e Administrador) com as seguintes operações:

* **Efetuar Saque:** Libera o valor solicitado pelo cliente calculando automaticamente o menor número de notas possíveis.
* **Relatório de Cédulas:** Exibe um balanço detalhado da quantidade de notas disponíveis em cada gaveta do caixa.
* **Valor Total Disponível:** Calcula e exibe o montante financeiro (em Reais) armazenado no equipamento.
* **Reposição de Cédulas:** Permite ao administrador abastecer gavetas específicas informando o valor da cédula e a quantidade inserida.
* **Cota Mínima:** Define um limite de segurança financeiro. Se o saldo do caixa ficar abaixo deste valor após uma operação, o sistema entra em modo de bloqueio.
* **Sair / Extrato Final:** Encerra a aplicação e emite um extrato global com data e hora de todas as operações e tentativas realizadas na sessão.

## Lógica e Arquitetura

O desenvolvimento seguiu rigorosamente os requisitos técnicos exigidos:

* **Contrato de Interface:** A classe principal de negócio implementa a interface `ICaixaEletronico`, garantindo a padronização das assinaturas dos métodos essenciais e o isolamento do "Model" da "View".
* **Estrutura de Dados:** O gerenciamento do dinheiro é feito através de uma matriz estática `6x2`. A primeira coluna armazena os valores nominais (100, 50, 20, 10, 5 e 2) e a segunda coluna gerencia o estoque em tempo real.
* **Algoritmo Guloso (Greedy):** A rotina de saque utiliza uma abordagem gulosa. O algoritmo percorre a matriz da maior nota para a menor, sempre tentando abater o saldo devedor com o maior valor possível, respeitando o estoque físico da gaveta.
* **Travas e Validações (Fail-Fast):** O método de saque possui validações instantâneas no topo de sua execução para impedir saques nulos ou negativos, falha por falta de cédulas suficientes e bloqueio físico (o dispensador não emite mais de 30 cédulas por operação).

## Tecnologias Utilizadas

* **Linguagem:** Java (JDK)
* **Interface Gráfica:** Java Swing / WindowBuilder
* **IDE Recomendada:** Eclipse IDE
