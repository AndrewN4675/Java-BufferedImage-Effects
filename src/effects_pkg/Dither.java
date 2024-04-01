package effects_pkg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Dither extends Effect {

    //VARIABLES
    private int modValue;

    //CONSTRUCTORS
    public Dither(int modValue) {
        setUIStyle();
        setModValue(modValue);
    }

    public Dither() {
        setUIStyle();
        this.modValue = 1;
    }

    //FUNCTIONS
    @Override
    public BufferedImage process(BufferedImage inputImage, boolean newFile, String filePath) {
        this.outputImage = inputImage;
        this.newPath = filePath;
        dither();
        if (newFile) {
            createNewPng();
        }
        return this.outputImage;
    }

    @Override
    public BufferedImage process(boolean newFile) {
        try {
            this.file = getFile();
            if (file != null) {
                String newName = "Dithered " + file.getName();  //adding effect to the files name
                newPath = file.getAbsolutePath().replace(file.getName(), "") + newName; //making the path for the new file to go to the same folder as the original
                this.outputImage = ImageIO.read(file);
                dither();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (newFile) {
            createNewPng();
        }

        return this.outputImage;
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

    private void dither() {
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

                //calculate how much error was produced by quantization of the pixel
                int errorR = (int) (red - newRed);
                int errorG = (int) (green - newGreen);
                int errorB = (int) (blue - newBlue);

                //spread the calculated error to the surrounding pixels
                error(x + 1, y, errorR, errorG, errorB, 7.0 / 16.0);//Right
                error(x - 1, y + 1, errorR, errorG, errorB, 3.0 / 16.0);//Bottom left
                error(x, y + 1, errorR, errorG, errorB, 5.0 / 16.0);//Bottom
                error(x + 1, y + 1, errorR, errorG, errorB, 1.0 / 16.0);//Bottom right
            }
        }
    }

    //Floyd–Steinberg dithering error spreading algorithm
    private void error(int x, int y, int errorR, int errorG, int errorB, double multi) {
        Color pixelsToProcess = new Color(this.outputImage.getRGB(x, y));
        int newRed = (int) (pixelsToProcess.getRed() + errorR * multi);
        int newGreen = (int) (pixelsToProcess.getGreen() + errorG * multi);
        int newBlue = (int) (pixelsToProcess.getBlue() + errorB * multi);
        Color processedPixel = new Color(normalizeColor(newRed), normalizeColor(newGreen), normalizeColor(newBlue));
        this.outputImage.setRGB(x, y, processedPixel.getRGB());
    }

    //Normalize colors to set number of colors per channel
    private int normalizeColor(int value) {
        if (value <= 0) {
            return 0;
        } else return Math.min(value, 255);
    }
}