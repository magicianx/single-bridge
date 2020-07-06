Pipeline {
    agent {
        docker {
            image 'maven：3-alpine'
            args '-v /home/sa/.m2:/root/.m2'
        }
    }

    stages {
        stage('Build for dev') {
            when {
                branch 'cicd'
            }
            steps {
                sh 'echo a'
                sh 'mvn -B -DskipTests clean package'
           }
        }


    }
}