package org.example.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
public class ImageUtil {
    public static void resizeImage(File inputImage, File outputImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage originalImage = ImageIO.read(inputImage);
        Image resizedImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);

        BufferedImage bufferedResizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        bufferedResizedImage.getGraphics().drawImage(resizedImage, 0, 0, null);

        ImageIO.write(bufferedResizedImage, "jpg", outputImage);
    }
}
