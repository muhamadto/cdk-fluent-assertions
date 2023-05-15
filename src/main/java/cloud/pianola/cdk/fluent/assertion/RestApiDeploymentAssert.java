package cloud.pianola.cdk.fluent.assertion;

import java.util.Map;
import org.assertj.core.api.Assertions;

/**
 * Fluent assertions for AWS::ApiGateway::Deployment. This should be used if the resource map is
 * extracted from the AWS template. Otherwise, start with
 * {@link CDKStackAssert#containsRestApiDeployment(String)}.
 * <p>
 * Example:
 * </p>
 * <pre>
 *     {@code
 *     RestApiDeploymentAssert.assertThat(map)
 *         .hasRestApiId("examplelambdafunctionrestapi(.*)")
 *         .hasDependency("examplelambdafunctionrestapiproxyANY(.*)")
 *         .hasDependency("examplelambdafunctionrestapiproxy(.*)")
 *         .hasDependency("examplelambdafunctionrestapiANY(.*)")
 *         .hasDependency("examplelambdafunctionrestapinamePOST(.*)")
 *         .hasDependency("examplelambdafunctionrestapiname(.*)")
 *         .hasDescription("Automatically created by the RestApi construct");
 *     }
 * </pre>
 */
@SuppressWarnings("unchecked")
public class RestApiDeploymentAssert extends
    AbstractCDKResourcesAssert<RestApiDeploymentAssert, Map<String, Object>> {

  private RestApiDeploymentAssert(final Map<String, Object> actual) {
    super(actual, RestApiDeploymentAssert.class);
  }

  public static RestApiDeploymentAssert assertThat(final Map<String, Object> actual) {
    return new RestApiDeploymentAssert(actual);
  }

  public RestApiDeploymentAssert hasRestApiId(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> restApiId = (Map<String, String>) properties.get("RestApiId");

    Assertions.assertThat(restApiId)
        .isNotEmpty()
        .extracting("Ref")
        .matches(arn -> arn.toString().matches(expected));

    return this;
  }
}
