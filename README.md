# expression-utils-java
Java utility library (with static methods) to parse array-based nested expressions.

[![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/ekoindia/expression-utils-java)](https://github.com/orgs/ekoindia/packages?repo_name=expression-utils-java)
[![GitHub Workflow Status (with branch)](https://img.shields.io/github/actions/workflow/status/ekoindia/expression-utils-java/gradle-cicd.yml)](/)
[![GitHub issues](https://img.shields.io/github/issues/ekoindia/aeps-gateway-lib)](https://github.com/ekoindia/aeps-gateway-lib/issues)
<a href="https://eko.in" target="_blank">![Eko.in](https://img.shields.io/badge/Develop%20with-Eko.in-brightgreen)</a>
<a href="https://twitter.com/intent/tweet?text=Wow:&url=https%3A%2F%2Fgithub.com%2Fekoindia%2Faeps-gateway-lib" target="_blank"><img alt="Twitter" src="https://img.shields.io/twitter/url?style=social&url=https%3A%2F%2Fgithub.com%2Fekoindia%2Faeps-gateway-lib"></a>
<a href="https://twitter.com/intent/follow?screen_name=ekospeaks" target="_blank">![Twitter Follow](https://img.shields.io/twitter/follow/ekospeaks?label=Follow&style=social)</a>
<a href="http://dsc.gg/ekodevs" target="_blank">![Discord Chat](https://img.shields.io/discord/1038006952573292574)</a>

---

## Installation
- In the [Packages](https://github.com/orgs/ekoindia/packages?repo_name=expression-utils-java) section, select the package `expression-utils-java.lib`
- Follow the installation instructions
- Or, download the library jar file and include in your Java project

## Usage

### Example 
```java
import in.eko.exprutils.ExpressionParser;

// Single expression example: 2 + 3
(int)ExpressionParser.parseExpression("['+', 2, 3]");  // returns 5

// Nested expression example: 2 + (3 * 4)
(int)ExpressionParser.parseExpression("['+', 2, ['*', 3, 4]]");  // returns 14
```

> Note: see the [test cases](lib/src/test/java/in/eko/exprutils/) for detailed examples.


### Available functions:

| Class            | Method            | Inputs                                                  | Output                | Desc                                                                             |
|------------------|-------------------|---------------------------------------------------------|-----------------------|----------------------------------------------------------------------------------|
| ExpressionParser | parseExpression() | expression (string encoded Json array)                  | parsed value (Object) | returns parses an expression                                                     |
|                  | isValidOperator() | String operator                                         | boolean               | Checks if the provided operator is supported by the expressionParser             |
|                  | interpolate()     | String expr, Map<String, String> data                   | interpolated string   | Interpolate/replace values of dollar-curly-brace-wrapped variables into a string |
| Hash             | getHash()         | String input, String type                               | hash                  | Generate hash of a string using the `type` algorithm (MD5, SHA-256, etc)         |
|                  | sha256()          | String input                                            | sha-256 hash          | Generate SHA-256 hash of a string                                                |
|                  | sha512()          | String input                                            | sha-512 hash          | Generate SHA-512 hash of a string                                                |
|                  | md5()             | String input                                            | md5 hash              | Generate md5 hash of a string                                                    |
| JsonObj          | get()             | String/JSONObject obj, String key, String default_value | value (Object)        | Retrieve deep/nested value from a JSON Object                                    |
|                  | set()             | String obj, String key, Object value                    | object (String)       | Set a key-value pair deep within a JSON Object                                   |
| JWT              | generate()        | ...                                                     | generated token       | Generate JWT (token) with the given configuration                                |
|                  | parse()           | ...                                                     | claim map             | Validates & parses a JWT (token)                                                 |

### Operators supported by the parseExpression() function:
| Operator | Purpose                      | Expression Example                                       | Result              |
|----------|------------------------------|----------------------------------------------------------|---------------------|
| +        | Add                          | ['+', 2, 3]                                              | 5                   |
| -        | Subtract                     | ['-', 3, 2]                                              | 1                   |
| &ast;    | Multiply                     | ['&ast;', 2, 3]                                          | 6                   |
| /        | Divide                       | ['/', 4, 2]                                              | 2                   |
| CONCAT   | Concatenate string           | ['CONCAT', 'Hello', 'World']                             | HelloWorld          |
| GET      | Get nested-value from object | ['GET', {'b':{'d':{'e':3}}}, 'b.d.e']                    | 3                   |
| SET      | Set nested-value into object | ['SET', {'b':{'d':{}}}, 'b.d.e', 4]                      | {'b':{'d':{'e':4}}} |
| SHA256   | Get sha-256 hash             | ['SHA256', 'hello world']                                | b94d27...cde9       |
| SHA512   | Get sha-512 hash             | ['SHA512', 'hello world']                                | 309ecc48...cd76f    |
| MD5      | Get MD5 hash                 | ['MD5', 'hello world']                                   | 5eb63...5acdc3      |
| JWT      | Generate JWT                 | ['JWT', '&lt;secret-key&gt;', 'HS256', '{"issuer":...}'] | generated token     | 


## Contribution Guide

### Local Test
- ` ./gradlew test --info `

### Local Build
- `./gradlew build`

### Publish New Release
- Merge changes into the main branch
- Goto the [Releases](/releases) section and create a new release
- A new build will automatically be created and published to [Github Packages](https://github.com/orgs/ekoindia/packages?repo_name=expression-utils-java)
