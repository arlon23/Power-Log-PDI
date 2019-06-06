/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infraBasica;

import java.util.ArrayList;

public class ListaPixel {
     public ArrayList<Pixel> registros = new ArrayList();
    
    public double TotalDAnt = 0;
    public double TotalD = 0;

    public double TotalR = 0;
    public double TotalG = 0;
    public double TotalB = 0;
    
    public double totalCor[] = new double[17];

    public void add(Pixel pPixel){
        this.registros.add(pPixel);
        
        this.TotalR = this.TotalR + pPixel.R; 
        this.TotalG = this.TotalG + pPixel.G;
        this.TotalB = this.TotalB + pPixel.B;
        for (int i = 0; i < totalCor.length; i++) {
            totalCor[i] = totalCor[i]+pPixel.getCor(i+1);            
        }                
        //this.TotalD = this.TotalD + pPixel.D;
        this.TotalD = pPixel.D;
    
    }
    
   public float getMediaCor(int cor){
       return (float) (totalCor[cor]/registros.size());
   }
    public double getMD(){
        return   ( (TotalR / size()) + (TotalG / size()) + (TotalB / size()) ) / 3 ;
    }


    public double getMediaR(){
        return   (TotalR / size());
    }
    public double getMediaG(){
        return   (TotalG / size());
    }
    public double getMediaB(){
        return   (TotalB / size());
    }
    public Pixel get(int Indice){
        return this.registros.get(Indice);
    }
    
    public int size(){
        return this.registros.size();
    }
    
    public void InicializarLista(){
        this.registros.clear();
        this.TotalD = 0;
        this.TotalR = 0;
        this.TotalG = 0;
        this.TotalB = 0;
    }   
    public boolean localizaXY(int x,int y){
        for (int i = 0; i < size(); i++) {
            Pixel p = registros.get(i);                                                            
            if(x == p.getX() && y==p.getY())
                return true;
        }
        return false;
    }
    public Pixel getPixelXY(int x,int y){
        for (int i = 0; i < size(); i++) {
            Pixel p = registros.get(i);                                                            
            if(x == p.getX() && y==p.getY())
                return p;
        }
        return null;
    }
    public int localizaXYPos(int x,int y){
        for (int i = 0; i < size(); i++) {
            Pixel p = registros.get(i);                                                            
            if(x == p.getX() && y==p.getY())
                return i;
        }
        return -1;
    }
    
}
