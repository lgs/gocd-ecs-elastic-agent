/*
 * Copyright 2020 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thoughtworks.gocd.elasticagent.ecs.requests;

import com.thoughtworks.gocd.elasticagent.ecs.domain.Agent;
import com.thoughtworks.gocd.elasticagent.ecs.domain.ClusterProfileProperties;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ShouldAssignWorkRequestTest {

    @Test
    void shouldDeserializeFromJSON() {
        String json = "{\n" +
                "  \"environment\": \"prod\",\n" +
                "  \"agent\": {\n" +
                "    \"agent_id\": \"42\",\n" +
                "    \"agent_state\": \"Idle\",\n" +
                "    \"build_state\": \"Idle\",\n" +
                "    \"config_state\": \"Enabled\"\n" +
                "  },\n" +
                "  \"elastic_agent_profile_properties\": {\n" +
                "    \"Image\": \"go-agent\"\n" +
                "  },\n" +
                "  \"cluster_profile_properties\": {\n" +
                "    \"GoServerUrl\": \"https://cd.server.com/go\", \n" +
                "    \"ClusterName\": \"deployment-cluster\"\n" +
                "  }\n" +
                "}";

        ShouldAssignWorkRequest request = ShouldAssignWorkRequest.fromJSON(json);
        assertThat(request.environment()).isEqualTo("prod");
        assertThat(request.agent()).isEqualTo(new Agent("42", Agent.AgentState.Idle, Agent.BuildState.Idle, Agent.ConfigState.Enabled));
        assertThat(request.elasticProfile().getImage()).isEqualTo("go-agent");

        Map<String, String> clusterProfileConfigurations = new HashMap<>();
        clusterProfileConfigurations.put("GoServerUrl", "https://cd.server.com/go");
        clusterProfileConfigurations.put("ClusterName", "deployment-cluster");
        ClusterProfileProperties expectedClusterProfileProperties = ClusterProfileProperties.fromConfiguration(clusterProfileConfigurations);

        assertThat(request.clusterProfileProperties()).isEqualTo(expectedClusterProfileProperties);
    }
}
