package com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.enums;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Slf4j
public class BsgoalSignatureCompute {
    public static void main(String[] args) {
        //忽略大小写按Key排序
        Comparator<String> comparator = Comparator.comparing(String::toLowerCase);
        SortedMap<String, String> map = new TreeMap<>(comparator);
        map.put("ChargeType", "1");
        map.put("VisitToPerson", "11");
        map.put("BeginTime", "2020-04-16 09:00:00");
        map.put("GateIDs", "1");
        map.put("Visitor", "1");
        map.put("BusinessCode", "AppointmentAdd_001");
        map.put("VisitorMobile", "11");
        map.put("PlateNumber", "粤A88888213");
        map.put("EndTime", "2020-04-16 19:00:00");
        map.put("Remark", "11");
        map.put("AppointTime", "2020-04-16 19:00:00");
        map.put("SignType", "SHA1");
        //博思高提供的ParkingID
        map.put("ParkingID", "4613901876730916727");
        //博思高提供的CompanyID
        map.put("CompanyID", "5713831726460572114");
        //博思高提供的AccessCode
        map.put("AccessCode", "50202212-84f0-11e8-8d22-00163e0c01c9");
        //博思高提供的Secretkey
        map.put("Secretkey", "50402239-84f0-11e8-8d22-00163e0c01c9");
        //获取签名结果6a7e65910278e976357e6296df371b6ae99d9874
        String signResult = Signature(map);
        System.out.println(signResult);
    }

    public static String Signature(SortedMap<String, String> paramMap) {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, String> stringStringEntry : paramMap.entrySet()) {
            String key = stringStringEntry.getKey();
            String value = stringStringEntry.getValue();
            list.add(key + "=" + value);
        }
        String arrString = String.join("&", list);
        log.debug("arrString:{}", arrString);
        String sign = "";
        try {
            sign = shaEncode(arrString);
        } catch (Exception e) {
            sign = "";
        }
        return sign;
    }

    private static String shaEncode(String inStr) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            log.error("shaEncode error", e);
            return "";
        }
        byte[] byteArray = inStr.getBytes(StandardCharsets.UTF_8);
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString().replace("-", "").toLowerCase();
    }
}
