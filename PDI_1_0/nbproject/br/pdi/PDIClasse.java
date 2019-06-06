package pdi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import interfaces.ImageInterface;
import swing.Conf;

@SuppressWarnings("serial")
public class PDIClasse extends JComponent implements ImageInterface {

	private BufferedImage image;
	private Color[] cores = new Color[10];
        private long numPixelsClasse=0;
	
	boolean useColor = Conf.feature_color;
	boolean useMedia = Conf.feature_media;
	boolean useVariancia = Conf.feature_variancia;
	boolean useSkewness = Conf.feature_skewness;
	boolean useCurtose = Conf.feature_curtose;
	
	public PDIClasse(BufferedImage img, int n) {

		initCores();
		
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				if (Conf.classes[x][y] == n) {
					Color c = new Color(img.getRGB(x, y));
					//out.setRGB(x, y, cores[Conf.classes[x][y]].getRGB());
					out.setRGB(x, y, c.getRGB());
                                        numPixelsClasse++;
				}
				else
					out.setRGB(x, y, Color.LIGHT_GRAY.getRGB());
			}
		}
		
		image = out;
	}
	public long getNumPixels(){
            return numPixelsClasse;
        }
	private void initCores() {
		cores[0] = Color.red;
		cores[1] = Color.green;
		cores[2] = Color.BLUE;
		cores[3] = Color.YELLOW;
		cores[4] = Color.CYAN;
		cores[5] = Color.MAGENTA;
		cores[6] = Color.ORANGE;
		cores[7] = Color.WHITE;
		cores[8] = Color.DARK_GRAY;
		cores[9] = Color.BLACK;
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
