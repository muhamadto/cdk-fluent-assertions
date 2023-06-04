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

import static cloud.pianola.cdk.fluent.assertion.CdkResourceType.APIGATEWAY_RESTAPI;
import static cloud.pianola.cdk.fluent.assertion.CdkResourceType.APIGATEWAY_RESTAPI_ACCOUNT;
import static cloud.pianola.cdk.fluent.assertion.CdkResourceType.APIGATEWAY_RESTAPI_DEPLOYMENT;
import static cloud.pianola.cdk.fluent.assertion.CdkResourceType.APIGATEWAY_RESTAPI_METHOD;
import static cloud.pianola.cdk.fluent.assertion.CdkResourceType.APIGATEWAY_RESTAPI_RESOURCE;
import static cloud.pianola.cdk.fluent.assertion.CdkResourceType.APIGATEWAY_RESTAPI_STAGE;
import static cloud.pianola.cdk.fluent.assertion.CdkResourceType.LAMBDA_EVENT_INVOKE_CONFIG;
import static cloud.pianola.cdk.fluent.assertion.CdkResourceType.LAMBDA_FUNCTION;
import static cloud.pianola.cdk.fluent.assertion.CdkResourceType.LAMBDA_PERMISSION;
import static cloud.pianola.cdk.fluent.assertion.CdkResourceType.POLICY;
import static cloud.pianola.cdk.fluent.assertion.CdkResourceType.QUEUE;
import static cloud.pianola.cdk.fluent.assertion.CdkResourceType.QUEUE_POLICY;
import static cloud.pianola.cdk.fluent.assertion.CdkResourceType.ROLE;
import static cloud.pianola.cdk.fluent.assertion.CdkResourceType.TOPIC;
import static cloud.pianola.cdk.fluent.assertion.CdkResourceType.TOPIC_SUBSCRIPTION;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import java.util.List;
import java.util.Map;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import software.amazon.awscdk.assertions.Match;
import software.amazon.awscdk.assertions.Template;

public class CDKStackAssert extends AbstractAssert<CDKStackAssert, Template> {

  private CDKStackAssert(final Template actual) {
    super(actual, CDKStackAssert.class);
  }

  /**
   * Fluent assertions for CDK resources. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}.
   *
   * @param actual {@link Template} instance
   * @return {@link CDKStackAssert} instance
   */
  public static CDKStackAssert assertThat(final Template actual) {
    return new CDKStackAssert(actual);
  }

  /**
   * Fluent assertions for AWS::ApiGateway::RestApi. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from,
   * then {@link RestApiAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *       CDKStackAssert.assertThat(template)
   *         .containsRestApi("example-lambda-function-rest-api")
   *         .hasTag("COST_CENTRE", "pianola-core")
   *         .hasTag("ENV", "Test");
   *          }
   *   </pre>
   *   @param expected rest api name
   *   @return {@link RestApiAssert} instance
   */
  public RestApiAssert containsRestApi(final String expected) {
    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "Name", stringLikeRegexp(expected))
    );

    final Map<String, Map<String, Object>> resources = actual.findResources(
        APIGATEWAY_RESTAPI.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return RestApiAssert.assertThat(resources.values().stream().findFirst().get())
        .hasRestApi(expected);
  }

  /**
   * Fluent assertions for AWS::ApiGateway::Account. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from,
   * then {@link RestApiAccountAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *       CDKStackAssert.assertThat(template)
   *         .containsRestApiAccountWithCloudWatchRoleArn("examplelambdafunctionrestapiCloudWatchRole(.*)")
   *         .hasDependency("examplelambdafunctionrestapi(.*)")
   *         .hasUpdateReplacePolicy("Retain")
   *         .hasDeletionPolicy("Retain");
   *          }
   *   </pre>
   *   @param cloudWatchRoleArn cloud watch role arn
   *   @return {@link RestApiAccountAssert} instance
   */
  public RestApiAccountAssert containsRestApiAccountWithCloudWatchRoleArn(
      final String cloudWatchRoleArn) {

    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "CloudWatchRoleArn", Map.of(
                "Fn::GetAtt", List.of(stringLikeRegexp(cloudWatchRoleArn), "Arn")
            )
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(APIGATEWAY_RESTAPI_ACCOUNT.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty();

    return RestApiAccountAssert.assertThat(resources.values().stream().findFirst().get())
        .hasCloudWatchRole(cloudWatchRoleArn);
  }

  /**
   * Fluent assertions for AWS::ApiGateway::Deployment. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from,
   * then {@link RestApiDeploymentAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *       CDKStackAssert.assertThat(template)
   *         .containsRestApiDeployment("examplelambdafunctionrestapi(.*)")
   *         .hasDependency("examplelambdafunctionrestapiproxyANY(.*)")
   *         .hasDependency("examplelambdafunctionrestapiproxy(.*)")
   *         .hasDependency("examplelambdafunctionrestapiANY(.*)")
   *         .hasDependency("examplelambdafunctionrestapinamePOST(.*)")
   *         .hasDependency("examplelambdafunctionrestapiname(.*)")
   *         .hasDescription("Automatically created by the RestApi construct");
   *          }
   *   </pre>
   *   @param expectedRestApiId rest api id
   *   @return {@link RestApiDeploymentAssert} instance
   */
  public RestApiDeploymentAssert containsRestApiDeployment(final String expectedRestApiId) {
    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "RestApiId", Map.of("Ref", stringLikeRegexp(expectedRestApiId))
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(APIGATEWAY_RESTAPI_DEPLOYMENT.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return RestApiDeploymentAssert.assertThat(resources.values().stream().findFirst().get())
        .hasRestApiId(expectedRestApiId);
  }

  /**
   * Fluent assertions for AWS::ApiGateway::Method. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from,
   * then {@link RestApiMethodAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *       CDKStackAssert.assertThat(template)
   *         .containsNonRootRestApiMethod(httpMethod, "examplelambdafunctionrestapiname(.*)")
   *         .hasHttpMethod("POST")
   *         .hasIntegration("POST", "AWS_PROXY")
   *         .hasAuthorizationType("NONE")
   *         .hasRestApiId(("examplelambdafunctionrestapi(.*)"));
   *          }
   *   </pre>
   *   @param method http method
   *   @param resourceId resource id
   *   @return {@link RestApiMethodAssert} instance
   */
  public RestApiMethodAssert containsNonRootRestApiMethod(final String method,
      final String resourceId) {
    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "HttpMethod", method,
            "ResourceId", Map.of("Ref", stringLikeRegexp(resourceId))
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(APIGATEWAY_RESTAPI_METHOD.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return RestApiMethodAssert.assertThat(resources.values().stream().findFirst().get())
        .hasNonRootResourceId(resourceId);
  }

  /**
   * Fluent assertions for AWS::ApiGateway::Method. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from,
   * then {@link RestApiMethodAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *       CDKStackAssert.assertThat(template)
   *         .containsRootRestApiMethod(method, "examplelambdafunctionrestapi(.*)")
   *         .hasHttpMethod("ANY")
   *         .hasIntegration("POST", "AWS_PROXY")
   *         .hasAuthorizationType("NONE")
   *         .hasRestApiId(("examplelambdafunctionrestapi(.*)"));
   *          }
   *   </pre>
   *   @param method http method
   *   @param resourceId resource id
   *   @return {@link RestApiMethodAssert} instance
   */
  public RestApiMethodAssert containsRootRestApiMethod(final String method,
      final String resourceId) {
    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "HttpMethod", method,
            "ResourceId", Map.of(
                "Fn::GetAtt", List.of(stringLikeRegexp(resourceId), "RootResourceId")
            )
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(APIGATEWAY_RESTAPI_METHOD.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return RestApiMethodAssert.assertThat(resources.values().stream().findFirst().get())
        .hasRootResourceId(resourceId);
  }

  /**
   * Fluent assertions for AWS::ApiGateway::Stage. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from,
   * then {@link RestApiStageAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *       CDKStackAssert.assertThat(template)
   *         .containsRestApiStage("test")
   *         .hasRestApiId(("examplelambdafunctionrestapi(.*)"))
   *         .hasDeploymentId(("examplelambdafunctionrestapiDeployment(.*)"))
   *         .hasDependency("examplelambdafunctionrestapiAccount(.*)")
   *         .hasTag("COST_CENTRE", "pianola-core")
   *         .hasTag("ENV", "Test");
   *          }
   *   </pre>
   *   @param stageName stage name
   *   @return {@link RestApiStageAssert} instance
   */
  public RestApiStageAssert containsRestApiStage(final String stageName) {
    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "StageName", stringLikeRegexp(stageName)
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(APIGATEWAY_RESTAPI_STAGE.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return RestApiStageAssert.assertThat(resources.values().stream().findFirst().get())
        .hasStageName(stageName);
  }

  /**
   * Fluent assertions for AWS::ApiGateway::Resource. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from,
   * then {@link RestApiResourceAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *     final String restApiId = "examplelambdafunctionrestapi(.*)";
   *
   *     final Map<String, List<Object>> parentId = Map.of(
   *         "Fn::GetAtt", List.of(Match.stringLikeRegexp(restApiId), "RootResourceId")
   *     );
   *
   *     CDKStackAssert.assertThat(template)
   *        .containsRestApiResource("{proxy+}", restApiId, parentId)
   *        .hasRestApiId(restApiId)
   *        .hasParentId(restApiId);
   *          }
   *   </pre>
   *   @param pathPart path part
   *   @param restApiId rest api id
   *   @param parentId parent id
   *   @return {@link RestApiResourceAssert} instance
   */
  public RestApiResourceAssert containsRestApiResource(final String pathPart,
      final String restApiId,
      final Map<String, List<Object>> parentId) {
    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "PathPart", pathPart,
            "RestApiId", Map.of("Ref", stringLikeRegexp(restApiId)),
            "ParentId", parentId
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(APIGATEWAY_RESTAPI_RESOURCE.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return RestApiResourceAssert.assertThat(resources.values().stream().findFirst().get())
        .hasPath(pathPart);
  }

  /**
   * Fluent assertions for AWS::IAM::Policy. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from,
   * then {@link PolicyAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *       CDKStackAssert.assertThat(template)
   *        .containsPolicy(policyName)
   *        .isAssociatedWithRole("examplelambdafunctionrole(.*)")
   *        .hasPolicyDocumentVersion(policyDocumentVersion)
   *        .hasPolicyDocumentStatement(principal, resource, action, effect, policyDocumentVersion);
   *          }
   * </pre>
   *  @param policyName policy name
   *  @return {@link PolicyAssert} instance
   */
  public PolicyAssert containsPolicy(final String policyName) {
    final Map<String, Map<String, Object>> properties =
        Map.of("Properties", Map.of("PolicyName", Match.stringLikeRegexp(policyName)));

    final Map<String, Map<String, Object>> resources =
        actual.findResources(POLICY.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return PolicyAssert.assertThat(resources.values().stream().findFirst().get())
        .hasPolicy(policyName);
  }

  /**
   * Fluent assertions for AWS::IAM::Role. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from,
   * then {@link RoleAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *       CDKStackAssert.assertThat(template)
   *          .containsRoleWithManagedPolicyArn(managedPolicyArn)
   *          .hasAssumeRolePolicyDocument(principal,
   *             resource,
   *             effect,
   *             policyDocumentVersion,
   *             action);
   *             }
   * </pre>
   * @param managedPolicyArn managed policy arn
   * @return {@link RoleAssert} instance
   */
  public RoleAssert containsRoleWithManagedPolicyArn(final String managedPolicyArn) {

    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "ManagedPolicyArns", List.of(
                Map.of("Fn::Join", List.of(
                        "",
                        List.of("arn:",
                            Map.of("Ref", "AWS::Partition"),
                            managedPolicyArn)
                    )
                )
            )
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(ROLE.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return RoleAssert.assertThat(resources.values().stream().findFirst().get())
        .hasManagedPolicyArn(managedPolicyArn);
  }

  /**
   * Fluent assertions for AWS::Lambda::Function. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from,
   * then {@link LambdaAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *       CDKStackAssert.assertThat(template)
   *         .containsFunction("example-lambda-function")
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
   *         }
   * </pre>
   * @param expected expected function name
   * @return {@link LambdaAssert} instance
   */
  public LambdaAssert containsFunction(final String expected) {
    final Map<String, Map<String, String>> properties = Map.of("Properties",
        Map.of("FunctionName", expected));

    final Map<String, Map<String, Object>> resources =
        actual.findResources(LAMBDA_FUNCTION.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return LambdaAssert.assertThat(resources.values().stream().findFirst().get())
        .hasFunction(expected);
  }

  /**
   * Fluent assertions for AWS::Lambda::EventInvokeConfig. Assertions are done directly on an object
   * of {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted
   * from, then {@link LambdaEventInvokeConfigAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *       CDKStackAssert.assertThat(template)
   *         .containsLambdaEventInvokeConfig(functionName, successEventDestination, failureEventDestination)
   *         .hasLambdaEventInvokeConfigQualifier("$LATEST")
   *         .hasLambdaEventInvokeConfigMaximumRetryAttempts(2);
   *          }
   * </pre>
   * @param functionName function name
   * @param successEventDestination success event destination
   * @param failureEventDestination failure event destination
   * @return {@link LambdaEventInvokeConfigAssert} instance
   */
  public LambdaEventInvokeConfigAssert containsLambdaEventInvokeConfig(final String functionName,
      final String successEventDestination,
      final String failureEventDestination) {

    final Map<String, Map<String, Object>> properties = Map.of("Properties",
        Map.of("FunctionName", Map.of("Ref", stringLikeRegexp(functionName)),
            "DestinationConfig", Map.of(
                "OnFailure",
                Map.of("Destination", Map.of("Ref", stringLikeRegexp(failureEventDestination))),
                "OnSuccess",
                Map.of("Destination", Map.of("Ref", stringLikeRegexp(successEventDestination)))
            )
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(LAMBDA_EVENT_INVOKE_CONFIG.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return LambdaEventInvokeConfigAssert.assertThat(resources.values().stream().findFirst().get())
        .hasLambdaEventInvokeConfig(functionName, successEventDestination, failureEventDestination);
  }

  /**
   * Fluent assertions for AWS::Lambda::EventInvokeConfig. Assertions are done directly on an object
   * of {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted
   * from, then {@link LambdaEventInvokeConfigAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *       CDKStackAssert.assertThat(template)
   *         .containsLambdaEventInvokeConfig(functionName)
   *         .hasLambdaEventInvokeConfigQualifier("$LATEST")
   *         .hasLambdaEventInvokeConfigMaximumRetryAttempts(2);
   *          }
   * </pre>
   * @param functionName function name
   * @return {@link LambdaEventInvokeConfigAssert} instance
   */
  public LambdaEventInvokeConfigAssert containsLambdaEventInvokeConfig(final String functionName) {

    final Map<String, Map<String, Object>> properties = Map.of("Properties",
        Map.of("FunctionName", Map.of("Ref", stringLikeRegexp(functionName))
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(LAMBDA_EVENT_INVOKE_CONFIG.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return LambdaEventInvokeConfigAssert.assertThat(resources.values().stream().findFirst().get())
        .hasLambdaEventInvokeConfig(functionName, null, null);
  }

  /**
   * Fluent assertions for AWS::Lambda::Permission. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from,
   * then {@link LambdaPermissionAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *   final List<Object> sourceArns = List.of(
   *         "arn:",
   *         Map.of("Ref", exact("AWS::Partition")),
   *         ":execute-api:",
   *         Map.of("Ref", exact("AWS::Region")),
   *         ":",
   *         Map.of("Ref", exact("AWS::AccountId")),
   *         ":",
   *         Map.of("Ref", stringLikeRegexp("examplelambdafunctionrestapi(.*)")),
   *         "/",
   *         Map.of("Ref", stringLikeRegexp("examplelambdafunctionrestapi(.*)")),
   *         "/\*\/");
   *
   *       CDKStackAssert.assertThat(template)
   *          .containsLambdaPermission(functionName, action, principal, sourceArns);
   *          }
   *         </pre>
   * @param functionName the name of the lambda function
   * @param action the action
   * @param principal the principal
   * @param sourceArns the source arns
   * @return {@link LambdaPermissionAssert} instance
   */
  public LambdaPermissionAssert containsLambdaPermission(
      final String functionName,
      final String action,
      final String principal,
      final List<Object> sourceArns) {

    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "Action", action,
            "Principal", principal,
            "SourceArn", Map.of("Fn::Join", List.of("", sourceArns)),
            "FunctionName", Map.of("Fn::GetAtt", List.of(stringLikeRegexp(functionName), "Arn"))));

    final Map<String, Map<String, Object>> resources =
        actual.findResources(LAMBDA_PERMISSION.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return LambdaPermissionAssert.assertThat(resources.values().stream().findFirst().get())
        .hasLambdaPermission(functionName, action, principal);
  }

  /**
   * Fluent assertions for AWS::SQS::QueuePolicy. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from,
   * then {@link QueuePolicyAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *       CDKStackAssert.assertThat(template)
   *         .containsQueuePolicy("examplelambdafunctionfailurequeue(.*)", "sqs:SendMessage", "examplelambdafunctionfailurequeue(.*)", "sns.amazonaws.com")
   *         .hasEffect("Allow")
   *         .hasCondition("ArnEquals", "aws:SourceArn", "examplelambdafunctionfailuretopic(.*)");
   *     }
   * </pre>
   * @param queueReference the queue reference (e.g. examplelambdafunctionfailurequeue(.*) )
   * @param policyStatementAction the policy statement action (e.g. sqs:SendMessage)
   * @param policyStatementResource the policy statement resource (e.g. examplelambdafunctionfailurequeue(.*) )
   * @param policyStatementPrincipalService the policy statement principal service (e.g. sns.amazonaws.com)
   * @return {@link QueuePolicyAssert} instance
   */
  public QueuePolicyAssert containsQueuePolicy(final String queueReference,
      final String policyStatementAction,
      final String policyStatementResource,
      final String policyStatementPrincipalService) {

    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of("Queues", List.of(Map.of("Ref", stringLikeRegexp(queueReference))),
            "PolicyDocument", Map.of(
                "Version", "2012-10-17",
                "Statement", List.of(
                    Map.of(
                        "Action", policyStatementAction,
                        "Resource", Map.of("Fn::GetAtt",
                            List.of(stringLikeRegexp(policyStatementResource), "Arn")),
                        "Principal", Map.of(
                            "Service", policyStatementPrincipalService
                        )
                    )
                )
            )
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(QUEUE_POLICY.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return QueuePolicyAssert.assertThat(resources.values().stream().findFirst().get())
        .hasQueuePolicy(queueReference,
            policyStatementAction,
            policyStatementResource,
            policyStatementPrincipalService);
  }

  /**
   * Fluent assertions for AWS::SQS::Queue. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from,
   * then {@link QueueAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *       CDKStackAssert.assertThat(template)
   *            .containsQueue("example-lambda-function-success-queue")
   *            .hasTag("COST_CENTRE", "pianola-core")
   *            .hasTag("ENV", "Test")
   *            .hasDeadLetterQueue("examplelambdafunctionsuccessqueuedlq(.*)")
   *            .hasMaxRetrialCount(3)
   *            .hasUpdateReplacePolicy("Delete")
   *            .hasDeletionPolicy("Delete");
   *            }
   * </pre>
   * @param name the name of the queue
   *  @return {@link QueueAssert} instance
   */
  public QueueAssert containsQueue(final String name) {

    final Map<String, Map<String, Object>> resources =
        actual.findResources(QUEUE.getValue(), Map.of("Properties", Map.of("QueueName", name)));

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return QueueAssert.assertThat(resources.values().stream().findFirst().get())
        .hasQueue(name);
  }

  /**
   * Fluent assertions for AWS::SNS::Topic. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from,
   * then {@link TopicAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *       CDKStackAssert.assertThat(template)
   *            .containsTopic("example-lambda-function-topic.dlq")
   *            .hasTag("COST_CENTRE", "pianola-core")
   *            .hasTag("ENV", "Test");
   *     }
   * </pre>
   * @param name the name of the topic
   * @return {@link TopicAssert} instance
   */
  public TopicAssert containsTopic(final String name) {
    final Map<String, Map<String, String>> properties =
        Map.of("Properties", Map.of("TopicName", name));
    final Map<String, Map<String, Object>> resources =
        actual.findResources(TOPIC.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return TopicAssert.assertThat(resources.values().stream().findFirst().get())
        .hasTopic(name);
  }

  /**
   * Fluent assertions for AWS::SNS::Subscription. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from,
   * then {@link TopicSubscriptionAssert} should be used instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *       CDKStackAssert.assertThat(template)
   *            .containsTopicSubscription(topicArn, protocol, endpoint);
   *     }
   * </pre>
   * @param topicArn the topic arn
   * @param protocol the protocol
   * @param endpoint the endpoint
   */
  public void containsTopicSubscription(final String topicArn,
      final String protocol,
      final String endpoint) {
    final Map<String, Map<String, Object>> properties =
        Map.of("Properties", Map.of(
            "Endpoint", Map.of("Fn::GetAtt", List.of(stringLikeRegexp(endpoint), "Arn")),
            "Protocol", protocol,
            "TopicArn", Map.of("Ref", stringLikeRegexp(topicArn)))
        );

    final Map<String, Map<String, Object>> resources = actual.findResources(
        TOPIC_SUBSCRIPTION.getValue(),
        properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    TopicSubscriptionAssert.assertThat(resources.values().stream().findFirst().get())
        .hasTopicSubscription(topicArn, protocol, endpoint);
  }
}
