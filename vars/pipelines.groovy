def call(String tipo) {
    if (tipo == "microservicio") {
        pipeline {
            agent any
            stages {
                stage("Gradle Version") {
                    steps {
                        sh "./gradlew --version"
                    }
                    post{
                        failure {
                            script{
                                error "Failed, exiting now..."
                            }
                        }
                        aborted {
                            script{
                                error "aborted, exiting now..."
                            }
                        }
                        unstable {
                            script{
                                error "Unstable, exiting now..."
                            }
                        }
                    }
                }
                stage("Cleaning workspace") {
                    steps {
                        sh "./gradlew clean"
                    }
                    post{
                        failure {
                            script{
                                error "Failed, exiting now..."
                            }
                        }
                        aborted {
                            script{
                                error "aborted, exiting now..."
                            }
                        }
                        unstable {
                            script{
                                error "Unstable, exiting now..."
                            }
                        }
                    }
                }
                stage('Build') {
                    steps {
                        sh './gradlew build'
                        sh 'pwd'
                        sh 'ls'
                    }
                    post{
                        failure {
                            script{
                                error "Failed, exiting now..."
                            }
                        }
                        aborted {
                            script{
                                error "aborted, exiting now..."
                            }
                        }
                        unstable {
                            script{
                                error "Unstable, exiting now..."
                            }
                        }
                    }
                }
                stage('Test') {
                    steps {
                        sh './gradlew test'
                    }
                    post{
                        failure {
                            script{
                                error "Failed, exiting now..."
                            }
                        }
                        aborted {
                            script{
                                error "aborted, exiting now..."
                            }
                        }
                        unstable {
                            script{
                                error "Unstable, exiting now..."
                            }
                        }
                    }
                }
                stage('Stopping Service') {
                    steps {
                        sh 'pwd'
                        sh 'ls'
                    }
                    post{
                        failure {
                            script{
                                error "Failed, exiting now..."
                            }
                        }
                        aborted {
                            script{
                                error "aborted, exiting now..."
                            }
                        }
                        unstable {
                            script{
                                error "Unstable, exiting now..."
                            }
                        }
                    }
                }
                stage('Create Directory') {
                    steps {
                        sh 'cd deploy/'
                        sh 'cp -r product-dal/ /opt'
                        sh 'cd ..'
                    }
                    post{
                        failure {
                            script{
                                error "Failed, exiting now..."
                            }
                        }
                        aborted {
                            script{
                                error "aborted, exiting now..."
                            }
                        }
                        unstable {
                            script{
                                error "Unstable, exiting now..."
                            }
                        }
                    }
                }
                stage('Installing service') {
                    steps {
                        sh 'cp build/libs/service.jar /opt/product-dal/service/'
                        sh 'cd /opt/product-dal/service/'
                        sh 'ls'
                    }
                    post{
                        failure {
                            script{
                                error "Failed, exiting now..."
                            }
                        }
                        aborted {
                            script{
                                error "aborted, exiting now..."
                            }
                        }
                        unstable {
                            script{
                                error "Unstable, exiting now..."
                            }
                        }
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