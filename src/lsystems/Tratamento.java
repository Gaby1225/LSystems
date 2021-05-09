/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lsystems;
import java.util.HashMap;

/**
 *
 * @author Administrador
 */
public class Tratamento
{    
    public static void recebeDados(double angulo, int iteracoes, String axioma, HashMap<String, String> regras, int maiorRegra){
        String analizar = axioma;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iteracoes; i++){
            sb.setLength(0);
            while(analizar.length() > 0){
                if (maiorRegra <= analizar.length()){
                    for (int j = maiorRegra; j > 0; j--){
                        if (regras.containsKey(analizar.substring(0, j))){
                            sb.append(regras.get(analizar.substring(0,j)));
                            if (analizar.substring(0,j).length() >= analizar.length())
                                analizar = "";
                            else
                                analizar = analizar.substring(j);
                        }
                    }
                    if (analizar.length() > 0 && !regras.containsKey(analizar.substring(0,1))){
                        sb.append(analizar.charAt(0));
                        analizar = analizar.substring(1);
                        
                    }
                }
                else{
                    for (int j = analizar.length(); j > 0; j--){
                        if (regras.containsKey(analizar.substring(0, j))){
                            sb.append(regras.get(analizar.substring(0,j)));
                            if (analizar.substring(0,j).length() >= analizar.length())
                                analizar = "";
                            else
                                analizar = analizar.substring(j);
                        }
                    }
                    if (analizar.length() > 0 && !regras.containsKey(analizar.substring(0,1))){
                        sb.append(analizar.charAt(0));
                        analizar = analizar.substring(1);
                    }
                }
            }
            analizar = sb.toString();
        }
        System.out.println(sb.toString());
        Desenho.desenhaSVG(angulo, sb.toString());

    }
    

}
