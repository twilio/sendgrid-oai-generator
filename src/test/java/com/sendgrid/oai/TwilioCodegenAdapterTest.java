package com.sendgrid.oai;

import com.sendgrid.oai.common.TwilioCodegenAdapter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TwilioCodegenAdapterTest {

    @Test
    public void testGetDomainFromURL(){
        Server server = new Server();
        server.setUrl("https://api.sendgrid.com");
        OpenAPI openAPI = new OpenAPI();
        openAPI.setServers(Collections.singletonList(server));
        TwilioCodegenAdapter twilioCodegenAdapter = new TwilioCodegenAdapter(null,null);
        assertEquals(twilioCodegenAdapter.getDomainFromOpenAPI(openAPI),"api");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetDomainFromURLWhenDomainIsNotPresent(){
        Server server = new Server();
        server.setUrl("https://.sendgrid.com");
        OpenAPI openAPI = new OpenAPI();
        openAPI.setServers(Collections.singletonList(server));
        TwilioCodegenAdapter twilioCodegenAdapter = new TwilioCodegenAdapter(null,null);
        twilioCodegenAdapter.getDomainFromOpenAPI(openAPI);
    }

    @Test
    public void testGetExtensionFromOpenAPISpec(){
        Map<String,Object> map = new HashMap<>();
        map.put("x-extension","domain_authentication");
        OpenAPI openAPI = new OpenAPI();
        Info info = new Info();
        info.setExtensions(map);
        openAPI.setInfo(info);
        TwilioCodegenAdapter twilioCodegenAdapter = new TwilioCodegenAdapter(null,null);
        assertEquals("domainAuthentication",twilioCodegenAdapter.getExtensionFromOpenAPISpec(map));
    }

    @Test
    public void testGetExtensionFromInputPath(){
        TwilioCodegenAdapter twilioCodegenAdapter = new TwilioCodegenAdapter(null,null);
        assertEquals("domainAuthentication",twilioCodegenAdapter.getExtensionFromFileName("/Users/manisingh/github/sendgrid/sendgrid-oai-generator/examples/spec/tsg_domain_authentication.yaml"));
    }
}
