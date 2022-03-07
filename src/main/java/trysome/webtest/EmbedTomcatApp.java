package trysome.webtest;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

//具体注释看trysome/webtest/mvc/MVCDemoApp.java
public class EmbedTomcatApp {
    public static void main(String[] args) throws LifecycleException, InterruptedException {
        //启动tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.getInteger("port",8888));
        tomcat.getConnector();
        //创建webapp
        Context context = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
        //设置ROOT
        WebResourceRoot resourceRoot = new StandardRoot(context);
        resourceRoot.addPreResources(
                new DirResourceSet(resourceRoot,
                        "/WEB-INF/classes",
                        new File("target/classes").getAbsolutePath(),
                "/"));
        context.setResources(resourceRoot);
        tomcat.start();
        tomcat.getServer().await();
    }
}
