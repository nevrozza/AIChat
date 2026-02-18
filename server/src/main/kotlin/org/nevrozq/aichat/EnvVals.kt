package org.nevrozq.aichat

object EnvVals {
    val sqlUrl = "jdbc:postgresql://db/" + System.getenv("POSTGRES_DB")
    val sqlUser = System.getenv("POSTGRES_USER")
    val sqlPassword = System.getenv("POSTGRES_PASSWORD")

    val serverPort = System.getenv("SERVER_PORT").toInt()
}