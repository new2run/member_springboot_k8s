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
                                            execCommand: 'sh /home/springbootdemo/SpringBootDemo/deploy/start_service.sh', 
                                            execTimeout: 120000, 
                                            flatten: false, 
                                            makeEmptyDirs: false, 
                                            noDefaultExcludes: false, 
                                            patternSeparator: '[, ]+', 
                                            //SSH 서버의 디폴트 경로가 /home/springbootdemo로 등록되어 있으므로,
                                            //배포 경로는 /home/springbootdemo/SpringBootDemo/deploy 가 됨
                                            remoteDirectory: '/SpringBootDemo/deploy',
                                            remoteDirectorySDF: false, 
                                            removePrefix: 'build/libs', 
                                            //젠킨스 VM의 기본경로 /var/lib/jenkins/workspace/<파이프라인명>/build/libs 경로에 생성된 jar 파일을 WAS로 배포함
                                            sourceFiles: 'build/libs/*.jar')],
                                            usePromotionTimestamp: false, 
                                            useWorkspaceInPromotion: false, 
                                            verbose: true
                            )
                        ]
                )
                echo '********** Publish complete**********'
            }
        }
        //SonarQube 소스 검사
        stage('SonarQube - Code Analysis'){
            steps {
                withSonarQubeEnv('SonarServer') {
                    sh '''
                        ./gradlew sonar \
                          -Dsonar.projectKey=jenkins-sonarqube \
                          -Dsonar.host.url=http://192.168.216.200:9000 \
                          -Dsonar.login=sonar_token
                    '''
                    echo 'Code Analysis Success'
                }
            }
        }
    }
}
