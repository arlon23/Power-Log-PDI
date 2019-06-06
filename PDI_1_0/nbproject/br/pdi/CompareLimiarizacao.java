/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdi;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

/**
 *
 * @author Bruno
 */
public class CompareLimiarizacao {

    //total de true negative
    private long ntn = 0;
    //total de true positive
    private long ntp = 0;
    //total de false negative
    private long nfn = 0;
    //total de false positive
    private long nfp = 0;

    //imagem Original
    private long boI = 0;
    private long foI = 0;
    //imagem de teste
    private long bt = 0;
    private long ft = 0;

    //% true positive
    private float tp = 0;

    //% true negative
    private float tn = 0;

    //% false positive
    private float fp = 0;

    //% false negative
    private float fn = 0;

    //respostas Missclassification Error / Erro relativo de 1º plano /
    private float me = 0;
    private float rae = 0;

    private float precision = 0;
    private float accuracy = 0;
    private float recall = 0;
    private float error = 0;
    private float fMeasure = 0;
    private float nrm = 0;

    //imgI = Imagem GroundTruth | imgT = imagem Teste
    public void comparar(BufferedImage imgI, BufferedImage imgT) {

        int w = imgI.getWidth();
        int h = imgI.getHeight();

        int wT = imgT.getWidth();
        int hT = imgT.getHeight();

        short img[][] = new short[w][h];
        short imgTest[][] = new short[wT][hT];

        if (w == wT && h == hT) {
            for (int x = 0; x < imgI.getWidth(); x++) {
                for (int y = 0; y < imgI.getHeight(); y++) {

                    Color c = new Color(imgI.getRGB(x, y));
                    //evita erros das imagens JPG
                    if (c.getRed() < 20 && c.getBlue() < 20 && c.getGreen() < 20) {
                        img[x][y] = 0;//cor preta
                        foI++;
                    } else {
                        img[x][y] = 1;
                        boI++;
                    }
                    Color cT = new Color(imgT.getRGB(x, y));
                    //evita erros das imagens JPG
                    if (cT.getRed() < 20 && cT.getBlue() < 20 && cT.getGreen() < 20) {
                        imgTest[x][y] = 0;//cor preta
                        ft++;
                    } else {
                        imgTest[x][y] = 1;
                        bt++;
                    }
                    if (img[x][y] == imgTest[x][y]) {
                        if (img[x][y] == 0) {
                            ntp++;
                        } else {
                            ntn++;
                        }
                    } else {
                        //preto na ideal e branco na teste
                        if (img[x][y] == 0 && imgTest[x][y] == 1) {
                            nfp++;
                        } else {
                            nfn++;
                        }
                    }
                }//for y
            }//for x
        } else {
            System.out.println("As imagens não são do mesmo tamanho!");
        }
    }

    public float getMe() {
        float x = ((boI + foI) == 0) ? 0.0000001f : ((float) (boI + foI));
        me = ((float) 1 - (((ntp + ntn)) / x));
        return me;
    }

    public float getRae() {
        float x = ((foI) == 0) ? 0.0000001f : ((float) (foI));
        float y = ((ft) == 0) ? 0.0000001f : ((float) (ft));
        if (foI > ft) {
            rae = ((float) foI - ft) / x;
        } else {
            rae = ((float) ft - foI) / y;
        }
        return rae;
    }

    public float truePos() {
        float x = ((ft) == 0) ? 0.00001f : ((float) (ft));
        tp = ((float) ntp) / x;
        return tp;
    }

    public float trueNegative() {
        float x = ((bt) == 0) ? 0.00001f : ((float) (bt));
        tn = ((float) ntn) / x;
        return tn;
    }

    public float falsePos() {
        float x = ((bt) == 0) ? 0.00001f : ((float) (bt));
        fp = ((float) nfp) / x;
        return fp;
    }

    public float falseNegative() {
        float x = ((ft) == 0) ? 0.00001f : ((float) (ft));
        fn = ((float) nfn) / x;
        return fn;
    }

    public float getPrecision() {
        float trueP = truePos();
        float falseP = falsePos();
        float x = ((trueP + falseP) == 0) ? 0.00001f : ((float) (trueP + falseP));
        precision = trueP / x;
        return precision;
    }

    public float getRecall() {
        float trueP = truePos();
        float falseN = falseNegative();
        float x = ((trueP + falseN) == 0) ? 0.00001f : ((float) (trueP + falseN));
        recall = trueP / x;
        return recall;
    }

    public float getAccuracy() {
        float trueP = truePos();
        float trueN = trueNegative();
        float falseN = falseNegative();
        float falseP = falsePos();
        float x = ((trueP + trueN + falseP + falseN) == 0) ? 0.00001f : ((float) (trueP + trueN + falseP + falseN));
        accuracy = (trueP + trueN) / x;
        return accuracy;
    }

    public float getError() {
        float trueP = truePos();
        float trueN = trueNegative();
        float falseN = falseNegative();
        float falseP = falsePos();
        float x = ((trueP + trueN + falseP + falseN) == 0) ? 0.00001f : ((float) (trueP + trueN + falseP + falseN));
        error = (falseP + falseN) / x;
        return error;
    }

    public float getFMeasure() {
        float x = ((getRecall() + getPrecision()) == 0) ? 0.00001f : ((float) (getRecall() + getPrecision()));
        fMeasure = (2f * getRecall() * getPrecision()) / x;
        return fMeasure;
    }

    public float getNrm() {
        float x = ((nfn + ntp) == 0) ? 0.00001f : ((float) (nfn + ntp));
        float y = ((nfp + ntn) == 0) ? 0.00001f : ((float) (nfp + ntn));

        float nrfm = ((float) nfn) / x;
        float nrfp = ((float) nfp) / y;
        nrm = (nrfm + nrfp) / 2f;
        return nrm;
    }

    //Mathews Correlation Coeficient
    public float getMMC() {
        float mmc;
        mmc = ((ntp * ntn) - (nfp * nfn)) / (float) Math.sqrt((ntn + nfn) * (ntn + nfp) * (ntp + nfn) * (ntp + nfp));
        return mmc;
    }

    @Override
    public String toString() {
        String texto = "TP = " + truePos() + "\n";
        texto += "TN = " + trueNegative() + "\n";
        texto += "FP = " + falsePos() + "\n";
        texto += "FN = " + falseNegative() + "\n";
        texto += "ME = " + getMe() + "\n";
        texto += "RAE = " + getRae() + "\n";
        texto += "Precision = " + getPrecision() + "\n";
        texto += "Accuracy = " + getAccuracy() + "\n";
        texto += "Recall = " + getRecall() + "\n";
        texto += "Error = " + getError() + "\n";
        texto += "FMeasure = " + getFMeasure() + "\n";
        texto += "NRM = " + getNrm() + "\n";

        return texto;

    }

    public String toStringArq() {
        DecimalFormat df = new DecimalFormat("0.0000");
        String texto = df.format(truePos()) + ";";
        texto += df.format(trueNegative()) + ";";
        texto += df.format(falsePos()) + ";";
        texto += df.format(falseNegative()) + ";";
        texto += df.format(getMe()) + ";";
        texto += df.format(getRae()) + ";";
        texto += df.format(getPrecision()) + ";";
        texto += df.format(getAccuracy()) + ";";
        texto += df.format(getRecall()) + ";";
        texto += df.format(getError()) + ";";
        texto += df.format(getFMeasure()) + ";";
        texto += df.format(getNrm()) + ";";

        return texto;

    }

}
