/*
 *   Licensed to Muhammad Hamadto
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package cloud.pianola.cdk.fluent.assertion;

import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;

/**
 * Fluent assertions for AWS::SNS::Subscription. This should be used if the resource map is extracted
 * from the AWS template. Otherwise, start with {@link CDKStackAssert#containsTopicSubscription(String, String, String)}.
 * <p>
 * Example:
 * </p>
 * <pre>
 *     {@code
 *         TopicAssert.assertThat(map)
 *            .hasTopicSubscription(expectedTopicArn, expectedProtocol, expectedEndpoint);
 *     }
 * </pre>
 */
@SuppressWarnings("unchecked")
public class TopicSubscriptionAssert extends
    AbstractCDKResourcesAssert<TopicSubscriptionAssert, Map<String, Object>> {

  private TopicSubscriptionAssert(final Map<String, Object> actual) {
    super(actual, TopicSubscriptionAssert.class);
  }

  public static TopicSubscriptionAssert assertThat(final Map<String, Object> actual) {
    return new TopicSubscriptionAssert(actual);
  }

  public TopicSubscriptionAssert hasTopicSubscription(final String expectedTopicArn,
      final String expectedProtocol,
      final String expectedEndpoint) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");

    final Map<String, List<String>> actualEndpoint =
        (Map<String, List<String>>) properties.get("Endpoint");

    final Map<String, Object> actualTopicArn = (Map<String, Object>) properties.get("TopicArn");

    final String actualProtocol = (String) properties.get("Protocol");

    Assertions.assertThat(actualProtocol)
        .isEqualTo(expectedProtocol);

    final List<String> endpointFun = actualEndpoint.get("Fn::GetAtt");

    Assertions.assertThat(endpointFun)
        .isInstanceOf(List.class)
        .anySatisfy(s -> Assertions.assertThat(s)
            .isInstanceOf(String.class)
            .matches(e -> e.matches(expectedEndpoint)));

    Assertions.assertThat(actualTopicArn.get("Ref").toString())
        .matches(expectedTopicArn);

    return this;
  }
}
