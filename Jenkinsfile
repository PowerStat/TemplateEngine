pipeline
 {
  agent none

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


  stages
   {
    stage('Clean windows')
     {
      agent 
       { 
        label 'windows'
       }
      steps
       {
        bat 'mvn --batch-mode clean'
       }
     }

    stage('Clean linux')
     {
      agent 
       { 
        label 'linux'
       }
      steps
       {
        sh 'mvn --batch-mode clean'
       }
     }

    stage('Build windows')
     {
      agent 
       { 
        label 'windows'
       }
      steps
       {
        bat 'mvn --batch-mode compile'
       }
     }

    stage('Build linux')
     {
      agent 
       { 
        label 'linux'
       }
      steps
       {
        sh 'mvn --batch-mode compile'
       }
     }

    stage('UnitTests windows')
     {
      agent 
       { 
        label 'windows'
       }
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

    stage('UnitTests linux')
     {
      agent 
       { 
        label 'linux'
       }
      steps
       {
        sh 'mvn --batch-mode resources:testResources compiler:testCompile surefire:test'
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
    stage('MutationTests windows')
     {
      agent 
       { 
        label 'windows'
       }
      steps
       {
        bat 'mvn --batch-mode org.pitest:pitest-maven:mutationCoverage'
       }
     }

    stage('MutationTests linux')
     {
      agent 
       { 
        label 'linux'
       }
      steps
       {
        sh 'mvn --batch-mode org.pitest:pitest-maven:mutationCoverage'
       }
     }
*/

    stage('Sanity check windows')
     {
      agent 
       { 
        label 'windows'
       }
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
      /*
      post
       {
        always
         {
          // recordIssues enabledForFailure: true, tools: [mavenConsole(), java(), javaDoc(), checkStyle(), spotBugs(), cpd(pattern: '** /target/cpd.xml'), pmdParser(pattern: '** /target/pmd.xml')]
         }
       }
      */
     }

    stage('Sanity check linux')
     {
      agent 
       { 
        label 'linux'
       }
      steps
       {
        sh 'mvn --batch-mode checkstyle:checkstyle pmd:pmd pmd:cpd com.github.spotbugs:spotbugs-maven-plugin:spotbugs'
       }
     }

    stage('Packaging windows')
     {
      agent 
       { 
        label 'windows'
       }
      steps
       {
        bat 'mvn --batch-mode jar:jar'
       }
     }

    stage('Packaging linux')
     {
      agent 
       { 
        label 'linux'
       }
      steps
       {
        sh 'mvn --batch-mode jar:jar'
       }
     }

    stage('install local windows')
     {
      agent 
       { 
        label 'windows'
       }
      steps
       {
        bat 'mvn --batch-mode jar:jar install:install' // maven-jar-plugin falseCreation default is false, so no doubled jar construction here, but required for maven-install-plugin internal data
       }
     }

    stage('Install local linux')
     {
      agent 
       { 
        label 'linux'
       }
      steps
       {
        sh 'mvn --batch-mode jar:jar install:install' // maven-jar-plugin falseCreation default is false, so no doubled jar construction here, but required for maven-install-plugin internal data
       }
     }
      
    stage('Integration tests windows')
     {
      agent 
       { 
        label 'windows'
       }
      steps
       {
        bat 'mvn --batch-mode failsafe:integration-test failsafe:verify'
       }
     }

    stage('Integration tests linux')
     {
      agent 
       { 
        label 'linux'
       }
      steps
       {
        sh 'mvn --batch-mode failsafe:integration-test failsafe:verify'
       }
     }

    stage('Documentation windows')
     {
      agent 
       { 
        label 'windows'
       }
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

    stage('Documentation linux')
     {
      agent 
       { 
        label 'linux'
       }
      steps
       {
        sh 'mvn --batch-mode -Dweb.server=www.powerstat.de site'
       }
      post
       {
        always
         {
          publishHTML(target: [reportName: 'Site', reportDir: 'target/site', reportFiles: 'index.html', keepAll: false])
         }
       }
     }

    // Security tests
    // Fault tolerance tests
    // Performance tests cucumber
    
    /*
    stage('Release windows')
     {
      agent 
       { 
        label 'windows'
       }
      steps
       {
        bat 'mvn --batch-mode release:clean'
        bat 'mvn --batch-mode release:prepare'
        bat 'mvn --batch-mode release:perform'
       }
     }
    */

    /*
    stage('Deliver windows')
     {
      agent 
       { 
        label 'windows'
       }
      steps
       {
        bat 'mvn --batch-mode deploy:deploy deploy-site'
       }
     }
    */

   }

 }
