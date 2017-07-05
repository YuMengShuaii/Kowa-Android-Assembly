package com.nat.android.javashoplib.pay.payutils;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by LDD on 17/5/4.
 */

public interface PaymentActControl  {
    void paymentCallback(int code,String message);
    Activity getActivity();
}
