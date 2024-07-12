package util;

import com.lsy.baselib.crypto.protocol.PKCS7Signature;
import com.lsy.baselib.crypto.util.CryptUtil;
import com.lsy.baselib.crypto.util.FileUtil;



import java.util.Base64;


import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class Sign {
    /**
     * 获取RSA加签私钥
     *
     * @param pwd         商户密码
     * @param filepathKey 中信银行提供的私钥文件路径
     * @return RSA加签私钥
     */

    public static String getPriKey(String pwd, String filepathKey) {
        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();
        try {

            char[] keyPassword = pwd.toCharArray();
            byte[] base64EncodedPrivateKey = FileUtil.read4file(filepathKey);

            PrivateKey signerPrivateKey = CryptUtil.decryptPrivateKey(decoder.decode(base64EncodedPrivateKey), keyPassword);
            byte[] keyBates = signerPrivateKey.getEncoded();
            return encoder.encodeToString(keyBates).replaceAll("[\r\n]", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * RSA加签
     *
     * @param sendMsg 待发送的内容
     * @return 签名结果
     */
    public static String signRSA(String sendMsg, String priKeyText) {
        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(decoder.decode(priKeyText));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(privateKey);
            signature.update(sendMsg.getBytes());
            byte[] sign = signature.sign();
            return encoder.encodeToString(sign);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
        /**
         * 获取RSA验签公钥
         *
         * @param filepathCer 中信银行提供的公钥文件路径
         * @return RSA验签公钥
         */
        public static String getPubCer(String filepathCer) {
            Base64.Encoder encoder = Base64.getEncoder();
            Base64.Decoder decoder = Base64.getDecoder();
            try {
                // 获取公钥
                byte[] base64EncodedSenderCert = FileUtil.read4file(filepathCer);
                X509Certificate signerCertificate = CryptUtil.generateX509Certificate(decoder.decode(base64EncodedSenderCert));
                PublicKey senderPubKey = signerCertificate.getPublicKey();
                byte[] keyBates = senderPubKey.getEncoded();
                return encoder.encodeToString(keyBates).replaceAll("[\r\n]", "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        /**
         * RSA验签
         *
         * @param receiveMsg 已接收的内容
         * @param sign       签名
         * @param pubKeyText 中信公钥
         * @return 签名结果
         */
        public static Boolean verifySignRSA(String receiveMsg, String sign, String pubKeyText) {
            Base64.Decoder decoder = Base64.getDecoder();
            try {
                X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decoder.decode(pubKeyText));
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
                Signature signature = Signature.getInstance("SHA1WithRSA");
                signature.initVerify(publicKey);
                signature.update(receiveMsg.getBytes("GBK"));
                return signature.verify(decoder.decode(sign));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

    /**
     * 加签
     *
     * @param info        待加签数据
     * @param pwd         密码内容
     * @param filepathKey 私钥文件路径
     * @param filepathCer 公钥文件路径
     * @return 签名结果
     */
    public static String sign(String info, String pwd, String filepathKey, String filepathCer) {
        try {
            // 密码
            Base64.Encoder encoder = Base64.getEncoder();
            Base64.Decoder decoder = Base64.getDecoder();
            char[] keyPassword = pwd.toCharArray();
            // 获取私钥
            byte[] base64EncodedPrivateKey = FileUtil.read4file(filepathKey);
            PrivateKey signerPrivateKey = CryptUtil.decryptPrivateKey(decoder.decode(base64EncodedPrivateKey), keyPassword);
            // 获取公钥
            byte[] base64EncodedCert = FileUtil.read4file(filepathCer);
            X509Certificate signerCertificate = CryptUtil.generateX509Certificate(decoder.decode(base64EncodedCert));
            // 加签
            byte[] signature = PKCS7Signature.sign(info.getBytes("GBK"), signerPrivateKey, signerCertificate, null, false);
            return new String(encoder.encode(signature));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 验签
     *
     * @param info        待验签数据
     * @param sign        签名
     * @param filepathCer 公钥文件路径
     * @return true-验签成功 false-验签失败
     */
    public static Boolean verifySign(String info, String sign, String filepathCer) {
        try {
            // 获取公钥
            Base64.Encoder encoder = Base64.getEncoder();
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] base64EncodedSenderCert = FileUtil.read4file(filepathCer);
            X509Certificate signerCertificate = CryptUtil.generateX509Certificate(decoder.decode(base64EncodedSenderCert));
            PublicKey senderPubKey = signerCertificate.getPublicKey();
            // 验签
            return PKCS7Signature.verifyDetachedSignature(info.getBytes("GBK"), encoder.encode(sign.getBytes()), senderPubKey);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}

