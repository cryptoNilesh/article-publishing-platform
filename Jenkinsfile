
pipeline {
    agent any

    environment {
    PATH = "/Users/mac/.nvm/versions/node/v26.13.0/bin:/opt/homebrew/bin:${env.PATH}"
}

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out Article Publishing Platform'
                checkout scm
            }
        }

        stage('Backend Build & Test') {
            steps {
                dir('article_platform') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean test package'
                }
            }
        }

        stage('Frontend Install & Build') {
            steps {
                dir('frontend') {
                    sh 'npm ci'
                    sh 'npm run build'
                }
            }
        }

        stage('Build Successful') {
            steps {
                echo 'Backend and Frontend builds completed successfully!'
            }
        }
    }

    post {
        success {
            echo 'CI Pipeline: SUCCESS'
        }

        failure {
            echo 'CI Pipeline: FAILED'
        }
    }
}
