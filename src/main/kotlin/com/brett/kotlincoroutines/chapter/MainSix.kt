package com.brett.kotlincoroutines.chapter

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
//    MainSix.launch()
//    MainSix.runBlocking()
//    MainSix.async()
//    MainSix.async1()
    MainSix.runBlockingToLaunch()
}

class MainSix() {
    companion object {
        @OptIn(DelicateCoroutinesApi::class)
        fun launch() {
            GlobalScope.launch {
                delay(1000L)
                println("World!")
            }
            GlobalScope.launch {
                delay(1000L)
                println("World!")
            }
            GlobalScope.launch {
                delay(1000L)
                println("World!")
            }
            println("Hello,")
            Thread.sleep(2000L)
        }

        fun runBlocking() {
            runBlocking {
                delay(1000L)
                println("World!")
            }
            runBlocking {
                delay(1000L)
                println("World!")
            }
            runBlocking {
                delay(1000L)
                println("World!")
            }
            println("Hello,")
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun async() {
            return runBlocking {
                val resultDeferred: Deferred<Int> =
                    GlobalScope.async {
                        delay(1000L)
                        42
                    }

                val result: Int = resultDeferred.await()
                println(result)
                println(resultDeferred.await())
            }
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun async1() {
            return runBlocking {
                val res1 =
                    GlobalScope.async {
                        delay(1000L)
                        "Text 1"
                    }
                val res2 =
                    GlobalScope.async {
                        delay(3000L)
                        "Text 2"
                    }
                val res3 =
                    GlobalScope.async {
                        delay(2000L)
                        "Text 3"
                    }
                println(res1.await())
                println(res2.await())
                println(res3.await())
            }
        }

        fun runBlockingToLaunch() {
            return runBlocking {
                this.launch {
                    delay(1000L)
                    println("World!")
                }
                launch {
                    delay(2000L)
                    println("World!")
                }
                println("Hello,")
            }
        }
    }
}
