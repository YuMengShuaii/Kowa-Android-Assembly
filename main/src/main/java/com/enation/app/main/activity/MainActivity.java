package com.enation.app.main.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.enation.app.main.R;
import com.enation.app.main.update.Updater;
import com.nat.android.javashoplib.utils.Router;
import com.nat.android.javashoplib.utils.RxPermissions;

import rx.Subscriber;
public class MainActivity extends AppCompatActivity{
    private  MainActivity activity;
    private  final  String HomeLocation = "com.enation.android.firstbundle.activity.FirstActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.init(activity,HomeLocation).go();
            }
        });
        findViewById(R.id.rebushu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RxPermissions(activity)
                .request(Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean){
                            Updater.update(getBaseContext(), new Updater.UpdataListener() {
                                @Override
                                public void success() {

                                }

                                @Override
                                public void failed() {

                                }
                            });
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
}
