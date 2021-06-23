def call(String tipo) {
    if (tipo == "microservicio") {
        pipeline {
            agent any
            stages {
                stage("Configuring pipeline") {
                    steps {
                        branchName = sh(label: 'getBranchName', returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim()
                        println branchName
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