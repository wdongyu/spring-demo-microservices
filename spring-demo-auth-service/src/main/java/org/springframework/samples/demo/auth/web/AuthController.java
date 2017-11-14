package org.springframework.samples.demo.auth.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wdongyu
 */

@RestController
public class AuthController {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "/auth" ,method = RequestMethod.GET)
    public String auth() {
        ServiceInstance instance = client.getLocalServiceInstance();
        logger.info("/auth, host:" + instance.getHost() + ", service_id:" + instance.getServiceId());
        return "Token from Authenticate";
    }

}
