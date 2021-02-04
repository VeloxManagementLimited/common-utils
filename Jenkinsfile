pipeline {
    agent any
    environment {
        boolean isStartedByUser = false
        boolean isStartedByParent = false
    }
    options {
        buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '3', numToKeepStr: '3')
        disableConcurrentBuilds()
        timestamps()
    }
    stages {

        stage ('Test') {
            steps {
                script {
                    isStartedByUser = currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause').size() > 0
                    isStartedByParent = currentBuild.getBuildCauses('hudson.model.Cause$UpstreamCause').size() > 0
                }
                sh """
                    echo \"isStartedByUser = $isStartedByUser\"
                    echo \"isStartedByParent = $isStartedByParent\"
                    
                    ./mvnw -U clean test
                """
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}