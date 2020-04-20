package com.example.JUnit5Demo

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//  annotate this, to tell the JUnit to make the test instance creation differently,
//  this takes an "enum" as it's value i.e inside (),
//  PER_CLASS - a new instance will be created for each test method, test factory method, or test template method
//  PER_METHOD - this is the default/i.e this is same as not putting the annotation at all
//             - a new instance will be created once per test class

//  JUnit creates instance of the class -> run BeforeAll -> run BeforeEach on the same instance
//  so, here we've the same lifecycle hook running in the same order, but on the same instance,
//  rather than having to create new instances every time
class MathUtilsTest(var testInfo: TestInfo, var testReporter: TestReporter) {

    val mathUtils = MathUtils()


    @BeforeAll
    fun beforeAllInit(){
        println("Before all...")
    }
//    this doesn't work because, this needs to run before all, i.e before the creation of object of MathUtils,
//    which is particularly not possible, so we need to make the method static, to overcome this.
//    But, in this case, hence Kotlin doesn't support static keyword,
//    if we annotate the testClass with @TestInstance(TestInstance.Lifecycle.PER_CLASS),
//    then we don't need to make this method static.


    @BeforeEach //  runs before each test method
    fun init(testInfo: TestInfo, testReporter: TestReporter){
        this.testInfo = testInfo
        this.testReporter = testReporter
        var mathUtils = MathUtils()

        testReporter.publishEntry("Running " + testInfo.displayName + " with tags ${testInfo.tags}")
    }

    @AfterEach  //  runs after each test method
    fun cleanup(){
        println("Cleaning up...")
    }

    @Test
//    @EnabledOnOs(OS.LINUX)  //  this will only run on LINUX OS
    @DisplayName("Testing add method")  //  makes this passed value as the name of the Method during testing
    fun testAdd(): Unit{
//        println("Test ran successfully!") //  we don't want to call the print method again again, it slows down

//        var mathUtils = MathUtils() //  We’ve to create an instance of the class here
                                //  but, it's not required for static classes
        var expected: Int = 2
        var actual: Int = mathUtils.add(1,1)

        var isServerUp: Boolean = true //  here, for eg: for this test to run, we need the server to run,
                                        // otherwise don't run this test
        assumeTrue(isServerUp)  //  so, we're basically telling JUnit that, when we're running this,
                                // we should assume it to be true while running the test
//        if this assumptions turns out to be incorrect, don't run this test, skip it
//        similar to enable/disable but, this gives programmatic control

        assertEquals(expected, actual, {"The add method should add two numbers!" })
        //  this 3rd arg i.e message is added to make marking easy and make the test results noticeable.
        //  But, this message is shown only when the test fails, now hence assertEquals is a function,
        //  these 3 params are sent to the function, so eventually assertEquals will take these params and calculate stuff,
        //  hence this turns to be costly and time taking.
        //  So, by making this a lambda expression, this will only be sent to the function, when the test fails.
    }

    @Test
    @Disabled    //  this test fails normally because we've made this to by using "fail",
                // but if we use @Disabled annotation, the test runner will skip this method i.e will not run this method.
    @DisplayName("TestDriven Development")
    fun testDisabled(){
        fail<String>("This test should be disabled")
    }

    @Test
    @DisplayName("Testing Exception Handling")
    fun testDiv(){
                assertThrows(ArithmeticException::class.java, { mathUtils.div(1,0) } , "Divide by zero should throw!")
        // this test fails for 2 reasons:
        //  1. if the exception doesn't throw at all
        //  2. if wrong exception is thrown
    }

    @Nested
    @DisplayName("add method")
    inner class AddTest{
        //  the mathUtil i.e defined initially at the start of the main class,
        //  will not be working, by making this class -> inner class
        //  we've made this inner class, so we don't need to again define the mathUtils inside this class

        //  if, any one of the method fails, this class fails the test altogether

        @Test
        @DisplayName("when adding two positive numbers")
        fun testAddPositive(){
//            val mathUtils = MathUtils()
            assertEquals(2, mathUtils.add(1,1))
        }

        @Test
        @DisplayName("when adding two negative numbers")
        fun testAddNegative(){
//            val mathUtils = MathUtils()
            assertEquals(-2, mathUtils.add(-1,-1))
        }
    }

    @Test
    @DisplayName("multiply method")
    @Tag("Math")
    fun testMultiply(){
        println("Running " + testInfo.displayName + " with tags ${testInfo.tags}")
        assertAll(
                { assertEquals(4, mathUtils.multiply(2,2))},
                { assertEquals(0, mathUtils.multiply(2,0))},
                { assertEquals(-2, mathUtils.multiply(2,-1))}
        )
    }

    @RepeatedTest(3)    //  this annotation will repeat the test 5 times
    @DisplayName("circle area method")
    @Tag("Circle")
    fun testComputeCircleArea(repetitionInfo: RepetitionInfo){
        //  the repetitionInfo argument, is passed every time, this method is run
        //  this will contain information about the repetition
        repetitionInfo.currentRepetition
        repetitionInfo.totalRepetitions // these two methods are available in the RepetitionInfo lib,
                                        //  we can make use of these two, to write logic
        assertEquals(12.566370614359172, mathUtils.computeCircleArea(2))
    }
}