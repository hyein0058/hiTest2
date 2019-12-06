pipeline {
    agent none
    stages {
        stage('Build') {
            agent any
            steps {
                echo "Build"
                sh 'chmod +x ./gradlew'
                sh('./gradlew build')
            }
        }

        stage('Build docker ps') {
            agent any
            steps {
                sh 'docker build -t gradlet2 .'
                sh 'docker run -d -p 8089:8089 gradlet2'
            }
        }
    }

    //마지막 어떻게 할껀지
    post {
        always {
            echo 'step fin!!!!'
            slackSend color: "good", message: "FIN STEP: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
        }
        success {
            echo 'build success!!!!!'
            slackSend color: "good", message: "SUCCESS BUILD: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
        }
        failure {
            echo 'build Fail!!!!!'
            slackSend color: "danger", message: "FAIL BUILD: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
            //mail to: team@gmail.com, subject: 'Pipeline fail email'
        }
    }
}