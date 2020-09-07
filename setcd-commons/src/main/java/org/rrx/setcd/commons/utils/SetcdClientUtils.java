package org.rrx.setcd.commons.utils;

import org.rrx.setcd.commons.clients.GetOptionBuilder;
import org.rrx.setcd.commons.clients.KeyValueBean;
import org.rrx.setcd.commons.clients.SetcdClients;
import org.rrx.setcd.commons.election.ElectionCandidate;

import java.util.List;

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

    public static List<KeyValueBean> getValue(String key, GetOptionBuilder builder) {
        try {
            return SetcdClients.getInstance().getValue(key,builder);
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

    public static Long deleteValue(String key) {
        try {
            return SetcdClients.getInstance().delValue(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void election(String key,Long lockTTl,ElectionCandidate electionCandidate) {
        try {
            SetcdClients.getInstance().election(key,lockTTl,electionCandidate);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

//    public static void election2() {
//        try {
//            SetcdClients.getInstance().election2();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
}
