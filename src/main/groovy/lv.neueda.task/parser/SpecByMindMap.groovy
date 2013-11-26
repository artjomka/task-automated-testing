package lv.neueda.task.parser

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
            TestSuite test = new TestSuite(name: testNode.@TEXT)
            def requestNode = testNode.find { node -> node.@TEXT.tokenize(':')[0] == "Request" }
            assert requestNode != null: "request node can't be null"
            Request request = new Request(method: requestNode.node[0].@TEXT.tokenize(':')[1].trim(), path: requestNode.node[1].@TEXT.tokenize(':')[1].trim())
            test.request = request
            def testCases = testNode.children().minus(requestNode)
            testCases.each { testCase ->
                def variables = [:]
                variables.put(testCase?.node[0].@TEXT.tokenize(":")[0].trim(), testCase.node[0].@TEXT.tokenize(":")[1].trim())
                variables.put(testCase?.node[1].@TEXT.tokenize(":")[0].trim(), testCase.node[1].@TEXT.tokenize(":")[1].trim())
                test.testCases.add(new TestCase(name: testCase.@TEXT.tokenize(':')[1].trim(),
                        variables: variables,
                        result: testCase.node[2].@TEXT.tokenize(":")[1].trim()))
            }
            data.testSuites.add(test)

        }

        data
    }
}
