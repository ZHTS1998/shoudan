package util;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;


public class Billutil {
    public static void uncompress(String fileData, String newFilePath) {
        Inflater inflater = new Inflater();
        inflater.setInput(Base64.getDecoder().decode(fileData));
        File file = new File(newFilePath);

        byte[] outByte = new byte[1024];
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); OutputStream out = new FileOutputStream(file)) {

            while (!inflater.finished()) {
                int len = inflater.inflate(outByte);

                if (len == 0) {
                    break;
                }
                bos.write(outByte, 0, len);
                out.write(bos.toByteArray());
                bos.reset();
            }
            inflater.end();
        } catch (IOException | DataFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fileData    文件数据
     * @param oriFileMd5  原始md5
     */
    public static boolean isSameMd5(String fileData ,String oriFileMd5) throws Exception {

       byte[] outData = fileData.getBytes();
        byte[] decodeData = com.lsy.baselib.crypto.util.Base64.decode(outData);
        String fileMd5 = calculateMD5(decodeData);
        return fileMd5.equals(oriFileMd5);
    }
    public static String calculateMD5(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


}
