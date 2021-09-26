package io.github.binjamil.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceUtil {
    
    private final String serviceAddress;

    @Autowired
    public ServiceUtil(@Value("${server.port}") String port) {
        this.serviceAddress = findMyHostname() + "/" + findMyIpAddress() + ":" + port;
    }

    public String getServiceAddress() {
        return this.serviceAddress;
    }

    private String findMyIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "Unknown IP Address";
        }
    }

    private String findMyHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Unknown host name";
        }
    }
}
