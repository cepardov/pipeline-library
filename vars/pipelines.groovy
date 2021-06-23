def call(String tipo) {
    if (tipo == "microservicio") {
        pipeline {
            agent any
            environment {
                BRANCH = getBrachName()
            }
            stages {
                stage("Configuring pipeline") {
                    steps {
                        sh 'echo $BRANCH'
                    }
                }
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
                        sh 'pwd'
                        sh 'ls'
                    }
                }
                stage('Test') {
                    steps {
                        sh './gradlew test'
                    }
                }
                stage('Stopping Service') {
                    steps {
                        sh 'pwd'
                        sh 'ls'
                    }
                }
                stage('Create Directory') {
                    steps {
                        sh 'pwd'
                        sh 'cp -r deploy/product-dal/ /opt'
                    }
                }
                stage('Installing service') {
                    steps {
                        sh 'cp build/libs/service.jar /opt/product-dal/service/'
                        sh 'cd /opt/product-dal/service/'
                        sh 'ls'
                    }
                }
            }

            /*post {
                always {
                    deleteDir()
                }
            }*/
        }
    } else {
        pipeline {
            agent any
            stages {
                stage("Gradle Version") {
                    steps {
                        sh "./gradlew --version"
                    }
                }
            }
        }
    }
}

def getBrachName() {
    def origin = sh(returnStdout: true, script: 'git name-rev --name-only HEAD').trim()
    String[] originSplited = origin.split('/')
    int splitNumber = originSplited.length

    if (splitNumber == 2) {
        return originSplited[2]
    } else {
        return 'develop'
    }
}