Warning! This is a crude proof-of-concept effort by a beginner Java developer.

The basic functionality "works", but there's plenty to add/fix.

To test, open up "test.aes.txt" and paste this in as the passphrase: Iñtërnâtiônàlizætiøn 

=== annoying dependency

Until I can re-implement this with a native AES-256 library, you must have the
"Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files 6" installed in
your runtime. http://www.oracle.com/technetwork/java/javase/downloads/index.html

Place the two files "local_policy.jar" and "US_export_policy.jar" in <JRE>/lib/security
(replacing those already there). You also need to replace them in the runtime of your
JDK if debugging.