@Library('my-jenkins-libs') _
pipeline {
    agent none


    stages {
        stage('Build for dev') {
            when {
                branch 'cicd'
            }
            
            agent {
                docker {
                    image 'maven:3-alpine'
                    args '-v /home/sa/.m2:/root/.m2'
                }
            }
            
            steps {
                zMvn("clean")
           }
        }

        stage('Deploy to test') {
            when {
                branch 'cicd'
            }
            
            agent {label 'master'}
            
            steps {
                script {
                    def remote = [:]
                    remote.name = 'gnode2'
                    remote.host = 'api1.vchat.club'
                    remote.allowAnyHosts = true
                    withCredentials([sshUserPrivateKey(
                        keyFileVariable:"key",
                        credentialsId:"gnode-key",
                        usernameVariable:"userName")]) {
                            remote.user = userName
                            remote.identityFile = key
                            sshCommand remote: remote, command: "pwd"
                            sshPut remote: remote, from: 'target/single-bridge-1.7.0-SNAPSHOT.jar', into: '.'
                        }
                }
            }
        }
    }
}