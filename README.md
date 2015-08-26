Automate end to end for given walmart.com site
==========================================

This is the repository for Automating the End to end implementation

     Technical choices 									==> Java ,Selenium Chrome browser ,TestNG
     Reasoning behind your technical choices		==> I am very much hands on with Java programming and have used Selenium in my previous project for automating the UI functionality. TestNg is standard test framework with lots of testing functionality.
     Trade offs												==> Input for this test case is taken from the property file (input.properties).This test case runs sequentially  for each item provided in the input.properties file.Asserted each scenarios and also i have given a system.out which will be clear as log. Since i did not use logging i have choose to console output.
    Additional things could have be taken care of	==> Can think of a way to execute the test cases Parallel. Assert and provide some alternate way to continue the test case if there are more than one item then remove the additional items and continue with the test case. Can have more than given scenarios with additional amount of time.Can do more about Timing issues.