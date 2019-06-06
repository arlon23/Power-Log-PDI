/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infraBasica;

import java.awt.Color;
import java.math.BigDecimal;

public class Pixel {
      
    public Color c;
    public int X;
    public int Y;
    
    public int R; 
    public int G; 
    public int B; 
    
    public double Ye;
    public double Cb;
    public double Cr;

    //{0 - RGB | 1 - YCbCr | 2 - YUV | 3 - HSL | 4-HSV | 5 - HSI}
    public float espCor[][];
    
    public boolean fogo=false;
    
    public static double Ymean=0;
    public static double Cbmean=0;
    public static double Crmean=0;
    
    public static double totalY=0;
    public static double totalCb=0;
    public static double totalCr=0;
    public static int totalPx=0;
    
    public double CentroideR = 0;
    public double CentroideG = 0;
    public double CentroideB = 0;
    
    public double CentroideAntR = 0;
    public double CentroideAntG = 0;
    public double CentroideAntB = 0;    
    
    public double CentroideRGB = 0;
    public double CentroideAntRGB = 0;
    
    public int Cl=0; // classe, sendo 0 não fogo e 1 candidato a fogo
    public double D; // distancia
        
    public Pixel(){
         espCor = new float[6][3];
    }

    public Pixel(double cor, double cor0, double cor1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public void setPos(int x1,int y1){
        this.X = x1;
        this.Y = y1;
    }
    public int getX(){
        return this.X;       
    }
    public int getY(){
        return this.Y;
    }
    public float getCor(int ec){
        float col=0;
        switch(ec){
            case 1: col = getRGB()[0]; //R
                    break;
            case 2: col = getRGB()[1]; //G
                    break;
            case 3: col = getRGB()[2]; //B
                    break;
            case 4: col = getYCbCr()[0]; //Y
                    break;
            case 5: col = getYCbCr()[1]; //Cb
                    break;
            case 6: col = getYCbCr()[2]; //Cr
                    break;
            case 7: col = getHSV()[0]; //H
                    break;
            case 8: col = getHSV()[1]; //S
                    break;
            case 9: col = getHSV()[2]; //V
                    break;
            case 10: col = getHSL()[1]; //10 - S (HSL)                   
                    break;
            case 11: col = getHSL()[2]; //11-L (HSL)
                    break;
            case 12: col = getYUV()[0]; //12 - Y (YUV)
                    break;
            case 13: col = getYUV()[1]; // 13 - U (YUV)
                    break;
            case 14: col = getYUV()[2]; //14 - V (YUV)
                    break;
            case 15: col = getHSI()[0]; //15 - H (YUV)
                    break;
            case 16: col = getHSI()[1]; //16 - S (YUV)
                    break;
            case 17: col = getHSI()[2]; //17 - I (YUV)
                    break;
        }
        return col;
                 
    }
    
    public void setCor(int ec, float col){        
        switch(ec){
            case 1: espCor[0][0]= col; //R
                    R = (int) col*255;
                    break;
            case 2: espCor[0][1]= col; //G
                    G = (int) col*255;
                    break;
            case 3: espCor[0][2]= col; //B
                    B = (int) col*255;
                    break;
            case 4: espCor[1][0] = col; //Y
                    break;
            case 5: espCor[1][1]=col; //Cb
                    break;
            case 6: espCor[1][2] = col; //Cr
                    break;
            case 7: espCor[4][0] = col; //H
                    break;
            case 8: espCor[4][1] = col; //S
                    break;
            case 9: espCor[4][2]=col; //V
                    break;
            case 10: espCor[3][1]=col; //10 - S (HSL)                   
                    break;
            case 11: espCor[3][2]=col; //11-L (HSL)
                    break;
            case 12: espCor[2][0]=col; //12 - Y (YUV)
                    break;
            case 13: espCor[2][1]=col; // 13 - U (YUV)
                    break;
            case 14: espCor[2][2]=col; //|14 - V (YUV)
                    break;
            case 15: espCor[5][0]=col; //|15 - H 
                    break;
            case 16: espCor[5][1]=col; //|16 - S 
                    break;
            case 17: espCor[5][2]=col; //|17 - I 
                    break;
        }                         
    }
    public Pixel(int rr, int gg, int bb){
        espCor = new float[1][3];
        
        this.R = rr;
        this.G = gg;
        this.B = bb;
        c = new Color(rr, gg, bb);
        
        //RGB
        espCor[0][0] = rr/255f;
        espCor[0][1] = gg/255f;
        espCor[0][2] = bb/255f;
        //-----------------------------------
     
    }
    public float[] getRGB(){       
        return  espCor[0];
    }
    public float[] getYCbCr(){
        int rr=R;
        int gg=G;
        int bb=B;
        float yCbCr[] = new float[3];
        yCbCr[0] = ((0.2568f*rr + 0.5041f*gg + 0.0979f*bb) +16f)/255f; // Y
        yCbCr[1] = ((-0.1482f*rr + -0.2910f*gg + 0.4392f*bb)+128f)/255f; //Cb
        yCbCr[2] = ((0.4392f*rr + -0.3678f*gg + -0.0714f*bb)+128f)/255f; // Cr  
        return yCbCr;
    }
     public float[] getYUV(){
        float rgb[] = new float[3];
        rgb = getRGB();
        float yuv[] = new float[3];
        yuv[0] = (0.299f* rgb[0] + 0.587f* rgb[1] + 0.114f* rgb[2]); // Y
        yuv[1] = 0.492f*( rgb[2]) - yuv[0]; //U
        yuv[2] = 0.877f*( rgb[0]) - yuv[0]; // V
        return yuv;
    }
     public float[] getHSL(){
        float hsl[] = new float[3];
        float hsv[] = getHSV();
        hsl[0] = hsv[0]; // H
        hsl[1] = hsv[1]; //S
        hsl[2] = hsv[2] - hsv[1]/2; // L  
        return hsl;
    }
     public float[] getHSV(){
        int rr=R;
        int gg=G;
        int bb=B;
        float hsv[] = new float[3];
        Color.RGBtoHSB(rr,bb,gg,hsv);
        return hsv;
    }
     public float[] getHSI(){
       float hsi[] = new float[3];
        int rr=R;
        int gg=G;
        int bb=B;
        Color c2 = new Color(rr, gg, bb);
        double r1;
        double g1;
        double b1;
        if((rr+bb+gg)>0){
        r1 = rr/(rr+bb+gg);
        g1 = gg/(rr+bb+gg);
        b1 = bb/(rr+bb+gg);
        }
        else{
            r1=0;
            g1=0;
            b1=0;
        }
            
        double norm = 0;
        double omega = 0;
        double intensity = 0;
        double hue = 0;
        double saturation = 0;
        //Intensidade ok
        if (R == 0 && G == 0 && B == 0) {
            intensity = 0;
        } else {
            intensity = (double) (R + G + B) / (3.0 * 255);
        }
        if (Color.WHITE.getRGB() == c2.getRGB()) {
            saturation = 0;
            hue = 0;
            intensity = 1;
            //System.out.println("passou branco :)");
        } else if (Color.BLACK.getRGB() == c2.getRGB()) {
            saturation = 1;
            //System.out.println("passou preto");
        } else {
            double tmp = Math.min(r1, Math.min(g1, b1));
            saturation = 1.0 - (3.0 * tmp); /// (double) (r + g + b));
//            System.out.println(saturation + "Valor Satuaração");
            if (saturation < 0) {
                saturation = 0;
            } else if (saturation > 1) {
                saturation = 1;
            }
        }

        if (saturation != 0) {
            if (r1 + g1 + b1 == 0) {
                hue = 0;
            } else {
                omega = 0.5 * ((r1 - g1) + (r1 - b1)) / Math.sqrt(Math.pow((r1 - g1), 2) + (r1 - b1) * (g1 - b1));
                //System.out.println(omega + "valor de omega1;");
                omega = Math.acos(omega);
                //System.out.println(omega + "valor de omega;");
                if (b1 <= g1) {
                    hue = omega;
                } else {
                    hue = 2 * Math.PI - omega;
                    //System.out.println(hue + "valor de HUE;");
                }
            }
        }
        hsi[0] = (float)hue;     
        hsi[1] = (float)saturation;        
        hsi[2] = (float)intensity; 
         
        return hsi;
    }
     
    public void imprime(){
        float rgb[]=new float[3];
        float yCbCr[]=new float[3];
        float yUV[]=new float[3];
        float hsl[]=new float[3];
        float hsv[]=new float[3];
        float hsi[]=new float[3];
        rgb = getRGB();
        yCbCr = getYCbCr();
        yUV = getYUV();
        hsl = getHSL();
        hsi=getHSI();
        hsv = getHSV();
        System.out.println("RGB >> "+rgb[0]+" "+rgb[1]+" "+rgb[2]);
        System.out.println("YCbCr >> "+yCbCr[0]+" "+yCbCr[1]+" "+yCbCr[2]);        
        System.out.println("HSL >> "+hsl[0]+" "+hsl[1]+" "+hsl[2]);                
        System.out.println("HSV >> "+hsv[0]+" "+hsv[1]+" "+hsv[2]);
        System.out.println("YUV >> "+yUV[0]+" "+yUV[1]+" "+yUV[2]);
        System.out.println("HSI >> "+hsi[0]+" "+hsi[1]+" "+hsi[2]);
    }
     public String imprimeCSV_RGB(){
        String linha="";
        float rgb[]=new float[3];       
        rgb = getRGB();      
        linha  = linha.concat("("+X+","+Y+");"+rgb[0]+";"+rgb[1]+";"+rgb[2]+"\n");
           
        return linha;
    }
     public String imprimeCSV_YCbCr(){
        String linha="";
        float yCbCr[]=new float[3];
        yCbCr = getYCbCr();
        BigDecimal ych = new BigDecimal(yCbCr[0]).setScale(4,BigDecimal.ROUND_HALF_UP);
        BigDecimal cbch = new BigDecimal(yCbCr[1]).setScale(4,BigDecimal.ROUND_HALF_UP);
        BigDecimal crch = new BigDecimal(yCbCr[2]).setScale(4,BigDecimal.ROUND_HALF_UP);
        
        linha  = linha.concat("("+X+","+Y+")"+";"+ych+";"+cbch+";"+crch+"\n");                   
        return linha;
    }
    public Integer getValorDoRGB(int RGB) {
        int temp = 0xFF & RGB;// 0x000000FF & 0xBBBBBBTT -> [000000TT] : [0..255]
        return temp;
    }    
    
    public int CalcularDistanciaEuclidiana( Pixel pCentroide ){
        int retorno = 0;
        // calcula a raiz quadrada (sqrt) da soma do cálculo da potencia (pow) das três camadas (R, G e B)
        retorno = (int)  Math.sqrt(  
                            Math.pow((pCentroide.CentroideR-this.R), 2) +
                            Math.pow((pCentroide.CentroideG-this.G), 2) +
                            Math.pow((pCentroide.CentroideB-this.B), 2)    
                                    ) ;
        
        return retorno;
    }
    
    
    public double CalcularDistanciaEuclidianaD( Pixel pCentroide ){
        double retorno = 0;
        // calcula a raiz quadrada (sqrt) da soma do cálculo da potencia (pow) das três camadas (R, G e B)
        retorno =   Math.sqrt(  
                            Math.pow((pCentroide.CentroideR-this.R), 2) +
                            Math.pow((pCentroide.CentroideG-this.G), 2) +
                            Math.pow((pCentroide.CentroideB-this.B), 2)    
                                    ) ;
        
        return retorno;
    }
    
    public void InicializarCentroides(){
        this.CentroideR = this.R;
        this.CentroideG = this.G;
        this.CentroideB = this.B;        
        this.CentroideAntR = this.R;
        this.CentroideAntG = this.G;
        this.CentroideAntB = this.B;            
    }

    public double getVlrCentroides(){
        double retorno = (CentroideR + CentroideG + CentroideB);
        //System.out.println("    Valor dos Centróides:  " + retorno );
        return (int)retorno;
    }

    public double getVlrCentroidesAnt(){
        double retorno = (CentroideAntR + CentroideAntG + CentroideAntB);
        //System.out.println("    Valor dos Centróides Ant:  " + retorno );
        return (int)retorno;
    }
    
    public void setCentroideR(double CentroideR) {        
        this.CentroideAntR = this.CentroideR;
        this.CentroideR = CentroideR;
        
        //System.out.println("   Alterando centróide R  |  Anterior: " + this.CentroideAntR + "    Atual: " + this.CentroideR  );
    }

    public void setCentroideG(double CentroideG) {
        this.CentroideAntG = this.CentroideG;
        this.CentroideG = CentroideG;
    }
    
    public void setCentroideB(double CentroideB) {
        this.CentroideAntB = this.CentroideB;
        this.CentroideB = CentroideB;
    }

    
    public void setCentroideRGB(double CentroideRGB) {
        this.CentroideAntRGB = this.CentroideRGB;
        this.CentroideRGB = CentroideRGB;
    }
    
    
    public boolean CompararCentroides(){
        if ( this.CentroideAntR != this.CentroideR ) { return false;  }
        if ( this.CentroideAntG != this.CentroideG ) { return false;  }
        if ( this.CentroideAntB != this.CentroideB ) { return false;  }
        
        return true;
    }

    
    
    
    public void setCentrosDefault() {
        float part = 1.0f / (float) (4);
        for (int i = 0; i < 4; i++) {
            double teste = Math.floor((i + 1) * part * 255);
            //System.out.println("Teste " + i + ": " + teste );
        }
    }            
        
}
