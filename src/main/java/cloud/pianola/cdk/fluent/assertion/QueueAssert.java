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
 * Fluent assertions for AWS::SQS::Queue. This should be used if the resource map is extracted
 * from the AWS template. Otherwise, start with {@link CDKStackAssert#containsQueue(String)}.
 * <p>
 * Example:
 * </p>
 * <pre>
 *     {@code
 *         QueueAssert.assertThat(map)
 *            .hasQueue("example-lambda-function-success-queue")
 *            .hasTag("COST_CENTRE", "pianola-core")
 *            .hasTag("ENV", "Test")
 *            .hasDeadLetterQueue("examplelambdafunctionsuccessqueuedlq(.*)")
 *            .hasMaxRetrialCount(3)
 *            .hasUpdateReplacePolicy("Delete")
 *            .hasDeletionPolicy("Delete");
 *            }
 *     }
 * </pre>
 */
@SuppressWarnings("unchecked")
public class QueueAssert extends AbstractCDKResourcesAssert<QueueAssert, Map<String, Object>> {

  private QueueAssert(final Map<String, Object> actual) {
    super(actual, QueueAssert.class);
  }

  public static QueueAssert assertThat(final Map<String, Object> actual) {
    return new QueueAssert(actual);
  }

  public QueueAssert hasQueue(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");

    final String topicName = (String) properties.get("QueueName");

    Assertions.assertThat(topicName)
        .isInstanceOf(String.class)
        .isEqualTo(expected);

    return this;
  }

  public QueueAssert hasDeadLetterQueue(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, List<Object>> redrivePolicy =
        (Map<String, List<Object>>) properties.get("RedrivePolicy");
    final Map<String, Object> deadLetterTargetArn =
        (Map<String, Object>) redrivePolicy.get("deadLetterTargetArn");
    final List<String> roleArnFun = (List<String>) deadLetterTargetArn.get("Fn::GetAtt");

    Assertions.assertThat(roleArnFun)
        .isInstanceOf(List.class)
        .anySatisfy(s -> Assertions.assertThat(s)
            .isInstanceOf(String.class)
            .matches(e -> e.matches(expected)));

    return this;
  }

  public QueueAssert hasMaxRetrialCount(final Integer deletionPolicy) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Integer> redrivePolicy =
        (Map<String, Integer>) properties.get("RedrivePolicy");
    final Integer maxReceiveCount = redrivePolicy.get("maxReceiveCount");

    Assertions.assertThat(maxReceiveCount)
        .isEqualTo(deletionPolicy);

    return this;
  }
}
