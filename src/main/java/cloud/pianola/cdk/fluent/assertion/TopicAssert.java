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

import java.util.Map;
import org.assertj.core.api.Assertions;

/**
 * Fluent assertions for AWS::SNS::Topic. This should be used if the resource map is extracted
 * from the AWS template. Otherwise, start with {@link CDKStackAssert#containsTopic(String)}.
 * <p>
 * Example:
 * </p>
 * <pre>
 *     {@code
 *         TopicAssert.assertThat(map)
 *            .hasTopic("example-lambda-function-topic.dlq")
 *            .hasTag("COST_CENTRE", "pianola-core")
 *            .hasTag("ENV", "Test");
 *     }
 * </pre>
 */
@SuppressWarnings("unchecked")
public class TopicAssert extends AbstractCDKResourcesAssert<TopicAssert, Map<String, Object>> {

  private TopicAssert(final Map<String, Object> actual) {
    super(actual, TopicAssert.class);
  }

  public static TopicAssert assertThat(final Map<String, Object> actual) {
    return new TopicAssert(actual);
  }

  public TopicAssert hasTopic(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");

    final String topicName = (String) properties.get("TopicName");

    Assertions.assertThat(topicName)
        .isEqualTo(expected);

    return this;
  }
}
