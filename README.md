# cdk-fluent-assertions

# cdk-fluent-assertions

[![CodeQL](https://github.com/muhamadto/cdk-fluent-assertions/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/muhamadto/cdk-fluent-assertions/actions/workflows/codeql-analysis.yml)
[![Build](https://github.com/muhamadto/cdk-fluent-assertions/actions/workflows/build.yml/badge.svg)](https://github.com/muhamadto/cdk-fluent-assertions/actions/workflows/build.yml)
[![Publish package to GitHub Packages](https://github.com/muhamadto/cdk-fluent-assertions/actions/workflows/release.yml/badge.svg?event=release)](https://github.com/muhamadto/cdk-fluent-assertions/actions/workflows/release.yml)

AssertJ-like fluent assertions for AWS CDK testing

###  AWS CDK testing framework

AWS offers a framework to verify that your stack is well-formed and that it matches your expectations. This framework can be found [here](https://docs.aws.amazon.com/cdk/latest/guide/testing.html).

Here is an example to very the stack contains a specific role:


```java

template.hasResourceProperties("AWS::IAM::Role", Match.objectEquals(
    Collections.singletonMap("AssumeRolePolicyDocument", Map.of(
        "Version", "2012-10-17",
        "Statement", Collections.singletonList(Map.of(
            "Action", "sts:AssumeRole",
            "Effect", "Allow",
            "Principal", Collections.singletonMap(
                "Service", Collections.singletonMap(
                    "Fn::Join", Arrays.asList(
                            "",
                            Arrays.asList("states.", Match.anyValue(), ".amazonaws.com")
                    )
                )
            )
        ))
    ))
));
```

###  AWS CDK fluent testing library
While working on a small [open-source project](https://github.com/muhamadto/cdk-fluent-assertions), I was varying a CDK stack containing some resources. I noticed that the tests written using the AWS framework were verbose and presented challenges in terms of readability and maintainability. In response, I decided to create a small library to make it easier to read, write and maintain tests.

To illustrate, an equivalent representation to the aforementioned example can be found below: as follows"
```java
CDKStackAssert.assertThat(template)
  .containsRoleWithManagedPolicyArn(managedPolicyArn)
  .hasAssumeRolePolicyDocument("states.amazonaws.com", null, "Allow", "2012-10-17", "sts:AssumeRole");
```

#### How to use it

* The library is available as Github package. To use it, add the following dependency to your project:

```xml
<dependency>
  <groupId>cloud.pianola</groupId>
  <artifactId>cdk-fluent-assertions</artifactId>
  <version>1.0.0</version>
  <scope>test</scope>
</dependency>
```

* Create a class `TemplateSupport` in your testing packages. This class will be used to load the template and create the `CDKStackAssert` object.

```java
public abstract class TemplateSupport {

  public static final String ENV = "test";
  public static final String TEST_CDK_BUCKET = "test-cdk-bucket";
  public static final String QUALIFIER = "test";
  static Template template;
  private static final String STACK_NAME = "example-lambda-function-test-stack";
  
  @TempDir
  private static Path TEMP_DIR;

  @BeforeAll
  static void initAll() throws IOException {
    final Path lambdaCodePath = TestLambdaUtils.getTestLambdaCodePath(TEMP_DIR);

    final Map<String, String> tags = createTags(ENV, TAG_VALUE_COST_CENTRE);
    final App app = new App();
    final ExampleLambdaStack stack = StackUtils.createStack(app, STACK_NAME, lambdaCodePath.toString(), QUALIFIER, TEST_CDK_BUCKET, ENV);

    tags.entrySet().stream()
        .filter(tag -> Objects.nonNull(tag.getValue()))
        .forEach(tag -> Tags.of(app).add(tag.getKey(), tag.getValue()));

    template = Template.fromStack(stack);
  }

  @AfterAll
  static void cleanup() {
    template = null;
  }
}
```
* Extend the `TemplateSupport` class in your test classes and use the `CDKStackAssert` object to verify your stack.

```java
class LambdaTest extends TemplateSupport {

  public static final String TEST = "test";

  @Test
  void should_have_lambda_function() {

    CDKStackAssert.assertThat(template)
        .containsFunction("example-lambda-function")
        .hasHandler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
        .hasCode("test-cdk-bucket", "(.*).zip")
        .hasRole("examplelambdafunctionrole(.*)")
        .hasDependency("examplelambdafunctionrole(.*)")
        .hasDependency("examplelambdafunctionroleDefaultPolicy(.*)")
        .hasTag("COST_CENTRE", TAG_VALUE_COST_CENTRE)
        .hasTag("ENV", TEST)
        .hasEnvironmentVariable("ENV", TEST)
        .hasEnvironmentVariable("SPRING_PROFILES_ACTIVE", TEST)
        .hasDescription("Lambda example")
        .hasMemorySize(512)
        .hasRuntime("provided.al2")
        .hasTimeout(3);
  }
}
```