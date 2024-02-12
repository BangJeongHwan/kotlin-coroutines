package com.brett.kotlincoroutines.chapter

import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1.0/user")
class UserCoroutineController(
    private val userCoroutineService: UserCoroutineService,
) {
    @GetMapping("", "/")
    suspend fun findUser(): UserDto {
        return UserDto.from(userCoroutineService.findUser())
    }
}

@Service
class UserCoroutineService() {
    fun findUser(): User {
        return User(
            name = "brett",
            age = 33,
        )
    }
}

data class User(
    val name: String,
    val age: Int,
)

data class UserDto(
    val name: String,
    val age: Int,
) {
    companion object {
        fun from(user: User): UserDto =
            UserDto(
                name = user.name,
                age = user.age,
            )
    }
}
