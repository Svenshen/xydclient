/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @autoor szh
 */

package com.hzs.xydclient.http;

import java.io.File;
import java.util.List;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.EndpointImplFactory;
import org.apache.cxf.endpoint.dynamic.DynamicClientFactory;
import org.apache.cxf.jaxws.support.JaxWsEndpointImplFactory;

/**
 *
 * @author  szh
 * QQ:873689
 * @date 2020-6-9 17:39:21
 */
/**
 * 覆写父类的compileJavaSrc方法，解决动态编译乱码问题
 *
 * @author yueli.liao
 * @date 2019-03-08 14:10
 */
public class JaxWsDynamicClientFactory extends DynamicClientFactory {

    protected JaxWsDynamicClientFactory(Bus bus) {
        super(bus);
    }

    @Override
    protected EndpointImplFactory getEndpointImplFactory() {
        return JaxWsEndpointImplFactory.getSingleton();
    }

    protected boolean allowWrapperOps() {
        return true;
    }

    /**
     * Create a new instance using a specific <tt>Bus</tt>.
     *
     * @param b the <tt>Bus</tt> to use in subsequent operations with the
     *            instance
     * @return the new instance
     */
    public static JaxWsDynamicClientFactory newInstance(Bus b) {
        return new JaxWsDynamicClientFactory(b);
    }

    /**
     * Create a new instance using a default <tt>Bus</tt>.
     *
     * @return the new instance
     * @see CXFBusFactory#getDefaultBus()
     */
    public static JaxWsDynamicClientFactory newInstance() {
        Bus bus = CXFBusFactory.getThreadDefaultBus();
        return new JaxWsDynamicClientFactory(bus);
    }

    /**
     * 覆写父类的该方法<br/>
     * 注：解决此（错误：编码GBK的不可映射字符）问题
     *
     * @return
     */
    @Override
    protected boolean compileJavaSrc(String classPath, List<File> srcList, String dest) {
        org.apache.cxf.common.util.Compiler javaCompiler
                = new org.apache.cxf.common.util.Compiler();

        // 设置编译编码格式（此处为新增代码）
        javaCompiler.setEncoding("UTF-8");

        javaCompiler.setClassPath(classPath);
        javaCompiler.setOutputDir(dest);
        javaCompiler.setTarget("1.8");

        return javaCompiler.compileFiles(srcList);
    }

}
