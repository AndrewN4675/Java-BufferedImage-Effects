# Java-BufferedImage-Effects
A simple implementation of graphical effects that work with Java BufferedImages such as grayscale conversion, color inversion, Floyd–Steinberg dithering, noise, etc...

** All effects are calculated using the CPU

## Using Effects

### Process Function:
There are two process functions which are identical for all effects. 

```java
BufferedImage EFFECT_NAME process(boolean newFile);
```
and 
```java
BufferedImage EFFECT_NAME process(BufferedImage inputImage, boolean newFile, String filePath);
```
The first function will load a file finder window which allows the user to find the image that they want to process and when finished, will create a new file in the sam folder if requested.
The second function requires an input BufferImage and much like the first function, will create a new png file in the provided folder.

### Combining Effects:
Process functions can be nested to combine effects.
Example:
```java
Grayscale gs = new Grayscale();
Dither di = new Dither();
di.process(gs.process(false), true, "C:/Path/to/folder/");
```
will create a png image called "Dithered.png" in the folder provided.

## Effects

### Base Image:
<img src= Images/Base.jpg width="667" height="500">

### Floyd–Steinberg dithering:
<img src= Images/Dithered.jpg width="667" height="500">
The example above is created with a dithering mod value of 2

### Grayscale:
<img src= Images/Grayscale.jpg width="667" height="500">
The example above is created with the rec601 grayscale conversion algorithm

### Color Inversion:
<img src= Images/Inverted.jpg width="667" height="500">

### Noise:
<img src= Images/Noise.jpg width="667" height="500">
The example above is created with a noise intensity value of 0.5

### Floyd–Steinberg dithering + Grayscale:
<img src= Images/Combo.png width="667" height="500">
