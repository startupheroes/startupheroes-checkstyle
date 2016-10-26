# Checks for File:

1. Translation : Checks to ensure the correct translation of code by checking locale-specific resource files(like .properties files)
for consistency regarding their keys.
2. FileLength : Checks for long source files whether maximum file length is 1500 lines.
3. FileTabCharacter : Checks to see if a file contains a tab character.

# Checks for Spaces:

1. RegexpSingleline("\s+$") : Checks line has trailing spaces.
2. RegexpSingleline("^\s*(for|if)\b[^ ]") : Checks space needed before opening parenthesis after if and for.
3. RegexpSingleline("^\s*for \(.*?([^ ]:|:[^ ])") : Checks space needed around ':' character in for.

# Checks for Naming Conventions:

1. ConstantName : Checks that constant names conform to default format.(default format : "^[a-z][a-zA-Z0-9]*$")
2. LocalFinalVariableName : Checks that local final variable names conform to default format.
3. LocalVariableName : Checks that local, non-final variable names conform to default format.
4. MemberName : Checks that instance variable names conform to default format.
5. MethodName : Checks that method names conform to default format.
6. PackageName : Checks that package names conform to default format.
7. ParameterName : Checks that parameter names conform to default format.
8. StaticVariableName : Checks that static, non-final variable names conform to default format.
9. TypeName : Checks that type names conform to default format.

# Checks for imports:

1. AvoidStarImport : Check that finds import statements that use the * notation.
2. IllegalImport : Checks for imports from a set of illegal packages.
3. RedundantImport : Checks for imports that are redundant like it is a duplicate of another import.
4. UnusedImports : Checks for unused import statements.

# Checks for Size Violations:

1. LineLength : Checks for long lines. Max line length is 100.
2. MethodLength : Checks for long methods. Max line length is 200.

# Checks for Whitespace:

1. EmptyForIteratorPad: Checks the padding of an empty for iterator; that is whether a space is required at an empty for iterator.
2. GenericWhitespace: Checks that the whitespace around the Generic tokens (angle brackets) "<" and ">" are correct to the typical convention.
3. MethodParamPad: Checks the padding between the identifier of a method definition,
constructor definition, method call, or constructor invocation; and the left parenthesis of the parameter list.
4. NoWhitespaceAfter: Checks that there is no whitespace after a token.
5. NoWhitespaceBefore: Checks that there is no whitespace before a token.
6. ParenPad: Checks the padding of parentheses; that is whether a space is required after a left parenthesis and before a right parenthesis.
7. TypecastParenPad: Checks the padding of parentheses for typecasts. That is whether a space is required after a left parenthesis and before a right parenthesis
8. WhitespaceAfter: Checks that a token is followed by whitespace, with the exception that it does not check for whitespace after the semicolon of an empty for iterator.
9. WhitespaceAround: Checks that a token is surrounded by whitespace.

# Checks for Modifiers:

1. ModifierOrder : Checks that the order of modifiers conforms to the suggestions in the
  [Java Language specification, sections 8.1.1, 8.3.1 and 8.4.3](http://docs.oracle.com/javase/specs/jls/se8/html/jls-8.html)
The correct order is: public, protected, private, abstract, static, final, transient, volatile, synchronized, native, strictfp
2. RedundantModifier : Checks for redundant modifiers in interface and annotation definitions,
final modifier on methods of final classes, interface declarations that are declared as static,
non public class constructors and enum constructors, nested enum definitions that are declared static.

# Checks for Blocks:

1. AvoidNestedBlocks: Checks nested blocks.
2. EmptyBlock: Checks for empty blocks.
3. LeftCurly: Checks the placement of left curly braces on types, methods and other blocks.
4. NeedBraces: Checks for braces around code blocks.
5. RightCurly: Checks the placement of right curly braces.

# Checks for Coding problems:

1. EmptyStatement: Detects empty statements (standalone ';').
2. EqualsAvoidNull: Checks that any combination of String literals is on the left side of an equals() comparison.
3. EqualsHashCode: Checks that classes that override equals() also override hashCode().
4. IllegalInstantiation: Checks for illegal instantiations where a factory method is preferred.
5. MagicNumber: Checks that there are no a magic number that is a numeric literal that is not defined as a constant.
6. MissingSwitchDefault: Checks that switch statement has "default" clause.
7. DefaultComesLast: Check that the default is after all the cases in a switch statement.
8. SimplifyBooleanExpression: Checks for overly complicated boolean expressions.
9. SimplifyBooleanReturn: Checks for overly complicated boolean return statements.

# Checks for Class design:

1. FinalClass: Checks that class which has only private ctors is declared as final.
2. HideUtilityClassConstructor: Make sure that utility classes (classes that contain only static methods) do not have a public constructor.
3. InterfaceIsType: Use Interfaces only to define types. An interface should describe a type,
it is therefore inappropriate to define an interface that does not contain any methods but only constants.
4. VisibilityModifier: Checks visibility of class members. Only static final, immutable or annotated by specified annotation members
may be public, other class members must be private unless allowProtected/Package is set.

# Miscellaneous other checks:

1. OuterTypeFilename: Checks that the outer type name and the file name match. For example, the class Foo must be in a file named Foo.java.
2. IllegalTokenText: Checks for illegal token text to avoid using corresponding octal or Unicode escape.
3. AvoidEscapedUnicodeCharacters: Checks for escaped unicode characters.
4. OneTopLevelClass: Checks that each top-level class, interface or enum resides in a source file of its own.
5. NoLineWrap: Checks that chosen statements are not line-wrapped. By default this Check restricts wrapping import and package statements,
but it's possible to check any statement.
```
 Examples of line-wrapped statements (bad case):
 package com.puppycrawl.
     tools.checkstyle.checks;

 import com.puppycrawl.tools.
     checkstyle.api.AbstractCheck;

 Examples of not line-wrapped statements (good case):
 import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
```
6. OneStatementPerLine: Restricts the number of statements per line to one.
Rationale: It's very difficult to read multiple statements on one line.
```
 Each line causes violation:
 int var1; int var2;
 var1 = 1; var2 = 2;
 int var1 = 1; int var2 = 2;
 var1++; var2++;
 Object obj1 = new Object(); Object obj2 = new Object();
 import java.io.EOFException; import java.io.BufferedReader;
 ;; //two empty statements on the same line.

 Multi-line statements:
 int var1 = 1
 ; var2 = 2; //violation here
 int o = 1, p = 2,
 r = 5; int t; //violation here
```
7. MultipleVariableDeclarations: Checks that each variable declaration is in its own statement and on its own line.
One declaration per line is recommended since it encourages commenting. In other words,
```
int level; // indentation level
int size;  // size of table
is preferred over

int level, size;
Do not put different types on the same line. Example:
 int foo,  fooarray[]; //WRONG!
```
8. ArrayTypeStyle: Checks the style of array type definitions, default Java style is used.
```
 Some like Java-style: {@code public static void main(String[] args)}
 and some like C-style: public static void main(String args[])
```
9. FallThrough: Checks for fall through in switch statements. Finds locations where a case contains Java code,
but lacks a break, return, throw or continue statement.
10. UpperEll: Checks that long constants are defined with an upper ell, 'L'.
11. EmptyLineSeparator: Checks for empty line separators after header, package, all import declarations,
fields, constructors, methods, nested classes, static initializers and instance initializers.
12. SeparatorWrap(Dot): Checks line wrapping with separators for dot at the new line.
13. SeparatorWrap(Comma): Checks line wrapping with separators for comma at the eof.
14. NoFinalizer: Checks that no method having zero parameters is defined using the name finalize.
15. AbbreviationAsWordInName: The Check validate abbreviations(consecutive capital letters) length in identifier name,
it also allows to enforce camel case naming.
16. AnnotationLocation(Class, Interface, Enum, Method, Constructor): Not allow same line multiple annotations.
17. AnnotationLocation(Variable): Allow same line multiple annotations for variables.
18. EmptyCatchBlock: Checks for empty catch blocks.
19. CommentsIndentation: This Check controls the indentation between comments and surrounding code.
Comments are indented at the same level as the surrounding code.
20. DeclarationOrder:  * Checks that the parts of a class or interface declaration appear in the order suggested by the
  [Code Conventions for the Java Programming Language](http://www.oracle.com/technetwork/java/javase/documentation/codeconventions-141855.html#1852)

```
 - Class (static) variables. First the public class variables, then
       the protected, then package level (no access modifier), and then
       the private.
 - Instance variables. First the public class variables, then
       the protected, then package level (no access modifier), and then
       the private.
 - Constructors
 - Methods
```
21. ExplicitInitialization: Checks if any class or object member is explicitly initialized to default for its type value
(null for object references, zero for numeric types and char and false for boolean.)
22. PackageDeclaration: Ensures that a class has a package declaration, and (optionally)
whether the package name matches the directory name for the source file.


# Custom checks:

1. AntiHungarianCheck: Checks for Hungarian notations that variables starts with 'm'.
2. LoggerOrderCheck: Checks for LOGGER definitions should be at the top of the class.
3. RedundantMultipleAnnotationCheck: Checks for together usage of given set of annotations redundantly.
4. BlockedAnnotationKeyCheck: Checks to block of usage the given key inside given annotation.
5. EntityEqualsHashCodeCheck: Checks whether there is or not a default equals and hashCode methods in entity class.
6. EntityToStringCheck: Checks whether there is or not toString method in entity class.
7. EntityGettersSettersCheck: Checks if there is no getter or setter any variable of entity class.
