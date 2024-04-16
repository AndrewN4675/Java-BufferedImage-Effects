//Copyright (c) 2024 Andrew Neal

package effects_pkg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Invert extends Effect {

    //CONSTRUCTORS
    public Invert(){
        setUIStyle();
    }

    //FUNCTIONS
    @Override
    public BufferedImage process(BufferedImage inputImage, boolean newFile, String filePath) {
        this.outputImage = inputImage;
        this.newPath = filePath;
        invert();
        if(newFile){//create a new file if requested
            createNewPng();
        }
        return this.outputImage;
    }

    @Override
    public BufferedImage process(boolean newFile){
        try {
            this.file = getFile();
            if(file != null){
                String newName ="Inverted " + file.getName(); //adding effect to the files name
                newPath = file.getAbsolutePath().replace(file.getName(), "") + newName; //making the path for the new file to go to the same folder as the original
                this.outputImage = ImageIO.read(file);
                invert();
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }

        if(newFile){
            createNewPng();
        }
        return this.outputImage;
    }

    private void invert(){
        for (int x = 0; x < this.outputImage.getWidth(); x++) {
            for (int y = 0; y < this.outputImage.getHeight(); y++) {
                int RGB = this.outputImage.getRGB(x, y);

                //extract RGB values from RGB integer
                int red = (RGB >> 16) & 0x000000FF;
                int green = (RGB >> 8) & 0x000000FF;
                int blue = (RGB) & 0x000000FF;

                red = 255 - red;
                green = 255 - green;
                blue = 255 - blue;

                Color processedPixel = new Color(red, green, blue);
                this.outputImage.setRGB(x, y, processedPixel.getRGB());
            }
        }
    }
}
