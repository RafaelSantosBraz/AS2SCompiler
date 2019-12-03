# Another Source-to-Source Compiler

Implementation of a source-to-source compiler (currently converting Java programs into C programs and vice-versa*) through the principles of a language neutral-based compilation framework.

The compiler is designed to be easily extended, so you can add support to other Imperative (Object-Oriented and Procedural) programming languages by providing Translation Schemes for them.

The complete description of the compiler, its documentation, documentation and usage instructions and examples can be found on the [project website](https://rafaelsantosbraz.github.io/AS2SCompiler/).

## Compiling the Compiler

To compile the AS2SCompiler source code, it is necessary to install and JDK v8+ (it was tested through [OpenJDK](https://openjdk.java.net/) 8) and the [ANTLR](https://www.antlr.org/) v4+.

After that, compile the _StSCompiler_ project and all its subprojects (CParser, CodeGenerator, JavaParser, Shortener, ToolPack, TranslationMapInterpreter, and eCSTAdapter).

## How to Contribute

Everyone can modify the source code. Follow those steps to do and feel free to submit your improvements:

* Fork this repository.
* Change or add code (following the coding pattern).
* Test your changes.
* Create a pull request.

### notes

*Disclaimer: It is not possible to translate the entire language so far, only a limited set of aspects are currently covered by AS2SCompiler.
