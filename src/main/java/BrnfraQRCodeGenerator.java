import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;

public class BrnfraQRCodeGenerator {



    public static void main(String[] args) throws IOException, WriterException, NotFoundException {

        String filePath = "myFirstQRCOde.png";
        File qrFile = new File(filePath);
        String qrCodeText = "https://github.com/brnfra/java-QRcode-generator";
        int size = 125;
        String fileType = "png";
        createQRImage(qrFile, qrCodeText, size, fileType);
        System.out.println("Data information stored in QRCode is:\n" +
                readQRCodeFile(qrFile));

    }

    private static void createQRImage(File qrFile,
                                      String qrCodeText,
                                      int size,
                                      String fileType)
            throws WriterException, IOException
    {
        //Create the ByteMatrix for the QR-Code from String
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);

        //Make the BufferedImage that are to hold the QRCode
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);

        //paint and save the image
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < matrixWidth; i++)
        {
            for (int j = 0; j < matrixWidth; j++ )
            {
                if(byteMatrix.get(i,j))
                {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }

        ImageIO.write(image, fileType, qrFile);

    }

    private static String readQRCodeFile (File qrFile)
            throws IOException, NotFoundException
    {
        BinaryBitmap binaryBitmap = new BinaryBitmap(
                new HybridBinarizer(
                        new BufferedImageLuminanceSource(ImageIO.read(
                                new FileInputStream(qrFile)
                        ))
                )
        );
        Result result = new MultiFormatReader().decode(binaryBitmap);
        return result.getText();
    }

}
