package com.technoverse.platformManager.utils;

import org.apache.maven.shared.invoker.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;


public class MavenBuildUtil {

    public static void build(String projectPath,String mavenHome) throws MavenInvocationException {
        String pomUrl=projectPath+"/pom.xml";
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile( new File( pomUrl ) );
        request.setGoals( Collections.singletonList( "install" ) );
        System.setProperty("maven.home",mavenHome);
        Invoker invoker = new DefaultInvoker();
        InvocationResult result = invoker.execute( request );
        if ( result.getExitCode() != 0 )
        {
            throw new IllegalStateException( "Build failed." );
        }
    }
}
