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
 * Fluent assertions for AWS::IAM::Policy. This should be used if the resource map is extracted from
 * the AWS template. Otherwise, start with {@link CDKStackAssert#containsPolicy(String)}.
 * <p>
 * Example:
 * </p>
 * <pre>
 *     {@code
 *     PolicyAssert.assertThat(map)
 *       .hasPolicy(policyName)
 *       .isAssociatedWithRole("examplelambdafunctionrole(.*)")
 *       .hasPolicyDocumentVersion(policyDocumentVersion)
 *       .hasPolicyDocumentStatement(principal, resource, action, effect, policyDocumentVersion);
 *     }
 * </pre>
 */
@SuppressWarnings("unchecked")
public class PolicyAssert extends
    AbstractCDKResourcesAssert<PolicyAssert, Map<String, Object>> {

  private PolicyAssert(final Map<String, Object> actual) {
    super(actual, PolicyAssert.class);
  }

  public static PolicyAssert assertThat(final Map<String, Object> actual) {
    return new PolicyAssert(actual);
  }

  public PolicyAssert hasPolicy(final String expected) {

    final String policyName =
        ((Map<String, String>) actual.get("Properties")).get("PolicyName");

    Assertions.assertThat(policyName)
        .isNotNull()
        .isInstanceOf(String.class)
        .matches(actualPolicyName -> actualPolicyName.matches(expected));

    return this;
  }

  public PolicyAssert hasPolicyDocumentVersion(final String expected) {
    final Map<String, Object> properties =
        ((Map<String, Object>) actual.get("Properties"));

    final Map<String, Object> policyDocument =
        ((Map<String, Object>) properties.get("PolicyDocument"));

    Assertions.assertThat(policyDocument)
        .isNotNull()
        .isNotEmpty()
        .containsEntry("Version", expected);

    return this;
  }


  public PolicyAssert hasPolicyDocumentStatement(final String principal,
      final String resource,
      final String action,
      final String effect,
      final String policyDocumentVersion) {
    final Map<String, Object> properties =
        ((Map<String, Object>) actual.get("Properties"));

    final Map<String, Object> policyDocument =
        ((Map<String, Object>) properties.get("PolicyDocument"));

    return hasPolicy(principal, resource, effect, policyDocumentVersion, action, policyDocument);
  }

  public PolicyAssert isAssociatedWithRole(final String expected) {
    final List<Object> roles =
        ((Map<String, List<Object>>) actual.get("Properties")).get("Roles");

    Assertions.assertThat(roles)
        .isNotNull()
        .isNotEmpty()
        .allMatch(role -> ((Map<String, String>) role).get("Ref").matches(expected));

    return this;
  }
}
