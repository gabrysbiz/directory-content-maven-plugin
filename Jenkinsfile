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
           step($class: 'WsCleanup')
        }
        stage('Checkout') {
            checkout scm
        }
        stage('Build') {
            withMaven(maven: 'MVN-3', jdk: 'JDK-9', mavenLocalRepo: '.repository', options: [junitPublisher(disabled: true)]) {
                sh 'mvn -e install site -DskipTests'
            }
        }
        stage('Test') {
            withMaven(maven: 'MVN-3', jdk: 'JDK-9', mavenLocalRepo: '.repository', options: [openTasksPublisher(disabled: true), dependenciesFingerprintPublisher(disabled: true)]) {
                sh 'mvn -e test'
            }
        }
        stage('Post Build Cleanup') {
           step($class: 'WsCleanup')
        }
    }
}
