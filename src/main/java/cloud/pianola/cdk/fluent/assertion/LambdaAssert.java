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
 * Fluent assertions for AWS::Lambda::Function. This should be used if the resource map is extracted
 * from the AWS template. Otherwise, start with {@link CDKStackAssert#containsFunction(String)}.
 * <p>
 * Example:
 * </p>
 * <pre>
 *     {@code
 *       LambdaAssert.assertThat(map)
 *         .hasFunction("example-lambda-function")
 *         .hasHandler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
 *         .hasCode("test-cdk-bucket", "(.*).zip")
 *         .hasRole("examplelambdafunctionrole(.*)")
 *         .hasDependency("examplelambdafunctionrole(.*)")
 *         .hasDependency("examplelambdafunctionroleDefaultPolicy(.*)")
 *         .hasTag("COST_CENTRE", "pianola-core")
 *         .hasTag("ENV", "Test")
 *         .hasEnvironmentVariable("ENV", "Test")
 *         .hasEnvironmentVariable("SPRING_PROFILES_ACTIVE", "Test")
 *         .hasDescription("Lambda example")
 *         .hasMemorySize(512)
 *         .hasRuntime("provided.al2")
 *         .hasTimeout(3);
 *     }
 * </pre>
 */
@SuppressWarnings("unchecked")
public class LambdaAssert extends AbstractCDKResourcesAssert<LambdaAssert, Map<String, Object>> {

  private LambdaAssert(final Map<String, Object> actual) {
    super(actual, LambdaAssert.class);
  }

  public static LambdaAssert assertThat(final Map<String, Object> actual) {
    return new LambdaAssert(actual);
  }

  public LambdaAssert hasFunction(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");

    Assertions.assertThat(properties.get("FunctionName"))
        .isInstanceOf(String.class)
        .matches(expected::equals);

    return this;
  }

  public LambdaAssert hasHandler(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final String handler = (String) properties.get("Handler");

    Assertions.assertThat(handler)
        .isInstanceOf(String.class)
        .isEqualTo(expected);

    return this;
  }

  public LambdaAssert hasCode(final String s3Bucket, final String s3Key) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> lambdaCode = (Map<String, String>) properties.get("Code");

    Assertions.assertThat(lambdaCode)
        .isInstanceOf(Map.class)
        .hasSize(2)
        .containsEntry("S3Bucket", s3Bucket)
        .hasEntrySatisfying("S3Key", value -> Assertions.assertThat(value)
            .isInstanceOf(String.class).matches(e -> e.matches(s3Key)));

    return this;
  }

  public LambdaAssert hasRole(final String arnRegex) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> role = (Map<String, Object>) properties.get("Role");
    final List<String> roleArnFun = (List<String>) role.get("Fn::GetAtt");

    Assertions.assertThat(roleArnFun)
        .isInstanceOf(List.class)
        .anySatisfy(s -> Assertions.assertThat(s)
            .isInstanceOf(String.class)
            .matches(e -> e.matches(arnRegex)));

    return this;
  }
}
