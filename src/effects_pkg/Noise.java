//Copyright (c) 2024 Andrew Neal

package effects_pkg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Noise extends Effect{

    //VARIABLES
    private double intens;

    //CONSTRUCTORS
    public Noise(double intensitity)
    {
        setUIStyle();
        setIntensity(intensitity);
    }

    public Noise()
    {
        setUIStyle();
        this.intens = 0.1;
    }

    //FUNCTIONS
    @Override
    public BufferedImage process(BufferedImage inputImage, boolean newFile, String filePath){
        this.outputImage = inputImage;
        this.newPath = filePath;
        noise();
        if(newFile){
            createNewPng();
        }
        return this.outputImage;
    }

    @Override
    public BufferedImage process(boolean newFile){
        try {
            this.file = getFile();
            if(file != null){
                String newName ="Noise " + file.getName();  //adding effect to the files name
                newPath = file.getAbsolutePath().replace(file.getName(), "") + newName; //making the path for the new file to go to the same folder as the original
                this.outputImage = ImageIO.read(file);
                noise();
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }

        if(newFile){
            createNewPng();
        }

        return this.outputImage;
    }


    //determines the intensity of the noise 0 - 1
    public void setIntensity(double intensity){
        this.intens = intensity;
        if(this.intens < 0){
            this.intens = 0;
        }else if(this.intens > 1) {
            this.intens = 1;
        }
    }

    private int normalizeColor ( int value){
        if (value <= 0) {
            return 0;
        } else if (value >= 255) {
            return 255;
        }
        return value;
    }

    private void noise()
    {
        Random rand = new Random();
        for (int y = 0; y < this.outputImage.getHeight(); y++) {
            for (int x = 0; x < this.outputImage.getWidth(); x++) {
                int color = outputImage.getRGB(x, y);

                //extract RGB values from RGB integer
                int red = (color >> 16) & 0x000000FF;
                int green = (color >> 8) & 0x000000FF;
                int blue = (color) & 0x000000FF;

                int noise  = Math.abs(rand.nextInt() % (int)(255 * this.intens));

                red = normalizeColor(red + noise) ;
                green = normalizeColor(green + noise);
                blue = normalizeColor(blue + noise);

                Color processedPixel = new Color(red, green, blue);
                outputImage.setRGB(x, y, processedPixel.getRGB()); //setting the RGB values at each x and yint newRGB = (alpha & 0xff) << 24 | (newRed & 0xff) << 16 | ((int) newGreen & 0xff) << 8 | (newBlue & 0xff);
            }
        }
    }
}
