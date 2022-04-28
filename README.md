# KtorBugWithMonitoring

This repo illustrates a regression we noticed as of Ktors 2.0.0.
This was working with Ktors 2.0.0-beta1.

The gist of the bug is that we add an MDC context for the JWTPrincipal and that somehow causes the application call parameters to be reset during Authentication.

In `Monitoring.kt` we have:
```kotlin
        // BUG: Calling principal some
        mdc("identityId") {
            it.principal<JWTPrincipal>()?.subject ?: "No subject"
        }
```

In `Security.kt` we have:
```kotlin
val teamId = this.parameters["teamId"]
if (teamId == null) {
    println("TeamID is unexpectedly null!")
    throw Exception("TeamID is null when it should't be")
}
```

We are finding that the parameters list is empty if and only if the call to application.principal is made from the Monitoring plugin.

We have a test illustrating this in `ApplicationTest.kt`.
```kotlin
    @Test
    fun `With monitoring authentication fails when it should not fail`() = testApplication {
        application {
            configureSecurity()
            configureRouting()
            configureMonitoring()
        }
        val client = this.createCustomClient()

        client.get("/teams/123e4567-e89b-12d3-a456-556642440000").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
```

If we remove the `configureMonitoring` call, the test passes.
