# CodeGenerator
Generate code for server and client sides

# Config
	applicationId "stan.code.generator"
	versionCode 1611200020
	versionName "0.002"

# Build information
## build
```
javac -sourcepath ./src/main/java -d bin -classpath lib/* ./src/main/java/stan/code/generator/Main.java
```

## run
```
java -classpath lib/*;bin stan.code.generator.Main
```
