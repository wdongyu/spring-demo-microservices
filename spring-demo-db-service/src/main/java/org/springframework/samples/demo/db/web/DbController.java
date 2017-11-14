package org.springframework.samples.demo.db.web;

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
public class DbController {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "/db" ,method = RequestMethod.GET)
    public String db() {
        ServiceInstance instance = client.getLocalServiceInstance();
        logger.info("/db, host:" + instance.getHost() + ", service_id:" + instance.getServiceId());
        return "Info from Database";
    }

}
