import org.junit.Test;
import vistar.practice.demo.ImageProcessor;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static vistar.practice.demo.ImageProcessor.parseSuffix;

public class ImageProcessorTests {

    //todo -> Вставить путь к любой фотке для тестирования
    private static final String INPUT_FILEPATH = "";

    //todo -> Вставить путь для сохранения фото
    private static final String OUTPUT_FILEPATH = "";

    @Test
    public void test() throws IOException {

        var image = ImageIO.read(
                new File(INPUT_FILEPATH)
        );

        var bytes = ImageProcessor.toBytes(image, INPUT_FILEPATH);
        image = ImageProcessor.readFromBytes(bytes);

        var baos = new ByteArrayOutputStream();
        assert image != null;
        ImageIO.write(image, parseSuffix(INPUT_FILEPATH), baos);
        var byteArray = baos.toByteArray();


        var bufferedImage = ImageProcessor.readFromBytes(byteArray);
        assert bufferedImage != null;

        bufferedImage = ImageProcessor.resizeAndCrop(
                bufferedImage,
                bufferedImage.getWidth() / 3,
                bufferedImage.getHeight()
        );
        ImageIO.write(
                bufferedImage,
                parseSuffix(OUTPUT_FILEPATH),
                new File(OUTPUT_FILEPATH)
        );
    }
}
