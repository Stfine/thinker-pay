package vc.thinker.pay.util;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.net.ssl.SSLContext;
import java.util.Map;

/**
 *
 * @author ZhangGaoXiang
 * @time May 8, 20194:19:16 PM
 */
public class HttpKitUtils {
	
	private static Logger logger = LoggerFactory.getLogger(HttpKitUtils.class);

	private static CloseableHttpClient httpClient;

	/**
	 * 信任SSL证书
	 */
	static {
		try {
			SSLContext sslContext = SSLContextBuilder.create().useProtocol(SSLConnectionSocketFactory.SSL)
					.loadTrustMaterial((x, y) -> true).build();
			RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(8000).build();
			httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).setSSLContext(sslContext)
					.setSSLHostnameVerifier((x, y) -> true).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * post请求 发送json格式的报文 StringEntity
	 * 
	 * @param url
	 * @param jsonString
	 * @return
	 */
	public static String doPost(String url, String jsonString) {
		try {
			HttpPost httpPost = new HttpPost(url);
			
			RequestConfig config = RequestConfig.custom()
					.setConnectTimeout(5000)
					.setConnectionRequestTimeout(2000)
					.setSocketTimeout(8000)
					.build();
			httpPost.setConfig(config);
			
			StringEntity stringEntity = new StringEntity(jsonString, "utf-8");
			stringEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(stringEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
//			int statusCode = response.getStatusLine().getStatusCode();
//			if (statusCode != 200) {
//				httpPost.abort();
//				throw new RuntimeException("HttpClient is error status code :" + statusCode);
//			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			logger.info("======Exception:[{}]",e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public static String doGet(String url, Map<String, String> map) throws Exception {
		
		try {
			HttpGet httpGet = new HttpGet(url);
			RequestConfig config = RequestConfig.custom()
					.setConnectTimeout(5000)
					.setConnectionRequestTimeout(2000)
					.setSocketTimeout(8000)
					.build();
			httpGet.setConfig(config);
			if(!map.isEmpty()) {
				map.keySet().forEach(e->{
					httpGet.setHeader(e, map.get(e).toString());
				});
			}
			
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				throw new RuntimeException("HttpClient is error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			logger.info("=====请求第三方出现异常[{}]", e.getMessage());
			throw e;
		}
	}
	
}
