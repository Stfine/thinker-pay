//import java.io.IOException;
//import java.security.KeyManagementException;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.UnrecoverableKeyException;
//
//import org.junit.Test;
//
//import vc.thinker.pay.weixin.WeixinConfig;
//import vc.thinker.pay.weixin.api.PayUtils;
//import vc.thinker.pay.weixin.utils.HttpsRequest;
//
//public class WeixinTest {
//
//	@Test
//	public void validateUrlcertificate(){
//		WeixinConfig config=new WeixinConfig();
//		config.setCertificateType(WeixinConfig.CertificateType.url);
//		config.setCertLocalPath("http://test.f2.thinker.vc/7,0be4dc59c0");
//		try {
//			HttpsRequest httpsRequest = new HttpsRequest("1236624702",config);
//		} catch (UnrecoverableKeyException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (KeyManagementException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (KeyStoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//}
