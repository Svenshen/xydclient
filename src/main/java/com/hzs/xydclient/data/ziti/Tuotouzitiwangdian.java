/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @autoor szh
 */

package com.hzs.xydclient.data.ziti;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author  szh
 * QQ:873689
 * @date 2020-9-21 10:42:28
 */
@Data
public class Tuotouzitiwangdian implements Serializable{

    String sheng;
    
    String shi;
    
    String qu;
    
    String jigoudaima;
    
    String jigoumingcheng;
    
    int tuotouliang;
    
    int yingyewangdianzhuanziti;
    
    int daxuexiaoyuanzhuanziti;
    
    int toudiwangdianzhuanziti;
    
    int shehuiwangdianzhuangziti;
    
    int baokantingzhuanziti;
    
}
