package org.springframework.samples.demo.portal.web;

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
public class PortalController {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "/portal" ,method = RequestMethod.GET)
    public String portal() {
        //ServiceInstance instance = client.getLocalServiceInstance();
        //logger.info("/db, host:" + instance.getHost() + ", service_id:" + instance.getServiceId());
        //String authUrl = this.serviceUrl("auth-service");
        //String procUrl = this.serviceUrl("proc-service");
        //String fromAuth = restTemplate.getForEntity("http://auth-service/auth", String.class).getBody();
        //String fromProc = restTemplate.getForEntity("http://proc-service/proc", String.class).getBody();
        //return (" ---  Portal part  --- " + '\n' + fromAuth + '\n' + fromProc);
    
        //String portInfo = serviceInfo("portal-service");
        //String portId = getCommitId(portInfo);
        //String portTime = getCommitTime(portInfo);

        String authUrl = serviceUrl("auth-service") + "/auth/portal/0/0";
        if (authUrl == null)
            return "Not exist auth-service";
        else {
            String fromAuth = restTemplate.getForEntity(authUrl, String.class).getBody();
            int index = fromAuth.indexOf(":");
            String commitId = fromAuth.substring(0, index);
            String tmp = fromAuth.substring(index+1);
            index = tmp.indexOf(":");
            String username = tmp.substring(0, index), password = tmp.substring(index+1);
            logger.info(commitId + ":" + username + ":" + password);
            String procUrl = serviceUrl("proc-service") + "/proc/" + commitId + "/" + username + "/" + password;
            return restTemplate.getForEntity(procUrl, String.class).getBody() + " " + commitId;
        }
    }

    public String serviceInfo(String serviceName) {
        List<ServiceInstance> list = this.client.getInstances(serviceName);
        try {
            if (list != null && list.size() > 0 ) {
                URL url = new URL(list.get(list.size()-1).getUri().toString() + "/info");
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                InputStream is = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
                StringBuffer bs = new StringBuffer();
                String str = null;
                while((str=buffer.readLine())!=null){
                    bs.append(str);
                }
                return bs.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String serviceUrl(String serviceName) {
        List<ServiceInstance> list = this.client.getInstances(serviceName);
        String uri = null;
        if (list != null && list.size() > 0 ) {
            uri = list.get(0).getUri().toString();
        }

        return uri;
    }

    public String getCommitId(String str) {
        try {
            if (str == null) return null;
            JSONObject json = (JSONObject)(new JSONParser().parse(str));
            json = (JSONObject)(json.get("git"));
            json = (JSONObject)(json.get("commit"));
            return json.get("id").toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getCommitTime(String str) {
        try {
            JSONObject json = (JSONObject)(new JSONParser().parse(str));
            json = (JSONObject)(json.get("git"));
            json = (JSONObject)(json.get("commit"));
            return json.get("time").toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
