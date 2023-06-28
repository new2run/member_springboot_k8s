pipeline {
    agent any

    stages {
        //git 소스 clone
        stage('git clone') {
            steps {
                // github repository 'dev' branch의 소스들을 git clone 수행
                // clone한 소스들은 jenkins 서버의 "/var/lib/jenkins/workspace/<Pipeline 명> 경로에 생성
                git branch: 'dev', credentialsId: 'git_cred', url: 'https://github.com/new2run/member_springboot.git'
                echo '**********git clone complete **********'
                
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

        //빌드된 jar 파일을 WAS 서버로 ssh 전송
        stage('Publish on SSH - WAS'){
            steps {
                sshPublisher(
                    publishers: 
                        [
                            sshPublisherDesc(
                                configName: 'VM_WAS', 
                                transfers: 
                                    [
                                        sshTransfer(
                                            cleanRemote: false, 
                                            excludes: '', 
                                            execCommand: 'echo \'ssh complete\'', 
                                            execTimeout: 120000, 
                                            flatten: false, 
                                            makeEmptyDirs: false, 
                                            noDefaultExcludes: false, 
                                            patternSeparator: '[, ]+', 
                                            remoteDirectory: '/SpringBootDemo/deploy', 
                                            remoteDirectorySDF: false, 
                                            removePrefix: 'build/libs', 
                                            sourceFiles: 'build/libs/*.jar')], 
                                            usePromotionTimestamp: false, 
                                            useWorkspaceInPromotion: false, 
                                            verbose: true
                            )
                        ]
                )
            }
        }
    }
}
