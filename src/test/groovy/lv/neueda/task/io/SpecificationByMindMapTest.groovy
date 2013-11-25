package lv.neueda.task.io

import spock.lang.Specification



class SpecificationByMindMapTest extends Specification {


    def "File not found"(){
        Spec specificationParse = new SpecByMindMap()

        def result = specificationParse.parseSpecification('abr')
        when:
        result
        then:
        thrown AssertionError
    }

    def "Read specification from mind map file"() {
        Spec specificationParse = new SpecByMindMap()

        def result = specificationParse.parseSpecification('C:\\Users\\artjom\\IdeaProjects\\task-automated-testing\\src\\main\\resources\\mindmap.xml')
        when:
        result
        then:
        result != null
        result.name == "Calculator tests"
        result.tests != null
        result.tests.size() == 4

        def addTest = result.tests.find{test -> test.name == "Add"}
        def subtractTest = result.tests.find{test -> test.name == "Subtract"}
        def multiplyTest = result.tests.find{test -> test.name == "Multiply"}
        def divideTest = result.tests.find{test -> test.name == "Divide"}

        expect:
        addTest != null
        addTest.testInfos != null
        addTest.testInfos.size() == 2
        addTest.request != null
        addTest.request.method == "POST"
        addTest.request.path == "/rest/add"

        subtractTest != null
        subtractTest.testInfos != null
        subtractTest.testInfos.size() == 2
        subtractTest.request.method == "POST"
        subtractTest.request.path == "/rest/subtract"

        multiplyTest != null
        multiplyTest.testInfos != null
        multiplyTest.testInfos.size() == 2
        multiplyTest.request.method == "POST"
        multiplyTest.request.path == "/rest/multiply"

        divideTest != null
        divideTest.testInfos != null
        divideTest.testInfos.size() == 3
        divideTest.request.method == "POST"
        divideTest.request.path == "/rest/divide"




    }



    def "Apply closure action variables"(def variableOne, def variableTwo, def result, def action) {

        expect:
        action(variableOne, variableTwo) == result

        where:
        variableOne | variableTwo | result | action
        5           | 9           | 45     | { x, y -> return x * y }
        -2.3        | -6.76       | 15.548 | { x, y -> return x * y }
        5           | 2           | 2.5    | { x, y -> return x / y }
        3           | 4           | 7      | { x, y -> return x + y }
    }


}