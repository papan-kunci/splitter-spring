package com.nicolaslu.splitter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class SplitterApplication

fun main(args: Array<String>) {
	runApplication<SplitterApplication>(*args)
}
