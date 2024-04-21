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
    protected String newPath;
    protected BufferedImage outputImage;
    protected File file;

    //FUNCTIONS
    public BufferedImage process(BufferedImage inputImage,boolean newFile, String filePath)
    {
        this.outputImage = inputImage;
        this.newPath = filePath;
        this.applyEffect();
        if (newFile) {
            createNewPNG();
        }
        return this.outputImage;
    }

    public BufferedImage process(boolean newFile)
    {
         try {
            this.file = getFile();
            if (file != null) {
                String newName = this.effectName + file.getName();  //adding effect to the files name
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

    protected abstract void applyEffect();//Method where effect calculations are performed 

    protected static void setUIStyle(){
        try {
            //Make the JFileChooser look like Windows 11 or 10
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {// handle exception if OS is not Windows
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
