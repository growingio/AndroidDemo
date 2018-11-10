package com.example.growingio.lib_codebag;

import android.app.Application;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * author WangYing
 * time   2018/04/2018/4/24:下午8:31
 * email  wangying@growingio.com
 */
public class ApplicationTools {

    public static Application getApplication() {
        Application application = null;
        Class<?> activityThreadClass;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            Field appField = activityThreadClass
                    .getDeclaredField("mInitialApplication");
            // Object object = activityThreadClass.newInstance();
            final Method method = activityThreadClass.getMethod(
                    "currentActivityThread", new Class[0]);
            // 得到当前的ActivityThread对象
            Object localObject = method.invoke(null, (Object[]) null);
            appField.setAccessible(true);
            application = (Application) appField.get(localObject);
            // appField.
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return application;
    }
}
