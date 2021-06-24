#!/usr/bin/env groovy
def call(String tipo) {
    if (tipo == "microservicio") {
        pipeline {
            agent any
            environment {
                PROJECT_NAME = getProjectName()
                BRANCH = getBrachName()
            }
            stages {
                stage("Get data") {
                    environment {
                        PORT = getPort()
                    }
                    steps {
                        sh 'echo $PORT'
                    }
                }
                stage("Configuring pipeline") {
                    when {
                        expression { return verifiFile() }
                    }
                    input {
                        message "Numero de puerto"
                        submitterParameter PORT
                        parameters {
                            string(name: 'PORT_TMP', defaultValue: '8500', description: 'Puerto usado por este servicio')
                        }
                    }
                    steps {
                        sh 'echo $PORT_TMP > $BRANCH'
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
                        sh 'echo /opt/$PROJECT_NAME/$BRANCH'
                        sh 'sudo mkdir -p /opt/$PROJECT_NAME/$BRANCH'
                        sh 'sudo cp -r deploy/* /opt/$PROJECT_NAME/$BRANCH'
                    }
                }
                stage('Installing service') {
                    steps {
                        sh 'sudo cp build/libs/service.jar /opt/$PROJECT_NAME/$BRANCH/service/'
                    }
                }
                stage('Set Permissions') {
                    steps {
                        sh 'sudo chmod +x /opt/$PROJECT_NAME/$BRANCH/service/start.sh'
                        sh 'sudo chmod +x /opt/$PROJECT_NAME/$BRANCH/service/stop.sh'
                        sh 'sudo chmod +x /opt/$PROJECT_NAME/$BRANCH/startNodes.sh'
                        sh 'sudo chmod +x /opt/$PROJECT_NAME/$BRANCH/stopNodes.sh'
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
def getProjectName() {
    def originalJob = env.JOB_NAME
    println('original job: ' + originalJob)
    String[] originSplited = originalJob.split('/') as String[]
    int splitNumber = originSplited.length
    println("number: " + splitNumber)

    if (splitNumber == 3) {
        println('project name recognized')
        return originSplited[1]
    } else {
        println('project namet recognized')
        return 'undefined'
    }
}
def getBrachName() {
    def originalJob = env.JOB_NAME
    println('original job: ' + originalJob)
    String[] originSplited = originalJob.split('/') as String[]
    int splitNumber = originSplited.length
    println("number: " + splitNumber)

    if (splitNumber == 3) {
        println('brach recognized')
        return originSplited[2]
    } else {
        println('brach not recognized')
        return 'develop'
    }
}
def getPort() {
    try {
        def port = sh (returnStdout: true, script: 'cat $BRANCH').trim()
        return port
    } catch (Exception exc) {
        return "NA"
    }
}

def verifiFile() {
    try {
        sh (returnStdout: true, script: 'cat $BRANCH').trim()
        return false
    } catch (Exception exc) {
        return true
    }
}