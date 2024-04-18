//Copyright (c) 2024 Andrew Neal

package effects_pkg;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

abstract class Effect {

    //VARIABLES
    protected String effectName;
    protected String newPath;//saves the file location
    protected BufferedImage outputImage;
    protected File file;

    //FUNCTIONS
    public BufferedImage process(BufferedImage inputImage,boolean newFile, String filePath)
    {
        this.outputImage = inputImage;
        this.newPath = filePath;
        this.applyEffect();
        if (newFile) {
            createNewPng();
        }
        return this.outputImage;
    }

    public BufferedImage process(boolean newFile)
    {
         try {
            this.file = getFile();
            if (file != null) {
                String newName = this.effectName + file.getName();  //adding effect to the files name
                newPath = file.getAbsolutePath().replace(file.getName(), "") + newName; //making the path for the new file to go to the same folder as the original
                this.outputImage = ImageIO.read(file);
                this.applyEffect();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (newFile) {
            createNewPng();
        }

        return this.outputImage;
    }

    protected static void setUIStyle(){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//will make the JFileChooser look like Windows 11 or 10
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

    protected void createNewPng()  {
        try {
            ImageIO.write(this.outputImage, "png", new File(newPath));
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    protected abstract void applyEffect();
}
