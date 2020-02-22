package vc.thinker.pay.weixin.utils;

import java.io.ByteArrayOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class QRCodeUtils {
	/**
     * 生成二维码图片 不存储 直接以流的形式输出到页面
     * @param content
     * @param response
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String encodeQrcode(String content){
        if(StringUtils.isBlank(content))
            return "";

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
		BitMatrix bitMatrix;
		try {
			bitMatrix = new MultiFormatWriter().encode(content,
					BarcodeFormat.QR_CODE, 300, 300, null);
			MatrixToImageWriter.writeToStream(bitMatrix, "png", stream);
			String qrcode_base64 = Base64.encodeBase64String(stream.toByteArray());


			return qrcode_base64;
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){

		}
		return "";


    }
}
