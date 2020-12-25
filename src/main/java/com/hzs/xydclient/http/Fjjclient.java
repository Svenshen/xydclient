/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @autoor szh
 */

package com.hzs.xydclient.http;

import com.hzs.xydclient.data.Luojigekoufjj;
import lombok.Data;
import org.apache.cxf.endpoint.Client;

/**
 *
 * @author  szh
 * QQ:873689
 * @date 2020-6-9 16:20:59
 */
@Data
public class Fjjclient {

    Client client;
    
    public Fjjclient(){
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        client = dcf.createClient("http://10.4.188.85/pcs-tc-nc-job/WyService/services/CommWY?wsdl");
        //client = dcf.createClient("http://10.136.31.132:18888/pcs-tc-nc-job/WyService/services/CommWY?wsdl");
    }
    
    public Luojigekoufjj getGKCX(String mailno) throws Exception{
        Luojigekoufjj l = new Luojigekoufjj();
        l.setMailno(mailno);
        Object[] objects  = client.invoke("getGKCX", new Object[]{"#HEAD::2019SD21520017FJ99999999::"+mailno+"::21502103::SD21520017::::::||END"});
        String str = String.valueOf(objects[0]);
        if(str.contains("HEAD")){
            String strs[] = str.split("::");
            l.setFjjcode(strs[12]);
            l.setFjjname(strs[13]);
            l.setFjjwuli(strs[14]);
        }else if(str.contains("MSG")){
            String strs[] = str.split("::");
            l.setFjjwuli(strs[2]);
            l.setFjjname(strs[2]);
            l.setFjjcode(strs[2]);
        }
        return l;
    }
    
    
}
