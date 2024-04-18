//Copyright (c) 2024 Andrew Neal

package effects_pkg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Threshold extends Effect{

    //VARIABLES
    private int modValue;

    //CONSTRUCTORS
    public Threshold(int modValue) {
        setUIStyle();
        setModValue(modValue);
        this.effectName = "Threshold ";
    }

    public Threshold() {
        setUIStyle();
        this.modValue = 1;
        this.effectName = "Threshold ";
    }

    //FUNCTIONS
    @Override
    protected void applyEffect() {
        for (int y = 0; y < this.outputImage.getHeight() - 1; y++) {
            for (int x = 1; x < this.outputImage.getWidth() - 1; x++) {

                //get each pixel's RGB value and split it into respective channels
                int RGB = this.outputImage.getRGB(x, y);

                //extract RGB values from RGB integer
                float red = (RGB >> 16) & 0x000000FF;
                float green = (RGB >> 8) & 0x000000FF;
                float blue = (RGB) & 0x000000FF;

                //Quantize colors
                int newRed = Math.round(this.modValue * red / 255) * 255 / this.modValue;
                int newGreen = Math.round(this.modValue * green / 255) * 255 / this.modValue;
                int newBlue = Math.round(this.modValue * blue / 255) * 255 / this.modValue;
                Color newRGB = new Color(newRed, newGreen, newBlue);

                this.outputImage.setRGB(x, y, newRGB.getRGB());
            }
        }
    }

    //determines how limited the pallet is 0 - 255
    public void setModValue(int modValue) {
        this.modValue = modValue;
        if (this.modValue < 1) {
            this.modValue = 1;
        } else if (this.modValue > 255) {
            this.modValue = 255;
        }
    }
}
