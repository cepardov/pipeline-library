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
                        sh 'echo $JOB_NAME'
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
    println('origin is ' + origin)
    String[] originSplited = origin.split('/') as String[]
    int splitNumber = originSplited.length
    println("number: " + splitNumber)

    if (splitNumber == 3) {
        println('brach recognised')
        return originSplited[2]
    } else {
        println('brach not recognised')
        return 'develop'
    }
}