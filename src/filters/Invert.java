//Copyright (c) 2024 Andrew Neal

package filters;

import java.awt.Color;

public class Invert extends Filter {

    //CONSTRUCTORS
    public Invert(){
        setUIStyle();
        this.effectName = "Invert ";
    }

    //METHODS
    @Override
    protected void applyEffect() {
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
