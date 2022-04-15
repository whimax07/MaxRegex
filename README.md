# MaxRegex
My attempted to write the start of a regex system. Prompted by a Q on LeetCode. It is also to help understand compilers a bit more.

### How To Run
To run simple regex use [SimpleRegex](src/regex/SimpleRegex.java).
To run the more elaborate version of regex use the api in [MaxRegex](src/regex/MaxRegex.java).

### Example
Simple Regex
```java
String testString = "AABBCC";
String pattern = "AAB+C.";
SimpleRegex regex = new SimpleRegex(testString, pattern);

// 'matches' will be true.
boolean matches = regex.matches();
```

Elaborate Regex
```java
String testString = "AABBCC";
String pattern = "AAB+C.";

// 'matches' will be true.
boolean matches = MaxRegex.whollyContainedIn(pattern, testString);
```
