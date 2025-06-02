pipeline {
    agent any

    stages {
        stage('Clone') {
            steps {
                git url: 'https://github.com/Akin321/IronWeb.git', branch: 'main'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                sshagent (credentials: ['0cb6fa5e-b06b-46ee-af98-a518fdf5e187']) {
                    sh '''
                    # Copy JAR to EC2 instance
                    scp target/Iron-1.0.0.jar ubuntu@13.60.3.172:/home/ubuntu/Iron.jar
                    
                    # Kill any running Java app
                    ssh ubuntu@13.60.3.172 'pkill -f "java -jar" || true'
                    
                    # Run Spring Boot app in the background
                    ssh ubuntu@13.60.3.172 'nohup java -jar /home/ubuntu/Iron.jar > /home/ubuntu/Iron.log 2>&1 &'
                    '''
                }
            }
        }
    }
}
