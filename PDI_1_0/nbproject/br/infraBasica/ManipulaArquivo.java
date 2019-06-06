/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infraBasica;

import java.io.File;
import javax.swing.JFileChooser;


/**
 *
 * @author Bruno
 */
public class ManipulaArquivo {
    String pastaSalvar="";
    String pastaSalvar2 = "";
    public String selecionarDiretorio() {  
     String caminho = null; 
     
  
     JFileChooser dFC = new JFileChooser("D:\\TrabDoc\\");  
     dFC.setDialogTitle("Escolha o diretório das imagens");
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
   public File[] abrePasta() {
      
      File f = null;
      File[] paths;
      
      try{      
         // create new file
         f = new File(selecionarDiretorio());
         
         // returns pathnames for files and directory
         paths = f.listFiles();
         
         // for each pathname in pathname array
         for(File path:paths)
         {
            // prints file and directory paths
            System.out.println(path);
         } 
         return paths;
      }catch(Exception e){
         // if any error occurs
         e.printStackTrace();
      }
     return null;
   }             
}
