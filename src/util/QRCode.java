package util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


public class QRCode {
    public void generateQRCode(String content, int width, int height,String path) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE,width,height);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ImageIO.write(bufferedImage, "png", new File(path));
    }
}
