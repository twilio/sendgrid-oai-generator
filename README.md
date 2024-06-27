# OpenAPI Generator for Sendgrid helper libraries

## Overview
This is a boiler-plate project to generate your own project derived from an OpenAPI specification.

Its goal is to get you started with the basic plumbing, so you can put in your own logic. It won't work without your changes applied. Continue reading this doc to get more details on how to do that.

## What's OpenAPI
The goal of OpenAPI is to define a standard, language-agnostic interface to REST APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection.

When properly described with OpenAPI, a consumer can understand and interact with the remote service with a minimal amount of implementation logic. Similar to what interfaces have done for lower-level programming, OpenAPI removes the guesswork in calling the service.

Check out [OpenAPI-Spec](https://github.com/OAI/OpenAPI-Specification) for additional information about the OpenAPI project, including additional libraries with support for other languages and more.

## How do I use this?
`Note: The instructions below will generate the sendgrid-java library.`

Clone this repo into your local machine. It will include:

```
.
|- README.md    // this file
|- pom.xml      // build script
|-- src
|--- main
|---- java
|----- com.sendgrid.oai.java.SendgridJavaGenerator.java // generator file
|---- resources
|----- sendgrid-java // template files
|----- META-INF
|------ services
|------- org.openapitools.codegen.CodegenConfig
|-- scripts
|--- build_sendgrid_library.py // script to OpenAPI Generator
|--- __init__.py
|--- clean_unused_imports.py // removes unused imports from generated files
|--- generate.sh
|--- prism.sh
```

You _will_ need to make changes in at least the following:

`SendgridJavaGenerator.java`

Templates in this folder:

`src/main/resources/sendgrid-java`

Once modified, you can run this:

```
mvn package
```

In your generator project. A single jar file will be produced in `target`. You can now use that with [OpenAPI Generator](https://openapi-generator.tech):

### For mac/linux:
```
java -cp /path/to/openapi-generator-cli.jar:/path/to/your.jar org.openapitools.codegen.OpenAPIGenerator generate -g sendgrid-java -i /path/to/openapi.yaml -o ./test
```

(Do not forget to replace the values `/path/to/openapi-generator-cli.jar`, `/path/to/your.jar` and `/path/to/openapi.yaml` in the previous command)

Here is an example script to generate [sendgrid-java](https://github.com/sendgrid/sendgrid-java) from our [OpenAPI specification](https://github.com/twilio/sendgrid-oai): [build_sendgrid_library.py](./scripts/build_sendgrid_library.py).

### For Windows
You will need to use `;` instead of `:` in the classpath, e.g.
```
java -cp /path/to/openapi-generator-cli.jar;/path/to/your.jar org.openapitools.codegen.OpenAPIGenerator generate -g sendgrid-java -i /path/to/openapi.yaml -o ./test
```

Now your templates are available to the client generator, and you can write output values.

## But how do I modify this?
The `SendgridJavaGenerator.java` has comments in it--lots of comments.  There is no good substitute for reading the code more, though.  See how the `SendgridJavaGenerator` implements `CodegenConfig`. That class has the signature of all values that can be overridden.

You can also step through SendgridJavaGenerator.java in a debugger.  Just debug the JUnit test in DebugCodegenLauncher. That runs the command line tool and lets you inspect what the code is doing.

For the templates themselves, you have a number of values available to you for generation. You can execute the `java` command from above while passing different debug flags to show the object you have available during client generation:

```
# The following additional debug options are available for all codegen targets:
# -DdebugOpenAPI prints the OpenAPI Specification as interpreted by the codegen
# -DdebugModels prints models passed to the template engine
# -DdebugOperations prints operations passed to the template engine
# -DdebugSupportingFiles prints additional data passed to the template engine

java -DdebugOperations -cp /path/to/openapi-generator-cli.jar:/path/to/your.jar org.openapitools.codegen.OpenAPIGenerator generate -g sendgrid-java -i /path/to/openapi.yaml -o ./test
```

Will, for example, output the debug info for operations.
You can use this info in the `api.mustache` file.

## Generating sendgrid-java

To generate [`sendgrid-java`](https://github.com/sendgrid/sendgrid-java) from [`sendgrid-oai`](https://github.com/twilio/sendgrid-oai)

### Setup

1. Clone this repo
2. Clone [sendgrid-oai](https://github.com/twilio/sendgrid-oai)
3. Clone [sendgrid-java](https://github.com/sendgrid/sendgrid-java)
4. Navigate to your local `sendgrid-oai-generator` and run `make install`

### Code Generation

Update `<path to>` and execute the following from the root of this repo:

* To generate the entire suite, run `make install && python3 scripts/build_sendgrid_library.py <path to>/sendgrid-oai/spec/yaml <path to>/sendgrid-java`
* To generate the provider for a single domain such as studio, run `make install && python3 examples/build_sendgrid_library.py <path to>/sendgrid-oai/spec/yaml/tsg_alerts_v3.yaml <path to>/sendgrid-java`
