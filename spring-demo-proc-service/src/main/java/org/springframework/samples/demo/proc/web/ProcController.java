package org.springframework.samples.demo.proc.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

    public String serviceUrl(String serviceName) {
    	List<ServiceInstance> list = this.client.getInstances(serviceName);
    	if (list != null && list.size() > 0 ) {
            return list.get(list.size()-1).getUri().toString();
    	}
    	return null;
    }

    @RequestMapping(value = "/proc" ,method = RequestMethod.GET)
    public String proc() {
        //ServiceInstance instance = client.getLocalServiceInstance();
        //logger.info("/db, host:" + instance.getHost() + ", service_id:" + instance.getServiceId());
        //return "Info from Database";
	String authUrl = serviceUrl("auth-service");
	String dbUrl = serviceUrl("db-service");
	String fromAuth = restTemplate.getForEntity(authUrl + "/auth",String.class).getBody();
	String fromDb = restTemplate.getForEntity(dbUrl + "/db",String.class).getBody();
	return (" ---  Process part  --- " + '\n' + fromAuth + '\n' + fromDb);
    }

}
