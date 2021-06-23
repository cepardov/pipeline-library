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
                        sh 'echo $BRANCH'
                        sh 'echo $PROJECT_NAME'
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
def getProjectName() {
    def originalJob = System.getenv("JOB_NAME")
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
    def originalJob = System.getenv("JOB_NAME")
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