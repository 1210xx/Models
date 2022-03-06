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
        //Todo:参见EmbedTomcatApp
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.getInteger("port",8866));
        tomcat.getConnector();
        Context context = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
        WebResourceRoot resourceRoot = new StandardRoot(context);
        resourceRoot.addPreResources(new DirResourceSet(resourceRoot, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(),"/"));
        context.setResources(resourceRoot);
        tomcat.start();
        tomcat.getServer().await();
    }

}
