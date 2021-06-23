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
                stage("Configuring pipeline") {
                    steps {
                        def datas = readYaml (file: 'develop.yml')
                        sh 'echo $BRANCH'
                        sh 'echo $PROJECT_NAME'
                        sh 'echo ' + datas.version
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