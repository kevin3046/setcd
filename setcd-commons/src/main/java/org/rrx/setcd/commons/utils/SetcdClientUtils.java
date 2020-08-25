package org.rrx.setcd.commons.utils;

import org.rrx.setcd.commons.clients.SetcdClients;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/8/23 14:28
 * @Description:
 */
public class SetcdClientUtils {


    public static String getValue(String key) {
        try {
            return SetcdClients.getInstance().getValue(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Long putValue(String key, String value) {
        try {
            return SetcdClients.getInstance().putValue(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
