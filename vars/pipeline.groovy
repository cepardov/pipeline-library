def call(String pipeline) {
    if (pipeline == "microservicio") {
        pipeline {
            agent any
            stages {
                stage("Gradle Version") {
                    steps {
                        sh "./gradlew --version"
                    }
                }
                stage("Cleaning workspace") {
                    steps {
                        sh "./gradlew clean"
                    }
                }
                stage('Build') {
                    steps {
                        sh './gradlew build'
                    }
                }
                stage('Test') {
                    steps {
                        sh './gradlew test'
                    }
                }
            }

            post {
                always {
                    deleteDir()
                }
            }
        }
    } else {
        pipeline {
            agent any
            stages {
                stage("Hello") {
                    steps {
                        sh "echo 'hello'"
                    }
                }
            }
        }
    }
}