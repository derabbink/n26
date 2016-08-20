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

[1]: http://blog.palominolabs.com/2011/08/15/a-simple-java-web-stack-with-guice-jetty-jersey-and-jackson/
[2]: https://www.eclipse.org/jetty/documentation/current/runner.html

