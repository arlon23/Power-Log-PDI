package swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

import interfaces.ImageInterface;
import pdi.PDIClasse;
import pdi.PDIInverso;
import pdi.PDIHorizonte;
import pdi.PDIKmeansGray;

@SuppressWarnings("serial")
public class JImageWindow extends JInternalFrame {

    private long numPixelsClasse = 0;

    public enum Tipo {

        NORMAL,
        INVERSO, KMEANS, KMEANS2, CLASSE, HORIZONTE, KMEANSGRAY
    }

    private ImageInterface canvas;

    public JImageWindow(BufferedImage img, Tipo tipo) {
        setMaximizable(true);
        setClosable(true);
        setResizable(true);
        Dimension d = new Dimension(img.getWidth() + 28, img.getHeight() + 50);
        if (img.getWidth() > 1024) {
            d.width = 1024;
        }
        if (img.getHeight() > 700) {
            d.height = 700;
        }
        setSize(d.width, d.height);
        setLayout(new BorderLayout());
        switch (tipo) {
            case NORMAL:
                canvas = new ImageCanvas(img);
                break;
            case INVERSO:
                canvas = new PDIInverso(img);
                break;
            case HORIZONTE:
                canvas = new PDIHorizonte(img);
                break;
            case KMEANSGRAY:
                canvas = new PDIKmeansGray(img);
                break;
            default:
                break;
        }
        JScrollPane scroll = new JScrollPane((JComponent) canvas);

        add(scroll, BorderLayout.CENTER);

    }

    public JImageWindow(BufferedImage img, Tipo tipo, int n) {
        setMaximizable(true);
        setClosable(true);
        setResizable(true);
        Dimension d = new Dimension(img.getWidth() + 28, img.getHeight() + 50);
        if (img.getWidth() > 1024) {
            d.width = 1024;
        }
        if (img.getHeight() > 700) {
            d.height = 700;
        }
        setSize(d.width, d.height);
        setLayout(new BorderLayout());
        switch (tipo) {
            case CLASSE:
                canvas = new PDIClasse(img, n);
                PDIClasse pdi = (PDIClasse) canvas;
                numPixelsClasse = pdi.getNumPixels();
                break;
            default:
                break;
        }
        JScrollPane scroll = new JScrollPane((JComponent) canvas);

        add(scroll, BorderLayout.CENTER);

    }

    public BufferedImage getImage() {
        return canvas.getImage();
    }

    public long getNumPixels() {
        return numPixelsClasse;
    }
}
