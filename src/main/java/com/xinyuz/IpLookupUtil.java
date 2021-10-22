package com.xinyuz;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import net.ipip.ipdb.City;
import net.ipip.ipdb.IPFormatException;

/**
 * 根据IP查询归属地
 * @author zhouandr
 *
 */
public class IpLookupUtil {

    private static City ipdbCity;

    /**
     * 通过IP查询城市
     * 
     * @param ip (IPv4或者 IPv6)
     * @param language
     * @return 例如[中国,广东,广州]
     */
    public static String[] lookupIp(String ip, String language) {
        try {
            if (null == ipdbCity) {
//                ipdbCity = new City(new IpLookupUtil().getClass().getResource("/").getPath() + "qqwry.ipdb");
                
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                InputStream is          = classloader.getResourceAsStream("qqwry.ipdb");
                ipdbCity = new City(is);
            }
            return ipdbCity.find(ip, language);
        } catch (IOException | IPFormatException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(IpLookupUtil.lookupIp("58.62.28.25", "CN")));
        System.out.println(Arrays.toString(IpLookupUtil.lookupIp("27.0.3.155", "CN")));
        System.out.println(Arrays.toString(IpLookupUtil.lookupIp("169.254.79.129", "CN")));
    }

}