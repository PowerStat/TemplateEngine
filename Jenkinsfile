pipeline
 {
  agent // any
   {
    label 'windows' // linux, docker
   }

  /*
  parameters
   {
    // string(name: 'WITH_CONFIG', defaultValue: 'localhost', description: 'Build with configuration for a specific host.')
    // booleanParam(name: 'DEBUG_BUILD', defaultValue: true, description: 'Build as debug or production version.')
    // text(name: 'DEPLOY_TEXT', defaultValue: 'One\nTwo\nThree\n', description: '')
    // choice(name: 'CHOICES', choices: ['one', 'two', 'three'], description: '')
    // file(name: 'FILE', description: 'Some file to upload')
    // password(name: 'PASSWORD', defaultValue: 'SECRET', description: 'A secret password')
   }
  */

  tools
   {
    maven 'Maven3'
    jdk 'JDK11'
   }

  environment
   {
    DISABLE_AUTH = 'true'
   }

  options
   {
    buildDiscarder(logRotator(numToKeepStr: '4'))
    skipStagesAfterUnstable()
    disableConcurrentBuilds()
    // timestamps()
    // timeout(time: 1, unit: 'HOURS')
    // parallelsAlwaysFailFast()
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
        bat 'mvn --batch-mode resources:testResources compiler:testCompile surefire:test'
        //  -Dmaven.test.failure.ignore=true
       }
      post
       {
        always
         {
          junit testResults: 'target/surefire-reports/*.xml'
         }
       }
     }
/*
    stage('MutationTests')
     {
      steps
       {
        bat 'mvn --batch-mode org.pitest:pitest-maven:mutationCoverage'
       }
     }
*/
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
          recordIssues enabledForFailure: true
          // tools: [mavenConsole(), java(), javaDoc(), checkStyle(), spotBugs(), cpd(pattern: '**/target/cpd.xml'), pmdParser(pattern: '**/target/pmd.xml')]
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
        bat 'mvn --batch-mode -Dweb.server=www.powerstat.de site'
        // Change history
       }
      post
       {
        always
         {
          publishHTML(target: [reportName: 'Site', reportDir: 'target/site', reportFiles: 'index.html', keepAll: false])
          // publishHTML(target: [reportName: 'Manual', reportDir: 'target/generated-docs', reportFiles: 'TemplateEngine.html', keepAll: false])
         }
       }
     }

    stage('install local')
     {
      steps
       {
        bat 'mvn --batch-mode install:install'
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
    stage('Release')
     {
      steps
       {
        bat 'mvn --batch-mode release:clean'
        bat 'mvn --batch-mode release:prepare'
        bat 'mvn --batch-mode release:perform'
       }
     }
    */

    /*
    stage('Deliver')
     {
      steps
       {
        bat 'mvn --batch-mode deploy:deploy deploy-site'
       }
     }
    */

   }

 }
