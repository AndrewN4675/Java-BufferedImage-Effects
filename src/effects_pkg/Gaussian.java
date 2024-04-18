//Copyright (c) 2024 Andrew Neal

package effects_pkg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.Math;

public class Gaussian extends Effect{

    private int radi;
    private double[][] gaussMatrix;

    public Gaussian(int radius){
        this.setRadius(radius);
        setUIStyle();
    }

    public Gaussian(){
        this.radi = 3;
        setUIStyle();
    }

    //FUNCTIONS
    @Override
    public BufferedImage process(BufferedImage inputImage, boolean newFile, String filePath) {
        this.outputImage = inputImage;
        this.newPath = filePath;
        gaussianBlur();
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
                String newName ="Gaussian " + file.getName();  //adding effect to the files name
                newPath = file.getAbsolutePath().replace(file.getName(), "") + newName; //making the path for the new file to go to the same folder as the original
                this.outputImage = ImageIO.read(file);
                gaussianBlur();
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }

        if(newFile){
            createNewPng();
        }
        return this.outputImage;
    }

    public void setRadius(int radius){
        if(radius >= 0){
            this.radi = radius;
        }else{
            System.out.println("Radius must be a positive integer");
        }
    }

    private void gaussianBlur(){
        this.createGaussianMatrix();

        for (int x = this.radi; x < this.outputImage.getWidth() - this.radi; x++) {
            for (int y = this.radi; y < this.outputImage.getHeight() - this.radi; y++) {

                double dRed = 0.0;
                double dGreen = 0.0;
                double dBlue = 0.0;

                //process pixel from matrix values
                for (int kernX = -this.radi; kernX < this.radi; kernX++){
                    for (int kernY = -this.radi; kernY < this.radi; kernY++){

                        double kernVal = this.gaussMatrix[kernX + this.radi][kernY+ this.radi];
                        int RGB = this.outputImage.getRGB(x - kernX, y - kernY);

                        //extract RGB values from RGB integer
                        int red = (RGB >> 16) & 0x000000FF;
                        int green = (RGB >> 8) & 0x000000FF;
                        int blue = (RGB) & 0x000000FF;

                        dRed += red * kernVal;
                        dGreen += green * kernVal;
                        dBlue += blue * kernVal;
                    }
                }

                Color processedPixel = new Color((int)(dRed) , (int)(dGreen), (int)(dBlue));
                this.outputImage.setRGB(x, y, processedPixel.getRGB());
            }
        }
    }

    //Creates the Gaussian matrix
    private void createGaussianMatrix(){
        double sigma = Math.max(this.radi / 2.0, 1);
        this.gaussMatrix = new double[(2 * this.radi) + 1][(2 * this.radi) + 1];

        double denom = sigma * sigma * Math.TAU;
        double sum = 0.0;

        //calculate matrix values
        for (int x = -this.radi; x < this.radi; x++){
            for (int y = -this.radi; y < this.radi; y++){
                double val = Math.pow(Math.E, -(double)((x * x) + (y * y)) / (2.0 * sigma * sigma)) / denom;
                this.gaussMatrix[x + this.radi][y + this.radi] = val;
                sum += val;
            }
        }

        //average the matrix
        for (int x = 0; x < (2 * this.radi) + 1; x++){
            for (int y = 0; y < (2 * this.radi) + 1; y++){
                this.gaussMatrix[x][y] = this.gaussMatrix[x][y] / sum;
            }
        }
    }
}
