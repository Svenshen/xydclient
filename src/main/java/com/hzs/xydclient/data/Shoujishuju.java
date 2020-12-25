/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @autoor szh
 */

package com.hzs.xydclient.data;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 *
 * @author  szh
 * QQ:873689
 * @date 2020-6-28 17:20:34
 */
@Data
public class Shoujishuju implements Serializable{

    String resCode;
    
    String resMess;
    
    String comment;
    
    List<Shoujimingxi> detail;
    
    String innerHtml;
    
    
}
