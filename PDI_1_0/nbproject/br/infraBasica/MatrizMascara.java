/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infraBasica;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Bruno
 */
public class MatrizMascara {
    //Define o tamanho da matriz e seus vizinhos para considerar nos calculos
    
    public BufferedImage convolucao(float[][] mascara, BufferedImage img){
        BufferedImage imgRes = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        int w = img.getWidth();
        int h = img.getHeight();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {                
                int soma=0;
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        float val = mascara[i+1][j+1];
                        if(val!=0){
                            if(x+i>=0 && y+j>=0 && (x+i)< w && (y+j)<h){
                                Color c = new Color(img.getRGB(x+i, y+j));
                                int corRes = (c.getRed() + c.getGreen() + c.getBlue())/3;
                                soma += corRes*val;
                            }//if
                        }//if                        
                    }//j - coluna da mascara                    
                }//i - linha da mascara
                
                int valorRGB = (soma>255)?255:soma;
                valorRGB = (valorRGB<0)?0:valorRGB;
                Color corNOva = new Color(valorRGB,valorRGB,valorRGB);
                imgRes.setRGB(x, y, corNOva.getRGB());
            }//coluna da imagem            
        }//linha da imagem
        return imgRes;
    }
    
    public BufferedImage convolucaoCor(float[][] mascara, BufferedImage img){
        BufferedImage imgRes = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        int w = img.getWidth();
        int h = img.getHeight();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {                
                int somaR=0;
                int somaG = 0;
                int somaB = 0;
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        float val = mascara[i+1][j+1];
                        if(val!=0){
                            if(x+i>=0 && y+j>=0 && (x+i)< w && (y+j)<h){
                                Color c = new Color(img.getRGB(x+i, y+j));                                
                                
                                somaR += c.getRed()*val;
                                somaG += c.getGreen()*val;
                                somaB += c.getBlue()*val;
                            }//if
                        }//if                        
                    }//j - coluna da mascara                    
                }//i - linha da mascara
                
                int valorR = (somaR>255)?255:somaR;
                valorR = (valorR<0)?0:valorR;
                
                int valorG = (somaG>255)?255:somaG;
                valorG = (valorG<0)?0:valorG;
                
                int valorB = (somaB>255)?255:somaB;
                valorB = (valorB<0)?0:valorB;
                
                Color corNOva = new Color(valorR,valorG,valorB);
                imgRes.setRGB(x, y, corNOva.getRGB());
            }//coluna da imagem            
        }//linha da imagem
        return imgRes;
    }
            
    //ordem tem que ser impar!
    public float[] retMatriz(int ordem, int x, int y, int i2, float[][][] corOrigem){
        int tam  = ordem*ordem;
        float[] resultado = new float[tam];
        if(ordem%2!=0){
            int fator = ordem/2;            
            int cont=0;
            for(int j=-fator;j<=fator;j++){
                for(int i=-fator;i<=fator;i++){
                    if( (x+i)>=0 && (y+j)>=0 && (x+i)<corOrigem.length && (y+j)<corOrigem[0].length)
                        resultado[cont] = corOrigem[x+i][y+j][i2];
                    else
                        resultado[cont]=-1;
                    cont++;
                }
            }
        }        
        return resultado;
    }
    public float mediaMatr(int ordem, int x, int y, int i2, float[][][] corOrigem){
        int tam  = ordem*ordem;
        float[] resultado;
        resultado = retMatriz(ordem, x, y, i2, corOrigem);
        float somaX=0;
        int contN=0;
        for (int i = 0; i < tam; i++) {
           if(resultado[i]!=-1)
            somaX=somaX+resultado[i];
           else
              contN++; 
        }        
        return somaX/(tam-contN);
    }
    
    public float variMatr(int ordem, int x, int y, int i2, float[][][] corOrigem, float[][][] mecs){
        int tam  = ordem*ordem;
        float[] resultado;
        resultado = retMatriz(ordem, x, y, i2, corOrigem);
        float somaX=0;
        int contN=0;
        for (int i = 0; i < tam; i++) {
           if(resultado[i]!=-1)
            somaX+= Math.pow(resultado[i] - mecs[x][y][i2], 2.0);
           else
              contN++; 
        }        
        return somaX/(tam-contN);
    }
    
    public float skewMatr(int ordem, int x, int y, int i2, float[][][] corOrigem, float[][][] mecs){
        int tam  = ordem*ordem;
        float[] resultado;
        resultado = retMatriz(ordem, x, y, i2, corOrigem);
        float somaX=0;
        float somaX2=0;
        int contN=0;
        for (int i = 0; i < tam; i++) {
           if(resultado[i]!=-1){
                somaX+= Math.pow(resultado[i] - mecs[x][y][i2], 3.0);
                somaX2+=Math.pow(resultado[i] - mecs[x][y][i2], 2.0);
           }
           else
              contN++; 
        }    
        
        return (float) ((somaX / (tam-contN)) / Math.pow((somaX2 / (tam-contN)), 1.5));
    }
    
    public float curtMatr(int ordem, int x, int y, int i2, float[][][] corOrigem, float[][][] mecs){
        int tam  = ordem*ordem;
        float[] resultado;
        resultado = retMatriz(ordem, x, y, i2, corOrigem);
        float somaX=0;
        float somaX2=0;
        int contN=0;
        for (int i = 0; i < tam; i++) {
           if(resultado[i]!=-1){
                somaX+= Math.pow(resultado[i] - mecs[x][y][i2], 4.0);
                somaX2+=Math.pow(resultado[i] - mecs[x][y][i2], 2.0);
           }
           else
              contN++; 
        }    
        
        return (float) ((somaX / (tam-contN)) / Math.pow((somaX2 / (tam-contN)), 2.0));
    }
    
}
