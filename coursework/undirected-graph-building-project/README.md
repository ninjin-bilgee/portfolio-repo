# Prereqs
1. Java (same version as in pom.xml)
2. Maven (`mvn -v` to verify)

I recommend using Intellij IDEA for easiest setup.

# Common Commands
- Build: `mvn clean compile`
- Run the app: `mvn -q compile` followed by `java -cp target/classes edu.cwru.csds293.Main`
- Run tests (without jacoco): `mvn test`
- Coverage with jacoco: `mvn verify`
- Package into jar: `mvn package`
- Clean up build artifacts: `mvn clean`
- Format everything `mvn spotless:apply`
- Verify formatting `mvn spotless:check`