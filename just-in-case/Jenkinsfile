pipeline {
    agent any
    tools{
        maven 'Maven 3.9.9'
    }
    stages {
        stage('Checkout') {
            steps {
                // Check out the code from Git
                git url: 'https://github.com/Yohannes09/Tenmo'
            }
        }
        stage('Build') {
            steps {
                // Build the project using Maven
                sh 'mvn clean install'
            }
        }
        stage('Test') {
            steps {
                // Run tests
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                // Deploy the application (example placeholder)
                echo 'Deploying application...'
            }
        }
    }
    post {
        always {
            // Actions that should always run, e.g., cleanup
            echo 'Cleaning up...'
        }
    }
}