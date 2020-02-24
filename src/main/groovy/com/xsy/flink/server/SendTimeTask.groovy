package com.xsy.flink.server

import com.xsy.chat.server.util.ConnectionManager
import com.xsy.flink.util.TestDataMaker

class SendTimeTask implements Runnable {
    @Override
    void run() {
        def data = TestDataMaker.getData()
        def keyList = data.keySet() as List
        int i = 1
        while (true) {
            try {
                if (ConnectionManager.instance.allConnection.isEmpty()) {
                    sleep(60 * 1000)
                } else {
                    def filename = keyList.get(Math.random() * keyList.size() as Integer)
                    def fileSize = data.get(filename).size()
                    def value = data.get(filename).get(i % fileSize)
                    ConnectionManager.instance.sendToAll("${filename}||${value}\r\n" as String)
                    i++
                    sleep(10)
                }
                if (i > Integer.MAX_VALUE - 100000) {
                    i = 0
                }
            } catch (Exception e) {
                e.printStackTrace()
            }
        }
    }
}
