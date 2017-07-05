package com.nat.android.javashoplib.pay;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Xml;
import com.ta.utdid2.android.utils.StringUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.nat.android.javashoplib.encryption.DesUtils;
import com.nat.android.javashoplib.init.FrameInit;
import com.nat.android.javashoplib.pay.payutils.MD5;
import com.nat.android.javashoplib.pay.payutils.PayOrderControl;
import com.nat.android.javashoplib.pay.payutils.PaymentActControl;
import com.nat.android.javashoplib.pay.payutils.PaymentModelContorl;
import com.nat.android.javashoplib.pay.payutils.Util;
import com.nat.android.javashoplib.utils.CurrencyUtils;

import static android.content.ContentValues.TAG;

public class WechatHelper {

    public static final String SHARED_PREFERENCE_NAME = "WechatPay";

    /**
     * appid
     */
    private String appId = "";

    /**
     * 商户号
     */
    private String mchId = "";

    /**
     * API密钥，在商户平台设置
     */
    private String apiKey = "";

    private IWXAPI msgApi = null;

    private PayOrderControl order;

    private PaymentModelContorl payment;

    private PaymentActControl paymentActivity;

    private static  WechatHelper instance;

    //处理支付
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //生成预订单
            if(msg.what == 1) {
                Map<String, String> unifiedorder = (Map<String, String>) msg.obj;
                if(unifiedorder == null || !unifiedorder.containsKey("prepay_id") || StringUtils.isEmpty(unifiedorder.get("prepay_id"))){
                    paymentActivity.paymentCallback(0, "支付失败,请您重试或者换另外一种支付方式!");
                    return;
                }

                String prepayId = unifiedorder.get("prepay_id");
                SharedPreferences.Editor localEditor = paymentActivity.getActivity().getSharedPreferences(SHARED_PREFERENCE_NAME, 0).edit();
                localEditor.putString(order.getSn(), prepayId);
                localEditor.commit();

                PayReq payReq = createPayReq(prepayId);
                msgApi.registerApp(appId);
                msgApi.sendReq(payReq);

            }
            super.handleMessage(msg);
        }
    };

    private WechatHelper() {
    }

    public static WechatHelper init(PaymentActControl paymentActivity, PayOrderControl order, PaymentModelContorl  payment){
        if (instance==null){
            instance = new WechatHelper();
        }
        instance.config(paymentActivity,order,payment);

        return instance;
    }

    private void config(PaymentActControl paymentActivity, PayOrderControl order, PaymentModelContorl  payment) {
        this.paymentActivity = paymentActivity;
        this.order = order;
        this.payment = payment;

        msgApi = WXAPIFactory.createWXAPI(paymentActivity.getActivity(), null);

        //解析PayCfg中的参数
        JSONObject paramObject = null;
                try {
                    paramObject = new JSONObject(DesUtils.decode(payment.getConfig()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
        if (paramObject == null)
            return;
        appId = paramObject.optString("appid");
        mchId = paramObject.optString("mchid");
        apiKey = paramObject.optString("key");
    }

    /**
     * 发起微信支付
     */
    public void pay(){
        //是否已经生在过预订单
        SharedPreferences sharedPreferences = paymentActivity.getActivity().getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        if(sharedPreferences.contains(order.getSn())){
            PayReq payReq = createPayReq(sharedPreferences.getString(order.getSn(), ""));
            msgApi.registerApp(appId);
            msgApi.sendReq(payReq);
            return;
        }

        //生成prepay_id进行支付
        new Thread() {
            @Override
            public void run() {

                String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
                String entity = createProductArgs();

                Log.e("orion",entity);

                byte[] buf = Util.httpPost(url, entity);

                String content = new String(buf);
                Log.e("orion", content);
                Map<String,String> unifiedorder = decodeXml(content);

                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = unifiedorder;
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 生成PayReq对象
     *
     * @param prepayId 预订单id
     * @return
     */
    private PayReq createPayReq(String prepayId) {
        PayReq payReq = new PayReq();
        payReq.appId = appId;
        payReq.partnerId = mchId;
        payReq.prepayId = prepayId;
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = genNonceStr();
        payReq.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", payReq.appId));
        signParams.add(new BasicNameValuePair("noncestr", payReq.nonceStr));
        signParams.add(new BasicNameValuePair("package", payReq.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", payReq.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", payReq.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", payReq.timeStamp));

        payReq.sign = genAppSign(signParams);

        System.out.println("sign\n" + payReq.sign + "\n\n");
        Log.e("orion", signParams.toString());

        return payReq;
    }

    /**
     * 生成预订单参数
     *
     * @return
     */
    private String createProductArgs() {
        StringBuffer xml = new StringBuffer();
        try {
            String nonceStr = genNonceStr();
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", appId));
            packageParams.add(new BasicNameValuePair("body", "" + order.getSn()));
            packageParams.add(new BasicNameValuePair("mch_id", mchId));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", FrameInit.getPayUrl() + "/api/shop/b_wechatMobilePlugin_payment-callback/execute.do"));
            packageParams.add(new BasicNameValuePair("out_trade_no", order.getSn()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", toFen(order.getNeedPayMoney())));
//            packageParams.add(new BasicNameValuePair("total_fee", "1"));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));
            String xmlstring = toXml(packageParams);
            return xmlstring;
        } catch (Exception e) {
            Log.e(TAG, "genProductArgs fail," + e.getMessage());
            return null;
        }
    }

    /**
     * 生成随机字串
     *
     * @return
     */
    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 签名
     *
     * @param params
     * @return
     */
    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(apiKey);

        System.out.println("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");


            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e("orion", sb.toString());
        return sb.toString();
    }

    private Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;

    }

    /**
     * 生成签名
     */
    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(apiKey);

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", packageSign);
        return packageSign;
    }

    private String toFen(Double money) {
        money = CurrencyUtils.mul(money, 100);
        String str = String.valueOf(money);
        str = str.substring(0, str.indexOf("."));
        return str;
    }

}
