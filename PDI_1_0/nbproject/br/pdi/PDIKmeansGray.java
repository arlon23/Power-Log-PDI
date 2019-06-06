package pdi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import interfaces.ImageInterface;
import swing.Conf;

@SuppressWarnings("serial")
public class PDIKmeansGray extends JComponent implements ImageInterface {

    private BufferedImage image;
    private Color[] cores = new Color[10];

    private float[][] c1;
    private float[][] c2;
    private float[][] c3;
    private float[][] mec1;
    private float[][] mec2;
    private float[][] mec3;
    private float[][] vac1;
    private float[][] vac2;
    private float[][] vac3;
    private float[][] skc1;
    private float[][] skc2;
    private float[][] skc3;
    private float[][] csc1;
    private float[][] csc2;
    private float[][] csc3;

    boolean useColor = Conf.feature_color;
    boolean useMedia = Conf.feature_media;
    boolean useVariancia = Conf.feature_variancia;
    boolean useSkewness = Conf.feature_skewness;
    boolean useCurtose = Conf.feature_curtose;

    public PDIKmeansGray(BufferedImage img) {
        initCores();
        initCaracteristicas(img);
        image = processaKmeans(img, Conf.kmeans_classes);
    }

    /*private*/ public BufferedImage processaKmeans(BufferedImage image, int n) {
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        // ************************************
        // *** Declarações e inicializações ***
        // ************************************
        // *** Declarando e definindo os primeiros centróides
        float[] centroc1 = new float[n];
        float[] centroc2 = new float[n];
        float[] centroc3 = new float[n];
        float[] centromec1 = new float[n];
        float[] centromec2 = new float[n];
        float[] centromec3 = new float[n];
        float[] centrovac1 = new float[n];
        float[] centrovac2 = new float[n];
        float[] centrovac3 = new float[n];
        float[] centroskc1 = new float[n];
        float[] centroskc2 = new float[n];
        float[] centroskc3 = new float[n];
        float[] centrocsc1 = new float[n];
        float[] centrocsc2 = new float[n];
        float[] centrocsc3 = new float[n];
        int px = (int) (image.getWidth() / (n + 1));
        int py = (int) (image.getHeight() / (n + 1));
        for (int i = 0; i < n; i++) {
            if (useColor) {
                centroc1[i] = c1[(i + 1) * px][(i + 1) * py];
                centroc2[i] = c2[(i + 1) * px][(i + 1) * py];
                centroc3[i] = c3[(i + 1) * px][(i + 1) * py];
            }
            if (useMedia) {
                centromec1[i] = mec1[(i + 1) * px][(i + 1) * py];
                centromec2[i] = mec2[(i + 1) * px][(i + 1) * py];
                centromec3[i] = mec3[(i + 1) * px][(i + 1) * py];
            }
            if (useVariancia) {
                centrovac1[i] = vac1[(i + 1) * px][(i + 1) * py];
                centrovac2[i] = vac2[(i + 1) * px][(i + 1) * py];
                centrovac3[i] = vac3[(i + 1) * px][(i + 1) * py];
            }
            if (useSkewness) {
                centroskc1[i] = skc1[(i + 1) * px][(i + 1) * py];
                centroskc2[i] = skc2[(i + 1) * px][(i + 1) * py];
                centroskc3[i] = skc3[(i + 1) * px][(i + 1) * py];
            }
            if (useCurtose) {
                centrocsc1[i] = csc1[(i + 1) * px][(i + 1) * py];
                centrocsc2[i] = csc2[(i + 1) * px][(i + 1) * py];
                centrocsc3[i] = csc3[(i + 1) * px][(i + 1) * py];
            }
        }

        // *** Declarando e inicializando as estruturas que calcularão os novos centróides
        float[] somaNovoCentroc1 = new float[n];
        float[] somaNovoCentroc2 = new float[n];
        float[] somaNovoCentroc3 = new float[n];
        float[] somaNovoCentromec1 = new float[n];
        float[] somaNovoCentromec2 = new float[n];
        float[] somaNovoCentromec3 = new float[n];
        float[] somaNovoCentrovac1 = new float[n];
        float[] somaNovoCentrovac2 = new float[n];
        float[] somaNovoCentrovac3 = new float[n];
        float[] somaNovoCentroskc1 = new float[n];
        float[] somaNovoCentroskc2 = new float[n];
        float[] somaNovoCentroskc3 = new float[n];
        float[] somaNovoCentrocsc1 = new float[n];
        float[] somaNovoCentrocsc2 = new float[n];
        float[] somaNovoCentrocsc3 = new float[n];
        int[] count = new int[n];
        for (int i = 0; i < n; i++) {
            somaNovoCentroc1[i] = 0;
            somaNovoCentroc2[i] = 0;
            somaNovoCentroc3[i] = 0;
            somaNovoCentromec1[i] = 0;
            somaNovoCentromec2[i] = 0;
            somaNovoCentromec3[i] = 0;
            somaNovoCentrovac1[i] = 0;
            somaNovoCentrovac2[i] = 0;
            somaNovoCentrovac3[i] = 0;
            somaNovoCentroskc1[i] = 0;
            somaNovoCentroskc2[i] = 0;
            somaNovoCentroskc3[i] = 0;
            somaNovoCentrocsc1[i] = 0;
            somaNovoCentrocsc2[i] = 0;
            somaNovoCentrocsc3[i] = 0;
            count[i] = 0;
        }
        // *** Declarando e inicializando os vetores de classe
        int[][] k = new int[image.getWidth()][image.getHeight()];
        int[][] oldk = new int[image.getWidth()][image.getHeight()];
        int changek = 0;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                k[x][y] = 0;
                oldk[x][y] = 0;
            }
        }

        // **************************
        // *** Corpo do Algoritmo ***
        // **************************
        // *** Cálculo das distâncias euclidianas ***
        for (int itera = 0; itera < 500; itera++) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    float maiorDist = 32765;

                    for (int i = 0; i < n; i++) {
                        float soma = 0;
                        if (useColor) {
                            soma += Math.pow(c1[x][y] - centroc1[i], 2.0);
                            soma += Math.pow(c2[x][y] - centroc2[i], 2.0);
                            soma += Math.pow(c3[x][y] - centroc3[i], 2.0);
                        }
                        if (useMedia) {
                            soma += Math.pow(mec1[x][y] - centromec1[i], 2.0);
                            soma += Math.pow(mec2[x][y] - centromec2[i], 2.0);
                            soma += Math.pow(mec3[x][y] - centromec3[i], 2.0);
                        }
                        if (useVariancia) {
                            soma += Math.pow(vac1[x][y] - centrovac1[i], 2.0);
                            soma += Math.pow(vac2[x][y] - centrovac2[i], 2.0);
                            soma += Math.pow(vac3[x][y] - centrovac3[i], 2.0);
                        }
                        if (useSkewness) {
                            soma += Math.pow(skc1[x][y] - centroskc1[i], 2.0);
                            soma += Math.pow(skc2[x][y] - centroskc2[i], 2.0);
                            soma += Math.pow(skc3[x][y] - centroskc3[i], 2.0);
                        }
                        if (useCurtose) {
                            soma += Math.pow(csc1[x][y] - centrocsc1[i], 2.0);
                            soma += Math.pow(csc2[x][y] - centrocsc2[i], 2.0);
                            soma += Math.pow(csc3[x][y] - centrocsc3[i], 2.0);
                        }
                        float dist = (float) Math.sqrt(soma);

                        if (dist < maiorDist) {
                            maiorDist = dist;
                            k[x][y] = i;
                        }
                    }

                    if (k[x][y] != oldk[x][y]) {
                        changek++;
                        oldk[x][y] = k[x][y];
                    }

                    // Somando para o cálculo dos novos centróides
                    if (useColor) {
                        somaNovoCentroc1[k[x][y]] += c1[x][y];
                        somaNovoCentroc2[k[x][y]] += c2[x][y];
                        somaNovoCentroc3[k[x][y]] += c3[x][y];
                    }
                    if (useMedia) {
                        somaNovoCentromec1[k[x][y]] += mec1[x][y];
                        somaNovoCentromec2[k[x][y]] += mec2[x][y];
                        somaNovoCentromec3[k[x][y]] += mec3[x][y];
                    }
                    if (useVariancia) {
                        somaNovoCentrovac1[k[x][y]] += vac1[x][y];
                        somaNovoCentrovac2[k[x][y]] += vac2[x][y];
                        somaNovoCentrovac3[k[x][y]] += vac3[x][y];
                    }
                    if (useSkewness) {
                        somaNovoCentroskc1[k[x][y]] += skc1[x][y];
                        somaNovoCentroskc2[k[x][y]] += skc2[x][y];
                        somaNovoCentroskc3[k[x][y]] += skc3[x][y];
                    }
                    if (useCurtose) {
                        somaNovoCentrocsc1[k[x][y]] += csc1[x][y];
                        somaNovoCentrocsc2[k[x][y]] += csc2[x][y];
                        somaNovoCentrocsc3[k[x][y]] += csc3[x][y];
                    }
                    count[k[x][y]]++;
                }
            }
            // *** Cálculo dos novos centróides
            for (int i = 0; i < n; i++) {
                if (useColor) {
                    centroc1[i] = somaNovoCentroc1[i] / count[i];
                    centroc2[i] = somaNovoCentroc2[i] / count[i];
                    centroc3[i] = somaNovoCentroc3[i] / count[i];
                }
                if (useMedia) {
                    centromec1[i] = somaNovoCentromec1[i] / count[i];
                    centromec2[i] = somaNovoCentromec2[i] / count[i];
                    centromec3[i] = somaNovoCentromec3[i] / count[i];
                }
                if (useVariancia) {
                    centrovac1[i] = somaNovoCentrovac1[i] / count[i];
                    centrovac2[i] = somaNovoCentrovac2[i] / count[i];
                    centrovac3[i] = somaNovoCentrovac3[i] / count[i];
                }
                if (useSkewness) {
                    centroskc1[i] = somaNovoCentroskc1[i] / count[i];
                    centroskc2[i] = somaNovoCentroskc2[i] / count[i];
                    centroskc3[i] = somaNovoCentroskc3[i] / count[i];
                }
                if (useCurtose) {
                    centrocsc1[i] = somaNovoCentrocsc1[i] / count[i];
                    centrocsc2[i] = somaNovoCentrocsc2[i] / count[i];
                    centrocsc3[i] = somaNovoCentrocsc3[i] / count[i];
                }
            }
            for (int i = 0; i < n; i++) {
                somaNovoCentroc1[i] = 0;
                somaNovoCentroc2[i] = 0;
                somaNovoCentroc3[i] = 0;
                somaNovoCentromec1[i] = 0;
                somaNovoCentromec2[i] = 0;
                somaNovoCentromec3[i] = 0;
                somaNovoCentrovac1[i] = 0;
                somaNovoCentrovac2[i] = 0;
                somaNovoCentrovac3[i] = 0;
                somaNovoCentroskc1[i] = 0;
                somaNovoCentroskc2[i] = 0;
                somaNovoCentroskc3[i] = 0;
                somaNovoCentrocsc1[i] = 0;
                somaNovoCentrocsc2[i] = 0;
                somaNovoCentrocsc3[i] = 0;
                count[i] = 0;
            }
            System.out.println("Changes: " + changek);
            if (changek < image.getWidth() * image.getHeight() * 0.001) {
                break;
            }
            changek = 0;
        }

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                out.setRGB(x, y, cores[k[x][y]].getRGB());
            }
        }

        Conf.classes = new int[image.getWidth()][image.getHeight()];
        Conf.classes = k;

        return out;
    }

    private void initCores() {
        cores[0] = Color.WHITE;
        cores[1] = Color.BLACK;
        cores[2] = Color.BLUE;
        cores[3] = Color.YELLOW;
        cores[4] = Color.CYAN;
        cores[5] = Color.MAGENTA;
        cores[6] = Color.ORANGE;
        cores[7] = Color.GREEN;
        cores[8] = Color.DARK_GRAY;
        cores[9] = Color.RED;
    }

    private void initCaracteristicas(BufferedImage image) {
        c1 = new float[image.getWidth()][image.getHeight()];
        c2 = new float[image.getWidth()][image.getHeight()];
        c3 = new float[image.getWidth()][image.getHeight()];
        if (useMedia || useVariancia || useSkewness || useCurtose) {
            mec1 = new float[image.getWidth()][image.getHeight()];
            mec2 = new float[image.getWidth()][image.getHeight()];
            mec3 = new float[image.getWidth()][image.getHeight()];
        }
        if (useVariancia) {
            vac1 = new float[image.getWidth()][image.getHeight()];
            vac2 = new float[image.getWidth()][image.getHeight()];
            vac3 = new float[image.getWidth()][image.getHeight()];
        }
        if (useSkewness) {
            skc1 = new float[image.getWidth()][image.getHeight()];
            skc2 = new float[image.getWidth()][image.getHeight()];
            skc3 = new float[image.getWidth()][image.getHeight()];
        }
        if (useCurtose) {
            csc1 = new float[image.getWidth()][image.getHeight()];
            csc2 = new float[image.getWidth()][image.getHeight()];
            csc3 = new float[image.getWidth()][image.getHeight()];
        }

        if (useMedia || useVariancia || useSkewness || useCurtose) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    if (x == 0 || y == 0 || x == image.getWidth() - 1 || y == image.getHeight() - 1) {
                        Color c = new Color(image.getRGB(x, y));
                        mec1[x][y] = c.getRed();
                        mec2[x][y] = c.getGreen();
                        mec3[x][y] = c.getBlue();
                    } else {
                        Color[] color = new Color[9];
                        color[0] = new Color(image.getRGB(x - 1, y - 1));
                        color[1] = new Color(image.getRGB(x, y - 1));
                        color[2] = new Color(image.getRGB(x + 1, y - 1));
                        color[3] = new Color(image.getRGB(x - 1, y));
                        color[4] = new Color(image.getRGB(x, y));
                        color[5] = new Color(image.getRGB(x + 1, y));
                        color[6] = new Color(image.getRGB(x - 1, y + 1));
                        color[7] = new Color(image.getRGB(x, y + 1));
                        color[8] = new Color(image.getRGB(x + 1, y + 1));
                        /*color[9] = new Color(image.getRGB(x - 1, y - 1));
                        color[10] = new Color(image.getRGB(x, y - 1));
                        color[11] = new Color(image.getRGB(x + 1, y - 1));
                        color[12] = new Color(image.getRGB(x - 1, y));
                        color[13] = new Color(image.getRGB(x, y));
                        color[14] = new Color(image.getRGB(x + 1, y));
                        color[15] = new Color(image.getRGB(x - 1, y + 1));
                        color[16] = new Color(image.getRGB(x, y + 1));
                        color[17] = new Color(image.getRGB(x + 1, y + 1));
                        color[18] = new Color(image.getRGB(x - 1, y - 1));
                        color[19] = new Color(image.getRGB(x, y - 1));
                        color[20] = new Color(image.getRGB(x + 1, y - 1));
                        color[21] = new Color(image.getRGB(x - 1, y));
                        color[22] = new Color(image.getRGB(x, y));
                        color[23] = new Color(image.getRGB(x + 1, y));
                        color[24] = new Color(image.getRGB(x - 1, y + 1));
                        color[25] = new Color(image.getRGB(x - 1, y - 1));
                        color[26] = new Color(image.getRGB(x, y - 1));
                        color[27] = new Color(image.getRGB(x + 1, y - 1));
                        color[28] = new Color(image.getRGB(x - 1, y));
                        color[29] = new Color(image.getRGB(x, y));
                        color[30] = new Color(image.getRGB(x + 1, y));
                        color[31] = new Color(image.getRGB(x - 1, y + 1));
                        color[32] = new Color(image.getRGB(x, y + 1));
                        color[33] = new Color(image.getRGB(x + 1, y + 1));
                        color[34] = new Color(image.getRGB(x - 1, y - 1));
                        color[35] = new Color(image.getRGB(x, y - 1));
                        color[36] = new Color(image.getRGB(x + 1, y - 1));
                        color[37] = new Color(image.getRGB(x - 1, y));
                        color[38] = new Color(image.getRGB(x, y));
                        color[39] = new Color(image.getRGB(x + 1, y));
                        color[40] = new Color(image.getRGB(x - 1, y + 1));
                        color[41] = new Color(image.getRGB(x, y + 1));
                        color[42] = new Color(image.getRGB(x + 1, y + 1));
                        color[43] = new Color(image.getRGB(x - 1, y - 1));
                        color[44] = new Color(image.getRGB(x, y - 1));
                        color[45] = new Color(image.getRGB(x + 1, y - 1));
                        color[46] = new Color(image.getRGB(x - 1, y));
                        color[47] = new Color(image.getRGB(x, y));
                        color[48] = new Color(image.getRGB(x + 1, y));
                       */
                        float somaR = 0;
                        float somaG = 0;
                        float somaB = 0;
                        for (int i = 0; i < 9; i++) {
                            somaR += color[i].getRed();
                            somaG += color[i].getGreen();
                            somaB += color[i].getBlue();
                        }
                        mec1[x][y] = somaR / 9;
                        mec2[x][y] = somaG / 9;
                        mec3[x][y] = somaB / 9;
                    }
                }
            }
        }
        if (useVariancia) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    if (x == 0 || y == 0 || x == image.getWidth() - 1 || y == image.getHeight() - 1) {
                        Color c = new Color(image.getRGB(x, y));
                        vac1[x][y] = c.getRed();
                        vac2[x][y] = c.getGreen();
                        vac3[x][y] = c.getBlue();
                    } else {
                        Color[] color = new Color[9];
                        color[0] = new Color(image.getRGB(x - 1, y - 1));
                        color[1] = new Color(image.getRGB(x, y - 1));
                        color[2] = new Color(image.getRGB(x + 1, y - 1));
                        color[3] = new Color(image.getRGB(x - 1, y));
                        color[4] = new Color(image.getRGB(x, y));
                        color[5] = new Color(image.getRGB(x + 1, y));
                        color[6] = new Color(image.getRGB(x - 1, y + 1));
                        color[7] = new Color(image.getRGB(x, y + 1));
                        color[8] = new Color(image.getRGB(x + 1, y + 1));
                        float somaR = 0;
                        float somaG = 0;
                        float somaB = 0;
                        for (int i = 0; i < 9; i++) {
                            somaR += Math.pow(color[i].getRed() - mec1[x][y], 2.0);
                            somaG += Math.pow(color[i].getGreen() - mec2[x][y], 2.0);
                            somaB += Math.pow(color[i].getBlue() - mec3[x][y], 2.0);
                        }
                        vac1[x][y] = somaR / 9;
                        vac2[x][y] = somaG / 9;
                        vac3[x][y] = somaB / 9;
                    }
                }
            }
        }
        if (useSkewness) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    if (x == 0 || y == 0 || x == image.getWidth() - 1 || y == image.getHeight() - 1) {
                        Color c = new Color(image.getRGB(x, y));
                        skc1[x][y] = c.getRed();
                        skc2[x][y] = c.getGreen();
                        skc3[x][y] = c.getBlue();
                    } else {
                        Color[] color = new Color[9];
                        color[0] = new Color(image.getRGB(x - 1, y - 1));
                        color[1] = new Color(image.getRGB(x, y - 1));
                        color[2] = new Color(image.getRGB(x + 1, y - 1));
                        color[3] = new Color(image.getRGB(x - 1, y));
                        color[4] = new Color(image.getRGB(x, y));
                        color[5] = new Color(image.getRGB(x + 1, y));
                        color[6] = new Color(image.getRGB(x - 1, y + 1));
                        color[7] = new Color(image.getRGB(x, y + 1));
                        color[8] = new Color(image.getRGB(x + 1, y + 1));
                        float somaR1 = 0;
                        float somaG1 = 0;
                        float somaB1 = 0;
                        float somaR2 = 0;
                        float somaG2 = 0;
                        float somaB2 = 0;
                        for (int i = 0; i < 9; i++) {
                            somaR1 += Math.pow(color[i].getRed() - mec1[x][y], 3.0);
                            somaG1 += Math.pow(color[i].getGreen() - mec2[x][y], 3.0);
                            somaB1 += Math.pow(color[i].getBlue() - mec3[x][y], 3.0);
                            somaR2 += Math.pow(color[i].getRed() - mec1[x][y], 2.0);
                            somaG2 += Math.pow(color[i].getGreen() - mec2[x][y], 2.0);
                            somaB2 += Math.pow(color[i].getBlue() - mec3[x][y], 2.0);
                        }
                        skc1[x][y] = (float) ((somaR1 / 9) / Math.pow((somaR2 / 9), 1.5));
                        skc2[x][y] = (float) ((somaG1 / 9) / Math.pow((somaG2 / 9), 1.5));
                        skc3[x][y] = (float) ((somaB1 / 9) / Math.pow((somaB2 / 9), 1.5));
                    }
                }
            }
        }
        if (useCurtose) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    if (x == 0 || y == 0 || x == image.getWidth() - 1 || y == image.getHeight() - 1) {
                        Color c = new Color(image.getRGB(x, y));
                        csc1[x][y] = c.getRed();
                        csc2[x][y] = c.getGreen();
                        csc3[x][y] = c.getBlue();
                    } else {
                        Color[] color = new Color[9];
                        color[0] = new Color(image.getRGB(x - 1, y - 1));
                        color[1] = new Color(image.getRGB(x, y - 1));
                        color[2] = new Color(image.getRGB(x + 1, y - 1));
                        color[3] = new Color(image.getRGB(x - 1, y));
                        color[4] = new Color(image.getRGB(x, y));
                        color[5] = new Color(image.getRGB(x + 1, y));
                        color[6] = new Color(image.getRGB(x - 1, y + 1));
                        color[7] = new Color(image.getRGB(x, y + 1));
                        color[8] = new Color(image.getRGB(x + 1, y + 1));
                        float somaR1 = 0;
                        float somaG1 = 0;
                        float somaB1 = 0;
                        float somaR2 = 0;
                        float somaG2 = 0;
                        float somaB2 = 0;
                        for (int i = 0; i < 9; i++) {
                            somaR1 += Math.pow(color[i].getRed() - mec1[x][y], 4.0);
                            somaG1 += Math.pow(color[i].getGreen() - mec2[x][y], 4.0);
                            somaB1 += Math.pow(color[i].getBlue() - mec3[x][y], 4.0);
                            somaR2 += Math.pow(color[i].getRed() - mec1[x][y], 2.0);
                            somaG2 += Math.pow(color[i].getGreen() - mec2[x][y], 2.0);
                            somaB2 += Math.pow(color[i].getBlue() - mec3[x][y], 2.0);
                        }
                        csc1[x][y] = (float) ((somaR1 / 9) / Math.pow((somaR2 / 9), 2.0));
                        csc2[x][y] = (float) ((somaG1 / 9) / Math.pow((somaG2 / 9), 2.0));
                        csc3[x][y] = (float) ((somaB1 / 9) / Math.pow((somaB2 / 9), 2.0));
                    }
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(image.getWidth(), image.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    public BufferedImage getImage() {
        return image;
    }

}
