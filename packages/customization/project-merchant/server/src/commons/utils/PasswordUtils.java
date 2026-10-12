package com.guhecloud.cube.deliver.project.merchant.commons.utils;

import java.util.Random;

public class PasswordUtils {


    public static String getRandomPassword(int len) {
        int i; //⽣成的随机数
        int count = 0; //⽣成的密码的长度
        // 密码字典
        char[] str = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                '?', '!', '@', '#', '$', '%', '-', '+'
        };
        StringBuilder stringBuffer = new StringBuilder("");
        Random r = new Random();
        while (count < len) {
            //⽣成 0 ~ 密码字典-1之间的随机数
            i = r.nextInt(str.length);
            stringBuffer.append(str[i]);
            count++;
        }
        return stringBuffer.toString();
    }

}
