OLE Plugin Configuration Proof-Of-Concept
==========================================

Contains a demonstration of how to implement a plugin for a Spring-configured web application.

This project includes both a complete (if tiny) Spring MVC web application and includes,
 alongside that, code in a different (Java) package that adds functionality to the base
 MVC application.  Pretend that the code and resources in these packages are in different JAR
 files.

The aim is to illustrate how, with minor modifications, medium to large web applications
could support plugin functionality, allowing different development teams to add or
modify existing functionality without having to modify a centrally managed codebase.

How It Works
------------

This project encodes a Spring MVC webapp that contains two packages: `edu.ncsu.lib` contains
code and resources that represent the base web application, along with all of the traditional configuration files in `WEB-INF`.

The Java classes and resources in the `com.example.plugin` package represent the "foreign" code
of the plugin.  Notionally, this code could be contained in a separate JAR that is dropped into
the `WEB-INF/lib` directory of the parent WAR file (this is, in fact, the intended use case).

Running the demo
----------------
`$ mvn tomcat7:run`
( browse to http://localhost:8080 )
( click on the link  )

If the subsequent page reads "you have been served (by a plugin)", and shows some sample data,
it means the demo was successful -- the page at http://localhost:8080/plugin is served by a
Spring MVC controller that was added by the `<import resources="classpath*:**/pluginConfig.xml />` element
in the MVC context's XML file (`WEB-INF/plugin-test-servlet.xml`).

Actually, other tests are taking place behind the scenes (e.g the plugin code tries to access beans defined
in the parent app's Spring context and use them; if any of this fails, the plugin will not load successfully.)

License
-------

This is a demonstration, and uses basic Spring features to achieve its goal.  It is intended to
serve as inspiration, and not as something one would directly build upon.  That said, it is provided
under the GNU GPL v3. License and is copyright 2014 North Carolina State University.
