# description

Since `1.3.0.Final` calling `DriverManager.getConnection()` in a test from a Jupiter Extension in combination with `@QuarkusTest`
will throw an exception

# steps to reproduce

Clone this repo

     $ git clone git@github.com:tgippi/quarkus-liquibase-issue.git
      
Run the tests

    $ mvn clean test

And, then, see the stacktrace
<pre>
 SQL No suitable driver found for jdbc:h2:mem:test;DB_CLOSE_DELAY=-1...
</pre>

Set the quarkus version   
`<quarkus.version>1.3.0.Final</quarkus.version>`   
to   
`<quarkus.version>1.2.1.Final</quarkus.version>`

Run the tests again

    $ mvn clean test

And then, see that it works

---
It looks like a class loading issue. The `java.sql.DriverManager` contains the driver `org.h2.Driver` but the
following code in the `isDriverAllowed()` method returns `false`

<pre>
result = aClass == driver.getClass();
</pre>

While debugging I saw that `aClass.getClassLoader()` returns `ClassLoader$AppClassLoader`  
And `driver.getClass().getClassLoader()` returns `QuarkusClassLoader`
