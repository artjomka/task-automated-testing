package lv.neueda.task.parser

import lv.neueda.task.specification.Data
import lv.neueda.task.specification.Request
import lv.neueda.task.specification.Test
import lv.neueda.task.specification.TestCase

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
        Data data = new Data(name: rootNode.@TEXT)
        rootNode.children().each { testNode ->
            Test test = new Test(name: testNode.@TEXT)
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
            data.tests.add(test)

        }

        data
    }
}