package lv.neueda.task

import groovyx.net.http.ContentType
import groovyx.net.http.Method
import groovyx.net.http.RESTClient
import lv.neueda.task.parser.Spec
import lv.neueda.task.parser.SpecByMindMap

class Main {
    static main(args) {
        assert args.size() == 0: "Please provide full path to mind map file "
        Spec spec = new SpecByMindMap()
        def data = spec.getSpecificationData(args[0])
        def errors = []
        def restClient = new RESTClient("http://neueda.jelastic.dogado.eu/calculator")
        println 'Processing ${data.name}'

        data.tests.each { test ->
            println 'Processing ${test.name} tests'

            def method
            switch (test.request.method.toLowerCase()) {
                case 'post': method = Method.POST
                    break
                default:
                    throw new IllegalArgumentException('Method ${test.request.method.toLowerCase()} is not supported')
            }
            def path = test.request.path

            test.testCases.each { testCase ->
                println 'Running test case ${testCase.name}'
                restClient.request(method, ContentType.JSON) { request ->
                    uri.path = path
                    body = [testCase.variables[]]

                }
            }
        }

    }
}
