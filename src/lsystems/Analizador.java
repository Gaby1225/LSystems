/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lsystems;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Administrador
 */
public class Analizador {

    public static void main(String[] args) throws IOException {
        try {
            lerArquivo();
        } catch (Exception ex) {
            String mensagem = ex.getMessage();
            
            SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMdd HHmmss");
            Date date = new Date(System.currentTimeMillis());
            
            String name = "Log de Erros " + formatter.format(date) + ".txt";
            File arquivo = new File(name);
            arquivo.createNewFile();
                       
            try (FileWriter escritor = new FileWriter(name))
            {
                escritor.write(mensagem);
            }
            
        }
    }

    public static void lerArquivo() throws Exception {
        File arquivo = new File("entrada.txt");

        char[] gramatica = new char[6];
        int passos = 0, maiorRegra = 0;
        String axioma = "";
        double angulo = 0;
        HashMap<String, String> regras = new HashMap();

        boolean gram = false, pass = false, axi = false, ang = false, reg = false;

        if (arquivo.exists()) {
            Scanner scanner = new Scanner(arquivo);
            while (scanner.hasNext()) {
                String linha = scanner.nextLine();
                if (linha.startsWith("Σ = ") || linha.startsWith("V = ") || linha.startsWith("v = ")) {
                    gramatica = processaGramatica(linha.substring(4));
                    gram = true;
                } else if (linha.startsWith("n = ")) {
                    passos = Integer.parseInt(linha.substring(4));
                    pass = true;
                } else if (linha.startsWith("ω = ")) {
                    axioma = linha.substring(4);
                    axi = true;
                } else if (linha.startsWith("δ = ") || linha.startsWith("a = ")) {
                    int simboloAngulo = linha.indexOf('°');
                    if (simboloAngulo == -1) {
                        simboloAngulo = linha.length();
                    }
                    angulo = Double.parseDouble(linha.substring(4, simboloAngulo));
                    ang = true;
                } else if (linha.startsWith("p") || linha.startsWith("P")) {
                    maiorRegra = processaRegra(linha.substring(4), regras, maiorRegra);
                    reg = true;
                } else {
                    throw new Exception("O arquivo não está formatado corretamente");
                }
            }

            if (gram && pass & axi && ang && reg) {
                axioma = converteString(axioma, gramatica);
                regras = converteRegras(regras, gramatica);

                Tratamento.recebeDados(angulo, passos, axioma, regras, maiorRegra);
            } else {
                throw new Exception("O arquivo não está formatado corretamente. Algum dos parametros não foi informado.");
            }
        } else {
            throw new Exception("Arquivo não encontrado");
        }
    }

    public static char[] processaGramatica(String gramatica) throws Exception {
        char[] retorno = new char[6];

        String[] parametros = gramatica.split(",");
        if (parametros.length == 6) {
            retorno[0] = parametros[0].trim().charAt(0);
            retorno[1] = parametros[1].trim().charAt(0);
            retorno[2] = parametros[2].trim().charAt(0);
            retorno[3] = parametros[3].trim().charAt(0);
            retorno[4] = parametros[4].trim().charAt(0);
            retorno[5] = parametros[5].trim().charAt(0);
        } else {
            throw new Exception("Linha gramatica não possui 6 parametros");
        }

        return retorno;
    }

    public static int processaRegra(String regra, HashMap regras, int maiorRegra) throws Exception {
        //F → F+F−F−F+F

        String[] r = regra.split("→");

        if (r.length == 2) {
            regras.putIfAbsent(r[0].trim(), r[1].trim());
        } else {
            throw new Exception("Há um comando invalido no arquivo");
        }

        if (r[0].length() > maiorRegra) {
            maiorRegra = r[0].trim().length();
        }

        return maiorRegra;
    }

    public static String converteString(String axioma, char[] gramatica) throws Exception {
        for (char c : axioma.toCharArray()) {
            if (c != ' ' && c != gramatica[0] && c != gramatica[1]
                    && c != gramatica[2] && c != gramatica[3] && c != gramatica[4]
                    && c != gramatica[5]) {
                throw new Exception("Um dos simbolos no axioma não pertence à gramatica");
            }
        }

        axioma = axioma.replace(gramatica[0], '+');
        axioma = axioma.replace(gramatica[1], '-');
        axioma = axioma.replace(gramatica[2], 'F');
        axioma = axioma.replace(gramatica[3], 'f');
        axioma = axioma.replace(gramatica[4], '[');
        axioma = axioma.replace(gramatica[5], ']');

        return axioma;
    }

    public static HashMap<String, String> converteRegras(HashMap<String, String> regras, char[] gramatica) throws Exception {
        HashMap<String, String> retorno = new HashMap<>();

        for (Map.Entry<String, String> par : regras.entrySet()) {
            retorno.put(converteString(par.getKey(), gramatica), converteString(par.getValue(), gramatica));
        }

        return retorno;
    }
}
