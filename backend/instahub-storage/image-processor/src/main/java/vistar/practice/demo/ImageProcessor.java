package vistar.practice.demo;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ImageProcessor {

    public static BufferedImage readFromBytes(byte[] data) {

        try {
            InputStream is = new ByteArrayInputStream(data);
            return ImageIO.read(is);
        } catch (IOException ex) {
            log.error("Error while reading image from byte array", ex);
        }
        return null;
    }

    public static BufferedImage resizeAndCrop(
            BufferedImage originalImage,
            int targetWidth,
            int targetHeight
    ) {

        int mcm = mostCommonMultiple(targetWidth, targetHeight);

        int targetWidthScale = targetWidth / mcm;
        int targetHeightScale = targetHeight / mcm;

        int originalImageWidth = originalImage.getWidth();
        int originalImageHeight = originalImage.getHeight();


        if (originalImageWidth < targetWidthScale || originalImageHeight < targetHeightScale) {
            throw new ImagingOpException("Impossible to resize photo: bad photo size or target proportions");
        }

        // Флаг, что режем фото именно по ширине, подгоняя под пропорцию
        boolean cutWidth = originalImageWidth * targetHeightScale> originalImageHeight * targetWidthScale;

        int proportionedWidth;
        int proportionedHeight;

        if (cutWidth) {
            proportionedHeight = originalImageHeight - originalImageHeight % targetHeightScale;
            proportionedWidth = proportionedHeight * targetWidthScale / targetHeightScale;
        } else {
            proportionedWidth = originalImageWidth - originalImageWidth % targetWidthScale;
            proportionedHeight = proportionedWidth * targetHeightScale / targetWidthScale;
        }

        if (proportionedWidth < targetWidth || proportionedHeight < targetHeight) {
            throw new ImagingOpException("Impossible to resize photo: bad target proportions");
        }

        int x = originalImageWidth / 2 - proportionedWidth / 2;
        int y = originalImageHeight / 2 - proportionedHeight / 2;

        Image proportionedImage = originalImage.getSubimage(x, y, proportionedWidth, proportionedHeight);

        Image resultingImage = proportionedImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

        return outputImage;
    }

    public static byte[] toBytes(BufferedImage bufferedImage, String format) {

        try {
            var baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, format, baos);
            return baos.toByteArray();
        } catch (IOException ex) {
            log.error("Error while transferring image to byte array", ex);
        }
        return null;
    }

    public static String parseSuffix(String filename) {

        if (!filename.contains(".") || filename.indexOf(".") == filename.length() - 1) {
            return "";
        }

        int index = filename.length() - 1;
        while (filename.charAt(index) != '.') {
            --index;
        }

        return filename.substring(index + 1);
    }

    private static int mostCommonMultiple(int x, int y) {

        while (x != 0 && y != 0) {
            if (x > y) {
                x = x % y;
            } else {
                y = y % x;
            }
        }
        return x + y;
    }
}
