/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lsystems;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author Administrador
 */
public class Desenho
{
    
    public static void desenhaSVG(double angulo, String saida) throws IOException
    {
        StringBuilder sb = new StringBuilder(); 
        sb.append("<!DOCTYPE html>\n" +
                  "<html>\n" +
                  "<head>\n" +
                  "<title>Page Title</title>\n" +
                  "</head>\n" +
                  "<body>\n" +
                  "<svg width=\"1600\" height=\"1600\">\n");
        double step = 5, stepx = 0, stepy = 5, x1 = 800, x2 = 800, y1 = 800, y2 = 800;
        double anguloAtual = 0;
        Stack<double[]> pilha = new Stack<double[]>();
        double[] vetor = new double[4];
        for(char c : saida.toCharArray()){
            if (c == 'F'){
                x2 += stepx;
                y2 += stepy;
                sb.append("<line x1=\"" + x1 + "\" x2=\"" + x2  + "\" y1=\"" + y1  + "\" y2=\"" + y2  + "\" style= \"stroke:rgb(255,0,0); stroke-width:2\"/>\n");
                x1 = x2;                
                y1 = y2;                
            }
            else if (c == 'f'){
                x2 += stepx;
                x1 = x2;
                y2 += stepy;
                y1 = y2;                
            }
            else if (c == '+'){
                anguloAtual = anguloAtual - angulo;
                if (anguloAtual < 0)
                    anguloAtual = 360 + anguloAtual;
                stepy = (Math.round(Math.cos(anguloAtual * Math.PI / 180) * 100) / 100) * step;
                stepx = (Math.round(Math.sin(anguloAtual * Math.PI / 180) * 100) / 100) * step;
            }
            else if (c == '-'){
                anguloAtual = anguloAtual + angulo;
                if (anguloAtual > 360)
                    anguloAtual = anguloAtual - 360;
                stepy = (Math.round(Math.cos(anguloAtual * Math.PI / 180) * 100) / 100) * step;
                stepx = (Math.round(Math.sin(anguloAtual * Math.PI / 180) * 100) / 100) * step;
            }
            else if (c == '['){
                vetor[0] = x1;
                vetor[1] = x2;
                vetor[2] = y1;
                vetor[3] = y2;
                pilha.push(vetor);
            }
            else{
                vetor = pilha.pop();
                x1 = vetor[0];
                x2 = vetor[1];
                y1 = vetor[2];
                y2 = vetor[3];
            }
        }
        sb.append("</body>\n" +
                  "</html>");
        File file = new File("desenho.svg");
        if (!file.createNewFile()){
            file.delete();
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file);
        writer.write(sb.toString());
        writer.close();
    }
        
}
