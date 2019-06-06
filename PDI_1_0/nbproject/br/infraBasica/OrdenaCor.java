/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infraBasica;

/**
 *
 * @author Bruno
 */
public class OrdenaCor {
    private float cores[];
    private int tam;
    public OrdenaCor(int t){
        cores = new float[t];
        tam=0;
    }
    //insere elementos de forma ordenada!
    public void insere(float c){
        if(tam==0){            
            cores[tam]=c;                        
        }else{
            for(int i=tam;i>0;i--){
                if(cores[i-1]>c)
                    cores[i]=cores[i-1];
                else{
                    cores[i] = c;
                    i=0;
                }
            }
        }
        tam++;
    }
    public int getTamanho(){
        return tam;
    }
    public float mediana(){
        float med;
        int x = tam/2;
        if(tam%2!=0)            
            med = (cores[x]+cores[x+1])/2;        
        else
            med = cores[x];
        return med;
    }
    
}
