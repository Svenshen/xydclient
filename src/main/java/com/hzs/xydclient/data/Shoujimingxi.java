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
 * @date 2020-6-28 17:22:32
 */
@Data
public class Shoujimingxi implements Serializable{

    String count;
    String pkpWaybillId;
    String bizProductName;
    String ioType;
    String ecommerceNo;
    String waybillNo;
    String bizOccurDate;
    String postOrgNo;
    String postOrgName;
    String postPersonName;
    String senderNo;
    String sender;
    String senderLinker;
    String senderMobile;
    String senderIdType;
    String senderIdNo;
    String senderAddr;
    String senderProvinceName;
    String senderCityName;
    String senderCountyName;
    String senderDistrictNo;
    String senderPostcode;
    String receiverLinker;
    String receiverMobile;
    String receiverAddr;
    String receiverCountryName;
    String receiverProvinceNo;
    String receiverProvinceName;
    String receiverCityName;
    String receiverCountyName;
    String receiverPostcode;
    String realWeight;
    String feeWeight;
    String length;
    String width;
    String height;
    String codAmount;
    String receiptFlag;
    String receiptWaybillNo;
    String receiptFeeAmount;
    String insuranceFlag;
    String insuranceAmount;
    String insurancePremiumAmount;
    String transferType;
    String allowFeeFlag;
    String isFeedFlag;
    String feeDate;
    String postageTotal;
    String postageStandard;
    String postagePaid;
    String postageOther;
    String settlementMode;
    String gmtModified;
    String theOrgGridNo;
    String sortingCode;
    String oneBillFlag;
    String receiverArriveOrgName;
    String isJinguan;
    String pickupPersonName;
    
    
}
