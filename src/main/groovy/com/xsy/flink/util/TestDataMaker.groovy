package com.xsy.flink.util

class TestDataMaker {

    static Map<String, List<String>> getData() {
        def data = [:]
        new File("data").eachFile {
            data.put(it.name, it.readLines().findAll {
                it != ""
            })
        }
        return data
    }
}
