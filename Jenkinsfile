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
                    remote = zGetRemote("gnode2")
                    echo remote.name
                }
            }
        }
    }
}
