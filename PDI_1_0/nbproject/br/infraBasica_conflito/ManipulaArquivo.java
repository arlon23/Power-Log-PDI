/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infraBasica_conflito;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import swing.Conf;
import swing.JImageWindow;

/**
 *
 * @author Bruno
 */
public class ManipulaArquivo {

    String pastaSalvar = "";
    String pastaSalvar2 = "";

    public String selecionarDiretorio(String texto) {
        String caminho = null;

        JFileChooser dFC = new JFileChooser("D:\\TrabDoc\\");
        dFC.setDialogTitle(texto);
        dFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int resposta = dFC.showOpenDialog(null);
        if (resposta == JFileChooser.APPROVE_OPTION) {
            caminho = dFC.getSelectedFile().getPath();
        } else {
            System.out.print("Execução cancelada.");
            //System.exit(0);  
        }

        return caminho;
    }

    public File[] abrePasta(String texto) {

        File f = null;
        File[] paths;

        try {
            // create new file
            f = new File(selecionarDiretorio(texto));

            // returns pathnames for files and directory
            paths = f.listFiles();

            // for each pathname in pathname array
            for (File path : paths) {
                // prints file and directory paths
                System.out.println(path);
            }
            return paths;
        } catch (Exception e) {
            // if any error occurs
            e.printStackTrace();
        }
        return null;
    }

    public void leConfs(File conf) {
        try {
            FileReader fr = new FileReader(conf);
            BufferedReader br = new BufferedReader(fr);

            //abre caminho dos arquivos a serem processados
            File arqImgs[] = abrePasta("Escolha a pasta das imagens a serem processadas");

            pastaSalvar = selecionarDiretorio("Escolha a pasta onde serão guardadas as imagens processadas");
            pastaSalvar += "\\";
            //equanto houver mais linhas
            boolean primeiraLinha = true;
            while (br.ready()) {
                //lê a proxima linha
                String linha = br.readLine();
                if (!primeiraLinha) {
                    String vars[] = linha.split(";");
                    String configuracao = "_";
                    if (vars.length == 13) {
                        System.out.println(vars[0]);
                        int classes = Integer.parseInt(vars[0]);
                        Conf.kmeans_classes = classes;
                        if (vars[1].contains("1")) {
                            //pastaSalvar += "\\Cor\\";
                            Conf.feature_color = true;
                            configuracao = configuracao.concat("C_");
                        } else {
                            Conf.feature_color = false;
                        }
                        if (vars[2].contains("1")) {
                            Conf.feature_variancia = true;
                            //pastaSalvar += "\\Variancia\\";
                            configuracao = configuracao.concat("V_");
                        } else {
                            Conf.feature_variancia = false;
                        }
                        if (vars[3].contains("1")) {
                            Conf.feature_media = true;
                            //pastaSalvar += "\\Media\\";
                            configuracao = configuracao.concat("M_");

                        } else {
                            Conf.feature_media = false;
                        }
                        if (vars[4].contains("1")) {
                            Conf.feature_skewness = true;
                            //pastaSalvar += "\\Skewness\\";
                            configuracao = configuracao.concat("S_");
                        } else {
                            Conf.feature_skewness = false;
                        }
                        if (vars[5].contains("1")) {
                            Conf.feature_curtose = true;
                            //pastaSalvar += "\\Curtose\\";
                            configuracao = configuracao.concat("Cr_");
                        } else {
                            Conf.feature_curtose = false;
                        }
                        if (vars[6].contains("1")) {
                            Conf.feature_centroidMediaFogo = true;
                            //pastaSalvar2 = "\\MediaFogo\\";
                            configuracao = configuracao.concat("Cf_");
                        } else {
                            Conf.feature_centroidMediaFogo = false;
                        }
                        //numero de canais
                        int cont = 0;
                        int c1 = Integer.parseInt(vars[7]);
                        int c2 = Integer.parseInt(vars[8]);
                        int c3 = Integer.parseInt(vars[9]);
                        int c4 = Integer.parseInt(vars[10]);
                        int c5 = Integer.parseInt(vars[11]);
                        if (c1 != 0) {
                            cont++;
                        }
                        if (c2 != 0) {
                            cont++;
                        }
                        if (c3 != 0) {
                            cont++;
                        }
                        if (c4 != 0) {
                            cont++;
                        }
                        if (c5 != 0) {
                            cont++;
                        }
                        int cores[] = new int[cont];
                        int i = 0;
                        String color = "";
                        if (c1 != 0) {
                            cores[i] = c1;
                            i++;
                            color = color.concat(c1 + "_");
                        }
                        if (c2 != 0) {
                            cores[i] = c2;
                            i++;
                            color = color.concat(c2 + "_");
                        }
                        if (c3 != 0) {
                            cores[i] = c3;
                            i++;
                            color = color.concat(c3 + "_");
                        }
                        if (c4 != 0) {
                            cores[i] = c4;
                            i++;
                            color = color.concat(c4 + "_");
                        }
                        if (c5 != 0) {
                            cores[i] = c5;
                            i++;
                            color = color.concat(c5 + "_");
                        }

                        configuracao = configuracao.concat(color);
                        Conf.color_chanels = cores;

                        String ord = "_O" + vars[12] + "_";
                        configuracao = configuracao.concat(ord);
                        int tamOrdem = Integer.parseInt(vars[12]);
                        Conf.tam_matriz = tamOrdem;

                        for (File path : arqImgs) {
                            if (path.getName().contains(".jpg")) {
                                System.out.println("Processando: " + path.getName());
                                BufferedImage imagem = ImageIO.read(path);
                                // processaImg(imagem, path, configuracao);
                                imagem = null;
                            } else {
                                System.out.println("Não é imagem...");
                            }
                        }
                    }

                } else {
                    primeiraLinha = false;
                }

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManipulaArquivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException iox) {
            System.out.println("Erro na leitura do arquivo" + iox.getMessage());
        }

    }
   

    public void inversoLote() {
        try {
            //abre caminho dos arquivos a serem processados
            File arqImgs[] = abrePasta("Escolha a pasta das imagens a serem Invertidas");

            pastaSalvar = selecionarDiretorio("Escolha a pasta onde serão guardadas as imagens processadas");
            pastaSalvar += "\\";
            for (File path : arqImgs) {
                if (path.getName().contains(".jpg") || path.getName().contains(".bmp") || path.getName().contains(".png")) {
                    System.out.println("Processando: " + path.getName());
                    BufferedImage imagem = ImageIO.read(path);
                    BufferedImage res = new JImageWindow(imagem, JImageWindow.Tipo.INVERSO).getImage();
                    try {
                        if (!Paths.get(pastaSalvar).toFile().exists()) {
                            (new File(pastaSalvar)).mkdir();
                        }

                        File outputFile = new File(pastaSalvar + path.getName() + ".bmp");
                        ImageIO.write(res, "bmp", outputFile);

                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    imagem = null;
                    res = null;
                } else {
                    System.out.println("Não é imagem...");
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManipulaArquivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException iox) {
            System.out.println("Erro na leitura do arquivo" + iox.getMessage());
        }
    }
}
