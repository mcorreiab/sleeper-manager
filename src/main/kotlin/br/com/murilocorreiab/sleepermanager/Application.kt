package br.com.murilocorreiab.sleepermanager

import io.micronaut.runtime.Micronaut.*

fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.murilocorreia.sleepermanager")
		.start()
}

