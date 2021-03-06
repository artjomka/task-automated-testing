package lv.neueda.homework.service

import spock.lang.Shared
import spock.lang.Specification

class SpecByMindMapTest extends Specification {

    @Shared
    private Spec specificationParse = new SpecByMindMap()

    def "No resource found "() {
        when:
        specificationParse.parseSpecification("nofile")
        then:
        AssertionError exception = thrown()
        exception.message.contains "No resource with name nofile found"
    }

    def "Read specification from mind map file"() {
        setup:
        def config = new ConfigSlurper().parse(new File('src/test/resources/test.properties').toURI().toURL())

        def result = specificationParse.parseSpecification(config.get("mindMapFile"))

        when:
        result
        then:
        result != null
        result.name == "Calculator tests"
        result.testSuites != null
        result.testSuites.size() == 4

        when:
        def addTest = result.testSuites.find { test -> test.name == "Add" }
        def subtractTest = result.testSuites.find { test -> test.name == "Subtract" }
        def multiplyTest = result.testSuites.find { test -> test.name == "Multiply" }
        def divideTest = result.testSuites.find { test -> test.name == "Divide" }

        then:
        addTest != null
        addTest.testCases != null
        addTest.testCases.size() == 2
        addTest.request != null
        addTest.request.method == "POST"
        addTest.request.path == "/rest/add"

        subtractTest != null
        subtractTest.testCases != null
        subtractTest.testCases.size() == 2
        subtractTest.request.method == "POST"
        subtractTest.request.path == "/rest/subtract"

        multiplyTest != null
        multiplyTest.testCases != null
        multiplyTest.testCases.size() == 2
        multiplyTest.request.method == "POST"
        multiplyTest.request.path == "/rest/multiply"

        divideTest != null
        divideTest.testCases != null
        divideTest.testCases.size() == 3
        divideTest.request.method == "POST"
        divideTest.request.path == "/rest/divide"

        when:
        def addTestCaseOne = addTest.testCases.find { testCase -> testCase.name == "simple addition" }
        def addTestCaseTwo = addTest.testCases.find { testCase -> testCase.name == "adding a negative number" }

        then:
        addTestCaseOne.variables.size() == 2
        "variableOne" in addTestCaseOne.variables.keySet()
        "variableTwo" in addTestCaseOne.variables.keySet()
        "6" in addTestCaseOne.variables.values()
        "8" in addTestCaseOne.variables.values()
        addTestCaseOne.result == "14"

        addTestCaseTwo.variables.size() == 2
        "variableOne" in addTestCaseTwo.variables.keySet()
        "variableTwo" in addTestCaseTwo.variables.keySet()
        "-5.34" in addTestCaseTwo.variables.values()
        "3.95" in addTestCaseTwo.variables.values()
        addTestCaseTwo.result == "-1.39"

    }

}