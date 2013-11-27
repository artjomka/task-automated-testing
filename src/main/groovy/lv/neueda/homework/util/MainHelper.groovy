package lv.neueda.homework.util

import groovyx.net.http.Method
import lv.neueda.homework.specification.Request

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
