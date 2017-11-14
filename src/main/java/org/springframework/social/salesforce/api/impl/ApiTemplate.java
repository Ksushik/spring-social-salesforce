/**
 * Copyright (C) 2017 https://github.com/jottley/spring-social-salesforce
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.salesforce.api.impl;

import com.fasterxml.jackson.databind.JsonNode;

import org.apache.commons.lang3.StringUtils;
import org.springframework.social.salesforce.api.ApiOperations;
import org.springframework.social.salesforce.api.ApiVersion;
import org.springframework.social.salesforce.api.InvalidSalesforceApiVersionException;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default implementation of ApiOperations.
 *
 * @author Umut Utkan
 * @author Jared Ottley
 */
public class ApiTemplate extends AbstractSalesForceOperations<Salesforce> implements ApiOperations {

    private RestTemplate restTemplate;
    private String version;
    
    
    String versionRegEx = "v[0-9][0-9]\\.[0-9]";


    public ApiTemplate(Salesforce api, RestTemplate restTemplate) {
        super(api);
        this.restTemplate = restTemplate;
    }


    public List<ApiVersion> getVersions() {
        URI uri = URIBuilder.fromUri(api.getBaseUrl()).build();
        JsonNode dataNode = restTemplate.getForObject(uri, JsonNode.class);
        return api.readList(dataNode, ApiVersion.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getServices(String version) {
        requireAuthorization();
        return restTemplate.getForObject(api.getBaseUrl() + "/{version}", Map.class, version);
    }
    
    
    @SuppressWarnings("unchecked")
    public Map<String, String> getServices() {
        requireAuthorization();
        return this.getServices(getVersion());
    }
    
    
    public void setVersion(String version)
            throws InvalidSalesforceApiVersionException
    {
        if (StringUtils.isNotBlank(version) && validateVersionString(version))
        {
            this.version = version;
        }
        else
        {
            throw new InvalidSalesforceApiVersionException(version);
        }
    }
    
    
    public String getVersion()
    {
        if (StringUtils.isNotBlank(version))
        {
            return version;
        }
        else
        {
            return DEFAULT_API_VERSION;
        }
    }
    
    
    private boolean validateVersionString(String version)
    {
        if (StringUtils.isNotBlank(version))
        {
            Pattern pattern = Pattern.compile(versionRegEx);
            Matcher matcher = pattern.matcher(version);
            
            return matcher.find();
        }
        
        return false;
    }

}
