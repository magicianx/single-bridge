pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
            args '-v /home/sa/.m2:/root/.m2'
        }
    }

    triggers {
        gitlab(triggerOnPush: true, triggerOnMergeRequest: true, branchFilterType: 'All',
        secretToken: "${env.GITLAB_TRIGGER_TOKEN}")
    }

    stages {
        stage('Build for dev') {
            when {
                branch 'cicd'
            }
            steps {
                zMvn()
                echo "${env.GIT_COMMIT}"
           }
        }

        stage('Build for cicd2') {
            when {
                branch 'cicd2'
            }
            steps {
                zMvn()
            }
        }

        stage('Deploy to test') {
            agent none
            when {
                branch 'cicd'
            }
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
                        }
                    sshCommand remote: remote, command: "which ssh"
                    
                }
            }
        }
    }

    post {
        failure {
            updateGitlabCommitStatus name: 'build', state: 'failed'
        }

        success {
            updateGitlabCommitStatus name: 'build', state: 'success'
        }

        always {
            cleanWs()
        }
    }


    options {
        gitLabConnection('GITLAB')
    }
}

def zMvn() {
    configFileProvider([configFile(fileId: 'maven-global-settings', variable: 'MAVEN_GLOBAL_SETTINGS')]){
        sh 'mvn -s ${MAVEN_GLOBAL_SETTINGS} -B -DskipTests clean package'
    }
}