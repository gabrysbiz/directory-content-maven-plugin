properties([[
    $class: 'BuildDiscarderProperty',
    strategy: [
        $class: 'LogRotator',
        artifactDaysToKeepStr: '-1',
        artifactNumToKeepStr: '10',
        daysToKeepStr: '-1',
        numToKeepStr: '10'
    ]
]])

node {
    timestamps {
        stage('Pre Build Cleanup') {
            cleanWs()
        }
        stage('Checkout') {
            checkout scm
        }

        stage('Build') {
            withMaven(maven: 'MVN-3', jdk: 'JDK-9', mavenLocalRepo: '.repository', options: [junitPublisher(disabled: true), openTasksPublisher(disabled: true)]) {
                sh 'mvn -e package -DskipTests'
            }
        }
        stage('Verify') {
            withMaven(maven: 'MVN-3', jdk: 'JDK-9', mavenLocalRepo: '.repository', options: [artifactsPublisher(disabled: true), dependenciesFingerprintPublisher(disabled: true)]) {
                sh 'mvn -e verify'
            }
        }
        stage('Build Docs') {
            withMaven(maven: 'MVN-3', jdk: 'JDK-9', mavenLocalRepo: '.repository', options: [artifactsPublisher(disabled: true), dependenciesFingerprintPublisher(disabled: true), junitPublisher(disabled: true), openTasksPublisher(disabled: true)]) {
                sh 'mvn -e site'
            }
        }

        stage('Post Build Cleanup') {
            cleanWs()
        }
    }
}
