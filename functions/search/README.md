# aws-smf/functions/search
Lambda function to search for an instrument in the backend ElasticSearch database
- Get method: search for a single instrument based on the id (symbol)
- Post method: search for instruments using an elacticSearch query passed in the body
- Other methods: error

## Build
* generates the target/search.jar package (without sending it to aws S3 and nor deploy it)
> mvn clean package

## Test
(https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-cli-command-reference-sam-local-start-lambda.html)
* starts the Search function locally (url: http://127.0.0.1:3001/ ) :
> sam local start-lambda [--template template.yaml]
* execute aws cli command:
> aws lambda invoke --function-name "SearchFunction" --endpoint-url "http://127.0.0.1:3001" --no-verify-ssl /tmp/out.txt
* starts locally the SearchFunction with the api gateway in front (url: http://127.0.0.1:3000/smf/instrument ) : 
> sam local start-api [--template-name template.yaml]

## Maven Integration tests
* Lambda integration tests unsing aws java core generic lambda client
* API Gateway integration tests using REST-assert (https://phauer.com/2016/testing-restful-services-java-best-practices/)
* starts a local instance of the lambdas and execute integration tests on it
> mvn integration-test
* next step: start an instance of the api gateway and do integration tests on lambda (direct access) and api gateway
