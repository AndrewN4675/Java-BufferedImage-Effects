# Java-BufferedImage-Filters
A library designed to provide users with CPU based graphical filters that can be performed on Java Buffered Images or Buffered Image supported file formats

## Using Filters

### Process Function:
There are two process functions which are identical for all filters:

```java
BufferedImage /*FILTER_NAME*/ process(boolean newFile);
```
Prompts the user with a file finder window, allowing them to select the image they wish to process. 
Once the image has been processed, a new PNG file will be created in the same directory as the orignal image if requested.

and

```java
BufferedImage /*FILTER_NAME*/ process(BufferedImage inputImage, boolean newFile, String filePath);
```
Requires an input Buffered Image and will create it creates a new PNG file in the provided folder once the image has been processed if requested

### Combining Filters:
Process functions can be nested to combine filters.
Example:
```java
Grayscale gs = new Grayscale();
Dither di = new Dither();
di.process(gs.process(false), true, "C:/Path/to/folder/");
```
will create a png image called "Dithered.png" in the folder provided.

## Example Filters

### Base Image:
<img src= Images/Base.jpg width="667" height="500">

### Floyd–Steinberg dithering:
<img src= Images/Dithered.jpg width="667" height="500">
The example above is created with a dithering mod value of 2

### Grayscale:
<img src= Images/Grayscale.jpg width="667" height="500">
The example above is created with the rec601 grayscale conversion algorithm

### Floyd–Steinberg dithering + Grayscale:
<img src= Images/Combo.png width="667" height="500">
resulting image from the sample code in Combining Filters
