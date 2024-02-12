package com.brett.kotlincoroutines.chapter

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext

suspend fun main() {
//    MainSeven.findCoroutineContext()
//    MainSeven.addCoroutineContext()
//    MainSeven.emptyCoroutineContext()
//    MainSeven.minusCoroutineContext()
//    MainSeven.foldCoroutineContext()
//    MainSeven.parentCoroutineContextAndBuilder()
//    MainSeven.childCoroutineContextAndBuilder()
//    MainSeven.outerInnerCoroutineContext()
    MainSeven.customCoroutineContext()
}

class MainSeven() {
    companion object {
        fun findCoroutineContext() {
            val ctx: CoroutineContext = CoroutineName("A name")
            val coroutineName = ctx[CoroutineName]
            println(coroutineName)
            val job = ctx[Job]
            println(job)
        }

        fun addCoroutineContext() {
            val ctx1 = CoroutineName("Name1")
            println(ctx1[CoroutineName]?.name) // Name1
            println(ctx1[Job]?.isActive) // null

            val ctx2 = Job()
            println(ctx2[CoroutineName]?.name) // null
            println(ctx2[Job]?.isActive) // 'Active' 상태으므로 true
            // 빌더를 통해 생성되는 잡의 기본 상태가 'Active' 상태으므로 true가 됨

            val ctx3 = ctx1 + ctx2
            println(ctx3[CoroutineName]?.name) // Name1
            println(ctx3[Job]?.isActive) // true
        }

        fun emptyCoroutineContext() {
            val empty = EmptyCoroutineContext
            println(empty[CoroutineName]) // null
            println(empty[Job]) // null

            val ctxName = empty + CoroutineName("Name1") + empty
            println(ctxName[CoroutineName])
        }

        fun minusCoroutineContext() {
            val ctx1 = CoroutineName("Name1") + Job()
            println(ctx1[CoroutineName]?.name) // Name1
            println(ctx1[Job]?.isActive) // true

            val ctx2 = ctx1.minusKey(CoroutineName)
            println(ctx2[CoroutineName]?.name) // null
            println(ctx2[Job]?.isActive) // true

            val ctx3 = (ctx1 + CoroutineName("Name2")).minusKey(CoroutineName)
            println(ctx3[CoroutineName]?.name) // null
            println(ctx3[Job]?.isActive) // true
        }

        fun foldCoroutineContext() {
            val ctx = CoroutineName("Name1") + Job()
            ctx.fold("") { acc, element -> "$acc$element" }
                .also(::println)

            val empty = emptyList<CoroutineContext>()
            ctx.fold(empty) { acc, element -> acc + element }
                .joinToString()
                .also(::println)
        }

        private fun CoroutineScope.log(msg: String) {
            val name = coroutineContext[CoroutineName]?.name
            println("[$name] $msg")
        }

        fun parentCoroutineContextAndBuilder() {
            return runBlocking(CoroutineName("main")) {
                log("Start") // [main] Start
                val v1 =
                    async {
                        delay(500)
                        log("Running async") // [main] Running async
                        42
                    }
                launch {
                    delay(1000)
                    log("Running launch") // [main] Running launch
                }
                log("The answer is ${v1.await()}") // [main] The answer is 42
            }
        }

        fun childCoroutineContextAndBuilder() {
            return runBlocking(CoroutineName("main")) {
                log("Start") // [main] Start
                val v1 =
                    async(CoroutineName("c1")) {
                        delay(500)
                        log("Running async") // [c1] Running async
                        42
                    }
                launch(CoroutineName("c2")) {
                    delay(1000)
                    log("Running launch") // [c2] Running launch
                }
                log("The answer is ${v1.await()}") // [main] The answer is 42
            }
        }

        private suspend fun printName() {
            println(coroutineContext[CoroutineName]?.name)
        }

        suspend fun outerInnerCoroutineContext() {
            return withContext(CoroutineName("Outer")) {
                printName() // Outer
                launch(CoroutineName("Inner")) {
                    printName() // Inner
                }
                delay(10)
                printName() // Outer
            }
        }

        private suspend fun printNext() {
            coroutineContext[CounterContext]?.printNext()
        }

        suspend fun customCoroutineContext() {
            return withContext(CounterContext("Outer")) {
                printNext() // Outer: 0
                launch {
                    printNext() // Outer: 1
                    launch {
                        printNext() // Outer: 2
                    }
                    launch(CounterContext("Inner")) {
                        printNext() // Inner: 0
                        printNext() // Inner: 1
                        launch {
                            printNext() // Inner: 2
                        }
                    }
                }
                printNext() // Outer: 3
            }
        }
    }
}

class CounterContext(
    private val name: String,
) : CoroutineContext.Element {
    override val key: CoroutineContext.Key<*> = Key
    private var nextNumber = 0

    fun printNext() {
        println("$name: $nextNumber")
        nextNumber++
    }

    companion object Key : CoroutineContext.Key<CounterContext>
}
