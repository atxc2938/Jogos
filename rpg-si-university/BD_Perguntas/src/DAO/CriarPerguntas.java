package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CriarPerguntas {
    public static void inserirPerguntas() {
        String sql = "INSERT IGNORE INTO pergunta (mundo, texto, opcao_a, opcao_b, opcao_c, opcao_d, resposta_correta) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        String[][] perguntas = {
            // Banco de Dados (6 perguntas)
            {"Banco de Dados", "Qual comando SQL é usado para criar um novo banco de dados?", "NEW DATABASE", "ADD DATABASE", "MAKE DATABASE", "CREATE DATABASE", "0"}, // A correta foi colocada em índice 0 (opção criada em A)
            {"Banco de Dados", "Qual normalização elimina dependências transitivas?", "Primeira Forma Normal", "Segunda Forma Normal", "Terceira Forma Normal", "Forma Normal Boyce-Codd", "2"}, // C
            {"Banco de Dados", "Qual isolamento permite leituras não repetíveis mas não dirty reads?", "REPEATABLE READ", "READ COMMITTED", "SERIALIZABLE", "READ UNCOMMITTED", "1"}, // B (READ COMMITTED movida para índice 1)
            {"Banco de Dados", "Qual tipo de dado é usado para armazenar valores verdadeiro/falso?", "BIT", "FLAG", "CHECK", "BOOLEAN", "3"}, // D (BOOLEAN movida para índice 3)
            {"Banco de Dados", "O que é um deadlock em transações?", "Situação onde transações estão bloqueadas", "Transação não commitada", "Erro de sintaxe SQL", "Falha na conexão", "0"}, // A
            {"Banco de Dados", "Qual comando SQL remove uma tabela do banco?", "DELETE TABLE", "DROP TABLE", "REMOVE TABLE", "ERASE TABLE", "1"}, // B (DROP TABLE movida para índice 1)

            // HTML e JavaScript (6 perguntas)
            {"HTML e JavaScript", "Qual tag HTML cria um botão clicável?", "&lt;input type='button'&gt;", "&lt;button&gt;", "&lt;a&gt;", "&lt;div&gt;", "1"},
            {"HTML e JavaScript", "O que é hoisting em JavaScript?", "Movimento de elementos", "Otimização", "Carregamento", "Elevação de declarações", "3"}, // D ("Elevação de declarações" em índice 3)
            {"HTML e JavaScript", "Qual a diferença entre == e === em JavaScript?", "== valor e tipo", "=== valor e tipo", "Sem diferença", "=== mais rápido", "1"}, // B ("=== valor e tipo" em índice 1)
            {"HTML e JavaScript", "Como exibir um alerta em JavaScript?", "message()", "popup()", "show()", "alert()", "3"}, // D ("alert()" em índice 3)
            {"HTML e JavaScript", "Qual método converte JSON para objeto?", "JSON.decode()", "JSON.toObject()", "JSON.parse()", "JSON.convert()", "2"}, // C ("JSON.parse()" em índice 2)
            {"HTML e JavaScript", "Qual propriedade altera o fundo?", "bgcolor", "color-background", "back-color", "background-color", "3"}, // D ("background-color" em índice 3)

            // Java (6 perguntas)
            {"Java", "Qual palavra-chave define classe abstrata?", "abstract", "virtual", "interface", "base", "0"}, // A
            {"Java", "O que é garbage collection?", "Coleta de dados", "Liberação de memória", "Organização", "Backup", "1"}, // B ("Liberação de memória" em índice 1)
            {"Java", "Diferença ArrayList e LinkedList?", "Iguais", "ArrayList thread-safe", "ArrayList rápido acesso", "LinkedList rápido acesso", "2"}, // C ("ArrayList rápido acesso" em índice 2)
            {"Java", "Como converter String para int?", "String.toInt()", "Convert.toInteger()", "Integer.valueOf()", "Integer.parseInt()", "3"}, // D ("Integer.parseInt()" em índice 3)
            {"Java", "Qual anotação para sobrescrever?", "@Overwrite", "@Inherit", "@Extend", "@Override", "3"}, // D ("@Override" em índice 3)
            {"Java", "Qual método retorna tamanho da lista?", "length()", "size()", "count()", "getSize()", "1"}, // B ("size()" em índice 1)

            // Linguagem C (6 perguntas)
            {"Linguagem C", "Como declarar array de 5 inteiros?", "int[] arr", "integer arr[5]", "int arr[5]", "array int[5]", "2"}, // C ("int arr[5]" em índice 2)
            {"Linguagem C", "Qual função encerra programa?", "end()", "stop()", "quit()", "exit()", "3"}, // D ("exit()" em índice 3)
            {"Linguagem C", "O que é ponteiro nulo?", "Aponta para zero", "Não declarado", "Para inteiro", "Constante", "0"}, // A
            {"Linguagem C", "Qual operador retorna tamanho?", "size", "sizeof", "length", "sizeof()", "1"}, // B ("sizeof" em índice 1)
            {"Linguagem C", "Diferença malloc e calloc?", "malloc mais rápido", "calloc menos memória", "calloc inicializa", "malloc para arrays", "2"}, // C ("calloc inicializa" em índice 2)
            {"Linguagem C", "Como declarar constante?", "const", "final", "constant", "#define", "3"}, // D (#define em índice 3)

            // Rede de Computadores (6 perguntas)
            {"Rede de Computadores", "Qual protocolo resolve nomes?", "DNS", "DHCP", "HTTP", "FTP", "0"}, // A
            {"Rede de Computadores", "O que é NAT?", "Protocolo roteamento", "Tradução de endereços", "Tipo de cabo", "Criptografia", "1"}, // B ("Tradução de endereços" em índice 1)
            {"Rede de Computadores", "Diferença TCP e UDP?", "UDP mais lento", "TCP sem controle", "TCP confiável", "UDP garante entrega", "2"}, // C ("TCP confiável" em índice 2)
            {"Rede de Computadores", "Máscara classe C?", "255.255.255.255", "255.255.0.0", "255.0.0.0", "255.255.255.0", "3"}, // D ("255.255.255.0" em índice 3)
            {"Rede de Computadores", "O que é servidor DHCP?", "Traduz nomes", "Atribui IPs", "Armazena páginas", "Gerencia emails", "1"}, // B ("Atribui IPs" em índice 1)
            {"Rede de Computadores", "Qual porta HTTPS?", "8080", "21", "443", "80", "2"} // C ("443" em índice 2)
        };
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (String[] pergunta : perguntas) {
                pstmt.setString(1, pergunta[0]);
                pstmt.setString(2, pergunta[1]);
                pstmt.setString(3, pergunta[2]);
                pstmt.setString(4, pergunta[3]);
                pstmt.setString(5, pergunta[4]);
                pstmt.setString(6, pergunta[5]);
                pstmt.setInt(7, Integer.parseInt(pergunta[6]));
                pstmt.executeUpdate();
            }
            
            System.out.println("Perguntas inseridas no banco de dados");
            
        } catch (Exception e) {
            System.out.println("Erro ao inserir perguntas: " + e.getMessage());
        }
    }
}
