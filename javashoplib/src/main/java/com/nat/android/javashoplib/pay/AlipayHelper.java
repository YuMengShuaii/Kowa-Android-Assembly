package com.nat.android.javashoplib.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.nat.android.javashoplib.encryption.DesUtils;
import com.nat.android.javashoplib.init.FrameInit;
import com.nat.android.javashoplib.pay.payutils.PayOrderControl;
import com.nat.android.javashoplib.pay.payutils.PayResult;
import com.nat.android.javashoplib.pay.payutils.PaymentActControl;
import com.nat.android.javashoplib.pay.payutils.PaymentModelContorl;
import com.nat.android.javashoplib.pay.payutils.SignUtils;

/**
 * Created by Dawei on 10/8/16.
 */

public class AlipayHelper {

    /**
     * 商户PID
     */
    private String partner = "";

    /**
     * 商户收款账号
     */
    private String seller = "";

    /**
     * 商户私钥，pkcs8格式
     */
    private String rsa_private = "";

    private PayOrderControl order;

    private PaymentActControl paymentActivity;

    private PaymentModelContorl payment;

    private static AlipayHelper instance;
    /**
     * 支付宝支付回调处理
     */
    private Handler alipayHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((String) msg.obj);

            // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
            String resultInfo = payResult.getResult();

            String resultStatus = payResult.getResultStatus();

            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                paymentActivity.paymentCallback(1, "订单支付成功！");
            } else {
                // 判断resultStatus 为非“9000”则代表可能支付失败
                // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    paymentActivity.paymentCallback(0, "支付结果确认中！");
                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    paymentActivity.paymentCallback(0, "支付失败，请您重试！");
                }
            }
        }
    };


    public static AlipayHelper init(PaymentActControl paymentActivity, PayOrderControl order, PaymentModelContorl payment){
        if (instance==null){
            instance = new AlipayHelper();
        }
        instance.config(paymentActivity,order,payment);
        return instance;
    }

    private AlipayHelper() {
    }

    private void config(PaymentActControl paymentActivity, PayOrderControl order, PaymentModelContorl payment) {
        this.paymentActivity = paymentActivity;
        this.order = order;
        this.payment = payment;

        //解析PayCfg中的参数
        JSONObject paramObject = null;
                try {
                    paramObject = new JSONObject(DesUtils.decode(payment.getConfig()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            partner = paramObject.optString("partner");
            seller = paramObject.optString("seller_email");
            rsa_private = paramObject.optString("rsa_private");
            Log.e("asd",partner+" 111  "+seller+" 222  "+rsa_private);

        if (paramObject == null)
            return;


    }

    /**
     * 发起支付
     */
    public void pay(){
        final String payInfo = createPaymentString();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(paymentActivity.getActivity());
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                System.out.println(result);

                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                alipayHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 生成发起支付的信息字符串
     * @return
     */
    private String createPaymentString() {
        // 订单
        String orderInfo = createOrderInfo();

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        return orderInfo + "&sign=\"" + sign + "\"&"
                + "sign_type=\"RSA\"";
    }

    /**
     * 创建订单信息
     */
    private String createOrderInfo() {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + partner + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + seller + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + order.getSn() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"网店订单：" + order.getSn() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"订单：" + order.getSn() + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + String.format("%.2f", order.getNeedPayMoney()) + "\"";
//        orderInfo += "&total_fee=" + "\"0.01\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + FrameInit.getPayUrl() + "/api/shop/b_alipayMobilePlugin_payment-callback/execute.do"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        return orderInfo;
    }

    /**
     * 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, rsa_private);
    }

}
