# NUMBER26 Code Challenge
This project uses Java 8 and Gradle. You will need these to compile the project.

This project contains a servlet that hosts a simple RESTful JSON API. For this, I'm mainly using Guice, Jersey, Jackson and Dropwizard's Metrics. Some of this structure was inspired by [this post][1].

## Directory Structure
You can think of each nested Gradle project as a Maven module.

* `n26-code-challenge` wrapper to hold everything together and declare common dependencies
  * `api` API endpoints (you could add another project like this for e.g. a web UI)
  * `common` Shared logic (e.g. Jersey AOP, 
  * `service` Business logic, data storage
  * `webapp` Servlet definition

## Logical/Dependency Structure
Nodes in this tree-like list are dependencies of their parents. Since `webapp` is the entry point to the entire web app, this is the root of the tree.

* `webapp`
  * `api`
    * `common`
    * `service`
      * `common`
  * `common`

## Launching the Project
Perhaps the two simplest options are **using Gradle** and **running it standalone**.

For the first option, you can run `gradle jettyRunWar` (which is the same as what `./run.sh` does). This is very convenient, as this will take care of (re)compiling everything before launching Jetty with the `webapp` war.
One disadvantage is that the Jetty Plugin for Gradle doesn't handle logging very well, which means you don't get any logging from your application out of SLF4J.

In order to run it standalone, you should download the [Jetty Runner][2]. Next, you need to compile the code by running `gradle war`.
Assuming you named the runner `jetty-runner.jar` and stored it next to the `n26-code-challenge/` directory, you can now launch the app by running `java -jar jetty-runner.jar n26-code-challenge/webapp/build/libs/webapp-1.0.war`

Once your servlet container is running, you should be able to reach it at `http://localhost:8080/`
You can inspect the API's performance (HTTP status code rates, response time) using `jconsole`. Look for the data in the `metrics` MBean.

[1]: http://blog.palominolabs.com/2011/08/15/a-simple-java-web-stack-with-guice-jetty-jersey-and-jackson/
[2]: https://www.eclipse.org/jetty/documentation/current/runner.html

## Testing
There are unit tests and integration tests in the `service` module. These tests make sure the business logic is correct.
For manual acceptance testing, I provided a `curl-commands.txt` file outlining some of the operations you can do with this API.

## Storage
Given the explicit ban of SQL, I set aside my fondness of Flyway and jOOQ. Instead, I now created an in-memory storage system using `HashMap`s and `HashMultimap`s, since those could be translated into a NoSQL or RDBMS persistence solution relatively easily.

If SQL hadn't been disallowed, I would have created a storage system using the following:
* A schema file written in SQL
* H2 and Flyway, to read the schema into memory
* jOOQ to generate code from the H2 DB
* jOOQ-generated classes to read & write data at runtime

## Complexity
Reading anything from the API takes `O(1)` time. Adding or updating (i.e. `PUT`ting) transactions takes `O(k)` time, where `k` is the number of transactions (transitively) connected to the new/updated one via its `parent_id`. This complexity arises from the fact that on every such operation, the sums of the transaction subtree are being updated. The added benefit of this is that reading takes `O(1)` time. My decision to make reading the computationally cheapest operation is based on my personal experience that read operations happen far more frequently than write operations (in the context of HTTP APIs).
Storing all data takes `O(n)` space, where `n` is the total number of stored transactions. This is the most efficient solution.

More details on this can be found in the comments in the `TransactionStoreImpl` and `TypeStoreImpl` classes, in the `com.abbink.n26.service.storage` package.
