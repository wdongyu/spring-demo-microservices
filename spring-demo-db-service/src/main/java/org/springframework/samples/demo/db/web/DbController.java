package org.springframework.samples.demo.db.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Properties;

import com.netflix.discovery.DiscoveryManager;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.DomainManager;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.Request;
import org.omg.CORBA.SetOverrideType;
import org.omg.SendingContext.RunTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wdongyu
 */

@RestController
public class DbController {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "/db" ,method = RequestMethod.GET)
    public String db() {
        ServiceInstance instance = client.getLocalServiceInstance();
        //DiscoveryManager.getInstance().shutdownComponent();
        logger.info("/db, host:" + instance.getHost() + ", service_id:" + instance.getServiceId());
        //Properties properties = System.getProperties();
        return "Info from Database";
    }

    @RequestMapping(value = "/servicePath" ,method = RequestMethod.GET)
    @CrossOrigin(origins = "*")
    public JSONObject servicePath() {
        Properties properties = System.getProperties();
        String path = properties.getProperty("user.dir");
        String res = "{\"path\" : \"" + path + "\"}";

        //execCommand("/usr/bin/open -a /Applications/Utilities/Terminal.app");
        //execCommand("cd " + path);
        //execCommand("mvn spring-boot:run");
        Process pro = null;
        try {
            //String[] cmdString = new String[]{"cd " + path, "mvn spring-boot:run"};
            Runtime rt = Runtime.getRuntime();
            //pro = rt.exec("open -n /Applications/Utilities/Terminal.app");
            //String[] s = new String[]{"/bin/bash","-c","cd ~/Document; ls -l"};
            //pro = rt.exec(s);
            pro = rt.exec("pwd");
            //pro = rt.exec("mvn spring-boot:run", null, new File(path));
            //ProcessBuilder pb = new ProcessBuilder(cmdString);
            //pb.start();
            //return (JSONObject)(new JSONParser().parse(res));
        } catch (Exception e) {
            //TODO: handle exception
            logger.info(e);
        }

        if (pro != null) {
            BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(pro.getOutputStream())), true);
            //out.println("pwd");
            try {
               String line;
               while ((line = in.readLine()) != null) {
                  logger.info(line);
               }
               pro.waitFor();
               in.close();
               out.close();
               pro.destroy();
               return (JSONObject)(new JSONParser().parse(res));
            }
            catch (Exception e) {
               logger.info("aaa");
            }
         }
        
        return null;
    }


    private void execCommand(String command) {
        try {
            Process pro = Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            logger.info("fail to exec " + command);
        }
    }
}
