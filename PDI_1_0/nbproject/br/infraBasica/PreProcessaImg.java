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
public class PreProcessaImg {

    ListaPixel Imagem1 = new ListaPixel();

    public double max(double r, double g, double b) {
        if (r >= g && r >= b) {
            return r;
        } else if (g >= r && g >= b) {
            return g;
        } else {
            return b;
        }
    }

    public double min(double r, double g, double b) {
        if (r <= g && r <= b) {
            return r;
        } else if (g <= r && g <= b) {
            return g;
        } else {
            return b;
        }
    }

    public BufferedImage pintaFogo(ListaPixel imagem, BufferedImage ori, BufferedImage res) {
        int Cores[] = {Color.YELLOW.getRGB()};
        int Contador = 0;
        for (int x = 0; x < res.getWidth(); x++) {
            for (int y = 0; y < res.getHeight(); y++) {
                Pixel pPixel = Imagem1.get(Contador);
                if (pPixel.fogo) {
                    res.setRGB(x, y, Cores[0]);
                } else {
                    res.setRGB(x, y, ori.getRGB(x, y));
                }
                Contador = Contador + 1;
            }
        }
        return res;
    }

    public int[][] retornaImgBin(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        int imgBin[][] = new int[w][h];
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                Color c = new Color(img.getRGB(x, y));
                //evita erros das imagens JPG
                if (c.getRed() < 20 && c.getBlue() < 20 && c.getGreen() < 20) {
                    imgBin[x][y] = 0;//cor preta            
                } else {
                    imgBin[x][y] = 1;
                }
            }
        }
        return imgBin;
    }

    public BufferedImage retornaBinToImagem(int imgBin[][], BufferedImage imageRes) {
        int w = imgBin.length;
        int h = imgBin[0].length;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (imgBin[i][j] == 1) {
                    imageRes.setRGB(i, j, Color.WHITE.getRGB());
                } else {
                    imageRes.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }
        return imageRes;
    }

    //Le a imagem e a transforma em uma lista de pixel

    public ListaPixel leImg(BufferedImage bufImg1) {
        for (int x = 0; x < bufImg1.getWidth(); x++) {
            for (int y = 0; y < bufImg1.getHeight(); y++) {
                Color c = new Color(bufImg1.getRGB(x, y));
                Pixel pPixel = new Pixel(c.getRed(), c.getGreen(), c.getBlue());
                pPixel.c = c;
                pPixel.X = x;
                pPixel.Y = y;
                Imagem1.add(pPixel);
            }
        }
        return Imagem1;
    }
}
