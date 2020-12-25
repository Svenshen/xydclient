/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @autoor szh
 */

package com.hzs.xydclient.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author  szh
 * QQ:873689
 * @date 2020-1-17 15:51:14
 */

public class B65_md5 {

    
    
    public static String jiami(String data) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("md5");
        
        byte[] md5 = md.digest(data.getBytes());
        String base64 = Base64.encodeBase64String(md5);
        
        return base64;
    }
}
