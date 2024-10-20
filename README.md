## Modular `module-info`

Project that offers annotations like `@ModuleInfo` & co. and an annotation processor that collects all
such annotations and generates `module-info.class`. This is useful when:
- monolith `module-info.java` is hard to maintain
- many individual classes want to contribute to a single `module-info`
- one targets older JDK than JDK9, but wants to be good modular citizen

### Try it!

```bash
# compile all projects and run all tests
$ mvm clean install
# verify `module-info.class` is properly generated
$ jar --describe-module -f tests/target/*.jar
org.apidesign.modular.tests jar:file:///ModularModuleInfo/tests/target/tests-1.0-SNAPSHOT.jar!/module-info.class
requires java.base
```

The `tests` project is using `@ModuleInfo` annotation and the _modular module info annotation processor_ 
uses that info and generates proper `module-info.class` that properly describes the module.
