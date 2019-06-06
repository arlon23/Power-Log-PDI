package pdi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Stack;


import javax.swing.JComponent;

import interfaces.ImageInterface;


@SuppressWarnings("serial")
public class PDIHorizonte extends JComponent implements ImageInterface {
	
	private BufferedImage img;
	
	// inicializa quantidade de pilhas = a quantidade de linhas
	public Pilha LinhaX = new Pilha(); // pixelX
	public Pilha LinhaY = new Pilha(); // pixelY

	public Pilha LinhaMaiorX = new Pilha();
	public Pilha LinhaMaiorY = new Pilha();

	public PDIHorizonte(BufferedImage img) {
		
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		
		int totalPixels = img.getWidth() * img.getHeight();
		int contPixels = 0;
		Color imgOriginal[] = new Color [totalPixels];
		
		Color imgLinhaHorizonte[][] = new Color [img.getWidth()][img.getHeight()];
		Color imgBufferLinhaHorizonte[][] = new Color [img.getWidth()][img.getHeight()];
		
		// Kernel para detecao de contornos SOBEL EDGE Detector
		// http://www.cs.haifa.ac.il/~dkeren/ip/lecture9.pdf
		// http://citeseerx.ist.psu.edu/viewdoc/download;jsessionid=257D5241EC66C4D700736654E65F8522?doi=10.1.1.27.1821&rep=rep1&type=pdf
		float[][] kernel = {{ -1, -1, -1}, 
							{ -1, 9, -1}, 
							{ -1, -1, -1}};

		// contando numero de pixels pretos e numero de pixels brancos da imagem
		for(int largura = 0; largura < img.getWidth(); largura++)
		{
			for(int altura = 0; altura < img.getHeight(); altura++)
			{
				imgOriginal[contPixels] = new Color(img.getRGB(largura, altura));
				contPixels++;
			}
		}
		
		

		// procurando os contornos
		// pesquisa coluna a coluna
		for(int largura = 1; largura < img.getWidth()-1; largura++)
		{
			for(int altura = 1; altura < img.getHeight()-1; altura++)
			{
				float sum = 0; // Kernel sum for this pixel
				// verifica os vizinhos baseados no kernel
				for(int kx = -1; kx <= 1; kx++)
				{
					for(int ky = -1; ky <= 1; ky++)
					{
						// Calculate the adjacent pixel for this kernel point
						int pos = (largura + kx)*img.getHeight()+ (altura + ky);
						// Image is grayscale, red/green/blue are identical
						float val = imgOriginal[pos].getRGB();
						// Multiply adjacent pixels based on the kernel values
						sum += kernel[ky+1][kx+1] * val;
					}
				}
				// For this pixel in the new image, set the gray value
				// based on the sum from the kernel
				if(sum >= 150)// valor empirico. Se for contorno e um valor infinito, senao e nulo
				{
					// pinta pixel do contorno na cor preta
					imgBufferLinhaHorizonte[largura][altura] = Color.BLACK;
				}
				else
				{
					// pinta pixel fora do contorno na cor branca
					imgBufferLinhaHorizonte[largura][altura] = Color.WHITE;
				}
//				out.setRGB(largura, altura, imgBufferLinhaHorizonte[largura][altura].getRGB());
			}
		}
		// exibe na tela a imagem com todas as linhas encontradas
		this.img = out;
		
		int qtdLinhas = 0;
		
		//conta quantas linhas comecam do lado esquerdo da imagem
		for(int largura = 1, altura = 1; altura < img.getHeight()-1; altura++)
		{
			if(imgBufferLinhaHorizonte[largura][altura] == Color.BLACK)
			{
				qtdLinhas++;
			}
		}
		
 		
		// inicializa o buffer com a posicao do pixel que sera empilhado
 		int []Pixels = {0,0};
		int []PixelAnterior = {0,0};
		
 		// encontrando primeiro pixel de cada
 		// fazer o contador de pixels para cada uma das linhas (empilha as coordenadas)
		for(int largura = 1, altura = 1; altura < img.getHeight()-1; altura++)
		{
			if(imgBufferLinhaHorizonte[largura][altura] == Color.BLACK)
			{
				if( qtdLinhas > 0 )
				{
					// se a pilha estiver vazia
					if( LinhaX.pilhaVazia() )
					{
						Pixels[0]=largura;
						Pixels[1]=altura;

						LinhaX.empilhar(Pixels[0]); //incrementa numero de pixels da linha
						LinhaY.empilhar(Pixels[1]); //incrementa numero de pixels da linha
						imgBufferLinhaHorizonte[Pixels[0]][Pixels[1]] = Color.BLUE;
						
						// Encontrar pixels vizinhos (seguir a linha)
						EncontrarVizinho(imgBufferLinhaHorizonte, img.getWidth(), img.getHeight(), largura, altura, Pixels, PixelAnterior);
					}
					qtdLinhas--;
				}
				// Se a nova linha for maior que a anterior, substitui
				if( LinhaX.tamanho() > LinhaMaiorX.tamanho() )
				{
					while(!LinhaMaiorX.pilhaVazia()){
						LinhaMaiorX.desempilhar();
						LinhaMaiorY.desempilhar();
					};

					do{
						LinhaMaiorX.empilhar(LinhaX.exibeUltimoValor());
						LinhaMaiorY.empilhar(LinhaY.exibeUltimoValor());
						LinhaX.desempilhar();
						LinhaY.desempilhar();
					}while(!LinhaX.pilhaVazia());
				}
				else
				{
					LinhaX.pilhaEsvaziar();
					LinhaY.pilhaEsvaziar();
/*					do{
						// Apaga a pilha para que possa ser utilizada na detecaoo da proxima linha
						LinhaX.desempilhar();
						LinhaY.desempilhar();
					}while(!LinhaX.pilhaVazia());
*/				}
			}
 		}
		
		// enquanto houverem objetos na linha maior, verifica os pixels e pinta de preto
		do{
			Pixels[0] = LinhaMaiorX.exibeUltimoValor(); //retira posicao X do pixel da linha
			Pixels[1] = LinhaMaiorY.exibeUltimoValor(); //retira posicao Y do pixel da linha
			
			imgLinhaHorizonte[Pixels[0]][Pixels[1]] = Color.BLACK;
			
			// retira posicao dos pixels da linha maior da pilha
			LinhaMaiorX.desempilhar();
			LinhaMaiorY.desempilhar();
			
		}while(!LinhaMaiorX.pilhaVazia());

		// pinta toda a imgLinhaHorizonte de branco
		for( int largura = 1; largura < img.getWidth()-1; largura++)
		{
			for( int altura = 1; altura < img.getHeight()-1; altura++)
			{
				if(imgLinhaHorizonte[largura][altura] != Color.BLACK)
				{
					imgLinhaHorizonte[largura][altura] = Color.WHITE;
				}
				out.setRGB(largura, altura, imgLinhaHorizonte[largura][altura].getRGB());
			}
		}

		// exibe na tela a imagem somente com a linha do horizonte
		this.img = out;
	}

	
	
	
	//Pilha
	public class Pilha {
	    
	    public Stack<Integer> pilha;
	    public int posicaoPilha;

	    public Pilha() {
	        this.posicaoPilha = -1;
	// indica que esta nula, vazia, pois posicao um indica que contem informacao
	        this.pilha = new Stack<Integer>();
	// criando uma pilha de inteiros
	    }

	    public boolean pilhaVazia() {
	        //isEmpty
	        if (this.posicaoPilha == -1) {
	            return true;
	        }
	        return false;
	    }

	    public boolean pilhaEsvaziar() {
	        //esvazia a pilha por completo
	        pilha.clear();
	        this.posicaoPilha = -1;
	        return true;
	    }

	    public int tamanho() {
	        //is
	        if (this.pilhaVazia()) {
	            return 0;
	        }
	        return this.posicaoPilha + 1;
	    }

	    int exibeUltimoValor() {
	        //top
	        if (pilha.isEmpty()) {
	            return -1;
	        }
	        return pilha.get(pilha.size() - 1);
	    }

	    // metodo que retira um elemento da pilha
	    boolean desempilhar()
	    {
	        //pop
	        if (pilha.isEmpty()) {
	            return false;
	        }
	        pilha.pop();
	        this.posicaoPilha -= 1;
	        return true;
	    }

	    // metodo que insere um elemento na pilha
	    boolean empilhar(int valor)
	    {
	        // push
	        if (pilha.push(valor) != null) {
	        	this.posicaoPilha += 1;
	            return true;
	        }
	        return false;
	    }
	}
	
	

	// classe que varre a vizinhanca do pixel encontrado para seguir a linha
	public void EncontrarVizinho(Color[][] img, int largura, int altura, int x, int y, int []Pixel, int []PixelAnterior)
	{
		int []tmp = {0,0};
		tmp[0] = Pixel[0];
		tmp[1] = Pixel[1];
		
		// verifica se a posicao a ser procurada esta dentro do limite da imagem
		if( x < largura-2 && y < altura-2 )
		{
			// varre linha a linha na janela 3x3 do pixel em quest�o
			for(int kx = 1; kx >= -1; kx--)
			{
				// varre coluna a coluna na janela 3x3 do pixel em questao
				for(int ky = 1; ky >= -1; ky--)
				{
					Pixel[0] = kx+x;
					Pixel[1] = ky+y;
	
					// verifica se o vizinho em questao e da cor preta
					if((img[x+kx][y+ky] == Color.BLACK))
					{
						LinhaX.empilhar(Pixel[0]); // guarda coordenada x na pilha
						LinhaY.empilhar(Pixel[1]); // guarda coordenada y na pilha
						img[Pixel[0]][Pixel[1]] = Color.WHITE; // pinta o pixel guardado de branco (apaga)
						EncontrarVizinho(img, largura, altura, kx+x, ky+y, Pixel, tmp); // recursivamente chama a funcao para encontrar o proximo vizinho
					}
				}
			}
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(img.getWidth(), img.getHeight());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
	}

	public BufferedImage getImage() {
		return img;
	}
	
}