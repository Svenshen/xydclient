/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @autoor szh
 */

package com.hzs.xydclient.data;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author  szh
 * QQ:873689
 * @date 2020-6-12 8:47:16
 */
@Data
public class Guiji implements Serializable{
    
    String baseProductName;
    String baseProductNo;
    String bizProductName;
    String bizProductNo;
    String desc;
    String kind;
    String opCode;
    String opName;
    String opOrgCode;
    String opOrgSimpleName;
    String opTime;
    String operatorName;
    String operatorNo;
    String postDate;
    String receivePlace;
    String source;
    String traceNo;
    

}
