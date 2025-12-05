plugins {
    // 루트에서는 Android/Kotlin 플러그인을 절대 적용하지 않는다.
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

// Flutter가 자동 생성한 build 경로 설정 유지
val newBuildDir: Directory =
    rootProject.layout.buildDirectory
        .dir("../../build")
        .get()
rootProject.layout.buildDirectory.value(newBuildDir)

subprojects {
    val newSubprojectBuildDir: Directory = newBuildDir.dir(project.name)
    project.layout.buildDirectory.value(newSubprojectBuildDir)
}

subprojects {
    project.evaluationDependsOn(":app")
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
