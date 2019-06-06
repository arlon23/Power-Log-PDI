/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdi;

import java.awt.image.BufferedImage;
import infraBasica.PreProcessaImg;

/**
 *
 * @author BrunoMiguel
 */
public class MorfologiaBinaria {
    
    PreProcessaImg preimg = new PreProcessaImg();
    
    int HORIZONTAL[][] =   {{0,0,0},
                            {1,1,1},
                            {0,0,0}}; 
    int VERTICAL[][] =      {{0,1,0},
                            {0,1,0},
                            {0,1,0}}; 
    int CRUZ[][] =          {{0,1,0},
                             {1,1,1},
                             {0,1,0}};     
    int QUADRADO[][] =      {{1,1,1},
                             {1,1,1},
                             {1,1,1}};    
    int ROMBUS[][] =      {{0,0,0,0,0,0,0},
                           {0,0,0,1,0,0,0},
                           {0,0,1,1,1,0,0},
                           {0,1,1,1,1,1,0},
                           {0,0,1,1,1,0,0},
                           {0,0,0,1,0,0,0},
                           {0,0,0,0,0,0,0}}; 
    
    public int[][] selecionaEE(String ee){
        int elementoEstru[][];
        switch(ee){
            case "horizontal": elementoEstru = HORIZONTAL;
                                break;
            case "vertical": elementoEstru = VERTICAL;
                                break;
            case "cruz": elementoEstru = CRUZ;
                                break;
            case "quadrado": elementoEstru = QUADRADO;
                                break;
            case "rombus": elementoEstru = ROMBUS;
                                break;
            default : elementoEstru = QUADRADO;
                                break;    
        }
        return elementoEstru;
    }
       
    public BufferedImage erosao(BufferedImage img, String ee){
        /*
        EROSÃO: min(f(y):y pertence a elementoEstru)
        */
        int w = img.getWidth();
        int h = img.getHeight();
        int elementoEstru[][] = selecionaEE(ee);   
        int inicio = elementoEstru.length;
        inicio = inicio/2;
        BufferedImage imageRes = new BufferedImage(w, h, img.getType());
        
        int imgBin[][] = preimg.retornaImgBin(img);
        int imgRes[][] = new int[w][h];
        for(int i=0; i<w;i++)
            for(int j=0;j<h;j++)
                imgRes[i][j]=imgBin[i][j];
        
        for(int x=inicio;x<w-inicio;x++){
            for(int y=inicio;y<h-inicio;y++){
                
                //acessando elemento estruturante!
                int min=10;
                for(int i=-inicio; i<=inicio; i++){
                    for(int j=-inicio; j<=inicio;j++){
                        int val = elementoEstru[i+inicio][j+inicio];
                        if(val!=0){
                            if(x+i>=0 && y+j>=0 && (x+i)< w && (y+j)<h){
                                int valImg = imgBin[x+i][y+j];
                                if(valImg < min)
                                    min = valImg;                                
                            }//if
                        }//if 
                    }
                }//for = elemento estruturante
                imgRes[x][y]=min;
            }
        }
        
       return imageRes = preimg.retornaBinToImagem(imgRes, imageRes);
    }
    
    
    public BufferedImage dilatacao(BufferedImage img, String ee){
        /*
        DILATACAO: max(f(y):y pertence a elementoEstru)
        */
        int w = img.getWidth();
        int h = img.getHeight();
        int elementoEstru[][] = selecionaEE(ee);   
        int inicio = elementoEstru.length;
        inicio = inicio/2;
        BufferedImage imageRes = new BufferedImage(w, h, img.getType());
        
        int imgBin[][] = preimg.retornaImgBin(img);
        int imgRes[][] = new int[w][h];
        for(int i=0; i<w;i++)
            for(int j=0;j<h;j++)
                imgRes[i][j]=imgBin[i][j];
        
        for(int x=inicio;x<w-inicio;x++){
            for(int y=inicio;y<h-inicio;y++){
                
                //acessando elemento estruturante!
                int max=-10;
                for(int i=-inicio; i<=inicio; i++){
                    for(int j=-inicio; j<=inicio;j++){
                        int val = elementoEstru[i+inicio][j+inicio];
                        if(val!=0){
                            if(x+i>=0 && y+j>=0 && (x+i)< w && (y+j)<h){
                                int valImg = imgBin[x+i][y+j];
                                if(valImg > max)
                                    max = valImg;                                
                            }//if
                        }//if 
                    }
                }//for = elemento estruturante
                imgRes[x][y]=max;
            }
        }
         return imageRes = preimg.retornaBinToImagem(imgRes, imageRes);
    }
    
    public BufferedImage abertura(BufferedImage img, String ee){
        int w = img.getWidth();
        int h = img.getHeight();       
        BufferedImage imageRes = new BufferedImage(w, h, img.getType());
        imageRes = erosao(img,ee);
        imageRes = dilatacao(imageRes,ee);
        return imageRes;
    }
    
    public BufferedImage fechamento(BufferedImage img, String ee){
        int w = img.getWidth();
        int h = img.getHeight();       
        BufferedImage imageRes = new BufferedImage(w, h, img.getType());
        imageRes = dilatacao(img,ee);
        imageRes = erosao(imageRes,ee);
        return imageRes;        
    }
    
}
