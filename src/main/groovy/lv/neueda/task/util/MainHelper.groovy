package lv.neueda.task.util

import groovyx.net.http.Method
import lv.neueda.task.specification.Request
import lv.neueda.task.specification.TestSuite

class MainHelper {

     Method getHttpRequestMethod(Request request) {
        def method
        switch (request.method.toLowerCase()) {
            case 'post': method = Method.POST
                break
            default:
                throw new IllegalArgumentException("Method ${request.method.toLowerCase()} is not supported")
        }
        method
    }
}
