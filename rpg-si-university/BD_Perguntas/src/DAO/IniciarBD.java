package DAO;

public class IniciarBD {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO CONFIGURAÇÃO DO BANCO DE DADOS PERGUNTAS RPG ===");
        
        try {
            System.out.println("0. Verificando driver MySQL...");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("✓ Driver MySQL encontrado");
            } catch (ClassNotFoundException e) {
                System.err.println("❌ DRIVER MYSQL NÃO ENCONTRADO!");
                System.err.println("Solução: Adicione o MySQL Connector/J ao classpath:");
                System.err.println("1. Baixe de: https://dev.mysql.com/downloads/connector/j/");
                System.err.println("2. Extraia o .zip");
                System.err.println("3. No Eclipse: Project → Properties → Java Build Path → Libraries → Add External JARs");
                System.err.println("4. Selecione o arquivo mysql-connector-j-8.x.x.jar");
                return;
            }
            
            System.out.println("1. Criando banco de dados...");
            CriarBanco.criarBanco();
            
            System.out.println("2. Criando estrutura da tabela...");
            CriarEstrutura.criarTabela();
            
            System.out.println("3. Inserindo perguntas no banco...");
            CriarPerguntas.inserirPerguntas();
            
            System.out.println("========================================================");
            System.out.println("BANCO DE DADOS CONFIGURADO COM SUCESSO!");
            System.out.println("Banco: perguntasRPG");
            System.out.println("Tabela: pergunta");
            System.out.println("Total de perguntas inseridas: 50");
            System.out.println("Mundos disponíveis: Banco de Dados, HTML e JavaScript, Java, Linguagem C, Rede de Computadores");
            System.out.println("========================================================");
            
            System.out.println("\nTestando conexão e consulta...");
            testarConexao();
            
        } catch (Exception e) {
            System.out.println("ERRO durante a configuração do banco: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testarConexao() {
        try {
            System.out.println("Testando conexão com o banco...");
            
            // Testar conexão
            var conn = DatabaseConnection.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Conexão com o banco estabelecida com sucesso");
                conn.close();
            }
            
            System.out.println("Testando consulta de perguntas...");
            var perguntasBancoDados = PerguntaDAO.getPerguntasPorMundo("Banco de Dados");
            System.out.println("✓ Perguntas de Banco de Dados carregadas: " + perguntasBancoDados.size());
            
            var perguntasJava = PerguntaDAO.getPerguntasPorMundo("Java");
            System.out.println("✓ Perguntas de Java carregadas: " + perguntasJava.size());
            
            var perguntasHTML = PerguntaDAO.getPerguntasPorMundo("HTML e JavaScript");
            System.out.println("✓ Perguntas de HTML e JavaScript carregadas: " + perguntasHTML.size());
            
            var perguntasC = PerguntaDAO.getPerguntasPorMundo("Linguagem C");
            System.out.println("✓ Perguntas de Linguagem C carregadas: " + perguntasC.size());
            
            var perguntasRede = PerguntaDAO.getPerguntasPorMundo("Rede de Computadores");
            System.out.println("✓ Perguntas de Rede de Computadores carregadas: " + perguntasRede.size());
            
            System.out.println("\n✅ TODOS OS TESTES FORAM BEM-SUCEDIDOS!");
            System.out.println("O banco de dados está pronto para ser usado pelo RPG!");
            
        } catch (Exception e) {
            System.out.println("❌ Erro durante os testes: " + e.getMessage());
            System.out.println("Verifique se:");
            System.out.println("1. O MySQL está instalado e rodando");
            System.out.println("2. O usuário 'root' não tem senha (ou altere a senha no DatabaseConnection.java)");
            System.out.println("3. A porta 3306 está liberada");
        }
    }
}