pipeline
 {
  agent // any
   {
    label 'windows' // linux, docker
   }

  tools
   {
    maven 'Maven3'
    jdk 'JDK11'
   }

  options
   {
    buildDiscarder(logRotator(numToKeepStr: '4'))
    skipStagesAfterUnstable()
    disableConcurrentBuilds()
   }


  triggers
   {
    // MINUTE HOUR DOM MONTH DOW
    pollSCM('H 6-18/4 * * 1-5')
   }


  stages // windows, linux
   {
    stage('Clean')
     {
      steps
       {
        bat 'mvn --batch-mode clean'
       }
     }

    stage('Build')
     {
      steps
       {
        bat 'mvn --batch-mode compile'
       }
     }

    stage('UnitTests')
     {
      steps
       {
        bat 'mvn --batch-mode compiler:testCompile surefire:test -Dmaven.test.failure.ignore=true'
        // bat 'mvn --batch-mode clover:check -Dmaven.clover.targetPercentage=10%'
        // cobertura
        // jacoco
        // publishCoverage
       }
      post
       {
        always
         {
          junit testResults: 'target/surefire-reports/*.xml'
         }
       }
     }

    stage('MutationTests')
     {
      steps
       {
        bat 'mvn --batch-mode org.pitest:pitest-maven:mutationCoverage'
       }
     }

    stage('Sanity check')
     {
      steps
       {
        bat 'mvn --batch-mode checkstyle:checkstyle pmd:pmd pmd:cpd com.github.spotbugs:spotbugs-maven-plugin:spotbugs'
        // scanForIssues tools: [java(), javaDoc(), eclipse(), cssLint(), groovyScript(), jsLint()]
        // sonarQube
        // yuiCompressor
        // dependencyCheckAnalyzer
        // dependencyCheckPublisher
        // dependencyTrackPublisher
        // Arachni Scanner Plugin 
        // Black Duck Detect 
        /*
        codesonar
        withSonarQubeEnv('My SonarQube Server')
         {
          bat 'mvn --batch-mode sonar:sonar'
         }
        waitForQualityGate
        */
       }
      post
       {
        always
         {
          recordIssues enabledForFailure: true, tools: [mavenConsole(), java(), javaDoc(), checkStyle(), spotBugs(), cpd(pattern: '**/target/cpd.xml'), pmdParser(pattern: '**/target/pmd.xml')]
         }
       }
     }

    stage('Packaging')
     {
      steps
       {
        bat 'mvn --batch-mode jar:jar'
       }
     }

    stage('Documentation')
     {
      steps
       {
        bat 'mvn --batch-mode site'
        // Change history
        // docbook generate handbook web and pdf
       }
      post
       {
        always
         {
          publishHTML(target: [reportName: 'Site', reportDir: 'target/site', reportFiles: 'index.html', keepAll: false])
         }
       }
     }

    stage('install local')
     {
      steps
       {
        bat 'mvn --batch-mode source:jar install:install'
       }
     }
          
    stage('Integration tests')
     {
      steps
       {
        bat 'mvn --batch-mode failsafe:integration-test failsafe:verify'
       }
     }
     
    // Security tests
    // Fault tolerance tests
    // Performance tests cucumber
    
    /*
    stage('Build release')
     {
      steps
       {
        bat 'mvn --batch-mode release:clean'
        bat 'mvn --batch-mode release:prepare'
        bat 'mvn --batch-mode release:perform' // depoy, site-deploy included!
       }
     }
    */

    /*
    stage('Deliver')
     {
      / *
      when
       {
        allOf
         {
          environment name: 'DEBUG_BUILD', value: 'false'
          not {environment name: 'WITH_CONFIG', value: 'localhost'}
         }
       }
      * /

      steps
       {
        // to maven central
        bat 'mvn --batch-mode deploy:deploy'
       }
     }
    */

   }

  post
   {
    success
     {
      echo 'This will run only if successful'
      // publish to github releases
     }

    failure
     {
      echo 'This will run only if failed'
      // mail to: 'powerstat@github.com',  subject: "Failed Pipeline: ${currentBuild.fullDisplayName}", body: "Something is wrong with ${env.BUILD_URL}"
     }

    unstable
     {
      echo 'This will run only if the run was marked as unstable'
      // mail to: 'powerstat@github.com',  subject: "Failed Pipeline: ${currentBuild.fullDisplayName}", body: "Something is wrong with ${env.BUILD_URL}"
     }

   }

 }
