//Copyright (c) 2024 Andrew Neal

package filters;

import java.awt.Color;
import java.util.Random;

public class Noise extends Filter {

    //VARIABLES
    private double intens;

    //CONSTRUCTORS
    public Noise(double intensitity)
    {
        setUIStyle();
        setIntensity(intensitity);
        this.effectName = "Noise ";
    }

    public Noise()
    {
        setUIStyle();
        this.intens = 0.1;
        this.effectName = "Noise ";
    }

    //METHODS
    @Override
    protected void applyEffect() {
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

    //Normalize colors to set number of colors per channel
    private int normalizeColor(int value) {
        if (value <= 0) {
            return 0;
        } else return Math.min(value, 255);
    }

    //determines the intensity of the noise 0 - 1
    public void setIntensity(double intensity){
        if(intensity >= 0.0 && intensity <= 1.0 ){
            this.intens = intensity;
        }else{
            System.out.println("Intensity out of range. 0.0 - 1.0");
        }
    }
}
