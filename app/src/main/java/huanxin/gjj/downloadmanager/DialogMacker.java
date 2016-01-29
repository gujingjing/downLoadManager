package huanxin.gjj.downloadmanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

/**
 * 作者：gjj on 2016/1/29 11:21
 * 邮箱：Gujj512@163.com
 */
public class DialogMacker {
    public static Dialog dialog;
    public  static ProgressWheel progressWheel;
    private static View view;
    private static DialogMacker dialogMacker;
    private static Context context;
    public static  DialogMacker getInstance(Context contexts){
        context=contexts;
        dialog = new Dialog(context, R.style.alertDialogView);
        view=View.inflate(context,R.layout.progress_view,null);
        progressWheel= (ProgressWheel) view.findViewById(R.id.progress_bar_two);
        if(dialogMacker==null){
            dialogMacker=new DialogMacker();
        }
        dialog.setContentView(view);
//        方法一：
//        setCanceledOnTouchOutside(false);调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
//        方法二：
//        setCanceleable(false);调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用
        dialog.setCanceledOnTouchOutside(false);
        return dialogMacker;
    }
    public   Dialog showDialog(){
        dialog.show();
        progressWheel.resetCount();
        return dialog;
    }
    public   DialogMacker setProgress(int progress){
        progressWheel.setProgressText(progress);
        return dialogMacker;
    }
    public void dismiss(){
        dialog.dismiss();
        progressWheel=null;
        view=null;
        context=null;
        dialog=null;
    }
}
