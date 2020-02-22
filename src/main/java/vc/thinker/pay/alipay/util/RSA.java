package vc.thinker.pay.alipay.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import vc.thinker.pay.weixin.WeixinConfig;

public class RSA {

	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	/**
	 * RSA签名
	 * 
	 * @param content
	 *            待签名数据
	 * @param privateKey
	 *            商户私钥
	 * @param input_charset
	 *            编码格式
	 * @return 签名值
	 */
	public static String sign(String content, String privateKey,
			String input_charset) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(input_charset));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * RSA验签名检查
	 * 
	 * @param content
	 *            待签名数据
	 * @param sign
	 *            签名值
	 * @param ali_public_key
	 *            支付宝公钥
	 * @param input_charset
	 *            编码格式
	 * @return 布尔值
	 */
	public static boolean verify(String content, String sign,
			String ali_public_key, String input_charset) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(ali_public_key);
			PublicKey pubKey = keyFactory
					.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes(input_charset));

			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            密文
	 * @param private_key
	 *            商户私钥
	 * @param input_charset
	 *            编码格式
	 * @return 解密后的字符串
	 */
	public static String decrypt(String content, String private_key,
			String input_charset) throws Exception {
		PrivateKey prikey = getPrivateKey(private_key);

		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, prikey);

		InputStream ins = new ByteArrayInputStream(Base64.decode(content));
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		// rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
		byte[] buf = new byte[128];
		int bufl;

		while ((bufl = ins.read(buf)) != -1) {
			byte[] block = null;

			if (buf.length == bufl) {
				block = buf;
			} else {
				block = new byte[bufl];
				for (int i = 0; i < bufl; i++) {
					block[i] = buf[i];
				}
			}

			writer.write(cipher.doFinal(block));
		}

		return new String(writer.toByteArray(), input_charset);
	}

	/**
	 * 得到私钥
	 * 
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String key) throws Exception {

		byte[] keyBytes;

		keyBytes = Base64.decode(key);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		return privateKey;
	}

    /** 
     * 公钥加密过程 
     *  
     * @param publicKey 
     *            公钥 
     * @param plainTextData 
     *            明文数据 
     * @return 
     * @throws Exception 
     *             加密过程中的异常信息 
     */  
    public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData)  
            throws Exception {  
        if (publicKey == null) {  
            throw new Exception("加密公钥为空, 请设置");  
        }  
        Cipher cipher = null;  
        try {  
            // 使用默认RSA  
            cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING");
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());  
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
            byte[] output = cipher.doFinal(plainTextData);  
            return output;  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此加密算法");  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
            return null;  
        } catch (InvalidKeyException e) {  
            throw new Exception("加密公钥非法,请检查");  
        } catch (IllegalBlockSizeException e) {  
            throw new Exception("明文长度非法");  
        } catch (BadPaddingException e) {  
            throw new Exception("明文数据已损坏");  
        }  
    }
    
    /** 
     * 从文件中输入流中加载公钥 
     *  
     * @param in 
     *            公钥输入流 
     * @throws Exception 
     *             加载公钥时产生的异常 
     */  
    public static String loadPublicKeyByResource(String path) throws Exception {  
        try {  
            Resource resource = new ClassPathResource(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));  
            String readLine = null;  
            StringBuilder sb = new StringBuilder();  
            while ((readLine = br.readLine()) != null) {  
                sb.append(readLine);  
            }  
            br.close();  
            return sb.toString();  
        } catch (IOException e) {  
            throw new Exception("公钥数据流读取错误");  
        } catch (NullPointerException e) {  
            throw new Exception("公钥输入流为空");  
        }  
    }  
  
    public static PublicKey getPubKeyByFile(String publicKeyPath,String keyAlgorithm){  
        PublicKey publicKey = null;  
        InputStream inputStream = null;  
        try{
            inputStream = new FileInputStream(publicKeyPath);  
            publicKey = getPublicKey(inputStream,keyAlgorithm);  
        } catch (Exception e) {  
            e.printStackTrace();//EAD PUBLIC KEY ERROR
            System.out.println("加载公钥出错!");  
        } finally {  
            if (inputStream != null){  
                try {  
                    inputStream.close();  
                }catch (Exception e){  
                    System.out.println("加载公钥,关闭流时出错!");  
                }  
            }  
        }  
        return publicKey;  
    }  
    
    public static PublicKey getPublicKey(InputStream inputStream, String keyAlgorithm) throws Exception {  
        try 
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream)); 
            StringBuilder sb = new StringBuilder();  
            String readLine = null;
            while ((readLine = br.readLine()) != null) {  
                if (readLine.charAt(0) == '-') {  
                    continue;  
                } else {  
                    sb.append(readLine);  
                    sb.append('\r');  
                }  
            }  
            X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decode(sb.toString()));  
            KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
            PublicKey publicKey = keyFactory.generatePublic(pubX509);  
            return publicKey;  
        } catch (Exception e) {  
        	e.printStackTrace();
            throw new Exception("READ PUBLIC KEY ERROR:", e);  
        } finally {  
            try {  
                if (inputStream != null) {  
                    inputStream.close();  
                }  
            } catch (IOException e) {  
                inputStream = null;  
                throw new Exception("INPUT STREAM CLOSE ERROR:", e);  
            }  
        }  
    }  
    public static PublicKey getPublicKey(String keyText, String keyAlgorithm) throws Exception {  
    	try 
    	{
    		X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decode(keyText));  
    		KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
    		PublicKey publicKey = keyFactory.generatePublic(pubX509);  
    		return publicKey;  
    	} catch (Exception e) {  
    		throw new Exception("READ PUBLIC KEY ERROR:", e);  
    	}
    }  
    
    public static byte[] encrypt(byte[] plainBytes, PublicKey publicKey, int keyLength, int reserveSize, String cipherAlgorithm) throws Exception {  
        int keyByteSize = keyLength / 8;  
        int encryptBlockSize = keyByteSize - reserveSize;  
        int nBlock = plainBytes.length / encryptBlockSize;  
        if ((plainBytes.length % encryptBlockSize) != 0) {  
            nBlock += 1;  
        }  
        ByteArrayOutputStream outbuf = null;  
        try {  
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);  
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
  
            outbuf = new ByteArrayOutputStream(nBlock * keyByteSize);  
            for (int offset = 0; offset < plainBytes.length; offset += encryptBlockSize) {  
                int inputLen = plainBytes.length - offset;  
                if (inputLen > encryptBlockSize) {  
                    inputLen = encryptBlockSize;  
                }  
                byte[] encryptedBlock = cipher.doFinal(plainBytes, offset, inputLen);  
                outbuf.write(encryptedBlock);  
            }  
            outbuf.flush();  
            return outbuf.toByteArray();  
        } catch (Exception e) {  
            throw new Exception("ENCRYPT ERROR:", e);  
        } finally {  
            try{  
                if(outbuf != null){  
                    outbuf.close();  
                }  
            }catch (Exception e){  
                outbuf = null;  
                throw new Exception("CLOSE ByteArrayOutputStream ERROR:", e);  
            }  
        }  
    }  
    
    public static String encrypt(WeixinConfig config,String content) throws Exception {
    	
		try {
			
			String rsa ="RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING";
			
			 X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decode(config.getPayBankPublicKey()));  
	         KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	         PublicKey publicKey = keyFactory.generatePublic(pubX509); 
			
			return Base64.encode(encrypt(content.getBytes("utf-8"),publicKey,2048,11,rsa));
		} catch (Exception e) {
			throw new Exception("ERROR:", e);
		}
    }
    
    
    public static void main(String[] args) {
    	
//    	String rsa ="RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING";
//    	
//    	String fileName = "public_js_8.pem";
//    	ClassLoader classLoader = RSA.class.getClassLoader();
//    	URL url = classLoader.getResource(fileName);
//    	String filePath = url.getPath();
    	
//    	System.out.println(filePath);
//    	ClassLoader classLoader = getClass().getClassLoader();
//    	URL url = classLoader.getResource("public_js_8.pem");
    	
//    	PublicKey pub=getPubKeyByFile(filePath,"RSA"); 
    	
         
		try {
//			System.out.println(Base64.encode(encrypt("1123")));
//			System.out.println(Base64.encode(encrypt("张高翔").getBytes()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
