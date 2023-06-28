pipeline {
    agent any

    stages {
        stage('git clone') {
            steps {
                git branch: 'dev', credentialsId: 'git_cred', url: 'https://github.com/new2run/member_springboot.git'
            }
        }
    }
}
