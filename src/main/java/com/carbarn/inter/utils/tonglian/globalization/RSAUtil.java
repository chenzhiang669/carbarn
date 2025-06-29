package com.carbarn.inter.utils.tonglian.globalization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class RSAUtil {
	private static final Logger logger = LoggerFactory.getLogger(RSAUtil.class);
	
	/** Cipher 运算法则公式 = 算法/模式/填充 */
	public static final String RSA_ECB_PKCS1Padding = "RSA/ECB/PKCS1Padding";

	public static final String SIGN_SHA1_WITH_RSA = "SHA1WithRSA";
	public static final String SIGN_SHA256_WITH_RSA = "SHA256WithRSA";
	public static final String SIGN_MD5_WITH_RSA = "MD5withRSA";

	// --------------------------------- SIGN -----------------------------------//

	public static String sign(String content, String privateKey, String input_charset) throws Exception {
		if (privateKey == null) {
			throw new Exception("加密私钥为空, 请设置");
		}
		PrivateKey privateKeyInfo = getPrivateKey(privateKey);
		return sign(content, privateKeyInfo, input_charset);
	}


	public static String sign(String content, PrivateKey privateKey, String input_charset) throws Exception {
		if (privateKey == null) {
			throw new Exception("加密私钥为空, 请设置");
		}
		java.security.Signature signature = java.security.Signature.getInstance(SIGN_SHA1_WITH_RSA);
		signature.initSign(privateKey);
		signature.update(content.getBytes(input_charset));
		return Base64.encode(signature.sign());
	}
	

	public static String signSHA256RSA(String content, String privateKey, String input_charset)
			throws Exception {
		if (privateKey == null) {
			throw new Exception("加密私钥为空, 请设置");
		}
		PrivateKey privateKeyInfo = getPrivateKey(privateKey);
		return signSHA256RSA(content, privateKeyInfo, input_charset);
	}
	

	public static String signSHA256RSA(String content, PrivateKey privateKey, String input_charset)
			throws Exception {
		if (privateKey == null) {
			throw new Exception("加密私钥为空, 请设置");
		}
		java.security.Signature signature = java.security.Signature.getInstance(SIGN_SHA256_WITH_RSA);
		signature.initSign(privateKey);
		signature.update(content.getBytes(input_charset));
		return Base64.encode(signature.sign());
	}

	public static String signMD5RSA(String content, PrivateKey privateKey, String input_charset)
			throws Exception {
		if (privateKey == null) {
			throw new Exception("加密私钥为空, 请设置");
		}
		java.security.Signature signature = java.security.Signature.getInstance(SIGN_MD5_WITH_RSA);
		signature.initSign(privateKey);
		signature.update(content.getBytes(input_charset));
		return Base64.encode(signature.sign());
	}
	
	// --------------------------------- VERIFY -----------------------------------//
	public static boolean verify(String content, String sign, String publicKey, String input_charset) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			return verify(content, sign, pubKey, input_charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
	

	public static boolean verify(String content, String sign, PublicKey publicKey, String inputCharset) {
		try {
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_SHA1_WITH_RSA);
			signature.initVerify(publicKey);
			signature.update(content.getBytes(inputCharset));
			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean verifyMD5RSA(String content, String sign, PublicKey publicKey, String inputCharset) {
		try {
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_MD5_WITH_RSA);
			signature.initVerify(publicKey);
			signature.update(content.getBytes(inputCharset));
			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public static boolean verifySHA256(String content, String sign, String publicKey, String inputCharset) {
		try {
			return verifySHA256(content, sign, getPublicKey(publicKey), inputCharset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean verifySHA256(String content, String sign, PublicKey publicKey, String inputCharset) {
		try {
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_SHA256_WITH_RSA);
			signature.initVerify(publicKey);
			signature.update(content.getBytes(inputCharset));
			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


    
	
	// --------------------------------- DECRYPT -------------------------------//

	public static String decryptByPublic(String content, String publicKey, String input_charset) throws Exception {
		PublicKey pubKey = getPublicKey(publicKey);
		return decrypt(content, input_charset, pubKey);
	}
    
	public static String decrypt(String content, String private_key, String input_charset) throws Exception {
		PrivateKey prikey = getPrivateKey(private_key);
		return decrypt(content, input_charset, prikey);
	}

	public static String decrypt(String content, PrivateKey privateKey, String input_charset,
			String transformation) throws Exception {
		return decrypt(content, input_charset, privateKey, 128);
	}

	private static String decrypt(String content, String input_charset, Key key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchProviderException {
		return decrypt(content, input_charset, key, 128);
	}

	public static String decrypt(String content, String input_charset, Key key, int contentSize)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchProviderException {
		Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1Padding, new org.bouncycastle.jce.provider.BouncyCastleProvider());
		cipher.init(Cipher.DECRYPT_MODE, key);
		InputStream ins = new ByteArrayInputStream(Base64.decode(content));

		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		// rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
		byte[] buf = new byte[contentSize];
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

	public static String decryptNoProvider(String content, String input_charset, Key key,
			int contentSize) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchProviderException {
		Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1Padding);
		cipher.init(Cipher.DECRYPT_MODE, key);
		InputStream ins = new ByteArrayInputStream(Base64.decode(content));

		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		// rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
		byte[] buf = new byte[contentSize];
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
		byte[] keyBytes = buildPKCS8Key(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	private static byte[] buildPKCS8Key(String privateKey) throws IOException {
		if (privateKey.contains("-----BEGIN PRIVATE KEY-----")) {
			return Base64.decode(privateKey.replaceAll("-----\\w+ PRIVATE KEY-----", ""));
		} else if (privateKey.contains("-----BEGIN RSA PRIVATE KEY-----")) {
			final byte[] innerKey = Base64.decode(privateKey.replaceAll("-----\\w+ RSA PRIVATE KEY-----", ""));
			final byte[] result = new byte[innerKey.length + 26];
			System.arraycopy(Base64.decode("MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKY="), 0, result, 0, 26);
			System.arraycopy(BigInteger.valueOf(result.length - 4).toByteArray(), 0, result, 2, 2);
			System.arraycopy(BigInteger.valueOf(innerKey.length).toByteArray(), 0, result, 24, 2);
			System.arraycopy(innerKey, 0, result, 26, innerKey.length);
			return result;
		} else {
			return Base64.decode(privateKey);
		}
	}

	public static KeyInfo getKeyInfoByPFXStr(String pfxStr, String password) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
		// FileInputStream fis = new FileInputStream(pfxPath);
		InputStream fis = new ByteArrayInputStream(Base64.decode(pfxStr));
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(fis, password.toCharArray());
		fis.close();
		Enumeration<String> enumas = ks.aliases();
		String keyAlias = null;
		if (enumas.hasMoreElements())// we are readin just one certificate.
		{
			keyAlias = enumas.nextElement();
		}

		KeyInfo keyInfo = new KeyInfo();

		PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, password.toCharArray());
		Certificate cert = ks.getCertificate(keyAlias);
		PublicKey pubkey = cert.getPublicKey();

		keyInfo.privateKey = prikey;
		keyInfo.publicKey = pubkey;
		return keyInfo;
	}

	public static class KeyInfo {

		PublicKey publicKey;
		PrivateKey privateKey;

		public PublicKey getPublicKey() {
			return publicKey;
		}

		public PrivateKey getPrivateKey() {
			return privateKey;
		}
	}

	public static PublicKey getPublicKey(String key) throws Exception {
		if (key == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		key = key.replaceAll("\\-{5}[\\w\\s]+\\-{5}[\\r\\n|\\n]", "");
		byte[] buffer = Base64.decode(key);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
		return keyFactory.generatePublic(keySpec);
	}

	public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = Base64.decode(key);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1Padding); //"RSA/ECB/PKCS1Padding"
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = Base64.decode(key);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1Padding); //"RSA/ECB/PKCS1Padding"
		cipher.init(2, publicKey);
		return cipher.doFinal(data);
	}

    public static String readFile(String filePath, String charSet) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        try {
            FileChannel fileChannel = fileInputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fileChannel.size());
            fileChannel.read(byteBuffer);
            byteBuffer.flip();
            return new String(byteBuffer.array(), charSet);
        } finally {
            fileInputStream.close();
        }
    }


	//@陈志昂自己添加的接口: 签名
	public static String sign(TreeMap<String, String> map,
							   String priv_key) {
		try {
			StringBuilder signature = new StringBuilder();
			for (String key : map.keySet()) {
				if (key.equals("sign"))
					continue;
				String value = map.get(key);
				if (value != null && value.length() != 0) {
					signature.append(key + "=" + value.trim() + "&");
				}
			}

			String preSign = signature.toString().substring(0, signature.length() - 1).toString();
			if (map.get("signType") == null || map.get("signType").equals("MD5")){
				preSign = preSign.concat("&key=").concat(Config.KEY);
				System.out.println("签名待请求字符串" + preSign);
				return DigestUtils.md5Hex(preSign.getBytes("UTF-8")).toUpperCase();
			}else{
				System.out.println("签名待请求字符串" + preSign);
				return RSAUtil.signSHA256RSA(preSign, priv_key, "UTF-8");
			}

		} catch (Exception e) {
			System.out.println("签名异常");
			e.printStackTrace();
		}
		return null;
	}

	//@陈志昂自己添加的接口: 验证签名
	public static boolean verifySign(String respStr,
									 String signType,
									 String pub_key) {
		try {
			JSONObject jsonObject = JSONObject.parseObject(respStr, Feature.OrderedField);
			String resSign = jsonObject.getString("sign");

			Map<String, String> resSignMap = new TreeMap<>();
			for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
				if (entry.getKey().equals("sign"))
					continue;
				resSignMap.put(entry.getKey(), (String)entry.getValue().toString());
			}
			StringBuilder resSb = new StringBuilder();
			for (Map.Entry<String, String> entry : resSignMap.entrySet()) {
				resSb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
			String preSign = resSb.toString().substring(0, resSb.toString().length() - 1).toString();
			if(signType == null || signType.equals("MD5")){
				preSign = preSign.concat("&key=").concat(Config.KEY);
				System.out.println("待验签字符串" + preSign);
				Boolean vResult = DigestUtils.md5Hex(preSign.getBytes("UTF-8")).toUpperCase().equals(resSign);
				if (!vResult) {
					System.out.println("验证签名失败[" + resSign + ":" + DigestUtils.md5Hex(preSign.getBytes("UTF-8")).toUpperCase() +"]" );
					return false;
				}else{
					System.out.println("MD5签名验证成功");
					return true;
				}
			}else{
				PublicKey accessPublicKey = RSAUtil.getPublicKey(pub_key);
				System.out.println("preSign:" + preSign);
				System.out.println("resSign:" + resSign);
				// 验证签名信息
				if (!RSAUtil.verifySHA256(preSign, resSign, accessPublicKey, "UTF-8")) {
					System.out.println("验证签名不通过 :\n" +  resSign + "\n" +  preSign);
					return false;
				}else{
					System.out.println("RSA2签名验证成功");
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println("签名异常");
			e.printStackTrace();
			return false;
		}
	}


	//国外支付交易结果查询
	public static String getGlobalOrderState(String url,
										   TreeMap<String, String> param){
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);

			// 准备表单参数
			List<NameValuePair> params = new ArrayList<>();
			for (String key : param.keySet()) {
				params.add(new BasicNameValuePair(key, param.get(key)));
			}

			httpPost.setEntity(new UrlEncodedFormEntity(params));
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int code = response.getStatusLine().getStatusCode();
			logger.info("code: " + code);
			String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
			logger.info("responseString: " + responseString);
			return responseString;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getPayUrl(String base_http_url,
								   String tonglian_url,
								   JSONObject param_json){
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(tonglian_url);

			// 准备表单参数
			List<NameValuePair> params = new ArrayList<>();
			for (String key : param_json.keySet()) {
				params.add(new BasicNameValuePair(key, param_json.getString(key)));
			}

			httpPost.setEntity(new UrlEncodedFormEntity(params));
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int code = response.getStatusLine().getStatusCode();
			System.out.println("code: " + code);
			if(200 == response.getStatusLine().getStatusCode()){
				String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
				logger.info("responseString:{}", responseString);
				JSONObject json = JSON.parseObject(responseString);
				String payUrl = json.getString("payUrl");
				logger.info("payUrl:{}", payUrl);
				String payUrlEncode = URLEncoder.encode(payUrl, StandardCharsets.UTF_8.toString());
				String pay_url = base_http_url +  payUrlEncode;
				logger.info("pay_url:{}", pay_url);
				return pay_url;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String str = "{\n" +
				"        \"shippingCity\": \"Mountain View\",\n" +
				"        \"sign\": \"SmfldZs6AOOo9ovf5p3YZz0fPflTvPksSn7Uw/PEH8EZCLm7clinYHelqkZu4LENb+SCEAOgC1f9HPB5qwom9Z6axpQcVUvzwpz4frcNT5Ca71DMd47jsGfWUU9dW7lDAkabArLn00fhTeTWDWWB1VJSz0JxFwkW1UvGHWvyFU6ptBGk47AlFMrhdcGRre2JTVEw6iRBJpJNpPkk3qEyufQvBUWKXd1Vpv/ZSvK5Lz0sq1KY1McUZge8NF2Ricjh96YBDpZ77Scw4cOoHAN15mwidV/D2hr5k1yBWTQNwOENPwZxSzc/YBZIDS9jONWeRiClTJkCpRZjsIcHGtqQMg==\",\n" +
				"        \"language\": \"en\",\n" +
				"        \"shippingAddress1\": \"1295 Charleston Rd\",\n" +
				"        \"billingAddress1\": \"1295 Charleston Rd\",\n" +
				"        \"shippingCountry\": \"US\",\n" +
				"        \"shippingZipCode\": \"94043\",\n" +
				"        \"billingFirstName\": \"noreal\",\n" +
				"        \"billingCountry\": \"US\",\n" +
				"        \"signType\": \"RSA2\",\n" +
				"        \"currency\": \"USD\",\n" +
				"        \"returnUrl\": \"http://chechuhai.top:8080/static/global-payed.html\",\n" +
				"        \"email\": \"1461251592@qq.com\",\n" +
				"        \"amount\": \"500\",\n" +
				"        \"shippingLastName\": \"name\",\n" +
				"        \"accessOrderId\": \"20250531092343B023JCivv8\",\n" +
				"        \"billingZipCode\": \"94043\",\n" +
				"        \"shippingPhone\": \"00000000000\",\n" +
				"        \"billingState\": \"CA\",\n" +
				"        \"version\": \"V2.0.0\",\n" +
				"        \"productInfo\": \"[]\",\n" +
				"        \"billingLastName\": \"name\",\n" +
				"        \"transType\": \"Pay\",\n" +
				"        \"shippingFirstName\": \"noreal\",\n" +
				"        \"mchtId\": \"086310058120027\",\n" +
				"        \"notifyUrl\": \"http://chechuhai.top:8080/carbarn/pay/callbackglobal\",\n" +
				"        \"shippingState\": \"CA\",\n" +
				"        \"billingCity\": \"Mountain View\",\n" +
				"        \"billingPhone\": \"00000000000\"\n" +
				"    }";

		String pub_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxbffUvWnWwvOJSxMRBeL4xR4rqxTUZgVRDJlVXiUH80G0P+n2NLPtlcdJR1XA+WVXQ0cG05dixvvEpVh+ml5rzpOY+QyPcBiRMD6Ni/Bh/UR2xzELtI6/flDve5AQoyjWSplpGzbD631Gx8VriihSC2mXL+C2DbIwTb1ZL0VOu+JSVUPk2fGbDy73OfiN2pyRi27uQbdYoKm576TFdbHmLFJzW7gwUqGB5dPQj7xkXOZK5kcqLsMC5V4sHCQzQ7nZeVKIcI9iwrogk1n5HfzHtbctWKwGUfWv84frzO3S3PWggZ4nNv+yWAbT8HCigtGYsfTg2zXoS2oRDar8ZvSrQIDAQAB";
		boolean bool = RSAUtil.verifySign(str, "RSA2", pub_key);
		if(bool){
			System.out.println("verifySign success");
		}else{
			System.out.println("verifySign fail");
		}
	}
}