val domaVersion = "3.0.0"

fun updateVersion() {
    // Gradle
    ant.withGroovyBuilder {
        "replaceregexp"(
            "match" to """(["']org.seasar.doma:doma-(core|kotlin|processor|slf4j|template)?:)[^"']*(["'])""",
            "replace" to "\\1${domaVersion}\\3",
            "encoding" to "UTF-8",
            "flags" to "g"
        ) {
            "fileset"("dir" to ".") {
                "include"("name" to "docs/**/*.rst")
            }
        }
    }
    
    // Maven
    ant.withGroovyBuilder {
        "replaceregexp"(
            "match" to """(<doma.version>).*(</doma.version>)""",
            "replace" to "\\1${domaVersion}\\2",
            "encoding" to "UTF-8",
            "flags" to "g"
        ) {
            "fileset"("dir" to ".") {
                "include"("name" to "docs/**/*.rst")
            }
        }
    }
}

tasks {
    register("updateVersion") {
        doLast {
            updateVersion()
        }
    }
}
