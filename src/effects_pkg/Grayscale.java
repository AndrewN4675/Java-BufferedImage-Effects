package effects_pkg;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Grayscale extends Effect{

    //VARIABLES
    private int conv;

    //CONSTRUCTORS
    public Grayscale(int conversionType){
        setUIStyle();
        this.set_conversion(conversionType);
    }

    public Grayscale(){
        setUIStyle();
        this.conv = 1;
    }

    //FUNCTIONS
    @Override
    public BufferedImage process(BufferedImage inputImage, boolean newFile) {
        this.outputImage = inputImage;
        grayScale();
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
                String newName ="Grayscale " + file.getName();  //adding effect to the files name
                newPath = file.getAbsolutePath().replace(file.getName(), "") + newName; //making the path for the new file to go to the same folder as the original
                this.outputImage = ImageIO.read(file);
                grayScale();
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }

        if(newFile){
            createNewPng();
        }
        return this.outputImage;
    }

    //determines the conversion algorithm used 1 - 3
    public void set_conversion(int conversionType){
        if(conversionType > 0 && conversionType < 4){
            conv = conversionType;
        }else{
            System.out.println("Error setting greyscale conversion type:");
            System.out.println("1 - rec601 \n2 - ITU-BT.709 \n 3 - ITU-R BT.2100");
        }
    }

    private void grayScale(){
        for (int x = 0; x < this.outputImage.getWidth(); x++) {
            for (int y = 0; y < this.outputImage.getHeight(); y++) {
                int RGB = this.outputImage.getRGB(x, y);

                //extract RGB values from RGB integer
                int red = (RGB >> 16) & 0x000000FF;
                int green = (RGB >> 8) & 0x000000FF;
                int blue = (RGB) & 0x000000FF;

                //determine which conversion algorithm to use
                int lum = switch (this.conv) {
                    case 1 -> (int)(0.299 * red + 0.587 * green + 0.114 * blue);
                    case 2 -> (int)(0.2126 * red + 0.7152 * green + 0.0722 * blue);
                    case 3 -> (int)(0.2627 * red + 0.6780 * green + 0.0593 * blue);
                    default -> 0;
                };

                Color processedPixel = new Color(lum, lum, lum);
                this.outputImage.setRGB(x, y, processedPixel.getRGB());
            }
        }
    }
}