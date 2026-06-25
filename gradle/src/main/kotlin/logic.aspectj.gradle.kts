import logic.tasks.AspectJCompiledJar

configurations {
    register("aspectj")
    register("aspect")
}

tasks.register<AspectJCompiledJar>("aspectJCompiledJar")
