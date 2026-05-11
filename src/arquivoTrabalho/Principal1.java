package arquivoTrabalho;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;


public class Principal1 {
    public static void main (String args[]){
        Scanner teclado = new Scanner(System.in);
        File sistema = new File("sistemaDoProfessor");
        if(!sistema.exists()){  //pasta dos arquivos do programa
           sistema.mkdir();
        }


        int opcao = 0;
        while(opcao != 3){
            opcao = menu(teclado, opcao);

            switch (opcao) {
                case 1:
                    criarArquivo(teclado, sistema);
                    break;
                case 2:
                    gerarResultado(teclado,sistema);
                    break;
                case 3:
                    System.out.println("Saindo...");
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
            
        }
        /*// Verifica se a pasta existe antes de criar
        if (!diretorio.exists()) {
            boolean criado = diretorio.mkdir(); // ou diretorio.mkdirs() para criar pais
            if (criado) {
                System.out.println("Diretório criado: " + diretorio.getPath());
            } else {
                System.out.println("Falha ao criar o diretório.");
            }
        } else {
            System.out.println("O diretório já existe.");
        } */



        teclado.close();
    }

    private static int menu(Scanner teclado,int opcao){
        System.out.println("Escolha uma das opções abaixo:");
        System.out.println("1 - Criar arquivo de respostas dos alunos");
        System.out.println("2 - Gerar resultado de uma disciplina");
        System.out.println("3 - Sair");


        //Permita ao usuário criar o arquivo com as respostas de todos os 
        // alunos de uma disciplina.O nome do arquivo será o nome da disciplina. 
        
        System.out.println();
        //Permita ao usuário gerar o resultado de uma disciplina. Seu programa 
        //deve permitir ao usuário escolher a disciplina e então informar a 
        //localização do arquivo contendo o gabarito oficial da prova (apenas 
        //uma linha com as 10 respostas corretas) da disciplina escolhida. Em 
        //seguida, deve produzir como resposta dois outros arquivos: um contendo 
        //a lista dos alunos e seus respectivos pontos (número de acertos) ordenadas 
        //por ordem alfabética, e outro contendo as mesmas informações, porém ordenado 
        //por ordem decrescente de notas (quantidade de acertos) e mostrando ao final 
        //a média da turma. Caso o aluno tenha marcado todas as questões com V ou F, o 
        //aluno receberá 0. Permita ao usuário visualizar esses dados na tela.
        //Fique livre para organizar seus arquivos em diretórios da melhor maneira possível.
        opcao = teclado.nextInt();
        teclado.nextLine();
        return opcao;
    }
    private static void criarArquivo(Scanner teclado,File sistema){
        boolean temMaisAluno = true;
        System.out.println("Insira o nome da disciplina: ");
        String nomeDisciplina = teclado.nextLine();
       
        try{
            File disciplina = new File(nomeDisciplina);
            File notas = new File(sistema, nomeDisciplina + ".txt");

            if(!disciplina.exists())
                disciplina.mkdir();

            if(!notas.createNewFile())
                return;

        }catch(IOException e ){
            System.out.println(e.getMessage());return;
        }
        
        try(BufferedWriter notasWrite = new BufferedWriter(new FileWriter(nomeDisciplina,true))){
            while(temMaisAluno){   
                System.out.println("Insira as informações referentes ao aluno: ");
                System.out.println("Nome: ");
                String nome = teclado.nextLine();
                System.out.println("Respostas: ");
                String respostas = teclado.nextLine();
        
                notasWrite.write(nome+"\t"+respostas);
                notasWrite.newLine();
                
                System.out.println("Gostaria de inserir mais alunos? ");
                int opcao = 0;
                while (opcao != 1 || opcao != 2) {
                    System.out.println("(1-Sim,2-Não)");
                    opcao = teclado.nextInt();
                    switch(opcao){
                        case 1:
                            teclado.nextLine();break;
                        case 2:
                            teclado.nextLine();temMaisAluno = false;break;
                        default:
                            System.out.println("Opção inválida! Tente novamente.");break;

                    }
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();return;
        }

        System.out.println("Gostaria de adicionar outra disciplina?");
        while(true){
            System.out.println("(1-Sim,2-Não)");
            switch(teclado.nextInt()){
                case 1:
                    teclado.nextLine();criarArquivo(teclado,sistema);return;
                case 2:
                    teclado.nextLine();break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");

            }
        }

    }

    private static void gerarResultado(Scanner teclado, File sistema){
        System.out.println("Informe o nome da disciplina: ");
        String nomeDisciplina = teclado.nextLine();
        System.out.println("Insira o caminho do gabarito oficial:");
        String caminhoGabarito = teclado.nextLine();

        ArrayList<String> dadosAlunos = new ArrayList<>();
        try(BufferedReader disciplina = new BufferedReader(new FileReader(nomeDisciplina))){
            String linha = disciplina.readLine();
            if(linha != null){
                dadosAlunos.add(linha);
            }
        }catch(FileNotFoundException e){
            System.out.println("Disciplina não encontrada!");return;
        }catch(IOException e){
            System.out.println(e.getMessage());return;
        }
        
        try(BufferedReader arqGabarito = new BufferedReader(new FileReader(caminhoGabarito))){
            String gabarito = arqGabarito.readLine();
            if(gabarito != null){
                gabarito = gabarito.toUpperCase();
                listaOrdemAlfabetica(gabarito, dadosAlunos,nomeDisciplina);
                listaOrdemDecrescente(gabarito, dadosAlunos,nomeDisciplina);
            }

        }catch(FileNotFoundException e){
            System.out.println("Gabarito não encontrado!");return;

        }catch(IOException e){
            e.printStackTrace();return;
        }
        //em que método vai mostrar os arquivos?
    }

    private static void listaOrdemAlfabetica(String gabarito, ArrayList <String> dadosAlunos,String nomeDisciplina){
        //precisa de array pra fazer a ordenacao?

        /*lista dos alunos e seus respectivos pontos (número de acertos) ordenadas 
        //por ordem alfabética */


    }

    private static void listaOrdemDecrescente(String gabarito, ArrayList <String> dadosAlunos, String nomeDisciplina){
        /*ordenado 
        //por ordem decrescente de notas (quantidade de acertos) e mostrando ao final 
    //a média da turma. */
        double media = 0;
        for(String aluno:dadosAlunos){
            media += avaliarAluno(gabarito, aluno);
        }
        media = media/dadosAlunos.size(); 
        //listaPessoas.sort((p1, p2) -> Integer.compare(p1.getIdade(), p2.getIdade()));
        dadosAlunos.sort((a1,a2) -> Integer.compare(avaliarAluno(gabarito,a2),avaliarAluno(gabarito,a1)));

        File listaDescrecente = new File(nomeDisciplina, nomeDisciplina + "_listaNotasDecrescente.txt");

        try(BufferedWriter listaDecrescente = new BufferedWriter(new FileWriter(listaDescrecente))){
            for(String aluno:dadosAlunos){
                String [] linha = aluno.split("\t");
                listaDecrescente.write(linha[1]+"\t"+avaliarAluno(gabarito, aluno));
                listaDecrescente.newLine();
            }
            listaDecrescente.write("Média da Turma: "+media);
            
        }catch(IOException e){
            e.printStackTrace();return;
        }

    }

    private static int avaliarAluno(String gabarito,String aluno){
        aluno = aluno.toUpperCase();
        int nota = 0;
        int f = 0;
        int v = 0;

        for(int i = 0;i<gabarito.length();i++){
            if(gabarito.charAt(i) == aluno.charAt(i))
                nota++;
            if(aluno.charAt(i) == 'F')
                f++;
            if(aluno.charAt(i) == 'V')
                v++;
        }

        if(v == 10 || f == 10)
            return 0; 

        return nota;
    }
}