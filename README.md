# Another Source-to-Source Compiler

Implementation of a source-to-source compiler (currently converting Java programs into C programs and vice-versa*) through the principles of a language neutral-based compilation framework.

The compiler is designed to be easily extended, so you can add support to other Imperative (Object-Oriented and Procedural) programming languages by providing Translation Schemes for them.

The complete description of the compiler, its documentation, documentation and usage instructions and examples can be found on the [project website](https://rafaelsantosbraz.github.io/AS2SCompiler/).

Download the executable jar [here](https://github.com/RafaelSantosBraz/AS2SCompiler/releases).

## Compiling the Compiler

To compile the AS2SCompiler source code, download those awesome open-source projects:

* JDK v11+ (it was tested through [OpenJDK](https://openjdk.java.net/) 11.0.8)
* [Maven](https://maven.apache.org/)

After that, enter the _AS2SCompiler_ directory and execute the following command:

```sh
$ mvn clean package
```

The compiler will be in the _target_ directory.

## How to Contribute

Everyone can modify the source code. Follow those steps to do and feel free to submit your improvements:

* Fork this repository.
* Change or add code.
* Create a pull request.

### notes

*Disclaimer: It is not possible to translate the entire language so far, only a limited set of aspects are currently covered by AS2SCompiler.
