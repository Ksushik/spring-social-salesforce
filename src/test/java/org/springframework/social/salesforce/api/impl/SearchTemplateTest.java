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

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.social.salesforce.api.ResultItem;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;


/**
 * @author Umut Utkan
 * @author Jared Ottley
 */
public class SearchTemplateTest extends AbstractSalesforceTest {

    @Test
    public void search() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/" + salesforce.apiOperations().getVersion() + "/search?q=FIND+%7Bxxx*%7D+IN+ALL+FIELDS+RETURNING+Contact%2C+Account"))
                .andExpect(method(GET))
                .andRespond(withStatus(HttpStatus.OK).body(loadResource("search.json")).headers(responseHeaders));
        List<ResultItem> results = salesforce.searchOperations().search("FIND {xxx*} IN ALL FIELDS RETURNING Contact, Account");
        assertEquals(4, results.size());
        assertEquals("Contact", results.get(0).getType());
        assertEquals("Contact", results.get(1).getType());
        assertEquals("Account", results.get(2).getType());
        assertEquals("Account", results.get(3).getType());
    }

}
