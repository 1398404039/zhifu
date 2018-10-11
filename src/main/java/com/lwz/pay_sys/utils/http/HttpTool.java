package com.lwz.pay_sys.utils.http;

import com.lwz.pay_sys.utils.response.ConstVar;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LIKE
 * @date 2017年1月18日 下午6:07:06
 */
public class HttpTool {
    private static Logger logger = LoggerFactory.getLogger(HttpTool.class);
    private static PoolingHttpClientConnectionManager cm;

    private static void init() {
        if (cm == null) {
            cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(50);// 整个链接池最大链接数
            cm.setDefaultMaxPerRoute(5);// 每个路由最大链接数，默认是2
        }
    }

    public static String httpGetRequest(String url) {
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet);
    }

    public static String httpPostRequest(String url) {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost);
    }

    public static String httpGetRequest(String url, Map<String, Object> params) {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        List<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);
        try {
            HttpGet httpGet = new HttpGet(ub.build());
            return getResult(httpGet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        List<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        return getResult(httpGet);
    }

    public static String httpPostRequest(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, ConstVar.ENCODING));
        return getResult(httpPost);
    }

    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        List<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, ConstVar.ENCODING));
        return getResult(httpPost);
    }

    private static List<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }
        return pairs;
    }

    private static CloseableHttpClient getHttpClient() {
        init();
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    private static String getResult(HttpRequestBase request) {
        CloseableHttpClient httpClient = getHttpClient();
        try {
            CloseableHttpResponse resp = httpClient.execute(request);

            if (resp.getStatusLine().getStatusCode() == 200) {//响应状态

                long contentLength = resp.getEntity().getContentLength();

                if (contentLength == 0) {
                    return "";
                }

                HttpEntity entity = resp.getEntity();// 响应实体
                String result = EntityUtils.toString(entity);
                resp.close();
                logger.info("httpClient 结果=>：" + result);
                return result;
            } else {
                logger.debug(HttpTool.class.getName() + " getResult error", resp.getStatusLine().getStatusCode());
            }

			/*HttpEntity entity = resp.getEntity();// 响应实体
			if (entity != null) {
				// long len = entity.getContentLength();// -1 表示长度未知
				String result = EntityUtils.toString(entity);
				resp.close();
				logger.info("httpClient 结果=>：" + result);
				return result;
			}*/
        } catch (Exception e) {
            logger.debug(HttpTool.class.getName() + " getResult error", e);
            e.printStackTrace();
        }
        return "";
    }

//	public static String httpPost(String url, JSONObject jsonParam, boolean noNeedResponse) {
//		CloseableHttpClient httpClient = HttpClients.createDefault();
//		JSONObject jsonResult = null;
//		HttpPost method = new HttpPost(url);
//		try {
//			if (jsonResult != null) {
//				// 解决中文乱码问题
//				StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
//				entity.setContentEncoding("UTF-8");
//				entity.setContentType("application/json");
//				method.setEntity(entity);
//			}
//			HttpResponse result = httpClient.execute(method);
//			url = URLDecoder.decode(url, "UTF-8");
//			/** 请求发送成功，并得到响应 **/
//			if (result.getStatusLine().getStatusCode() == 200) {
//				String str = "";
//				try {
//					/** 读取服务器返回过来的json字符串数据 **/
//					str = EntityUtils.toString(result.getEntity());
//					if (noNeedResponse) {
//						return null;
//					}
//					/** 把json字符串转换成json对象 **/
//					jsonResult = JSONObject.parseObject(str);
//				} catch (Exception e) {
//					logger.error("post请求提交失败:" + url, e);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return jsonResult;
//	}

    /**
     * 发送get请求
     *
     * @param
     * @return
     */
//	public static String httpGet(String url) {
//		// get请求返回结果
//		JSONObject jsonResult = null;
//		String str = null;
//		try {
//			DefaultHttpClient client = new DefaultHttpClient();
//			// 发送get请求
//			HttpGet request = new HttpGet(url);
//			HttpResponse response = client.execute(request);
//
//			/** 请求发送成功，并得到响应 **/
//			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				/** 读取服务器返回过来的json字符串数据 **/
//				str = EntityUtils.toString(response.getEntity());
//				url = URLDecoder.decode(url, "UTF-8");
//			} else {
//				logger.error("get请求提交失败:" + url);
//			}
//		} catch (IOException e) {
//			logger.error("get请求提交失败:" + url, e);
//		}
//		return str;
//	}
    public static void main(String[] args) {
//		System.out.println(httpGet(
//				"http://sms.253.com/msg/send?un=N9094140&pw=inTd7kmfp0eb87&phone=18312690206&msg=【学为教育】欢迎体验253云通讯产品验证码是253253&rd=1"));
    }

}
