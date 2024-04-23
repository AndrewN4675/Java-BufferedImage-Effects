//Copyright (c) 2024 Andrew Neal

package filters;

import java.awt.Color;
import java.lang.Math;

public class Gaussian extends Filter {

    //VARIABLES
    public boolean blackBorder = false; //determines if edges are blacked out
    private int radi;
    private double[][] gaussMatrix;

    //CONSTRUCTORS
    public Gaussian(int radius){
        this.setRadius(radius);
        setUIStyle();
        this.effectName = "Gaussian ";
    }

    public Gaussian(){
        this.radi = 3;
        setUIStyle();
        this.effectName = "Gaussian ";
    }

    //METHODS
    @Override
    protected void applyEffect() {
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
        if(this.blackBorder)
        {
            for (int x = 0; x < this.outputImage.getWidth(); x++) {
                for (int y = 0; y < this.outputImage.getHeight(); y++) {
                    if ( (x < this.radi || x > this.outputImage.getWidth() - this.radi)
                            || (y < this.radi || y > this.outputImage.getHeight() - this.radi))
                    {
                        this.outputImage.setRGB(x, y, new Color(0,0,0).getRGB());
                    }
                }
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

    public void setRadius(int radius){
        if(radius >= 0){
            this.radi = radius;
        }else{
            System.out.println("Radius must be a positive integer");
        }
    }
}
