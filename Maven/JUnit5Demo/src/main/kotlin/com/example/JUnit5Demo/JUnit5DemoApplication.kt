package com.example.JUnit5Demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JUnit5DemoApplication

fun main(args: Array<String>) {
	runApplication<JUnit5DemoApplication>(*args)

	var obj = MathUtils()
	println(obj.computeCircleArea(2))
}
