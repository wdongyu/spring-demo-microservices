package org.springframework.samples.demo.proc.web;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


/**
 * @author wdongyu
 */

@RestController
public class ProcController {

    //private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "/proc" ,method = RequestMethod.GET)
    public String proc() {
        //ServiceInstance instance = client.getLocalServiceInstance();
        //logger.info("/db, host:" + instance.getHost() + ", service_id:" + instance.getServiceId());
        //return "Info from Database";
	//String authUrl = serviceUrl("auth-service");
    String dbUrl = serviceUrl("db-service") + "/db";
    return dbUrl; 
    //return restTemplate.getForEntity(dbUrl,String.class).getBody();
	//String fromAuth = restTemplate.getForEntity("http://auth-service/auth",String.class).getBody();
	//String fromDb = restTemplate.getForEntity("http://db-service/db",String.class).getBody();
	//return (" ---  Process part  --- " + '\n' + fromAuth + '\n' + fromDb);
    }


    public String serviceUrl(String serviceName) {
        List<ServiceInstance> list = this.client.getInstances(serviceName);
        try {
            if (list != null && list.size() > 0 ) {
                URL url = new URL(list.get(list.size()-1).getUri().toString() + "/info");
                
                return getCommitId(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public String getCommitId(URL url) {
        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
            StringBuffer bs = new StringBuffer();
            String str = null;
            while((str=buffer.readLine())!=null){
                bs.append(str);
            }

            JSONObject json = (JSONObject)(new JSONParser().parse(bs.toString()));
            json = (JSONObject)(json.get("git"));
            json = (JSONObject)(json.get("commit"));
            return json.get("id").toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
