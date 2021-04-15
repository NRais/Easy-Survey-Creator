
# command line : java -jar C:\Users\Nuser\Documents\NetBeansProjects\proguard.jar @ C:\Users\Nuser\Documents\NetBeansProjects\EasySurveyCreator\EasySurveyCreator.pro
# package only complete packaged for store jar files with all the libraries included
# NOTE: it takes a bit so be patient

-injars       C:\Users\Nuser\Documents\NetBeansProjects\EasySurveyCreator\store\EasySurveyCreator.jar
-outjars      C:\Users\Nuser\Documents\NetBeansProjects\EasySurveyCreator\store\EasySurveyCreator_OUT.jar
-libraryjars  <java.home>/lib/rt.jar 
-optimizationpasses 3 
-overloadaggressively 
-repackageclasses '' 
-allowaccessmodification 
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keep class org.jsoup.**
-keep class org.apache.**
-keep class org.jfree.**
-dontwarn org.**



-keep public class library.MainStartup { 
      public static void main(java.lang.String[]); 
}