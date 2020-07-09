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
                configFileProvider([configFile(fileId: 'maven-global-settings', variable: 'MAVEN_GLOBAL_SETTINGS')]){
                    sh 'mvn -s ${MAVEN_GLOBAL_SETTINGS} -B -DskipTests clean package'
                }
           }
        }

        stage('Build for cicd2') {
            when {
                branch 'cicd2'
            }
            steps {
                configFileProvider([configFile(fileId: 'maven-global-settings', variable: 'MAVEN_GLOBAL_SETTINGS')]){
                    sh 'mvn -s ${MAVEN_GLOBAL_SETTINGS} -B -DskipTests clean package'
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
    }


    options {
        gitLabConnection('GITLAB')
    }

}