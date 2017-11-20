package org.springframework.samples.demo.portal.web;

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
public class PortalController {

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

    @RequestMapping(value = "/portal" ,method = RequestMethod.GET)
    public String portal() {
        //ServiceInstance instance = client.getLocalServiceInstance();
        //logger.info("/db, host:" + instance.getHost() + ", service_id:" + instance.getServiceId());
        //return "Info from Database";
	String authUrl = this.serviceUrl("auth-service");
	String procUrl = this.serviceUrl("proc-service");
	String fromAuth = restTemplate.getForEntity(authUrl + "/auth", String.class).getBody();
	String fromProc = restTemplate.getForEntity(procUrl + "/proc", String.class).getBody();
	return (" ---  Portal part  --- " + '\n' + fromAuth + '\n' + fromProc);
    }

}
