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
            jiraSendBuildInfo site: 'velotrade.atlassian.net'
            jiraSendDeploymentInfo site: 'velotrade.atlassian.net', environmentId: "jitpack.io", environmentName: 'jitpack.io', environmentType: 'production'
            cleanWs()
        }
        success {
            script {
                if (currentBuild.previousBuild && currentBuild.previousBuild.result != 'SUCCESS') {
                    office365ConnectorSend color: '#54a25a', status: 'SUCCESS', webhookUrl: 'https://outlook.office.com/webhook/a989057f-a666-4a7a-939e-0d69db8f5688@27681cd5-338b-44e5-b370-7711fd273f8f/JenkinsCI/a034352a836b46eea2e6a419a07fee6f/51fa9e31-bcef-4874-be94-5ad1d2b115fa'
                }
            }
        }
        failure {
            office365ConnectorSend color: '#ff5a57', status: 'FAILURE', webhookUrl: 'https://outlook.office.com/webhook/a989057f-a666-4a7a-939e-0d69db8f5688@27681cd5-338b-44e5-b370-7711fd273f8f/JenkinsCI/a034352a836b46eea2e6a419a07fee6f/51fa9e31-bcef-4874-be94-5ad1d2b115fa'
        }
    }
}