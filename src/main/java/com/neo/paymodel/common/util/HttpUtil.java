package com.neo.paymodel.common.util;

import net.sf.json.JSONObject;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

public final class HttpUtil {

    public static String getSerchPersion(String url, String param) {
        /* 1 生成 HttpClinet 对象并设置参数 */
        HttpClient httpClient = new HttpClient();
        // 设置 Http 连接超时为秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        /* 2 生成 GetMethod 对象并设置参数 */
        GetMethod getMethod = new GetMethod(url);
        // 设置 get 请求超时为  5秒
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        // 设置请求重试处理，用的是默认的重试处理：请求三次
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        String response = "";
        /* 3 执行 HTTP GET 请求 */
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            /* 4 判断访问的状态码 */
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("请求出错: " + getMethod.getStatusLine());
            }
            /* 5 处理 HTTP 响应内容 */
            // HTTP响应头部信息，这里简单打印
            Header[] headers = getMethod.getResponseHeaders();
            for (Header h : headers)
                System.out.println(h.getName() + "------------ " + h.getValue());
            // 读取 HTTP 响应内容，这里简单打印网页内容
            byte[] responseBody = getMethod.getResponseBody();// 读取为字节数组
            response = new String(responseBody, param);
            System.out.println("----------response:" + response);
            // 读取为 InputStream，在网页内容数据量大时候推荐使用
            // InputStream response = getMethod.getResponseBodyAsStream();
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("请检查输入的URL!");
            e.printStackTrace();
        } catch (IOException e) {
            // 发生网络异常
            System.out.println("发生网络异常!");
            e.printStackTrace();
        } finally {
            /* 6 .释放连接 */
            getMethod.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
        return response;
    }

    /**
     * post请求
     *
     * @param url
     * @param json
     * @return
     */
    public static JSONObject doPost(String url, JSONObject json) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        JSONObject response = null;
        try {
            StringEntity s = new StringEntity(json.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                response = JSONObject.fromObject(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    static String UNKNOWN_IP = "unknown";

    public static String getRealIp(String ip) {
        //
        if (ip.indexOf(",") != -1) {
            return StringUtils.left(ip.split(",")[0], 15);

        }
        return ip;
    }

    public static boolean isRealIP(String ip) {
        return StringUtils.isNotBlank(ip) && !UNKNOWN_IP.equalsIgnoreCase(ip);
    }

    public static String getIpAddr(HttpServletRequest request) {
        //
        String ip = null;
        //
        ip = request.getHeader("X-Forwarded-For");
        if (isRealIP(ip)) {
            return getRealIp(ip);
        }
        //
        ip = request.getHeader("Proxy-Client-IP");
        if (isRealIP(ip)) {
            return getRealIp(ip);
        }
        //
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isRealIP(ip)) {
            return getRealIp(ip);
        }
        //
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isRealIP(ip)) {
            return getRealIp(ip);
        }
        //
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isRealIP(ip)) {
            return getRealIp(ip);
        }
        //
        ip = request.getParameter("__fromReferIP");
        if (isRealIP(ip)) {
            return getRealIp(ip);
        }
        // NGINX用
        ip = request.getHeader("X-Real-IP");
        if (isRealIP(ip)) {
            return getRealIp(ip);
        }
        ip = request.getRemoteAddr();
        //
        return ip;
    }

    /**
     * Wap网关Via头信息中特有的描述信息
     */
    private static String mobileGateWayHeaders[] = new String[]{"ZXWAP",// 中兴提供的wap网关的via信息，例如：Via=ZXWAP
            // GateWayZTE
            // Technologies，
            "chinamobile.com",// 中国移动的诺基亚wap网关，例如：Via=WTP/1.1
            // GDSZ-PB-GW003-WAP07.gd.chinamobile.com (Nokia
            // WAP Gateway 4.1 CD1/ECD13_D/4.1.04)
            "monternet.com",// 移动梦网的网关，例如：Via=WTP/1.1
            // BJBJ-PS-WAP1-GW08.bj1.monternet.com. (Nokia WAP
            // Gateway 4.1 CD1/ECD13_E/4.1.05)
            "infoX",// 华为提供的wap网关，例如：Via=HTTP/1.1 GDGZ-PS-GW011-WAP2 (infoX-WISG
            // Huawei Technologies)，或Via=infoX WAP Gateway V300R001
            // Huawei Technologies
            "XMS 724Solutions HTG",// 国外电信运营商的wap网关，不知道是哪一家
            "wap.lizongbo.com",// 自己测试时模拟的头信息
            "Bytemobile"// 貌似是一个给移动互联网提供解决方案提高网络运行效率的，例如：Via=1.1 Bytemobile OSN
            // WebProxy/5.1
    };
    /**
     * 手机浏览器的User-Agent里的关键词
     */
    private static String[] mobileUserAgents = new String[]{"Nokia",// 诺基亚，有山寨机也写这个的，总还算是手机，Mozilla/5.0
            // (Nokia5800
            // XpressMusic)UC
            // AppleWebkit(like
            // Gecko)
            // Safari/530
            "SAMSUNG",// 三星手机
            // SAMSUNG-GT-B7722/1.0+SHP/VPP/R5+Dolfin/1.5+Nextreaming+SMM-MMS/1.2.0+profile/MIDP-2.1+configuration/CLDC-1.1
            "MIDP-2",// j2me2.0，Mozilla/5.0 (SymbianOS/9.3; U; Series60/3.2
            // NokiaE75-1 /110.48.125 Profile/MIDP-2.1
            // Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML like
            // Gecko) Safari/413
            "CLDC1.1",// M600/MIDP2.0/CLDC1.1/Screen-240X320
            "SymbianOS",// 塞班系统的，
            "MAUI",// MTK山寨机默认ua
            "UNTRUSTED/1.0",// 疑似山寨机的ua，基本可以确定还是手机
            "Windows CE",// Windows CE，Mozilla/4.0 (compatible; MSIE 6.0;
            // Windows CE; IEMobile 7.11)
            "iPhone",// iPhone是否也转wap？不管它，先区分出来再说。Mozilla/5.0 (iPhone; U; CPU
            // iPhone OS 4_1 like Mac OS X; zh-cn) AppleWebKit/532.9
            // (KHTML like Gecko) Mobile/8B117
            "iPad",// iPad的ua，Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X;
            // zh-cn) AppleWebKit/531.21.10 (KHTML like Gecko)
            // Version/4.0.4 Mobile/7B367 Safari/531.21.10
            "Android",// Android是否也转wap？Mozilla/5.0 (Linux; U; Android
            // 2.1-update1; zh-cn; XT800 Build/TITA_M2_16.22.7)
            // AppleWebKit/530.17 (KHTML like Gecko) Version/4.0
            // Mobile Safari/530.17
            "BlackBerry",// BlackBerry8310/2.7.0.106-4.5.0.182
            "UCWEB",// ucweb是否只给wap页面？ Nokia5800
            // XpressMusic/UCWEB7.5.0.66/50/999
            "ucweb",// 小写的ucweb貌似是uc的代理服务器Mozilla/6.0 (compatible; MSIE 6.0;)
            // Opera ucweb-squid
            "BREW",// 很奇怪的ua，例如：REW-Applet/0x20068888 (BREW/3.1.5.20; DeviceId:
            // 40105; Lang: zhcn) ucweb-squid
            "J2ME",// 很奇怪的ua，只有J2ME四个字母
            "YULONG",// 宇龙手机，YULONG-CoolpadN68/10.14 IPANEL/2.0 CTC/1.0
            "YuLong",// 还是宇龙
            "COOLPAD",// 宇龙酷派YL-COOLPADS100/08.10.S100 POLARIS/2.9 CTC/1.0
            "TIANYU",// 天语手机TIANYU-KTOUCH/V209/MIDP2.0/CLDC1.1/Screen-240X320
            "TY-",// 天语，TY-F6229/701116_6215_V0230 JUPITOR/2.2 CTC/1.0
            "K-Touch",// 还是天语K-Touch_N2200_CMCC/TBG110022_1223_V0801 MTK/6223
            // Release/30.07.2008 Browser/WAP2.0
            "Haier",// 海尔手机，Haier-HG-M217_CMCC/3.0 Release/12.1.2007
            // Browser/WAP2.0
            "DOPOD",// 多普达手机
            "Lenovo",// 联想手机，Lenovo-P650WG/S100 LMP/LML Release/2010.02.22
            // Profile/MIDP2.0 Configuration/CLDC1.1
            "LENOVO",// 联想手机，比如：LENOVO-P780/176A
            "HUAQIN",// 华勤手机
            "AIGO-",// 爱国者居然也出过手机，AIGO-800C/2.04 TMSS-BROWSER/1.0.0 CTC/1.0
            "CTC/1.0",// 含义不明
            "CTC/2.0",// 含义不明
            "CMCC",// 移动定制手机，K-Touch_N2200_CMCC/TBG110022_1223_V0801 MTK/6223
            // Release/30.07.2008 Browser/WAP2.0
            "DAXIAN",// 大显手机DAXIAN X180 UP.Browser/6.2.3.2(GUI) MMP/2.0
            "MOT-",// 摩托罗拉，MOT-MOTOROKRE6/1.0 LinuxOS/2.4.20 Release/8.4.2006
            // Browser/Opera8.00 Profile/MIDP2.0 Configuration/CLDC1.1
            // Software/R533_G_11.10.54R
            "SonyEricsson",// 索爱手机，SonyEricssonP990i/R100 Mozilla/4.0
            // (compatible; MSIE 6.0; Symbian OS; 405) Opera
            // 8.65 [zh-CN]
            "GIONEE",// 金立手机
            "HTC",// HTC手机
            "ZTE",// 中兴手机，ZTE-A211/P109A2V1.0.0/WAP2.0 Profile
            "HUAWEI",// 华为手机，
            "webOS",// palm手机，Mozilla/5.0 (webOS/1.4.5; U; zh-CN)
            // AppleWebKit/532.2 (KHTML like Gecko) Version/1.0
            // Safari/532.2 Pre/1.0
            "GoBrowser",// 3g GoBrowser.User-Agent=Nokia5230/GoBrowser/2.0.290
            // Safari
            "IEMobile",// Windows CE手机自带浏览器，
            "WAP2.0"// 支持wap 2.0的
    };
    /**
     * 电脑上的IE或Firefox浏览器等的User-Agent关键词
     */
    private static String[] pcHeaders = new String[]{"Windows 98",
            "Windows ME", "Windows 2000", "Windows XP", "Windows NT", "Ubuntu"};

    /**
     * 根据当前请求的特征，判断该请求是否来自手机终端，主要检测特殊的头信息，以及user-Agent这个header
     *
     * @param request http请求
     * @return 如果命中手机特征规则，则返回对应的特征字符串
     */
    public static boolean isMobileDevice(HttpServletRequest request) {
        boolean b = false;
        boolean pcFlag = false;
        boolean mobileFlag = false;
        String via = request.getHeader("Via");
        String userAgent = request.getHeader("user-agent");
        for (int i = 0; via != null && !via.trim().equals("")
                && i < mobileGateWayHeaders.length; i++) {
            if (via.contains(mobileGateWayHeaders[i])) {
                mobileFlag = true;
                break;
            }
        }
        for (int i = 0; !mobileFlag && userAgent != null
                && !userAgent.trim().equals("") && i < mobileUserAgents.length; i++) {
            if (userAgent.contains(mobileUserAgents[i])) {
                mobileFlag = true;
                break;
            }
        }
        for (int i = 0; userAgent != null && !userAgent.trim().equals("")
                && i < pcHeaders.length; i++) {
            if (userAgent.contains(pcHeaders[i])) {
                pcFlag = true;
                break;
            }
        }
        if (mobileFlag == true && pcFlag == false) {
            b = true;
        }
        return b;// false pc true shouji
    }

    public static String getMobileDeviceSystem(String user_agent) {//操作系统
        String temp = "";
        try {
            temp = user_agent.replaceAll(" ", "").toUpperCase();
            if (checkAgentIsIos(user_agent)) {
                temp = temp.substring(temp.indexOf("MOZILLA") + 1, temp.length());
                temp = temp.substring(temp.indexOf("(") + 1, temp.length());
                temp = temp.substring(0, temp.indexOf(")"));
                for (String str : userAgentReplaceArgs) {
                    temp = temp.replaceAll(str, "");
                }
                temp = temp.replaceAll(";;", ";");

                if (temp.split(";").length >= 2) {
                    temp = temp.split(";")[0];
                }
            } else {
                temp = temp.substring(temp.indexOf("MOZILLA") + 1, temp.length());
                temp = temp.substring(temp.indexOf("(") + 1, temp.length());
                temp = temp.substring(0, temp.indexOf(")"));
                for (String str : userAgentReplaceArgs) {
                    temp = temp.replaceAll(str, "");
                }
                temp = temp.replaceAll(";;", ";");

                if (temp.split(";").length > 2) {
                    temp = temp.split(";")[0] + ";" + temp.split(";")[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static String getMobileDeviceMachine(String user_agent) {//手机设备型号
        String temp = "";
        try {
            temp = user_agent.replaceAll(" ", "").toUpperCase();
            if (checkAgentIsIos(user_agent)) {
                temp = temp.substring(temp.indexOf("MOZILLA") + 1, temp.length());
                temp = temp.substring(temp.indexOf("(") + 1, temp.length());
                temp = temp.substring(0, temp.indexOf(")"));
                for (String str : userAgentReplaceArgs) {
                    temp = temp.replaceAll(str, "");
                }
                temp = temp.replaceAll(";;", ";");

                if (temp.split(";").length >= 2) {
                    temp = temp.split(";")[1];
                }
            } else {
                if (temp.startsWith("MOZILLA")) {
                    temp = temp.substring(temp.indexOf("(") + 1, temp.length());
                    temp = temp.substring(0, temp.indexOf(")"));
                    for (String str : userAgentReplaceArgs) {
                        temp = temp.replaceAll(str, "");
                    }
                    temp = temp.replaceAll(";;", ";");

                    if (temp.split(";").length >= 3) {
                        temp = temp.split(";")[2];
                    }
                } else {
                    temp = temp.substring(0, temp.indexOf("ANDROID"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static String getMobileDeviceMobile(String user_agent) {//MOBILE参数
        String temp = "";
        try {
            temp = user_agent.toUpperCase();

            if (checkAgentIsIos(user_agent)) {
                temp = temp.substring(temp.indexOf("MOBILE/") + 7, temp.length());
                if (temp.indexOf(" ") != -1) {
                    temp = temp.substring(0, temp.indexOf(" "));
                }
            } else {
                temp = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * 判断User-Agent 是不是来自于移动端-IOS
     *
     * @param ua
     * @return
     */
    public static boolean checkAgentIsIos(String ua) {
        boolean flag = false;
        if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
            // 排除 苹果桌面系统
            if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
                for (String item : BROWSE_USER_AGENT_IOS) {
                    if (ua.toUpperCase().contains(item)) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    public static void main(String[] args) {
        String user_agent = "Mozilla/5.0 (Linux; Android 5.1; vivo X6D Build/LMY47I) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Mobile Safari/537.36";
        System.out.println("System:  " + getMobileDeviceSystem(user_agent));
        System.out.println("Machine: " + getMobileDeviceMachine(user_agent));
        System.out.println("Mobile:  " + getMobileDeviceMobile(user_agent));
    }

    private static final String[] userAgentReplaceArgs = {"U;", ";WV", "ZH-CN", "EN-US"};
    private final static String[] BROWSE_USER_AGENT_IOS = {"IPHONE", "IPOD", "IPAD"};


    public static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static final String QSTRING_EQUAL = "=";
    private static final String QSTRING_SPLIT = "&";

    public static String buildOriginalURL(HttpServletRequest request) {
        StringBuffer originalURL = request.getRequestURL();
        String url = originalURL.toString();
        Map<String, String[]> parameters = request.getParameterMap();
        if (parameters != null && parameters.size() > 0) {
            originalURL.append("?");
            for (String key : parameters.keySet()) {
                String[] values = parameters.get(key);
                for (String value : values) {
                    originalURL.append(key).append("=").append(value).append("&");
                }
            }
            url = originalURL.substring(0, originalURL.length() - 1); // 去掉最后一个&
        }
        return url;
    }

    public static Map<String, Object> getParameterMap(HttpServletRequest request, boolean decode) throws Exception {
        // 参数Map
        Map<String, String[]> properties = request.getParameterMap();
        // 返回值Map
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Iterator<?> entries = properties.entrySet().iterator();
        Map.Entry<String, Object> entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry<String, Object>) entries.next();
            name = entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            if (decode) {
                name = URLDecoder.decode(name, "UTF-8");
                value = URLDecoder.decode(value, "UTF-8");
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

    /**
     * POST请求，Map形式数据
     *
     * @param url     请求地址
     * @param param   请求数据
     * @param charset 编码方式
     */
    public static String sendPost(String url, Map<String, Object> param,
                                  String charset) throws UnsupportedEncodingException {
        long start_time = System.currentTimeMillis();
        StringBuffer buffer = new StringBuffer();
        if (param != null && !param.isEmpty()) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                buffer.append(entry.getKey()).append("=").append(
                        URLEncoder.encode(entry.getValue().toString(), charset)).append("&");

            }
        }
        buffer.deleteCharAt(buffer.length() - 1);

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(buffer);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), charset));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (param != null && param.size() > 0) {
            url += ((url.contains("?") ? "&" : "?") + PayUtil.createLinkString(param, false, false));
        }
        logger.info("请求URL:{} 耗时:{}", url, System.currentTimeMillis() - start_time);
        return result;
    }

    /**
     * POST请求，Map形式数据
     *
     * @param url     请求地址
     * @param param   请求数据
     * @param charset 编码方式
     */
    public static String sendPost(String url, Map<String, Object> param,
                                  String charset, String type) throws UnsupportedEncodingException {
        long start_time = System.currentTimeMillis();
        StringBuffer buffer = new StringBuffer();
        if (param != null && !param.isEmpty()) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                buffer.append(entry.getKey()).append("=").append(
                        URLEncoder.encode(entry.getValue().toString(), charset)).append("&");

            }
        }
        buffer.deleteCharAt(buffer.length() - 1);

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            if (StringUtils.isNotBlank(type)) {
                conn.setRequestProperty("Content-Type", type);
            }
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(buffer);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), charset));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (param != null && param.size() > 0) {
            url += ((url.contains("?") ? "&" : "?") + PayUtil.createLinkString(param, false, false));
        }
        logger.info("请求URL:{} 耗时:{}", url, System.currentTimeMillis() - start_time);
        return result;
    }

    public static List<String[]> parameterPost(Map<String, Object> map) {
        // 返回值List
        List<String[]> list = new ArrayList<String[]>(2);
        Iterator<?> entries = map.entrySet().iterator();
        Map.Entry<String, Object> entry;
        String name[] = new String[map.size()];
        String value[] = new String[map.size()];
        for (int i = 0; i < map.size(); i++) {
            entry = (Map.Entry<String, Object>) entries.next();
            Object nameobj = entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value[i] = "";
            } else {

                value[i] = valueObj.toString();
            }
            name[i] = (String) nameobj;
        }
        list.add(0, name);
        list.add(1, value);
        return list;
    }

    public static String sendGet(String url, String charset, int timeOut) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setConnectTimeout(timeOut);
            connection.setReadTimeout(timeOut);
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String httpPostWithJSON(String url, String jsonData) throws Exception {

        HttpClient httpClient = new HttpClient();
        // 设置连接超时时间(单位毫秒)
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60 * 1000);
        // 设置读取超时时间(单位毫秒)
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(60 * 1000);
        PostMethod method = new PostMethod(url);
        String info = null;
        try {

            RequestEntity entity = new StringRequestEntity(jsonData, "application/json", "UTF-8");
            method.setRequestEntity(entity);
            httpClient.executeMethod(method);
            int code = method.getStatusCode();
            if (code == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = reader.readLine()) != null) {
                    stringBuffer.append(str);
                }
                info = stringBuffer.toString();
                logger.info("httpPostWithJSON 返回报文：" + info);
            } else {
                logger.error("httpPostWithJSON 接口返回失败  httpStatusCode=" + code);
            }

        } catch (Exception ex) {
            logger.error("内部接口报文发送异常:" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
        return info;
    }

    public static String readFileContent1(InputStream is) {

        // InputStream is = null;

        BufferedReader br = null;
        InputStreamReader isr = null;
        BufferedWriter bw = null;
        StringBuffer line = new StringBuffer();
        String str = null;
        // File file1 = new File("info2.txt");
        // OutputStreamWriter osw = null;
        try {
            // is = new FileInputStream(soureFile);
            isr = new InputStreamReader(is, "gb2312");
            FileOutputStream fos;
            // fos = new FileOutputStream(file1);
            // osw = new OutputStreamWriter(fos);
            br = new BufferedReader(isr);
            // bw = new BufferedWriter(osw);
            while ((str = br.readLine()) != null) {
                line.append(str);
                // System.out.println("str:" + str);
                line.append("\n");
            }
            // System.out.println("line:" + line);
            // System.out.println("line.toString():"+line.toString());
            // bw.write("我发沙敦府打算发是！");
            // bw.close();

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return line.toString();
    }

   /* *//**
     * 获取IP地址
     *
     *
     * @return
     *//*
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        //
        ip = request.getHeader("X-Forwarded-For");
        if (isRealIP(ip)) {
            return getRealIp(ip);
        }
        //
        ip = request.getHeader("Proxy-Client-IP");
        if (isRealIP(ip)) {
            return getRealIp(ip);
        }
        //
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isRealIP(ip)) {
            return getRealIp(ip);
        }
        //
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isRealIP(ip)) {
            return getRealIp(ip);
        }
        //
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isRealIP(ip)) {
            return getRealIp(ip);
        }
        //
        ip = request.getParameter("__fromReferIP");
        if (isRealIP(ip)) {
            return getRealIp(ip);
        }
        // NGINX用
        ip = request.getHeader("X-Real-IP");
        if (isRealIP(ip)) {
            return getRealIp(ip);
        }
        ip = request.getRemoteAddr();
        //
        return ip;
    }*/

    //tatic String UNKNOWN_IP = "unknown";

  /*  public static boolean isRealIP(String ip) {
        return StringUtils.isNotBlank(ip) && !UNKNOWN_IP.equalsIgnoreCase(ip);
    }*/

   /* public static String getRealIp(String ip) {
        //
        if (ip.indexOf(",") != -1) {
            return StringUtils.left(ip.split(",")[0], 15);

        }
        return ip;
    }*/

    public static String getBodyString(BufferedReader br) {
        String inputLine;
        String str = "";
        try {
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return str;
    }

    static void setHttpProxy(HttpClient httpClient) {


    }
}
