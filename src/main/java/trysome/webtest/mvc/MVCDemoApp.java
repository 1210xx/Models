package trysome.webtest.mvc;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public class MVCDemoApp {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.getInteger("port",8866));
        //获取默认的http connector
        tomcat.getConnector();
        //相当于将自己编写的war包放入webapp目录
        //contextPath : 相当于放入webapp目录的层级
        //docBase : context的基本目录 base dir for the context，for static files. Must exist, relative to the server home
        Context ctx = tomcat.addWebapp("/", new File("src/main/webapp").getAbsolutePath());
        //创建一个完整的webapp资源集合
        WebResourceRoot resourceRoot = new StandardRoot(ctx);
        //为webapp新增一个'Pre'资源
        resourceRoot.addPreResources(new DirResourceSet(resourceRoot, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(),"/"));
        ctx.setResources(resourceRoot);
        tomcat.start();
        //Wait until a proper shutdown command is received, then return.
        tomcat.getServer().await();
    }

}
