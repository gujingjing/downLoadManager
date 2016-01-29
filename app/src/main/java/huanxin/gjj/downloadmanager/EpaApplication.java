package huanxin.gjj.downloadmanager;

import android.app.Application;

/**
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉 全局application
 *
 * @author 14052012
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class EpaApplication extends Application {

    static EpaApplication epp;

    @Override
    public void onCreate() {
        super.onCreate();
        epp = this;
    }

    public static EpaApplication getEpaApplication() {
        return epp;
    }
}
