package com.neo.paymodel.common.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.security.rsa.RSACore;
import sun.security.rsa.RSAKeyFactory;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public abstract class SignatureTools {

    public static void main(String[] args) throws Exception {
        String content =
                "{\"amount\":\"10\",\"bankAccountName\":\"嬴政1\",\"bankAccountNo\":\"456465464654\",\"bankName\":\"中国银行1\",\"bankNameSub\":\"中国银行1\",\"mercId\":\"BLG0004\",\"mercOrderId\":\"12002\",\"method\":\"trade.payout.order.create\",\"notifyUrl\":\"http://test/1111\",\"time\":1574437776}";


        //生成rsa秘钥
      /*  byte[][] keyPairBytes = genRsaKeyPairBytes(2048);
        String privateKey = new String(Base64.getEncoder().encode(keyPairBytes[0]));
        String publicKey = new String(Base64.getEncoder().encode(keyPairBytes[1]));*/
        String privateKey =
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC+fhMMVirJet07\n" +
                        "eopdF3tP/K3e0IqHvIYcw+Vzg8ahX40wRm0dyxkZZu28teEG1ZIbmQO3foomvhyo\n" +
                        "eZxq2wo0HX2Z8ioE/kKf8XXOxx49CDFZkf9KRtvyumU8ei4rMN75CQwckS+8CAjG\n" +
                        "pgfEhweIKtSbkO0T+u1q8r6e8e+ThwIPkrXKJ4xutr+8GzfRw0ePfxeiRmfctHoI\n" +
                        "ahE7nNQQ0/huMVTKt4IBeKmA1OtI1C/mT5kiWfkX11qQ+8jO6ap0QkRCUIOgIi6Y\n" +
                        "gQ0qCuOmlw7Ny2ll4VgxIabBesn9U0XOx2eVa9/TyOG8HZyTk+bxOWyXs1iDfPbc\n" +
                        "4yhTakA9AgMBAAECggEAO/DT8ZfTkt6UarjefPh8lui9lVUtohSSZs7FjpmykOjO\n" +
                        "NpiSkUNtPG8E89KPYmUluoVlhAHPWu2Lev/5Vi5igY+eDCkFS9juAgIqvcwuGapQ\n" +
                        "TQ0Btc+iOjiozmTrzi9smhRcuUh+Kr6kdE2eOBr9Jb3EdMcfsp/MwVBs5vreFh9j\n" +
                        "6Iocq8/2s2VS3XxePJIsLUlCUXcEjB+Azu+mzfkJ4MH1VLD4MtgVRLdEf8qbCsuw\n" +
                        "qrV7LlhU3/mwVGlwdHpS5OQ5kXo+32QnjEnAYdqNJHlND5aEc93E15yiODGniuKK\n" +
                        "bluDN5sinjmmRnPToJassOrWsY0oqBWZeUc2FklwAQKBgQDw3T9NTZI0euzHePI7\n" +
                        "lUJZe64G80Ju5FXTIF551xKLbH5iugzKm8ZuRt9U4b9+CgG0IGKbTOzC2GgQy6qs\n" +
                        "oeGsRKlPJhcPQb6/5i7fuzYjtRsJ3erB6HSX2K8vbokqCDkpeBRefcxJAB33H6aj\n" +
                        "BlwHEklQ3IsVKwXipS3CrEmaPQKBgQDKdoC6kYe7RYv4rKNqNkqlou5xAbmv/KoR\n" +
                        "Q3Sqg8zmE/ADT4L/q8woGkdiMGmIfS5BACuIxcQrMhIBkfNsfrriqV09otfmzgbJ\n" +
                        "vByj6pYYw778XEGhoDS0wjIKOFrS32bDRR83O75ZHNtZcQPFx8iOKt4J2aXQoB17\n" +
                        "EYono5ueAQKBgBhc+Lsz/nbqfCrNFAts1JJCjV7MmkqnM/swCf5e/9Q0fDA2S3bu\n" +
                        "/MpIxuxKDVmNX5C9udulk0k+JICWaUQe97m6FDoaSzjS2J2GhfVBdX91ITAFT2qf\n" +
                        "vbNiSo4MqzkiLc4BBGZuaQx1Q5Ksk+lnikV41wuj0zmn6Wd8d716OdOtAoGADaxX\n" +
                        "IUJ20jTuMQsntiKYg/su8z7UlYgQzD2sAWAJVHiAOxkBlda6YPL1YzI1zlv15bnn\n" +
                        "NsN0bh5hjO2YbPlaEbiybVk0D4hWeQVVQdSz8lQ178EjRZEWPibnTj1kFN21iPaA\n" +
                        "FzDXuBj0z2OMh6zoZIGMoUmrox1NIGSc2fR4rAECgYB9gqQdmT+j5W7yKTWARexT\n" +
                        "fbCDrMgmu7D7Fu+7FCXIPknDssBFcr019TnAxj10Bbfgp20pVtI4L2iFqqObTtxa\n" +
                        "fT/gSetRy/UsSwaDjlZuHetw0k3gF/jW0Dhak3rlU2FMFJCo2N4o9owPtmBlVStd\n" +
                        "B7PZCP4gkf9GS/vN9ovK4Q==";
        String publicKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvn4TDFYqyXrdO3qKXRd7\n" +
                        "T/yt3tCKh7yGHMPlc4PGoV+NMEZtHcsZGWbtvLXhBtWSG5kDt36KJr4cqHmcatsK\n" +
                        "NB19mfIqBP5Cn/F1zscePQgxWZH/Skbb8rplPHouKzDe+QkMHJEvvAgIxqYHxIcH\n" +
                        "iCrUm5DtE/rtavK+nvHvk4cCD5K1yieMbra/vBs30cNHj38XokZn3LR6CGoRO5zU\n" +
                        "ENP4bjFUyreCAXipgNTrSNQv5k+ZIln5F9dakPvIzumqdEJEQlCDoCIumIENKgrj\n" +
                        "ppcOzctpZeFYMSGmwXrJ/VNFzsdnlWvf08jhvB2ck5Pm8Tlsl7NYg3z23OMoU2pA\n" +
                        "PQIDAQAB";

        List<String> list1 = splitByBytes(privateKey, 64);
        List<String> list2 = splitByBytes(publicKey, 64);

        System.out.println("-----BEGIN PRIVATE KEY-----");
        for (String str : list1) {
            System.out.println(str);
        }
        System.out.println("-----END PRIVATE KEY-----");

        System.out.println("");

        System.out.println("-----BEGIN PUBLIC KEY-----");
        for (String str : list2) {
            System.out.println(str);
        }
        System.out.println("-----END PUBLIC KEY-----");


        String rsaEncrypt = null;
        String rsaDecrypt = null;

        rsaEncrypt = rsaEncryptByPublicKey(content, publicKey, "UTF-8");
        rsaDecrypt = rsaDecryptByPrivateKey(rsaEncrypt, privateKey, "UTF-8");
        System.out.println("1:rsa私钥分段解密:" + rsaDecrypt.equals(content));
        rsaEncrypt = rsaEncryptByPrivateKey(content, privateKey, "UTF-8");
        rsaDecrypt = rsaDecryptByPublicKey(rsaEncrypt, publicKey, "UTF-8");
        System.out.println("2:rsa公钥分段解密:" + rsaDecrypt.equals(content));

        String sign = rsa256Sign(content, privateKey, "UTF-8");
        boolean b = rsa256CheckContent(content, sign, publicKey, "UTF-8");
        System.out.println("3:SHA256WithRSA验签结果:" + rsaDecrypt.equals(content));

        sign = rsaSign(content, privateKey, "UTF-8");
        b = rsaCheckContent(content, sign, publicKey, "UTF-8");
        System.out.println("3:SHA1WithRSA验签结果:" + rsaDecrypt.equals(content));

        System.out.println("4.1:MD5:" + MD5(content));
        System.out.println("4.2:MD5:" + MD5(content));

        System.out.println("5.1:HmacSHA256:" + HmacSHA256(content, "0123456789"));
        System.out.println("5.2:HmacSHA256:" + HmacSHA256(content, content));
        //256秘钥问题需要安转jce:https://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
        //Exception in thread "main" java.security.InvalidKeyException: Illegal key size or default parameters

        //String key256 = generateNonceStr();
        String key256 = generateNonceStr().substring(0, 16);
        String aesEncrypt = AES_ENCRYPT(content, key256);
        String aesDecrypt = AES_DECRYPT(aesEncrypt, key256);
        System.out.println("6:AES解密结果:" + aesDecrypt.equals(content));

    }

    //功能：按固定长度来分割字符串 chenst
    public static ArrayList<String> splitByBytes(String text, int length) throws UnsupportedEncodingException {

        String encode = "UTF-8";
        if (text == null) {
            return null;
        }
        ArrayList<String> list = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        int currentLength = 0;
        for (int i = 0; i < text.toCharArray().length; i++) {
            char c = text.charAt(i);
            currentLength += String.valueOf(c).getBytes(encode).length;
            if (currentLength <= length) {
                sb.append(c);
            } else {
                currentLength = 0;
                currentLength += String.valueOf(c).getBytes(encode).length;
                list.add(sb.toString());
                sb.replace(0, sb.length(), "");
                sb.append(c);
            }
            if (i == text.toCharArray().length - 1)
                list.add(sb.toString());
        }
        return list;
    }

    public static final String SIGN_TYPE_RSA = "RSA";

    public static final String SIGN_TYPE_RSA_PAD = "RSA/ECB/PKCS1Padding";
    //int MAX_ENCRYPT_BLOCK = RSACore.getByteLength(RSAKeyFactory.toRSAKey(pubKey)) - 11;  -11与PKCS1Padding有关

    public static final String SIGN_ALGORITHMS = "SHA1withRSA";

    public static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

    /**
     * RSA最大加密明文大小,秘钥1024位
     */
    private static final int MAX_ENCRYPT_BLOCK_1024 = 117;

    /**
     * RSA最大解密密文大小,秘钥1024位
     */
    private static final int MAX_DECRYPT_BLOCK_1024 = 128;

    /**
     * RSA最大加密明文大小,秘钥2048位
     */
    private static final int MAX_ENCRYPT_BLOCK_2048 = 245;

    /**
     * RSA最大解密密文大小,秘钥2048位
     */
    private static final int MAX_DECRYPT_BLOCK_2048 = 256;

    /**
     * RSA公钥和私钥生成
     *
     * @param keySize 秘钥长度 1024 2048 4096
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public static byte[][] genRsaKeyPairBytes(int keySize)
            throws NoSuchAlgorithmException, NoSuchProviderException {
        byte[][] keyPairBytes = new byte[2][];

        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(keySize, new SecureRandom());
        KeyPair pair = gen.generateKeyPair();

        keyPairBytes[0] = pair.getPrivate().getEncoded();
        keyPairBytes[1] = pair.getPublic().getEncoded();

        return keyPairBytes;
    }

    /**
     * sha256WithRsa 加签
     */
    public static String rsa256Sign(String content, String privateKey,
            String charset) throws ApiException {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA,
                                                       new ByteArrayInputStream(privateKey.getBytes()));

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_SHA256RSA_ALGORITHMS);
            signature.initSign(priKey);

            if (isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            byte[] signed = signature.sign();


            Base64.Encoder encoder = Base64.getEncoder();
            byte[] encode = encoder.encode(signed);
            return new String(encode);
        } catch (Exception e) {
            throw new ApiException("RSAcontent = " + content + "; charset = " + charset, e);
        }

    }

    /**
     * sha1WithRsa 加签
     */
    public static String rsaSign(String content, String privateKey,
            String charset) throws ApiException {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA,
                                                       new ByteArrayInputStream(privateKey.getBytes()));
            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            if (isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }
            byte[] signed = signature.sign();
            return new String(Base64.getEncoder().encode(signed));
        } catch (InvalidKeySpecException ie) {
            throw new ApiException("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", ie);
        } catch (Exception e) {
            throw new ApiException("RSAcontent = " + content + "; charset = " + charset, e);
        }
    }

    /**
     * PrivateKey
     *
     * @param algorithm
     * @param ins
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm,
            InputStream ins) throws Exception {
        if (ins == null || isEmpty(algorithm)) {
            return null;
        }
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] encodedKey = readText(ins).getBytes();
        encodedKey = Base64.getDecoder().decode(encodedKey);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    /**
     * PublicKey
     *
     * @param algorithm
     * @param ins
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKeyFromX509(String algorithm,
            InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        StringWriter writer = new StringWriter();
        io(new InputStreamReader(ins), writer);
        byte[] encodedKey = writer.toString().getBytes();
        encodedKey = Base64.getDecoder().decode(encodedKey);
        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }

    /**
     * 验签:SHA256WithRSA
     *
     * @param content
     * @param sign
     * @param publicKey
     * @param charset
     * @return
     * @throws ApiException
     */
    public static boolean rsa256CheckContent(String content, String sign, String publicKey,
            String charset) throws ApiException {
        try {
            PublicKey pubKey = getPublicKeyFromX509("RSA",
                                                    new ByteArrayInputStream(publicKey.getBytes()));
            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_SHA256RSA_ALGORITHMS);
            signature.initVerify(pubKey);
            if (isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }
            return signature.verify(Base64.getDecoder().decode(sign.getBytes()));
        } catch (Exception e) {
            throw new ApiException(
                    "RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, e);
        }
    }

    /**
     * 验签:SHA1WithRSA
     *
     * @param content
     * @param sign
     * @param publicKey
     * @param charset
     * @return
     * @throws ApiException
     */
    public static boolean rsaCheckContent(String content, String sign, String publicKey,
            String charset) throws ApiException {
        try {
            PublicKey pubKey = getPublicKeyFromX509("RSA",
                                                    new ByteArrayInputStream(publicKey.getBytes()));

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            if (isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }
            return signature.verify(Base64.getDecoder().decode(sign.getBytes()));
        } catch (Exception e) {
            throw new ApiException(
                    "RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, e);
        }
    }


    /**
     * rsa公钥分段加密
     *
     * @param content   待加密内容
     * @param publicKey 公钥
     * @param charset   字符集，如UTF-8, GBK, GB2312
     * @return 密文内容
     * @throws ApiException
     */
    public static String rsaEncryptByPublicKey(String content, String publicKey,
            String charset) throws ApiException {
        try {
            PublicKey pubKey = getPublicKeyFromX509(SIGN_TYPE_RSA,
                                                    new ByteArrayInputStream(publicKey.getBytes()));

            int MAX_ENCRYPT_BLOCK = RSACore.getByteLength(RSAKeyFactory.toRSAKey(pubKey)) - 11;

            Cipher cipher = Cipher.getInstance(SIGN_TYPE_RSA_PAD);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] data = isEmpty(charset) ? content.getBytes()
                    : content.getBytes(charset);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = Base64.getEncoder().encode(out.toByteArray());
            out.close();

            return isEmpty(charset) ? new String(encryptedData)
                    : new String(encryptedData, charset);
        } catch (Exception e) {
            throw new ApiException("EncryptContent = " + content + ",charset = " + charset,
                                   e);
        }
    }

    /**
     * rsa私钥分段解密
     *
     * @param content    待解密内容
     * @param privateKey 私钥
     * @param charset    字符集，如UTF-8, GBK, GB2312
     * @return 明文内容
     * @throws ApiException
     */
    public static String rsaDecryptByPrivateKey(String content, String privateKey,
            String charset) throws ApiException {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA,
                                                       new ByteArrayInputStream(privateKey.getBytes()));

            int MAX_DECRYPT_BLOCK = RSACore.getByteLength(RSAKeyFactory.toRSAKey(priKey));


            Cipher cipher = Cipher.getInstance(SIGN_TYPE_RSA_PAD);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            byte[] encryptedData = isEmpty(charset)
                    ? Base64.getDecoder().decode(content.getBytes())
                    : Base64.getDecoder().decode(content.getBytes(charset));
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();

            return isEmpty(charset) ? new String(decryptedData)
                    : new String(decryptedData, charset);
        } catch (Exception e) {
            throw new ApiException("EncodeContent = " + content + ",charset = " + charset, e);
        }
    }

    /**
     * rsa私钥分段加密
     *
     * @param content    待加密内容
     * @param privateKey 私钥
     * @param charset    字符集，如UTF-8, GBK, GB2312
     * @return 密文内容
     * @throws ApiException
     */
    public static String rsaEncryptByPrivateKey(String content, String privateKey,
            String charset) throws ApiException {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA,
                                                       new ByteArrayInputStream(privateKey.getBytes()));

            int MAX_ENCRYPT_BLOCK = RSACore.getByteLength(RSAKeyFactory.toRSAKey(priKey)) - 11;
            Cipher cipher = Cipher.getInstance(SIGN_TYPE_RSA_PAD);
            cipher.init(Cipher.ENCRYPT_MODE, priKey);
            byte[] data = isEmpty(charset) ? content.getBytes()
                    : content.getBytes(charset);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = Base64.getEncoder().encode(out.toByteArray());
            out.close();

            return isEmpty(charset) ? new String(encryptedData)
                    : new String(encryptedData, charset);
        } catch (Exception e) {
            throw new ApiException("EncryptContent = " + content + ",charset = " + charset,
                                   e);
        }
    }

    /**
     * rsa公钥分段解密
     *
     * @param content   待解密内容
     * @param publicKey 私钥
     * @param charset   字符集，如UTF-8, GBK, GB2312
     * @return 明文内容
     * @throws ApiException
     */
    public static String rsaDecryptByPublicKey(String content, String publicKey,
            String charset) throws ApiException {
        try {
            PublicKey pubKey = getPublicKeyFromX509(SIGN_TYPE_RSA,
                                                    new ByteArrayInputStream(publicKey.getBytes()));
            Cipher cipher = Cipher.getInstance(SIGN_TYPE_RSA_PAD);

            int MAX_DECRYPT_BLOCK = RSACore.getByteLength(RSAKeyFactory.toRSAKey(pubKey));

            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            byte[] encryptedData = isEmpty(charset)
                    ? Base64.getDecoder().decode(content.getBytes())
                    : Base64.getDecoder().decode(content.getBytes(charset));
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();

            return isEmpty(charset) ? new String(decryptedData)
                    : new String(decryptedData, charset);
        } catch (Exception e) {
            throw new ApiException("EncodeContent = " + content + ",charset = " + charset, e);
        }
    }

    public static boolean isEmpty(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(value.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 支持的Algorithm以及提供者
     */
    public static void support() {
        Security.addProvider(new BouncyCastleProvider());
        for (Provider provider : Security.getProviders()) {
            System.out.println("Provider: " + provider.getName());
            for (Provider.Service service : provider.getServices()) {
                System.out.println("  Algorithm: " + service.getAlgorithm());
            }
        }
    }

    /**
     * AES 加密
     *
     * @param data
     * @param key  长度 128/192/256 bits.256注意jdk问题
     * @return
     * @throws Exception
     */
    public static String AES_ENCRYPT(String data, String key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] result = cipher.doFinal(data.getBytes("UTF-8"));
        byte[] encode = Base64.getEncoder().encode(result);
        return new String(encode);
    }

    /**
     * AES 解密
     *
     * @param data
     * @param key  128/192/256 bits.256注意jdk问题
     * @return
     * @throws Exception
     */
    public static String AES_DECRYPT(String data, String key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] encode = Base64.getDecoder().decode(data);
        byte[] result = cipher.doFinal(encode);

        return new String(result, "UTF-8");
    }


    /**
     * MD5
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * HMACSHA256
     *
     * @param data 待处理数据
     * @param key  密钥
     * @return 加密结果
     */
    public static String HmacSHA256(String data, String key) throws Exception {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        hmacSHA256.init(secret_key);
        byte[] array = hmacSHA256.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }


    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new SecureRandom();

    /**
     * 作为MD5和HMACSHA256的随机值
     *
     * @return
     */
    public static String generateNonceStr() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    public static class ApiException extends Exception {
        private String errCode;
        private String errMsg;

        public ApiException() {
            super();
        }

        public ApiException(String message, Throwable cause) {
            super(message, cause);
        }

        public ApiException(String message) {
            super(message);
        }

        public ApiException(Throwable cause) {
            super(cause);
        }

        public ApiException(String errCode, String errMsg) {
            super(errCode + ":" + errMsg);
            this.errCode = errCode;
            this.errMsg = errMsg;
        }

        public String getErrCode() {
            return this.errCode;
        }

        public String getErrMsg() {
            return this.errMsg;
        }

    }


    private static String readText(InputStream in)
            throws IOException {
        return readText(in, null);
    }

    private static String readText(InputStream in, String encoding)
            throws IOException {
        Reader reader = (encoding == null) ? new InputStreamReader(in) : new InputStreamReader(in,
                                                                                               encoding);
        return readText(reader);
    }

    private static String readText(Reader reader) throws IOException {
        StringWriter writer = new StringWriter();
        io(reader, writer);
        return writer.toString();
    }

    private static void io(Reader in, Writer out) throws IOException {
        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        int amount;

        while ((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }
    }
}
