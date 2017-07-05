package com.nat.android.javashoplib.weight.yms_pickerview;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import com.nat.android.javashoplib.R;
import com.nat.android.javashoplib.utils.Utils;

/**
 * Created by LDD on 17/3/3.
 */

public class YmsPickView<T extends BasePickerModel> {
    private Context context;
    private Button  cancel;
    private Button  confirm;
    private ImageView back;
    private Dialog dialog;
    private TextView hint;
    private ArrayList<T> data1;
    private ArrayList<T> data2;
    private ArrayList<T> data3;
    private ArrayList<T> data4;
    private PickAdapter adapter;
    private YmsDialogListener<T> listener;
    private T  bean1,bean2,bean3,bean4;
    private String text1 = "",text2 = "",text3 = "",text4 ="";

    /**
     * YmsPickView构造方法！
     * @param context   上下文
     * @param data      数据
     */
    public YmsPickView(Context context, ArrayList<T> data) {
        this.context = context;
        this.data1 = data;
        init();
    }
    public void setYmsListener(YmsDialogListener<T> listener){
        this.listener = listener;
    }
    public void setCancelStyle(int Rid){
        cancel.setBackground(context.getDrawable(Rid));
    }
    public void setConfrim(int Rid){
        confirm.setBackground(context.getDrawable(Rid));
    }
    public void setBackImage(int Rid){
        back.setImageResource(Rid);
    }
    public void setCancelText(String text){
        cancel.setText(text);
    }
    public void setConfirm(String text){
        confirm.setText(text);
    }
    public void setData(ArrayList<T> data){
        if (data!=null&&data.size()>0&&adapter!=null){
            @PickerType.Type int type =data.get(0).getType();
            if (type==PickerType.First){
                data1=data;
                adapter.setData(data1);
            }
            if (type==PickerType.Second){
                data2=data;
                adapter.setData(data2);
            }
            if (type==PickerType.Third){
                data3=data;
                adapter.setData(data3);
            }
            if (type==PickerType.Fourth){
                data4=data;
                adapter.setData(data4);
            }
        }
    }

    private void init(){
        adapter = new PickAdapter(context,data1);
        dialog = new Dialog(context,R.style.Dialog);
        View view =  LayoutInflater.from(context).inflate(R.layout.city_choice,null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.nicai);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.width = (int) (Utils.getScreenWidth(context)/1.5);
        layoutParams.height = (int) (Utils.getScreenHeight(context)/1.5);
        linearLayout.setLayoutParams(layoutParams);
        dialog.setContentView(view);
        hint = (TextView) view.findViewById(R.id.hint);
        cancel = (Button) view.findViewById(R.id.cancel);
        confirm = (Button) view.findViewById(R.id.confirm);
        back = (ImageView) view.findViewById(R.id.back);
        final ListView listView = (ListView) view.findViewById(R.id.citylist);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        adapter = new PickAdapter(context,data1);
        listView.setAdapter(adapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.getResult(getResult());
                }
                dialog.dismiss();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initHint((T) adapter.getItem(position),adapter.getItem(position).getPickerName());
                setResult((T) adapter.getItem(position));
                if (listener!=null){
                    listener.setPickData((T) adapter.getItem(position));
                }
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
    }


    private void initHint(T bean,String value){
        if (bean.getType() == PickerType.First){
            text1 = value;
            text2 = "";
            text3 = "";
            text4 = "";
            bean1 = bean;
            bean2 = null;
            bean3 = null;
            bean4 = null;
        }
        if (bean.getType() == PickerType.Second){
            text2 = value;
            text3 = "";
            text4 = "";
            bean2 = bean;
            bean3 = null;
            bean4 = null;
        }
        if (bean.getType() == PickerType.Third){
            text3 = value;
            text4 = "";
            bean3 = bean;
            bean4 = null;
        }
        if (bean.getType() == PickerType.Fourth){
            text4 = value;
            bean4 = bean;
        }
        hint.setText(text1+" "+text2+" "+text3+" "+text4);
    }

    private void back(){
       @PickerType.Type int type = adapter.getItem(0).getType();
        if (type == PickerType.First){
            return;
        }
        if (type == PickerType.Second){
            adapter.setData(data1);
        }
        if (type == PickerType.Third){
            adapter.setData(data2);
        }
        if (type == PickerType.Fourth){
            adapter.setData(data3);
        }
    }

    private void setResult(T result){
        @PickerType.Type int pickerType = result.getType();
        if (pickerType == PickerType.First){
            bean1 = result;
            return;
        }
        if (pickerType == PickerType.Second){
            bean2 = result;
            return;
        }
        if (pickerType == PickerType.Third){
            bean3 = result;
            return;
        }
        if (pickerType == PickerType.Fourth){
            bean4 = result;
            return;
        }else{
            Utils.ToastS("YmsPickerView配置出错，请检查参数！");
        }
    }
    private ArrayList<T> getResult(){
        ArrayList<T> data = new ArrayList<>();
        if (bean1!=null){
            data.add(bean1);
        }
        if (bean2!=null){
            data.add(bean2);
        }
        if (bean3!=null){
            data.add(bean3);
        }
        if (bean4!=null){
            data.add(bean4);
        }
        return data;
    }

    public void show(){
        if (!(dialog.isShowing())&&data1!=null&&adapter!=null){
            bean1 =null;
            bean2 = null;
            bean3=null;
            bean4=null;
            text1="";
            text2="";
            text3 ="";
            text4 ="";
            hint.setText("");
            data2=null;
            data3=null;
            data4=null;
            adapter.setData(data1);
            dialog.show();
        }else{
            Utils.ToastS("YmsPickerView配置出错，请检查参数！");
        }
    }

    public interface YmsDialogListener<T extends BasePickerModel>{
            void setPickData(T data);
            void getResult(ArrayList<T> result);
    }
}
