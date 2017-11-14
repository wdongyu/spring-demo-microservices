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

/**
 * @author wdongyu
 */

@RestController
public class PortalController {

    //private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    RestTemplate restTemplate;
    //private DiscoveryClient client;

    @RequestMapping(value = "/portal" ,method = RequestMethod.GET)
    public String portal() {
        //ServiceInstance instance = client.getLocalServiceInstance();
        //logger.info("/db, host:" + instance.getHost() + ", service_id:" + instance.getServiceId());
        //return "Info from Database";
	String fromAuth = restTemplate.getForEntity("http://localhost:8082/auth",String.class).getBody();
	String fromProc = restTemplate.getForEntity("http://localhost:8083/proc",String.class).getBody();
	return (" ---  Portal part  --- " + '\n' + fromAuth + '\n' + fromProc);
    }

}
