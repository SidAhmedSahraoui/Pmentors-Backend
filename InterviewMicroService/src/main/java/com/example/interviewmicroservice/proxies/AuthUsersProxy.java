package com.example.interviewmicroservice.proxies;

import com.example.interviewmicroservice.DTOs.ProxyUserResponse;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Auth-Service")
@LoadBalancerClient(name = "Auth-Service")
public interface AuthUsersProxy {

    @GetMapping("/auth/proxy/provider/{id}/{token}")
    ProxyUserResponse getProvider(@PathVariable("id") String email,
                                  @PathVariable("token") String token);

}
