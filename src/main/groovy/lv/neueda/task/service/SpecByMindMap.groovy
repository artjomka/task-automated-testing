package lv.neueda.task.service
import lv.neueda.task.Constants
import lv.neueda.task.specification.Request
import lv.neueda.task.specification.SpecData
import lv.neueda.task.specification.TestCase
import lv.neueda.task.specification.TestSuite

class SpecByMindMap implements Spec {


    @Override
    def parseSpecification(specificationPath) {
        File file = new File(specificationPath)

        assert file.exists(): "File ${file} not found"
        assert file.isFile(): "File ${file} is not file"

        def rootNode = new XmlParser().parse(file)
        getSpecificationData(rootNode.node[0])
    }

    def getSpecificationData(Node rootNode) {
        SpecData data = new SpecData(name: rootNode.@TEXT)
        rootNode.children().each { testNode ->
            TestSuite testSuite = new TestSuite(name: testNode.@TEXT)
            def requestNode = testNode.find { node -> node.@TEXT.tokenize(Constants.SEPARATOR)[0] == Constants.REQUEST }
            assert requestNode != null: "request node can't be null"
            Request request = new Request(method: requestNode.node[0].@TEXT.tokenize(Constants.SEPARATOR)[1].trim(),
                    path: requestNode.node[1].@TEXT.tokenize(Constants.SEPARATOR)[1].trim())
            testSuite.request = request
            def testCases = testNode.children().minus(requestNode)
            assert testCases.size() > 0: "No test cases found for test suite ${testSuite.name}"
            testCases.each { testCase ->
                assert testCase != null: "Test case cant be null"
                def variables = [:]
                variables.put(testCase.node[0].@TEXT.tokenize(Constants.SEPARATOR)[0].trim(), testCase.node[0].@TEXT.tokenize(Constants.SEPARATOR)[1].trim())
                variables.put(testCase.node[1].@TEXT.tokenize(Constants.SEPARATOR)[0].trim(), testCase.node[1].@TEXT.tokenize(Constants.SEPARATOR)[1].trim())
                testSuite.testCases.add(new TestCase(name: testCase.@TEXT.tokenize(Constants.SEPARATOR)[1].trim(),
                        variables: variables,
                        result: testCase.node[2].@TEXT.tokenize(Constants.SEPARATOR)[1].trim()))
            }
            data.testSuites.add(testSuite)

        }

        data
    }
}
