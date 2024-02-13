package com.brett.kotlincoroutines.chapter

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlin.random.Random

class MainNine {
    companion object {
        suspend fun cancel() {
            return coroutineScope {
                val job =
                    launch {
                        repeat(1_000) { i ->
                            delay(200)
                            println("Printing $i")
                        }
                    }

                delay(1100)
                job.cancel()
                job.join()
                println("Cancelled successfully")
            }
        }

        suspend fun cancelAfterNotAddJoin() {
            return coroutineScope {
                val job =
                    launch {
                        repeat(1_000) { i ->
                            delay(100)
                            Thread.sleep(100) // 오래 걸리는 연산이라 가정
                            println("Printing $i")
                        }
                    }

                delay(1000)
                job.cancel()
                println("Cancelled successfully")
            }
        }

        suspend fun cancelAfterAddJoin() {
            return coroutineScope {
                val job =
                    launch {
                        repeat(1_000) { i ->
                            delay(100)
                            Thread.sleep(100) // 오래 걸리는 연산이라 가정
                            println("Printing $i")
                        }
                    }

                delay(1000)
                job.cancel()
                job.join()
                println("Cancelled successfully")
            }
        }

        suspend fun cancelAndJoin() {
            return coroutineScope {
                val job =
                    launch {
                        repeat(1_000) { i ->
                            delay(200)
//                        Thread.sleep(100)   // 오래 걸리는 연산이라 가정
                            println("Printing $i")
                        }
                    }

                delay(1100)
                job.cancelAndJoin()
                println("Cancelled successfully")
            }
        }

        suspend fun cancellationException() {
            return coroutineScope {
                val job = Job()
                launch(job) {
                    try {
                        repeat(1_000) { i ->
                            delay(200)
                            println("Printing $i")
                        }
                    } catch (e: CancellationException) {
                        println(e)
                        throw e
                    }
                }

                delay(1100)
                job.cancelAndJoin()
                println("Cancelled successfully")
                delay(1000)
            }
        }

        suspend fun cancellationExceptionFinally() {
            return coroutineScope {
                val job = Job()
                launch(job) {
                    try {
                        delay(Random.nextLong(2000))
                        println("Done")
                    } finally {
                        println("Will always be printed")
                    }
                }

                delay(1000)
                job.cancelAndJoin()
            }
        }

        suspend fun cancellationExceptionFinallyInAnotherAction() {
            return coroutineScope {
                val job = Job()
                launch(job) {
                    try {
                        delay(2000)
                        println("Job is Done")
                    } finally {
                        println("Finally")
                        launch {
                            println("Will not be printed")
                        }
                        delay(1000) // 여기서 예외가 발생됨
                        println("Will not be printed")
                    }
                }

                delay(1000)
                job.cancelAndJoin()
                println("Cancel done")
            }
        }

        suspend fun withContextNonCancellable() {
            return coroutineScope {
                val job = Job()
                launch(job) {
                    try {
                        delay(200)
                        println("Coroutine finished")
                    } finally {
                        println("Finally")
                        withContext(NonCancellable) {
                            delay(1000)
                            println("Cleanup done")
                        }
                    }
                }

                delay(100)
                job.cancelAndJoin()
                println("Done")
            }
        }

        suspend fun invokeOnCompletion() {
            return coroutineScope {
                val job =
                    launch {
                        delay(1000)
                    }
                job.invokeOnCompletion { exception: Throwable? ->
                    println("Finished")
                }
                delay(400)
                job.cancelAndJoin()
            }
        }

        suspend fun invokeOnCompletionHandling() {
            return coroutineScope {
                val job =
                    launch {
                        val num = Random.nextLong(2400)
                        println("delay num : $num")
                        delay(num)
                        println("Finished")
                    }
                delay(800)
                job.invokeOnCompletion { exception: Throwable? ->
                    println("Will always be printed")
                    println("The exception was: $exception")
                }
                delay(800)
                job.cancelAndJoin()
            }
        }

        suspend fun dontStopForceStop() {
            return coroutineScope {
                val job = Job()
                launch(job) {
                    repeat(1_000) { i ->
                        Thread.sleep(200)
                        println("Printing $i")
                    }
                }
                delay(1000)
                job.cancelAndJoin()
                println("Cancelled successfully")
                delay(1000)
            }
        }

        suspend fun yieldAction() {
            return coroutineScope {
                val job = Job()
                launch(job) {
                    repeat(1_000) { i ->
                        Thread.sleep(200)
                        yield()
                        println("Printing $i")
                    }
                }
                delay(1100)
                job.cancelAndJoin()
                println("Cancelled successfully")
                delay(1000)
            }
        }

        suspend fun jobStatusIsActive() {
            return coroutineScope {
                val job = Job()
                launch(job) {
                    do {
                        Thread.sleep(200)
                        println("Printing")
                    } while (isActive)
                }
                delay(1100)
                job.cancelAndJoin()
                println("Cancelled successfully")
            }
        }

        suspend fun jobStatusEnsureActive() {
            return coroutineScope {
                val job = Job()
                launch(job) {
                    repeat(1_000) { i ->
                        Thread.sleep(200)
                        ensureActive()
                        println("Printing $i")
                    }
                }
                delay(1100)
                job.cancelAndJoin()
                println("Cancelled successfully")
            }
        }

//        suspend fun someTask() = suspendCancellableCoroutine { cont ->
//            cont.invokeOnCancellation {
//                // 정리 작업을 수행
//            }
//            // 나머지 구현 부분
//        }
    }
}

suspend fun main() {
//    MainNine.cancel()
//    MainNine.cancelAfterNotAddJoin()
//    MainNine.cancelAfterAddJoin()
//    MainNine.cancelAndJoin()

//    MainNine.cancellationException()
//    MainNine.cancellationExceptionFinally()

//    MainNine.cancellationExceptionFinallyInAnotherAction()
//    MainNine.withContextNonCancellable()
//    MainNine.invokeOnCompletion()
//    MainNine.invokeOnCompletionHandling()

//    MainNine.dontStopForceStop()
//    MainNine.yieldAction()

//    MainNine.jobStatusIsActive()
    MainNine.jobStatusEnsureActive()
}
