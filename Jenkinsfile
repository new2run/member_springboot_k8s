pipeline {
    agent any

    stages {
        //git 소스 clone
        stage('git clone') {
            try {
                // github repository 'dev' branch의 소스들을 git clone 수행
                // clone한 소스들은 jenkins 서버의 "/var/lib/jenkins/workspace/<Pipeline 명> 경로에 생성
                git branch: 'dev', credentialsId: 'git_cred', url: 'https://github.com/new2run/member_springboot.git'
                echo '********************************git clone complete ********************************************'
            }
            catch (exc) {
                echo 'git clone fail !!'
            }
        }

        //git clone하여 생성된 JAVA 소스 빌드
        stage('springboot build'){
            steps {
                sh '''
                    echo 'start bootjar'
                    ./gradlew clean bootJar
                '''
            }
        }
    }
}
