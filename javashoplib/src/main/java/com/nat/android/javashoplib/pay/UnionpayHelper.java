package com.nat.android.javashoplib.pay;

import com.ta.utdid2.android.utils.StringUtils;
import com.unionpay.UPPayAssistEx;
import com.nat.android.javashoplib.pay.payutils.PayOrderControl;
import com.nat.android.javashoplib.pay.payutils.PaymentActControl;
import com.nat.android.javashoplib.pay.payutils.PaymentModelContorl;

/**
 * Created by Dawei on 10/8/16.
 */

public class UnionpayHelper {

    private static final String SERVER_MODE = "00";

    private PayOrderControl order;

    private PaymentActControl paymentActivity;

    private PaymentModelContorl payment;

    private static UnionpayHelper instance;

    public static UnionpayHelper init(PaymentActControl paymentActivity, PayOrderControl order, PaymentModelContorl payment){
        if (instance==null){
            instance = new UnionpayHelper();
        }
        instance.config(paymentActivity,order,payment);
        return instance;
    }

    private UnionpayHelper() {
    }

    private void config(PaymentActControl paymentActivity, PayOrderControl order, PaymentModelContorl payment) {
        this.paymentActivity = paymentActivity;
        this.order = order;
        this.payment = payment;
    }

    /**
     * 发起支付
     */
    public void pay(String tn){
        if(StringUtils.isEmpty(tn)){
            paymentActivity.paymentCallback(0, "支付失败，请您重试！");
            return;
        }
        UPPayAssistEx.startPay(paymentActivity.getActivity(), null, null, tn, SERVER_MODE);
    }
}
