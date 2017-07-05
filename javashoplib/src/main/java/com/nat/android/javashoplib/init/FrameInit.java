package com.nat.android.javashoplib.init;

import android.content.Context;

import com.nat.android.javashoplib.pay.AlipayHelper;

/**
 * Created by LDD on 17/2/8.
 */

public class FrameInit {
     public static final boolean LOG=true;
     public static Context Mcontext;
     private static boolean isOpenScroll = true;
     private static String PayUrl;
     private static  String HomeActivity ="";
     public static void init(Context context){Mcontext = context.getApplicationContext();
     }
     public static void initPay(String url){
          PayUrl = url;
     }

     public static String getPayUrl() {
          return PayUrl;
     }

     public static boolean isOpenScroll() {
          return isOpenScroll;
     }

     public static void setIsOpenScroll(boolean isOpenScroll) {
          FrameInit.isOpenScroll = isOpenScroll;
     }

     public static String getHomeActivity() {
          return HomeActivity;
     }

     public static void setHomeActivity(String homeActivity) {
          HomeActivity = homeActivity;
     }
}
