//Copyright (c) 2024 Andrew Neal

package filters;

import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.JFileChooser;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

abstract class Filter {

    //VARIABLES
    protected String effectName;
    protected String newPath;//saves the file location
    protected BufferedImage outputImage;
    protected File file;

    //METHODS
    public BufferedImage process(BufferedImage inputImage, boolean newFile, String folderPath)
    {
        if(inputImage == null) return null;
        this.outputImage = inputImage;
        this.applyEffect();
        if (newFile) {
            if(folderPath.endsWith("/")){
                this.newPath = folderPath + "Filtered-Image.png";
                createNewPNG();
            }else{
                System.out.println("Provided path is not a folder");
            }
        }
        return this.outputImage;
    }

    public BufferedImage process(boolean newFile)
    {
         try {
            this.file = getFile();
            if (file != null) {
                String newName = this.effectName + file.getName();
                //making the path for the new file to go to the same folder as the original
                newPath = file.getAbsolutePath().replace(file.getName(), "") + newName;
                this.outputImage = ImageIO.read(file);
                this.applyEffect();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (newFile) {
            createNewPNG();
        }

        return this.outputImage;
    }

    protected abstract void applyEffect();

    protected static void setUIStyle(){
        try {//will make the JFileChooser look like Windows 11 or 10
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {// handle exception
        }
    }

    protected File getFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    protected void createNewPNG()  {
        try {
            ImageIO.write(this.outputImage, "png", new File(newPath));
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
