package com.brett.kotlincoroutines.chapter

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainEight {
    companion object {
        suspend fun jobCoroutineScope() {
            return coroutineScope {
                // 빌더로 생성된 잡은
                val job = Job()
                println(job) // Active
                // 메서드로 완료시킬 때까지 Active 상태입니다.
                job.complete()
                println(job) // Completed

                // launch는 기본적으로 활설화되어 있음
                val activeJob =
                    launch {
                        delay(1000)
                    }
                println(activeJob) // Active
                // 여기서 job이 완료될 때까지 기다림
                activeJob.join()
                println(activeJob) // Completed

                // launch는 New 상태로 지연 시작됩니다.
                val lazyJob =
                    launch(start = CoroutineStart.LAZY) {
                        delay(1000)
                    }
                println(lazyJob) // New
                lazyJob.start()
                println(lazyJob) // Active
                lazyJob.join()
                println(lazyJob) // Completed
            }
        }

        fun parentChildCoroutineContext() {
            return runBlocking {
                val name = CoroutineName("Some name")
                val job = Job()

                launch(name + job) {
                    val childName = coroutineContext[CoroutineName]
                    println("childName >> $childName")
                    println("name >> $name")
                    println(childName == name) // false
                    val childJob = coroutineContext[Job]
                    println("childJob >> $childJob")
                    println("job >> $job")
                    println("job.children.first() >> ${job.children.first()}")
                    println(childJob == job) // false
                    println(childJob == job.children.first()) // true
                }
            }
        }

        fun notThingActionParentCoroutineScope() {
            return runBlocking {
                launch(Job()) { // 새로운 잡이 부모로부터 상속받은 잡을 대체
                    delay(1000)
                    println("Will not be printed")
                }
            }
            // 아무것도 출력하지 않고, 즉시 종료
        }

        fun joinHoldingCoroutineScope() {
            return runBlocking {
                val job1 =
                    launch {
                        delay(1000)
                        println("Test1")
                    }
                val job2 =
                    launch {
                        delay(2000)
                        println("Test2")
                    }

                job1.join()
                job2.join()
                println("All tests are done")
            }
        }

        suspend fun foreverWaitingCoroutineScope() {
            return coroutineScope {
                val job = Job()
                launch(job) {
                    delay(1000)
                    println("Text 1")
                }
                launch(job) {
                    delay(2000)
                    println("Text 2")
                }
                job.join()
                println("Will not be printed")
            }
        }

        suspend fun endingCoroutineScope() {
            return coroutineScope {
                val job = Job()
                launch(job) {
                    delay(1000)
                    println("Text 1")
                }
                launch(job) {
                    delay(2000)
                    println("Text 2")
                }
                job.children.forEach { it.join() }
                println("Will be printed")
            }
        }

        fun jobCompleteCoroutineScope() {
            return runBlocking {
                val job = Job()
                launch(job) {
                    repeat(5) { num ->
                        delay(1000)
                        println("Rep$num")
                    }
                }

                launch {
                    delay(10000)
                    job.complete()
                }

                job.join()

                launch(job) {
                    println("Will not be printed")
                }

                println("Done")
            }
        }

        fun jobCompleteExceptionallyCoroutineScope() {
            return runBlocking {
                val job = Job()

                launch(job) {
                    repeat(5) { num ->
                        delay(1000)
                        println("Rep$num")
                    }
                }

                launch {
                    delay(3000)
                    job.completeExceptionally(Error("Some error"))
                }

                job.join()

                launch(job) {
                    println("Will not be printed")
                }

                println("Done")
            }
        }
    }
}

suspend fun main() {
//    MainEight.jobCoroutineScope()
//    MainEight.parentChildCoroutineContext()
//    MainEight.notThingActionParentCoroutineScope()
//    MainEight.joinHoldingCoroutineScope()
//    MainEight.foreverWaitingCoroutineScope()
//     MainEight.endingCoroutineScope()
//    MainEight.jobCompleteCoroutineScope()
    MainEight.jobCompleteExceptionallyCoroutineScope()
}
