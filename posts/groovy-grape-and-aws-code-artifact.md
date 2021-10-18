---
title: Groovy Grape script to fetch a AWS Code Artifact token
summary: I find Grape really powerful to prototype.
date_published: 2021-10-18T15:17:28+01:00
keywords:groovy,aws,awscodeartifact
---

# [%title]

[Apache Groovy](https://groovy-lang.org) has a great feature called [Grape](https://docs.groovy-lang.org/latest/html/documentation/grape.html):

> Grape is a JAR dependency manager embedded into Groovy. Grape lets you quickly add maven repository dependencies to your classpath, making scripting even easier. The simplest use is as simple as adding an annotation to your script:

I recently needed a script to fetch a token to consume a [AWS Code Artifact](https://aws.amazon.com/codeartifact/) repository and save it in a `local.properties` file.

## Script 

The following script shows how easy is to do that with a Groovy Script:

_codeartifact.groovy_
```groovy
@Grab(group='software.amazon.awssdk', module='codeartifact', version='2.16.42')
@Grab(group='software.amazon.awssdk', module='sts', version='2.16.42')
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.codeartifact.CodeartifactClient
import software.amazon.awssdk.services.codeartifact.CodeartifactClientBuilder
import software.amazon.awssdk.services.codeartifact.model.GetAuthorizationTokenRequest
import software.amazon.awssdk.services.codeartifact.model.GetAuthorizationTokenResponse

String awsCodeArtifactRegion = 'eu-central-1'
String awsCodeArtifactDomain = 'XXX'
String awsCodeArtifactOwner = 'YYY'
String awsCodeArtifactRepository = 'libs'

class Token {
    String token
    Long expiration
}
class TokenRequest {
    String owner

    String domain

    String profile

    String region

    Integer durationSeconds
}
class TokenFetcher {
    static Token fetchToken(TokenRequest tokenRequest) {
        CodeartifactClient client = instantiateClient(tokenRequest)
        GetAuthorizationTokenRequest request = authorizationTokenRequest(tokenRequest)
        GetAuthorizationTokenResponse rsp = client.getAuthorizationToken(request)
        new Token(token: rsp.authorizationToken(), expiration: rsp.expiration().toEpochMilli())
    }
    private static GetAuthorizationTokenRequest authorizationTokenRequest(TokenRequest tokenRequest) {
        GetAuthorizationTokenRequest.Builder tokenRequestBuilder = GetAuthorizationTokenRequest.builder()
        if (tokenRequest.durationSeconds) {
            tokenRequestBuilder = tokenRequestBuilder.durationSeconds(tokenRequest.durationSeconds)
        }
        tokenRequestBuilder.domain(tokenRequest.domain)
        tokenRequestBuilder.domainOwner(tokenRequest.owner)

        (GetAuthorizationTokenRequest) tokenRequestBuilder.build()
    }
    private static CodeartifactClient instantiateClient(TokenRequest tokenRequest) {
        CodeartifactClientBuilder builder = CodeartifactClient.builder()
        if (tokenRequest.region) {
            builder = builder.region(Region.of(tokenRequest.region))
        }
        if (tokenRequest.profile) {
            builder = builder.credentialsProvider(ProfileCredentialsProvider.create(tokenRequest.profile))
        }
        builder.build()
    }
}
TokenRequest tokenRequest = new TokenRequest()
tokenRequest.owner = awsCodeArtifactOwner
tokenRequest.domain = awsCodeArtifactDomain
tokenRequest.durationSeconds = 43200L
tokenRequest.region = awsCodeArtifactRegion
Token token = TokenFetcher.fetchToken(tokenRequest)
println """\
{
    "token": "${token.token}",
    "expiration": "${token.expiration}",
}
"""
Properties properties = new Properties()
properties.setProperty("expiration","" + token.expiration)
properties.setProperty("codeartifactToken", token.token)
File outputFile = new File('local.properties')
if (!outputFile.exists()) {
    outputFile.createNewFile()
}
properties.store(outputFile.newOutputStream(), "")
println "saved token to local.properties"
```

## Run 

To execute the script: 

- Edit `codeartifact.groovy` and enter your AWS Code Artifact region, domain, owner and lib parameters at the top of the file. 
- Run `aws configure` and authenticate with a user with IAM access to AWS Code Artifact.
- Run `groovy codeartifact.groovy` and obtain a token. 

We needed a script instead of using the AWS CLI always, because in some environments we did not have the AWS CLI installed and we only had a AWS Secret Key and Access key ID exposed as environment variables.